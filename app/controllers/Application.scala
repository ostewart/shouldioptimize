package controllers

import javax.inject.Inject

import com.shouldioptimize.model.{Ec2Pricing, PreTaxHourlyTimeCostModel}
import play.api.cache.Cached
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationResult}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

case class TimeCostInput(hourlyRate: BigDecimal, instanceType: String)

class Application @Inject()(val messagesApi: MessagesApi, cached: Cached) extends Controller with I18nSupport {
  val validInstanceType = Constraint[String] ({
    case it if Ec2Pricing.all.contains(it) => Valid
    case _ => Invalid("invalid instance type")
  }: PartialFunction[String,ValidationResult])

  val TimeCostInputForm = Form(
    mapping(
      "hourlyRate" -> bigDecimal,
      "instanceType" -> text.verifying(validInstanceType)
    )(TimeCostInput.apply)(TimeCostInput.unapply))

  private val DefaultHourlyRate = BigDecimal("42.88")
  private val DefaultInstanceType = "t2.large"
  private val DefaultForm = TimeCostInputForm.fill(TimeCostInput(BigDecimal("42.88"), DefaultInstanceType))

  def index = cached.status(_ => "/index", OK, 60) {
    Action {
      Ok(views.html.results(new PreTaxHourlyTimeCostModel(BigDecimal("42.88")).calculate.values, DefaultForm))
    }
  }

  def results = Action { request =>
    val form = TimeCostInputForm.bind(request.queryString.mapValues(_.head))
    form.fold(
      formWithErrors => BadRequest(views.html.results(new PreTaxHourlyTimeCostModel(DefaultHourlyRate).calculate.values, formWithErrors)),
      timeCostInput => Ok(views.html.results(new PreTaxHourlyTimeCostModel(timeCostInput.hourlyRate).calculate.values, form))
    )
  }

}

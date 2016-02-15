package controllers

import javax.inject.Inject

import com.shouldioptimize.model.{Ec2Pricing, TaxFreeHourlyTimeCostModel}
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.{ValidationResult, Constraint, Invalid, Valid}
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._

case class TimeCostInput(hourlyRate: BigDecimal, instanceType: String)

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val validInstanceType = Constraint[String] ({
    case it if Ec2Pricing.all.contains(it) => Valid
    case _ => Invalid("invalid instance type")
  }: PartialFunction[String,ValidationResult])

  val TimeCostInputForm = Form(
    mapping(
      "hourlyRate" -> bigDecimal,
      "instanceType" -> text.verifying(validInstanceType)
    )(TimeCostInput.apply)(TimeCostInput.unapply))

  private val DefaultInstanceType = "t2.large"
  private val DefaultForm = TimeCostInputForm.fill(TimeCostInput(BigDecimal("42.88"), DefaultInstanceType))

  def index = Action {
    Ok(views.html.results(new TaxFreeHourlyTimeCostModel(BigDecimal("42.88"), BigDecimal("33.964")).calculate.values, DefaultForm))
  }

  def results = Action { request =>
    val form = TimeCostInputForm.bind(request.queryString.mapValues(_.head))
    form.fold(
      formWithErrors => BadRequest(views.html.results(new TaxFreeHourlyTimeCostModel(BigDecimal("42.88"), BigDecimal("33.964")).calculate.values, formWithErrors)),
      timeCostInput => Ok(views.html.results(new TaxFreeHourlyTimeCostModel(timeCostInput.hourlyRate, BigDecimal("33.964")).calculate.values, form))
    )
  }

}

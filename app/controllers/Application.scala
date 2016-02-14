package controllers

import com.shouldioptimize.model.TaxFreeHourlyTimeCostModel
import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.results(new TaxFreeHourlyTimeCostModel(BigDecimal("100.00"), BigDecimal("33.964")).calculate.values))
  }

}

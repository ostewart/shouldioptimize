package com.whenshouldioptimize.model

case class TimeCostResult(instanceType: String, instanceHourlyCost: BigDecimal, hoursPerProgrammerHour: BigDecimal)

trait TimeCostModel {
  def calculate: Map[String,TimeCostResult]
}

class TaxFreeHourlyTimeCostModel(hourlyRate: BigDecimal, effectiveTaxRate: BigDecimal) extends TimeCostModel {
  val pricing = Ec2Pricing.all
  val hourlyRateAfterTax = (BigDecimal(100) - effectiveTaxRate) * hourlyRate
  def calculate: Map[String,TimeCostResult] = pricing.map {
    case (instanceType, instanceHourlyCost) => instanceType -> TimeCostResult(instanceType, instanceHourlyCost, hourlyRateAfterTax / instanceHourlyCost)
  }
}

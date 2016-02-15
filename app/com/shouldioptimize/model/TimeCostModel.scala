package com.shouldioptimize.model

case class TimeCostResult(instanceType: String, instanceHourlyCost: BigDecimal, hoursPerProgrammerHour: BigDecimal)

trait TimeCostModel {
  def calculate: Map[String,TimeCostResult]
}

class HourlyTimeCostModel(hourlyRate: BigDecimal, effectiveTaxRate: BigDecimal) extends TimeCostModel {
  val pricing = Ec2Pricing.all
  val hourlyRateAfterTax = (BigDecimal(100) - effectiveTaxRate) / BigDecimal(100) * hourlyRate
  def calculate: Map[String,TimeCostResult] = pricing.map {
    case (instanceType, instanceHourlyCost) => instanceType -> TimeCostResult(instanceType, instanceHourlyCost, hourlyRateAfterTax / instanceHourlyCost)
  }
}

class PreTaxHourlyTimeCostModel(hourlyRate: BigDecimal) extends TimeCostModel {
  val pricing = Ec2Pricing.all
  override def calculate: Map[String,TimeCostResult] = pricing.map {
    case (instanceType, instanceHourlyCost) => instanceType -> TimeCostResult(instanceType, instanceHourlyCost, hourlyRate / instanceHourlyCost)
  }

}
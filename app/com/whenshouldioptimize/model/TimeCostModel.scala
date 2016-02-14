package com.whenshouldioptimize.model

trait TimeCostModel {
  def calculate: Map[String,BigDecimal]
}

class TaxFreeHourlyTimeCostModel(hourlyRate: BigDecimal, effectiveTaxRate: BigDecimal) extends TimeCostModel {
  val pricing = Ec2Pricing.all
  val hourlyRateAfterTax = (BigDecimal(100) - effectiveTaxRate) * hourlyRate
  def calculate: Map[String,BigDecimal] = pricing.mapValues(hourlyRateAfterTax / _)
}

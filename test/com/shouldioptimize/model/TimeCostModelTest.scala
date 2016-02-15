package com.shouldioptimize.model

import org.scalatest.{Matchers, FunSpec}

class TimeCostModelTest extends FunSpec with Matchers {
  describe("Hourly time cost model") {
    it("calculates time cost for a seq of ec2 pricing") {
      val effectiveTaxRate = BigDecimal("33.964")
      val hourlyRate = BigDecimal("100.00")
      val model = new HourlyTimeCostModel(hourlyRate, effectiveTaxRate)
      val taxMultiplier = (BigDecimal(100) - effectiveTaxRate) / BigDecimal(100)

      model.calculate("t2.nano") should equal(TimeCostResult("t2.nano", Ec2Pricing.all("t2.nano"), hourlyRate * taxMultiplier / Ec2Pricing.all("t2.nano")))
    }
  }

  describe("Pre-tax hourly time cost model") {
    it("calculates time cost for a seq of ec2 pricing") {
      val model = new PreTaxHourlyTimeCostModel(BigDecimal("100.00"))

      model.calculate("t2.nano") should equal(TimeCostResult("t2.nano", Ec2Pricing.all("t2.nano"), BigDecimal("100.00") / Ec2Pricing.all("t2.nano")))
    }
  }
}

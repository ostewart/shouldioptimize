package com.whenshouldioptimize.model

import org.scalatest.{Matchers, FunSpec}

class TimeCostModelTest extends FunSpec with Matchers {
  describe("Time cost model") {
    it("calculates time cost for a seq of ec2 pricing") {
      val effectiveTaxRate = BigDecimal("33.964")
      val model = new TaxFreeHourlyTimeCostModel(BigDecimal("100.00"), effectiveTaxRate)
      val taxMultiplier = BigDecimal(100) - effectiveTaxRate

      model.calculate("t2.nano") should equal(TimeCostResult("t2.nano", Ec2Pricing.all("t2.nano"), BigDecimal("100.00") * taxMultiplier / Ec2Pricing.all("t2.nano")))
    }
  }
}

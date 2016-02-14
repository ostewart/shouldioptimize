package com.whenshouldioptimize.model

object Ec2Pricing {
  val all = Map("t2.nano" -> BigDecimal("0.0065"),
                "t2.micro" -> BigDecimal("0.013"),
                "t2.small" -> BigDecimal("0.026"),
                "t2.medium" -> BigDecimal("0.052"))
}

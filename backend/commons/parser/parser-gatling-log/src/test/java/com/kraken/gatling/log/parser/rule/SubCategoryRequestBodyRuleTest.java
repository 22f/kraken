package com.kraken.gatling.log.parser.rule;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class SubCategoryRequestBodyRuleTest extends SubCategoryRuleTest {

  @Autowired
  SubCategoryRequestBodyRule rule;

  @BeforeEach
  public void before() {
    super.before(rule);
  }
}
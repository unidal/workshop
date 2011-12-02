package com.ebay.eunit.testfwk.spi;

public interface ITestClassRunner {
   public void setPlan(ITestPlan<? extends ITestCallback> plan, Object description);
}

package com.ebay.eunit.testfwk.spi;

import com.ebay.eunit.model.entity.EunitMethod;

public interface ITestCaseBuilder<T extends ITestCallback> {
   public ITestCase<T> build(IClassContext ctx, EunitMethod eunitMethod);
}

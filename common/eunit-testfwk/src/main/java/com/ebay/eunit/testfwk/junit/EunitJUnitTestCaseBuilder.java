package com.ebay.eunit.testfwk.junit;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.EunitTaskType;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.ITestCase;
import com.ebay.eunit.testfwk.spi.ITestCaseBuilder;

public class EunitJUnitTestCaseBuilder implements ITestCaseBuilder<JUnitCallback> {
   @Override
   public ITestCase<JUnitCallback> build(IClassContext ctx, EunitMethod eunitMethod) {
      JUnitTestCase testCase = new JUnitTestCase(eunitMethod);

      testCase.addTask(EunitTaskType.TEST_CASE, eunitMethod);

      return testCase;
   }
}

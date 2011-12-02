package com.ebay.eunit.cmd.testfwk.junit;

import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;
import com.ebay.eunit.cmd.testfwk.HttpTaskType;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.junit.EunitJUnitTestCaseBuilder;
import com.ebay.eunit.testfwk.junit.JUnitCallback;
import com.ebay.eunit.testfwk.junit.JUnitTestCase;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IModelContext;
import com.ebay.eunit.testfwk.spi.ITestCase;

public class CommandJUnitTestCaseBuilder extends EunitJUnitTestCaseBuilder {
   @Override
   public ITestCase<JUnitCallback> build(IClassContext classContext, EunitMethod eunitMethod) {
      JUnitTestCase testCase = new JUnitTestCase(eunitMethod);
      IModelContext<RootEntity> ctx = classContext.forModel();
      CommandEntity cmd = ctx.getModel().findCommand(eunitMethod.getName());

      if (cmd != null) {
         testCase.addTask(HttpTaskType.EXECUTE, eunitMethod);
         testCase.addTask(HttpTaskType.VERIFY, eunitMethod);
      } else {
         return super.build(classContext, eunitMethod);
      }

      return testCase;
   }
}

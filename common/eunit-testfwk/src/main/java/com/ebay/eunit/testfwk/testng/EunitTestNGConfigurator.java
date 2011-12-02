package com.ebay.eunit.testfwk.testng;

import com.ebay.eunit.handler.ExpectedExceptionHandler;
import com.ebay.eunit.handler.ExpectedExceptionsHandler;
import com.ebay.eunit.handler.GroupsHandler;
import com.ebay.eunit.handler.IdHandler;
import com.ebay.eunit.handler.InterceptHandler;
import com.ebay.eunit.handler.RunGroupsHandler;
import com.ebay.eunit.handler.RunIgnoreHandler;
import com.ebay.eunit.handler.ServiceProviderHandler;
import com.ebay.eunit.handler.testng.AfterClassHandler;
import com.ebay.eunit.handler.testng.AfterMethodHandler;
import com.ebay.eunit.handler.testng.BeforeClassHandler;
import com.ebay.eunit.handler.testng.BeforeMethodHandler;
import com.ebay.eunit.handler.testng.TestHandler;
import com.ebay.eunit.invocation.EunitCaseContextFactory;
import com.ebay.eunit.invocation.EunitMethodInvoker;
import com.ebay.eunit.testfwk.ClassProcessor;
import com.ebay.eunit.testfwk.EunitEventListener;
import com.ebay.eunit.testfwk.EunitTaskExecutor;
import com.ebay.eunit.testfwk.junit.EunitJUnitTestCaseBuilder;
import com.ebay.eunit.testfwk.spi.IConfigurator;
import com.ebay.eunit.testfwk.spi.Registry;

public class EunitTestNGConfigurator implements IConfigurator {
   @Override
   public void configure(Registry registry) {
      registry.registerEventListener(EunitEventListener.INSTANCE);
      registry.registerClassProcessor(new ClassProcessor());
      registry.registerTaskExecutors(EunitTaskExecutor.values());
      registry.registerCaseContextFactory(new EunitCaseContextFactory());
      registry.registerMethodInvoker(new EunitMethodInvoker());

      registry.registerTestPlanBuilder(new EunitTestNGTestPlanBuilder());
      registry.registerTestCaseBuilder(new EunitJUnitTestCaseBuilder());

      registry.registerAnnotationHandler(TestHandler.INSTANCE);
      registry.registerAnnotationHandler(BeforeMethodHandler.INSTANCE);
      registry.registerAnnotationHandler(AfterMethodHandler.INSTANCE);
      registry.registerAnnotationHandler(BeforeClassHandler.INSTANCE);
      registry.registerAnnotationHandler(AfterClassHandler.INSTANCE);
      registry.registerAnnotationHandler(RunIgnoreHandler.INSTANCE);
      
      registry.registerAnnotationHandler(ExpectedExceptionHandler.INSTANCE);
      registry.registerAnnotationHandler(ExpectedExceptionsHandler.INSTANCE);
      registry.registerAnnotationHandler(IdHandler.INSTANCE);
      registry.registerAnnotationHandler(GroupsHandler.INSTANCE);
      registry.registerAnnotationHandler(InterceptHandler.INSTANCE);
      registry.registerAnnotationHandler(RunGroupsHandler.INSTANCE);
      registry.registerAnnotationHandler(RunIgnoreHandler.INSTANCE);

      registry.registerMetaAnnotationHandler(ServiceProviderHandler.INSTANCE);
   }
}

package com.ebay.eunit.testfwk.junit;

import com.ebay.eunit.handler.ExpectedExceptionHandler;
import com.ebay.eunit.handler.ExpectedExceptionsHandler;
import com.ebay.eunit.handler.GroupsHandler;
import com.ebay.eunit.handler.IdHandler;
import com.ebay.eunit.handler.InterceptHandler;
import com.ebay.eunit.handler.RunGroupsHandler;
import com.ebay.eunit.handler.RunIgnoreHandler;
import com.ebay.eunit.handler.ServiceProviderHandler;
import com.ebay.eunit.handler.junit.AfterClassHandler;
import com.ebay.eunit.handler.junit.AfterHandler;
import com.ebay.eunit.handler.junit.BeforeClassHandler;
import com.ebay.eunit.handler.junit.BeforeHandler;
import com.ebay.eunit.handler.junit.IgnoreHandler;
import com.ebay.eunit.handler.junit.TestHandler;
import com.ebay.eunit.invocation.EunitCaseContextFactory;
import com.ebay.eunit.invocation.EunitMethodInvoker;
import com.ebay.eunit.invocation.EunitParameterResolver;
import com.ebay.eunit.testfwk.ClassProcessor;
import com.ebay.eunit.testfwk.EunitEventListener;
import com.ebay.eunit.testfwk.EunitTaskExecutor;
import com.ebay.eunit.testfwk.spi.IConfigurator;
import com.ebay.eunit.testfwk.spi.Registry;
import com.ebay.eunit.testfwk.spi.task.Priority;

public class EunitJUnitConfigurator implements IConfigurator {
   @Override
   public void configure(Registry registry) {
      registry.registerEventListener(EunitEventListener.INSTANCE);
      registry.registerClassProcessor(new ClassProcessor());
      registry.registerTaskExecutors(EunitTaskExecutor.values());
      registry.registerCaseContextFactory(new EunitCaseContextFactory());
      registry.registerMethodInvoker(new EunitMethodInvoker());
      registry.registerParamResolver(EunitParameterResolver.INSTANCE);
      registry.registerCaseValve(Priority.HIGH, EunitExceptionValve.INSTANCE);

      registry.registerTestPlanBuilder(new EunitJUnitTestPlanBuilder());
      registry.registerTestCaseBuilder(new EunitJUnitTestCaseBuilder());

      registry.registerAnnotationHandler(TestHandler.INSTANCE);
      registry.registerAnnotationHandler(IgnoreHandler.INSTANCE);
      registry.registerAnnotationHandler(BeforeClassHandler.INSTANCE);
      registry.registerAnnotationHandler(AfterClassHandler.INSTANCE);
      registry.registerAnnotationHandler(BeforeHandler.INSTANCE);
      registry.registerAnnotationHandler(AfterHandler.INSTANCE);
      
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

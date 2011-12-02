package com.ebay.eunit.cmd.testfwk.junit;

import com.ebay.eunit.cmd.handler.ExpectedResponseBodyHandler;
import com.ebay.eunit.cmd.handler.ExpectedResponseHandler;
import com.ebay.eunit.cmd.handler.ExpectedResponseHeadersHandler;
import com.ebay.eunit.cmd.handler.GETHandler;
import com.ebay.eunit.cmd.handler.HttpProtocolHandler;
import com.ebay.eunit.cmd.handler.POSTHandler;
import com.ebay.eunit.cmd.handler.WithRequestHeadersHandler;
import com.ebay.eunit.cmd.handler.WithRequestParametersHandler;
import com.ebay.eunit.cmd.testfwk.CommandEventListener;
import com.ebay.eunit.cmd.testfwk.HttpTaskExecutor;
import com.ebay.eunit.testfwk.junit.EunitJUnitConfigurator;
import com.ebay.eunit.testfwk.spi.Registry;

public class CommandConfigurator extends EunitJUnitConfigurator {
   public static final CommandConfigurator INSTANCE = new CommandConfigurator();

   private CommandConfigurator() {
   }

   public void configure(Registry registry) {
      super.configure(registry);

      // override event listener and test case builder
      registry.registerEventListener(CommandEventListener.INSTANCE);
      registry.registerTestCaseBuilder(new CommandJUnitTestCaseBuilder());

      registry.registerAnnotationHandler(GETHandler.INSTANCE);
      registry.registerAnnotationHandler(POSTHandler.INSTANCE);
      registry.registerAnnotationHandler(WithRequestHeadersHandler.INSTANCE);
      registry.registerAnnotationHandler(WithRequestParametersHandler.INSTANCE);
      registry.registerAnnotationHandler(ExpectedResponseHandler.INSTANCE);
      registry.registerAnnotationHandler(ExpectedResponseHeadersHandler.INSTANCE);
      registry.registerAnnotationHandler(ExpectedResponseBodyHandler.INSTANCE);

      registry.registerTaskExecutors(HttpTaskExecutor.values());

      registry.registerMetaAnnotationHandler(HttpProtocolHandler.INSTANCE);
   }
}

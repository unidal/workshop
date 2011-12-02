package com.ebay.eunit.cmd;

import org.junit.runners.model.InitializationError;

import com.ebay.eunit.EunitJUnit4Runner;
import com.ebay.eunit.cmd.testfwk.junit.CommandConfigurator;
import com.ebay.eunit.testfwk.spi.IConfigurator;

public class CommandClassRunner extends EunitJUnit4Runner {
   public CommandClassRunner(Class<?> clazz) throws InitializationError {
      super(clazz);
   }

   public CommandClassRunner(Class<?> clazz, String methodName) throws InitializationError {
      super(clazz, methodName);
   }

   @Override
   protected IConfigurator getConfigurator() {
      return CommandConfigurator.INSTANCE;
   }
}

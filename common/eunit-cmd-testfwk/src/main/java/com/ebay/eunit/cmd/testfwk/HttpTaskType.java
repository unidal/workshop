package com.ebay.eunit.cmd.testfwk;

import com.ebay.eunit.testfwk.spi.task.ITaskType;

public enum HttpTaskType implements ITaskType {
   EXECUTE,

   VERIFY;

   @Override
   public String getName() {
      return name();
   }
}
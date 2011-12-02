package com.ebay.eunit.benchmark.testfwk;

import com.ebay.eunit.testfwk.spi.task.ITaskType;

public enum CpuTaskType implements ITaskType {
   START,

   WARMUP,

   EXECUTE,

   END;

   @Override
   public String getName() {
      return name();
   }
}
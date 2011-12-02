package com.ebay.eunit.benchmark.testfwk;

import com.ebay.eunit.testfwk.EunitTaskType;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.task.ITaskExecutor;

public enum ReportTaskExecutor implements ITaskExecutor<EunitTaskType> {
   XML_REPORT(EunitTaskType.AFTER_CLASS) {
      @Override
      public void execute(ICaseContext ctx) {
         System.out.println(ctx.getClassContext().forModel().getModel());
      }
   };

   private EunitTaskType m_type;

   private ReportTaskExecutor(EunitTaskType type) {
      m_type = type;
   }

   @Override
   public EunitTaskType getTaskType() {
      return m_type;
   }
}
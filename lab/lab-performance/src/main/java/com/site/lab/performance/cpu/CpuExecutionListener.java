package com.site.lab.performance.cpu;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import com.site.lab.performance.ExecutionContext;
import com.site.lab.performance.ExecutionListener;
import com.site.lab.performance.model.Case;

public class CpuExecutionListener implements ExecutionListener {
   private CpuMeta m_meta;

   private long m_cpuTime0;

   private long m_elapsedTime0;

   private long m_cpuTime1;

   private long m_elapsedTime1;

   private ThreadMXBean m_mxbean = ManagementFactory.getThreadMXBean();

   private void destroy() {
      m_meta = null;
   }

   public boolean isEligible(ExecutionContext ctx) {
      m_meta = ctx.getMethod().getAnnotation(CpuMeta.class);

      if (m_meta != null) {
         ctx.setExecutions(m_meta.loops());
         ctx.setWarmups(m_meta.warmups());

         return true;
      } else {
         return false;
      }
   }

   public boolean shouldForkForGclog(ExecutionContext ctx) {
      return false;
   }

   public void onAfterExecutions(final ExecutionContext ctx) {
      final long cpuTime = (m_mxbean.getCurrentThreadCpuTime() - m_cpuTime1);
      final long elapsedTime = System.nanoTime() - m_elapsedTime1;
      final Case c = ctx.getCase();

      c.setElapsedTime(elapsedTime / ctx.getExecutions());
      c.setElapsedTotalTime(elapsedTime);
      c.setCpuLoops(ctx.getExecutions());
      c.setCpuTime(cpuTime / ctx.getExecutions());
      c.setCpuTotalTime(cpuTime);

      final int warmups = m_meta.warmups();
      if (warmups > 0) {
         c.setCpuFirstTime(m_cpuTime1 - m_cpuTime0 - (warmups - 1) * c.getCpuTime());
         c.setElapsedFirstTime(m_elapsedTime1 - m_elapsedTime0 - (warmups - 1) * c.getElapsedTime());

         // set to undefined(-1)
         // where measure precision is small than sampling error
         if (c.getCpuFirstTime() <= 0) {
            c.setCpuFirstTime(-1);
         }

         if (c.getElapsedFirstTime() <= 0) {
            c.setElapsedFirstTime(-1);
         }
      }

      destroy();
   }

   public void onBeforeExecutions(final ExecutionContext ctx) {
      m_cpuTime1 = m_mxbean.getCurrentThreadCpuTime();
      m_elapsedTime1 = System.nanoTime();
   }

   public void onBeforeWarmups(final ExecutionContext ctx) {
      m_cpuTime0 = m_mxbean.getCurrentThreadCpuTime();
      m_elapsedTime0 = System.nanoTime();
   }

   public void onExecution(final ExecutionContext ctx) {
      // do nothing here
   }
}

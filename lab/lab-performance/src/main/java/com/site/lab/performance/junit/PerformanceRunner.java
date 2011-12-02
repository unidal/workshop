package com.site.lab.performance.junit;

import java.io.File;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.site.lab.performance.BenchmarkExecution;
import com.site.lab.performance.Timer;

public class PerformanceRunner extends Runner {
   private Class<?> m_clazz;

   private Description m_description;

   private PerformanceReportMeta m_meta;

   static {
      Timer.initialize();
   }

   public PerformanceRunner(Class<?> clazz) {
      m_clazz = clazz;
      m_meta = clazz.getAnnotation(PerformanceReportMeta.class);
      m_description = Description.createTestDescription(m_clazz, m_clazz.getSimpleName());
   }

   @Override
   public Description getDescription() {
      return m_description;
   }

   @Override
   public void run(RunNotifier notifier) {
      notifier.fireTestStarted(m_description);

      try {
         BenchmarkExecution execution = new BenchmarkExecution(m_clazz, null);

         execution.execute();

         if (m_meta != null) {
            execution.generateReport(new File(m_meta.value()));
         }
      } catch (Exception e) {
         notifier.fireTestFailure(new Failure(m_description, e));
      } finally {
         notifier.fireTestFinished(m_description);
      }
   }
}

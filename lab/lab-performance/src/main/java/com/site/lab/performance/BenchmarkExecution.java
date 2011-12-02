package com.site.lab.performance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.site.lab.performance.cpu.CpuExecutionListener;
import com.site.lab.performance.memory.MemoryExecutionListener;
import com.site.lab.performance.model.Benchmark;

public class BenchmarkExecution {
   private Class<?> m_clazz;

   private Benchmark m_benchmark;

   private List<ExecutionListener> m_listeners;

   public BenchmarkExecution(Class<?> clazz, Benchmark benchmark) {
      m_clazz = clazz;
      m_benchmark = benchmark == null ? new Benchmark() : benchmark;

      m_listeners = new ArrayList<ExecutionListener>();
      m_listeners.add(new MemoryExecutionListener());
      m_listeners.add(new CpuExecutionListener());
   }

   public void execute() {
      try {
         Executor executor = new Executor(m_clazz, m_benchmark);

         for (ExecutionListener listener : m_listeners) {
            executor.addListener(listener);
         }

         executor.execute();
      } catch (Exception e) {
         throw new RuntimeException("Error when executing performance benchmark for class: " + m_clazz + ". " + e, e);
      }
   }

   public void generateReport(File reportFile) {
      BenchmarkReporter reporter = new BenchmarkReporter();

      reporter.generateReport(m_benchmark, reportFile);
   }
}

package com.site.lab.performance;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.site.lab.performance.model.Benchmark;
import com.site.lab.performance.model.Case;
import com.site.lab.performance.model.Suite;

public class Executor {
   private Class<?> m_clazz;

   private Benchmark m_benchmark;

   private final List<ExecutionListener> m_listeners;

   public Executor(final Class<?> clazz, final Benchmark benchmark) {
      m_clazz = clazz;
      m_benchmark = benchmark != null ? benchmark : new Benchmark();
      m_listeners = new ArrayList<ExecutionListener>();
   }

   public void execute() throws Exception {
      final List<Method> methods = getElegibleMethods();
      final ExecutionContext ctx = new ExecutionContext();
      final Object objectInstance = m_clazz.newInstance();
      final Suite suite = BenchmarkHelper.newSuite(m_benchmark, m_clazz);

      for (final Method method : methods) {
         ctx.reset(method);
         
         for (final ExecutionListener listener : m_listeners) {
            // check eligible
            if (listener.isEligible(ctx)) {
               Case kase = BenchmarkHelper.newCase(suite, method.getName());

               if (kase.getStartTime() == 0) {
                  kase.setStartTime(Timer.getCurrentTime());
               }

               ctx.setCase(kase);

               try {
                  executeSingle(listener, ctx, objectInstance, method);
               } catch (Exception e) {
                  final StringWriter sw = new StringWriter(2048);

                  e.printStackTrace(new PrintWriter(sw));
                  ctx.getCase().setNotes("Exception: " + sw.toString());
               } finally {
                  kase.setEndTime(Timer.getCurrentTime());
               }
            }
         }
      }
   }

   private void executeSingle(final ExecutionListener listener, final ExecutionContext ctx,
         final Object objectInstance, Method method) throws Exception {
      final Object[] params = getMethodParameters(method, ctx);
      
      // make sure no extra memory allocation within executions
      final int count = ctx.getExecutions();
      int index;

      // before warm-ups
      listener.onBeforeWarmups(ctx);

      // warm up
      for (int i = ctx.getWarmups(); i > 0; i--) {
         method.invoke(objectInstance, params);
      }

      // before executions
      listener.onBeforeExecutions(ctx);

      // do executions
      for (index = 0; index < count; index++) {
         ctx.setCurrentIndex(index);
         ctx.setCurrentInstance(method.invoke(objectInstance, params));

         listener.onExecution(ctx);
      }

      // after executions
      listener.onAfterExecutions(ctx);
   }

   private Object[] getMethodParameters(final Method method, final ExecutionContext ctx) {
      final int len = method.getParameterTypes().length;

      switch (len) {
      case 0:
         return new Object[0];
      case 1:
         return new Object[] { ctx };
      }

      throw new RuntimeException("Only at most one parameter is allowed for " + method);
   }

   private List<Method> getElegibleMethods() {
      final List<Method> methods = new ArrayList<Method>();

      for (Method method : m_clazz.getMethods()) {
         if (method.getAnnotations().length > 0) {
            methods.add(method);
         }
      }

      Collections.sort(methods, new Comparator<Method>() {
         public int compare(final Method o1, final Method o2) {
            return o1.getName().compareTo(o2.getName());
         }
      });

      return methods;
   }

   public Benchmark getBenchmark() {
      return m_benchmark;
   }

   public void addListener(ExecutionListener listener) {
      if (!m_listeners.contains(listener)) {
         m_listeners.add(listener);
      }
   }
}

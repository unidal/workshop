package com.site.kernel.initialization;

import java.util.Stack;

import com.site.kernel.logging.Log;

public abstract class BaseModule {
   private static final Log s_log = Log.getLog(BaseModule.class);

   private BaseModule[] m_modules;

   private boolean m_initialized;

   public BaseModule(BaseModule[] modules) {
      m_modules = modules;
   }

   public final void doInitialization(ModuleContext ctx, Stack<BaseModule> stack, boolean verbose) {
      if (m_initialized) {
         return;
      }

      long start = System.currentTimeMillis();

      if (stack.contains(this)) {
         throw new RuntimeException("A dead loop dependent module detected: " + getStackTrace(stack));
      }

      if (verbose) {
         s_log.info("Initializing " + this.getClass().getName() + " {");
      }

      stack.push(this);

      int size = (m_modules == null ? 0 : m_modules.length);

      for (int i = 0; i < size; i++) {
         BaseModule module = m_modules[i];

         if (module != null && !module.isInitialized()) {
            module.doInitialization(ctx, stack, verbose);
         }
      }

      try {
         initialize(ctx);

         if (verbose) {
            s_log.info("Initializing " + this.getClass().getName() + " } DONE " + (System.currentTimeMillis() - start) + " ms");
         }
      } catch (Throwable e) {
         s_log.error("Initializing " + this.getClass().getName() + " } FAILED " + (System.currentTimeMillis() - start) + " ms", e);
      }

      m_initialized = true;
      stack.pop();
   }

   public final void doShutdown() {
      // TODO
   }

   private String getStackTrace(Stack<BaseModule> stack) {
      StringBuffer sb = new StringBuffer(1024);

      sb.append(this.getClass().getName());

      int size = stack.size();
      for (int i = size - 1; i >= 0; i--) {
         BaseModule module = stack.get(i);

         sb.append("   => ");
         sb.append(module.getClass().getName());
      }

      return sb.toString();
   }

   protected abstract void initialize(ModuleContext ctx);

   protected abstract void shutdown();

   public boolean isInitialized() {
      return m_initialized;
   }

}

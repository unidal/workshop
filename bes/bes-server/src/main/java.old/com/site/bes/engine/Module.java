package com.site.bes.engine;

import com.site.kernel.initialization.BaseModule;
import com.site.kernel.initialization.ModuleContext;

public class Module extends BaseModule {
   public static final Module FULL = defineFull();

   private static Module defineFull() {
      final BaseModule[] DEPENDENT_MODULES = { com.site.bes.Module.FULL, };

      return new Module(DEPENDENT_MODULES);
   }

   private Module(BaseModule[] modules) {
      super(modules);
   }

   protected void initialize(ModuleContext ctx) {
      EventEngine.initModels();
   }

   protected void shutdown() {
      // Do nothing here
   }
}

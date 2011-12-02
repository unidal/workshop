package com.site.bes.payload;

import com.site.bes.EventPayload;
import com.site.kernel.SystemRegistry;
import com.site.kernel.initialization.BaseModule;
import com.site.kernel.initialization.ModuleContext;

public class Module extends BaseModule {
   public static final Module FULL = defineFull();

   private static Module defineFull() {
      final BaseModule[] DEPENDENT_MODULES = {};

      return new Module(DEPENDENT_MODULES);
   }

   private Module(BaseModule[] modules) {
      super(modules);
   }

   protected void initialize(ModuleContext ctx) {
      registerMessagePayload();
   }

   private void registerMessagePayload() {
      SystemRegistry.register(EventPayload.class, MessageEventType.MESSAGE_NEW.getName(), MessagePayload.class);
      SystemRegistry.register(EventPayload.class, MessageEventType.MESSAGE_MODIFIED.getName(), MessagePayload.class);
      SystemRegistry.register(EventPayload.class, MessageEventType.MESSAGE_DELETED.getName(), MessagePayload.class);
   }

   protected void shutdown() {
      // Do nothing here
   }
}

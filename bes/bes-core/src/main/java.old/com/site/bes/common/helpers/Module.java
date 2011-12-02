package com.site.bes.common.helpers;

import com.site.bes.EventPayloadFormat;
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
      SystemRegistry.register(EventPayloadFormatter.class, EventPayloadFormat.PROPERTIES, PropertiesPayloadFormatter.class);
      SystemRegistry.register(EventPayloadFormatter.class, EventPayloadFormat.XML, XmlPayloadFormatter.class);
      SystemRegistry.register(EventPayloadFormatter.class, EventPayloadFormat.URL, UrlPayloadFormatter.class);
   }

   protected void shutdown() {
      // Do nothing here
   }
}

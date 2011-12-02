package com.ebay.eunit.testfwk;

import java.util.HashMap;

import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassProcessor;
import com.ebay.eunit.testfwk.spi.IConfigurator;
import com.ebay.eunit.testfwk.spi.ITestCallback;
import com.ebay.eunit.testfwk.spi.ITestClassRunner;
import com.ebay.eunit.testfwk.spi.ITestPlanBuilder;
import com.ebay.eunit.testfwk.spi.Registry;

public enum EunitManager {
   INSTANCE;

   private volatile HashMap<Class<?>, Registry> m_registries = new HashMap<Class<?>, Registry>();

   public void buildPlan(IClassContext ctx, ITestClassRunner runner) {
      IClassProcessor processor = ctx.getRegistry().getClassProcessor();
      ITestPlanBuilder<? extends ITestCallback> builder = ctx.getRegistry().getTestPlanBuilder();

      processor.process(ctx);
      builder.build(ctx);

      ctx.getTestPlan().bindTo(runner);
   }

   public Registry getRegistry(Class<?> namespace) {
      Registry registry = m_registries.get(namespace);

      if (registry == null) {
         throw new IllegalStateException(String.format("No registry defined for namespace(%s)!", namespace.getName()));
      } else {
         return registry;
      }
   }

   public boolean hasRegistry(Class<?> namespace) {
      return m_registries.containsKey(namespace);
   }

   public Registry initialize(Class<?> namespace, IConfigurator... configurators) {
      Registry registry = m_registries.get(namespace);

      if (registry == null) {
         synchronized (m_registries) {
            registry = m_registries.get(namespace);

            if (registry == null) {
               registry = new Registry(namespace);
               m_registries.put(namespace, registry);

               // registry is only configured at the first time
               for (IConfigurator configurator : configurators) {
                  configurator.configure(registry);
               }
            }
         }
      }

      return registry;
   }
}

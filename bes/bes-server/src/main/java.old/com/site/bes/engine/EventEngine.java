package com.site.bes.engine;

import java.io.File;

import com.site.bes.engine.config.ConsumersBo;
import com.site.kernel.SystemPathFinder;
import com.site.kernel.dal.model.DefaultHandler;
import com.site.kernel.dal.model.ModelRegistry;
import com.site.kernel.initialization.ModuleManager;
import com.site.kernel.logging.Log;

public class EventEngine {
   private static final Log s_log = Log.getLog(EventEngine.class);

   private static final String CONSUMER = "consumer.xml";

   private ConsumersBo m_consumers;

   public static void initModels() {
      ModelRegistry.register(com.site.bes.engine.config.ConsumersDo.class, com.site.bes.engine.config.ConsumersBo.class);
      ModelRegistry.register(com.site.bes.engine.config.ConsumerDo.class, com.site.bes.engine.config.ConsumerBo.class);
      ModelRegistry.register(com.site.bes.engine.config.ConfigurationDo.class, com.site.bes.engine.config.ConfigurationBo.class);
      ModelRegistry.register(com.site.bes.engine.config.ListenOnDo.class, com.site.bes.engine.config.ListenOnBo.class);
      ModelRegistry.register(com.site.bes.engine.config.EventDo.class, com.site.bes.engine.config.EventBo.class);
   }

   public static void main(String[] argv) {
      EventEngine engine = new EventEngine();

      engine.startEngine();
   }

   private ConsumersBo loadConsumers() {
      File xmlFile = new File(SystemPathFinder.getAppConfig(), CONSUMER);

      try {
         DefaultHandler parser = new DefaultHandler(ConsumersBo.class);

         parser.parse(xmlFile.getAbsolutePath());
         parser.validateModel();

         return (ConsumersBo) parser.getRootModel();
      } catch (Throwable t) {
         t.printStackTrace();

         throw new RuntimeException(t.getMessage());
      }
   }

   public void startEngine() {
      ModuleManager.initialize();

      m_consumers = (ConsumersBo) loadConsumers();
      m_consumers.startThread(null, null);

      // 5 minutes timeout, it's enough for 50 events to complete
      EventRecoveryThread recovery = new EventRecoveryThread(30 * 1000);
      recovery.start();
      
      s_log.info("Event Engine 1.0 started.");
   }
}

package com.site.bes.server;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.site.bes.engine.EventRecoveryThread;
import com.site.bes.engine.config.ConsumersBo;
import com.site.kernel.SystemPathFinder;
import com.site.kernel.SystemRegistry;
import com.site.kernel.dal.model.DefaultHandler;
import com.site.kernel.dal.model.ModelRegistry;
import com.site.kernel.initialization.ModuleManager;
import com.site.kernel.logging.Log;

public class BesServlet extends HttpServlet {
   private static final Log s_logger = Log.getLog(BesServlet.class);

   private static final String CONSUMER = "consumer.xml";

   private ConsumersBo m_consumers;

   static {
      initModels();
   }

   public static void initModels() {
      ModelRegistry.register(com.site.bes.engine.config.ConsumersDo.class, com.site.bes.engine.config.ConsumersBo.class);
      ModelRegistry.register(com.site.bes.engine.config.ConsumerDo.class, com.site.bes.engine.config.ConsumerBo.class);
      ModelRegistry.register(com.site.bes.engine.config.ListenOnDo.class, com.site.bes.engine.config.ListenOnBo.class);
      ModelRegistry.register(com.site.bes.engine.config.EventDo.class, com.site.bes.engine.config.EventBo.class);
   }

   public ConsumersBo getConsumers() {
      return m_consumers;
   }

   public void init(ServletConfig config) throws ServletException {
      try {
         startEngine();
      } catch (RuntimeException e) {
         e.printStackTrace();

         throw e;
      } catch (Throwable t) {
         t.printStackTrace();

         throw new RuntimeException(t);
      }
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

   protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      ServletOutputStream sos = res.getOutputStream();

      sos.print("Event engine 1.2, all rights reserved. Current time: " + new Date());
      sos.close();
   }

   private void startEngine() {
      ModuleManager.initialize();

      m_consumers = (ConsumersBo) loadConsumers();
      m_consumers.startThread(null, null);

      // 5 minutes timeout, it's enough for 50 events to complete
      EventRecoveryThread recovery = new EventRecoveryThread(30 * 1000);
      recovery.start();

      SystemRegistry.register(HttpServlet.class, BesServlet.class, this);
      s_logger.info("BES Server started.");
   }
}

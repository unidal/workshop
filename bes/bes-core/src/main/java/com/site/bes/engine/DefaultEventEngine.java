package com.site.bes.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.bes.consumer.EventConsumerEvent;
import com.site.bes.consumer.EventConsumerRegistry;
import com.site.bes.queue.EventQueue;
import com.site.bes.queue.EventQueueManager;
import com.site.lookup.ContainerHolder;

public class DefaultEventEngine extends ContainerHolder implements EventEngine, Initializable, LogEnabled {
   private EventConsumerRegistry m_registry;

   private EventQueueManager m_manager;

   private final List<EventDispatcher> m_dispatchers = new ArrayList<EventDispatcher>();

   private ExecutorService m_service = Executors.newCachedThreadPool();

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handleRegister(EventConsumerEvent event) {
      String eventType = event.getEventType();
      EventQueue queue = m_manager.getEventQueueByEventType(eventType);
      EventDispatcher dispatcher = lookup(EventDispatcher.class);

      dispatcher.setEventQueue(queue);
      dispatcher.setEventType(eventType);
      dispatcher.setConsumer(event.getConsumer());
      m_dispatchers.add(dispatcher);
   }

   public void initialize() throws InitializationException {
      m_registry.addListener(this);
   }

   public void start() {
      m_logger.info("Starting event engine ...");

      for (EventDispatcher dispatcher : m_dispatchers) {
         m_logger.info("Starting dispatcher for event type(" + dispatcher.getEventType() + ")");
         m_service.execute(dispatcher);
      }

      m_service.shutdown();
      m_logger.info("Started event engine.");
   }

   public void stop() {
      m_logger.info("Stopping event engine ...");
      m_service.shutdownNow();
      m_logger.info("Stopped event engine.");
   }
}

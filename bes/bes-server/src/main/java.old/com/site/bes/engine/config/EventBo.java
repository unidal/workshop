package com.site.bes.engine.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.site.bes.EventConsumer;
import com.site.bes.engine.DefaultEventBatch;
import com.site.bes.engine.EventScheduleThread;
import com.site.bes.engine.EventThread;
import com.site.bes.engine.EventTransporter;
import com.site.kernel.logging.Log;

public class EventBo extends EventDo {
   private static final Log LOG = Log.getLog(EventBo.class);

   static {
      init();
   }

   private EventThread m_thread;

   private EventScheduleThread m_scheduleThread;

   private EventScheduleThread createEventScheduleThread(ConsumerBo consumer, EventConsumer eventConsumer) {
      DefaultEventBatch batch = new DefaultEventBatch();
      EventTransporter transporter = new EventTransporter();

      transporter.setEventType(getType());
      transporter.setConsumerType(consumer.getType());
      transporter.setConsumerId(getLocalAddress());
      transporter.setEventRetryPolicy(eventConsumer.getRetryPolicy());

      EventScheduleThread scheduleThread = new EventScheduleThread();

      scheduleThread.setTransporter(transporter);
      scheduleThread.setQueue(batch);
      scheduleThread.setEventConsumer(eventConsumer);
      scheduleThread.setCheckInterval(consumer.getCheckInterval());
      scheduleThread.setName("EventScheduleThread[" + getType() + "-" + consumer.getType() + "]");

      return scheduleThread;
   }

   private EventThread createEventThread(ConsumerBo consumer, EventConsumer eventConsumer) {
      DefaultEventBatch batch = new DefaultEventBatch();
      EventTransporter transporter = new EventTransporter();

      transporter.setEventType(getType());
      transporter.setConsumerType(consumer.getType());
      transporter.setConsumerId(getLocalAddress());
      transporter.setEventRetryPolicy(eventConsumer.getRetryPolicy());

      EventThread thread = new EventThread();

      thread.setTransporter(transporter);
      thread.setQueue(batch);
      thread.setEventConsumer(eventConsumer);
      thread.setCheckInterval(consumer.getCheckInterval());
      thread.setName("EventThread[" + getType() + "-" + consumer.getType() + "]");

      return thread;
   }

   private String getLocalAddress() {
      try {
         return InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
         return "unknown";
      }
   }

   public void startThread(ConsumerBo consumer, EventConsumer eventConsumer) {
      m_thread = createEventThread(consumer, eventConsumer);
      m_scheduleThread = createEventScheduleThread(consumer, eventConsumer);

      m_thread.start();
      LOG.debug(m_thread.getName() + " started");
      m_scheduleThread.start();
      LOG.debug(m_scheduleThread.getName() + " started");
   }

   public void stopThread(ConsumerBo consumer) {
      if (m_thread != null) {
         m_thread.interrupt();
      }

      if (m_scheduleThread != null) {
         m_scheduleThread.interrupt();
      }
   }

   public boolean isThreadRunning() {
      if (m_thread != null) {
         return m_thread.isAlive();
      }

      return false;
   }
}

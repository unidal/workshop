package com.site.game.sanguo.thread.engine;

import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadHandler;

public class DefaultThreadEngine implements ThreadEngine, LogEnabled {
   private long m_interval;

   private boolean m_stopped;

   private List<ThreadHandler> m_handlers;

   private Logger m_logger;

   public void execute(ThreadContext ctx) {
      if (m_interval < 100) {
         m_interval = 1000; // minimal wait 1 second
      }

      while (!m_stopped) {
         long start = System.currentTimeMillis();

         for (ThreadHandler handler : m_handlers) {
            try {
               handler.handle(ctx);
            } catch (Exception e) {
               m_logger.warn("Error when executing " + handler.getClass().getSimpleName(), e);
            }
         }

         System.gc();

         waitForMoment(m_interval - (System.currentTimeMillis() - start));
      }
   }

   private void waitForMoment(long interval) {
      long timeLeft = interval;

      try {
         while (timeLeft > 0) {
            long start = System.currentTimeMillis();

            Thread.sleep(timeLeft);
            timeLeft -= System.currentTimeMillis() - start;
         }
      } catch (InterruptedException e) {
         m_stopped = true;
      }
   }

   public void setInterval(long interval) {
      m_interval = interval;
   }

   public boolean isStopped() {
      return m_stopped;
   }

   public void setStopped(boolean stopped) {
      m_stopped = stopped;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}

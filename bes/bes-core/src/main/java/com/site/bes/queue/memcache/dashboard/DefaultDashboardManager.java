package com.site.bes.queue.memcache.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.bes.queue.memcache.Cache;
import com.site.lookup.ContainerHolder;

public class DefaultDashboardManager extends ContainerHolder implements DashboardManager, LogEnabled {
   private Cache m_cache;

   private Map<String, Dashboard> m_dashboards = new HashMap<String, Dashboard>();

   private Logger m_logger;

   public Dashboard getDashboard(String eventType, String consumerType) {
      String key = eventType + ":" + consumerType;
      Dashboard dashboard = m_dashboards.get(key);

      if (dashboard == null) {
         synchronized (m_dashboards) {
            dashboard = m_dashboards.get(key);

            if (dashboard == null) {
               dashboard = lookup(Dashboard.class);

               dashboard.setEventType(eventType);
               dashboard.setConsumerType(consumerType);
               dashboard.setLastEventId(getLastEventId(dashboard));
            }
         }
      }

      return dashboard;
   }

   private int getLastEventId(Dashboard dashboard) {
      String id = dashboard.getId();
      Integer lastEventId = (Integer) m_cache.get(id);

      if (lastEventId == null) {
         lastEventId = Integer.valueOf(0);

         if (!m_cache.add(id, lastEventId)) {
            m_logger.warn("Dashboard is not added, make sure that MemCachedClient is setup correctly!");
         }
      }

      return lastEventId.intValue();
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}

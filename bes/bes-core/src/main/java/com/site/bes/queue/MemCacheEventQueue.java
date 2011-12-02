package com.site.bes.queue;

import java.util.List;

import com.site.bes.Event;
import com.site.bes.EventException;
import com.site.bes.queue.memcache.block.BlockManager;
import com.site.bes.queue.memcache.dashboard.Dashboard;
import com.site.bes.queue.memcache.dashboard.DashboardManager;
import com.site.lookup.ContainerHolder;

public class MemCacheEventQueue extends ContainerHolder implements EventQueue {
   private DashboardManager m_dashboardManager;

   private BlockManager m_blockManager;

   public int add(Event event) throws EventException {
      m_blockManager.add(event);
      return event.getId();
   }

   public List<Event> poll(String eventType, String consumerType, String consumerId) throws EventException {
      Dashboard dashboard = m_dashboardManager.getDashboard(eventType, consumerType);
      int lastEventId = dashboard.getLastEventId();

      return m_blockManager.getEvents(lastEventId);
   }

   public void update(List<Event> events, String consumerType, String consumerId) {
      // TODO Auto-generated method stub
      
   }
}

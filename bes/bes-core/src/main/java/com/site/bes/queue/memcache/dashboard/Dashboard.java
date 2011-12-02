package com.site.bes.queue.memcache.dashboard;

import com.site.bes.queue.memcache.Cacheable;

public interface Dashboard extends Cacheable {
   public String getConsumerType();

   public String getEventType();

   public int getLastEventId();

   public void setConsumerType(String consumerType);

   public void setEventType(String eventType);

   public void setLastEventId(int lastEventId);
}

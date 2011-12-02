package com.site.bes.queue.memcache.block;

import java.util.List;

import com.site.bes.Event;

public interface BlockManager {
   public List<Event> getEvents(int lastEventId);

   public void add(Event event);
}

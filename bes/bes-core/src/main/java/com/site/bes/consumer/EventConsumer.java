package com.site.bes.consumer;

import java.util.List;

import com.site.bes.Event;

public interface EventConsumer {
   public void process(List<Event> events);

   public String getId();

   public String getType();
}

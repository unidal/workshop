package com.site.bes.queue;

import java.util.List;

import com.site.bes.Event;
import com.site.bes.EventException;

public interface EventQueue {
   public int add(Event event) throws EventException;

   public List<Event> poll(String eventType, String consumerType, String consumerId) throws EventException;

   public void update(List<Event> events, String consumerType, String consumerId);
}

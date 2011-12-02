package com.site.bes.queue;

import java.util.Date;
import java.util.List;

import com.site.bes.Event;
import com.site.bes.queue.EventQueue;
import com.site.lookup.ComponentTestCase;

public class MysqlEventQueueTest extends ComponentTestCase {
   public void testAdd() throws Exception {
      EventQueue queue = lookup(EventQueue.class, "mysql");
      Event event = newEvent();
      int eventId = queue.add(event);

      assertTrue("No event id returned, it should be auto incremental.", eventId > 0);
   }

   public void testPoll() throws Exception {
      EventQueue queue = lookup(EventQueue.class, "mysql");
      List<Event> events = queue.poll("user.new", "test", "localhost:port");
      int len = events.size();

      assertTrue("No events fetched", len > 0);

      Event last = events.get(len - 1);

      assertEquals("user.new", last.getType());
      assertEquals("name=alice&email=alice@site.com", last.getPayload());
      assertEquals("u1234", last.getRefId());
      assertEquals("test@localhost:port", last.getProducer());
      assertEquals(0, last.getMaxRetryTimes());
      assertEquals(0, last.getPayloadFormat());
      assertEquals(0, last.getStatus());
      assertNotNull(last.getScheduleDate());
      assertNotNull(last.getCreationDate());
   }

   private Event newEvent() {
      Event event = new Event();

      event.setType("user.new");
      event.setPayload("name=alice&email=alice@site.com");
      event.setRefId("u1234");
      event.setProducer("test@localhost:port");
      event.setScheduleDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L));

      return event;
   }
}

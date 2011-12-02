package com.site.bes.queue.memcache.event;

import java.util.Date;

import com.site.bes.Event;
import com.site.bes.queue.memcache.Formatter;
import com.site.lookup.ComponentTestCase;

public class EventFormatterTest extends ComponentTestCase {
   @SuppressWarnings("unchecked")
   public void testMarshal() throws Exception {
      Formatter<Event, Integer> formatter = lookup(Formatter.class, "event");
      Event event = new Event();

      event.setId(123);
      event.setType("type");
      event.setMaxRetryTimes(5);
      event.setPayloadFormat(1);
      event.setPayload("payload");
      event.setProducer("producer");
      event.setRefId("refId");
      event.setScheduleDate(new Date(12345L));
      event.setCreationDate(new Date(12345678L));

      assertEquals("e-123", formatter.getId(event.getId()));
      assertEquals("123\t\"type\"\t\"refId\"\t1\t\"payload\"\t\"producer\"\t5\t12345\t12345678\t", formatter.getValue(event));
   }

   @SuppressWarnings("unchecked")
   public void testUnmarshal() throws Exception {
      Formatter<Event, Integer> formatter = lookup(Formatter.class, "event");
      Event event = formatter.parseValue("123\t\"type\"\t\"refId\"\t1\t\"payload\"\t\"producer\"\t5\t12345\t12345678\t");

      assertEquals(123, event.getId());
      assertEquals("type", event.getType());
      assertEquals(5, event.getMaxRetryTimes());
      assertEquals(1, event.getPayloadFormat());
      assertEquals("payload", event.getPayload());
      assertEquals("producer", event.getProducer());
      assertEquals("refId", event.getRefId());
      assertEquals(new Date(12345L), event.getScheduleDate());
      assertEquals(new Date(12345678L), event.getCreationDate());
   }
}

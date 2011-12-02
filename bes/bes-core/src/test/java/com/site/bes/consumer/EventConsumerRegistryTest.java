package com.site.bes.consumer;

import java.util.List;

import com.site.bes.Event;
import com.site.bes.consumer.annotation.Consumer;
import com.site.lookup.ComponentTestCase;

public class EventConsumerRegistryTest extends ComponentTestCase {
   public void testRegisterListener() throws Exception {
      EventConsumerRegistry manager = lookup(EventConsumerRegistry.class);

      manager.register(new EventConsumerMock("0"));
      manager.register("type.1", new EventConsumerMock("1"));
      manager.register("type.1", new EventConsumerMock("2"));
      manager.register("type.1", new EventConsumerMock("3"));
      manager.register("type.2", new EventConsumerMock("1"));

      StringBuilder sb = new StringBuilder(256);

      manager.addListener(new EventConsumerListenerMock(sb));
      assertEquals("type.2:0,type.2:1,type.1:1,type.1:2,type.1:3,", sb.toString());
   }

   public void testRegisterConsumer() throws Exception {
      EventConsumerRegistry manager = lookup(EventConsumerRegistry.class);
      StringBuilder sb = new StringBuilder(256);

      manager.addListener(new EventConsumerListenerMock(sb));

      manager.register(new EventConsumerMock("0"));
      manager.register("type.1", new EventConsumerMock("1"));
      manager.register("type.1", new EventConsumerMock("2"));
      manager.register("type.1", new EventConsumerMock("3"));
      manager.register("type.2", new EventConsumerMock("1"));

      assertEquals("[1, 2, 3]", manager.getConsumers("type.1").toString());
      assertEquals("[0, 1]", manager.getConsumers("type.2").toString());
      assertEquals("[]", manager.getConsumers("type.3").toString());
      assertEquals("type.2:0,type.1:1,type.1:2,type.1:3,type.2:1,", sb.toString());

      try {
         manager.register(new InvalidEventConsumer());
         fail("No exception thrown for an invalid event consumer");
      } catch (RuntimeException e) {
         // expected
      }
   }

   @Consumer(defaultEventType = "type.2")
   static final class EventConsumerMock implements EventConsumer {
      private String m_id;

      public EventConsumerMock(String id) {
         m_id = id;
      }

      public void process(List<Event> events) {
         throw new UnsupportedOperationException();
      }

      @Override
      public String toString() {
         return m_id;
      }

      public String getId() {
         return m_id;
      }

      public String getType() {
         return m_id;
      }
   }

   static final class InvalidEventConsumer implements EventConsumer {
      public void process(List<Event> events) {
         throw new UnsupportedOperationException();
      }

      public String getId() {
         return null;
      }

      public String getType() {
         return null;
      }
   }

   static final class EventConsumerListenerMock implements EventConsumerListener {
      private StringBuilder m_sb;

      public EventConsumerListenerMock(StringBuilder sb) {
         m_sb = sb;
      }

      public void handleRegister(EventConsumerEvent event) {
         m_sb.append(event.getEventType() + ":" + event.getConsumer() + ",");
      }
   }
}

package com.site.bes.engine;

import java.util.List;

import com.site.bes.Event;
import com.site.bes.consumer.EventConsumer;
import com.site.bes.consumer.EventConsumerRegistry;
import com.site.bes.consumer.annotation.Consumer;
import com.site.bes.queue.EventTypeRegistry;
import com.site.lookup.ComponentTestCase;

public class EventEngineTest extends ComponentTestCase {
   public void test() throws Exception {
      EventEngine engine = lookup(EventEngine.class);
      EventConsumerRegistry consumerRegistry = lookup(EventConsumerRegistry.class);
      EventTypeRegistry eventRegistry = lookup(EventTypeRegistry.class);

      consumerRegistry.addListener(engine);
      eventRegistry.register("type.1");
      eventRegistry.register("type.2");
      consumerRegistry.register("type.1", new EventConsumerMock("mock"));
      consumerRegistry.register("type.2", new EventConsumerMock("mock"));

      engine.start();
      Thread.sleep(500L);
      engine.stop();
   }

   @Consumer(defaultEventType = "type.2")
   private static final class EventConsumerMock implements EventConsumer {
      private String m_id;

      public EventConsumerMock(String id) {
         m_id = id;
      }

      public String getId() {
         return m_id;
      }

      public String getType() {
         return m_id;
      }

      public void process(List<Event> events) {
         System.out.println(events.size() + " events processed.");
      }

      @Override
      public String toString() {
         return m_id;
      }
   }
}

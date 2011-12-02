package com.site.bes.common.remote;

import com.site.bes.Event;
import com.site.bes.EventBatch;
import com.site.bes.EventConsumer;
import com.site.bes.common.helpers.DefaultEventBatch;
import com.site.kernel.SystemRegistry;

public class RemoteConsumerSkeleton {
   public String process(String eventBatchInString) {
      DefaultEventBatch batch = new DefaultEventBatch();
      String consumerType = setupEventBatch(batch, eventBatchInString);

      if (batch.length() > 0 && consumerType != null) {
         EventConsumer consumer = (EventConsumer) SystemRegistry.lookup(EventConsumer.class, consumerType);

         consumer.processEvents(batch);
      }

      return formatEventBatch(batch);
   }

   private String formatEventBatch(EventBatch batch) {
      StringBuffer sb = new StringBuffer(1024);

      synchronized (sb) {
         int len = batch.length();

         for (int i = 0; i < len; i++) {
            Event event = batch.get(i);

            sb.append(event.getEventId()).append('\t');
            sb.append(event.getEventState().getId()).append('\t');
            sb.append(event.getNextScheduleDate() == null ? 0 : event.getNextScheduleDate().getTime()).append('\t');
            sb.append('\n');
         }
      }

      return sb.toString();
   }

   private String setupEventBatch(DefaultEventBatch batch, String eventBatchInString) {
      // TODO Auto-generated method stub
      return null;
   }
}

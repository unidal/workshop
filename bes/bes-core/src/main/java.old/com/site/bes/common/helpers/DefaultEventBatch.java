package com.site.bes.common.helpers;

import java.util.ArrayList;
import java.util.List;

import com.site.bes.Event;
import com.site.bes.EventBatch;

public class DefaultEventBatch implements EventBatch {
   private List<Event> m_batch;

   private int m_index;

   public DefaultEventBatch() {
      m_batch = new ArrayList<Event>(256);
      m_index = 0;
   }

   public void reset() {
      m_index = 0;
   }

   public void clear() {
      synchronized (m_batch) {
         m_batch.clear();
         m_index = 0;
      }
   }

   public Event next() {
      synchronized (m_batch) {
         if (hasNext()) {
            DefaultEvent event = (DefaultEvent) m_batch.get(m_index);

            m_index++;
            return event;
         } else {
            return null;
         }
      }
   }

   public void add(Event event) {
      synchronized (m_batch) {
         m_batch.add(event);
      }
   }

   public int length() {
      return m_batch.size();
   }

   public Event get(int index) {
      if (index >= 0 && index <= m_batch.size()) {
         return (DefaultEvent) m_batch.get(index);
      } else {
         throw new IndexOutOfBoundsException("length: " + m_batch.size() + ", index=" + index);
      }
   }

   public boolean hasNext() {
      return m_index < m_batch.size();
   }

}

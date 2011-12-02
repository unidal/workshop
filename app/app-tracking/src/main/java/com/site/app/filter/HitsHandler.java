package com.site.app.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HitsHandler implements Handler {
   public static final String LAST_FIVE_MINUTES_HITS = "lastFiveMinutesHits";

   public static final long ONE_MINUTE = 60 * 1000L;

   public static final int MAX_SIZE = 5;

   private volatile Map<String, Entry> m_map = new HashMap<String, Entry>(1024);

   private long m_lastCheckTime;

   public void handle(final HttpServletRequest req, final HttpServletResponse res) {
      final String ip = req.getRemoteAddr();
      Entry entry = m_map.get(ip);

      if (entry == null) {
         synchronized (m_map) {
            entry = m_map.get(ip);

            if (entry == null) {
               entry = new Entry();
               m_map.put(ip, entry);
            }
         }
      }

      entry.visit();
      req.setAttribute(LAST_FIVE_MINUTES_HITS, entry.getTotalHits());
      recycle();
   }

   private void recycle() {
      final long current = System.currentTimeMillis() / ONE_MINUTE;

      if (current > m_lastCheckTime + 2) {
         synchronized (m_map) {
            if (current > m_lastCheckTime + 2) {
               final List<String> expiredIps = new ArrayList<String>();

               for (final Map.Entry<String, Entry> e : m_map.entrySet()) {
                  if (current - e.getValue().getLastMinute() > MAX_SIZE) {
                     expiredIps.add(e.getKey());
                  }
               }

               for (String ip : expiredIps) {
                  m_map.remove(ip);
                  System.out.println("remove expired IP: " + ip);
               }

               m_lastCheckTime = current;
            }
         }
      }
   }

   private static final class Entry {
      private int m_totalHits;

      private int[] m_hitsInMin;

      private long m_lastMinute;

      public Entry() {
         m_totalHits = 0;
         m_hitsInMin = new int[MAX_SIZE];
         m_lastMinute = System.currentTimeMillis() / ONE_MINUTE;
      }

      public void visit() {
         synchronized (this) {
            final long current = System.currentTimeMillis() / ONE_MINUTE;
            final int[] hits = m_hitsInMin;

            if (current - m_lastMinute >= MAX_SIZE) {
               for (int i = MAX_SIZE - 1; i >= 0; i--) {
                  hits[i] = 0;
               }

               m_totalHits = 0;
               m_lastMinute = current;
            } else {
               while (current > m_lastMinute) {
                  m_totalHits -= hits[0];
                  System.arraycopy(hits, 1, hits, 0, MAX_SIZE - 1);
                  hits[MAX_SIZE - 1] = 0;
                  m_lastMinute++;
               }
            }

            m_totalHits++;
            hits[MAX_SIZE - 1]++;
         }
      }

      public long getLastMinute() {
         return m_lastMinute;
      }

      public int getTotalHits() {
         synchronized (this) {
            return m_totalHits;
         }
      }
   }
}

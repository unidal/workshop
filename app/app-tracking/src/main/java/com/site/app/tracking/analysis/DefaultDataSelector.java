package com.site.app.tracking.analysis;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.site.app.tracking.dal.PageVisitLog;

public class DefaultDataSelector {
   private List<PageVisitLog> m_pageVisitLogs;

   public Map<Integer, Integer> getCategory1Map() {
      if (m_pageVisitLogs == null) {
         throw new IllegalStateException("No pageVisitLogs set yet");
      }

      Map<Integer, Integer> map = new TreeMap<Integer, Integer>();

      for (PageVisitLog log : m_pageVisitLogs) {
         int key = log.getCategory1();
         int totalVisits = log.getTotalVisits();

         Integer last = map.get(key);
         if (last == null) {
            map.put(key, totalVisits);
         } else {
            map.put(key, last + totalVisits);
         }
      }

      return map;
   }

   public Map<Integer, Integer> getCategory2Map() {
      if (m_pageVisitLogs == null) {
         throw new IllegalStateException("No pageVisitLogs set yet");
      }

      Map<Integer, Integer> map = new TreeMap<Integer, Integer>();

      for (PageVisitLog log : m_pageVisitLogs) {
         int key = log.getCategory2();
         int totalVisits = log.getTotalVisits();

         Integer last = map.get(key);
         if (last == null) {
            map.put(key, totalVisits);
         } else {
            map.put(key, last + totalVisits);
         }
      }

      return map;
   }

   public Map<Boolean, Integer> getOnTopMap() {
      if (m_pageVisitLogs == null) {
         throw new IllegalStateException("No pageVisitLogs set yet");
      }

      Map<Boolean, Integer> map = new TreeMap<Boolean, Integer>();

      for (PageVisitLog log : m_pageVisitLogs) {
         boolean key = log.isOnTop();
         int totalVisits = log.getTotalVisits();

         Integer last = map.get(key);
         if (last == null) {
            map.put(key, totalVisits);
         } else {
            map.put(key, last + totalVisits);
         }
      }

      return map;
   }

   public void setPageVisitLogs(List<PageVisitLog> pageVisitLogs) {
      m_pageVisitLogs = pageVisitLogs;
   }

}

package com.site.app.tracking.counter;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.app.tracking.dal.PageVisit;
import com.site.app.tracking.dal.PageVisitLog;
import com.site.app.tracking.dal.PageVisitTrack;
import com.site.dal.jdbc.DalException;

public class SimpleProcessor implements Processor, LogEnabled {
   private static final int INITIAL_SIZE = 10 * 1024;

   private Map<String, Entry> m_map = new HashMap<String, Entry>(INITIAL_SIZE);

   private Storage m_storage;

   private Logger m_logger;

   public int process(Payload payload) {
      String pageUrl = payload.getPageUrl();
      Entry entry = m_map.get(pageUrl);

      if (entry == null) {
         synchronized (this) {
            entry = m_map.get(pageUrl);

            if (entry == null) {
               entry = new Entry();

               entry.setPageUrl(pageUrl);
               m_map.put(pageUrl, entry);
               loadEntry(entry);
            }
         }
      }

      updateEntry(entry, payload);
      return entry.getVisits();
   }

   private void updateEntry(Entry entry, Payload payload) {
      try {
         PageVisit analytics = createLocal(entry.getPageId(), entry.getPageUrl(), 1);
         PageVisitLog analyticsLog = createLocalLog(entry.getPageId(), payload, 1);
         PageVisitTrack analyticsTrack = createLocalTrack(entry.getPageId(), payload);

         m_storage.updateLog(analyticsLog);
         m_storage.update(analytics);
         m_storage.insertTrack(analyticsTrack);

         entry.increase();
      } catch (DalException e) {
         m_logger.error("Update database failure. " + e, e);
      }
   }

   private PageVisitTrack createLocalTrack(int pageId, Payload payload) {
      PageVisitTrack track = new PageVisitTrack();

      track.setPageId(pageId);
      track.setFromPage(payload.getFromPage());
      track.setClientIp(payload.getClientIp());

      return track;
   }

   private PageVisitLog createLocalLog(int pageId, Payload payload, int visits) {
      PageVisitLog log = new PageVisitLog();

      log.setPageId(pageId);
      log.setFromPage(payload.getFromPage());
      log.setCategory1(payload.getCategory1());
      log.setCategory2(payload.getCategory2());
      log.setOnTop(payload.isOnTop());
      log.setVisits(visits);

      return log;
   }

   private PageVisit createLocal(int pageId, String pageUrl, int delta) {
      PageVisit a = new PageVisit();

      if (pageId > 0) {
         a.setKeyPageId(pageId);
         a.setPageId(pageId);
      }

      a.setPageUrl(pageUrl);
      a.setTotalVisits(delta);

      return a;
   }

   private void loadEntry(Entry entry) {
      try {
         PageVisit analytics = m_storage.get(entry.getPageUrl());

         entry.setPageId(analytics.getPageId());
         entry.setVisits(analytics.getTotalVisits());
      } catch (Exception e) {
         m_logger.error(e.getMessage(), e);
      }
   }

   static final class Entry {
      private int m_pageId;

      private String m_pageUrl;

      private int m_visits;

      public int getPageId() {
         return m_pageId;
      }

      public void setPageId(int pageId) {
         m_pageId = pageId;
      }

      public String getPageUrl() {
         return m_pageUrl;
      }

      public int getVisits() {
         return m_visits;
      }

      public void increase() {
         m_visits++;
      }

      public void setPageUrl(String pageUrl) {
         m_pageUrl = pageUrl;
      }

      public void setVisits(int visits) {
         m_visits = visits;
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}

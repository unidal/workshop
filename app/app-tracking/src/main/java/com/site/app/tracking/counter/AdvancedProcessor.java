package com.site.app.tracking.counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.app.tracking.dal.PageVisit;
import com.site.app.tracking.dal.PageVisitLog;
import com.site.app.tracking.dal.PageVisitTrack;
import com.site.dal.jdbc.DalException;

public class AdvancedProcessor implements Processor, Initializable, LogEnabled {
   private static final int INITIAL_SIZE = 10 * 1024;

   private Map<String, Entry> m_map = new HashMap<String, Entry>(INITIAL_SIZE);

   private Configuration m_configuration;

   Storage m_storage;

   Porter m_porter;

   Logger m_logger;

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
   
   private List<PageVisitLog> createLocalLogs(int pageId, Map<String, LogEntry> logs) {
      List<PageVisitLog> list = new ArrayList<PageVisitLog>();

      for (LogEntry e : logs.values()) {
         PageVisitLog log = new PageVisitLog();

         log.setPageId(pageId);
         log.setFromPage(e.getFromPage());
         log.setCategory1(e.getCategory1());
         log.setCategory2(e.getCategory2());
         log.setOnTop(e.isOnTop());
         log.setVisits(e.getVisits());

         list.add(log);
      }

      return list;
   }

   private List<PageVisitTrack> createLocalTracks(int pageId, List<TrackEntry> tracks) {
      List<PageVisitTrack> list = new ArrayList<PageVisitTrack>();

      for (TrackEntry e : tracks) {
         PageVisitTrack track = new PageVisitTrack();

         track.setPageId(pageId);
         track.setFromPage(e.getFromPage());
         track.setClientIp(e.getClientId());
         track.setCreationDate(e.getCreationDate());

         list.add(track);
      }

      return list;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   void getUpdatedEntries(List<PageVisit> masters, List<PageVisitLog> logs, List<PageVisitTrack> tracks) {
      for (Entry e : m_map.values()) {
         if (e.isDirty()) {
            synchronized (m_map) {
               Entry entry = e.clone();

               // replace with last visits and empty logs and tracks
               entry.setLastVisits(e.getVisits());
               m_map.put(e.getPageUrl(), entry);
            }

            int delta = e.getVisits() - e.getLastVisits();

            masters.add(createLocal(e.getPageId(), e.getPageUrl(), delta));
            logs.addAll(createLocalLogs(e.getPageId(), e.getLogs()));
            tracks.addAll(createLocalTracks(e.getPageId(), e.getTracks()));
         }
      }
   }

   public void initialize() throws InitializationException {
      m_porter = new Porter();
      m_porter.setName("Porter");
      m_porter.setCheckInterval(m_configuration.getCheckInterval());
      m_porter.start();

      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
            m_porter.shutdown();
         }
      });
   }

   private void loadEntry(Entry entry) {
      try {
         PageVisit analytics = m_storage.get(entry.getPageUrl());

         entry.setPageId(analytics.getPageId());
         entry.setLastVisits(analytics.getTotalVisits());
         entry.setVisits(analytics.getTotalVisits());
      } catch (Exception e) {
         m_logger.error(e.getMessage(), e);
      }
   }

   public int process(Payload payload) {
      String pageUrl = payload.getPageUrl();
      Entry entry = m_map.get(pageUrl);

      if (entry == null) {
         synchronized (m_map) {
            entry = m_map.get(pageUrl);

            if (entry == null) {
               entry = new Entry();

               m_map.put(pageUrl, entry);
               entry.setPageUrl(pageUrl);
               loadEntry(entry);
            }
         }
      }

      entry.increaseVisit(payload);
      entry.appendTrack(payload);

      return entry.getVisits();
   }

   static final class Entry implements Cloneable {
      private int m_pageId;

      private String m_pageUrl;

      private int m_visits;

      private int m_lastVisits;

      private Map<String, LogEntry> m_logs = new HashMap<String, LogEntry>();

      private List<TrackEntry> m_tracks = new ArrayList<TrackEntry>();

      public void appendTrack(Payload payload) {
         m_tracks.add(new TrackEntry(payload));
      }

      @Override
      public Entry clone() {
         Entry entry = new Entry();

         entry.setPageId(m_pageId);
         entry.setPageUrl(m_pageUrl);
         entry.setLastVisits(m_lastVisits);
         entry.setVisits(m_visits);

         return entry;
      }

      private String getKey(Payload payload) {
         return payload.getFromPage() + ":" + payload.getCategory1() + ":" + payload.getCategory2() + ":"
               + (payload.isOnTop() ? "1" : "0");
      }

      public int getLastVisits() {
         return m_lastVisits;
      }

      public Map<String, LogEntry> getLogs() {
         return m_logs;
      }

      public int getPageId() {
         return m_pageId;
      }

      public String getPageUrl() {
         return m_pageUrl;
      }

      public List<TrackEntry> getTracks() {
         return m_tracks;
      }

      public int getVisits() {
         return m_visits;
      }

      public synchronized void increaseVisit(Payload payload) {
         String key = getKey(payload);
         LogEntry log = m_logs.get(key);

         if (log == null) {
            synchronized (m_logs) {
               log = m_logs.get(key);

               if (log == null) {
                  log = new LogEntry(payload);
                  m_logs.put(key, log);
               }
            }
         }

         m_visits++;
         log.increaseVisit();
      }

      public boolean isDirty() {
         return m_lastVisits != m_visits;
      }

      public void setLastVisits(int lastVisits) {
         m_lastVisits = lastVisits;
      }

      public void setPageId(int pageId) {
         m_pageId = pageId;
      }

      public void setPageUrl(String pageUrl) {
         m_pageUrl = pageUrl;
      }

      public void setVisits(int visits) {
         m_visits = visits;
      }
   }

   static final class LogEntry {
      private String m_fromPage;

      private int m_category1;

      private int m_category2;

      private boolean m_onTop;

      private int m_visits;

      public LogEntry(Payload payload) {
         m_fromPage = payload.getFromPage();
         m_category1 = payload.getCategory1();
         m_category2 = payload.getCategory2();
         m_onTop = payload.isOnTop();
      }

      public int getCategory1() {
         return m_category1;
      }

      public int getCategory2() {
         return m_category2;
      }

      public String getFromPage() {
         return m_fromPage;
      }

      public synchronized int getVisits() {
         return m_visits;
      }

      public synchronized void increaseVisit() {
         m_visits++;
      }

      public boolean isOnTop() {
         return m_onTop;
      }
   }

   final class Porter extends Thread {
      private long m_checkInterval;

      private volatile boolean m_shutdown;

      private void flush() {
         List<PageVisit> masters = new ArrayList<PageVisit>();
         List<PageVisitLog> logs = new ArrayList<PageVisitLog>();
         List<PageVisitTrack> tracks = new ArrayList<PageVisitTrack>();

         try {
            getUpdatedEntries(masters, logs, tracks);

            if (!masters.isEmpty()) {
               m_storage.batchUpdate(masters);
            }

            if (!logs.isEmpty()) {
               m_storage.batchUpdateLog(logs);
            }

            if (!tracks.isEmpty()) {
               m_storage.batchInsertTrack(tracks);
            }
         } catch (DalException e) {
            m_logger.error("Flush to database failure. " + e, e);
         }
      }

      @Override
      public void run() {
         try {
            while (!m_shutdown) {
               flush();

               sleep(m_checkInterval);
            }
         } catch (InterruptedException e) {
            // ignore it
         }
      }

      public void setCheckInterval(long checkInterval) {
         m_checkInterval = checkInterval;
      }

      public void shutdown() {
         m_shutdown = true;
      }

   }

   static final class TrackEntry {
      private String m_fromPage;
      private String m_clientId;
      private Date m_creationDate;

      public TrackEntry(Payload payload) {
         m_fromPage = payload.getFromPage();
         m_clientId = payload.getClientIp();
         m_creationDate = new Date();
      }

      public String getClientId() {
         return m_clientId;
      }

      public Date getCreationDate() {
         return m_creationDate;
      }

      public String getFromPage() {
         return m_fromPage;
      }
   }
}

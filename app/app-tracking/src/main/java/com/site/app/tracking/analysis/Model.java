package com.site.app.tracking.analysis;

import java.util.List;
import java.util.Map;

import com.site.app.tracking.dal.IpTable;
import com.site.app.tracking.dal.PageVisit;
import com.site.app.tracking.dal.PageVisitLog;
import com.site.app.tracking.dal.PageVisitTrack;
import com.site.app.tracking.dal.Stats;

public class Model {
   private PageVisit m_pageVisit;

   private List<PageVisitLog> m_pageVisitLogs;

   private List<PageVisitTrack> m_pageVisitTracks;

   private List<Stats> m_statsList;

   private Map<String, IpTable> m_ipTableMap;

   private DefaultDataSelector m_dataSelector;

   private int m_purgeTableCount;

   public int getPurgeTableCount() {
      return m_purgeTableCount;
   }

   public List<Stats> getStatsList() {
      return m_statsList;
   }

   public void setStatsList(List<Stats> statsList) {
      m_statsList = statsList;
   }

   public void setPurgeTableCount(int purgeTableCount) {
      m_purgeTableCount = purgeTableCount;
   }

   public DefaultDataSelector getDataSelector() {
      return m_dataSelector;
   }

   public IpTable getIpTable(String ip) {
      return m_ipTableMap == null ? null : m_ipTableMap.get(ip);
   }

   public PageVisit getPageVisit() {
      return m_pageVisit;
   }

   public List<PageVisitLog> getPageVisitLogs() {
      return m_pageVisitLogs;
   }

   public List<PageVisitTrack> getPageVisitTracks() {
      return m_pageVisitTracks;
   }

   public void setDataSelector(DefaultDataSelector dataSelector) {
      m_dataSelector = dataSelector;
   }

   public void setIpTableMap(Map<String, IpTable> ipTableMap) {
      m_ipTableMap = ipTableMap;
   }

   public void setPageVisit(PageVisit pageVisit) {
      m_pageVisit = pageVisit;
   }

   public void setPageVisitLogs(List<PageVisitLog> pageVisitLogs) {
      m_pageVisitLogs = pageVisitLogs;
   }

   public void setPageVisitTracks(List<PageVisitTrack> pageVisitTracks) {
      m_pageVisitTracks = pageVisitTracks;
   }

}

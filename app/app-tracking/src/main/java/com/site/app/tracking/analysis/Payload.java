package com.site.app.tracking.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.site.app.error.ErrorObject;

public class Payload {
   private Action m_action;

   // For search page & visit stats & ip stats
   private Date m_dateFrom;

   private Date m_dateTo;

   // For search page
   private TopNGroupBy m_topnGroupBy;

   private int m_maxNum;

   // For details page
   private String m_pageUrl;

   // For signin
   private String m_user;

   private String m_username;

   private String m_password;

   private String m_lastUrl;

   // For visit stats & ip stats
   private StatsTable m_statsTable;

   private StatsGroupBy m_statsGroupBy;

   // For purge table
   private PurgeTable m_purgeTable;

   private PurgeTableAction m_purgeTableAction;

   private List<ErrorObject> m_errors = new ArrayList<ErrorObject>();

   public Action getAction() {
      return m_action;
   }

   public Date getDateFrom() {
      return m_dateFrom;
   }

   public Date getDateTo() {
      return m_dateTo;
   }

   public List<ErrorObject> getErrors() {
      return m_errors;
   }

   public String getLastUrl() {
      return m_lastUrl;
   }

   public int getMaxNum() {
      return m_maxNum;
   }

   public String getPageUrl() {
      return m_pageUrl;
   }

   public String getPassword() {
      return m_password;
   }

   public PurgeTable getPurgeTable() {
      return m_purgeTable;
   }

   public PurgeTableAction getPurgeTableAction() {
      return m_purgeTableAction;
   }

   public StatsGroupBy getStatsGroupBy() {
      return m_statsGroupBy;
   }

   public StatsTable getStatsTable() {
      return m_statsTable;
   }

   public TopNGroupBy getTopnGroupBy() {
      return m_topnGroupBy;
   }

   public String getUser() {
      return m_user;
   }

   public String getUsername() {
      return m_username;
   }

   public void setAction(Action action) {
      m_action = action;
   }

   public void setDateFrom(Date dateFrom) {
      m_dateFrom = dateFrom;
   }

   public void setDateTo(Date dateTo) {
      m_dateTo = dateTo;
   }

   public void setLastUrl(String lastUrl) {
      m_lastUrl = lastUrl;
   }

   public void setMaxNum(int maxNum) {
      m_maxNum = maxNum;
   }

   public void setPageUrl(String pageUrl) {
      m_pageUrl = pageUrl;
   }

   public void setPassword(String password) {
      m_password = password;
   }

   public void setPurgeTable(PurgeTable purgeTable) {
      m_purgeTable = purgeTable;
   }

   public void setPurgeTableAction(PurgeTableAction purgeTableAction) {
      m_purgeTableAction = purgeTableAction;
   }

   public void setStatsGroupBy(StatsGroupBy intervalBy) {
      m_statsGroupBy = intervalBy;
   }

   public void setStatsTable(StatsTable statsTable) {
      m_statsTable = statsTable;
   }

   public void setTopnGroupBy(TopNGroupBy groupBy) {
      m_topnGroupBy = groupBy;
   }

   public void setUser(String user) {
      m_user = user;
   }

   public void setUsername(String username) {
      m_username = username;
   }
}

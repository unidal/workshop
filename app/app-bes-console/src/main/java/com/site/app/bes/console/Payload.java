package com.site.app.bes.console;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.site.app.error.ErrorObject;

public class Payload {
   private Action m_action;

   // Next for search page
   private Date m_dateFrom;

   private Date m_dateTo;

   // Next for signin
   private String m_user;

   private String m_username;

   private String m_password;

   private String m_lastUrl;

   // Next for event stats
   private StatsGroupBy m_statsGroupBy;

   List<ErrorObject> m_errors = new ArrayList<ErrorObject>();

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

   public StatsGroupBy getStatsGroupBy() {
      return m_statsGroupBy;
   }

   public String getLastUrl() {
      return m_lastUrl;
   }

   public String getPassword() {
      return m_password;
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

   public void setStatsGroupBy(StatsGroupBy intervalBy) {
      m_statsGroupBy = intervalBy;
   }

   public void setLastUrl(String lastUrl) {
      m_lastUrl = lastUrl;
   }

   public void setPassword(String password) {
      m_password = password;
   }

   public void setUser(String user) {
      m_user = user;
   }

   public void setUsername(String username) {
      m_username = username;
   }
}

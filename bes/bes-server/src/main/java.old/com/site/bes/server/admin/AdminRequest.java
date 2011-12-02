package com.site.bes.server.admin;

import com.site.web.page.PageRequest;

public class AdminRequest extends PageRequest {
   private AdminFormAction m_formAction;

   private AdminPageMode m_pageMode;

   private AdminSortedByEnum m_sortedBy;

   private int m_eventId;

   private String m_eventType;

   private String m_consumerType;

   private long m_batchTimeout;

   private int m_lastFetchedId;

   public long getBatchTimeout() {
      return m_batchTimeout;
   }

   public String getConsumerType() {
      return m_consumerType;
   }

   public int getEventId() {
      return m_eventId;
   }

   public String getEventType() {
      return m_eventType;
   }

   public AdminFormAction getFormAction() {
      return m_formAction;
   }

   public int getLastFetchedId() {
      return m_lastFetchedId;
   }

   public AdminPageMode getPageMode() {
      return m_pageMode;
   }

   public AdminSortedByEnum getSortedBy() {
      return m_sortedBy;
   }

   public void setBatchTimeout(long batchTimeout) {
      m_batchTimeout = batchTimeout;
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
   }

   public void setEventId(int eventId) {
      m_eventId = eventId;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setFormAction(AdminFormAction formAction) {
      m_formAction = formAction;
   }

   public void setLastFetchedId(int lastFetchedId) {
      m_lastFetchedId = lastFetchedId;
   }

   public void setPageMode(AdminPageMode pageMode) {
      m_pageMode = pageMode;
   }

   public void setSortedBy(AdminSortedByEnum sortedBy) {
      m_sortedBy = sortedBy;
   }

}

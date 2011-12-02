package com.site.bes.common.dal;

import com.site.kernel.dal.db.DataRow;

public class EventBatchLogDo extends DataRow {
   private int m_batchId;
   private String m_eventType;
   private String m_consumerType;
   private int m_startEventId;
   private int m_endEventId;
   private String m_consumerId;
   private java.util.Date m_creationDate;
   private int m_processStatus;
   private int m_fetchedRows;
   private int m_successCount;
   private int m_failureCount;
   private int m_fetchTime;
   private int m_maxWaitTime;
   private int m_avgWaitTime;
   private int m_maxProcessTime;
   private int m_avgProcessTime;

   private int m_keyBatchId;
   private int m_eventId;
   private java.util.Date m_deadline;

   static {
      init();
   }

   protected static void init() {
      initialize("event-batch-log", EventBatchLogEntity.class, EventBatchLogEntity.HINT_FIELDS);
   }

   protected EventBatchLogDo() {
   }

   public int getAvgProcessTime() {
      return m_avgProcessTime;
   }
   
   public int getAvgWaitTime() {
      return m_avgWaitTime;
   }
   
   public int getBatchId() {
      return m_batchId;
   }
   
   public String getConsumerId() {
      return m_consumerId;
   }
   
   public String getConsumerType() {
      return m_consumerType;
   }
   
   public java.util.Date getCreationDate() {
      return m_creationDate;
   }
   
   public java.util.Date getDeadline() {
      return m_deadline;
   }
   
   public int getEndEventId() {
      return m_endEventId;
   }
   
   public int getEventId() {
      return m_eventId;
   }
   
   public String getEventType() {
      return m_eventType;
   }
   
   public int getFailureCount() {
      return m_failureCount;
   }
   
   public int getFetchedRows() {
      return m_fetchedRows;
   }
   
   public int getFetchTime() {
      return m_fetchTime;
   }
   
   public int getKeyBatchId() {
      return m_keyBatchId;
   }
   
   public int getMaxProcessTime() {
      return m_maxProcessTime;
   }
   
   public int getMaxWaitTime() {
      return m_maxWaitTime;
   }
   
   public int getProcessStatus() {
      return m_processStatus;
   }
   
   public int getStartEventId() {
      return m_startEventId;
   }
   
   public int getSuccessCount() {
      return m_successCount;
   }
   
   public void setAvgProcessTime(int avgProcessTime) {
      m_avgProcessTime = avgProcessTime;
      setFieldUsed(EventBatchLogEntity.AVG_PROCESS_TIME, true);
   }
   
   public void setAvgWaitTime(int avgWaitTime) {
      m_avgWaitTime = avgWaitTime;
      setFieldUsed(EventBatchLogEntity.AVG_WAIT_TIME, true);
   }
   
   public void setBatchId(int batchId) {
      m_batchId = batchId;
      setFieldUsed(EventBatchLogEntity.BATCH_ID, true);
   }
   
   public void setConsumerId(String consumerId) {
      m_consumerId = consumerId;
      setFieldUsed(EventBatchLogEntity.CONSUMER_ID, true);
   }
   
   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
      setFieldUsed(EventBatchLogEntity.CONSUMER_TYPE, true);
   }
   
   public void setCreationDate(java.util.Date creationDate) {
      m_creationDate = creationDate;
      setFieldUsed(EventBatchLogEntity.CREATION_DATE, true);
   }
   
   public void setDeadline(java.util.Date deadline) {
      m_deadline = deadline;
      setFieldUsed(EventBatchLogEntity.DEADLINE, true);
   }
   
   public void setEndEventId(int endEventId) {
      m_endEventId = endEventId;
      setFieldUsed(EventBatchLogEntity.END_EVENT_ID, true);
   }
   
   public void setEventId(int eventId) {
      m_eventId = eventId;
      setFieldUsed(EventBatchLogEntity.EVENT_ID, true);
   }
   
   public void setEventType(String eventType) {
      m_eventType = eventType;
      setFieldUsed(EventBatchLogEntity.EVENT_TYPE, true);
   }
   
   public void setFailureCount(int failureCount) {
      m_failureCount = failureCount;
      setFieldUsed(EventBatchLogEntity.FAILURE_COUNT, true);
   }
   
   public void setFetchedRows(int fetchedRows) {
      m_fetchedRows = fetchedRows;
      setFieldUsed(EventBatchLogEntity.FETCHED_ROWS, true);
   }
   
   public void setFetchTime(int fetchTime) {
      m_fetchTime = fetchTime;
      setFieldUsed(EventBatchLogEntity.FETCH_TIME, true);
   }
   
   public void setKeyBatchId(int keyBatchId) {
      m_keyBatchId = keyBatchId;
      setFieldUsed(EventBatchLogEntity.KEY_BATCH_ID, true);
   }
   
   public void setMaxProcessTime(int maxProcessTime) {
      m_maxProcessTime = maxProcessTime;
      setFieldUsed(EventBatchLogEntity.MAX_PROCESS_TIME, true);
   }
   
   public void setMaxWaitTime(int maxWaitTime) {
      m_maxWaitTime = maxWaitTime;
      setFieldUsed(EventBatchLogEntity.MAX_WAIT_TIME, true);
   }
   
   public void setProcessStatus(int processStatus) {
      m_processStatus = processStatus;
      setFieldUsed(EventBatchLogEntity.PROCESS_STATUS, true);
   }
   
   public void setStartEventId(int startEventId) {
      m_startEventId = startEventId;
      setFieldUsed(EventBatchLogEntity.START_EVENT_ID, true);
   }
   
   public void setSuccessCount(int successCount) {
      m_successCount = successCount;
      setFieldUsed(EventBatchLogEntity.SUCCESS_COUNT, true);
   }
   
}

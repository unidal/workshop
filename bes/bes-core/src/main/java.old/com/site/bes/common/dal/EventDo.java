package com.site.bes.common.dal;

import com.site.kernel.dal.db.DataRow;

public class EventDo extends DataRow {
   private int m_eventId;
   private String m_eventType;
   private String m_payload;
   private int m_payloadType;
   private String m_refId;
   private String m_producerType;
   private String m_producerId;
   private int m_maxRetryTimes;
   private java.util.Date m_scheduleDate;
   private java.util.Date m_creationDate;
   private int m_maxEventId;

   private int m_keyEventId;
   private int m_startEventId;
   private int m_endEventId;
   private java.util.Date m_startScheduleDate;
   private java.util.Date m_endScheduleDate;
   private String m_consumerType;
   private String m_consumerId;
   private int m_batchId;

   static {
      init();
   }

   protected static void init() {
      initialize("event", EventEntity.class, EventEntity.HINT_FIELDS);
   }

   protected EventDo() {
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
   
   public int getEndEventId() {
      return m_endEventId;
   }
   
   public java.util.Date getEndScheduleDate() {
      return m_endScheduleDate;
   }
   
   public int getEventId() {
      return m_eventId;
   }
   
   public String getEventType() {
      return m_eventType;
   }
   
   public int getKeyEventId() {
      return m_keyEventId;
   }
   
   public int getMaxEventId() {
      return m_maxEventId;
   }
   
   public int getMaxRetryTimes() {
      return m_maxRetryTimes;
   }
   
   public String getPayload() {
      return m_payload;
   }
   
   public int getPayloadType() {
      return m_payloadType;
   }
   
   public String getProducerId() {
      return m_producerId;
   }
   
   public String getProducerType() {
      return m_producerType;
   }
   
   public String getRefId() {
      return m_refId;
   }
   
   public java.util.Date getScheduleDate() {
      return m_scheduleDate;
   }
   
   public int getStartEventId() {
      return m_startEventId;
   }
   
   public java.util.Date getStartScheduleDate() {
      return m_startScheduleDate;
   }
   
   public void setBatchId(int batchId) {
      m_batchId = batchId;
      setFieldUsed(EventEntity.BATCH_ID, true);
   }
   
   public void setConsumerId(String consumerId) {
      m_consumerId = consumerId;
      setFieldUsed(EventEntity.CONSUMER_ID, true);
   }
   
   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
      setFieldUsed(EventEntity.CONSUMER_TYPE, true);
   }
   
   public void setCreationDate(java.util.Date creationDate) {
      m_creationDate = creationDate;
      setFieldUsed(EventEntity.CREATION_DATE, true);
   }
   
   public void setEndEventId(int endEventId) {
      m_endEventId = endEventId;
      setFieldUsed(EventEntity.END_EVENT_ID, true);
   }
   
   public void setEndScheduleDate(java.util.Date endScheduleDate) {
      m_endScheduleDate = endScheduleDate;
      setFieldUsed(EventEntity.END_SCHEDULE_DATE, true);
   }
   
   public void setEventId(int eventId) {
      m_eventId = eventId;
      setFieldUsed(EventEntity.EVENT_ID, true);
   }
   
   public void setEventType(String eventType) {
      m_eventType = eventType;
      setFieldUsed(EventEntity.EVENT_TYPE, true);
   }
   
   public void setKeyEventId(int keyEventId) {
      m_keyEventId = keyEventId;
      setFieldUsed(EventEntity.KEY_EVENT_ID, true);
   }
   
   public void setMaxEventId(int maxEventId) {
      m_maxEventId = maxEventId;
      setFieldUsed(EventEntity.MAX_EVENT_ID, true);
   }
   
   public void setMaxRetryTimes(int maxRetryTimes) {
      m_maxRetryTimes = maxRetryTimes;
      setFieldUsed(EventEntity.MAX_RETRY_TIMES, true);
   }
   
   public void setPayload(String payload) {
      m_payload = payload;
      setFieldUsed(EventEntity.PAYLOAD, true);
   }
   
   public void setPayloadType(int payloadType) {
      m_payloadType = payloadType;
      setFieldUsed(EventEntity.PAYLOAD_TYPE, true);
   }
   
   public void setProducerId(String producerId) {
      m_producerId = producerId;
      setFieldUsed(EventEntity.PRODUCER_ID, true);
   }
   
   public void setProducerType(String producerType) {
      m_producerType = producerType;
      setFieldUsed(EventEntity.PRODUCER_TYPE, true);
   }
   
   public void setRefId(String refId) {
      m_refId = refId;
      setFieldUsed(EventEntity.REF_ID, true);
   }
   
   public void setScheduleDate(java.util.Date scheduleDate) {
      m_scheduleDate = scheduleDate;
      setFieldUsed(EventEntity.SCHEDULE_DATE, true);
   }
   
   public void setStartEventId(int startEventId) {
      m_startEventId = startEventId;
      setFieldUsed(EventEntity.START_EVENT_ID, true);
   }
   
   public void setStartScheduleDate(java.util.Date startScheduleDate) {
      m_startScheduleDate = startScheduleDate;
      setFieldUsed(EventEntity.START_SCHEDULE_DATE, true);
   }
   
}

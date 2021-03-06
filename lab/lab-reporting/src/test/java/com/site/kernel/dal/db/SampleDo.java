package com.site.kernel.dal.db;

public class SampleDo extends DataRow {
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
      initialize("event", SampleEntity.class, SampleEntity.HINT_FIELDS);
   }

   protected SampleDo() {
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
      setFieldUsed(SampleEntity.BATCH_ID, true);
   }

   public void setConsumerId(String consumerId) {
      m_consumerId = consumerId;
      setFieldUsed(SampleEntity.CONSUMER_ID, true);
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
      setFieldUsed(SampleEntity.CONSUMER_TYPE, true);
   }

   public void setCreationDate(java.util.Date creationDate) {
      m_creationDate = creationDate;
      setFieldUsed(SampleEntity.CREATION_DATE, true);
   }

   public void setEndEventId(int endEventId) {
      m_endEventId = endEventId;
      setFieldUsed(SampleEntity.END_EVENT_ID, true);
   }

   public void setEndScheduleDate(java.util.Date endScheduleDate) {
      m_endScheduleDate = endScheduleDate;
      setFieldUsed(SampleEntity.END_SCHEDULE_DATE, true);
   }

   public void setEventId(int eventId) {
      m_eventId = eventId;
      setFieldUsed(SampleEntity.EVENT_ID, true);
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
      setFieldUsed(SampleEntity.EVENT_TYPE, true);
   }

   public void setKeyEventId(int keyEventId) {
      m_keyEventId = keyEventId;
      setFieldUsed(SampleEntity.KEY_EVENT_ID, true);
   }

   public void setMaxEventId(int maxEventId) {
      m_maxEventId = maxEventId;
      setFieldUsed(SampleEntity.MAX_EVENT_ID, true);
   }

   public void setMaxRetryTimes(int maxRetryTimes) {
      m_maxRetryTimes = maxRetryTimes;
      setFieldUsed(SampleEntity.MAX_RETRY_TIMES, true);
   }

   public void setPayload(String payload) {
      m_payload = payload;
      setFieldUsed(SampleEntity.PAYLOAD, true);
   }

   public void setPayloadType(int payloadType) {
      m_payloadType = payloadType;
      setFieldUsed(SampleEntity.PAYLOAD_TYPE, true);
   }

   public void setProducerId(String producerId) {
      m_producerId = producerId;
      setFieldUsed(SampleEntity.PRODUCER_ID, true);
   }

   public void setProducerType(String producerType) {
      m_producerType = producerType;
      setFieldUsed(SampleEntity.PRODUCER_TYPE, true);
   }

   public void setRefId(String refId) {
      m_refId = refId;
      setFieldUsed(SampleEntity.REF_ID, true);
   }

   public void setScheduleDate(java.util.Date scheduleDate) {
      m_scheduleDate = scheduleDate;
      setFieldUsed(SampleEntity.SCHEDULE_DATE, true);
   }

   public void setStartEventId(int startEventId) {
      m_startEventId = startEventId;
      setFieldUsed(SampleEntity.START_EVENT_ID, true);
   }

   public void setStartScheduleDate(java.util.Date startScheduleDate) {
      m_startScheduleDate = startScheduleDate;
      setFieldUsed(SampleEntity.START_SCHEDULE_DATE, true);
   }

}

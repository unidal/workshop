package com.site.bes.server.admin.type;

import java.util.Date;

import com.site.bes.EventPayloadFormat;

public final class EventInfoType {
   private int m_eventId;

   private String m_eventType;

   private String m_producerType;

   private String m_producerId;

   private int m_maxRetryTimes;

   private Date m_creationDate;

   private String m_payload;

   private EventPayloadFormat m_payloadFormat;
   
   public Date getCreationDate() {
      return m_creationDate;
   }

   public int getEventId() {
      return m_eventId;
   }

   public String getEventType() {
      return m_eventType;
   }

   public int getMaxRetryTimes() {
      return m_maxRetryTimes;
   }

   public String getPayload() {
      return m_payload;
   }

   public EventPayloadFormat getPayloadFormat() {
      return m_payloadFormat;
   }

   public String getProducerId() {
      return m_producerId;
   }

   public String getProducerType() {
      return m_producerType;
   }

   public void setCreationDate(Date creationDate) {
      m_creationDate = creationDate;
   }

   public void setEventId(int eventId) {
      m_eventId = eventId;
   }

   public void setEventType(String eventType) {
      m_eventType = eventType;
   }

   public void setMaxRetryTimes(int maxRetryTimes) {
      m_maxRetryTimes = maxRetryTimes;
   }

   public void setPayload(String payload) {
      m_payload = payload;
   }

   public void setPayloadFormat(EventPayloadFormat payloadFormat) {
      m_payloadFormat = payloadFormat;
   }

   public void setProducerId(String producerId) {
      m_producerId = producerId;
   }

   public void setProducerType(String producerType) {
      m_producerType = producerType;
   }
}
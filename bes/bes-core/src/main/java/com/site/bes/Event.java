package com.site.bes;

import java.util.Date;

public class Event {
   private int m_id;

   private String m_type;

   private String m_refId;

   private int m_status;

   private String m_payload;

   private int m_payloadFormat;

   private String m_producer;

   private Date m_scheduleDate;

   private int m_maxRetryTimes;

   private Date m_creationDate;

   public int getId() {
      return m_id;
   }

   public String getType() {
      return m_type;
   }

   public String getRefId() {
      return m_refId;
   }

   public int getStatus() {
      return m_status;
   }

   public String getPayload() {
      return m_payload;
   }

   public int getPayloadFormat() {
      return m_payloadFormat;
   }

   public String getProducer() {
      return m_producer;
   }

   public Date getScheduleDate() {
      return m_scheduleDate;
   }

   public int getMaxRetryTimes() {
      return m_maxRetryTimes;
   }

   public Date getCreationDate() {
      return m_creationDate;
   }

   public void setId(int id) {
      m_id = id;
   }

   public void setType(String type) {
      m_type = type;
   }

   public void setRefId(String refId) {
      m_refId = refId;
   }

   public void setStatus(int status) {
      m_status = status;
   }

   public void setPayload(String payload) {
      m_payload = payload;
   }

   public void setPayloadFormat(int payloadFormat) {
      m_payloadFormat = payloadFormat;
   }

   public void setProducer(String producer) {
      m_producer = producer;
   }

   public void setScheduleDate(Date scheduleDate) {
      m_scheduleDate = scheduleDate;
   }

   public void setMaxRetryTimes(int maxRetryTimes) {
      m_maxRetryTimes = maxRetryTimes;
   }

   public void setCreationDate(Date creationDate) {
      m_creationDate = creationDate;
   }
}

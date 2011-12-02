package com.site.bes.server.admin.type;

import static com.site.kernel.dal.Cardinality.ZERO_TO_MANY;
import static com.site.kernel.dal.ValueType.LONG;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ELEMENT;

import java.util.List;

import com.site.kernel.dal.model.XmlModelField;
import com.site.web.WebModel;
import com.site.web.WebModelField;

public class ConsumerType extends WebModel {
   public static final XmlModelField CONSUMER_TYPE = new XmlModelField("consumer-type", ELEMENT, STRING);

   public static final XmlModelField STATUS = new XmlModelField("status", ELEMENT, STRING);

   public static final XmlModelField CHECK_INTERVAL = new XmlModelField("check-interval", ELEMENT, LONG);

   public static final WebModelField EVENTS = new WebModelField("events", ZERO_TO_MANY, EventType.class);

   private String m_consumerType;

   private String m_status;

   private long m_checkInterval;

   private List<EventType> m_events;

   static {
      init();
   }

   protected static void init() {
      initialize();
   }

   public ConsumerType() {
      super("consumer");
   }

   public long getCheckInterval() {
      return m_checkInterval;
   }

   public String getConsumerType() {
      return m_consumerType;
   }

   public List<EventType> getEventList() {
      return m_events;
   }

   public String getStatus() {
      return m_status;
   }

   public void setCheckInterval(long checkInterval) {
      m_checkInterval = checkInterval;
      setFieldUsed(CHECK_INTERVAL, true);
   }

   public void setConsumerType(String consumerType) {
      m_consumerType = consumerType;
      setFieldUsed(CONSUMER_TYPE, true);
   }

   public void setEventList(List<EventType> events) {
      m_events = events;
      setFieldUsed(EVENTS, true);
   }

   public void setStatus(String status) {
      m_status = status;
      setFieldUsed(STATUS, true);
   }

}

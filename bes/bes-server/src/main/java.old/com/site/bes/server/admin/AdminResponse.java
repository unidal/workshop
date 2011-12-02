package com.site.bes.server.admin;

import java.util.List;

import com.site.bes.server.admin.type.ConsumerType;
import com.site.bes.server.admin.type.ConsumptionType;
import com.site.bes.server.admin.type.DashboardType;
import com.site.bes.server.admin.type.EventInfoType;
import com.site.bes.server.admin.type.EventType;
import com.site.web.page.PageResponse;

public class AdminResponse extends PageResponse {
   private List<ConsumerType> m_consumerList;

   private List<EventType> m_eventList;

   private EventInfoType m_eventInfo;

   private List<ConsumptionType> m_consumptionList;

   private DashboardType m_dashboard;
   
   public List<ConsumerType> getConsumerList() {
      return m_consumerList;
   }

   public List<ConsumptionType> getConsumptionList() {
      return m_consumptionList;
   }

   public DashboardType getDashboard() {
      return m_dashboard;
   }

   public EventInfoType getEventInfo() {
      return m_eventInfo;
   }

   public List<EventType> getEventList() {
      return m_eventList;
   }

   public void setConsumerList(List<ConsumerType> consumerList) {
      m_consumerList = consumerList;
   }

   public void setConsumptionList(List<ConsumptionType> consumptionList) {
      m_consumptionList = consumptionList;
   }

   public void setDashboard(DashboardType dashboard) {
      m_dashboard = dashboard;
   }

   public void setEventInfo(EventInfoType event) {
      m_eventInfo = event;
   }

   public void setEventList(List<EventType> eventList) {
      m_eventList = eventList;
   }

}

package com.site.app.bes.console;

import java.util.List;

import com.site.app.bes.dal.Event;

public class Model {
   private List<Event> m_eventList;

   private StatsDataSelector m_dataSelector;

   public StatsDataSelector getDataSelector() {
      return m_dataSelector;
   }

   public List<Event> getEventList() {
      return m_eventList;
   }

   public void setDataSelector(StatsDataSelector dataSelector) {
      m_dataSelector = dataSelector;
   }

   public void setEventList(List<Event> eventList) {
      m_eventList = eventList;
   }
}

package com.site.app.bes.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.app.bes.dal.Event;
import com.site.app.bes.dal.EventPlus;

public class StatsDataSelector {
   private Map<Long, Map<Integer, Integer>> m_map = new HashMap<Long, Map<Integer, Integer>>();

   public StatsDataSelector(List<EventPlus> eventPlusList) {
      init(eventPlusList);
   }

   public int getEvents() {
      int events = 0;

      for (Map<Integer, Integer> map : m_map.values()) {
         for (Integer val : map.values()) {
            events += val;
         }
      }

      return events;
   }

   public int getEvents(Event e) {
      long key = e.getCreationDateIn15m() + e.getCreationDateIn1h() + e.getCreationDateIn1d();
      Map<Integer, Integer> map = m_map.get(key);
      int events = 0;

      if (map != null) {
         for (Integer val : map.values()) {
            events += val;
         }
      }

      return events;
   }

   public int getEvents(Event e, int eventState) {
      long key = e.getCreationDateIn15m() + e.getCreationDateIn1h() + e.getCreationDateIn1d();
      Map<Integer, Integer> map = m_map.get(key);
      Integer events = map != null ? map.get(eventState) : null;

      if (events != null) {
         return events;
      } else {
         return 0;
      }
   }

   public int getEventsByState(int eventState) {
      int events = 0;

      for (Map<Integer, Integer> map : m_map.values()) {
         Integer val = map.get(eventState);

         if (val != null) {
            events += val;
         }
      }

      return events;
   }

   private void init(List<EventPlus> eventPlusList) {
      for (EventPlus e : eventPlusList) {
         long key = e.getCreationDateIn15m() + e.getCreationDateIn1h() + e.getCreationDateIn1d();
         Map<Integer, Integer> map = m_map.get(key);

         if (map == null) {
            map = new HashMap<Integer, Integer>();
            m_map.put(key, map);
         }

         map.put(e.getEventState(), e.getEvents());
      }
   }
}

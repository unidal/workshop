package com.site.bes.queue.memcache.dashboard;

import com.site.bes.queue.memcache.Formatter;
import com.site.bes.queue.memcache.FormatterException;
import com.site.lookup.ContainerHolder;

public class DashboardFormatter extends ContainerHolder implements Formatter<Dashboard, Dashboard> {
   public String getId(Dashboard dashboard) throws FormatterException {
      return "d-" + dashboard.getEventType() + "-" + dashboard.getConsumerType();
   }

   public String getValue(Dashboard dashboard) throws FormatterException {
      return String.valueOf(dashboard.getLastEventId());
   }

   public Dashboard parseValue(String value) throws FormatterException {
      Dashboard dashboard = lookup(Dashboard.class);

      try {
         dashboard.setLastEventId(Integer.parseInt(value));
      } catch (NumberFormatException e) {
         throw new FormatterException("Error when parsing dashboard: " + value, e);
      }
      
      return dashboard;
   }
}

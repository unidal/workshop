package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.callback.NotificationCallbackHandler;
import org.unidal.ezsell.dal.NotificationDao;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

class CallbackConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(NotificationCallbackHandler.class)
            .req(NotificationDao.class));

      return all;
   }
}
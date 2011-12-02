package com.site.app.bes.console;

import java.util.ArrayList;
import java.util.List;

import com.site.app.bes.dal.EventDao;
import com.site.app.bes.dal.EventPlusDao;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class DefaultComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(PayloadProvider.class, DefaultPayloadProvider.class));
      all.add(C(Processor.class, DefaultProcessor.class).req(EventDao.class, EventPlusDao.class));
      all.add(C(Viewer.class, JspViewer.class));

      return all;
   }
}

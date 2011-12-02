package com.site.app.tracking.counter;

import java.util.ArrayList;
import java.util.List;

import com.site.app.tracking.dal.PageVisitDao;
import com.site.app.tracking.dal.PageVisitLogDao;
import com.site.app.tracking.dal.PageVisitTrackDao;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class CounterComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(Configuration.class));
      all.add(C(CategoryMapping.class, DefaultCategoryMapping.class).req(Configuration.class));
      all.add(C(ImageGenerator.class, DefaultImageGenerator.class));
      all.add(C(TestPage.class, DefaultTestPage.class).req(Configuration.class));
      all.add(C(PayloadProvider.class, DefaultPayloadProvider.class).req(Configuration.class, CategoryMapping.class));
      all.add(C(Storage.class, DatabaseStorage.class).req(PageVisitDao.class, PageVisitLogDao.class, PageVisitTrackDao.class));
      // all.add(C(Processor.class, SimpleProcessor.class).req(Storage.class));
      all.add(C(Processor.class, AdvancedProcessor.class).req(Configuration.class, Storage.class));

      return all;
   }
}

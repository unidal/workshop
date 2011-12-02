package com.site.app.tracking.analysis;

import java.util.ArrayList;
import java.util.List;

import com.site.app.tracking.dal.IpTableDao;
import com.site.app.tracking.dal.PageVisitDao;
import com.site.app.tracking.dal.PageVisitLogDao;
import com.site.app.tracking.dal.PageVisitTrackDao;
import com.site.app.tracking.dal.StatsDao;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class AnalysisComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(PayloadProvider.class, DefaultPayloadProvider.class));
      all.add(C(Processor.class, DefaultProcessor.class).req(PageVisitDao.class, PageVisitLogDao.class,
               PageVisitTrackDao.class, IpTableDao.class, StatsDao.class));
      all.add(C(Viewer.class, JspViewer.class));

      return all;
   }
}

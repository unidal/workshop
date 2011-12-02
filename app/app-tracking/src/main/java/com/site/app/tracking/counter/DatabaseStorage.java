package com.site.app.tracking.counter;

import java.util.List;

import com.site.app.tracking.dal.PageVisit;
import com.site.app.tracking.dal.PageVisitDao;
import com.site.app.tracking.dal.PageVisitEntity;
import com.site.app.tracking.dal.PageVisitLog;
import com.site.app.tracking.dal.PageVisitLogDao;
import com.site.app.tracking.dal.PageVisitTrack;
import com.site.app.tracking.dal.PageVisitTrackDao;
import com.site.dal.jdbc.DalException;

public class DatabaseStorage implements Storage {
   private PageVisitDao m_analyticsDao;

   private PageVisitLogDao m_analyticsLogDao;
   
   private PageVisitTrackDao m_analyticsTrackDao;

   public PageVisit get(String pageUrl) throws DalException {
      PageVisit analytics = null;

      try {
         analytics = m_analyticsDao.findByPageUrl(pageUrl, PageVisitEntity.READSET_FULL);
      } catch (DalException e) {
         // ignore it
      }

      if (analytics == null) {
         // if not found, then we need to insert a empty record into database
         analytics = m_analyticsDao.createLocal();

         analytics.setPageUrl(pageUrl);
         analytics.setTotalVisits(0);
         m_analyticsDao.insert(analytics);
      }

      return analytics;
   }

   public void update(PageVisit protoDo) throws DalException {
      m_analyticsDao.updateTotalVisitsByPK(protoDo, PageVisitEntity.UPDATESET_FULL);
   }

   public void batchUpdate(List<PageVisit> masters) throws DalException {
      PageVisit[] protoDos = masters.toArray(new PageVisit[0]);

      m_analyticsDao.updateTotalVisitsByPK(protoDos, PageVisitEntity.UPDATESET_FULL);
   }

   public void updateLog(PageVisitLog protoDo) throws DalException {
      m_analyticsLogDao.insert(protoDo);
   }

   public void batchUpdateLog(List<PageVisitLog> logs) throws DalException {
      PageVisitLog[] protoDos = logs.toArray(new PageVisitLog[0]);

      m_analyticsLogDao.insert(protoDos);
   }

   public void insertTrack(PageVisitTrack analyticsTrack) throws DalException {
      m_analyticsTrackDao.insert(analyticsTrack);
   }

   public void batchInsertTrack(List<PageVisitTrack> tracks) throws DalException {
      PageVisitTrack[] protoDos = tracks.toArray(new PageVisitTrack[0]);

      m_analyticsTrackDao.insert(protoDos);
   }
}

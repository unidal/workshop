package com.site.app.tracking.counter;

import java.util.List;

import com.site.app.tracking.dal.PageVisit;
import com.site.app.tracking.dal.PageVisitLog;
import com.site.app.tracking.dal.PageVisitTrack;
import com.site.dal.jdbc.DalException;

public interface Storage {
   public PageVisit get(String pageUrl) throws DalException;

   public void update(PageVisit master) throws DalException;

   public void updateLog(PageVisitLog log) throws DalException;

   public void batchUpdate(List<PageVisit> masters) throws DalException;

   public void batchUpdateLog(List<PageVisitLog> logs) throws DalException;

   public void insertTrack(PageVisitTrack analyticsTrack) throws DalException;

   public void batchInsertTrack(List<PageVisitTrack> tracks) throws DalException;
}

package com.site.app.tracking.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.app.tracking.dal.IpTable;
import com.site.app.tracking.dal.IpTableDao;
import com.site.app.tracking.dal.IpTableEntity;
import com.site.app.tracking.dal.PageVisit;
import com.site.app.tracking.dal.PageVisitDao;
import com.site.app.tracking.dal.PageVisitEntity;
import com.site.app.tracking.dal.PageVisitLog;
import com.site.app.tracking.dal.PageVisitLogDao;
import com.site.app.tracking.dal.PageVisitLogEntity;
import com.site.app.tracking.dal.PageVisitTrack;
import com.site.app.tracking.dal.PageVisitTrackDao;
import com.site.app.tracking.dal.PageVisitTrackEntity;
import com.site.app.tracking.dal.Stats;
import com.site.app.tracking.dal.StatsDao;
import com.site.app.tracking.dal.StatsEntity;
import com.site.dal.jdbc.DalException;

public class DefaultProcessor implements Processor {
   private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

   private PageVisitDao m_dao;

   private PageVisitLogDao m_logDao;

   private PageVisitTrackDao m_trackDao;

   private IpTableDao m_ipTableDao;

   private StatsDao m_statsDao;

   private long getIpAddress(String clientIp) {
      long ipAddress = 0;
      long section = 0;

      if (clientIp != null) {
         int len = clientIp.length();

         for (int i = 0; i < len; i++) {
            char ch = clientIp.charAt(i);

            if (ch == '.') {
               ipAddress = ipAddress * 256 + section;
               section = 0;
            } else {
               section = section * 10 + (ch - '0');
            }
         }
      }

      ipAddress = ipAddress * 256 + section;

      return ipAddress;
   }

   private Map<String, IpTable> getIpTableMapFromTrack(List<PageVisitTrack> pageVisitTracks) {
      Map<String, IpTable> map = new HashMap<String, IpTable>();

      for (PageVisitTrack pageVisitTrack : pageVisitTracks) {
         String clientIp = pageVisitTrack.getClientIp();

         if (!map.containsKey(clientIp)) {
            long ipAddress = getIpAddress(clientIp);
            IpTable ipTable;

            try {
               ipTable = m_ipTableDao.findByIpAddress(ipAddress, IpTableEntity.READSET_FULL);
            } catch (DalException e) {
               ipTable = m_ipTableDao.createLocal();
            }

            map.put(clientIp, ipTable);
         }
      }

      return map;
   }

   private Map<String, IpTable> getIpTableMapFromStats(List<Stats> statsList) {
      Map<String, IpTable> map = new HashMap<String, IpTable>();

      for (Stats stats : statsList) {
         String clientIp = stats.getClientIp();

         if (!map.containsKey(clientIp)) {
            long ipAddress = getIpAddress(clientIp);
            IpTable ipTable;

            try {
               ipTable = m_ipTableDao.findByIpAddress(ipAddress, IpTableEntity.READSET_FULL);
            } catch (DalException e) {
               ipTable = m_ipTableDao.createLocal();
            }

            map.put(clientIp, ipTable);
         }
      }

      return map;
   }

   public Model process(Payload payload) {
      Model model = new Model();
      Action action = payload.getAction();

      try {
         switch (action) {
         case SIGN_IN:
            showSigninPage(payload, model);
            break;
         case MAIN_PAGE:
            showMainPage(payload, model);
            break;
         case SEARCH_TOP_N:
            showSearchTopN(payload, model);
            break;
         case PAGE_DETAIL:
            showPageDetail(payload, model);
            break;
         case VISIT_STATS:
            showVisitStats(payload, model);
            break;
         case IP_STATS:
            showIpStats(payload, model);
            break;
         case PURGE_TABLE:
            showPurgeTable(payload, model);
            break;
         }
      } catch (DalException e) {
         e.printStackTrace();
      }

      return model;
   }

   private void showIpStats(Payload payload, Model model) throws DalException {
      if (payload.getErrors().isEmpty()) {
         StatsTable table = payload.getStatsTable();
         PageVisit pageVisit = m_dao.createLocal();

         model.setPageVisit(pageVisit);

         switch (table) {
         case PAGE_VISIT_TRACK:
            List<PageVisitTrack> tracks = m_trackDao.findAllByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  PageVisitTrackEntity.READSET_T);

            if (tracks.size() == 1) {
               pageVisit.setTotalVisits(tracks.get(0).getTotalVisits());
            }

            List<PageVisitTrack> pageVisitTracks = null;

            switch (payload.getStatsGroupBy()) {
            case FIFTEEN_MINUTES:
               pageVisitTracks = m_trackDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     PageVisitTrackEntity.READSET_CCT_15M);
               break;
            case THIRTY_MINUTES:
               pageVisitTracks = m_trackDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     PageVisitTrackEntity.READSET_CCT_30M);
               break;
            case HOUR:
               pageVisitTracks = m_trackDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     PageVisitTrackEntity.READSET_CCT_1H);
               break;
            case DAY:
               pageVisitTracks = m_trackDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     PageVisitTrackEntity.READSET_CCT_1D);
               break;
            }

            model.setPageVisitTracks(pageVisitTracks);
            model.setIpTableMap(getIpTableMapFromTrack(pageVisitTracks));
            break;
         case STATS:
            List<Stats> stats = m_statsDao.findAllByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  StatsEntity.READSET_C);

            if (stats.size() == 1) {
               pageVisit.setTotalVisits(stats.get(0).getCount());
            }

            List<Stats> statsList = null;

            switch (payload.getStatsGroupBy()) {
            case FIFTEEN_MINUTES:
               statsList = m_statsDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     StatsEntity.READSET_CPCC_15M);
               break;
            case THIRTY_MINUTES:
               statsList = m_statsDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     StatsEntity.READSET_CPCC_30M);
               break;
            case HOUR:
               statsList = m_statsDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     StatsEntity.READSET_CPCC_1H);
               break;
            case DAY:
               statsList = m_statsDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                     StatsEntity.READSET_CPCC_1D);
               break;
            }

            model.setStatsList(statsList);
            model.setIpTableMap(getIpTableMapFromStats(statsList));
            break;
         }
      } else {
         // setup default values if necessary
         if (payload.getDateFrom() == null) {
            payload.setDateFrom(new Date());
         }

         if (payload.getDateTo() == null) {
            payload.setDateTo(new Date());
         }
      }
   }

   private void showPurgeTable(Payload payload, Model model) throws DalException {
      if (payload.getErrors().isEmpty()) {
         switch (payload.getPurgeTableAction()) {
         case CHECK:
            switch (payload.getPurgeTable()) {
            case PAGE_VISIT_TRACK:
               List<PageVisitTrack> tracks = m_trackDao.findAllByDateRange(new Date(0), payload.getDateTo(),
                     PageVisitTrackEntity.READSET_C);

               if (tracks.size() > 0) {
                  model.setPurgeTableCount(tracks.get(0).getCount());
               }

               break;
            case STATS:
               List<Stats> stats = m_statsDao.findAllByDateRange(new Date(0), payload.getDateTo(),
                     StatsEntity.READSET_C);

               if (stats.size() > 0) {
                  model.setPurgeTableCount(stats.get(0).getCount());
               }

               break;
            }

            break;
         case PURGE:
            switch (payload.getPurgeTable()) {
            case PAGE_VISIT_TRACK:
               PageVisitTrack track = new PageVisitTrack();

               track.setDateTo(payload.getDateTo());
               model.setPurgeTableCount(m_trackDao.deleteAllByEndOfDate(track));
               break;
            case STATS:
               Stats stats = new Stats();

               stats.setDateTo(payload.getDateTo());
               model.setPurgeTableCount(m_statsDao.deleteAllByEndOfDate(stats));
               break;
            }
            break;
         }
      } else {
         // setup default values if necessary
         if (payload.getDateTo() == null) {
            payload.setDateTo(new Date(System.currentTimeMillis() - 30 * ONE_DAY));
         }
      }
   }

   private void showMainPage(Payload payload, Model model) {
      // Do nothing
   }

   private void showPageDetail(Payload payload, Model model) {
      if (payload.getErrors().isEmpty()) {
         try {
            PageVisit pageVisit = m_dao.findByPageUrl(payload.getPageUrl(), PageVisitEntity.READSET_FULL);
            List<PageVisit> list = m_dao.findAllByGroup(payload.getPageUrl(), PageVisitEntity.READSET_CCO_T);
            List<PageVisitLog> pageVisitLogs = new ArrayList<PageVisitLog>(list.size());

            for (PageVisit e : list) {
               pageVisitLogs.add(e.getPageVisitLog());
            }

            DefaultDataSelector dataSelector = new DefaultDataSelector();

            dataSelector.setPageVisitLogs(pageVisitLogs);

            model.setPageVisit(pageVisit);
            model.setDataSelector(dataSelector);
         } catch (DalException e) {
            e.printStackTrace();
         }
      }
   }

   private void showSearchTopN(Payload payload, Model model) throws DalException {
      if (payload.getErrors().isEmpty()) {
         List<PageVisitLog> list = m_logDao.findAllByDateRange(payload.getDateFrom(), payload.getDateTo(),
               PageVisitLogEntity.READSET_T);
         PageVisit pageVisit = m_dao.createLocal();

         if (list.size() == 1) {
            pageVisit.setTotalVisits(list.get(0).getTotalVisits());
         }

         List<PageVisitLog> pageVisitLogs = null;

         switch (payload.getTopnGroupBy()) {
         case PAGE:
            pageVisitLogs = m_logDao.findTopNPageByDateRange(payload.getDateFrom(), payload.getDateTo(), payload
                  .getMaxNum(), PageVisitLogEntity.READSET_FULL_T_FULL);
            break;
         case SOURCE:
            pageVisitLogs = m_logDao.findTopNByDateRange(payload.getDateFrom(), payload.getDateTo(), payload
                  .getMaxNum(), PageVisitLogEntity.READSET_T_F);
            break;
         case CATEGORY1:
            pageVisitLogs = m_logDao.findTopNByDateRange(payload.getDateFrom(), payload.getDateTo(), payload
                  .getMaxNum(), PageVisitLogEntity.READSET_T_C1);
            break;
         case CATEGORY2:
            pageVisitLogs = m_logDao.findTopNByDateRange(payload.getDateFrom(), payload.getDateTo(), payload
                  .getMaxNum(), PageVisitLogEntity.READSET_T_C2);
            break;
         case ON_TOP:
            pageVisitLogs = m_logDao.findTopNByDateRange(payload.getDateFrom(), payload.getDateTo(), payload
                  .getMaxNum(), PageVisitLogEntity.READSET_T_O);
            break;
         }

         model.setPageVisit(pageVisit);
         model.setPageVisitLogs(pageVisitLogs);
      } else {
         // setup default values if necessary
         if (payload.getDateFrom() == null) {
            payload.setDateFrom(new Date());
         }

         if (payload.getDateTo() == null) {
            payload.setDateTo(new Date());
         }
      }
   }

   private void showSigninPage(Payload payload, Model model) {
      if ("admin".equals(payload.getUsername()) && "password".equals(payload.getPassword())) {
         payload.setUser(payload.getUsername());
      }
   }

   private void showVisitStats(Payload payload, Model model) throws DalException {
      List<PageVisitLog> list = m_logDao.findAllByDateRange(payload.getDateFrom(), payload.getDateTo(),
            PageVisitLogEntity.READSET_T);
      PageVisit pageVisit = m_dao.createLocal();

      if (list.size() == 1) {
         pageVisit.setTotalVisits(list.get(0).getTotalVisits());
      }

      if (payload.getErrors().isEmpty()) {
         List<PageVisitLog> pageVisitLogs = null;

         switch (payload.getStatsGroupBy()) {
         case FIFTEEN_MINUTES:
            pageVisitLogs = m_logDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  PageVisitLogEntity.READSET_CT_15M);
            break;
         case THIRTY_MINUTES:
            pageVisitLogs = m_logDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  PageVisitLogEntity.READSET_CT_30M);
            break;
         case HOUR:
            pageVisitLogs = m_logDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  PageVisitLogEntity.READSET_CT_1H);
            break;
         case DAY:
            pageVisitLogs = m_logDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  PageVisitLogEntity.READSET_CT_1D);
            break;
         }

         model.setPageVisit(pageVisit);
         model.setPageVisitLogs(pageVisitLogs);
      } else {
         // setup default values if necessary
         if (payload.getDateFrom() == null) {
            payload.setDateFrom(new Date());
         }

         if (payload.getDateTo() == null) {
            payload.setDateTo(new Date());
         }
      }
   }
}

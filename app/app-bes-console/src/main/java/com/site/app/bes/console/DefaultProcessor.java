package com.site.app.bes.console;

import java.util.Date;
import java.util.List;

import com.site.app.bes.dal.EventDao;
import com.site.app.bes.dal.Event;
import com.site.app.bes.dal.EventEntity;
import com.site.app.bes.dal.EventPlusDao;
import com.site.app.bes.dal.EventPlus;
import com.site.app.bes.dal.EventPlusEntity;
import com.site.dal.DalException;

public class DefaultProcessor implements Processor {
   private EventDao m_dao;

   private EventPlusDao m_plusDao;

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
         case EVENT_STATS:
            showEventStats(payload, model);
            break;
         case DAILY_REPORT:
            showDailyReport(payload, model);
            break;
         }
      } catch (DalException e) {
         e.printStackTrace();
      }

      return model;
   }

   private void showDailyReport(Payload payload, Model model) throws DalException {
      if (payload.getErrors().isEmpty()) {
         List<Event> eventList = null;
         List<EventPlus> eventPlusList = null;

         eventList = m_dao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(), EventEntity.READSET_CE_1D);
         eventPlusList = m_plusDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
               EventPlusEntity.READSET_CEE_1D);

         model.setEventList(eventList);
         model.setDataSelector(new StatsDataSelector(eventPlusList));
      }
   }

   private void showEventStats(Payload payload, Model model) throws DalException {
      if (payload.getErrors().isEmpty()) {
         List<Event> eventList = null;
         List<EventPlus> eventPlusList = null;

         switch (payload.getStatsGroupBy()) {
         case FIFTY_MINUTES:
            eventList = m_dao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(), EventEntity.READSET_CE_15M);
            eventPlusList = m_plusDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  EventPlusEntity.READSET_CEE_15M);
            break;
         case HOUR:
            eventList = m_dao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(), EventEntity.READSET_CE_1H);
            eventPlusList = m_plusDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  EventPlusEntity.READSET_CEE_1H);
            break;
         case DAY:
            eventList = m_dao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(), EventEntity.READSET_CE_1D);
            eventPlusList = m_plusDao.groupByDateRange(payload.getDateFrom(), payload.getDateTo(),
                  EventPlusEntity.READSET_CEE_1D);
            break;
         }

         model.setEventList(eventList);
         model.setDataSelector(new StatsDataSelector(eventPlusList));
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

   private void showMainPage(Payload payload, Model model) {
      // Do nothing
   }

   private void showSigninPage(Payload payload, Model model) {
      if ("admin".equals(payload.getUsername()) && "password".equals(payload.getPassword())) {
         payload.setUser(payload.getUsername());
      }
   }
}

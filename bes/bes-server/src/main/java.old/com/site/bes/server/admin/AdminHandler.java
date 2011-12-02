package com.site.bes.server.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.site.bes.EventPayloadFormat;
import com.site.bes.EventState;
import com.site.bes.common.dal.EventBatchLogDao;
import com.site.bes.common.dal.EventBatchLogDo;
import com.site.bes.common.dal.EventBatchLogEntity;
import com.site.bes.common.dal.EventDao;
import com.site.bes.common.dal.EventDashboardDao;
import com.site.bes.common.dal.EventDashboardDo;
import com.site.bes.common.dal.EventDashboardEntity;
import com.site.bes.common.dal.EventDo;
import com.site.bes.common.dal.EventEntity;
import com.site.bes.common.dal.EventPlusDao;
import com.site.bes.common.dal.EventPlusDo;
import com.site.bes.common.dal.EventPlusEntity;
import com.site.bes.engine.config.ConsumerBo;
import com.site.bes.engine.config.ConsumersBo;
import com.site.bes.engine.config.EventBo;
import com.site.bes.engine.config.ListenOnBo;
import com.site.bes.server.BesServlet;
import com.site.bes.server.admin.type.ConsumerType;
import com.site.bes.server.admin.type.ConsumptionType;
import com.site.bes.server.admin.type.DashboardType;
import com.site.bes.server.admin.type.EventInfoType;
import com.site.bes.server.admin.type.EventType;
import com.site.kernel.SystemRegistry;
import com.site.kernel.dal.DalException;
import com.site.web.page.PageContext;

public class AdminHandler {
   private static final AdminHandler s_instance = new AdminHandler();

   private static final String ENABLED = "Enabled";

   private static final String DISABLED = "Disabled";

   public static final AdminHandler getInstance() {
      return s_instance;
   }

   private ConsumersBo m_consumers;

   private AdminHandler() {
      try {
         BesServlet bes = (BesServlet) SystemRegistry.lookup(HttpServlet.class, BesServlet.class);

         m_consumers = bes.getConsumers();
      } catch (RuntimeException e) {
         throw new RuntimeException("Please make sure BesServlet is configured and initialized correctly.", e);
      }
   }

   private ConsumptionType findConsumer(List<ConsumptionType> consumers, EventPlusDo event) {
      for (ConsumptionType consumer : consumers) {
         if (consumer.getConsumerType().equals(event.getConsumerType())) {
            return consumer;
         }
      }

      return null;
   }

   private List<ConsumerType> getConsumerList(List<ConsumerBo> bos) {
      List<ConsumerType> consumerList = new ArrayList<ConsumerType>();

      for (ConsumerBo consumerBo : bos) {
         ConsumerType consumer = new ConsumerType();

         consumer.setConsumerType(consumerBo.getType());
         consumer.setStatus(consumerBo.isEnabled() ? ENABLED : DISABLED);
         consumer.setCheckInterval(consumerBo.getCheckInterval());

         ListenOnBo listenOn = consumerBo.getListenOnBo();
         List<EventType> eventList = new ArrayList<EventType>();

         if (listenOn != null) {
            List<EventBo> events = listenOn.getEventBos();

            for (EventBo eventBo : events) {
               EventType event = new EventType();

               event.setEventType(eventBo.getType());
               eventList.add(event);
            }
         }

         consumer.setEventList(eventList);
         consumerList.add(consumer);
      }

      return consumerList;
   }

   private List<EventType> getEventList(List<ConsumerBo> bos) {
      List<EventType> eventList = new ArrayList<EventType>();

      for (ConsumerBo consumerBo : bos) {
         ListenOnBo listenOn = consumerBo.getListenOnBo();

         if (listenOn != null) {
            List<EventBo> events = listenOn.getEventBos();

            for (EventBo eventBo : events) {
               EventType event = null;

               for (EventType e : eventList) {
                  if (e.getEventType().equals(eventBo.getType())) {
                     event = e;
                     break;
                  }
               }

               if (event == null) {
                  event = new EventType();

                  event.setEventType(eventBo.getType());
                  event.setConsumerList(new ArrayList<ConsumerType>());
                  eventList.add(event);
               }

               ConsumerType consumer = new ConsumerType();

               consumer.setConsumerType(consumerBo.getType());
               consumer.setStatus(consumerBo.isEnabled() ? ENABLED : DISABLED);
               consumer.setCheckInterval(consumerBo.getCheckInterval());

               event.getConsumerList().add(consumer);
            }
         }
      }

      return eventList;
   }

   public void handleEvent(PageContext ctx, AdminRequest req, AdminResponse res) {
      try {
         int eventId = req.getEventId();

         res.setEventInfo(loadEventInfo(eventId));
         res.setConsumptionList(loadConsumptionList(eventId));
      } catch (DalException e) {
         ctx.addError(AdminErrorId.EVENT_NOT_FOUND, null, e);
      }

      res.setResponseStatus(AdminResponseCode.EVENT);
   }

   public void handleDashboard(PageContext ctx, AdminRequest req, AdminResponse res) {
      AdminPageMode pageMode = req.getPageMode();

      if (pageMode == AdminPageMode.SUBMIT) {
         if (req.getLastFetchedId() == 0) {
            // max event id will be used by creating new dashboard entry
            int maxEventId = 0;
            List events = EventDao.getInstance().findByEventType(req.getEventType(), EventEntity.READSET_MAX_EVENT_ID);

            if (events.size() > 0) {
               EventDo event = (EventDo) events.get(0);

               maxEventId = event.getMaxEventId();
            }

            req.setLastFetchedId(maxEventId);
         }

         EventDashboardDo dashboard = EventDashboardDao.getInstance().createLocal();

         dashboard.setEventType(req.getEventType());
         dashboard.setConsumerType(req.getConsumerType());
         dashboard.setLastFetchedId(req.getLastFetchedId());
         dashboard.setBatchTimeout(req.getBatchTimeout());

         try {
            EventDashboardDao.getInstance().upsert(dashboard);
         } catch (DalException e) {
            ctx.addError(AdminErrorId.DAL_EXCEPTION, null, e);
         }
      } else if (pageMode == AdminPageMode.START) {
         try {
            m_consumers.startThread(req.getEventType(), req.getConsumerType());
         } catch (RuntimeException e) {
            ctx.addError(AdminErrorId.RUNTIME_EXCEPTION, null, e);
         }
      } else if (pageMode == AdminPageMode.STOP) {
         try {
            m_consumers.stopThread(req.getEventType(), req.getConsumerType());
         } catch (RuntimeException e) {
            ctx.addError(AdminErrorId.RUNTIME_EXCEPTION, null, e);
         }
      }

      if (!ctx.hasErrors()) {
         try {
            EventDashboardDo d = EventDashboardDao.getInstance().findByPK(req.getEventType(), req.getConsumerType(), EventDashboardEntity.READSET_FULL);
            DashboardType dashboard = new DashboardType();

            dashboard.setEventType(d.getEventType());
            dashboard.setConsumerType(d.getConsumerType());
            dashboard.setLastFetchedId(d.getLastFetchedId());
            dashboard.setLastScheduledDate(d.getLastScheduledDate());
            dashboard.setBatchTimeout(d.getBatchTimeout());
            dashboard.setCreationDate(d.getCreationDate());
            dashboard.setLastModifiedDate(d.getLastModifiedDate());
            dashboard.setRunning(m_consumers.isThreadRunning(req.getEventType(), req.getConsumerType()));

            res.setDashboard(dashboard);
         } catch (DalException e) {
            ctx.addError(AdminErrorId.DASHBOARD_NOT_FOUND, null, e);
         }
      }

      res.setResponseStatus(AdminResponseCode.DASHBOARD);
   }

   public void handleList(PageContext ctx, AdminRequest req, AdminResponse res) {
      AdminSortedByEnum sortedBy = req.getSortedBy();
      List<ConsumerBo> consumerBos = m_consumers.getConsumerBos();

      if (sortedBy == AdminSortedByEnum.CONSUMER) {
         res.setConsumerList(getConsumerList(consumerBos));
      } else if (sortedBy == AdminSortedByEnum.EVENT) {
         res.setEventList(getEventList(consumerBos));
      }

      res.setResponseStatus(AdminResponseCode.LIST);
   }

   private List<ConsumptionType> loadConsumptionList(int eventId) {
      List batchLogs = EventBatchLogDao.getInstance().findAllByEventId(eventId, EventBatchLogEntity.READSET_FULL);
      List<ConsumptionType> consumers = new ArrayList<ConsumptionType>();

      for (int i = 0; i < batchLogs.size(); i++) {
         EventBatchLogDo batchLog = (EventBatchLogDo) batchLogs.get(i);
         ConsumptionType consumer = new ConsumptionType();

         consumer.setEventId(eventId);
         consumer.setEventType(batchLog.getEventType());
         consumer.setConsumerType(batchLog.getConsumerType());
         consumer.setConsumerId(batchLog.getConsumerId());
         consumer.setEventState(EventState.COMPLETED);
         consumer.setRetriedTimes(0);
         consumer.setNextScheduleDate(null);
         consumers.add(consumer);
      }

      List events = EventPlusDao.getInstance().findAllByEventId(eventId, EventPlusEntity.READSET_FULL);
      for (int i = 0; i < events.size(); i++) {
         EventPlusDo event = (EventPlusDo) events.get(i);
         ConsumptionType consumer = findConsumer(consumers, event);

         if (consumer != null) {
            consumer.setConsumerId(event.getConsumerId());
            consumer.setEventState(EventState.getById(event.getEventState()));
            consumer.setRetriedTimes(event.getRetriedTimes());
            consumer.setNextScheduleDate(event.getNextScheduleDate());
         } else {
            consumer = new ConsumptionType();

            consumer.setEventId(eventId);
            consumer.setEventType(event.getEventType());
            consumer.setConsumerType(event.getConsumerType());
            consumer.setConsumerId(event.getConsumerId());
            consumer.setEventState(EventState.getById(event.getEventState()));
            consumer.setRetriedTimes(event.getRetriedTimes());
            consumer.setNextScheduleDate(event.getNextScheduleDate());

            consumers.add(consumer);
         }
      }

      return consumers;
   }

   private EventInfoType loadEventInfo(int eventId) throws DalException {
      EventDo event = EventDao.getInstance().findByPK(eventId, EventEntity.READSET_FULL);
      EventInfoType e = new EventInfoType();

      e.setEventId(event.getEventId());
      e.setEventType(event.getEventType());
      e.setMaxRetryTimes(event.getMaxRetryTimes());
      e.setProducerType(event.getProducerType());
      e.setProducerId(event.getProducerId());
      e.setCreationDate(event.getCreationDate());
      e.setPayload(event.getPayload());
      e.setPayloadFormat(EventPayloadFormat.getById(event.getPayloadType()));

      return e;
   }
}

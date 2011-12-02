package com.site.bes.queue;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.bes.Event;
import com.site.bes.EventException;
import com.site.bes.queue.mysql.EventTransformer;
import com.site.bes.queue.mysql.dal.EventDao;
import com.site.bes.queue.mysql.dal.EventEntity;
import com.site.dal.DalException;
import com.site.dal.jdbc.Ref;

public class MysqlEventQueue implements EventQueue, LogEnabled {
   private EventDao m_eventDao;

   private EventTransformer m_transformer;

   private Logger m_logger;

   public int add(Event event) throws EventException {
      try {
         com.site.bes.queue.mysql.dal.Event proto = m_transformer.convert(event);

         m_eventDao.insert(proto);

         m_logger.info("Added event(" + proto.getEventId() + ") with type(" + event.getType() + ") for producer("
               + event.getProducer() + ")");
         return proto.getEventId();
      } catch (DalException e) {
         throw new EventException("Error when adding event to mysql queue, " + e, e);
      }
   }

   public List<Event> poll(String eventType, String consumerType, String consumerId) throws EventException {
      Ref<Integer> batchId = new Ref<Integer>();

      try {
         List<com.site.bes.queue.mysql.dal.Event> protos = m_eventDao.fetchEvents(eventType, consumerType, consumerId, batchId,
               EventEntity.READSET_FETCH_EVENTS);
         List<Event> events = new ArrayList<Event>();

         for (com.site.bes.queue.mysql.dal.Event proto : protos) {
            Event event = m_transformer.convert(proto);

            event.setType(eventType);
            events.add(event);
         }

         if (events.size() > 0) {
            m_logger.info("fetched " + events.size() + " events in batch(" + batchId.get() + ") with type(" + eventType
                  + ") for consumer(" + consumerType + ", " + consumerId + ")");
         }

         return events;
      } catch (DalException e) {
         if (e.getMessage().contains("ResultSet is from UPDATE. No Data.")) {
            throw new EventException("Error when fetching events from mysql queue, maybe no dashboard created yet " + e, e);
         } else {
            throw new EventException("Error when fetching events from mysql queue, " + e, e);
         }
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void update(List<Event> events, String consumerType, String consumerId) {
      // TODO Auto-generated method stub
      
   }
}

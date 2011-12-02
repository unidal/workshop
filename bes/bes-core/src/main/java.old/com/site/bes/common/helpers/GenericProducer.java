package com.site.bes.common.helpers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import com.site.bes.EventPayload;
import com.site.bes.EventPayloadFormat;
import com.site.bes.common.dal.EventDao;
import com.site.bes.common.dal.EventDo;
import com.site.kernel.SystemRegistry;
import com.site.kernel.dal.DalException;

public class GenericProducer {
   private static GenericProducer s_instance = new GenericProducer();

   public static GenericProducer getInstance() {
      return s_instance;
   }

   private GenericProducer() {
   }

   private String getProducerId() {
      try {
         return InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
         return "127.0.0.1";
      }
   }

   private String getStringPayload(EventPayload payload, EventPayloadFormat format) {
      EventPayloadFormatter formatter = (EventPayloadFormatter) SystemRegistry.newInstance(EventPayloadFormatter.class, format);

      return formatter.format(payload);
   }

   public void insertEvent(String eventType, String referenceId, EventPayload payload, EventPayloadFormat format, String producerType) throws DalException {
      insertEvent(eventType, referenceId, payload, format, producerType, 0);
   }

   public void insertEvent(String eventType, String referenceId, EventPayload payload, EventPayloadFormat format, String producerType, int maxRetryTimes) throws DalException {
      insertEvent(eventType, referenceId, payload, format, producerType, maxRetryTimes, null);
   }

   public void insertEvent(String eventType, String referenceId, EventPayload payload, EventPayloadFormat format, String producerType, int maxRetryTimes, Date scheduleDate)
         throws DalException {
      if (format == null) {
         throw new NullPointerException("EventPayloadFormat can't be null");
      }

      EventDo event = EventDao.getInstance().createLocal();
      String strPayload = getStringPayload(payload, format);

      event.setEventType(eventType);
      event.setRefId(referenceId);
      event.setPayload(strPayload);
      event.setPayloadType(format.getId());
      event.setProducerType(producerType);
      event.setProducerId(getProducerId());
      event.setMaxRetryTimes(maxRetryTimes);
      event.setScheduleDate(scheduleDate);

      EventDao.getInstance().insert(event);
   }

}

package com.site.bes;

import java.util.Date;

public interface Event<T extends EventPayload> {
   public Date getCreationDate();

   public int getEventId();

   public T getEventPayload();

   public EventState getEventState();

   public String getEventType();

   public int getMaxRetryTimes();

   public Date getNextScheduleDate();

   public String getProducerId();

   public String getProducerType();

   public String getRefId();

   public int getRetriedTimes();

   public String getStringPayload();

   public void setEventState(EventState eventState);

   public void setNextScheduleDate(Date date);
}

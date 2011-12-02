package com.site.bes.queue.mysql;

import com.site.bes.queue.mysql.dal.Event;

public class EventTransformer {
   public Event convert(com.site.bes.Event event) {
      Event proto = new Event();

      proto.setEventType(event.getType());
      proto.setPayload(event.getPayload());
      proto.setPayloadType(event.getPayloadFormat());
      proto.setRefId(event.getRefId());
      proto.setProducerId(event.getProducer());
      proto.setScheduleDate(event.getScheduleDate());

      return proto;
   }
   
   public com.site.bes.Event convert(Event proto) {
      com.site.bes.Event event = new com.site.bes.Event();
      
      event.setId(proto.getEventId());
      event.setType(proto.getEventType());
      event.setPayload(proto.getPayload());
      event.setPayloadFormat(proto.getPayloadType());
      event.setRefId(proto.getRefId());
      event.setProducer(proto.getProducerId());
      event.setScheduleDate(proto.getScheduleDate());
      event.setCreationDate(proto.getCreationDate());
      
      return event;
   }
}

package com.site.kernel.dal.db;

import static com.site.kernel.dal.ValueType.DATE;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.db.DataRowField.NOT_NULL;
import static com.site.kernel.dal.db.DataRowField.NULL;

public class SampleEntity extends Entity {
   public static final SampleEntity EVENT = new SampleEntity("event", "e", SampleDo.class);

   public static final DataRowField EVENT_ID = new DataRowField(EVENT, "event-id", INT, "EVENT_ID", NOT_NULL, 11, 0, null, true);
   public static final DataRowField EVENT_TYPE = new DataRowField(EVENT, "event-type", STRING, "EVENT_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField PAYLOAD = new DataRowField(EVENT, "payload", STRING, "PAYLOAD", NOT_NULL, 4000, 0, null);
   public static final DataRowField PAYLOAD_TYPE = new DataRowField(EVENT, "payload-type", INT, "PAYLOAD_TYPE", NOT_NULL, 11, 0, null);
   public static final DataRowField REF_ID = new DataRowField(EVENT, "ref-id", STRING, "REF_ID", NULL, 32, 0, null);
   public static final DataRowField PRODUCER_TYPE = new DataRowField(EVENT, "producer-type", STRING, "PRODUCER_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField PRODUCER_ID = new DataRowField(EVENT, "producer-id", STRING, "PRODUCER_ID", NOT_NULL, 32, 0, null);
   public static final DataRowField MAX_RETRY_TIMES = new DataRowField(EVENT, "max-retry-times", INT, "MAX_RETRY_TIMES", NULL, 11, 0, null);
   public static final DataRowField SCHEDULE_DATE = new DataRowField(EVENT, "schedule-date", DATE, "SCHEDULE_DATE", NULL, 0, 0, null);
   public static final DataRowField CREATION_DATE = new DataRowField(EVENT, "creation-date", DATE, "CREATION_DATE", NULL, 0, 0, null, null, "NOW()", null);
   public static final DataRowField MAX_EVENT_ID = new DataRowField(EVENT, "max-event-id", INT, "MAX(event_id)", NULL, 11, 0, "maxEventId", "MAX(event_id)", null, null);

   public static final DataRowField KEY_EVENT_ID = new DataRowField(EVENT, "key-event-id", INT);
   public static final DataRowField START_EVENT_ID = new DataRowField(EVENT, "start-event-id", INT);
   public static final DataRowField END_EVENT_ID = new DataRowField(EVENT, "end-event-id", INT);
   public static final DataRowField START_SCHEDULE_DATE = new DataRowField(EVENT, "start-schedule-date", DATE);
   public static final DataRowField END_SCHEDULE_DATE = new DataRowField(EVENT, "end-schedule-date", DATE);
   public static final DataRowField CONSUMER_TYPE = new DataRowField(EVENT, "consumer-type", STRING);
   public static final DataRowField CONSUMER_ID = new DataRowField(EVENT, "consumer-id", STRING);
   public static final DataRowField BATCH_ID = new DataRowField(EVENT, "batch-id", INT);

   public static final Readset READSET_FULL = new Readset(new DataRowField[] {
         EVENT_ID,
         EVENT_TYPE,
         PAYLOAD,
         PAYLOAD_TYPE,
         REF_ID,
         PRODUCER_TYPE,
         PRODUCER_ID,
         MAX_RETRY_TIMES,
         SCHEDULE_DATE,
         CREATION_DATE });

   public static final Readset READSET_FETCH_EVENTS = new Readset(new DataRowField[] {
         EVENT_ID,
         PAYLOAD,
         PAYLOAD_TYPE,
         REF_ID,
         PRODUCER_TYPE,
         PRODUCER_ID,
         MAX_RETRY_TIMES,
         SCHEDULE_DATE,
         CREATION_DATE });

   public static final Readset READSET_MAX_EVENT_ID = new Readset(new DataRowField[] {
         MAX_EVENT_ID });

   public static final Updateset UPDATESET_FULL = new Updateset(new DataRowField[] {
         EVENT_ID,
         EVENT_TYPE,
         PAYLOAD,
         PAYLOAD_TYPE,
         REF_ID,
         PRODUCER_TYPE,
         PRODUCER_ID,
         MAX_RETRY_TIMES,
         SCHEDULE_DATE });

   public static final QueryDef FIND_BY_PK = new QueryDef(
         EVENT,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE EVENT_ID = ${key-event-id}");

   public static final QueryDef INSERT = new QueryDef(
         EVENT,
         QueryType.INSERT,
         "INSERT INTO <TABLE/> <FIELDS/>");

   public static final QueryDef UPDATE_BY_PK = new QueryDef(
         EVENT,
         QueryType.UPDATE,
         "UPDATE <TABLE/> SET <FIELDS/> WHERE EVENT_ID = ${key-event-id}");

   public static final QueryDef DELETE_BY_PK = new QueryDef(
         EVENT,
         QueryType.DELETE,
         "DELETE FROM <TABLE/> WHERE EVENT_ID = ${key-event-id}");

   public static final QueryDef FIND_ALL_BY_ID_RANGE = new QueryDef(
         EVENT,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE event_id BETWEEN ${start-event-id} AND ${end-event-id}");

   public static final QueryDef FETCH_EVENTS = new QueryDef(
         EVENT,
         QueryType.SELECT,
         "{ CALL sp_fetch_events(${event-type},${consumer-type},${consumer-id},#{batch-id}) }",
         true);

   public static final QueryDef FETCH_SCHEDULE_EVENTS = new QueryDef(
         EVENT,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM event_plus ep, event e WHERE e.event_id = ep.event_id AND ep.event_type = ${event-type} AND ep.consumer_type = ${consumer-type} AND ep.event_state IN (-1, -2) AND (ep.max_retry_times <= 0 OR ep.retried_times < ep.max_retry_times) AND ep.next_schedule_date > ${start-schedule-date} AND ep.next_schedule_date <= ${end-schedule-date} ORDER BY ep.next_schedule_date LIMIT 50");

   public static final QueryDef FIND_BY_EVENT_TYPE = new QueryDef(
         EVENT,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE event_type = ${event-type}");

   public static final DataRowField[] HINT_FIELDS = new DataRowField[] {  };

   private SampleEntity(String logicalName, String alias, Class doClass) {
      super(logicalName, alias, doClass);
   }

}

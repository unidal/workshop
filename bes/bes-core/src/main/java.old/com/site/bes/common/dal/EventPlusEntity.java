package com.site.bes.common.dal;

import static com.site.kernel.dal.ValueType.DATE;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.db.DataRowField.NOT_NULL;
import static com.site.kernel.dal.db.DataRowField.NULL;

import com.site.kernel.dal.db.Entity;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.QueryDef;
import com.site.kernel.dal.db.QueryType;
import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;

public class EventPlusEntity extends Entity {
   public static final EventPlusEntity EVENT_PLUS = new EventPlusEntity("event_plus", "ep", EventPlusDo.class);

   public static final DataRowField EVENT_ID = new DataRowField(EVENT_PLUS, "event-id", INT, "EVENT_ID", NOT_NULL, 11, 0, null);
   public static final DataRowField CONSUMER_TYPE = new DataRowField(EVENT_PLUS, "consumer-type", STRING, "CONSUMER_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField CONSUMER_ID = new DataRowField(EVENT_PLUS, "consumer-id", STRING, "CONSUMER_ID", NULL, 32, 0, null);
   public static final DataRowField EVENT_TYPE = new DataRowField(EVENT_PLUS, "event-type", STRING, "EVENT_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField NEXT_SCHEDULE_DATE = new DataRowField(EVENT_PLUS, "next-schedule-date", DATE, "NEXT_SCHEDULE_DATE", NULL, 0, 0, null);
   public static final DataRowField MAX_RETRY_TIMES = new DataRowField(EVENT_PLUS, "max-retry-times", INT, "MAX_RETRY_TIMES", NOT_NULL, 11, 0, null);
   public static final DataRowField RETRIED_TIMES = new DataRowField(EVENT_PLUS, "retried-times", INT, "RETRIED_TIMES", NOT_NULL, 11, 0, null, null, "0", "retried_times + 1");
   public static final DataRowField EVENT_STATE = new DataRowField(EVENT_PLUS, "event-state", INT, "EVENT_STATE", NOT_NULL, 11, 0, null);
   public static final DataRowField CREATION_DATE = new DataRowField(EVENT_PLUS, "creation-date", DATE, "CREATION_DATE", NULL, 0, 0, null, null, "NOW()", null);
   public static final DataRowField LAST_MODIFIED_DATE = new DataRowField(EVENT_PLUS, "last-modified-date", DATE, "LAST_MODIFIED_DATE", NOT_NULL, 0, 0, null, null, "NOW()", "NOW()");

   public static final DataRowField KEY_EVENT_ID = new DataRowField(EVENT_PLUS, "key-event-id", INT);
   public static final DataRowField KEY_CONSUMER_TYPE = new DataRowField(EVENT_PLUS, "key-consumer-type", STRING);

   public static final Readset READSET_FULL = new Readset(new DataRowField[] {
         EVENT_ID,
         CONSUMER_TYPE,
         CONSUMER_ID,
         EVENT_TYPE,
         NEXT_SCHEDULE_DATE,
         MAX_RETRY_TIMES,
         RETRIED_TIMES,
         EVENT_STATE,
         CREATION_DATE,
         LAST_MODIFIED_DATE });

   public static final Updateset UPDATESET_FULL = new Updateset(new DataRowField[] {
         EVENT_ID,
         CONSUMER_TYPE,
         CONSUMER_ID,
         EVENT_TYPE,
         NEXT_SCHEDULE_DATE,
         MAX_RETRY_TIMES,
         RETRIED_TIMES,
         EVENT_STATE,
         LAST_MODIFIED_DATE });

   public static final Updateset UPDATESET_EVENT_STATE = new Updateset(new DataRowField[] {
         EVENT_STATE,
         CONSUMER_ID,
         LAST_MODIFIED_DATE });

   public static final QueryDef FIND_BY_PK = new QueryDef(
         EVENT_PLUS,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE EVENT_ID = ${key-event-id} AND CONSUMER_TYPE = ${key-consumer-type}");

   public static final QueryDef INSERT = new QueryDef(
         EVENT_PLUS,
         QueryType.INSERT,
         "INSERT INTO <TABLE/> <FIELDS/>");

   public static final QueryDef UPDATE_BY_PK = new QueryDef(
         EVENT_PLUS,
         QueryType.UPDATE,
         "UPDATE <TABLE/> SET <FIELDS/> WHERE EVENT_ID = ${key-event-id} AND CONSUMER_TYPE = ${key-consumer-type}");

   public static final QueryDef DELETE_BY_PK = new QueryDef(
         EVENT_PLUS,
         QueryType.DELETE,
         "DELETE FROM <TABLE/> WHERE EVENT_ID = ${key-event-id} AND CONSUMER_TYPE = ${key-consumer-type}");

   public static final QueryDef UPSERT = new QueryDef(
         EVENT_PLUS,
         QueryType.INSERT,
         "INSERT INTO <TABLE/> <FIELDS/> ON DUPLICATE KEY UPDATE event_state = ${event-state}, next_schedule_date = ${next-schedule-date}, retried_times = retried_times + 1, last_modified_date = NOW()");

   public static final QueryDef CHANGE_EVENT_STATE_BY_PK = new QueryDef(
         EVENT_PLUS,
         QueryType.UPDATE,
         "UPDATE <TABLE/> SET <FIELDS/> WHERE event_id = ${key-event-id} AND event_state != ${event-state}");

   public static final QueryDef FIND_ALL_BY_EVENT_ID = new QueryDef(
         EVENT_PLUS,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE EVENT_ID = ${event-id}");

   public static final DataRowField[] HINT_FIELDS = new DataRowField[] {  };

   private EventPlusEntity(String logicalName, String alias, Class doClass) {
      super(logicalName, alias, doClass);
   }

}

package com.site.bes.common.dal;

import static com.site.kernel.dal.ValueType.DATE;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.LONG;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.db.DataRowField.NOT_NULL;
import static com.site.kernel.dal.db.DataRowField.NULL;

import com.site.kernel.dal.db.Entity;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.QueryDef;
import com.site.kernel.dal.db.QueryType;
import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;

public class EventDashboardEntity extends Entity {
   public static final EventDashboardEntity EVENT_DASHBOARD = new EventDashboardEntity("event_dashboard", "ed", EventDashboardDo.class);

   public static final DataRowField EVENT_TYPE = new DataRowField(EVENT_DASHBOARD, "event-type", STRING, "EVENT_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField CONSUMER_TYPE = new DataRowField(EVENT_DASHBOARD, "consumer-type", STRING, "CONSUMER_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField LAST_FETCHED_ID = new DataRowField(EVENT_DASHBOARD, "last-fetched-id", INT, "LAST_FETCHED_ID", NOT_NULL, 11, 0, null);
   public static final DataRowField LAST_SCHEDULED_DATE = new DataRowField(EVENT_DASHBOARD, "last-scheduled-date", DATE, "LAST_SCHEDULED_DATE", NULL, 0, 0, null);
   public static final DataRowField BATCH_TIMEOUT = new DataRowField(EVENT_DASHBOARD, "batch-timeout", LONG, "BATCH_TIMEOUT", NOT_NULL, 32, 0, null);
   public static final DataRowField CREATION_DATE = new DataRowField(EVENT_DASHBOARD, "creation-date", DATE, "CREATION_DATE", NULL, 0, 0, null, null, "NOW()", null);
   public static final DataRowField LAST_MODIFIED_DATE = new DataRowField(EVENT_DASHBOARD, "last-modified-date", DATE, "LAST_MODIFIED_DATE", NOT_NULL, 0, 0, null, null, "NOW()", "NOW()");

   public static final DataRowField KEY_EVENT_TYPE = new DataRowField(EVENT_DASHBOARD, "key-event-type", STRING);
   public static final DataRowField KEY_CONSUMER_TYPE = new DataRowField(EVENT_DASHBOARD, "key-consumer-type", STRING);

   public static final Readset READSET_FULL = new Readset(new DataRowField[] {
         EVENT_TYPE,
         CONSUMER_TYPE,
         LAST_FETCHED_ID,
         LAST_SCHEDULED_DATE,
         BATCH_TIMEOUT,
         CREATION_DATE,
         LAST_MODIFIED_DATE });

   public static final Updateset UPDATESET_FULL = new Updateset(new DataRowField[] {
         EVENT_TYPE,
         CONSUMER_TYPE,
         LAST_FETCHED_ID,
         LAST_SCHEDULED_DATE,
         BATCH_TIMEOUT,
         LAST_MODIFIED_DATE });

   public static final QueryDef FIND_BY_PK = new QueryDef(
         EVENT_DASHBOARD,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE EVENT_TYPE = ${key-event-type} AND CONSUMER_TYPE = ${key-consumer-type}");

   public static final QueryDef INSERT = new QueryDef(
         EVENT_DASHBOARD,
         QueryType.INSERT,
         "INSERT INTO <TABLE/> <FIELDS/>");

   public static final QueryDef UPDATE_BY_PK = new QueryDef(
         EVENT_DASHBOARD,
         QueryType.UPDATE,
         "UPDATE <TABLE/> SET <FIELDS/> WHERE EVENT_TYPE = ${key-event-type} AND CONSUMER_TYPE = ${key-consumer-type}");

   public static final QueryDef DELETE_BY_PK = new QueryDef(
         EVENT_DASHBOARD,
         QueryType.DELETE,
         "DELETE FROM <TABLE/> WHERE EVENT_TYPE = ${key-event-type} AND CONSUMER_TYPE = ${key-consumer-type}");

   public static final QueryDef UPSERT = new QueryDef(
         EVENT_DASHBOARD,
         QueryType.INSERT,
         "INSERT INTO <TABLE/> <FIELDS/> ON DUPLICATE KEY UPDATE batch_timeout = ${batch-timeout}, last_fetched_id = ${last-fetched-id}, last_modified_date = NOW()");

   public static final DataRowField[] HINT_FIELDS = new DataRowField[] {  };

   private EventDashboardEntity(String logicalName, String alias, Class doClass) {
      super(logicalName, alias, doClass);
   }

}

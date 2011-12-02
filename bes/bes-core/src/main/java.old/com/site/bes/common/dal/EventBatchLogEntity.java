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

public class EventBatchLogEntity extends Entity {
   public static final EventBatchLogEntity EVENT_BATCH_LOG = new EventBatchLogEntity("event_batch_log", "ebl", EventBatchLogDo.class);

   public static final DataRowField BATCH_ID = new DataRowField(EVENT_BATCH_LOG, "batch-id", INT, "BATCH_ID", NOT_NULL, 11, 0, null);
   public static final DataRowField EVENT_TYPE = new DataRowField(EVENT_BATCH_LOG, "event-type", STRING, "EVENT_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField CONSUMER_TYPE = new DataRowField(EVENT_BATCH_LOG, "consumer-type", STRING, "CONSUMER_TYPE", NOT_NULL, 32, 0, null);
   public static final DataRowField START_EVENT_ID = new DataRowField(EVENT_BATCH_LOG, "start-event-id", INT, "START_EVENT_ID", NOT_NULL, 11, 0, null);
   public static final DataRowField END_EVENT_ID = new DataRowField(EVENT_BATCH_LOG, "end-event-id", INT, "END_EVENT_ID", NOT_NULL, 11, 0, null);
   public static final DataRowField CONSUMER_ID = new DataRowField(EVENT_BATCH_LOG, "consumer-id", STRING, "CONSUMER_ID", NOT_NULL, 32, 0, null);
   public static final DataRowField CREATION_DATE = new DataRowField(EVENT_BATCH_LOG, "creation-date", DATE, "CREATION_DATE", NULL, 0, 0, null, null, "NOW()", null);
   public static final DataRowField PROCESS_STATUS = new DataRowField(EVENT_BATCH_LOG, "process-status", INT, "PROCESS_STATUS", NOT_NULL, 11, 0, null);
   public static final DataRowField FETCHED_ROWS = new DataRowField(EVENT_BATCH_LOG, "fetched-rows", INT, "FETCHED_ROWS", NULL, 11, 0, null);
   public static final DataRowField SUCCESS_COUNT = new DataRowField(EVENT_BATCH_LOG, "success-count", INT, "SUCCESS_COUNT", NULL, 11, 0, null);
   public static final DataRowField FAILURE_COUNT = new DataRowField(EVENT_BATCH_LOG, "failure-count", INT, "FAILURE_COUNT", NULL, 11, 0, null);
   public static final DataRowField FETCH_TIME = new DataRowField(EVENT_BATCH_LOG, "fetch-time", INT, "FETCH_TIME", NULL, 11, 0, null);
   public static final DataRowField MAX_WAIT_TIME = new DataRowField(EVENT_BATCH_LOG, "max-wait-time", INT, "MAX_WAIT_TIME", NULL, 11, 0, null);
   public static final DataRowField AVG_WAIT_TIME = new DataRowField(EVENT_BATCH_LOG, "avg-wait-time", INT, "AVG_WAIT_TIME", NULL, 11, 0, null);
   public static final DataRowField MAX_PROCESS_TIME = new DataRowField(EVENT_BATCH_LOG, "max-process-time", INT, "MAX_PROCESS_TIME", NULL, 11, 0, null);
   public static final DataRowField AVG_PROCESS_TIME = new DataRowField(EVENT_BATCH_LOG, "avg-process-time", INT, "AVG_PROCESS_TIME", NULL, 11, 0, null);

   public static final DataRowField KEY_BATCH_ID = new DataRowField(EVENT_BATCH_LOG, "key-batch-id", INT);
   public static final DataRowField EVENT_ID = new DataRowField(EVENT_BATCH_LOG, "event-id", INT);
   public static final DataRowField DEADLINE = new DataRowField(EVENT_BATCH_LOG, "deadline", DATE);

   public static final Readset READSET_FULL = new Readset(new DataRowField[] {
         BATCH_ID,
         EVENT_TYPE,
         CONSUMER_TYPE,
         START_EVENT_ID,
         END_EVENT_ID,
         CONSUMER_ID,
         CREATION_DATE,
         PROCESS_STATUS,
         FETCHED_ROWS,
         SUCCESS_COUNT,
         FAILURE_COUNT,
         FETCH_TIME,
         MAX_WAIT_TIME,
         AVG_WAIT_TIME,
         MAX_PROCESS_TIME,
         AVG_PROCESS_TIME });

   public static final Updateset UPDATESET_FULL = new Updateset(new DataRowField[] {
         BATCH_ID,
         EVENT_TYPE,
         CONSUMER_TYPE,
         START_EVENT_ID,
         END_EVENT_ID,
         CONSUMER_ID,
         PROCESS_STATUS,
         FETCHED_ROWS,
         SUCCESS_COUNT,
         FAILURE_COUNT,
         FETCH_TIME,
         MAX_WAIT_TIME,
         AVG_WAIT_TIME,
         MAX_PROCESS_TIME,
         AVG_PROCESS_TIME });

   public static final Updateset UPDATESET_EVENT_STAT = new Updateset(new DataRowField[] {
         PROCESS_STATUS,
         FETCHED_ROWS,
         SUCCESS_COUNT,
         FAILURE_COUNT,
         FETCH_TIME,
         MAX_WAIT_TIME,
         AVG_WAIT_TIME,
         MAX_PROCESS_TIME,
         AVG_PROCESS_TIME });

   public static final QueryDef FIND_BY_PK = new QueryDef(
         EVENT_BATCH_LOG,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE BATCH_ID = ${key-batch-id}");

   public static final QueryDef INSERT = new QueryDef(
         EVENT_BATCH_LOG,
         QueryType.INSERT,
         "INSERT INTO <TABLE/> <FIELDS/>");

   public static final QueryDef UPDATE_BY_PK = new QueryDef(
         EVENT_BATCH_LOG,
         QueryType.UPDATE,
         "UPDATE <TABLE/> SET <FIELDS/> WHERE BATCH_ID = ${key-batch-id}");

   public static final QueryDef DELETE_BY_PK = new QueryDef(
         EVENT_BATCH_LOG,
         QueryType.DELETE,
         "DELETE FROM <TABLE/> WHERE BATCH_ID = ${key-batch-id}");

   public static final QueryDef FIND_ALL_UNPROCESSED = new QueryDef(
         EVENT_BATCH_LOG,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE process_status = 0 AND CREATION_DATE < ${deadline} ORDER BY CREATION_DATE");

   public static final QueryDef FIND_ALL_BY_EVENT_ID = new QueryDef(
         EVENT_BATCH_LOG,
         QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE START_EVENT_ID <= ${event-id} AND END_EVENT_ID >= ${event-id}");

   public static final DataRowField[] HINT_FIELDS = new DataRowField[] {  };

   private EventBatchLogEntity(String logicalName, String alias, Class doClass) {
      super(logicalName, alias, doClass);
   }

}

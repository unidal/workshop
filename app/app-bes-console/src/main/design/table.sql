/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2008-5-25 7:51:44                            */
/*==============================================================*/


drop table if exists EVENT;

drop table if exists EVENT_BATCH_LOG;

drop table if exists EVENT_DASHBOARD;

drop table if exists EVENT_PLUS;

/*==============================================================*/
/* Table: EVENT                                                 */
/*==============================================================*/
create table EVENT
(
   EVENT_ID             int not null,
   EVENT_TYPE           varchar(32) not null,
   PAYLOAD              varchar(4000) not null,
   PAYLOAD_TYPE         int not null,
   REF_ID               varchar(32) not null,
   PRODUCER_TYPE        varchar(32) not null,
   PRODUCER_ID          varchar(32) not null,
   MAX_RETRY_TIMES      int,
   SCHEDULE_DATE        datetime,
   CREATION_DATE        timestamp not null,
   primary key (EVENT_ID)
);

/*==============================================================*/
/* Table: EVENT_BATCH_LOG                                       */
/*==============================================================*/
create table EVENT_BATCH_LOG
(
   BATCH_ID             int not null,
   EVENT_TYPE           varchar(32) not null,
   CONSUMER_TYPE        varchar(32) not null,
   START_EVENT_ID       int not null,
   END_EVENT_ID         int not null,
   CONSUMER_ID          varchar(32) not null,
   CREATION_DATE        timestamp not null,
   PROCESS_STATUS       int not null,
   FETCHED_ROWS         int,
   SUCCESS_COUNT        int,
   FAILURE_COUNT        int,
   FETCH_TIME           int,
   MAX_WAIT_TIME        int,
   AVG_WAIT_TIME        int,
   MAX_PROCESS_TIME     int,
   AVG_PROCESS_TIME     int,
   primary key (BATCH_ID)
);

/*==============================================================*/
/* Table: EVENT_DASHBOARD                                       */
/*==============================================================*/
create table EVENT_DASHBOARD
(
   EVENT_TYPE           varchar(32) not null,
   CONSUMER_TYPE        varchar(32) not null,
   LAST_FETCHED_ID      int not null,
   LAST_SCHEDULED_DATE  datetime not null,
   CREATION_DATE        timestamp not null,
   LAST_MODIFIED_DATE   datetime not null,
   BATCH_TIMEOUT        bigint not null,
   primary key (EVENT_TYPE, CONSUMER_TYPE)
);

/*==============================================================*/
/* Table: EVENT_PLUS                                            */
/*==============================================================*/
create table EVENT_PLUS
(
   EVENT_ID             int not null,
   CONSUMER_TYPE        varchar(32) not null,
   CONSUMER_ID          varchar(32),
   EVENT_TYPE           varchar(32) not null,
   NEXT_SCHEDULE_DATE   datetime,
   MAX_RETRY_TIMES      int not null,
   RETRIED_TIMES        int not null,
   EVENT_STATE          int not null,
   CREATION_DATE        timestamp not null,
   LAST_MODIFIED_DATE   datetime not null,
   primary key (EVENT_ID, CONSUMER_TYPE)
);


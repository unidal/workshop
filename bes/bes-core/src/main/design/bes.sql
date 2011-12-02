/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2007-1-20 23:53:30                           */
/*==============================================================*/


drop index NEXT_SCHEDULE_DATE on EVENT_PLUS;

drop table if exists EVENT;

drop table if exists EVENT_BATCH_LOG;

drop table if exists EVENT_DASHBOARD;

drop table if exists EVENT_PLUS;

/*==============================================================*/
/* Table: EVENT                                                 */
/*==============================================================*/
create table EVENT
(
   EVENT_ID             int not null auto_increment,
   EVENT_TYPE           varchar(32) not null,
   PAYLOAD              varchar(4000) not null,
   PAYLOAD_TYPE         int not null,
   REF_ID               varchar(32) not null,
   PRODUCER_TYPE        varchar(32) not null,
   PRODUCER_ID          varchar(32) not null,
   MAX_RETRY_TIMES      int,
   SCHEDULE_DATE        timestamp,
   CREATION_DATE        timestamp not null,
   primary key (EVENT_ID)
);

/*==============================================================*/
/* Table: EVENT_BATCH_LOG                                       */
/*==============================================================*/
create table EVENT_BATCH_LOG
(
   BATCH_ID             int not null auto_increment,
   EVENT_TYPE           varchar(32) not null,
   CONSUMER_TYPE        varchar(32) not null,
   START_EVENT_ID       int not null,
   END_EVENT_ID         int not null,
   CONSUMER_ID          varchar(32) not null,
   CREATION_DATE        timestamp not null,
   EVENT_STATUS         int not null,
   FETCHED_ROWS         int,
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
   LAST_PLUS_ID         int not null,
   LAST_PLUS_DATE       timestamp not null,
   CREATION_DATE        timestamp not null,
   LAST_MODIFIED_DATE   timestamp not null,
   primary key (EVENT_TYPE, CONSUMER_TYPE)
);

/*==============================================================*/
/* Table: EVENT_PLUS                                            */
/*==============================================================*/
create table EVENT_PLUS
(
   EVENT_ID             int not null,
   NEXT_SCHEDULE_DATE   timestamp,
   MAX_RETRY_TIMES      int,
   RETRIED_TIMES        int,
   EVENT_STATUS         int not null,
   CREATION_DATE        timestamp not null,
   LAST_MODIFIED_DATE   timestamp not null,
   primary key (EVENT_ID)
);

/*==============================================================*/
/* Index: NEXT_SCHEDULE_DATE                                    */
/*==============================================================*/
create index NEXT_SCHEDULE_DATE on EVENT_PLUS
(
   NEXT_SCHEDULE_DATE
);


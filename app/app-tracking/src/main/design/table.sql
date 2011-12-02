/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2008-8-9 21:39:30                            */
/*==============================================================*/


drop index page_visit_IDX_U on page_visit;

drop index page_visit_log_IDX_C on page_visit_log;

drop index page_visit_track_IDX_C on page_visit_track;

drop table if exists page_visit;

drop table if exists page_visit_log;

drop table if exists page_visit_track;

/*==============================================================*/
/* Table: page_visit                                            */
/*==============================================================*/
create table page_visit
(
   page_id              int not null auto_increment,
   page_url             varchar(200) not null,
   total_visits         int not null,
   creation_date        datetime not null,
   last_modified_date   datetime not null,
   primary key (page_id)
);

/*==============================================================*/
/* Index: page_visit_IDX_U                                      */
/*==============================================================*/
create unique index page_visit_IDX_U on page_visit
(
   page_url
);

/*==============================================================*/
/* Table: page_visit_log                                        */
/*==============================================================*/
create table page_visit_log
(
   log_id               int not null auto_increment,
   page_id              int not null,
   from_page            varchar(100) not null,
   category1            int,
   category2            int,
   on_top               int,
   visits               int not null,
   creation_date        datetime not null,
   primary key (log_id)
);

/*==============================================================*/
/* Index: page_visit_log_IDX_C                                  */
/*==============================================================*/
create index page_visit_log_IDX_C on page_visit_log
(
   creation_date
);

/*==============================================================*/
/* Table: page_visit_track                                      */
/*==============================================================*/
create table page_visit_track
(
   track_id             int not null auto_increment,
   page_id              int not null,
   from_page            varchar(100) not null,
   client_ip            varchar(15) not null,
   creation_date        datetime not null,
   primary key (track_id)
);

/*==============================================================*/
/* Index: page_visit_track_IDX_C                                */
/*==============================================================*/
create index page_visit_track_IDX_C on page_visit_track
(
   creation_date
);


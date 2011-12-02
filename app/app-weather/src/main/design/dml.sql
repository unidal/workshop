/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     6/19/2010 4:53:42 PM                         */
/*==============================================================*/


drop table if exists subscribe;

drop table if exists weather;

/*==============================================================*/
/* Table: subscribe                                             */
/*==============================================================*/
create table subscribe
(
   user_id              int not null,
   woeid                int not null,
   seq_id               int not null,
   primary key (user_id, woeid)
);

/*==============================================================*/
/* Table: weather                                               */
/*==============================================================*/
create table weather
(
   woeid                int not null,
   target_date          date not null,
   city_name            varchar(50) not null,
   condition_text       varchar(30) not null,
   condition_code       int not null,
   high_temperature     int not null,
   low_temperature      int not null,
   creation_date        datetime not null,
   last_modify_date     timestamp not null,
   primary key (woeid, target_date)
);


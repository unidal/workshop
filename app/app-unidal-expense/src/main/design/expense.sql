/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     1/3/2011 11:16:43 AM                         */
/*==============================================================*/


drop table if exists activity;

drop table if exists expense;

drop table if exists expense_details;

drop table if exists member;

drop table if exists trip;

drop table if exists trip_member;

/*==============================================================*/
/* Table: activity                                              */
/*==============================================================*/
create table activity
(
   id                   int not null auto_increment,
   trip_id              int not null,
   title                varchar(100) not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Table: expense                                               */
/*==============================================================*/
create table expense
(
   id                   int not null auto_increment,
   activity_id          int not null,
   title                varchar(100) not null,
   amount               float(8,2) not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Table: expense_details                                       */
/*==============================================================*/
create table expense_details
(
   expense_id           int not null,
   member_id            int not null,
   member_name          varchar(100) not null,
   weight               int,
   amount               float(8,2) not null,
   primary key (expense_id, member_id)
);

/*==============================================================*/
/* Table: member                                                */
/*==============================================================*/
create table member
(
   id                   int not null auto_increment,
   name                 varchar(100) not null,
   mobile_phone         varchar(11),
   status               int not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Table: trip                                                  */
/*==============================================================*/
create table trip
(
   id                   int not null auto_increment,
   title                varchar(100) not null,
   owner_id             int not null,
   auth_code            varchar(6) not null,
   status               int not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Table: trip_member                                           */
/*==============================================================*/
create table trip_member
(
   trip_id              int not null,
   member_id            int not null,
   primary key (trip_id, member_id)
);


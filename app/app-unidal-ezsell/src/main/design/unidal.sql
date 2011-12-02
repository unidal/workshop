/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2009/11/1 20:30:27                           */
/*==============================================================*/


drop index configuration_U_IDX_N on configuration;

drop index feedback_U_IDX_IT on feedback;

drop index label_U_IDX_IT on label;

drop index user_U_IDX_E on operator;

drop index seller_U_IDX_E on seller;

drop index transaction_IDX_P on transaction;

drop index transaction_IDX_S on transaction;

drop index transaction_U_IDX_IT on transaction;

drop table if exists buyer;

drop table if exists configuration;

drop table if exists feedback;

drop table if exists item;

drop table if exists item_preference;

drop table if exists label;

drop table if exists notification;

drop table if exists operator;

drop table if exists orders;

drop table if exists payment;

drop table if exists seller;

drop table if exists seller_ebay_team;

drop table if exists shipping;

drop table if exists shipping_address;

drop table if exists transaction;

/*==============================================================*/
/* Table: buyer                                                 */
/*==============================================================*/
create table buyer
(
   buyer_account        varchar(50) not null,
   buyer_name           varchar(50) not null,
   buyer_email          varchar(100),
   registration_date    datetime,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (buyer_account)
);

/*==============================================================*/
/* Table: configuration                                         */
/*==============================================================*/
create table configuration
(
   id                   int not null auto_increment,
   name                 varchar(100) not null,
   value                varchar(1000) not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Index: configuration_U_IDX_N                                 */
/*==============================================================*/
create unique index configuration_U_IDX_N on configuration
(
   name
);

/*==============================================================*/
/* Table: feedback                                              */
/*==============================================================*/
create table feedback
(
   id                   bigint not null,
   seller_id            int not null,
   item_id              bigint not null,
   transaction_id       bigint not null,
   type                 int not null,
   message              varchar(100),
   role                 int not null,
   comment_date         datetime not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Index: feedback_U_IDX_IT                                     */
/*==============================================================*/
create unique index feedback_U_IDX_IT on feedback
(
   item_id,
   transaction_id
);

/*==============================================================*/
/* Table: item                                                  */
/*==============================================================*/
create table item
(
   item_id              bigint not null,
   item_title           varchar(60) not null,
   original_item_id     bigint,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (item_id)
);

/*==============================================================*/
/* Table: item_preference                                       */
/*==============================================================*/
create table item_preference
(
   item_id              bigint not null,
   cd_title             varchar(60),
   cd_weight            varchar(10),
   cd_value             varchar(10),
   cd_origin            varchar(20),
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (item_id)
);

/*==============================================================*/
/* Table: label                                                 */
/*==============================================================*/
create table label
(
   label                varchar(30) not null,
   type                 int not null,
   id                   varchar(20) not null,
   primary key (label, type, id)
);

/*==============================================================*/
/* Index: label_U_IDX_IT                                        */
/*==============================================================*/
create index label_U_IDX_IT on label
(
   id,
   type
);

/*==============================================================*/
/* Table: notification                                          */
/*==============================================================*/
create table notification
(
   id                   int not null auto_increment,
   event_name           varchar(30) not null,
   seller_id            int not null,
   seller_account       varchar(20) not null,
   event_payload        text not null,
   status               int not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (id)
);

/*==============================================================*/
/* Table: operator                                              */
/*==============================================================*/
create table operator
(
   operator_id          int not null auto_increment,
   email                varchar(100) not null,
   encrypted_password   varchar(50) not null,
   login_failures       int not null,
   last_login_date      datetime not null,
   status               int not null,
   ebay_seller_id       int not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (operator_id)
);

/*==============================================================*/
/* Index: user_U_IDX_E                                          */
/*==============================================================*/
create unique index user_U_IDX_E on operator
(
   email
);

/*==============================================================*/
/* Table: orders                                                */
/*==============================================================*/
create table orders
(
   order_id             bigint not null,
   amount_paid          float(8,2) not null,
   adjustment_amount    float(8,2) not null,
   order_status         varchar(20) not null,
   settled              int not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (order_id)
);

/*==============================================================*/
/* Table: payment                                               */
/*==============================================================*/
create table payment
(
   payment_id           varchar(20) not null,
   seller_id            int not null,
   parent_payment_id    varchar(20) not null,
   receipt_id           varchar(20) not null,
   amount               float(8,2) not null,
   fee_amount           float(8,2) not null,
   settle_amount        float(8,2) not null,
   tax_amount           float(8,2) not null,
   currency_code        varchar(3) not null,
   exchange_rate        decimal(8,2),
   buyer_account        varchar(30) not null,
   buyer_name           varchar(50) not null,
   buyer_email          varchar(100),
   payment_type         varchar(20),
   payment_status       varchar(20),
   pending_reason       varchar(20),
   reason_code          varchar(20),
   notes                varchar(200),
   order_time           datetime not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   transaction_id       int,
   order_id             bigint,
   primary key (payment_id)
);

/*==============================================================*/
/* Table: seller                                                */
/*==============================================================*/
create table seller
(
   seller_id            int not null auto_increment,
   operator_id          int not null,
   ebay_account         varchar(20) not null,
   ebay_app_id          varchar(50) not null,
   ebay_auth_token      varchar(1000) not null,
   paypal_username      varchar(100),
   paypal_password      varchar(50),
   paypal_signature     varchar(100),
   transaction_last_fetch_date datetime,
   feedback_last_fetch_date datetime,
   team_at_ebay         int,
   name                 varchar(20),
   address              varchar(100),
   phone                varchar(20),
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (seller_id)
);

/*==============================================================*/
/* Index: seller_U_IDX_E                                        */
/*==============================================================*/
create unique index seller_U_IDX_E on seller
(
   ebay_account
);

/*==============================================================*/
/* Table: seller_ebay_team                                      */
/*==============================================================*/
create table seller_ebay_team
(
   seller_id            int not null,
   team_name            varchar(30) not null,
   team_leader          varchar(30) not null,
   team_leader_phone    varchar(30) not null,
   team_leader_cube     varchar(30) not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (seller_id)
);

/*==============================================================*/
/* Table: shipping                                              */
/*==============================================================*/
create table shipping
(
   shipping_tracking_id varchar(20) not null,
   seller_id            int not null,
   country              varchar(30),
   weight               int,
   price_before_discount float(8,2),
   discount_rate        decimal(8,2),
   customs_fee          float(8,2),
   price_after_discount float(8,2),
   shipping_date        datetime,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (shipping_tracking_id)
);

/*==============================================================*/
/* Table: shipping_address                                      */
/*==============================================================*/
create table shipping_address
(
   payment_id           varchar(20) not null,
   buyer_account        varchar(50) not null,
   name                 varchar(100) not null,
   street               varchar(100) not null,
   street2              varchar(100),
   city                 varchar(50) not null,
   state                varchar(50),
   country_code         varchar(2),
   country_name         varchar(50) not null,
   postal_code          varchar(20) not null,
   phone                varchar(50),
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   primary key (payment_id, buyer_account)
);

/*==============================================================*/
/* Table: transaction                                           */
/*==============================================================*/
create table transaction
(
   id                   int not null auto_increment,
   seller_id            int not null,
   order_id             bigint not null,
   item_id              bigint not null,
   item_title           varchar(100) not null,
   quantity_purchased   int not null,
   transaction_id       bigint not null,
   transaction_price    float(8,2) not null,
   transaction_site_id  varchar(20) not null,
   transaction_creation_date datetime not null,
   adjustment_amount    float(8,2),
   amount_paid          float(8,2),
   final_value_fee      float(8,2),
   buyer_account        varchar(50) not null,
   buyer_name           varchar(100) not null,
   checkout_message     varchar(400),
   status               int not null,
   creation_date        datetime not null,
   last_modified_date   timestamp not null,
   labels               varchar(200),
   feedback_status      int,
   feedback_recieved_message varchar(80),
   feedback_recieved_date datetime,
   feedback_left_message varchar(80),
   feedback_left_date   datetime,
   paid_time            datetime,
   payment_id           varchar(20),
   payment_fee          float(8,2),
   shipped_time         datetime,
   shipping_address     varchar(400),
   shipping_cost        float(8,2),
   shipping_carrier     varchar(20),
   shipping_tracking_id varchar(20),
   shipping_fee         float(8,2),
   primary key (id)
);

/*==============================================================*/
/* Index: transaction_U_IDX_IT                                  */
/*==============================================================*/
create unique index transaction_U_IDX_IT on transaction
(
   item_id,
   transaction_id
);

/*==============================================================*/
/* Index: transaction_IDX_P                                     */
/*==============================================================*/
create index transaction_IDX_P on transaction
(
   payment_id
);

/*==============================================================*/
/* Index: transaction_IDX_S                                     */
/*==============================================================*/
create index transaction_IDX_S on transaction
(
   shipping_tracking_id
);


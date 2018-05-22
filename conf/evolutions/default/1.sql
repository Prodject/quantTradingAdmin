# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table crypto_currencies (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  color                         varchar(255),
  constraint pk_crypto_currencies primary key (id)
);

create table exchange (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  code                          varchar(255),
  country                       varchar(255),
  color                         varchar(255),
  constraint pk_exchange primary key (id)
);

create table holding (
  id                            bigint auto_increment not null,
  robot_id                      bigint not null,
  robot_name                    varchar(30),
  exchange_id                   bigint not null,
  exchange_name                 varchar(30),
  user_id                       bigint not null,
  user_name                     varchar(255),
  symbol                        varchar(30),
  qty                           double(15,8) not null,
  price                         double,
  price_btc                     double,
  price_eth                     double,
  price_usdt                    double,
  amount_btc                    double,
  amount_eth                    double,
  amount_usdt                   double,
  amount                        double,
  price_actual                  double,
  amount_actual                 double,
  strategy                      varchar(255),
  created_at_timestamp          bigint not null,
  created_at                    datetime(6),
  updated_at                    datetime(6),
  constraint pk_holding primary key (id)
);

create table robot (
  id                            bigint auto_increment not null,
  uid                           varchar(255),
  name                          varchar(255),
  color                         varchar(255),
  created_at                    datetime(6),
  updated_at                    datetime(6),
  constraint pk_robot primary key (id)
);

create table role (
  id                            bigint auto_increment not null,
  role_short_name               varchar(255),
  role_display_name             varchar(255),
  info                          varchar(255),
  constraint pk_role primary key (id)
);

create table trade (
  id                            bigint auto_increment not null,
  side                          varchar(255),
  quantity                      double,
  trade_id                      varchar(255),
  robot_id                      bigint not null,
  robot_name                    varchar(30),
  user_id                       bigint not null,
  user_name                     varchar(255),
  exchange_id                   bigint not null,
  exchange_name                 varchar(30),
  price                         double,
  pair                          varchar(255),
  price_btc                     double,
  price_eth                     double,
  price_usdt                    double,
  amount                        double(15,8) not null,
  amount_btc                    double(15,8),
  amount_eth                    double(15,8),
  amount_usdt                   double(15,8),
  market_price                  double,
  market_amount                 double,
  fee                           double(15,8) not null,
  fee_btc                       double(15,8),
  fee_eth                       double(15,8),
  fee_usdt                      double(15,8),
  strategy                      varchar(255),
  created_at_timestamp          bigint not null,
  created_at                    datetime(6),
  updated_at                    datetime(6),
  constraint pk_trade primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  name                          varchar(255),
  password                      varchar(255),
  remember_token                varchar(255),
  role                          varchar(255),
  user_disabled                 tinyint(1) default 0 not null,
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists crypto_currencies;

drop table if exists exchange;

drop table if exists holding;

drop table if exists robot;

drop table if exists role;

drop table if exists trade;

drop table if exists user;


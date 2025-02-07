DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

create sequence configurations_seq start with 1 increment by 50;
create sequence server_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table configurations (configuration_cpu_cores bigint, configuration_price bigint, configuration_ram bigint, configuration_storage bigint, id_configuration bigint not null, configuration_name varchar(255), primary key (id_configuration));
create table instances (configuration_id bigint not null, server_id bigint, user_id bigint not null, instance_ip varchar(255), ip_hash varchar(255) not null, primary key (configuration_id, user_id, ip_hash));
create table server (id_server bigint not null, server_cpu_cores bigint, server_ram bigint, server_storage bigint, primary key (id_server));
create table users (id_user bigint not null, email varchar(255) not null, password varchar(255) not null, username varchar(255) not null, primary key (id_user));
alter table if exists instances add constraint FKdhavtbmhfxdt3m4hvo5q7pg8x foreign key (configuration_id) references configurations;
alter table if exists instances add constraint FK1ree01qeo0uyj802uf8jtv388 foreign key (server_id) references server;
alter table if exists instances add constraint FK8gmhkj8ibr3t27b5rpvxd3ac9 foreign key (user_id) references users;

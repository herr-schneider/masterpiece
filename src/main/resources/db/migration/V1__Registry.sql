DROP TABLE if exists packages;
DROP TABLE if exists addressees_table;
create table addressees_table (id bigint not null auto_increment, addressee_address varchar(255) not null, addressee_name varchar(255) not null, primary key (id));
create table packages (id bigint not null auto_increment, hu_num varchar(255), arrival date, doku_number varchar(10) not null, sender_address varchar(255) not null, sender_name varchar(255) not null, storage_status varchar(255), addressee_id bigint, primary key (id));
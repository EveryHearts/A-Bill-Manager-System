
##drop database if exists `bill_manager_db`;

create database `bill_manager_db`;

use `bill_manager_db`;

##show tables;

drop table if exists `user_info`;
create table `user_info`(
  `user_id` bigint not null auto_increment,
  `unit_id` bigint ,
  `role_code` int not null ,
  `user_name` varchar(10) not null,
  `phone` varchar(11) not null,
  `address` varchar(140),
  `age` int default 1,
  `gender` int default 0,
  `account` varchar(18) not null ,
  `password` varchar(100) not null,
  `status_code` int default 0,
  `create_date` datetime not null,
  `modify_date` datetime ,
  `description` varchar(140) default null,
  primary key(`user_id`)
)engine=InnoDB auto_increment=1000 default charset=utf8;

##update user_info set age = 24 where user_id = 1000;

drop table if exists `working_unit`;
create table `working_unit`(
  `unit_id` bigint not null auto_increment,
  `unit_name` varchar(30) not null ,
  `unit_address` varchar(140) ,
  `unit_phone` varchar(11) ,
  `unit_description` varchar(140) default null,
  `unit_principal_id` bigint not null,
  `register_date` datetime ,
  `modify_date` datetime ,
  primary key(`unit_id`),
  key `FK_unit_user_id` (`unit_principal_id`),
  constraint `FK_unit_user_id` foreign key (`unit_principal_id`)
  references `user_info`(`user_id`)
)engine=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `bill_kind`;
create table `bill_kind`(
  `bill_kind_id` bigint not null auto_increment,
  `unit_id` bigint default 0,
  `kind_name` varchar(30) not null,
  `status_code` int default 0,
  `create_date` datetime not null,
  `modify_date` datetime default null,
  primary key(`bill_kind_id`)
)engine=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `bill_book`;
create table `bill_book`(
  `bill_book_id` bigint not null auto_increment,
  `unit_id` bigint not null ,
  `bill_kind_id` bigint not null ,
  `begin_code` varchar(20) not null,
  `end_code` varchar(20) not null,
  `bill_copies` int default 1 comment "票据份数",
  `status_code` int default 0,
  `total_money` bigint default 0,
  `verify_money` bigint default 0,
  `create_date` datetime not null,
  `modify_date` datetime default null,
  `description` varchar(140) default null,
  primary key(`bill_book_id`),
  key `FK_book_unit_id` (`unit_id`),
  key `FK_book_kind_id` (`bill_kind_id`),
  constraint `FK_book_unit_id` foreign key (`unit_id`)
  references `working_unit`(`unit_id`),
  constraint `FK_book_kind_id` foreign key (`bill_kind_id`)
  references `bill_kind`(`bill_kind_id`)
)engine=InnoDB auto_increment=1 default charset=utf8;

alter table `bill_book` add column `operator_id` bigint default 0;

drop table if exists `bill_info`;
create table `bill_info`(
  `bill_id` bigint not null auto_increment,
  `bill_book_id` bigint not null,
  `bill_kind_id` bigint not null,
  `bill_code` varchar(20) not null,
  `bill_money` bigint default 0,
  `status_code` int default 0,
  `create_date` datetime not null,
  `modify_date` datetime default null,
  `description` varchar(140) default null,
  primary key(`bill_id`),
  key `FK_book_id` (`bill_book_id`),
  key `FK_kind_id` (`bill_kind_id`),
  constraint `FK_book_id` foreign key (`bill_book_id`)
  references `bill_book`(`bill_book_id`),
  constraint `FK_kind_id` foreign key (`bill_kind_id`)
  references `bill_kind`(`bill_kind_id`)
)engine=InnoDB auto_increment=1 default charset=utf8;

alter table `bill_info` add column `operator_id` bigint default 0;

drop table if exists `operation_record`;
create table `operation_record`(
  `record_id` bigint not null auto_increment,
  `unit_id` bigint ,
  `user_id` bigint ,
  `operation_sort` int ,
  `object_sort` int ,
  `object_id` bigint,
  `operation_date` datetime not null,
  primary key(`record_id`)
)engine=InnoDB auto_increment=1 default charset=utf8;

drop table if exists `message_info`;
create table `message_info`(
  `message_id` bigint not null auto_increment,
  `user_id` bigint not null ,
  `receptor_id` bigint not null,
  `content` varchar(140) not null,
  `message_sort` int default 0,
  `target_id` bigint ,
  `status_code` int default 0,
  `create_date` datetime not null,
  `modify_date` datetime default null,
  primary key(`message_id`),
  key `FK_message_user_id` (`user_id`),
  constraint `FK_message_user_id` foreign key (`user_id`)
  references `user_info`(`user_id`)
)engine=InnoDB auto_increment=1 default charset=utf8;

use `bill_manager_db`;


select * from user_info;

delete from user_info where user_id = 1001;

update user_info set gender = 0 where user_id = 1000;

select * from working_unit;

select * from operation_record;

insert into working_unit (`unit_name`,`unit_phone`,`unit_principal_id`,`registEr_date`)
values ("测试","13711116666",1000,now());

select * from bill_book;

select * from bill_book where unit_id = 1 order by create_date desc;

update bill_book set operator_id = 1000 where bill_book_id in (2,3,4);

select * from bill_kind ;

delete from bill_kind where bill_kind_id in (2,3);

select * from bill_info;

select * from operation_record where user_id = 1000;


drop database if exists JSchoolManagement;

create database JSchoolManagement;

use JSchoolManagement;

create table user (
	user_id int(11) not null auto_increment,
	user_firstname varchar(255) not null,
	user_lastname varchar(255) not null,
	user_phone varchar(31) not null,
	user_address varchar(255) not null,
	user_name varchar(255) not null,
	user_pass varchar(255) not null,
	user_type varchar(255) not null,
	constraint user_pk primary key(user_id)
);

create table class (
	class_id int(11) not null auto_increment,
	class_name varchar(255) not null,
	class_section varchar(255) not null,
	constraint user_pk primary key(class_id)
);

create table subject (
	subject_id int(11) not null auto_increment,
	subject_name varchar(255) not null,
	constraint user_pk primary key(subject_id)
);



insert into user(user_firstname,user_lastname,user_phone,user_address,user_name,user_pass,user_type) values
		("Hermione","Granger","8688383","Hogwarts","hgranger","crookshanks","Admin"),
		("Harry","Potter","8688381","Hogwarts","hpotter","hedwig","Teacher"),
		("Ron","Weasley","8688382","Hogwarts","rweasley","scabbers","Teacher");


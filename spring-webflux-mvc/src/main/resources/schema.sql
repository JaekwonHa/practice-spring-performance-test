drop table if exists person;
create table person (id bigint not null auto_increment, name varchar(255) not null, primary key (id)) engine=InnoDB;

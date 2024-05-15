/******************************************/
/*   数据库  */
/******************************************/

CREATE DATABASE IF NOT EXISTS demo;

USE demo;

CREATE USER 'demo' IDENTIFIED BY 'demo';

GRANT ALL ON demo.* TO demo;

FLUSH PRIVILEGES;

/******************************************/
/*   表   */
/******************************************/

CREATE TABLE IF NOT EXISTS `demo`
(
    `id`         bigint AUTO_INCREMENT    not null comment 'id',
    `comment`    varchar(255)             default null comment '备注',
    primary key (`id`)
);
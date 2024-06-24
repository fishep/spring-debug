-- DROP DATABASE `ads_demo`;
CREATE DATABASE IF NOT EXISTS `ads_demo`;

USE `ads_demo`;


-- ADS层（Application Data Service Layer）
create table ads_order_month
    MONTH
    price

select month, sum(price) from dws_order_product group by month
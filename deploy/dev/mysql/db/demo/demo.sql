CREATE DATABASE IF NOT EXISTS demo;
USE demo;
CREATE USER 'demo' IDENTIFIED BY 'demo';
GRANT ALL ON demo.* TO demo;
GRANT REPLICATION SLAVE ON *.* TO 'demo'@'%';
GRANT REPLICATION CLIENT ON *.* TO 'demo'@'%';
FLUSH PRIVILEGES;


SELECT ssl_type From mysql.user Where user="demo";

SELECT * From mysql.user Where user="demo";

ALTER USER 'demo'@'%' REQUIRE SSL;
ALTER USER 'demo'@'%' REQUIRE NONE;
FLUSH PRIVILEGES

show variables like "%ssl%";
show variables like "%zone%";

select id, user from information_schema.processlist;


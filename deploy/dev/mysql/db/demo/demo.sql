CREATE DATABASE IF NOT EXISTS demo;
USE demo;
CREATE USER 'demo' IDENTIFIED BY 'demo';
GRANT ALL ON demo.* TO demo;
GRANT REPLICATION SLAVE ON *.* TO 'demo'@'%';
GRANT REPLICATION CLIENT ON *.* TO 'demo'@'%';
FLUSH PRIVILEGES;

mysql> CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
mysql> GRANT SELECT, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'user' IDENTIFIED BY 'password';
mysql> FLUSH PRIVILEGES;

SELECT ssl_type From mysql.user Where user="demo";
SELECT * From mysql.user Where user="demo";
ALTER USER 'demo'@'%' REQUIRE SSL;
ALTER USER 'demo'@'%' REQUIRE NONE;
FLUSH PRIVILEGES

show variables like "%ssl%";
show variables like "%zone%";

select id, user from information_schema.processlist;

CREATE USER 'test' IDENTIFIED BY 'test';
SELECT * From mysql.user Where user="root" OR user = "demo" OR user = "test";
DROP USER 'test';
-- Public Key Retrieval is not allowed
-- ALTER USER 'demo'@'%' IDENTIFIED WITH mysql_native_password BY 'demo';
ALTER USER 'demo'@'%' IDENTIFIED WITH caching_sha2_password BY  'demo';
FLUSH PRIVILEGES; 



# 基于docker部署doris服务

### doris
```shell
#windows主机 找到文件 ~/.wslconfig 配置 kernelCommandLine = vsyscall=emulate

docker run -it --privileged --pid=host --name=change_count debian nsenter -t 1 -m -u -n -i sh
# docker 每次重启都要设置
#docker start -ai change_count
sysctl -q vm.max_map_count
sysctl -w vm.max_map_count=2000000

docker compose -p service up -d 

docker exec -it doris bash

curl http://127.0.0.1:8030/api/bootstrap

mysql -uroot -P9030 -h127.0.0.1
#查看 FE 运行状态
show frontends\G;
show backends\G;


CREATE DATABASE IF NOT EXISTS app_db;
USE app_db;
CREATE USER 'app_db' IDENTIFIED BY 'app_db';
GRANT ALL ON app_db.* TO app_db;
#GRANT REPLICATION SLAVE ON *.* TO 'app_db'@'%';
#GRANT REPLICATION CLIENT ON *.* TO 'app_db'@'%';
#FLUSH PRIVILEGES;

```
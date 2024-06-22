# 基于docker部署superset服务

### superset
```shell
docker compose -p service up -d 

docker exec -it superset bash

docker exec -it superset superset fab create-admin \
              --username admin \
              --firstname Superset \
              --lastname Admin \
              --email admin@superset.com \
              --password admin
              
docker exec -it superset superset db upgrade

# 示例数据，需要就执行
#docker exec -it superset superset load_examples

docker exec -it superset superset init

# http://localhost:8088/login/ -- u/p: [admin/admin]
# http://localhost:8088/swagger/v1 -- superset api

```

### How to extend this image
```shell
FROM apache/superset
# Switching to root to install the required packages
USER root
# Example: installing the MySQL driver to connect to the metadata database
# if you prefer Postgres, you may want to use `psycopg2-binary` instead
RUN pip install mysqlclient
# Example: installing a driver to connect to Redshift
# Find which driver you need based on the analytics database
# you want to connect to here:
# https://superset.apache.org/installation.html#database-dependencies
RUN pip install sqlalchemy-redshift

RUN pip install elasticsearch-dbapi
# 当前最高版本，只支持 es 7
#RUN pip install elasticsearch-dbapi==0.2.11
#RUN pip install elasticsearch==7.13.4

RUN pip install pydoris
#RUN pip install pydoris==1.0.5.3

# Switching back to using the `superset` user
USER superset
```

### 测试 superset 连接 mysql [参考](../../../doc/README.md)
```shell
# 访问 superset ，http://localhost:8088/login/ -- u/p: [admin/admin]
# 添加mysql数据源
# 。。。
```

### 测试 superset 连接 doris 
```shell

vim docker-compose.yml

# 启动superset，安装pydoris，重启superset
docker exec -it superset bash
pip install pydoris

# 访问 superset ，http://localhost:8088/login/ -- u/p: [admin/admin]
# 添加doris数据源
# 。。。

```

### 测试 superset 连接 elasticsearch 
```shell
# 过程一样，安装插件不一样 
pip install elasticsearch-dbapi

```
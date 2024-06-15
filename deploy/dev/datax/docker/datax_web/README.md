# 基于docker部署datax,datax_web服务

### datax_web
```shell
docker compose -p service up -d 

# 初始化数据库
docker cp ./bin/db/datax_web.sql mysql:/docker-entrypoint-initdb.d/
docker exec -it mysql bash
mysql -uroot -proot  < /docker-entrypoint-initdb.d/datax_web.sql

#docker cp datax:/home/datax/datax-web-2.1.2/modules/datax-admin/conf/bootstrap.properties ./conf

docker exec -it datax bash

# http://127.0.0.1:9527/index.html -- u/p: [admin/123456]

```
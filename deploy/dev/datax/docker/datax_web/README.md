# 基于docker部署datax,datax_web服务

### datax_web
```shell
# 需要启动mysql，并初始化数据库, mysql8 不行，datax-admin 使用的mysql 5.1.47
docker cp ./bin/db/datax_web.sql mysql:/docker-entrypoint-initdb.d/
docker exec -it mysql bash
mysql -uroot -proot  < /docker-entrypoint-initdb.d/datax_web.sql

docker compose -p service up -d 

#docker cp datax_web:/home/datax/datax-web-2.1.2/modules/datax-admin/conf/bootstrap.properties ./conf
#cat /home/datax/datax-web-2.1.2/modules/datax-admin/bin/console.out

docker exec -it datax_web bash

# http://127.0.0.1:9527/index.html -- u/p: [admin/123456]

```
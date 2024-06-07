# 基于docker部署seata服务

#### seata
```shell

# 需要先启动mysql，nacos服务
# 需要在nacos上添加配置 复制 ./config/seataServer.properties 即可

docker compose -p service up -d 

# http://seata.dev:7091 -- u/p: [seata/seata]

```
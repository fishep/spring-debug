# 基于docker部署redis服务

#### redis
```shell
docker compose -p service up -d 

docker exec -it redis bash

redis-cli

#跟随重定向
redis-cli -c 

```

### redis test
```shell
set key value
get key
del key
```

```shell

for i in {0..5}; do ping -c 1 redis-$i; done | grep PING | sed -r "s/PING redis-[0-9] \((.*)\): 56 data bytes/\1/"  | xargs -I {} echo {}:6379, | xargs

```
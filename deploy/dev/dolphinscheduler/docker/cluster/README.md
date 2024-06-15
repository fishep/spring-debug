# 基于docker部署dolphinscheduler服务

### dolphinscheduler
```shell
# 如果需要初始化或者升级数据库结构，需要指定profile为schema
docker-compose -p service --profile schema up -d

# 启动dolphinscheduler所有服务，指定profile为all
docker-compose -p service --profile all up -d

docker exec -it xxx bash

# http://localhost:12345/dolphinscheduler/ui -- u/p: [admin/dolphinscheduler123]

```

### 测试 dolphinscheduler
```shell
```
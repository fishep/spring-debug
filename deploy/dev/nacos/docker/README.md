# 基于docker部署nacos服务

#### standalone-derby
```shell

docker compose -p service up -d 

# http://localhost:8848/nacos/ -- u/p: [nacos/nacos]

```

#### standalone-mysql
```shell

# 需要先启动mysql 服务

docker compose -p service up -d 

# http://localhost:8848/nacos/ -- u/p: [nacos/nacos]

```

#### cluster-embedded
```shell

# 需要先启动mysql 服务

docker compose -p service up -d 

```



# 基于k8s部署mysql服务

### 目录结构
- mysql                      mysql单实例部署
- mysql-cluster              mysql集群部署，使用ndb
- mysql-replication          mysql集群部署，使用主从复制
- percona-xtradb-cluster     mysql集群部署，使用多主复制

### 创建命名空间
```shell
kubectl apply -f dev-namespace.yaml
```

#### mysql
```shell
kubectl apply -f mysql/
```

#### mysql-cluster
```shell
#@todo
kubectl apply -f mysql-cluster/
```

#### mysql-replication
```shell
#@todo
```

#### percona-xtradb-cluster
```shell
#@todo
```

### 数据库初始化
```shell
kubectl cp init-sql/ -n dev -c mysql mysql-0:docker-entrypoint-initdb.d/
kubectl exec -it -n dev pods/mysql-0 -- bash
cd /docker-entrypoint-initdb.d/init-sql && for SQL in *.sql; do mysql -uroot -p"root" < $SQL; done
```

###数据库测试
```shell
```
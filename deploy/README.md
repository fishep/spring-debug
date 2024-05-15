# 部署环境说明
> 应用的运行环境

### hosts文件的域名
- xxx.devops    devops域名

- xxx.dev    开发环境域名
- xxx.test   测试环境域名
- xxx.prod   生成环境域名

### 目录结构
```text
deploy                                            ----------- 当前目录
  |-- dev                                         ----------- 部署环境, dev,test,prod, devops
  |   |-- mysql                                   ----------- 服务名称, mysql, redis, ...
  |   |   |-- local                               ----------- 本地机器部署
  |   |   |   |-- standalone                      ----------- 单实例部署，名字任取
  |   |   |   |-- cluster                         ----------- 集群部署， 名字任取
  |   |   |-- docker                              ----------- docker部署
  |   |   |   |-- mysql                           ----------- 单实例部署，名字任取
  |   |   |   |-- mysql-cluster                   ----------- 使用分布式存储方案部署， 名字任取
  |   |   |   |-- mysql-replication               ----------- 使用主从的集群方案部署， 名字任取
  |   |   |   |-- percona-xtradb-cluster          ----------- 使用多主的集群方案部署， 名字任取
  |   |   |-- k8s                                 ----------- kubeneters部署
  |   |   |   |-- mysql-cluster                   ----------- 使用分布式存储方案部署， 名字任取
  |   |   |   |-- mysql-replication               ----------- 使用主从的集群方案部署， 名字任取
  |   |   |   |-- percona-xtradb-cluster          ----------- 使用多主的集群方案部署， 名字任取
  |-- local                                       ----------- 本地部署相关文档
  |-- docker                                      ----------- docker部署相关文档
  |-- k8s                                         ----------- k8s部署相关文档
```
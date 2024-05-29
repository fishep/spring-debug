# 基于docker部署elasticsearch服务

#### elasticsearch
```shell
docker compose -p service up -d 

docker exec -it elasticsearch bash

```

### elasticsearch test
```shell
#查看是否正常
curl localhost:9200

#查看节点健康状况
curl -XGET 'localhost:9200/_cat/health?v&pretty'

#查看当前节点的所有 Index
curl -XGET 'localhost:9200/_cat/indices?v&pretty'

#新建 Index
curl -XPUT 'localhost:9200/demo'

#新增记录
curl -XPOST -H 'Content-Type: application/json' 'localhost:9200/demo/_doc/1' -d '
{
  "id": 1,
  "comment": "this is a test"
}'

#查看记录
curl -XGET 'localhost:9200/demo/_doc/1'

#查看搜索
curl -XGET 'localhost:9200/demo/_search'

#搜索数据
curl -XPOST -H 'Content-Type: application/json' 'localhost:9200/demo/_search' -d '
{
  "query": {
    "term": {
      "comment": "test"
    }
  }
}'

#删除记录
curl -XDELETE 'localhost:9200/demo/_doc/1'

#删除 Index
curl -XDELETE 'localhost:9200/demo'

```

# 基于docker部署skywalking服务

#### skywalking
```shell

docker compose -p service up -d 

# http://localhost:8082/general -- u/p: [/]

```

### 客户端
```shell
-DSW_AGENT_NAME=sk
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=localhost:11800
-javaagent:E:\java\skywalking-agent\skywalking-agent.jar

-DSW_AGENT_NAME=sk
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=skoap.local:11800
-javaagent:E:\java\skywalking-agent\skywalking-agent.jar

-DSW_AGENT_NAME=sk
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=skoap.local:11800
-javaagent:/Users/fly.fei/bin/java/skywalking-agent/skywalking-agent.jar
```
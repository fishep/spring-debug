```shell
# on windows
startup.cmd -m standalone
shutdown.cmd

# on linux or mac
./startup.sh -m standalone
./shutdown.sh

# start with standalone mode
-Dnacos.standalone=true

```

```shell

# nacos 源码启动
cd nacos/consistency/src/main/proto
protoc --java_out ../java/ ./consistency.proto
protoc --java_out ../java/ ./Data.proto

```

```shell
-Dnacos.remote.client.grpc.server.check.timeout=10000
```

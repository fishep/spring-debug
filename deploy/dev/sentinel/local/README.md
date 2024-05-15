```shell
# start sentinel

# jdk8
java -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=localhost:8858 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
CTRL-C

# jdk18
java -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=localhost:8858 -Dproject.name=sentinel-dashboard -jar --add-exports=java.base/sun.net.util=ALL-UNNAMED sentinel-dashboard.jar
CTRL-C

```

```shell
# sentinel dashboard 源码启动
-Dcsp.sentinel.dashboard.server=localhost:8858
```



```shell

.\bin\mqnamesrv.cmd
.\bin\mqshutdown.cmd namesrv

.\bin\mqbroker.cmd -n localhost:9876 autoCreateTopicEnable=true
.\bin\mqshutdown.cmd broker

.\bin\tools.cmd  org.apache.rocketmq.example.quickstart.Producer
.\bin\tools.cmd  org.apache.rocketmq.example.quickstart.Consumer

```
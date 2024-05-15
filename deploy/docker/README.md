# docker

> 在docker部署服务和应用

# docker 常用指令

```shell
docker inspect redis | grep IPAddress
docker exec -it alpine bash

docker-compose -p grace up -d

docker manifest create fishep/alpine:latest fishep/alpine:arm64 fishep/alpine:amd64
docker manifest push fishep/alpine:latest
docker manifest rm fishep/alpine:latest

docker run -it --rm --name=alpine --network=grace_doris_net fishep/alpine:latest bash
docker cp ./test.csv alpine:/opt

docker run -it --name=alpine fishep/alpine:amd64 bash
docker commit alpine fishep/alpine:amd64
docker push fishep/alpine:amd64

```


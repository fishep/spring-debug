# docker for alpine 3.19.0
> alpine 3.19.0

### 更换镜像源
```shell
docker exec -it alpine bash

cat /etc/alpine-release

vim /etc/apk/repositories
https://mirrors.ustc.edu.cn/alpine/v3.19/main
https://mirrors.ustc.edu.cn/alpine/v3.19/community

apk update
apk add busybox-extras

# 跑不了
apk add openjdk8
java -version
# 跑不了
apk add python3
python3 --version

```


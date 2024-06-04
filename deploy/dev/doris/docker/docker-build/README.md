# 基于docker部署doris服务
> 未完成

### doris build 2.0.3 
```shell
# 下载doris-2.0.3 https://doris.apache.org/zh-CN/download/  
# to  resource/apache-doris-2.0.3-bin-arm64.tar.gz

cd ./docker-build/fe
docker build . -t fishep/doris:2.0.3-fe-arm64

cd ./docker-build/be
docker build . -t fishep/doris:2.0.3-be-arm64

#docker login
#docker push fishep/doris:2.0.3-fe-arm64
#docker push fishep/doris:2.0.3-be-arm64

cd ../cluster-2.0.3
docker compose -p service -f docker-compose-arm64.yaml up -d
```
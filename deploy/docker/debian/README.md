# docker for debian
> debian 12.5

### 更换镜像源
```shell
docker compose -p service up -d 

docker exec -it debian bash

cat /etc/debian_version

apt-get clean
mv /etc/apt/sources.list.d/debian.sources /etc/apt/sources.list.d/debian.sources.bak
#mv /etc/apt/sources.list.d/debian.sources.bak /etc/apt/sources.list.d/debian.sources
cat /etc/apt/sources.list
#cp /etc/apt/sources.list /etc/apt/sources.list.bak
echo "deb http://mirrors.aliyun.com/debian/ bookworm main non-free non-free-firmware" > /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/debian/ bookworm main non-free non-free-firmware" >> /etc/apt/sources.list
echo "deb http://mirrors.aliyun.com/debian-security/ bookworm-security main" >> /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/debian-security/ bookworm-security main" >> /etc/apt/sources.list
apt-get update

apt search openjdk-17-jdk
apt search python3
#apt-get install ca-certificates
apt-get install vim
apt-get install openjdk-17-jdk
apt-get install python3
ln -s /usr/bin/python3 /usr/bin/python

```

### 部署 DataX
```shell
docker cp datax.tar.gz debian:/opt

docker exec -it debian bash

#下载地址 https://datax-opensource.oss-cn-hangzhou.aliyuncs.com/202308/datax.tar.gz
tar -zxvf datax.tar.gz -C /opt
rm datax.tar.gz

cd /opt/datax
python datax.py {YOUR_JOB.json}
python bin/datax.py job/job.json

cd /opt/datax/bin
chmod a+x datax.py dxprof.py perftrace.py

datax.py job/job.json
```
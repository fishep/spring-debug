# docker
> 在docker部署php7.4

### 部署php7.4
```shell
docker-compose -p service up -d

docker exec -it php74 bash
cd ~ && mkdir .ssh
git config --global user.name "fly.fei"
git config --global user.email "fly.fei@feisu.com"
git config --global core.autocrlf input
git config --global core.safecrlf true
docker cp ~/.ssh/id_rsa php74:/root/.ssh
chmod 600 ~/.ssh/id_rsa

```


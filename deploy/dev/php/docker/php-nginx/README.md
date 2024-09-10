# 基于docker部署php服务

```shell
# https://github.com/TrafeX/docker-php-nginx
docker compose -p service up -d

#docker cp php:/var/www/ F:\project
#docker cp php:/var/www/ ~/project
#docker cp php:/etc/nginx/conf.d/default.conf ./etc/nginx/conf.d/default.conf
#docker cp php:/etc/php83/conf.d/custom.ini ./etc/php83/conf.d/custom.ini
#docker cp php:/etc/php83/php-fpm.d/www.conf ./etc/php83/php-fpm.d/www.conf

```
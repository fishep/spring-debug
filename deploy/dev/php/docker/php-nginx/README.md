# 基于docker部署php服务

```shell
# https://github.com/TrafeX/docker-php-nginx
docker compose -p service up -d

#docker cp php:/etc/nginx/conf.d/default.conf ./etc/nginx/conf.d/default.conf
#docker cp php:/etc/php83/conf.d/custom.ini ./etc/php83/conf.d/custom.ini
#docker cp php:/etc/php83/php-fpm.d/www.conf ./etc/php83/php-fpm.d/www.conf

#openssl req -x509 -nodes -newkey rsa:2048 -keyout https.key -out https.crt -subj "/CN=localhost" -days 5000
#openssl req -x509 -nodes -newkey rsa:2048 -keyout https.key -out https.crt -days 5000

```
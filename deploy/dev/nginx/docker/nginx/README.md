# docker
> 在docker部署nginx

### 部署nginx
```shell
docker-compose -p service up -d

docker exec -it nginx bash

curl http://localhost/index.html
curl http://localhost/index.php
curl http://localhost/phpinfo.php

curl http://project.dev/index.html
curl http://project.dev/index.php
curl http://project.dev/phpinfo.php

```

### 支持https
```shell
#openssl req -x509 -nodes -newkey rsa:2048 -keyout https.key -out https.crt -subj "/CN=localhost" -days 5000
#openssl req -x509 -nodes -newkey rsa:2048 -keyout https.key -out https.crt -days 9999
#openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout https.key -out https.crt
openssl req -x509 -nodes -days 365 -newkey rsa -keyout https.key -out https.crt

curl -k https://localhost/index.html
curl -k https://localhost/index.php
curl -k https://localhost/phpinfo.php

curl -k https://project.dev/index.html
curl -k https://project.dev/index.php
curl -k https://project.dev/phpinfo.php

# 8443:443
curl -k https://localhost:8443/index.html
curl -k https://localhost:8443/index.php
curl -k https://localhost:8443/phpinfo.php

curl -k https://project.dev:8443/index.html
curl -k https://project.dev:8443/index.php
curl -k https://project.dev:8443/phpinfo.php

```


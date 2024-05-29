# 基于docker部署superset服务

### superset
```shell
docker compose -p service up -d 

docker exec -it superset superset fab create-admin \
              --username admin \
              --firstname Superset \
              --lastname Admin \
              --email admin@superset.com \
              --password admin
              
docker exec -it superset superset db upgrade

docker exec -it superset superset load_examples

docker exec -it superset superset init

# http://localhost:8088/login/ -- u/p: [admin/admin]
# http://localhost:8088/swagger/v1 -- superset api

```

### How to extend this image
```shell
FROM apache/superset
# Switching to root to install the required packages
USER root
# Example: installing the MySQL driver to connect to the metadata database
# if you prefer Postgres, you may want to use `psycopg2-binary` instead
RUN pip install mysqlclient
# Example: installing a driver to connect to Redshift
# Find which driver you need based on the analytics database
# you want to connect to here:
# https://superset.apache.org/installation.html#database-dependencies
RUN pip install sqlalchemy-redshift
# Switching back to using the `superset` user
USER superset
```

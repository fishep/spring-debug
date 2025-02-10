# paimon
> paimon

### paimon
```shell

./bin/sql-client.sh
SET 'execution.runtime-mode' = 'batch';
SET 'execution.runtime-mode' = 'streaming';
SET 'execution.checkpointing.interval' = '10 s';

flink run \
    ./opt/flink/jar/paimon-flink-action-1.1-20250204.002533-45.jar \
    merge_into \
    --warehouse "file:/tmp/paimon" \
    --database "default" \
    --table "word_count_target" \
    --source_sql "CREATE CATALOG my_catalog WITH ( 'type'='paimon', 'warehouse'='file:/tmp/paimon');" \
    --source_sql "USE CATALOG my_catalog;" \
    --source_table "my_catalog.default.word_count" \
    --on "word_count_target.word = word_count.word" \
    --merge_actions not-matched-insert\
    --not_matched_insert_values "*" ;

    --source_sql "CREATE TABLE word_count_target ( word STRING PRIMARY KEY NOT ENFORCED, cnt BIGINT);" \

ALTER TABLE word_count_target SET ('consumer.expiration-time' = '1 d');
SELECT * FROM word_count_target /*+ OPTIONS('consumer-id' = 'myid', 'consumer.mode' = 'at-least-once') */;

```

### CDC
```shell

CREATE CATALOG my_catalog WITH (
    'type'='paimon',
    'warehouse'='file:/tmp/paimon'
);

USE CATALOG my_catalog;

CREATE TABLE paimon_flink_cdc_1 (
  `id` BIGINT PRIMARY KEY NOT ENFORCED,
  `comment` STRING
);
  
CREATE TABLE dwd_paimon_flink_cdc_1 (
  `id` BIGINT PRIMARY KEY NOT ENFORCED,
  `comment` STRING
);

insert into dwd_paimon_flink_cdc_1 select * from flink_cdc_1;

select * from dwd_paimon_flink_cdc_1;

#chown -R flink:flink tmp/paimon/default.db/paimon_flink_cdc_1/

flink run \
    ./opt/flink/jar/paimon-flink-action-1.1-20250204.002533-45.jar \
    mysql_sync_table \
    --warehouse file:/tmp/paimon \
    --database default \
    --table paimon_flink_cdc_1 \
    --primary_keys id \
    --mysql_conf hostname=mysql.dev \
    --mysql_conf username=demo \
    --mysql_conf password=demo \
    --mysql_conf database-name='demo' \
    --mysql_conf table-name='flink_cdc_1' \
    --source_sql "CREATE CATALOG my_catalog WITH ( 'type'='paimon', 'warehouse'='file:/tmp/paimon');" \
    --source_sql "USE CATALOG my_catalog;" \
    --table_conf bucket=4 \
    --table_conf changelog-producer=input \
    --table_conf sink.parallelism=4;
    
flink run \
    ./opt/flink/jar/paimon-flink-action-1.1-20250204.002533-45.jar \
    mysql_sync_table \
    --warehouse file:/tmp/paimon \
    --database default \
    --table paimon_flink_cdc_1 \
    --primary_keys id \
    --mysql_conf hostname=mysql.dev \
    --mysql_conf username=demo \
    --mysql_conf password=demo \
    --mysql_conf database-name='demo' \
    --mysql_conf table-name='flink_cdc_1' \
    --source_sql "CREATE CATALOG my_catalog WITH ( 'type'='paimon', 'warehouse'='file:/tmp/paimon');" \
    --source_sql "USE CATALOG my_catalog;" \
    --table_conf changelog-producer=input \
    --table_conf sink.parallelism=1;

```

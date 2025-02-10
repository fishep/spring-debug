# paimon
> paimon

### paimon
```shell
./bin/sql-client.sh -j /opt/flink/jar/paimon-flink-1.19-1.1-20250204.002533-45.jar -i init.sql

SELECT * FROM word_table;
SELECT * FROM word_count;

INSERT INTO word_table(`id`, `word`) values (1, 'hello');
INSERT INTO word_table(`id`, `word`) values (2, 'hello');
INSERT INTO word_table(`id`, `word`) values (3, 'world');
INSERT INTO word_table(`id`, `word`) values (4, 'hello world!');
INSERT INTO word_table(`id`, `word`) values (5, 'world');
INSERT INTO word_table(`id`, `word`) values (6, 'hello world!');
#UPDATE statement is not supported for streaming mode now.
#UPDATE word_table SET word = 'world' WHERE id = 4;
#DELETE statement is not supported for streaming mode now.
#DELETE FROM word_table WHERE id = 4;

INSERT INTO word_count SELECT word, COUNT(*) FROM word_table GROUP BY word;

```

### paimon-flink-action
```shell
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

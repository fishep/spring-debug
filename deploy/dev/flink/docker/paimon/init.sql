CREATE CATALOG paimon_catalog WITH (
    'type'='paimon',
    'warehouse'='file:/tmp/paimon'
);

USE CATALOG paimon_catalog;

-- USE demo;

SET sql-client.execution.result-mode=tableau;

SET execution.checkpointing.interval='10 s';

CREATE TABLE IF NOT EXISTS word_table (
    id BIGINT PRIMARY KEY NOT ENFORCED,
    word STRING
);

CREATE TABLE IF NOT EXISTS word_count (
    word STRING PRIMARY KEY NOT ENFORCED,
    cnt BIGINT
);

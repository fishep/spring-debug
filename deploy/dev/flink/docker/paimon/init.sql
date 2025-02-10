CREATE CATALOG my_catalog WITH (
    'type'='paimon',
    'warehouse'='file:/tmp/paimon'
);

USE CATALOG my_catalog;

USE demo;

SET sql-client.execution.result-mode=tableau;

SET execution.checkpointing.interval='10 s';
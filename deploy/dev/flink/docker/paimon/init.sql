CREATE CATALOG my_catalog WITH (
    'type'='paimon',
    'warehouse'='file:/opt/flink/tmp/paimon'
);

USE CATALOG my_catalog;

SET sql-client.execution.result-mode=tableau;

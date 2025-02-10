show catalogs;

CREATE CATALOG `paimon_catalog` PROPERTIES (
    "type" = "paimon",
    "warehouse" = "file:/tmp/paimon"
);

USE CATALOG paimon_catalog;

SELECT * FROM paimon_catalog.demo.word_table;

SELECT * FROM paimon_catalog.demo.word_count;


DROP TABLE IF EXISTS `word_count`;
CREATE TABLE `word_count` (
  `word` varchar(255) NOT NULL COMMENT '单词',
	`count` bigint NOT NULL COMMENT '单词出现的次数'
)UNIQUE KEY(`word`)
COMMENT "table word_count"
DISTRIBUTED BY HASH(`word`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

SELECT * FROM word_count;

INSERT INTO `word_count` SELECT word, cnt AS count FROM paimon_catalog.demo.word_count;
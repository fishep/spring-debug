package com.fishep.spring.debug.jdbc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.fishep.spring.debug.jdbc.mapper")
public class MybatisConfig {
}

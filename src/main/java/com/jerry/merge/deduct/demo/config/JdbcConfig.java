package com.jerry.merge.deduct.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * @author JerryZeng
 * @date 2020/7/31
 */
@Configuration
@EnableJdbcRepositories("com.jerry.merge.deduct.demo.repositories")
public class JdbcConfig {

}

package com.li.blog.config.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author li
 * @version 1.0
 * @date 18-8-10 下午4:54
 **/
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

}

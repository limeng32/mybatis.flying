package indi.mybatis.flying.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	@Bean("dataSource1")
	@Primary
	@ConfigurationProperties(prefix = "spring.dataSource1")
	public DataSource dataSource1() {
		DataSource dataSource = DataSourceBuilder.create().build();
		return dataSource;
	}

	@Bean("dataSource2")
	@ConfigurationProperties(prefix = "spring.dataSource2")
	public DataSource dataSource2() {
		DataSource dataSource2 = DataSourceBuilder.create().build();
		return dataSource2;
	}
}

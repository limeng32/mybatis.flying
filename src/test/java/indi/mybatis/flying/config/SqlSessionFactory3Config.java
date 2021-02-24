package indi.mybatis.flying.config;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.io.VFS;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = { "indi.mybatis.flying.mapper3" }, sqlSessionFactoryRef = "sqlSessionFactoryExamine")
public class SqlSessionFactory3Config {

	@ConfigurationProperties(prefix = "spring.data-source-examine")
	@Bean("dataSourceExamine")
	public DataSource dataSourceExamine() {
		return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
	}

	@Bean(name = "sqlSessionFactoryExamine")
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		// 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
		sessionFactory.setDataSource(dataSourceExamine());
		VFS.addImplClass(SpringBootVFS.class);
		// 扫描Model
		sessionFactory.setTypeAliasesPackage("indi.mybatis.flying");
		sessionFactory.setConfigLocation(new ClassPathResource("Configuration.xml"));
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] ra2 = resolver.getResources("classpath*:indi/mybatis/flying/mapper3/*.xml");
		Resource[] ra = (Resource[]) ArrayUtils.addAll(null, ra2);
		// 扫描映射文件
		sessionFactory.setMapperLocations(ra);
		return sessionFactory;
	}

}

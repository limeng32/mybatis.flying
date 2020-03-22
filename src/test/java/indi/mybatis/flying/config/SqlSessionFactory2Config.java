package indi.mybatis.flying.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.io.VFS;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = { "indi.mybatis.flying.mapper2" }, sqlSessionFactoryRef = "sqlSessionFactory2")
public class SqlSessionFactory2Config {

	@Autowired
	@Qualifier("dataSource2")
	private DataSource dataSource2;

	@Bean(name = "sqlSessionFactory2")
	@Primary
	public SqlSessionFactoryBean createSqlSessionFactory2Bean() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		/** 设置datasource */
		sqlSessionFactoryBean.setDataSource(dataSource2);
		VFS.addImplClass(SpringBootVFS.class);
		/** 设置mybatis configuration 扫描路径 */
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("Configuration.xml"));
		/** 设置typeAlias 包扫描路径 */
		sqlSessionFactoryBean.setTypeAliasesPackage("indi.mybatis.flying");
		/** 添加mapper 扫描路径 */
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] ra1 = resolver.getResources("classpath*:indi/mybatis/flying/mapper*/*.xml");
		sqlSessionFactoryBean.setMapperLocations(ra1); // 扫描映射文件
		return sqlSessionFactoryBean;
	}

}

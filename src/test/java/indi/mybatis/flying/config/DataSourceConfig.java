package indi.mybatis.flying.config;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
public class DataSourceConfig {

	@Bean("dataSource1")
	@Primary
	public AtomikosDataSourceBean dataSource1() {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSource(xaDataSource1());
		ds.setUniqueResourceName("testdb");
		ds.setMinPoolSize(1);
		ds.setMaxPoolSize(3);
		ds.setMaxIdleTime(60);
		return ds;
	}

	@Bean("dataSource2")
	public DataSource dataSource2() {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSource(xaDataSource2());
		ds.setUniqueResourceName("testdb2");
		ds.setMinPoolSize(1);
		ds.setMaxPoolSize(3);
		ds.setMaxIdleTime(60);
		return ds;
	}

	@ConfigurationProperties(prefix = "spring.data-source-examine") // dataSource2
	@Bean("dataSourceExamine")
	public DataSource dataSourceExamine() {
		return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
	}

	public XADataSource xaDataSource1() {
		DruidXADataSource xaDataSource = new DruidXADataSource();
		xaDataSource.setUrl(
				"jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MV_STORE=FALSE;MVCC=FALSE;FILE_LOCK=NO");
		xaDataSource.setUsername("sa1");
		xaDataSource.setPassword("null");
		return xaDataSource;
	}

	public XADataSource xaDataSource2() {
		DruidXADataSource xaDataSource = new DruidXADataSource();
		xaDataSource.setUrl(
				"jdbc:h2:mem:testdb2;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MV_STORE=FALSE;MVCC=FALSE;FILE_LOCK=NO");
		xaDataSource.setUsername("sa1");
		xaDataSource.setPassword("null");
		return xaDataSource;
	}

	@Bean(name = "springTransactionManager")
	// 用户数据源的事务管理器
	public JtaTransactionManager accountTxManager() {
		UserTransactionManager atomikosTransactionManager = new UserTransactionManager();
		atomikosTransactionManager.setForceShutdown(true);

		UserTransaction atomikosUserTransaction = new UserTransactionImp();
		try {
			atomikosUserTransaction.setTransactionTimeout(300);
		} catch (SystemException e) {
		}

		JtaTransactionManager springTransactionManager = new org.springframework.transaction.jta.JtaTransactionManager();
		springTransactionManager.setTransactionManager(atomikosTransactionManager);
		springTransactionManager.setUserTransaction(atomikosUserTransaction);
		springTransactionManager.setAllowCustomIsolationLevels(true);

		return springTransactionManager;
	}
}

package indi.mybatis.flying.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration("initialConfig")
@DependsOn("dataSourceConfig")
public class InitialConfig {

	@Autowired
	@Qualifier("dataSource1")
	private DataSource dataSource1;

	@Autowired
	@Qualifier("dataSource2")
	private DataSource dataSource2;

	@Bean(name = "jdbcTemplate1")
	@Primary
	public JdbcTemplate jdbcTemplate1() {
		return new JdbcTemplate(dataSource1);
	}

	@Bean(name = "jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2() {
		return new JdbcTemplate(dataSource2);
	}

	@PostConstruct
	private void init1() throws IOException {
		initCustomerDataSource1();
		initCustomerDataSource2();
	}

	private void initCustomerDataSource1() throws IOException {
		try {
			String s = getFileAsOneLine("/INIT_TABLE.sql");
			jdbcTemplate1().execute(s);
		} catch (Exception e) {
		}
	}

	private void initCustomerDataSource2() throws IOException {
		try {
			String s = getFileAsOneLine("/INIT_TABLE2.sql");
			jdbcTemplate2().execute(s);
		} catch (Exception e) {
		}
	}

	public String getFileAsOneLine(String path) throws IOException {
		StringBuilder sb = new StringBuilder();
		String str = null;
		try (InputStream is = getClass().getResourceAsStream(path);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		}
		return sb.toString();
	}
}

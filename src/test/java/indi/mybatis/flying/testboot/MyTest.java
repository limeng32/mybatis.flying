package indi.mybatis.flying.testboot;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DatabaseTearDowns;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.mapper.AccountMapper;
import indi.mybatis.flying.mapper2.Account2Mapper;
import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service2.Account2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class MyTest {

	@Autowired
	@Qualifier("dataSource1")
	private DataSource dataSource1;

	@Autowired
	@Qualifier("dataSource2")
	private DataSource dataSource2;

	@Autowired
	@Qualifier("sqlSessionFactory")
	private SqlSessionFactoryBean sqlSessionFactory;
	
	@Autowired
	@Qualifier("sqlSessionFactory2")
	private SqlSessionFactoryBean sqlSessionFactory2;

	@Autowired
	private AccountMapper accountMapper;
	
	@Autowired
	private Account2Mapper account2Mapper;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private Account2Service account2Service;

	@Test
	public void test1() {
		Assert.assertNotNull(dataSource1);
		Assert.assertNotNull(dataSource2);
		Assert.assertNotNull(sqlSessionFactory);
		Assert.assertNotNull(sqlSessionFactory2);
		Assert.assertNotNull(accountMapper);
		Assert.assertNotNull(account2Mapper);
		Assert.assertNotNull(accountService);
		Assert.assertNotNull(account2Service);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	public void test2() {
		Assert.assertTrue(true);
		Account_ account = accountService.select(1);
		System.out.println(JSONObject.toJSONString(account));
		Account2_ a2 = new Account2_();
		a2.setEmail("l@x.com");
		Account2_ account2 = account2Service.selectOne(a2);
		System.out.println(JSONObject.toJSONString(account2));
	}
}

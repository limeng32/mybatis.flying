package indi.mybatis.flying.test;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

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
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.service2.Account2Service;
import indi.mybatis.flying.service2.LoginLogSource2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class AccountTypeHandlerTest {

	@Autowired
	private DataSource dataSource1;

	@Autowired
	private AccountService accountService;

	@Autowired
	private Account2Service account2Service;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private LoginLogSource2Service loginLogSource2Service;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource1);
	}

	/* 测试AccountTypeHandler的功能 */
	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTypeHandlerTest/testAccountTypeHandler.datasource.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTypeHandlerTest/testAccountTypeHandler.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTypeHandlerTest/testAccountTypeHandler.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTypeHandlerTest/testAccountTypeHandler.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTypeHandlerTest/testAccountTypeHandler.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTypeHandlerTest/testAccountTypeHandler.datasource2.result.xml") })
	public void testAccountTypeHandler() {
		LoginLogSource2 longinLogSource = loginLogSource2Service.select(22);
		Assert.assertNotNull(longinLogSource);
		Assert.assertNotNull(longinLogSource.getAccount());
		Assert.assertEquals("ann@live.cn", longinLogSource.getAccount().getEmail());
	}
}

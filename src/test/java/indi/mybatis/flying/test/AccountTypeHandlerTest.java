package indi.mybatis.flying.test;

import java.util.Collection;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.service2.Account2Service;
import indi.mybatis.flying.service2.LoginLogSource2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
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
//	@Test
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
		LoginLogSource2 longinLogSource0 = loginLogSource2Service.selectWithoutAccount(22);
		Assert.assertNull(longinLogSource0.getAccount());

		LoginLogSource2 longinLogSource = loginLogSource2Service.select(22);
		Assert.assertNotNull(longinLogSource);
		Assert.assertNotNull(longinLogSource.getAccount());
		Assert.assertEquals("ann@live.cn", longinLogSource.getAccount().getEmail());

		Account_ ac = new Account_();
		ac.setId(1L);
		LoginLogSource2 l2c = new LoginLogSource2();
		l2c.setAccount(ac);
		Collection<LoginLogSource2> loginLogSource2C = loginLogSource2Service.selectAll(l2c);
		Assert.assertEquals(2, loginLogSource2C.size());
		for (LoginLogSource2 e : loginLogSource2C) {
			Assert.assertEquals("ann@live.cn", e.getAccount().getEmail());
		}

		LoginLogSource2 loginLogSource2 = loginLogSource2Service.select(24);
		Assert.assertNull(loginLogSource2.getAccount());

		LoginLogSource2 loginLogSource3 = loginLogSource2Service.select(25);
		Assert.assertNull(loginLogSource3.getAccount());

		Account_ ac2 = new Account_();
		ac2.setId(2L);
		LoginLogSource2 l2c2 = new LoginLogSource2();
		l2c2.setAccount(ac2);
		LoginLogSource2 loginLogSource4 = loginLogSource2Service.selectOne(l2c2);
		loginLogSource4 = loginLogSource2Service.selectOne(l2c2);
		loginLogSource4 = loginLogSource2Service.selectOne(l2c2);
		loginLogSource4 = loginLogSource2Service.selectOne(l2c2);
		loginLogSource4 = loginLogSource2Service.selectOne(l2c2);
		Assert.assertEquals("bob@live.cn", loginLogSource4.getAccount().getEmail());

		Account_ ac3 = new Account_();
		ac3.setId(1L);
		LoginLogSource2 l2c3 = new LoginLogSource2();
		l2c3.setAccount(ac3);
		l2c3.setLoginIP("ip1");
		int i = loginLogSource2Service.count(l2c3);
		Assert.assertEquals(1, i);

		Account_ account2 = accountService.select(2);
		loginLogSource2.setAccount(account2);
		loginLogSource2Service.update(loginLogSource2);

		loginLogSource4.setAccount(null);
		loginLogSource2Service.updatePersistent(loginLogSource4);

		Account_ account = accountService.select(1);
		loginLogSource2Service.loadAccount(account, new LoginLogSource2());
		Assert.assertEquals(2, account.getLoginLogSource2().size());
	}
}

package indi.mybatis.flying.test;

import java.util.Collection;

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

import com.alibaba.fastjson.JSONObject;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.DetailService;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.utils.FlyingManager;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1" })
@ContextConfiguration("classpath:spring-test.xml")
public class IgnoreTest {

	@Autowired
	private DataSource dataSource1;

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private DetailService detailService;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource1);
		// 测试安全源码
		Assert.assertEquals(1, accountService.selectCheckHealth());
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/ignoreTest/testIgnore.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/ignoreTest/testIgnore.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/ignoreTest/testIgnore.result.xml")
	public void testIgnore() {
		Account_ ac = new Account_();
		Collection<Account_> accountC = accountService.selectAllPrefixIgnore(ac);
		System.out.println("1::" + JSONObject.toJSONString(accountC));
		Account_[] accounts = accountC.toArray(new Account_[accountC.size()]);
		Assert.assertNotNull(accounts[0].getPassword());
		Assert.assertNull(accounts[0].getName());

		LoginLog_ lc = new LoginLog_();
		Collection<LoginLog_> loginLogC = loginLogService.selectAllPrefixIgnore(lc);
		System.out.println("2::" + JSONObject.toJSONString(loginLogC));
		LoginLog_[] loginLogs = loginLogC.toArray(new LoginLog_[loginLogC.size()]);
		Assert.assertNull(loginLogs[0].getAccount().getName());
		
		FlyingModel fm = FlyingManager.getFlyingModelFromCache("indi.mybatis.flying.mapper.LoginLogMapper.selectAllPrefixIgnore");
		System.out.println("fm::" + JSONObject.toJSONString(fm));
	}
}

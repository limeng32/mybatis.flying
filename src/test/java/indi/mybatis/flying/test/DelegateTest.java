package indi.mybatis.flying.test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.pagination.Order;
import indi.mybatis.flying.pagination.Page;
import indi.mybatis.flying.pagination.PageParam;
import indi.mybatis.flying.pagination.SortParam;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.pojo.condition.LoginLog_Condition;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.LoginLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class DelegateTest {

	@Autowired
	private DataSource dataSource1;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private LoginLogService loginLogService;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource1);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/delegateTest/testDelegate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegate.xml")
	public void testDelegate() {
		LoginLog_ l = new LoginLog_();
		l.setDelegateAccountId(11L);
		LoginLog_ loginLog = loginLogService.selectOnePrefix(l);
		Assert.assertNull(loginLog);

		int c = loginLogService.count(l);
		Assert.assertEquals(0, c);

		LoginLog_ loginLog2 = loginLogService.selectPrefix(1);
		LoginLog_Condition l2 = new LoginLog_Condition();
		l2.setDelegateAccountId(loginLog2.getDelegateAccountId());
		l2.setAccount(loginLog2.getAccount());
		l2.setLimiter(new PageParam(1, 2));
		Collection<LoginLog_> loginLogC = loginLogService.selectAllPrefix(l2);
		Page<LoginLog_> page2 = new Page<>(loginLogC, l2.getLimiter());
		System.out.println("::" + JSONObject.toJSONString(page2));

	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateUpdate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateUpdate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegateUpdate.xml")
	public void testDelegateUpdate() {
		LoginLog_ loginLog = loginLogService.select(1);
		loginLog.setAccount(null);
		loginLog.setDelegateAccountId(2L);
		loginLogService.update(loginLog);

		// 当login.getAccount()不为null时，修改accountId不起作用
		LoginLog_ loginLog2 = loginLogService.select(1);
		loginLog2.setDelegateAccountId(11L);
		loginLogService.update(loginLog2);

	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateUpdatePersistent.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateUpdatePersistent.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegateUpdatePersistent.xml")
	public void testDelegateUpdatePersistent() {
		LoginLog_ loginLog = loginLogService.select(1);
		loginLog.setAccount(null);
		loginLog.setDelegateAccountId(2L);
		loginLog.setLoginIP(null);
		loginLogService.updatePersistent(loginLog);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateInsert.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateInsert.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegateInsert.xml")
	public void testDelegateInsert() {
		LoginLog_ loginLog = new LoginLog_();
		loginLog.setId(1);
		loginLog.setLoginIP("0.0.0.1");
		loginLog.setDelegateAccountId(1L);
		loginLogService.insert(loginLog);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateInsertBatch.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/delegateTest/testDelegateInsertBatch.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegateInsertBatch.xml")
	public void testDelegateInsertBatch() {
		Collection<LoginLog_> c = new LinkedList<>();
		LoginLog_ l1 = new LoginLog_();
		l1.setLoginIP("0.0.0.1");
		l1.setDelegateAccountId(1L);
		c.add(l1);
		LoginLog_ l2 = new LoginLog_();
		l2.setLoginIP("0.0.0.2");
		l2.setDelegateAccountId(2L);
		c.add(l2);
		loginLogService.insertBatch(c);
	}

	// 测试delegate出现在CinditionMapper时的情况
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegateCondition.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/delegateTest/testDelegateCondition.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegateCondition.xml")
	public void testDelegateCondition() {
		LoginLog_Condition lc = new LoginLog_Condition();
		lc.setIpLikeFilter("0.1");
		Account_ a = new Account_();
		a.setName("ann");
		a.setRoleIdDelegate(3L);
		lc.setAccount(a);
		LoginLog_ loginLog = loginLogService.selectOne(lc);
		Assert.assertEquals("0.0.0.1", loginLog.getLoginIP());

		a.setRoleIdDelegate(4L);
		loginLog = loginLogService.selectOne(lc);
		Assert.assertNull(loginLog);
	}
	
	// 测试delegate与多对一查询是否兼容
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/delegateTest/testDelegate2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/delegateTest/testDelegate2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/delegateTest/testDelegate2.xml")
	public void testDelegate2() {
		Account_Condition a = new Account_Condition();
		a.setRoleIdDelegate(3L);
		a.setLimiter(new PageParam(1, 10));
		a.setSorter(new SortParam(new Order("id", Conditionable.Sequence.ASC)));
		List<Account_> accountC = accountService.selectAll(a);
		Page<Account_> page = new Page<>(accountC, a.getLimiter());
		System.out.println(JSONObject.toJSONString(page));
	}
}

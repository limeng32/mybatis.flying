package indi.mybatis.flying.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.Role_;
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
public class ConditionInTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginLogService loginLogService;

	/** 测试无外键情况下condition:in功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn.xml")
	public void testConditionIn() {
		Account_Condition ac = new Account_Condition();
		List<String> nameC = new ArrayList<>();
		nameC.add("ann");
		ac.setNameIn(nameC);
		Collection<Account_> c = accountService.selectAll(ac);
		Assert.assertEquals(1, c.size());

		nameC.add("bob");
		Collection<Account_> c2 = accountService.selectAll(ac);
		Assert.assertEquals(2, c2.size());
		
		ac.setNameIn(null);
		ac.setNameNotIn(nameC);
		c2 = accountService.selectAll(ac);
		Assert.assertEquals(1, c2.size());

		List<String> nameC2 = new ArrayList<>();
		ac.setNameIn(nameC2);
		ac.setNameNotIn(null);
		Collection<Account_> c3 = accountService.selectAll(ac);
		Assert.assertEquals(0, c3.size());

		ac.setNameIn(null);
		ac.setNameNotIn(nameC2);
		c3 = accountService.selectAll(ac);
		Assert.assertEquals(3, c3.size());
	}

	/** 测试有外键情况下condition:in功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn2.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn2.xml")
	public void testConditionIn2() {
		LoginLog_Condition lc = new LoginLog_Condition();
		List<String> loginIpC = new ArrayList<>();
		loginIpC.add("11");
		loginIpC.add("22");
		lc.setLoginIPIn(loginIpC);
		Account_Condition ac = new Account_Condition();
		List<String> nameC = new ArrayList<>();
		nameC.add("ann");
		nameC.add("bob");
		ac.setNameIn(nameC);
		lc.setAccount(ac);
		Collection<LoginLog_> c = loginLogService.selectAll(lc);
		Assert.assertEquals(2, c.size());
		int count = loginLogService.count(lc);
		Assert.assertEquals(2, count);

		Collection<LoginLog_> c2 = loginLogService.selectAllPrefix(lc);
		Assert.assertEquals(2, c2.size());
		int count2 = loginLogService.count(lc);
		Assert.assertEquals(2, count2);
	}

	/** 测试无外键情况下condition:in功能且变量类型为数字的情况 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn3.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn3.xml")
	public void testConditionIn3() {
		Account_Condition ac = new Account_Condition();
		List<Integer> opLockC = new ArrayList<>();
		opLockC.add(1);
		opLockC.add(2);
		ac.setOpLockIn(opLockC);
		int count = accountService.count(ac);
		Assert.assertEquals(2, count);
	}

	/** 测试无外键情况下condition:in功能且变量类型为时间的情况 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn4.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn4.xml")
	public void testConditionIn4() {
		LoginLog_Condition lc = new LoginLog_Condition();
		List<Date> timeC = new ArrayList<>();
		Calendar c = Calendar.getInstance(), c2 = Calendar.getInstance();
		c.clear();
		c2.clear();
		c.set(1970, 0, 1, 8, 0, 0);
		c2.set(1970, 0, 1, 8, 0, 1);
		timeC.add(c.getTime());
		timeC.add(c2.getTime());
		lc.setLoginTimeIn(timeC);
		int count = loginLogService.count(lc);
		Assert.assertEquals(2, count);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn5.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn5.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionInTest/testConditionIn5.xml")
	public void testConditionIn5() {
		Account_Condition ac = new Account_Condition();
		Account_Condition ac2 = new Account_Condition();
		Account_Condition ac3 = new Account_Condition();
		List<Integer> roleIds = new ArrayList<>();
		roleIds.add(1);
		roleIds.add(2);
		ac.setRoleIdIn(roleIds);
		Role_ r1 = new Role_();
		r1.setId(1);
		Role_ r2 = new Role_();
		r2.setId(2);
		int c = accountService.count(ac);
		Assert.assertEquals(2, c);
		ac3.setRoleIdNotIn(roleIds);
		int c3 = accountService.count(ac3);
		Assert.assertEquals(1, c3);
		ac2.setRoleIdNotIn(roleIds);
		Account_ account = accountService.selectOne(ac2);
		Assert.assertEquals("role3", account.getRole().getName());
		LoginLog_ l = new LoginLog_();
		l.setAccount(ac2);
		LoginLog_ loginLog = loginLogService.selectOne(l);
		Assert.assertEquals("role3", loginLog.getAccount().getRole().getName());
		List<String> nameIn = new ArrayList<>();
		nameIn.add("ann");
		nameIn.add("bob");
		Account_Condition ac4 = new Account_Condition();
		ac4.setNameIn(nameIn);
		LoginLog_ l2 = new LoginLog_();
		l2.setAccount(ac4);
		int c4 = loginLogService.count(l2);
		Assert.assertEquals(2, c4);
	}
}
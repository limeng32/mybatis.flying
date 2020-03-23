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
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
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
public class ConditionNotInTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginLogService loginLogService;

	/** 测试无外键情况下condition:notIn功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn.xml")
	public void testConditionNotIn() {
		Account_Condition ac = new Account_Condition();
		List<String> nameC = new ArrayList<>();
		nameC.add("ann");
		ac.setNameNotIn(nameC);
		Collection<Account_> c = accountService.selectAll(ac);
		Assert.assertEquals(1, c.size());

		nameC.add("bob");
		Collection<Account_> c2 = accountService.selectAll(ac);
		Assert.assertEquals(0, c2.size());

		List<String> nameC2 = new ArrayList<>();
		ac.setNameNotIn(nameC2);
		Collection<Account_> c3 = accountService.selectAll(ac);
		Assert.assertEquals(2, c3.size());
	}

	/** 测试有外键情况下condition:notIn功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn2.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn2.xml")
	public void testConditionNotIn2() {
		LoginLog_Condition lc = new LoginLog_Condition();
		Account_Condition ac = new Account_Condition();
		List<String> nameC = new ArrayList<>();
		nameC.add("ann");
		nameC.add("bob");
		ac.setNameNotIn(nameC);
		lc.setAccount(ac);
		Collection<LoginLog_> c = loginLogService.selectAll(lc);
		Assert.assertEquals(0, c.size());
		int count = loginLogService.count(lc);
		Assert.assertEquals(0, count);

		Collection<LoginLog_> c2 = loginLogService.selectAllPrefix(lc);
		Assert.assertEquals(0, c2.size());
		int count2 = loginLogService.count(lc);
		Assert.assertEquals(0, count2);
	}

	/** 测试无外键情况下condition:notIn功能且变量类型为数字的情况 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn3.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn3.xml")
	public void testConditionNotIn3() {
		Account_Condition ac = new Account_Condition();
		List<Integer> opLockC = new ArrayList<>();
		opLockC.add(1);
		opLockC.add(2);
		ac.setOpLockNotIn(opLockC);
		int count = accountService.count(ac);
		Assert.assertEquals(0, count);
	}

	/** 测试无外键情况下condition:notIn功能且变量类型为时间的情况 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn4.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionNotInTest/testConditionIn4.xml")
	public void testConditionNotIn4() {
		LoginLog_Condition lc = new LoginLog_Condition();
		List<Date> timeC = new ArrayList<>();
		Calendar c = Calendar.getInstance(), c2 = Calendar.getInstance();
		c.clear();
		c2.clear();
		c.set(1970, 0, 1, 8, 0, 0);
		c2.set(1970, 0, 1, 8, 0, 1);
		timeC.add(c.getTime());
		timeC.add(c2.getTime());
		lc.setLoginTimeNotIn(timeC);
		int count = loginLogService.count(lc);
		Assert.assertEquals(0, count);
	}
}

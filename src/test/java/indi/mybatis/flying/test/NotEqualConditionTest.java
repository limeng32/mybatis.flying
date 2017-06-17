package indi.mybatis.flying.test;

import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.condition.LoginLog_Condition;
import indi.mybatis.flying.service.LoginLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class NotEqualConditionTest {

	@Autowired
	private LoginLogService loginLogService;

	@BeforeClass
	public static void prepareDatabase() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/H2_TYPE.sql")
				.addScript("classpath:/INIT_TABLE.sql").build();
	}

	/** 测试condition:greaterThan功能 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/notEqualConditionTest/testConditionGreaterThan.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/notEqualConditionTest/testConditionGreaterThan.xml")
	public void testConditionGreaterThan() {
		LoginLog_Condition lc = new LoginLog_Condition(), lc2 = new LoginLog_Condition(),
				lc3 = new LoginLog_Condition(), lc4 = new LoginLog_Condition();
		lc.setLoginTimeGreaterThan(new Date(1));
		lc.setIdGreaterThan(0);
		Collection<LoginLog_> c = loginLogService.selectAll(lc);
		Assert.assertEquals(1, c.size());
		LoginLog_[] loginlogs = c.toArray(new LoginLog_[c.size()]);
		Assert.assertEquals("0.0.0.1", loginlogs[0].getLoginIP());

		lc2.setLoginTimeGreaterThan(new Date());
		Collection<LoginLog_> c2 = loginLogService.selectAll(lc2);
		Assert.assertEquals(0, c2.size());

		lc3.setIdGreaterThan(10);
		int count3 = loginLogService.count(lc3);
		Assert.assertEquals(0, count3);
		lc3.setIdGreaterThan(null);
		lc3.setIdGreaterOrEqual(10);
		count3 = loginLogService.count(lc3);
		Assert.assertEquals(1, count3);
		lc3.setIdGreaterOrEqual(null);
		lc3.setLoginTime(loginlogs[0].getLoginTime());
		count3 = loginLogService.count(lc3);
		Assert.assertEquals(1, count3);
		lc3.setLoginTime(null);
		lc3.setLoginTimeNotEqual(loginlogs[0].getLoginTime());
		count3 = loginLogService.count(lc3);
		Assert.assertEquals(0, count3);

		lc4.setIdGreaterThan(9);
		int count4 = loginLogService.count(lc4);
		Assert.assertEquals(1, count4);
		lc4.setIdGreaterThan(null);
		lc4.setIdLessThan(11);
		count4 = loginLogService.count(lc4);
		Assert.assertEquals(1, count4);
		lc4.setIdLessThan(10);
		count4 = loginLogService.count(lc4);
		Assert.assertEquals(0, count4);
		lc4.setIdLessThan(null);
		lc4.setIdLessOrEqual(10);
		count4 = loginLogService.count(lc4);
		Assert.assertEquals(1, count4);
		lc4.setIdLessOrEqual(null);
		lc4.setIdNotEqual(10);
		count4 = loginLogService.count(lc4);
		Assert.assertEquals(0, count4);
	}
}

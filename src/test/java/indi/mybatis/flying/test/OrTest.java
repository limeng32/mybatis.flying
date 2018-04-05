package indi.mybatis.flying.test;

import java.util.Collection;

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

import indi.mybatis.flying.pojo.Detail2_;
import indi.mybatis.flying.pojo.LogStatus;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.pojo.condition.Account_Condition2;
import indi.mybatis.flying.pojo.condition.LoginLogSource2Condition;
import indi.mybatis.flying.pojo.condition.LoginLog_Condition;
import indi.mybatis.flying.pojo.condition.Role_Condition;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.service2.Detail2Service;
import indi.mybatis.flying.service2.LoginLogSource2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class OrTest {

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private LoginLogSource2Service loginLogSource2Service;

	@Autowired
	private Detail2Service detail2Service;

	@Test
	public void test1() {
		Assert.assertNotNull(loginLogService);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr1.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr1.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr1.result.xml")
	public void testOr1() {
		LoginLog_Condition lc1 = new LoginLog_Condition();
		lc1.setLoginIPHeadLikeOr("a", "b");
		lc1.setIpLikeFilter("1");
		Collection<LoginLog_> LoginLogC = loginLogService.selectAll(lc1);
		Assert.assertEquals(2, LoginLogC.size());

		LoginLog_Condition lc2 = new LoginLog_Condition();
		lc2.setLoginIPLikeOr("b1", "c1");
		Collection<LoginLog_> LoginLogC2 = loginLogService.selectAll(lc2);
		Assert.assertEquals(2, LoginLogC2.size());

		int c2 = loginLogService.count(lc2);
		Assert.assertEquals(2, c2);

		LoginLog_Condition lc3 = new LoginLog_Condition();
		lc3.setLoginIPHeadLikeOrTailLike("a", "2");
		int c3 = loginLogService.count(lc3);
		Assert.assertEquals(2, c3);

		LoginLog_Condition lc4 = new LoginLog_Condition();
		lc4.setLoginIPEqualsOr("a1", "z3");
		int c4 = loginLogService.count(lc4);
		Assert.assertEquals(2, c4);

		LoginLog_Condition lc5 = new LoginLog_Condition();
		lc5.setLoginIPLikeOr("a1", "b1");
		lc5.setLoginIPEqualsOr("b1", "c1");
		int c5 = loginLogService.count(lc5);
		Assert.assertEquals(1, c5);

		LoginLog_Condition lc6 = new LoginLog_Condition();
		lc6.setNumEqualsOr(1, 2);
		int c6 = loginLogService.count(lc6);
		Assert.assertEquals(2, c6);

		LoginLog_Condition lc7 = new LoginLog_Condition();
		lc7.setNumEqualsOrLoginIPLike(1, "b1");
		Collection<LoginLog_> LoginLogC7 = loginLogService.selectAll(lc7);
		Assert.assertEquals(2, LoginLogC7.size());

		LoginLog_ l1 = new LoginLog_();
		l1.setLoginIP("a1");
		LoginLog_ loginLog_1 = loginLogService.selectOne(l1);

		LoginLog_ l2 = new LoginLog_();
		l2.setLoginIP("b1");
		LoginLog_ loginLog_2 = loginLogService.selectOne(l2);

		LoginLog_Condition lc8 = new LoginLog_Condition();
		lc8.setLoginTimeEqualsOr(loginLog_1.getLoginTime(), loginLog_2.getLoginTime());
		int c8 = loginLogService.count(lc8);
		Assert.assertEquals(2, c8);

		LoginLog_Condition lc9 = new LoginLog_Condition();
		lc9.setStatusEqualsOr(LogStatus.b, LogStatus.t);
		int c9 = loginLogService.count(lc9);
		Assert.assertEquals(2, c9);

		LoginLog_Condition lc10 = new LoginLog_Condition();
		lc10.setLoginIPNotEqualsOr("z2", "z3");
		int c10 = loginLogService.count(lc10);
		Assert.assertEquals(6, c10);

		LoginLog_Condition lc11 = new LoginLog_Condition();
		lc11.setNumGtOrLt(5, 2);
		int c11 = loginLogService.count(lc11);
		Assert.assertEquals(2, c11);

		LoginLog_Condition lc12 = new LoginLog_Condition();
		lc12.setNumGeOrLe(5, 2);
		int c12 = loginLogService.count(lc12);
		Assert.assertEquals(4, c12);

		LoginLog_Condition lc13 = new LoginLog_Condition();
		Object[] o13 = { 5, 2 };
		lc13.setNumGtOrLe(o13);
		int c13 = loginLogService.count(lc13);
		Assert.assertEquals(3, c13);

		LoginLog_Condition lc14 = new LoginLog_Condition();
		lc14.setNumGeOrLt(5, 2);
		int c14 = loginLogService.count(lc14);
		Assert.assertEquals(3, c14);

		LoginLog_Condition lc15 = new LoginLog_Condition();
		Object[] o15 = { true, false };
		lc15.setStatusIsNullOr(o15);
		int c15 = loginLogService.count(lc15);
		Assert.assertEquals(6, c15);

		LoginLog_Condition lc16 = new LoginLog_Condition();
		lc16.setStatusIsNullOrLoginIPEquals(false, "a1", "b1", "c1");// 最后一个参数无意义
		int c16 = loginLogService.count(lc16);
		Assert.assertEquals(4, c16);

	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr3.result.xml")
	public void testOr3() {
		LoginLog_Condition lc4 = new LoginLog_Condition();
		Account_Condition2 ac = new Account_Condition2();
		ac.setNameEqualsOr("ann", "bob");
		lc4.setAccount(ac);
		lc4.setIpLikeFilter("1");
		int i4 = loginLogService.count(lc4);
		Assert.assertEquals(2, i4);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr4.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr4.result.xml")
	public void testOr4() {
		LoginLog_Condition lc = new LoginLog_Condition();
		Account_Condition ac = new Account_Condition();
		ac.setNameEqualsOrLoginlogIpEquals("ann", "bob", "c1");
		lc.setAccount(ac);
		int i1 = loginLogService.count(lc);
		Assert.assertEquals(5, i1);

		LoginLog_Condition lc2 = new LoginLog_Condition();
		lc2.setAccount(new Account_Condition());
		lc2.getAccount().setRole(new Role_Condition());
		((Role_Condition) (lc2.getAccount().getRole())).setNameEqualsOrAccountNameEquals("admin", "cal");
		int i2 = loginLogService.count(lc2);
		Assert.assertEquals(6, i2);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr5.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr5.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr5.result.xml")
	public void testOr5() {
		LoginLog_Condition lc = new LoginLog_Condition();
		lc.setAccount(new Account_Condition());
		lc.getAccount().setRole(new Role_Condition());
		lc.getAccount().setRoleDeputy(new Role_Condition());
		((Role_Condition) lc.getAccount().getRole()).setNameEqualsOrAccountNameEquals("user", "bob");
		((Role_Condition) lc.getAccount().getRoleDeputy()).setNameEqualsOrAccountNameEquals("silver", "bob");
		Collection<LoginLog_> loginLogC = loginLogService.selectAll(lc);
		Assert.assertEquals(2, loginLogC.size());
	}

	/* 一个只有单独入参的或逻辑测试用例 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr6.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr6.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr6.result.xml")
	public void testOr6() {
		LoginLog_Condition lc = new LoginLog_Condition();
		lc.setAccount(new Account_Condition());
		lc.getAccount().setRole(new Role_Condition());
		((Role_Condition) lc.getAccount().getRole()).setAccountNameEquals("ann");
		int i = loginLogService.count(lc);
		Assert.assertEquals(2, i);

		LoginLog_Condition lc2 = new LoginLog_Condition();
		lc2.setAccount(new Account_Condition());
		lc2.getAccount().setRole(new Role_Condition());
		((Role_Condition) lc2.getAccount().getRole()).setNameEquals("admin");
		int i2 = loginLogService.count(lc2);
		Assert.assertEquals(4, i2);
	}

	/* 一个dbAssociationUniqueKey型外键的或逻辑测试用例 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr2.result.xml")
	public void testOr2() {
		LoginLog_Condition lc = new LoginLog_Condition();
		lc.setAccount(new Account_Condition());
		((Account_Condition) lc.getAccount()).setIdEqualsOr(1L, 2L);
		int i = loginLogService.count(lc);
		Assert.assertEquals(4, i);
	}

	/* 一个dbAssociationTypeHandler型外键的或逻辑测试用例 */
	@Test
	@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOr7.datasource2.xml")
	@ExpectedDatabase(connection = "dataSource2", assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/orTest/testOr7.datasource2.result.xml")
	@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOr7.datasource2.result.xml")
	public void testOr7() {
		LoginLogSource2Condition loginLogSource2 = new LoginLogSource2Condition();
		loginLogSource2.setAccountEqualsOr(1L, 2L);
		int i = loginLogSource2Service.count(loginLogSource2);
		Assert.assertEquals(4, i);
	}

	/* 一个在缓存状态下或逻辑查询的测试用例 */
	/* 需要同时涉及同库外键和跨库外键 */
	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOrMashup.dataSource1.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/orTest/testOrMashup.dataSource2.xml"), })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/orTest/testOrMashup.dataSource1.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/orTest/testOrMashup.dataSource2.result.xml"), })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOrMashup.dataSource1.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/orTest/testOrMashup.dataSource2.result.xml"), })
	public void testOrMashup() {
		LoginLogSource2Condition l2c = new LoginLogSource2Condition();
		l2c.setAccountEqualsOr2(1L, 2L, "23453");
		int i1 = loginLogSource2Service.count(l2c);
		Assert.assertEquals(3, i1);

		LoginLogSource2Condition l2c2 = new LoginLogSource2Condition();
		l2c2.setAccountEqualsOr3(1L, 2L, "23453", "d4");
		Detail2_ d2c = new Detail2_();
		d2c.setLoginLogSource2(l2c2);
		int i2 = detail2Service.count(d2c);
		Assert.assertEquals(4, i2);
	}
}

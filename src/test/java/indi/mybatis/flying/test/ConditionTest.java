package indi.mybatis.flying.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

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
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.models.Conditionable.Sequence;
import indi.mybatis.flying.pagination.Order;
import indi.mybatis.flying.pagination.SortParam;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.pojo.condition.Role_Condition;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.LoginLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource2", "dataSource" })
@ContextConfiguration("classpath:spring-test.xml")
public class ConditionTest {

	@BeforeClass
	public static void prepareDatabase() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/H2_TYPE.sql")
				.addScript("classpath:/INIT_TABLE.sql").build();
	}

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginLogService loginLogService;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource);
	}

	/** 测试condition:like功能 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testConditionLike.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testConditionLike.xml")
	public void testConditionLike() {
		Account_Condition ac = new Account_Condition();
		ac.setEmailLike("%%");
		Collection<Account_> c = accountService.selectAll(ac);
		Account_[] accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(1, accounts.length);
		Assert.assertEquals("an%%n@live.cn", accounts[0].getEmail());
	}

	/** 测试condition:like功能2：在parameter为null和为空字符串时的情况 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testConditionLike2.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testConditionLike2.xml")
	public void testConditionLike2() {
		Account_Condition ac = new Account_Condition(), ac2 = new Account_Condition();
		ac.setEmailLike(null);
		Collection<Account_> c = accountService.selectAll(ac);
		Account_[] accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(2, accounts.length);
		ac2.setEmailLike("");
		Collection<Account_> c2 = accountService.selectAll(ac);
		Assert.assertEquals(2, c2.size());
	}

	/** 测试condition:headLike功能 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testConditionHeadLike.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testConditionHeadLike.xml")
	public void testConditionHeadLike() {
		Account_Condition ac = new Account_Condition();
		ac.setEmailHeadLike("ann");
		Collection<Account_> c = accountService.selectAll(ac);
		Account_[] accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(1, accounts.length);
		Assert.assertEquals("ann@live.cn", accounts[0].getEmail());
	}

	/** 测试condition:tailLike功能 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testConditionTailLike.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testConditionTailLike.xml")
	public void testConditionTailLike() {
		Account_Condition ac = new Account_Condition();
		ac.setEmailTailLike("live.cn");
		Collection<Account_> c = accountService.selectAll(ac);
		Account_[] accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(1, accounts.length);
		Assert.assertEquals("ann@live.cn", accounts[0].getEmail());
	}

	@Test
	public void testSpliter() {
		String spliter = "\\s+";
		String source = " 1 2  34 ";
		String[] array = source.trim().split(spliter);
		Assert.assertEquals(3, array.length);
		Assert.assertEquals("34", array[2]);
		String[] array2 = source.trim().split(spliter, 2);
		Assert.assertEquals(2, array2.length);
		Assert.assertEquals("2  34", array2[1]);
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testMultiLikeAND.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testMultiLikeAND.xml")
	public void testMultiLikeAND() {
		Account_Condition ac = new Account_Condition();
		ac.setName("ann");
		ac.setEmailLike("as");
		List<String> multi = new ArrayList<>();
		multi.add("a");
		multi.add("s");
		multi.add("d");
		ac.setMultiLike(multi);
		Collection<Account_> c = accountService.selectAll(ac);
		Assert.assertEquals(1, c.size());
		int conut = accountService.count(ac);
		Assert.assertEquals(1, conut);
		Account_Condition ac2 = new Account_Condition();
		ac2.setName("ann");
		ac2.setEmailLike("as");
		List<String> multi2 = new LinkedList<>();
		ac2.setMultiLike(multi2);
		Collection<Account_> c2 = accountService.selectAll(ac2);
		Assert.assertEquals(1, c2.size());
		Account_Condition ac3 = new Account_Condition();
		List<String> multi3 = new ArrayList<>();
		multi3.add(null);
		multi3.add("a");
		multi3.add(null);
		ac3.setMultiLike(multi3);
		Collection<Account_> c3 = accountService.selectAll(ac3);
		Assert.assertEquals(1, c3.size());
		LoginLog_ lc = new LoginLog_();
		Account_Condition ac4 = new Account_Condition();
		List<String> multi4 = new ArrayList<>();
		multi4.add("a");
		ac4.setMultiLike(multi4);
		lc.setAccount(ac4);
		Collection<LoginLog_> c4 = loginLogService.selectAll(lc);
		Assert.assertEquals(1, c4.size());
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testMultiLikeOR.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testMultiLikeOR.xml")
	public void testMultiLikeOR() {
		Account_Condition ac = new Account_Condition();
		ac.setName("ann");
		ac.setEmailLike("as");
		List<String> multi = new ArrayList<>();
		multi.add(null);
		multi.add(null);
		multi.add(null);
		multi.add(null);
		ac.setMultiLikeOR(multi);
		Collection<Account_> c = accountService.selectAll(ac);
		Assert.assertEquals(1, c.size());
		int count = accountService.count(ac);
		Assert.assertEquals(1, count);
		Account_Condition ac2 = new Account_Condition();
		ac2.setName("ann");
		ac2.setEmailLike("as");
		List<String> multi2 = new ArrayList<>();
		multi2.add(null);
		multi2.add("a");
		multi2.add("sd");
		multi2.add("z");
		ac2.setMultiLikeOR(multi2);
		Collection<Account_> c2 = accountService.selectAll(ac2);
		Assert.assertEquals(1, c2.size());
		LoginLog_ lc = new LoginLog_();
		Account_Condition ac3 = new Account_Condition();
		List<String> multi3 = new ArrayList<>();
		multi3.add(null);
		multi3.add("a");
		multi3.add("sd");
		multi3.add("z");
		ac3.setMultiLikeOR(multi3);
		lc.setAccount(ac3);
		Collection<LoginLog_> c3 = loginLogService.selectAll(lc);
		Assert.assertEquals(1, c3.size());
	}

	/** 测试多重外键情况下sorter是否能正确发挥作用 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/conditionTest/testSorterWithMultiAssociation.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/conditionTest/testSorterWithMultiAssociation.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/conditionTest/testSorterWithMultiAssociation.xml")
	public void testSorterWithMultiAssociation() {
		Role_Condition rc1 = new Role_Condition();
		rc1.setName("role1");
		Role_Condition rc2 = new Role_Condition();
		rc2.setName("role2");
		Account_Condition ac = new Account_Condition();
		ac.setRole(rc1);
		ac.setRoleDeputy(rc2);
		ac.setSorter(new SortParam(new Order("name", Sequence.asc)));
		Collection<Account_> accountC = accountService.selectAll(ac);
		Account_[] accounts = accountC.toArray(new Account_[accountC.size()]);
		Assert.assertEquals(3, accounts.length);
		Assert.assertEquals("bob", accounts[0].getName());
	}
}

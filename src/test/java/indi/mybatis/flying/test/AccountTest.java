package indi.mybatis.flying.test;

import java.util.Collection;

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
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DatabaseTearDowns;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.exceptions.Configurer2Exception;
import indi.mybatis.flying.exceptions.ConfigurerException;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.pagination.Order;
import indi.mybatis.flying.pagination.PageParam;
import indi.mybatis.flying.pagination.SortParam;
import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.StoryStatus_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.service.TransactiveService;
import indi.mybatis.flying.service.TransactiveService4;
import indi.mybatis.flying.service2.Account2Service;
import indi.mybatis.flying.service2.LoginLogSource2Service;
import indi.mybatis.flying.service2.Role2Service;
import indi.mybatis.flying.service2.TransactiveService2;
import indi.mybatis.flying.service2.TransactiveService3;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class AccountTest {

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

	@Autowired
	private Role2Service role2Service;

	@Autowired
	private TransactiveService transactiveService;

	@Autowired
	private TransactiveService2 transactiveService2;

	@Autowired
	private TransactiveService3 transactiveService3;

	@Autowired
	private TransactiveService4 transactiveService4;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource1);
	}

	/** 测试insert功能（有乐观锁） */
	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	public void testInsert() {
		Account_ a = new Account_();
		a.setId(1);
		a.setName("ann");
		a.setEmail("ann@live.cn");
		a.setPassword("5a690d842935c51f26f473e025c1b97a");
		a.setActivated(true);
		a.setActivateValue("");
		accountService.insert(a);

		Role2_ role2_ = new Role2_();
		role2_.setName("new");
		role2Service.insert(role2_);

		LoginLog_ loginLog_ = new LoginLog_();
		loginLog_.setLoginIP("old");
		loginLogService.insert(loginLog_);

		LoginLogSource2 loginLogSource2 = new LoginLogSource2();
		loginLogSource2.setLoginIP("new");
		loginLogSource2Service.insert(loginLogSource2);
		Collection<LoginLogSource2> c = loginLogSource2Service.selectAll(new LoginLogSource2());
		LoginLogSource2[] loginLogSource2s = c.toArray(new LoginLogSource2[1]);
		Assert.assertEquals("new", loginLogSource2s[0].getLoginIP());

		Account2_ account2_ = new Account2_();
		account2_.setEmail("l@x.com");
		account2_.setNickname("nick");
		account2_.setRole(role2_);
		account2Service.insert(account2_);

		Collection<Account2_> c2 = account2Service.selectAll(new Account2_());
		Account2_[] account2_s = c2.toArray(new Account2_[1]);
		Assert.assertEquals("new", account2_s[0].getRole().getName());
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testTransactive.datasource.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testTransactive.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testTransactive.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testTransactive.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testTransactive.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testTransactive.datasource2.result.xml") })
	public void testTransactive() {
		try {
			transactiveService2.addAccount2Transactive();
		} catch (Configurer2Exception e) {
			e.printStackTrace();
		}
		try {
			transactiveService.addAccountTransactive();
		} catch (ConfigurerException e) {
			e.printStackTrace();
		}
		try {
			transactiveService2.addAccount2Transactive2();
		} catch (Configurer2Exception e) {
			e.printStackTrace();
		}
		try {
			transactiveService3.testTransactive();
		} catch (Configurer2Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testTransactive2.datasource.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testTransactive2.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testTransactive2.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testTransactive2.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testTransactive2.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testTransactive2.datasource2.result.xml") })
	public void testTransactive2() {
		try {
			transactiveService.addAccountTransactive();
		} catch (ConfigurerException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testTransactive3.datasource.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testTransactive3.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testTransactive3.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testTransactive3.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testTransactive3.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testTransactive3.datasource2.result.xml") })
	public void testTransactive3() {
		try {
			transactiveService4.testTransactive();
		} catch (ConfigurerException e) {
			e.printStackTrace();
		}
	}

	/** 测试update功能（有乐观锁） */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testUpdate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testUpdate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testUpdate.xml")
	public void testUpdate() {
		Account_ a = accountService.select(1);
		a.setEmail("ann@tom.com");
		a.setActivated(false);
		accountService.update(a);
	}

	/** 测试delete功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testDelete.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testDelete.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testDelete.xml")
	public void testDelete() {
		Account_ a = accountService.select(1);
		accountService.delete(a);
	}

	/** 测试sorter功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testSorter.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testSorter.xml")
	public void testSorter() {
		Account_Condition ac = new Account_Condition();
		ac.setSorter(new SortParam(new Order(Account_Condition.field_name, Conditionable.Sequence.desc)));
		Collection<Account_> c = accountService.selectAll(ac);
		Account_[] accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals("ann", accounts[3].getName());
		ac.setSorter(new SortParam(new Order(Account_Condition.field_name, Conditionable.Sequence.desc),
				new Order(Account_Condition.field_password, Conditionable.Sequence.desc)));
		c = accountService.selectAll(ac);
		accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(new Integer(4), accounts[0].getId());
		ac.setSorter(new SortParam(new Order(Account_Condition.field_name, Conditionable.Sequence.desc),
				new Order(Account_Condition.field_name, Conditionable.Sequence.asc)));
		c = accountService.selectAll(ac);
		accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals("ann", accounts[3].getName());
	}

	/** 测试limiter功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testLimiter.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testLimiter.xml")
	public void testLimiter() {
		Account_Condition ac = new Account_Condition();
		ac.setLimiter(new PageParam(1, 2));
		Collection<Account_> c = accountService.selectAll(ac);
		Account_[] accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(2, accounts.length);
		Assert.assertEquals(1, accounts[0].getId().intValue());
		Assert.assertEquals(2, accounts[1].getId().intValue());
		Assert.assertEquals(2, ac.getLimiter().getMaxPageNum());
		ac.setSorter(new SortParam(new Order(Account_Condition.field_id, Conditionable.Sequence.desc)));
		c = accountService.selectAll(ac);
		accounts = c.toArray(new Account_[c.size()]);
		Assert.assertEquals(2, accounts.length);
		Assert.assertEquals(4, accounts[0].getId().intValue());
		Assert.assertEquals(3, accounts[1].getId().intValue());
	}

	/** 测试CostumizeStatus功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testCostumizeStatus.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testCostumizeStatus.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testCostumizeStatus.xml")
	public void testCostumizeStatus() {
		Account_ a = accountService.select(1);
		Assert.assertEquals("发布", a.getStatus().text());
		Account_ ac = new Account_();
		ac.setStatus(StoryStatus_.p);
		Collection<Account_> c = accountService.selectAll(ac);
		Assert.assertEquals(1, c.size());
		Account_ a2 = accountService.select(2);
		Assert.assertEquals(StoryStatus_.s, a2.getStatus());
		a2.setStatus(StoryStatus_.c);
		accountService.update(a2);
		Account_ a3 = accountService.select(3);
		Assert.assertNull(a3.getStatus());
		a3.setPassword("5a690d842935c51f26f473e025c1b97a");
		accountService.updatePersistent(a3);
		Account_ a4 = accountService.select(3);
		Assert.assertNull(a4.getStatus());
	}

	/** 测试ignoreTag功能 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testIgnoredSelect.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testIgnoredSelect.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testIgnoredSelect.xml")
	public void testIgnoreTag() {
		Account_ account = accountService.select(1);
		Assert.assertNull(account.getPassword());

		Account_ ac = new Account_();
		Collection<Account_> accountC = accountService.selectAll(ac);
		for (Account_ a : accountC) {
			Assert.assertNull(a.getPassword());
		}

		Account_ ac2 = new Account_();
		ac2.setPassword("5a690d842935c51f26f473e025c1b97a");
		Collection<Account_> accountC2 = accountService.selectAll(ac2);
		Assert.assertEquals(1, accountC2.size());

		LoginLog_ loginLog = loginLogService.select(1);
		Assert.assertNull(loginLog.getAccount().getPassword());

		LoginLog_ lc2 = new LoginLog_();
		lc2.setAccount(ac2);
		Collection<LoginLog_> loginLogC = loginLogService.selectAll(lc2);
		Assert.assertEquals(1, loginLogC.size());
		for (LoginLog_ l : loginLogC) {
			Assert.assertEquals("0.0.0.1", l.getLoginIP());
		}
	}

	/** 测试deputyRole */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testDeputy.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testDeputy.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testDeputy.xml")
	public void testDeputy() {
		Account_ account = accountService.select(1);
		Assert.assertEquals("role1", account.getRole().getName());
		Assert.assertEquals("role2", account.getRoleDeputy().getName());

		Role_ rc = new Role_();
		rc.setName("role1");
		Role_ rdc = new Role_();
		rdc.setName("role2");
		Account_ ac = new Account_();
		ac.setRole(rc);
		ac.setRoleDeputy(rdc);
		Collection<Account_> accountC = accountService.selectAll(ac);
		Assert.assertEquals(1, accountC.size());

		int count = accountService.count(ac);
		Assert.assertEquals(1, count);
	}

	/** 更多的测试deputyRole */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testDeputy2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testDeputy2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testDeputy2.xml")
	public void testDeputy2() {
		Role_ rc = new Role_();
		rc.setName("role1");
		Role_ rdc = new Role_();
		rdc.setName("role2");
		Account_ ac = new Account_();
		ac.setRole(rc);
		ac.setRoleDeputy(rdc);
		Collection<Account_> accountC = accountService.selectAll(ac);
		Assert.assertEquals(1, accountC.size());
		int count = accountService.count(ac);
		Assert.assertEquals(1, count);

		Account_ ac2 = new Account_();
		ac2.setRole(rc);
		Collection<Account_> accountC2 = accountService.selectAll(ac2);
		Assert.assertEquals(2, accountC2.size());
		int count2 = accountService.count(ac2);
		Assert.assertEquals(2, count2);

		Account_ ac3 = new Account_();
		ac3.setRoleDeputy(rdc);
		Collection<Account_> accountC3 = accountService.selectAll(ac3);
		Assert.assertEquals(2, accountC3.size());
		int count3 = accountService.count(ac3);
		Assert.assertEquals(2, count3);

		Account_ ac4 = new Account_();
		Collection<Account_> accountC4 = accountService.selectAll(ac4);
		Assert.assertEquals(4, accountC4.size());
		int count4 = accountService.count(ac4);
		Assert.assertEquals(4, count4);
	}
}

package indi.mybatis.flying.test;

import java.util.ArrayList;
import java.util.Collection;

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
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.StoryStatus_;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.LoginLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class WhiteListTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginLogService loginLogService;

	@Test
	public void test1() {
		Assert.assertNotNull(accountService);
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteList.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteList.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteList.result.xml")
	public void testWhiteList() {
		Account_ account = accountService.selectSimple(1);
		Assert.assertNull(account.getEmail());
		Assert.assertNull(account.getPassword());
		Assert.assertNull(account.getPermission().getName());
		Assert.assertNull(account.getPermission().getFakeId());

		LoginLog_ l = new LoginLog_();
		l.setLoginIP("ip1");
		LoginLog_ loginLog = loginLogService.selectOneSimple(l);
		Assert.assertNull(loginLog.getLoginIP2());
		Assert.assertNull(loginLog.getAccount().getEmail());
		Assert.assertNull(loginLog.getAccount().getPassword());
		Assert.assertNull(loginLog.getAccount().getPermission().getName());
		Assert.assertNull(loginLog.getAccount().getPermission().getFakeId());
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListInsert.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListInsert.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListInsert.result.xml")
	public void testWhiteListInsert() {
		Account_ account = new Account_();
		account.setName("name");
		account.setEmail("email");
		account.setPassword("aaa");
		Role_ role = new Role_();
		role.setId(11);
		account.setRole(role);
		Permission permission = new Permission();
		permission.setId(22);
		account.setPermission(permission);
		accountService.insertSimpleNoName(account);
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListInsertBatch.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListInsertBatch.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListInsertBatch.result.xml")
	public void testWhiteListInsertBatch() {
		Account_ account = new Account_();
		account.setName("name");
		account.setEmail("email");
		account.setPassword("aaa");
		Role_ role = new Role_();
		role.setId(11);
		account.setRole(role);
		Permission permission = new Permission();
		permission.setId(22);
		account.setPermission(permission);

		Collection<Account_> ac = new ArrayList<>();
		ac.add(account);
		accountService.insertBatchSimpleNoName(ac);
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListUpdate.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListUpdate.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListUpdate.result.xml")
	public void testWhiteListUpdate() {
		Account_ account = accountService.select(1);
		account.setName("bob");
		account.setEmail("bob@live.cn");
		account.setPassword("bbb");
		account.setStatus(StoryStatus_.p);
		account.setActivateValue("bv");
		Role_ role = new Role_();
		role.setId(2);
		account.setRole(role);
		Permission permission = new Permission();
		permission.setId(22);
		account.setPermission(permission);
		accountService.updateSimpleNoName(account);
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListUpdatePersistent.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListUpdatePersistent.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/whiteListTest/testWhiteListUpdatePersistent.result.xml")
	public void testWhiteListUpdatePersistent() {
		Account_ account = accountService.select(1);
		account.setName("bob");
		account.setEmail("bob@live.cn");
		account.setPassword("bbb");
		account.setStatus(StoryStatus_.p);
		account.setActivateValue(null);
		Role_ role = new Role_();
		role.setId(33);
		account.setRole(role);
		accountService.updatePersistentSimpleNoName(account);
	}
}

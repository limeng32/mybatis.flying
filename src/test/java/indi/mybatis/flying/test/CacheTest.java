package indi.mybatis.flying.test;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import indi.mybatis.flying.pagination.PageParam;
import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Detail_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.DetailService;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.service.RoleService;
import indi.mybatis.flying.service2.Account2Service;
import indi.mybatis.flying.service2.Role2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class CacheTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private DetailService detailService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private Account2Service account2Service;

	@Autowired
	private Role2Service role2Service;

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/test.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest/test.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/test.result.xml")
	public void test() {
		String name = "ann";
		String newName = "bob";
		String loginIP = "0.0.0.1";

		Account_ a = new Account_();
		LoginLog_ l = new LoginLog_();

		a.setName(name);
		accountService.insert(a);

		l.setLoginIP(loginIP);
		l.setAccount(a);
		loginLogService.insert(l);

		LoginLog_ loginLog = loginLogService.select(l.getId());
		Assert.assertEquals(name, loginLog.getAccount().getName());
		Account_ account = accountService.select(a.getId());
		account.setName(newName);
		accountService.update(account);
		LoginLog_ loginLog2 = loginLogService.select(l.getId());
		Assert.assertEquals(newName, loginLog2.getAccount().getName());
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/test2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest/test2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/test2.result.xml")
	public void test2() {
		String name = "新权限", newName = "新角色", accountName = "ann", ip = "0.0.0.1";
		Role_ r = new Role_();
		r.setName(name);
		roleService.insert(r);

		Account_ a = new Account_();
		a.setName(accountName);
		a.setRole(r);
		accountService.insert(a);

		LoginLog_ l = new LoginLog_();
		l.setLoginIP(ip);
		l.setAccount(a);
		loginLogService.insert(l);

		Account_ account = accountService.select(a.getId());
		Assert.assertEquals(name, account.getRole().getName());

		LoginLog_ loginLog = loginLogService.select(l.getId());
		Assert.assertEquals(name, loginLog.getAccount().getRole().getName());

		Role_ role = roleService.select(r.getId());
		role.setName(newName);
		roleService.update(role);

		Account_ account2 = accountService.select(a.getId());
		Assert.assertEquals(newName, account2.getRole().getName());

		LoginLog_ loginLog2 = loginLogService.select(l.getId());
		Assert.assertEquals(newName, loginLog2.getAccount().getRole().getName());
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/test3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest/test3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/test3.result.xml")
	public void test3() {
		String name = "新权限", newName = "新角色", newName2 = "新新角色", accountName = "ann", ip = "0.0.0.1", detailName = "细节";
		Role_ r = new Role_();
		r.setName(name);
		roleService.insert(r);

		Account_ a = new Account_();
		a.setName(accountName);
		a.setRole(r);
		accountService.insert(a);

		LoginLog_ l = new LoginLog_();
		l.setLoginIP(ip);
		l.setAccount(a);
		loginLogService.insert(l);

		Detail_ d = new Detail_();
		d.setName(detailName);
		d.setLoginLog(l);
		detailService.insert(d);

		Account_ account = accountService.select(a.getId());
		Assert.assertEquals(name, account.getRole().getName());

		LoginLog_ loginLog = loginLogService.select(l.getId());
		Assert.assertEquals(name, loginLog.getAccount().getRole().getName());

		Role_ role = roleService.select(r.getId());
		role.setName(newName);
		roleService.update(role);

		Account_ account2 = accountService.select(a.getId());
		Assert.assertEquals(newName, account2.getRole().getName());

		LoginLog_ loginLog2 = loginLogService.select(l.getId());
		Assert.assertEquals(newName, loginLog2.getAccount().getRole().getName());

		Detail_ detail = detailService.select(d.getId());
		Assert.assertEquals(accountName, detail.getLoginLog().getAccount().getName());
		Assert.assertEquals(newName, detail.getLoginLog().getAccount().getRole().getName());

		// Account_ account3 = accountService.select(a.getId());
		// account3.setName(newAccountName);
		// accountService.update(account3);

		Role_ role2 = roleService.select(r.getId());
		role2.setName(newName2);
		roleService.update(role2);

		Detail_ detail2 = detailService.select(d.getId());
		Assert.assertEquals(newName2, detail2.getLoginLog().getAccount().getRole().getName());
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/testPagination.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest/testPagination.result.xml")
	public void testPagination() {
		String name1 = "ann", name2 = "bob", name3 = "carl", name4 = "duke";

		Account_ a1 = new Account_();
		a1.setId(1);
		a1.setName(name1);
		accountService.insert(a1);

		Account_ a2 = new Account_();
		a2.setId(2);
		a2.setName(name2);
		accountService.insert(a2);

		Account_ a3 = new Account_();
		a3.setId(3);
		a3.setName(name3);
		accountService.insert(a3);

		Account_ a4 = new Account_();
		a4.setId(4);
		a4.setName(name4);
		accountService.insert(a4);

		Account_Condition ac = new Account_Condition();
		ac.setLimiter(new PageParam(1, 2));
		Collection<Account_> c1 = accountService.selectAll(ac);
		Assert.assertEquals(2, c1.size());
		for (Account_ a : c1) {
			if (a.getId() == 1) {
				Assert.assertEquals(name1, a.getName());
			} else {
				Assert.assertEquals(name2, a.getName());
			}
		}
		Assert.assertEquals(2, ac.getLimiter().getMaxPageNum());

		ac.setLimiter(new PageParam(1, 2));
		c1 = accountService.selectAll(ac);
		Assert.assertEquals(2, ac.getLimiter().getMaxPageNum());

		accountService.delete(a1);
		accountService.delete(a2);
		accountService.delete(a3);
		accountService.delete(a4);
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test.datasource2.xml")
	@ExpectedDatabase(connection = "dataSource2", assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest2/test.datasource2.result.xml")
	@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test.datasource2.result.xml")
	public void test21() {
		String name = "ann";
		String roleName = "user";
		String newRoleName = "admin";

		Account2_ a = new Account2_();
		Role2_ r = new Role2_();

		r.setName(roleName);
		role2Service.insert(r);

		a.setName(name);
		a.setRole(r);
		account2Service.insert(a);

		Account2_ account2_ = account2Service.select(a.getId());
		Assert.assertEquals(roleName, account2_.getRole().getName());

		Role2_ r2 = role2Service.select(r.getId());
		r2.setName(newRoleName);
		role2Service.update(r2);

		account2_ = account2Service.select(a.getId());
		Assert.assertEquals(newRoleName, account2_.getRole().getName());
	}

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test2.datasource1.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test2.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest2/test2.datasource1.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest2/test2.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test2.datasource1.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test2.datasource2.result.xml") })
	public void test22() {
		String name = "ann";
		String roleName = "user";
		String newRoleName = "admin";

		Account_ a = new Account_();
		Role_ r = new Role_();

		r.setName(roleName);
		roleService.insert(r);

		a.setName(name);
		a.setRole(r);
		accountService.insert(a);

		Account_ account_ = accountService.select(a.getId());
		Assert.assertEquals(roleName, account_.getRole().getName());

		Role_ r12 = roleService.select(r.getId());
		r12.setName(newRoleName);
		roleService.update(r12);

		account_ = accountService.select(a.getId());
		Assert.assertEquals(newRoleName, account_.getRole().getName());

		Account2_ a2 = new Account2_();
		Role2_ r2 = new Role2_();

		r2.setName(roleName);
		role2Service.insert(r2);

		a2.setName(name);
		a2.setRole(r2);
		account2Service.insert(a2);

		Account2_ account2_ = account2Service.select(a2.getId());
		Assert.assertEquals(roleName, account2_.getRole().getName());

		Role2_ r22 = role2Service.select(r2.getId());
		r22.setName(newRoleName);
		role2Service.update(r22);

		account2_ = account2Service.select(a2.getId());
		Assert.assertEquals(newRoleName, account2_.getRole().getName());
	}
}

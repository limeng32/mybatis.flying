package indi.mybatis.flying.test;

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

import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.RoleService;
import indi.mybatis.flying.service2.Account2Service;
import indi.mybatis.flying.service2.Role2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class CacheTest2 {

	@Autowired
	private AccountService accountService;

	@Autowired
	private Account2Service account2Service;

	@Autowired
	private RoleService roleService;

	@Autowired
	private Role2Service role2Service;

	@Test
	@IfProfileValue(name = "CACHE", value = "true")
	@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test.datasource2.xml")
	@ExpectedDatabase(connection = "dataSource2", assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/cacheTest2/test.datasource2.result.xml")
	@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/cacheTest2/test.datasource2.result.xml")
	public void test() {
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
	public void test2() {
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

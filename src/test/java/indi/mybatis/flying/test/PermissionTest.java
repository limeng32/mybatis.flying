package indi.mybatis.flying.test;

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
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.PermissionService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class PermissionTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private PermissionService permissionService;

	@Test
	public void test() {
		Assert.assertNotNull(permissionService);
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/permissionTest/testSelect.datasource.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/permissionTest/testSelect.datasource.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/permissionTest/testSelect.datasource.result.xml")
	public void testSelect() {
		Permission permission = permissionService.select(2);
		Assert.assertEquals("ann", permission.getName());

		// 当account.permission为null时，外键不发生作用
		Collection<Account_> accounts = accountService.selectAll(new Account_());
		Assert.assertEquals(2, accounts.size());

		// 当account.permission不为null时，外键发生作用
		Permission p = new Permission();
		Account_Condition a = new Account_Condition();
		a.setEmailHeadLike("an");
		a.setPermission(p);
		Collection<Account_> accounts2 = accountService.selectAll(a);
		Assert.assertEquals(2, accounts2.size());
//		for (Account_ e : accounts2) {
//			Assert.assertEquals(1, e.getId().intValue());
//			Assert.assertEquals(20, e.getPermission().getFakeId().intValue());
//		}

		int c = accountService.count(a);
		Assert.assertEquals(2, c);
	}

	// 测试saltFor属性
	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/permissionTest/testSalt.datasource.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/permissionTest/testSalt.datasource.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/permissionTest/testSalt.datasource.result.xml")
	public void testSalt() {
		Permission p = new Permission();
		p.setName("ann");
		p.setSalt("ss");
		p.setSecret("se");
		permissionService.insert(p);

		Permission p2 = new Permission();
		p2.setName("bob");
		permissionService.insert(p2);
		p2.setSecret("a");
		p2.setSalt("b");
		permissionService.update(p2);
	}
}

package indi.mybatis.flying.test;

import java.util.ArrayList;
import java.util.Collection;
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
import indi.mybatis.flying.mapper.PermissionMapper;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;

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
	private PermissionMapper permissionService;

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
		p.setId(1);
		p.setName("ann");
		p.setSalt("ss");
		p.setSecret("se");
		permissionService.insert(p);

		Permission p2 = new Permission();
		p2.setId(2);
		p2.setName("bob");
		permissionService.insert(p2);
		p2.setSecret("a");
		p2.setSalt("b");
		permissionService.update(p2);

		List<Permission> l = new ArrayList<>();
		Permission p3 = new Permission();
		p3.setId(3);
		p3.setName("carl");
		p3.setSecret("s3");
		p3.setSalt("salt3");
		l.add(p3);
		Permission p4 = new Permission();
		p4.setId(4);
		p4.setName("duke");
		p4.setSecret("s4");
		p4.setSalt("salt4");
		l.add(p4);
		Permission p5 = new Permission();
		p5.setName("evan");
		p5.setId(5);
		l.add(p5);
		Permission p6 = new Permission();
		p6.setId(6);
		p6.setName("frank");
		l.add(p6);
		Permission p7 = new Permission();
		p7.setId(7);
		p7.setName("green");
		l.add(p7);
		Permission p8 = new Permission();
		p8.setId(8);
		p8.setName("hank");
		p8.setSecret("s8");
		l.add(p8);
		Permission p9 = new Permission();
		p9.setId(9);
		p9.setName("ivy");
		p9.setSalt("salt99");
		l.add(p9);
		permissionService.insertBatch(l);

		List<Permission> l2 = new ArrayList<>();
		p5.setSalt("salt55");
		p5.setSecret("s5");
		l2.add(p5);
		p6.setSalt("salt66");
		p6.setSecret("s6");
		l2.add(p6);
		permissionService.updateBatch(l2);

		p7.setName(null);
		p7.setSalt("salt77");
		p7.setSecret("s7");
		permissionService.updatePersistent(p7);
	}
}

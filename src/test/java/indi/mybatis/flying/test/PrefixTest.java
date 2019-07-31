package indi.mybatis.flying.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.alibaba.fastjson.JSONObject;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.utils.FlyingManager;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1" })
@ContextConfiguration("classpath:spring-test.xml")
public class PrefixTest {

	@Autowired
	private DataSource dataSource1;

	@Autowired
	private AccountService accountService;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource1);
		// 测试安全源码
		Assert.assertEquals(1, accountService.selectCheckHealth());
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/prefixTest/testSelect.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/prefixTest/testSelect.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/prefixTest/testSelect.result.xml")
	public void testSelect() {
		Map<String, String> map = new HashMap<>();
		map.put("id", "1");
		Account_ account = accountService.selectAsd(1);
		Assert.assertTrue(account.getActivated());
		Assert.assertEquals("bob", account.getName());
		Assert.assertEquals("bob@live.cn_", account.getEmail());
		Assert.assertNull(account.getPassword());
		Assert.assertEquals("a111", account.getActivateValue());
		Assert.assertEquals(11, account.getOpLock().intValue());

		Account_ ac = new Account_();
		ac.setName("carl");
		Permission pc = new Permission();
		pc.setName("carl");
		ac.setPermission(pc);
		Collection<Account_> accountC = accountService.selectAllAsd(ac);
		System.out.println(JSONObject.toJSONString(accountC));
		Account_[] accounts = accountC.toArray(new Account_[accountC.size()]);
		Assert.assertEquals(3, accounts[0].getId().intValue());
		Assert.assertEquals(23, accounts[0].getPermission().getFakeId().intValue());
		Assert.assertEquals(3, accounts[0].getPermission().getId().intValue());
		Assert.assertEquals("carl", accounts[0].getPermission().getName());
		Assert.assertEquals(13, accounts[0].getRole().getId().intValue());
		Assert.assertEquals("role3", accounts[0].getRole().getName());
		Assert.assertEquals(113, accounts[0].getRoleDeputy().getId().intValue());
		Assert.assertEquals("roleDeputy3", accounts[0].getRoleDeputy().getName());

		Assert.assertEquals(4, accounts[1].getId().intValue());
		Assert.assertEquals(24, accounts[1].getPermission().getFakeId().intValue());
		Assert.assertEquals(4, accounts[1].getPermission().getId().intValue());
		Assert.assertEquals("carl", accounts[1].getPermission().getName());
		Assert.assertEquals(14, accounts[1].getRole().getId().intValue());
		Assert.assertEquals("role4", accounts[1].getRole().getName());
		Assert.assertEquals(114, accounts[1].getRoleDeputy().getId().intValue());
		Assert.assertEquals("roleDeputy4", accounts[1].getRoleDeputy().getName());

		int c = accountService.countAsd(ac);
		Assert.assertEquals(2, c);

		FlyingModel fm = FlyingManager.getFlyingModelFromCache("indi.mybatis.flying.mapper.AccountMapper.selectAllAsd");
		System.out.println("fm::" + JSONObject.toJSONString(fm));

		Assert.assertEquals("noPassword", fm.getIgnoreTag());
		Assert.assertNull(fm.getPrefix());

		FlyingModel fm1 = fm.getProperties().get("permission");
		Assert.assertEquals("aaaa", fm1.getIgnoreTag());
		Assert.assertEquals("permission__", fm1.getPrefix());
		Assert.assertEquals("indi.mybatis.flying.mapper.PermissionMapper.select", fm1.getId());

		FlyingModel fm2 = fm.getProperties().get("role");
		Assert.assertEquals("indi.mybatis.flying.mapper.RoleMapper.select", fm2.getId());
		Assert.assertEquals("role__", fm2.getPrefix());

		FlyingModel fm3 = fm.getProperties().get("roleDeputy");
		Assert.assertEquals("roleDeputy__", fm3.getPrefix());

		Account_ ac2 = new Account_();
		ac2.setName("carl");
//		ac2.setPermission(new Permission());
		Collection<Account_> accountC2 = accountService.selectAllAsd(ac2);
		System.out.println("5::" + JSONObject.toJSONString(accountC2));
	}
}

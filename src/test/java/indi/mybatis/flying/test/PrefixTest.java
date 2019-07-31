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
import indi.mybatis.flying.pojo.Detail_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.pojo.condition.LoginLog_Condition;
import indi.mybatis.flying.pojo.condition.Role_Condition;
import indi.mybatis.flying.service.AccountService;
import indi.mybatis.flying.service.DetailService;
import indi.mybatis.flying.service.LoginLogService;
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

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private DetailService detailService;

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
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/prefixTest/testSelect2.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/prefixTest/testSelect2.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/prefixTest/testSelect2.result.xml")
	public void testSelect2() {
		LoginLog_ lc = new LoginLog_();
		lc.setLoginIP("ip3");
		lc.setAccount(new Account_());
		lc.getAccount().setRoleDeputy(new Role_Condition());
		((Role_Condition) (lc.getAccount().getRoleDeputy())).setNameNotEquals("roleDeputy2");
		Collection<LoginLog_> loginLogC = loginLogService.selectAllPrefix(lc);
		System.out.println("1::" + JSONObject.toJSONString(loginLogC));
		LoginLog_[] loginLogs = loginLogC.toArray(new LoginLog_[loginLogC.size()]);
		Assert.assertEquals(103, loginLogs[0].getId().intValue());
		Assert.assertEquals("ip2_3", loginLogs[0].getLoginIP2());
		Assert.assertEquals(3, loginLogs[0].getAccount().getId().intValue());
		Assert.assertEquals("ccc", loginLogs[0].getAccount().getPassword());
		Assert.assertEquals(13, loginLogs[0].getAccount().getRole().getId().intValue());
		Assert.assertEquals("role3", loginLogs[0].getAccount().getRole().getName());
		Assert.assertEquals(113, loginLogs[0].getAccount().getRoleDeputy().getId().intValue());
		Assert.assertEquals("roleDeputy3", loginLogs[0].getAccount().getRoleDeputy().getName());

		Assert.assertEquals(104, loginLogs[1].getId().intValue());
		Assert.assertEquals("ip2_4", loginLogs[1].getLoginIP2());
		Assert.assertEquals(4, loginLogs[1].getAccount().getId().intValue());
		Assert.assertEquals("ddd", loginLogs[1].getAccount().getPassword());
		Assert.assertEquals(14, loginLogs[1].getAccount().getRole().getId().intValue());
		Assert.assertEquals("role4", loginLogs[1].getAccount().getRole().getName());
		Assert.assertEquals(114, loginLogs[1].getAccount().getRoleDeputy().getId().intValue());
		Assert.assertEquals("roleDeputy4", loginLogs[1].getAccount().getRoleDeputy().getName());
	}

	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/prefixTest/testSelect3.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/prefixTest/testSelect3.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/prefixTest/testSelect3.result.xml")
	public void testSelect3() {
		Detail_ dc = new Detail_();
		dc.setName("d3");
		LoginLog_Condition lc = new LoginLog_Condition();
		lc.setIdNotEqual(102);
		dc.setLoginLog(lc);
		Collection<Detail_> detailC = detailService.selectAllPrefix(dc);
		System.out.println("11::" + JSONObject.toJSONString(detailC));
		Detail_[] details = detailC.toArray(new Detail_[detailC.size()]);

		Assert.assertEquals(203, details[0].getId().intValue());
		Assert.assertEquals(103, details[0].getLoginLog().getId().intValue());
		Assert.assertEquals("ip2_3", details[0].getLoginLog().getLoginIP2());
		Assert.assertEquals(3, details[0].getLoginLog().getAccount().getId().intValue());
		Assert.assertEquals("ccc", details[0].getLoginLog().getAccount().getPassword());
		Assert.assertEquals(13, details[0].getLoginLog().getAccount().getRole().getId().intValue());
		Assert.assertEquals("role3", details[0].getLoginLog().getAccount().getRole().getName());
		Assert.assertEquals(113, details[0].getLoginLog().getAccount().getRoleDeputy().getId().intValue());
		Assert.assertEquals("roleDeputy3", details[0].getLoginLog().getAccount().getRoleDeputy().getName());

		Assert.assertEquals(204, details[1].getId().intValue());
		Assert.assertEquals(104, details[1].getLoginLog().getId().intValue());
		Assert.assertEquals("ip2_4", details[1].getLoginLog().getLoginIP2());
		Assert.assertEquals(4, details[1].getLoginLog().getAccount().getId().intValue());
		Assert.assertEquals("ddd", details[1].getLoginLog().getAccount().getPassword());
		Assert.assertEquals(14, details[1].getLoginLog().getAccount().getRole().getId().intValue());
		Assert.assertEquals("role4", details[1].getLoginLog().getAccount().getRole().getName());
		Assert.assertEquals(114, details[1].getLoginLog().getAccount().getRoleDeputy().getId().intValue());
		Assert.assertEquals("roleDeputy4", details[1].getLoginLog().getAccount().getRoleDeputy().getName());
	}

	// 测试flyingModel深度小于传入object深度的情况
	@Test
	@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/prefixTest/testSelect4.xml")
	@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/prefixTest/testSelect4.result.xml")
	@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/prefixTest/testSelect4.result.xml")
	public void testSelect4() {
		Detail_ dc = new Detail_();
		dc.setName("d3");
		LoginLog_Condition lc = new LoginLog_Condition();
		lc.setIdNotEqual(102);
		dc.setLoginLog(lc);
		lc.setAccount(new Account_());
		lc.getAccount().setRoleDeputy(new Role_Condition());
		((Role_Condition) (lc.getAccount().getRoleDeputy())).setNameNotEquals("roleDeputy3");
		Collection<Detail_> detailC = detailService.selectAllPrefix2(dc);
		Assert.assertEquals(1, detailC.size());
	}
}

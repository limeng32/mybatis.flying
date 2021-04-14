package indi.mybatis.flying.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DatabaseTearDowns;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.mapper.AccountMapper;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class DirectSqlTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountMapper accountMapper;

	/** flying托管与非托管共存的情况 */
	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/directSqlTest/testDirectSelect.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/directSqlTest/testDirectSelect.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/directSqlTest/testDirectSelect.result.xml") })
	public void testDirectSelect() {
		Account_ account1 = accountService.selectDirect(1);
		Assert.assertEquals("ann", account1.getName());
		Assert.assertEquals("5a690d842935c51f26f473e025c1b97a", account1.getPassword());

		Account_ account2 = accountService.select(1);
		Assert.assertNull(account2.getPassword());

		Account_ account3 = accountService.selectEverything(1);
		Assert.assertEquals("5a690d842935c51f26f473e025c1b97a", account3.getPassword());

		Map<String, Object> map = new HashMap<>(4);
		map.put("name", "bob");
		map.put("email", "bob@live.cn");
		Collection<Account_> c1 = accountService.selectAllDirect(map);
		for (Account_ t : c1) {
			Assert.assertEquals("5a690d842935c51f26f473e025c1b97b", t.getPassword());
		}

		Account_ a1 = new Account_();
		a1.setName("bob");
		a1.setEmail("bob@live.cn");
		Collection<Account_> c2 = accountService.selectAll(a1);
		for (Account_ t : c2) {
			Assert.assertNull(t.getPassword());
		}

		Collection<Account_> c3 = accountService.selectAllEverything(a1);
		for (Account_ t : c3) {
			Assert.assertEquals("5a690d842935c51f26f473e025c1b97b", t.getPassword());
		}

		Collection<Account_> c4 = accountMapper.selectAllDirect2("bob", "bob@live.cn");
		for (Account_ t : c4) {
			Assert.assertEquals("5a690d842935c51f26f473e025c1b97b", t.getPassword());
		}

		Account_ a = new Account_();
		a.setName("bob");
		a.setEmail("bob@live.cn");
		Collection<Account_> c5 = accountMapper.selectAllDirect3(a);
		for (Account_ t : c5) {
			Assert.assertEquals("5a690d842935c51f26f473e025c1b97b", t.getPassword());
		}

		Collection<Account_> c6 = accountMapper.selectAllDirect4("bob@live.cn", "bob");
		for (Account_ t : c6) {
			Assert.assertEquals("5a690d842935c51f26f473e025c1b97b", t.getPassword());
		}
	}
}

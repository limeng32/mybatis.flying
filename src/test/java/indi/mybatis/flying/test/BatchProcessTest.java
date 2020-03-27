package indi.mybatis.flying.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
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
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class BatchProcessTest {

	@Autowired
	private AccountService accountService;

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate.xml")
	public void testBatchUpdate() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setActivated(false);
		ac.setOpLock(a.getOpLock());

		ac.setNameEqual("ann");
		ac.setActivateValueEqual("aaa");

		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate2.xml")
	public void testBatchUpdate2() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameLessOrEqual("c");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate3.xml")
	public void testBatchUpdate3() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameLessThan("c");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate4.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate4.xml")
	public void testBatchUpdate4() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameGreaterOrEqual("b");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate5.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate5.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate5.xml")
	public void testBatchUpdate5() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameGreaterThan("b");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate6.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate6.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate6.xml")
	public void testBatchUpdate6() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameLike("a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate7.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate7.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate7.xml")
	public void testBatchUpdate7() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameHeadLike("a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate8.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate8.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate8.xml")
	public void testBatchUpdate8() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameTailLike("a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate9.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate9.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate9.xml")
	public void testBatchUpdate9() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameNotEqual("2a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate10.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate10.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate10.xml")
	public void testBatchUpdate10() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameIsNull(true);
		accountService.update(ac);

		Account_Condition ac2 = new Account_Condition();
		ac2.setEmail("bob@tom.com");
		ac2.setOpLock(a.getOpLock());
		ac2.setNameIsNull(false);
		ac2.setActivateValueEqual("aaa");
		accountService.update(ac2);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate11.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate11.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate11.xml")
	public void testBatchUpdate11() {
		List<String> nameC = new ArrayList<>();
		nameC.add("a2");
		nameC.add("b");
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameIn(nameC);
		accountService.update(ac);

		Account_Condition ac2 = new Account_Condition();
		ac2.setEmail("bob@tom.com");
		ac2.setOpLock(a.getOpLock());
		ac2.setNameIn(new ArrayList<String>());
		accountService.update(ac2);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate12.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate12.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate12.xml")
	public void testBatchUpdate12() {
		List<String> nameC = new ArrayList<>();
		nameC.add("a2");
		nameC.add("b");
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setOpLock(a.getOpLock());
		ac.setNameNotIn(nameC);
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete.xml")
	public void testBatchDelete() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setOpLock(a.getOpLock());
		ac.setNameEqual("a2a");
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete2.xml")
	public void testBatchDelete2() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setOpLock(a.getOpLock());
		ac.setNameGreaterThan("b");
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete3.xml")
	public void testBatchDelete3() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setOpLock(a.getOpLock());
		ac.setNameLike("a");
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete4.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete4.xml")
	public void testBatchDelete4() {
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setOpLock(a.getOpLock());
		ac.setNameIsNull(true);
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete5.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete5.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete5.xml")
	public void testBatchDelete5() {
		List<String> nameC = new ArrayList<>();
		nameC.add("a2");
		nameC.add("b");
		Account_ a = accountService.select(1);
		Account_Condition ac = new Account_Condition();
		ac.setOpLock(a.getOpLock());
		ac.setNameIn(nameC);
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchInsert.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchInsert.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchInsert.datasource.result.xml") })
	public void testBatchInsert() {
		Collection<Account_> ac = new ArrayList<>();
		Role_ r1 = new Role_(), r2 = new Role_(), r3 = new Role_();
		r1.setId(1);
		r2.setId(2);
		r3.setId(3);
		Account_ a = new Account_();
		a.setId(1L);
		a.setName("ann");
		a.setEmail("ann@live.cn");
		a.setPassword("5a690d842935c51f26f473e025c1b97a");
		a.setActivated(true);
		a.setActivateValue("");
		a.setRole(r1);
		ac.add(a);

		Account_ a2 = new Account_();
		a2.setId(2L);
		a2.setName("bob");
		a2.setEmail("bob@live.cn");
		a2.setPassword("6a690d842935c51f26f473e025c1b97a");
		a2.setActivated(true);
		a2.setActivateValue("");
		a2.setRole(r2);
		ac.add(a2);

		Account_ a3 = new Account_();
		a3.setId(3L);
		a3.setName("carl");
		a3.setEmail("carl@live.cn");
		a3.setPassword("7a690d842935c51f26f473e025c1b97a");
		a3.setActivated(true);
		a3.setActivateValue("");
		a3.setRole(r3);
		ac.add(a3);
		accountService.insertBatch(ac);
		System.out.println(JSONObject.toJSONString(ac));
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchInsert2.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchInsert2.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchInsert2.datasource.result.xml") })
	public void testBatchInsert2() {
		Collection<Account_> ac = new ArrayList<>();
		Role_ r1 = new Role_(), r2 = new Role_(), r3 = new Role_();
		r1.setId(1);
		r2.setId(2);
		r3.setId(3);
		Account_ a = new Account_();
		a.setName("ann");
		a.setEmail("ann@live.cn");
		a.setPassword("5a690d842935c51f26f473e025c1b97a");
		a.setActivated(true);
		a.setActivateValue("");
		a.setRole(r1);
		ac.add(a);

		Account_ a2 = new Account_();
		a2.setName("bob");
		a2.setEmail("bob@live.cn");
		a2.setPassword("6a690d842935c51f26f473e025c1b97a");
		a2.setActivated(true);
		a2.setActivateValue("");
		a2.setRole(r2);
		ac.add(a2);

		Account_ a3 = new Account_();
		a3.setName("carl");
		a3.setEmail("carl@live.cn");
		a3.setPassword("7a690d842935c51f26f473e025c1b97a");
		a3.setActivated(true);
		a3.setActivateValue("");
		a3.setRole(r3);
		ac.add(a3);
		accountService.insertSnowFlakeBatch(ac);
		System.out.println(JSONObject.toJSONString(ac));

		Collection<Account_> c = accountService.selectAll(new Account_());
		System.out.println("2:" + JSONObject.toJSONString(c));
	}
}

package indi.mybatis.flying.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.StoryStatus_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
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
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setActivated(false);
		ac.setNameEqual("ann");
		ac.setActivateValueEqual("aaa");
		ac.setStatusEquals(StoryStatus_.SKETCH);

		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate2.xml")
	public void testBatchUpdate2() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameLessOrEqual("c");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate3.xml")
	public void testBatchUpdate3() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameLessThan("c");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate4.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate4.xml")
	public void testBatchUpdate4() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameGreaterOrEqual("b");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate5.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate5.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate5.xml")
	public void testBatchUpdate5() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameGreaterThan("b");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate6.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate6.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate6.xml")
	public void testBatchUpdate6() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameLike("a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate7.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate7.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate7.xml")
	public void testBatchUpdate7() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameHeadLike("a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate8.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate8.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate8.xml")
	public void testBatchUpdate8() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameTailLike("a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate9.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate9.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate9.xml")
	public void testBatchUpdate9() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameNotEqual("2a");
		accountService.update(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate10.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate10.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdate10.xml")
	public void testBatchUpdate10() {
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameIsNull(true);
		accountService.update(ac);

		Account_Condition ac2 = new Account_Condition();
		ac2.setEmail("bob@tom.com");
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
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameIn(nameC);
		accountService.update(ac);

		Account_Condition ac2 = new Account_Condition();
		ac2.setEmail("bob@tom.com");
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
		Account_Condition ac = new Account_Condition();
		ac.setEmail("ann@tom.com");
		ac.setNameNotIn(nameC);
		accountService.update(ac);

		Account_ a2 = new Account_();
		a2.setId(2L);
		a2.setPassword("aaa");
		// wrong opLock
		a2.setOpLock(10);
		accountService.update(a2);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdatePersistent.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdatePersistent.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchUpdatePersistent.xml")
	public void testBatchUpdatePersistent() {
		Account_Condition ac = new Account_Condition();
		ac.setId(1L);
		ac.setName("a2");
		ac.setOpLock(1);
		ac.setActivated(false);
		accountService.updatePersistent(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete.xml")
	public void testBatchDelete() {
		Account_Condition ac = new Account_Condition();
		ac.setNameEqual("a2a");
		ac.setStatusEquals(StoryStatus_.SKETCH);
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete2.xml")
	public void testBatchDelete2() {
		Account_Condition ac = new Account_Condition();
		ac.setNameGreaterThan("b");
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete3.xml")
	public void testBatchDelete3() {
		Account_Condition ac = new Account_Condition();
		ac.setNameLike("a");
		accountService.delete(ac);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete4.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testBatchDelete4.xml")
	public void testBatchDelete4() {
		Account_Condition ac = new Account_Condition();
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
		Account_Condition ac = new Account_Condition();
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
	public void testBatchInsert() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Collection<Account_> ac = new ArrayList<>();
		Role_ r1 = new Role_(), r2 = new Role_(), r3 = new Role_();
		r1.setId(1);
		r2.setId(2);
		r3.setId(3);
		Account_ a = new Account_();
		a.setId(1L);
//		a.setName("ann");
		a.setEmail("ann@live.cn");
		a.setPassword("5a690d842935c51f26f473e025c1b97a");
		a.setActivated(true);
		a.setActivateValue("");
//		a.setRole(r1);
		ac.add(a);

		Account_ a2 = new Account_();
		a2.setId(2L);
		a2.setName("bob");
		a2.setEmail("bob@live.cn");
		a2.setPassword("6a690d842935c51f26f473e025c1b97a");
		a2.setActivated(true);
		a2.setActivateValue("");
		a2.setRoleIdDelegate(23L);
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
		System.out.println(new org.json.JSONObject(ac));

		Collection<Account_> ac2 = new ArrayList<>();
		try {
			accountService.insertBatch(ac2);
			// below code should never reach
			Assert.assertTrue(false);
		} catch (Exception e) {

		}
		System.out.println(BeanUtils.describe(ac2));
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
//		a.setName("ann");
		a.setEmail("ann@live.cn");
		a.setPassword("5a690d842935c51f26f473e025c1b97a");
		a.setActivated(true);
		a.setActivateValue("");
//		a.setRole(r1);
		ac.add(a);

		Account_ a2 = new Account_();
		a2.setName("bob");
		a2.setEmail("bob@live.cn");
		a2.setPassword("6a690d842935c51f26f473e025c1b97a");
		a2.setActivated(true);
		a2.setActivateValue("");
		a2.setRoleIdDelegate(22L);
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
		for (Account_ e : ac) {
			Assert.assertNotNull(e.getId());
		}
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.result.xml") })
	public void testUpdateBatch() {
		List<Account_> l = new LinkedList<>();
		Account_ a1 = new Account_();
		Account_ a2 = new Account_();
		Account_ a3 = new Account_();
		a1.setId(1L);
		a1.setName("ann1");
		l.add(a1);
		a2.setId(2L);
		a2.setName("bob2");
		a2.setPassword("b");
		a2.setStatus(StoryStatus_.CANCEL);

		Role_ r1 = new Role_();
		r1.setId(11);
		a2.setRole(r1);

		l.add(a2);
		a3.setId(3L);
		a3.setName("carl3");
		a3.setRoleIdDelegate(24L);
		l.add(a3);
		int i = accountService.updateBatch(l);
		System.out.println("::" + i);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch2.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.result.xml") })
	public void testUpdateBatch2() {
		List<Account_> l = new LinkedList<>();
		Account_ a1 = new Account_();
		Account_ a2 = new Account_();
		Account_ a3 = new Account_();
		a1.setId(1L);
		a1.setName("ann1");
		l.add(a1);
		a2.setId(2L);
		a2.setName("bob2");
		a2.setPassword("b");
		a2.setStatus(StoryStatus_.CANCEL);

		Role_ r1 = new Role_();
		r1.setId(11);
		a2.setRole(r1);

		l.add(a2);
		a3.setId(3L);
		a3.setName("carl3");
		a3.setRoleIdDelegate(24L);
		l.add(a3);
		int i = accountService.updateBatch(l);
		System.out.println("::" + i);

		List<Account_> l2 = new LinkedList<>();
		Account_ a4 = new Account_();
		Account_ a5 = new Account_();
		a4.setId(4L);
		a4.setRole(r1);
		l2.add(a4);

		a5.setId(5L);
		a5.setRoleIdDelegate(24L);
		l2.add(a5);
		i = accountService.updateBatch(l2);
		System.out.println("::" + i);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch3.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.result.xml") })
	public void testUpdateBatch3() {
		List<Account_> l = new LinkedList<>();
		Account_ a1 = new Account_();
		Account_ a2 = new Account_();
		Account_ a3 = new Account_();
		a1.setId(1L);
		a1.setName("ann1");
		l.add(a1);

		a2.setId(2L);
		a2.setName("bob2");
		a2.setPassword("b");
		a2.setStatus(StoryStatus_.CANCEL);
		Role_ r1 = new Role_();
		r1.setId(11);
		a2.setRole(r1);
		l.add(a2);

		a3.setId(3L);
		a3.setName("carl3");
		Role_ r2 = new Role_();
		r2.setId(24);
		a3.setRole(r2);
		l.add(a3);
		int i = accountService.updateBatch(l);
		System.out.println("::" + i);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch4.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/batchProcessTest/testUpdateBatch.datasource.result.xml") })
	public void testUpdateBatch4() {
		List<Account_> l = new LinkedList<>();
		Account_ a1 = new Account_();
		Account_ a2 = new Account_();
		Account_ a3 = new Account_();
		a1.setId(1L);
		a1.setName("ann1");
		l.add(a1);

		a2.setId(2L);
		a2.setName("bob2");
		a2.setPassword("b");
		a2.setStatus(StoryStatus_.CANCEL);
		l.add(a2);

		a3.setId(3L);
		a3.setName("carl3");
		l.add(a3);
		int i = accountService.updateBatch(l);
		System.out.println("::" + i);
	}
}

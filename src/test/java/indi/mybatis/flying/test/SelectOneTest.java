package indi.mybatis.flying.test;

import java.util.Collection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.models.Conditionable.Sequence;
import indi.mybatis.flying.pagination.Order;
import indi.mybatis.flying.pagination.PageParam;
import indi.mybatis.flying.pagination.SortParam;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class SelectOneTest {

	@Autowired
	private AccountService accountService;

	@BeforeClass
	public static void prepareDatabase() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/H2_TYPE.sql")
				.addScript("classpath:/INIT_TABLE.sql").build();
	}

	/** 测试selectOne1 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne1.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne1.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne1.result.xml")
	public void testSelectOne1() {
		Account_ a1 = new Account_();
		a1.setName("ann");
		a1.setEmail("ann@live.cn");
		accountService.insert(a1);
		Account_ a2 = new Account_();
		a2.setName("bob");
		a2.setEmail("bob@live.cn");
		accountService.insert(a2);

		Account_ ac = new Account_();
		ac.setName("bob");
		Account_ account = accountService.selectOne(ac);
		Assert.assertEquals("bob@live.cn", account.getEmail());
	}

	/** 测试selectOne2 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne2.result.xml")
	public void testSelectOne2() {
		Account_ a1 = new Account_();
		a1.setName("ann");
		a1.setEmail("ann@live.cn");
		accountService.insert(a1);
		Account_ a2 = new Account_();
		a2.setName("ann");
		a2.setEmail("bob@live.cn");
		accountService.insert(a2);

		Account_Condition ac = new Account_Condition();
		ac.setLimiter(new PageParam(1, 2));
		ac.setSorter(new SortParam(new Order("id", Sequence.asc)));
		ac.setName("ann");
		Account_ account = accountService.selectOne(ac);
		Assert.assertEquals("ann@live.cn", account.getEmail());
		Assert.assertNull(ac.getLimiter());
	}

	/** 测试selectOne3，缓存测试 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne3.result.xml")
	public void testSelectOne3() {
		Account_ a1 = new Account_();
		a1.setName("ann");
		accountService.insert(a1);
		Account_ a2 = new Account_();
		a2.setName("bob");
		accountService.insert(a2);

		Account_ ac = new Account_();
		Account_ account1 = accountService.selectOne(ac);
		Assert.assertEquals("ann", account1.getName());
		Collection<Account_> accountC = accountService.selectAll(ac);
		Assert.assertEquals(2, accountC.size());
	}
}

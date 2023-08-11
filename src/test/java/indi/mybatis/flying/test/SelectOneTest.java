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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.models.Conditionable.Sequence;
import indi.mybatis.flying.pagination.Order;
import indi.mybatis.flying.pagination.PageParam;
import indi.mybatis.flying.pagination.SortParam;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class SelectOneTest {

	@Autowired
	private AccountService accountService;

	/** 测试selectOne2 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne2.result.xml")
	public void testSelectOne2() {
		Account_Condition ac = new Account_Condition();
		ac.setLimiter(new PageParam(6, 8));
		ac.setSorter(new SortParam(new Order("id", Sequence.ASC)));
		ac.setName("ann");
		Account_ account = accountService.selectOne(ac);
		Assert.assertEquals("ann@live.cn", account.getEmail());

		Account_Condition ac2 = new Account_Condition();
		ac2.setEmail("ann@live.cn");
		ac2.setSorter(new SortParam(new Order("id", Sequence.DESC)));
		ac2.setLimiter(new PageParam(2, 2));
		Account_ account2 = accountService.selectOne(ac2);
		Assert.assertEquals("elon", account2.getName());
	}

	/** 测试selectOne3，缓存测试 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/selectOneTest/testSelectOne3.result.xml")
	public void testSelectOne3() {
		Account_Condition ac = new Account_Condition();
		Account_ account1 = accountService.selectOne(ac);
		Assert.assertEquals("ann", account1.getName());
		ac.setEmail("ann@live.cn");
		ac.setLimiter(new PageParam(1, 8));
		ac.setSorter(new SortParam(new Order("id", Sequence.ASC), new Order("name", Sequence.DESC),
				new Order("id", Sequence.DESC)));
		Collection<Account_> accountC = accountService.selectAll(ac);
		Assert.assertEquals(2, accountC.size());
	}
}

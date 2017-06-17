package indi.mybatis.flying.test;

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

import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.service.AccountService;
import junit.framework.TestCase;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class InternalCacheTest extends TestCase {

	@Autowired
	private AccountService accountService;

	@BeforeClass
	public static void prepareDatabase() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/H2_TYPE.sql")
				.addScript("classpath:/INIT_TABLE.sql").build();
	}

	private class acctionSelect extends TestRunnable {

		public void runTest() throws Throwable {
			Account_ account = accountService.select(1);
			Assert.assertEquals("ann", account.getName());
			long l = 6;
			Thread.sleep(l * 1000);
			account = accountService.select(1);
			Assert.assertEquals("bob", account.getName());
		}
	}

	private class acctionUpdate extends TestRunnable {

		public void runTest() throws Throwable {
			long l = 3;
			Thread.sleep(l * 1000);
			Account_ account = accountService.select(1);
			account.setName("bob");
			accountService.update(account);
		}
	}

	/**
	 * 这是一个展示Junit配合Groboutils进行多线程测试的例子，在这个例子中accountInsert以子线程的方式执行多次，
	 * 主线程会等待子线程全部执行完后再结束
	 */
	@Test
	@IfProfileValue(name = "CACHE", value = "false")
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/internalCacheTest/testCacheThread.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/internalCacheTest/testCacheThread.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/internalCacheTest/testCacheThread.result.xml")
	public void testCacheThread() throws Throwable {

		// 实例化 TestRunnable 类
		TestRunnable tr1, tr2;
		tr1 = new acctionSelect();
		tr2 = new acctionUpdate();
		// 把实例传递给 MTTR
		TestRunnable[] trs = { tr1, tr2 };
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		// 执行MTTR和线程
		mttr.runTestRunnables();
	}

}

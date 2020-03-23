package indi.mybatis.flying.test;

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
import indi.mybatis.flying.service.AccountService;
import junit.framework.TestCase;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class ThreadTest extends TestCase {

	@Autowired
	private AccountService accountService;

	private class accountInsert extends TestRunnable {

		private Long id;

		private String name;

		private accountInsert(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public void runTest() throws Throwable {
			long l = Math.round(1 + Math.random() * 1);
			// 线程等待1到2秒
			Thread.sleep(l * 1000);
			Account_ account = new Account_();
			account.setId(id);
			account.setName(name);
			accountService.insert(account);
		}
	}

	/**
	 * 这是一个展示Junit配合Groboutils进行多线程测试的例子，在这个例子中accountInsert以子线程的方式执行多次，
	 * 主线程会等待子线程全部执行完后再结束
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/threadTest/testExampleThread.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/threadTest/testExampleThread.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/threadTest/testExampleThread.result.xml")
	public void testExampleThread() throws Throwable {

		// 实例化 TestRunnable 类
		TestRunnable tr1, tr2;
		tr1 = new accountInsert(2L, "ann");
		tr2 = new accountInsert(3L, "ann");
		// 把实例传递给 MTTR
		TestRunnable[] trs = { tr1, tr2 };
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		// 执行MTTR和线程
		mttr.runTestRunnables();
	}

}

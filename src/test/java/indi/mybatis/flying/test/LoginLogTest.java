package indi.mybatis.flying.test;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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

import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.service.LoginLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
@ContextConfiguration("classpath:spring-test.xml")
public class LoginLogTest {

	@BeforeClass
	public static void prepareDatabase() {
		new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/H2_TYPE.sql")
				.addScript("classpath:/INIT_TABLE.sql").build();
	}

	@Autowired
	private BasicDataSource dataSource;

	@Autowired
	private LoginLogService loginLogService;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource);
		Assert.assertNotNull(dataSource.getUsername());
	}

	/** 测试insert功能（无乐观锁） */
	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/loginLogTest/testInsert.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/loginLogTest/testInsert.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/loginLogTest/testInsert.xml")
	public void testInsert() {
		LoginLog_ a = new LoginLog_();
		a.setId(1);
		a.setLoginIP("0.0.0.1");
		loginLogService.insert(a);
	}
}

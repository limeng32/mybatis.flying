package indi.mybatis.flying.testboot;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
		"dataSource2" })
public class MyTest {

	@Autowired
	@Qualifier("dataSource1")
	private DataSource dataSource1;
	
	@Autowired
	@Qualifier("dataSource2")
	private DataSource dataSource2;

	@Test
	public void test1() {
		Assert.assertNotNull(dataSource1);
		Assert.assertNotNull(dataSource2);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource.result.xml"),
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/accountTest/testInsert.datasource2.result.xml") })
	public void test2() {
		Assert.assertTrue(true);
	}
}

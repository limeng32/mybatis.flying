package indi.mybatis.flying.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
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
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.pojo.Detail2_;
import indi.mybatis.flying.service2.Detail2Service;
import indi.mybatis.flying.service2.LoginLogSource2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class JpaTest {

	@Autowired
	private Detail2Service detail2Service;
	
	@Autowired
	private LoginLogSource2Service loginLogSource2Service;

	@Test
	public void test1() {
		Assert.assertNotNull(detail2Service);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource2", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/jpaTest/testDetail2.datasource2.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource2", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/jpaTest/testDetail2.datasource2.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource2", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/jpaTest/testDetail2.datasource2.result.xml") })
	public void testDetail2() {
		Detail2_ detail2 = detail2Service.select(1);
		Assert.assertEquals("n", detail2.getName());
		Assert.assertEquals(123, detail2.getNumber().intValue());
		Assert.assertEquals("l", detail2.getLoginLogSource2().getLoginIP());
		loginLogSource2Service.update(detail2.getLoginLogSource2());

		Detail2_ detail2_2 = new Detail2_();
		detail2_2.setName("name");
		detail2_2.setDetail("detail");
		detail2_2.setNumber(321);
		detail2Service.insert(detail2_2);

		Detail2_ detail2_c = new Detail2_();
		detail2_c.setName("name");
		detail2_c.setDetail("detail");
		Detail2_ detail2_3 = detail2Service.selectOne(detail2_c);
		Assert.assertEquals(detail2_2.getId(), detail2_3.getId());
	}
}

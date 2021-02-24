package indi.mybatis.flying.test;

import javax.sql.DataSource;

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
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.mapper3.EmpScoreDao;
import indi.mybatis.flying.pojo.EmpScore;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSourceExamine" })
public class AesCryptTest {

	@Autowired
	private DataSource dataSourceExamine;

	@Autowired
	private EmpScoreDao empScoreDao;

	@Test
	public void test1() {
		Assert.assertNotNull(dataSourceExamine);
	}

	@Test
	@DatabaseSetup(connection = "dataSourceExamine", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/aesCryptTest/test2.xml")
	@ExpectedDatabase(connection = "dataSourceExamine", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/aesCryptTest/test2.result.xml")
//	@DatabaseTearDown(connection = "dataSourceExamine", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/aesCryptTest/test2.result.xml")
	public void test2() {
		EmpScore es = empScoreDao.select(1L);
		Assert.assertEquals("111", es.getStaffId());

		EmpScore es2 = new EmpScore();
		es2.setId(2L);
		es2.setStaffId("222");
		es2.setStaffName("test2");
		es2.setSecret2("luffy");
		empScoreDao.insertAes(es2);

		EmpScore es4 = new EmpScore();
		es4.setId(4L);
		es4.setStaffId("444");
		es4.setStaffName("test2");
		es4.setSecret2("luffy2");
		empScoreDao.insertAes(es4);

		EmpScore es3 = new EmpScore();
		es3.setStaffName("test2");
		es3.setSecret2("luffy");
		EmpScore empScore2 = empScoreDao.selectAes(es2);
		System.out.println("::" + empScore2.getSecret2());
		Assert.assertEquals("luffy", empScore2.getSecret2());
		Assert.assertEquals("222", empScore2.getStaffId());
	}
}

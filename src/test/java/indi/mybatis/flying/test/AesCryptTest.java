package indi.mybatis.flying.test;

import java.util.ArrayList;
import java.util.List;

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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.mapper3.Account3Dao;
import indi.mybatis.flying.mapper3.EmpScoreDao;
import indi.mybatis.flying.pojo.Account3;
import indi.mybatis.flying.pojo.EmpScore;
import indi.mybatis.flying.pojo.condition.EmpScoreCondition;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSourceExamine" })
public class AesCryptTest {

	@Autowired
	private DataSource dataSourceExamine;

	@Autowired
	private Account3Dao account3Dao;

	@Autowired
	private EmpScoreDao empScoreDao;

	@Test
	public void test1() {
		Assert.assertNotNull(dataSourceExamine);
	}

	@Test
	@DatabaseSetup(connection = "dataSourceExamine", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/aesCryptTest/test2.xml")
	@ExpectedDatabase(connection = "dataSourceExamine", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/aesCryptTest/test2.result.xml")
//	@DatabaseTearDown(connection = "dataSourceExamine", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/aesCryptTest/test2.result.xml")
	public void test2() {

		EmpScore es = new EmpScore();
		es.setId(1L);
		es.setStaffId("111");
		es.setStaffName("test1");
		es.setSecret2("l");
		empScoreDao.insert(es);

		EmpScore es2 = new EmpScore();
		es2.setId(2L);
		es2.setStaffId("222");
		es2.setStaffName("test2");
		es2.setSecret2("luffy");
		empScoreDao.insert(es2);

		EmpScore es4 = new EmpScore();
		es4.setId(4L);
		es4.setStaffId("444");
		es4.setStaffName("test2");
		es4.setSecret2("luffy2");
		empScoreDao.insert(es4);

		EmpScore es5 = new EmpScore();
		es5.setId(5L);
		es5.setStaffId("555");
		es5.setStaffName("test3");
		es5.setSecret2("luffy");
		empScoreDao.insert(es5);

		EmpScore es3 = new EmpScore();
		es3.setStaffName("test2");
		es3.setSecret2("luffy");
		EmpScore empScore2 = empScoreDao.selectOne(es2);
		Assert.assertEquals("luffy", empScore2.getSecret2());
		Assert.assertEquals("222", empScore2.getStaffId());

		es2.setId(null);
		es2.setStaffName(null);
		es2.setStaffId(null);
		int c = empScoreDao.count(es2);
		Assert.assertEquals(2, c);

		List<EmpScore> l = new ArrayList<>();
		EmpScore es11 = new EmpScore();
		es11.setId(11L);
		es11.setStaffId("011");
		es11.setStaffName("test11");
		es11.setSecret2("luffy2");
		l.add(es11);
		EmpScore es12 = new EmpScore();
		es12.setId(12L);
		es12.setStaffId("012");
		es12.setStaffName("test12");
		es12.setSecret2("luffy2");
		l.add(es12);
		EmpScore es13 = new EmpScore();
		es13.setId(13L);
		es13.setStaffId("013");
		es13.setStaffName("test13");
		es13.setSecret2("luffy2");
		l.add(es13);
		empScoreDao.insertBatch(l);

		EmpScore es131 = new EmpScore();
		es131.setStaffName("test13");
		es131.setSecret2("luffy2");
		EmpScore empScore = empScoreDao.selectOne(es131);
		empScore.setStaffId(null);
		empScore.setSecret2("luffy3");
		empScoreDao.update(empScore);
		es131.setSecret2("luffy3");
		empScore = empScoreDao.selectOne(es131);
		Assert.assertEquals("luffy3", empScore.getSecret2());
		empScore.setSecret2(null);
		empScoreDao.updatePersistent(empScore);

		EmpScore es121 = new EmpScore();
		es121.setStaffName("test12");
		es121.setSecret2("luffy2");
		EmpScore empScore3 = empScoreDao.selectOne(es121);
		empScore3.setStaffName(null);
		empScoreDao.updatePersistent(empScore3);

		List<EmpScore> l2 = new ArrayList<>();
		EmpScore es14 = new EmpScore();
		es14.setId(14L);
		es14.setStaffId("014");
		es14.setStaffName("test14");
		es14.setSecret2("luffy4");
		l2.add(es14);
		EmpScore es15 = new EmpScore();
		es15.setId(15L);
		es15.setStaffId("015");
		es15.setStaffName("test15");
		es15.setSecret2("luffy4");
		l2.add(es15);
		EmpScore es16 = new EmpScore();
		es16.setId(16L);
		es16.setStaffId("016");
		es16.setStaffName("test16");
		es16.setSecret2("luffy4");
		l2.add(es16);
		empScoreDao.insertBatch(l2);
		es14.setId(14L);
		es14.setSecret2("luffy5");
		es15.setId(15L);
		es15.setSecret2("luffy5");
		es16.setId(16L);
		empScoreDao.updateBatch(l2);

		EmpScore empScore4 = empScoreDao.select(16L);
		Assert.assertEquals("luffy4", empScore4.getSecret2());

		Account3 a = new Account3();
		a.setId(1);
		a.setName("ann");
		a.setEmpScore(es);
		account3Dao.insert(a);

		Account3 account = account3Dao.select(1);
		Assert.assertEquals("ann", account.getName());
		Assert.assertEquals("l", account.getEmpScore().getSecret2());

		Account3 a2 = new Account3();
		a2.setName("ann");
		EmpScore es100 = new EmpScore();
		es100.setSecret2("l");
		a2.setEmpScore(es100);
		Account3 account2 = account3Dao.selectOne(a2);
		Assert.assertNotNull(account2);
		List<Account3> list = account3Dao.selectAll(a2);
		Assert.assertEquals(1, list.size());

		int c2 = account3Dao.count(a2);
		Assert.assertEquals(1, c2);

		EmpScoreCondition esc = new EmpScoreCondition();
		esc.setSecret2Like("luffy");
		int c3 = empScoreDao.count(esc);
		System.out.println("c3::" + c3);

		EmpScoreCondition esc2 = new EmpScoreCondition();
		esc2.setSecret2Equal("l");
		EmpScore empScore5 = empScoreDao.selectOne(esc2);
		Assert.assertEquals("111", empScore5.getStaffId());
	}
}

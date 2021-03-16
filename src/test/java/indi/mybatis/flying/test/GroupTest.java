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
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
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
import indi.mybatis.flying.mapper.EmpScore2Mapper;
import indi.mybatis.flying.mapper.ProjRatioMapper;
import indi.mybatis.flying.pojo.EmpScore2;
import indi.mybatis.flying.pojo.ProjRatio;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1" })
public class GroupTest {
	@Autowired
	private DataSource dataSource1;

	@Autowired
	private EmpScore2Mapper empScore2Mapper;

	@Autowired
	private ProjRatioMapper projRatioMapper;

	@Test
	public void testDataSource() {
		Assert.assertNotNull(dataSource1);
		Assert.assertNotNull(projRatioMapper);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/groupTest/test1.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/groupTest/test1.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/groupTest/test1.datasource.xml") })
	public void test1() {
		ProjRatio projRatio = projRatioMapper.select(1);
		System.out.println("::" + JSONObject.toJSONString(projRatio));
		EmpScore2 empScore = empScore2Mapper.select(1L);
		System.out.println("::" + JSONObject.toJSONString(empScore));
		Assert.assertEquals(1, empScore.getProjRatio().getId().intValue());

		EmpScore2 e = new EmpScore2();
		e.setId(3L);
		List<EmpScore2> empScore2List = empScore2Mapper.selectAll(e);
		Assert.assertEquals(1, empScore2List.size());
		Assert.assertEquals(3, empScore2List.get(0).getProjRatio().getId().intValue());

		ProjRatio p = new ProjRatio();
		p.setId(2L);
		e.setProjRatio(p);
		List<EmpScore2> empScore2List2 = empScore2Mapper.selectAll(e);
		Assert.assertEquals(1, empScore2List2.size());
		Assert.assertEquals(2, empScore2List2.get(0).getProjRatio().getId().intValue());

		EmpScore2 e2 = new EmpScore2();
		e2.setId(3L);
		int c = empScore2Mapper.count(e2);
		Assert.assertEquals(2, c);

		EmpScore2 e3 = new EmpScore2();
		e3.setStaffId("111");
		int c2 = empScore2Mapper.count(e3);
		Assert.assertEquals(2, c2);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/groupTest/test2.datasource.xml") })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/groupTest/test2.datasource.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/groupTest/test2.datasource.xml") })
	public void test2() {
		EmpScore2 e = new EmpScore2();
		e.setState("0");
		int c = empScore2Mapper.count(e);
		Assert.assertEquals(3, c);
		System.out.println("::" + JSONObject.toJSONString(e));

		List<EmpScore2> l = empScore2Mapper.selectAll(e);
		Assert.assertEquals(3, l.size());
		System.out.println("::" + JSONObject.toJSONString(e));

		EmpScore2 emp1 = empScore2Mapper.select(1L);
		emp1.setState("2");
		empScore2Mapper.update(emp1);

		EmpScore2 emp2 = empScore2Mapper.select(2L);
		System.out.println("::" + JSONObject.toJSONString(emp2));
		emp2.setState(null);
		empScore2Mapper.updatePersistent(emp2);

		EmpScore2 e2 = new EmpScore2();
		e2.setId(8L);
		e2.setStaffId("120");
		e2.setStaffName("丁七");
		e2.setYear("2020");
		e2.setSeason(4);
		e2.setState("1");
		empScore2Mapper.insert(e2);

		EmpScore2 e3 = new EmpScore2(), e4 = new EmpScore2();
		e3.setId(9L);
		e3.setStaffId("120");
		e3.setStaffName("丁七");
		e3.setYear("2020");
		e3.setSeason(4);
		e3.setState("1");

		e4.setId(10L);
		e4.setStaffId("120");
		e4.setStaffName("丁七");
		e4.setYear("2020");
		e4.setSeason(4);
		e4.setState("1");

		List<EmpScore2> l2 = new ArrayList<>();
		l2.add(e3);
		l2.add(e4);
		empScore2Mapper.insertBatch(l2);

		List<EmpScore2> l3 = empScore2Mapper.selectAll(e);
		Assert.assertEquals(2, l3.size());
		for (EmpScore2 entry : l3) {
			entry.setYear("2021");
		}
		empScore2Mapper.updateBatch(l3);
	}
}

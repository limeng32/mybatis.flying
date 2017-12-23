package indi.mybatis.flying.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.models.Or;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1", "dataSource2" })
@ContextConfiguration("classpath:spring-test.xml")
public class OrConditionTest {

	/* 测试Or对象 */
	@Test
	public void test1() {
		Or<String> or = new Or<>("a", 1, 2);
		Assert.assertEquals("a", or.getValue());
		Assert.assertEquals(2, or.getAppear().length);

		Or<Integer> or2 = new Or<>(6, 2, 3, 4);
		Assert.assertEquals(6, or2.getValue().intValue());
		Assert.assertEquals(3, or2.getAppear().length);
	}
}

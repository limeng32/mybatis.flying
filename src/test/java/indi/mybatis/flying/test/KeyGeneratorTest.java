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
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

import indi.mybatis.flying.pojo.Product;
import indi.mybatis.flying.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource1" })
@ContextConfiguration("classpath:spring-test.xml")
public class KeyGeneratorTest {

	@Autowired
	private ProductService productService;

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/keyGeneratorTest/test1.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/indi/mybatis/flying/test/keyGeneratorTest/test1.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/keyGeneratorTest/test1.xml")
	public void test1() {
		Product product = productService.select("a");
		Assert.assertEquals("n", product.getName());

		Product product2 = new Product();
		// product2.setId("b");
		product2.setName("n2");
		product2.setName2("asd");
		productService.insert(product2);
		Assert.assertEquals(32, product2.getId().length());

		Product p = new Product();
		p.setName("n2");
		Product product_ = productService.selectOne(p);
		Assert.assertEquals(32, product_.getId().length());

		Product product3 = new Product();
		product3.setId("cc");
		product3.setName("n3");
		productService.insert2(product3);
		Assert.assertEquals("cc", product3.getId());

		Product p2 = new Product();
		p2.setName("n3");
		Product product2_ = productService.selectOne(p2);
		Assert.assertEquals("cc", product2_.getId());

		Product product4 = new Product();
		product4.setId("dd");
		product4.setName("n4");
		productService.insert3(product4);
		Assert.assertEquals("dd", product4.getId());

		Product p3 = new Product();
		p3.setName("n4");
		Product product3_ = productService.selectOne(p3);
		Assert.assertEquals("dd", product3_.getId());

		Product product5 = new Product();
		product5.setName("n5");
		productService.insertMilliSecond(product5);

		Product p5 = new Product();
		p5.setName("n5");
		Product product5_ = productService.selectOne(p5);
		Assert.assertEquals(product5.getId(), product5_.getId());

		Product product6 = new Product();
		product6.setName("n6");
		productService.insertSnowFlake(product6);

		Product p6 = new Product();
		p6.setName("n6");
		Product product6_ = productService.selectOne(p6);
		Assert.assertEquals(product6.getId(), product6_.getId());

	}

}

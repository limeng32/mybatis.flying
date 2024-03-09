package indi.mybatis.flying.test;

import java.util.Collection;
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
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.pagination.PageParam;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.condition.Account_Condition;
import indi.mybatis.flying.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource1",
        "dataSource2" })
public class PostgreTest {
    @Autowired
    private DataSource dataSource1;

    @Autowired
    private AccountService accountService;

    @Test
    public void testDataSource() {
        Assert.assertNotNull(dataSource1);
    }

    @Test
    @DatabaseSetup(connection = "dataSource1", type = DatabaseOperation.CLEAN_INSERT, value = "/indi/mybatis/flying/test/postgreTest/testSelect.xml")
    @ExpectedDatabase(connection = "dataSource1", override = false, assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/indi/mybatis/flying/test/postgreTest/testSelect.result.xml")
    @DatabaseTearDown(connection = "dataSource1", type = DatabaseOperation.DELETE_ALL, value = "/indi/mybatis/flying/test/postgreTest/testSelect.result.xml")
    public void testSelect() {
        List<Account_> c1 = accountService.selectUseOffset(2, 1);
        // System.out.println("c1::" + c1);
        Assert.assertEquals("bob", c1.get(0).getName());

        Account_Condition ac = new Account_Condition();
        ac.setLimiter(new PageParam(1, 2));
        Collection<Account_> c = accountService.selectAll(ac);
        // System.out.println("accounts::" + c);

        Account_ account_ = accountService.selectOne(ac);
        // System.out.println("account_::" + account_);
    }
}

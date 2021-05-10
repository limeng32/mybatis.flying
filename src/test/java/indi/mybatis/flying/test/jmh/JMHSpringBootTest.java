package indi.mybatis.flying.test.jmh;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import indi.mybatis.flying.Application;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.service.AccountService;

@State(Scope.Thread)
public class JMHSpringBootTest {

	private ConfigurableApplicationContext context;
	private AccountService accountService;

	@Ignore
	@Test
	public void launchBenchmark() throws Exception {

		Options opt = new OptionsBuilder()
				// Specify which benchmarks to run.
				// You can be more specific if you'd like to run only one benchmark per test.
				.include(this.getClass().getSimpleName())
				// Set the following options as needed
				.mode(Mode.AverageTime).timeUnit(TimeUnit.MICROSECONDS).warmupTime(TimeValue.seconds(1))
				.warmupIterations(3).measurementTime(TimeValue.seconds(1)).measurementIterations(1).threads(1).forks(1)
				// .shouldFailOnError(true).shouldDoGC(true)
				// .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
				// .addProfiler(WinPerfAsmProfiler.class)
				.build();

		new Runner(opt).run();
	}

	/**
	 * setup初始化容器的时候只执行一次
	 */
	@Setup(Level.Trial)
	public void init() {
		context = SpringApplication.run(Application.class);
//		ApplicationContextProvider.
		accountService = context.getBean(AccountService.class);
		Account_ a = new Account_();
		a.setId(1L);
		a.setName("a");
		accountService.insertDirect(a);
	}

	@TearDown
	public void tearDown() {
		Account_ a = new Account_();
		a.setId(1L);
		accountService.delete(a);
	}

	@Benchmark
	public void test() {
		Account_ account = accountService.selectDirect(1);
		Assert.assertNotNull(account);
	}

	@Benchmark
	public void test2() {
		Account_ account = accountService.select(1);
		Assert.assertNotNull(account);
	}

	@Benchmark
	public void test3() {
		Account_ a = new Account_();
		a.setId(1L);
		Account_ account = accountService.selectOne(a);
		Assert.assertNotNull(account);
	}

	@Benchmark
	public void test4() {
		Account_ a = new Account_();
		a.setId(1L);
		Permission p = new Permission();
		p.setSecret("a");
		a.setPermission(p);
		Collection<Account_> account = accountService.selectAll(a);
		Assert.assertNotNull(account);
	}

	@Benchmark
	public void test5() {
		Account_ a = new Account_();
		a.setId(1L);
		Account_ account = accountService.selectOne(a);
		Assert.assertNotNull(account);
	}

	@Benchmark
	public void test6() {
		Account_ a = new Account_();
		a.setName("a");
//		Permission p = new Permission();
//		p.setSecret("b");
//		a.setPermission(p);
		Collection<Account_> account = accountService.selectAll(a);
		Assert.assertNotNull(account);
	}
}

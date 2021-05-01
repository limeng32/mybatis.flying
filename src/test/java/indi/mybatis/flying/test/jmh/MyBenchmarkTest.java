package indi.mybatis.flying.test.jmh;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import indi.mybatis.flying.pojo2.Account;
import indi.mybatis.flying.pojo2.Account2;
import indi.mybatis.flying.pojo2.LoginLog;

public class MyBenchmarkTest {

//	@Ignore
	@Test
	public void launchBenchmark() throws Exception {

		Options opt = new OptionsBuilder()
				// Specify which benchmarks to run.
				// You can be more specific if you'd like to run only one benchmark per test.
				.include(this.getClass().getSimpleName())
				// Set the following options as needed
				.mode(Mode.AverageTime).timeUnit(TimeUnit.NANOSECONDS).warmupTime(TimeValue.seconds(1))
				.warmupIterations(5).measurementTime(TimeValue.seconds(1)).measurementIterations(5).forks(1)
				// .shouldFailOnError(true).shouldDoGC(true)
				// .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
				// .addProfiler(WinPerfAsmProfiler.class)
				.build();

		new Runner(opt).run();
	}

	@Benchmark
	public Object testMethod() {
		Account2 a = new Account2();
		a.setId(1L);
		LoginLog l1 = new LoginLog();
		l1.setId(1);
		LoginLog l2 = new LoginLog();
		l2.setId(2);
		LoginLog l3 = new LoginLog();
		l3.setId(3);
		LoginLog l4 = new LoginLog();
		l4.setId(4);
		LoginLog l5 = new LoginLog();
		l5.setId(5);
		LoginLog l6 = new LoginLog();
		l6.setId(6);
		LoginLog l7 = new LoginLog();
		l7.setId(7);
		LoginLog l8 = new LoginLog();
		l8.setId(8);
		LoginLog l9 = new LoginLog();
		l9.setId(9);
		LoginLog l10 = new LoginLog();
		l10.setId(10);
		a.addLoginLog(l1);
		a.addLoginLog(l2);
		a.addLoginLog(l3);
		a.addLoginLog(l4);
		a.addLoginLog(l5);
		a.addLoginLog(l6);
		a.addLoginLog(l7);
		a.addLoginLog(l8);
		a.addLoginLog(l9);
		a.addLoginLog(l10);
		return a;
	}

	@Benchmark
	public Object testMethod2() {
		Account a = new Account();
		a.setId(1L);
		LoginLog l1 = new LoginLog();
		l1.setId(1);
		LoginLog l2 = new LoginLog();
		l2.setId(2);
		LoginLog l3 = new LoginLog();
		l3.setId(3);
		LoginLog l4 = new LoginLog();
		l4.setId(4);
		LoginLog l5 = new LoginLog();
		l5.setId(5);
		LoginLog l6 = new LoginLog();
		l6.setId(6);
		LoginLog l7 = new LoginLog();
		l7.setId(7);
		LoginLog l8 = new LoginLog();
		l8.setId(8);
		LoginLog l9 = new LoginLog();
		l9.setId(9);
		LoginLog l10 = new LoginLog();
		l10.setId(10);
		a.addLoginLog(l1);
		a.addLoginLog(l2);
		a.addLoginLog(l3);
		a.addLoginLog(l4);
		a.addLoginLog(l5);
		a.addLoginLog(l6);
		a.addLoginLog(l7);
		a.addLoginLog(l8);
		a.addLoginLog(l9);
		a.addLoginLog(l10);
		return a;
	}

}

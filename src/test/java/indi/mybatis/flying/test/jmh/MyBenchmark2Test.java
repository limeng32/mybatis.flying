package indi.mybatis.flying.test.jmh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.annotation.SystemProfileValueSource;

@ProfileValueSourceConfiguration(SystemProfileValueSource.class)
public class MyBenchmark2Test {

	@Ignore
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
	public List<Integer> testMethod() {
		int cardCount = 54;
		List<Integer> cardList = new ArrayList<Integer>();
		for (int i = 0; i < cardCount; i++) {
			cardList.add(i);
		}
		// 洗牌算法
		Random random = new Random();
		for (int i = 0; i < cardCount; i++) {
//			Random random = new Random();
			int rand = random.nextInt(cardCount);
			Collections.swap(cardList, i, rand);
		}
		return cardList;
	}
}

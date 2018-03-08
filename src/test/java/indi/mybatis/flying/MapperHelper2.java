package indi.mybatis.flying;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MapperHelper2 {

	public MapperHelper2() {

	}

	@Pointcut("execution(* indi.mybatis.flying.*.LoginLogSource2Mapper.*(..))")
	public void sleeppoint() {
	}

	@Before("sleeppoint()")
	public void before() throws Throwable {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_2);
		System.out.println("开始切为datasource2。");
	}

	@AfterReturning("sleeppoint()")
	public void afterReturning() throws Throwable {
		System.out.println("已经切为datasource2。");
	}
}

package indi.mybatis.flying;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MapperHelper {

	public MapperHelper() {

	}

	@Pointcut("execution(* indi.mybatis.flying.*.AccountMapper.*(..))")
	public void sleeppoint() {
	}

	@Before("sleeppoint()")
	public void before() throws Throwable {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_1);
		System.out.println("开始切为datasource1。");
	}

	@AfterReturning("sleeppoint()")
	public void afterReturning() throws Throwable {
		System.out.println("已经切为datasource1。");
	}
}

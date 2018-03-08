package indi.mybatis.flying;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ServiceHelper {

	public ServiceHelper() {

	}

	@Pointcut("execution(* indi.mybatis.flying.*.LoginLogSource2Service.count(..))")
	public void sleeppoint() {
	}

	@Before("sleeppoint()")
	public void before() throws Throwable {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_2);
		System.out.println("通常情况下睡觉之前要脱衣服！");
	}

	@AfterReturning("sleeppoint()")
	public void afterReturning() throws Throwable {
		System.out.println("起床后要先穿衣服！");
	}
}

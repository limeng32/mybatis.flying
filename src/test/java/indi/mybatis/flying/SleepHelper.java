package indi.mybatis.flying;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SleepHelper {

	public SleepHelper() {

	}
	
	@Pointcut("execution(* indi.mybatis.flying.Human.sleep())")
    public void sleeppoint(){}

	@Before("sleeppoint()")
	public void before() throws Throwable {
		System.out.println("通常情况下睡觉之前要脱衣服！");
	}

	@AfterReturning("sleeppoint()")
	public void afterReturning() throws Throwable {
		System.out.println("起床后要先穿衣服！");
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_2);
	}
}

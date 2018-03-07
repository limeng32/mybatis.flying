package indi.mybatis.flying;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

public class SleepHelper implements MethodBeforeAdvice,AfterReturningAdvice{
	 public void before(Method mtd, Object[] arg1, Object arg2)
	            throws Throwable {
	        System.out.println("通常情况下睡觉之前要脱衣服！");
	    }

	    public void afterReturning(Object arg0, Method arg1, Object[] arg2,
	            Object arg3) throws Throwable {
	        System.out.println("起床后要先穿衣服！");
	        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_2);
	    }
}

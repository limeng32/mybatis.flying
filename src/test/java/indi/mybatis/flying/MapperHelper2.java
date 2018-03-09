package indi.mybatis.flying;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MapperHelper2 {

	public MapperHelper2() {

	}

	@Pointcut("execution(* indi.mybatis.flying.mapper2.*Mapper.*(..))")
	public void sleeppoint() {
	}

	@Before("sleeppoint()")
	public void before() throws Throwable {
//		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_2);
		MultipleDataSource.setDataSourceKey(CustomerContextHolder.SESSION_FACTORY_2);
	}

	@AfterReturning("sleeppoint()")
	public void afterReturning() throws Throwable {
	}
}

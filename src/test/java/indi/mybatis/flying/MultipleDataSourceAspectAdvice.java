package indi.mybatis.flying;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import indi.mybatis.flying.mapper.LoginLogMapper;
import indi.mybatis.flying.mapper2.LoginLogSource2Mapper;
import indi.mybatis.flying.service.LoginLogService;
import indi.mybatis.flying.service2.LoginLogSource2Service;

@Aspect
public class MultipleDataSourceAspectAdvice {

	@Around("execution(* indi.mybatis.flying.cool.Cool.doing(..))")
	public Object doAround(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("::aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		if (jp.getTarget() instanceof LoginLogService || jp.getTarget() instanceof LoginLogMapper) {
			CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_1);
		} else if (jp.getTarget() instanceof LoginLogSource2Service
				|| jp.getTarget() instanceof LoginLogSource2Mapper) {
			CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_2);
		}
		return jp.proceed();
	}
}
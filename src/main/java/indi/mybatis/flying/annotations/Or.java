package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Or {

	/**
	 * 
	 * @since 0.9.3
	 * @return ConditionMapperAnnotation
	 */
	ConditionMapperAnnotation[] value();

}

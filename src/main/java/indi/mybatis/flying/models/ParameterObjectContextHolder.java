package indi.mybatis.flying.models;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class ParameterObjectContextHolder {

	private ParameterObjectContextHolder() {

	}

	private static final ThreadLocal<Object> contextHolder = new ThreadLocal<Object>() {
		@Override
		protected Object initialValue() {
			return null;
		}
	};

	public static void set(Object o) {
		contextHolder.set(o);
	}

	public static Object get() {
		return contextHolder.get();
	}
}

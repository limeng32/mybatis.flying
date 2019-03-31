package indi.mybatis.flying.models;

public class ParameterObjectContextHolder {

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
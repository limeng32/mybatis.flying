package indi.mybatis.flying.utils;

public class CookOriginalSql {
	private static final String FLYING = "flying";

	public static boolean hasFlyingFeature(String originalSql) {
		if (null != originalSql && originalSql.startsWith(FLYING)) {
			return true;
		}
		return false;
	}
}

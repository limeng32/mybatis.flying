package indi.mybatis.flying.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import indi.mybatis.flying.exception.AutoMapperExceptionEnum;
import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.statics.KeyGeneratorType;

public class CookOriginalSql {

	private static final String FLYING = "flying";

	private static final String FLYING_QUESTIONMARK = "flying?";

	private static Map<String, FlyingModel> flyingModelCache = new ConcurrentHashMap<>(128);

	private static final Log logger = LogFactory.getLog(CookOriginalSql.class);

	public static FlyingModel fetchFlyingFeature(String originalSql) {
		if (flyingModelCache.get(originalSql) != null) {
			return flyingModelCache.get(originalSql);
		}
		FlyingModel ret = new FlyingModel();
		String extension = null;
		if (null != originalSql && originalSql.startsWith(FLYING) && originalSql.indexOf(":") > -1) {
			String s1 = originalSql.substring(0, originalSql.indexOf(":"));
			if (FLYING.equals(s1) || FLYING_QUESTIONMARK.equals(s1)) {
				String s2 = originalSql.substring(originalSql.indexOf(":") + 1, originalSql.length());
				String actionTypeStr = null;
				if (s2.indexOf(":") > -1) {
					actionTypeStr = s2.substring(0, s2.indexOf(":"));
				} else {
					actionTypeStr = s2;
				}
				if (actionTypeStr.endsWith(")") && actionTypeStr.indexOf("(") != -1) {
					extension = actionTypeStr.substring(actionTypeStr.lastIndexOf("(") + 1, actionTypeStr.length() - 1);
					actionTypeStr = actionTypeStr.substring(0, actionTypeStr.lastIndexOf("("));
				}
				ActionType actionType = ActionType.valueOf(actionTypeStr);
				if (actionType != null) {
					ret.setHasFlyingFeature(true);
					ret.setActionType(actionType);
					if (s2.indexOf(":") > -1) {
						String s3 = s2.substring(s2.indexOf(":") + 1, s2.length());
						String ignoreTag = null;
						if (s3.indexOf(":") > -1) {
							ignoreTag = s3.substring(0, s3.indexOf(":"));
						} else {
							ignoreTag = s3;
						}
						ret.setIgnoreTag(ignoreTag);
					}
					if (ActionType.insert.equals(actionType) && extension != null) {
						KeyGeneratorType keyGenerationType = null;
						try {
							keyGenerationType = KeyGeneratorType.valueOf(extension);
						} catch (IllegalArgumentException e) {
							logger.error(new StringBuffer(AutoMapperExceptionEnum.wrongKeyGenerationType.description())
									.append(originalSql).toString());
						}
						ret.setKeyGeneratorType(keyGenerationType);
					}
					flyingModelCache.put(originalSql, ret);
					return ret;
				}
			}
		}
		ret.setHasFlyingFeature(false);
		flyingModelCache.put(originalSql, ret);
		return ret;
	}

}
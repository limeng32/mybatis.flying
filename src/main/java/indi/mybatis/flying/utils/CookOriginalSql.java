package indi.mybatis.flying.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import com.alibaba.fastjson.JSONObject;

import indi.mybatis.flying.exception.AutoMapperExceptionEnum;
import indi.mybatis.flying.handlers.MilliSecondKeyHandler;
import indi.mybatis.flying.handlers.SnowFlakeKeyHandler;
import indi.mybatis.flying.handlers.UuidKeyHandler;
import indi.mybatis.flying.handlers.UuidWithoutLineKeyHandler;
import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.statics.FlyingKeyword;
import indi.mybatis.flying.statics.KeyGeneratorType;
import indi.mybatis.flying.type.KeyHandler;

public class CookOriginalSql {

	private static final String FLYING = "flying";

	private static final String FLYING_LEFTBRACKET = "flying(";

	private static final String FLYING_QUESTIONMARK = "flying?";

	private static final String FLYING_QUESTIONMARK_LEFTBRACKET = "flying?(";

	private static Map<String, FlyingModel> flyingModelCache = new ConcurrentHashMap<>(128);

	private static Map<String, JSONObject> flyingModel2ndCache = new ConcurrentHashMap<>(128);

	private static final Log logger = LogFactory.getLog(CookOriginalSql.class);

	public static FlyingModel fetchFlyingFeatureNew(String originalSql, Configuration configuration,
			MappedStatement mappedStatement) {
		if (flyingModelCache.get(originalSql) != null) {
			return flyingModelCache.get(originalSql);
		}
		// 在CookOriginalSql中采用迭代的方式获取configuration中其它的元素的引用
		FlyingModel ret = new FlyingModel();
		if (null != originalSql && originalSql.startsWith(FLYING) && originalSql.indexOf(':') > -1) {
			String jsonStr = originalSql.substring(originalSql.indexOf(':') + 1, originalSql.length());
			try {
				JSONObject json = JSONObject.parseObject(jsonStr);
				buildFlyingModel(ret, json, originalSql, mappedStatement.getId(), true, null, null);
				dealInnerPropertiesIteration(mappedStatement.getId(), json, configuration,
						json.getJSONObject("properties"), ret);
				System.out.println("::::" + JSONObject.toJSONString(ret));
				flyingModelCache.put(originalSql, ret);
				return ret;
			} catch (Exception e) {
				// make sonar happy
				return fetchFlyingFeature(originalSql);
			}
		}
		ret.setHasFlyingFeature(false);
		flyingModelCache.put(originalSql, ret);
		return ret;
	}

	private static void buildFlyingModel(FlyingModel flyingModel, JSONObject json, String originalSql, String id,
			boolean b, JSONObject innerJson, String outerPrefix) {
		if (b) {
			ActionType actionType = ActionType.valueOf(json.getString(FlyingKeyword.ACTION.value()));
			flyingModel.setActionType(actionType);
			dealKeyHandler(actionType, json.getString(FlyingKeyword.KEY_GENERATOR.value()), originalSql, flyingModel);
		}
		flyingModel.setId(id);
		flyingModel.setHasFlyingFeature(true);
		if (innerJson != null) {
			flyingModel.setIgnoreTag(innerJson.getString(FlyingKeyword.IGNORE_TAG.value()));
			flyingModel.setUnstablePrefix(innerJson.getString(FlyingKeyword.PREFIX.value()));
			flyingModel.setDataSourceId(innerJson.getString(FlyingKeyword.DATA_SOURCE.value()));
			flyingModel.setConnectionCatalog(innerJson.getString(FlyingKeyword.CONNECTION_CATALOG.value()));
		}
		if (json.containsKey(FlyingKeyword.IGNORE_TAG.value())) {
			flyingModel.setIgnoreTag(json.getString(FlyingKeyword.IGNORE_TAG.value()));
		}
		if (json.containsKey(FlyingKeyword.PREFIX.value())) {
			flyingModel.setUnstablePrefix(json.getString(FlyingKeyword.PREFIX.value()));
		}
		if (json.containsKey(FlyingKeyword.DATA_SOURCE.value())) {
			flyingModel.setDataSourceId(json.getString(FlyingKeyword.DATA_SOURCE.value()));
		}
		if (json.containsKey(FlyingKeyword.CONNECTION_CATALOG.value())) {
			flyingModel.setConnectionCatalog(json.getString(FlyingKeyword.CONNECTION_CATALOG.value()));
		}
		flyingModel.setPrefix(outerPrefix == null ? (flyingModel.getUnstablePrefix())
				: (outerPrefix + flyingModel.getUnstablePrefix()));
	}

	private static JSONObject dealInnerPropertiesIteration(String id, JSONObject flyingJson,
			Configuration configuration, JSONObject threshold, FlyingModel flyingModel) {
		if (flyingModel2ndCache.get(id) != null) {
			return flyingModel2ndCache.get(id);
		}
		System.out.println("1::" + id);
		if (flyingJson == null) {
			flyingJson = new JSONObject();
		}
		if (threshold == null || threshold.isEmpty()) {
			return flyingJson;
		}
		for (Map.Entry<String, Object> e : threshold.getInnerMap().entrySet()) {
			System.out.println("a::" + e.getKey());
			System.out.println("b::" + e.getValue());
			JSONObject json = (JSONObject) e.getValue();
			if (json.containsKey(FlyingKeyword.FLYING.value())) {
				String flying = json.getString("flying");
				if (flying.indexOf('.') == -1 && id.indexOf('.') > -1) {
					flying = id.substring(0, id.lastIndexOf('.') + 1) + flying;
					System.out.println("c::" + flying);
				}
				String originalSql = configuration.getMappedStatement(flying).getBoundSql(null).getSql();
				String jsonStr = originalSql.substring(originalSql.indexOf(':') + 1, originalSql.length());
				JSONObject innerJson = JSONObject.parseObject(jsonStr);
				FlyingModel innerFlyingModel = new FlyingModel();
				buildFlyingModel(innerFlyingModel, json, originalSql, flying, false, innerJson,
						flyingModel.getPrefix());
				flyingModel.getProperties().put(e.getKey(), innerFlyingModel);
				dealInnerPropertiesIteration(flying, innerJson, configuration, innerJson.getJSONObject("properties"),
						innerFlyingModel);
			} else {
				FlyingModel innerFlyingModel = new FlyingModel();
				buildFlyingModel(innerFlyingModel, json, "", null, false, null, flyingModel.getPrefix());
				flyingModel.getProperties().put(e.getKey(), innerFlyingModel);
			}
		}
		return flyingJson;
	}

	public static FlyingModel fetchFlyingFeature(String originalSql) {
		if (flyingModelCache.get(originalSql) != null) {
			return flyingModelCache.get(originalSql);
		}
		FlyingModel ret = new FlyingModel();
		String extension = null;
		if (null != originalSql && originalSql.startsWith(FLYING) && originalSql.indexOf(':') > -1) {
			String dataSourceIdAndConnectionCatalog = null;
			String s1 = null;
			if ((originalSql.startsWith(FLYING_LEFTBRACKET) || originalSql.startsWith(FLYING_QUESTIONMARK_LEFTBRACKET))
					&& originalSql.indexOf(')') > 0) {
				String s0 = originalSql.substring(0, originalSql.indexOf(')') + 1);
				dataSourceIdAndConnectionCatalog = s0.substring(s0.indexOf('(') + 1, s0.lastIndexOf(')'));
				s1 = originalSql.substring(0, originalSql.indexOf(':', originalSql.indexOf(')')));
			} else {
				s1 = originalSql.substring(0, originalSql.indexOf(':'));
			}
			if (FLYING.equals(s1) || FLYING_QUESTIONMARK.equals(s1) || s1.startsWith(FLYING_LEFTBRACKET)
					|| originalSql.startsWith(FLYING_QUESTIONMARK_LEFTBRACKET)) {
				String s2 = null;
				if (s1.startsWith(FLYING_LEFTBRACKET) || originalSql.startsWith(FLYING_QUESTIONMARK_LEFTBRACKET)) {
					s2 = originalSql.substring(originalSql.indexOf(":", originalSql.indexOf(')')) + 1,
							originalSql.length());
				} else {
					s2 = originalSql.substring(originalSql.indexOf(':') + 1, originalSql.length());
				}
				String actionTypeStr = null;
				if (s2.indexOf(':') > -1) {
					actionTypeStr = s2.substring(0, s2.indexOf(':'));
				} else {
					actionTypeStr = s2;
				}
				if (actionTypeStr.endsWith(")") && actionTypeStr.indexOf('(') != -1) {
					extension = actionTypeStr.substring(actionTypeStr.lastIndexOf('(') + 1, actionTypeStr.length() - 1);
					actionTypeStr = actionTypeStr.substring(0, actionTypeStr.lastIndexOf('('));
				}
				ActionType actionType = ActionType.valueOf(actionTypeStr);
				if (actionType != null) {
					ret.setHasFlyingFeature(true);
					if (dataSourceIdAndConnectionCatalog != null
							&& dataSourceIdAndConnectionCatalog.indexOf(':') != -1) {
						ret.setDataSourceId(dataSourceIdAndConnectionCatalog.substring(0,
								dataSourceIdAndConnectionCatalog.indexOf(':')));
						ret.setConnectionCatalog(dataSourceIdAndConnectionCatalog.substring(
								dataSourceIdAndConnectionCatalog.indexOf(':') + 1,
								dataSourceIdAndConnectionCatalog.length()));
					}
					ret.setActionType(actionType);
					if (s2.indexOf(':') > -1) {
						String s3 = s2.substring(s2.indexOf(':') + 1, s2.length());
						String ignoreTag = null;
						if (s3.indexOf(':') > -1) {
							ignoreTag = s3.substring(0, s3.indexOf(':'));
						} else {
							ignoreTag = s3;
						}
						ret.setIgnoreTag(ignoreTag);
					}
					dealKeyHandler(actionType, extension, originalSql, ret);
					flyingModelCache.put(originalSql, ret);
					return ret;
				}
			}
		}
		ret.setHasFlyingFeature(false);
		flyingModelCache.put(originalSql, ret);
		return ret;
	}

	private static void dealKeyHandler(ActionType actionType, String extension, String originalSql, FlyingModel ret) {
		if (ActionType.insert.equals(actionType) && extension != null) {
			KeyGeneratorType keyGeneratorType = null;
			if (extension.indexOf(".") == -1) {
				try {
					keyGeneratorType = KeyGeneratorType.valueOf(extension);
				} catch (IllegalArgumentException e) {
					logger.error(new StringBuffer(AutoMapperExceptionEnum.wrongKeyGeneratorType.description())
							.append(originalSql).append(" because of ").append(e).toString());
				}
				ret.setKeyGeneratorType(keyGeneratorType);
				if (keyGeneratorType != null) {
					KeyHandler keyHandler;
					switch (keyGeneratorType) {
					case uuid:
						keyHandler = UuidKeyHandler.getInstance();
						break;
					case uuid_no_line:
						keyHandler = UuidWithoutLineKeyHandler.getInstance();
						break;
					case millisecond:
						keyHandler = MilliSecondKeyHandler.getInstance();
						break;
					case snowflake:
						keyHandler = SnowFlakeKeyHandler.getInstance();
						break;
					default:
						keyHandler = null;
						break;
					}
					ret.setKeyHandler(keyHandler);
				}
			} else {
				try {
					@SuppressWarnings("unchecked")
					Class<? extends KeyHandler> clazz = (Class<? extends KeyHandler>) Class.forName(extension);
					ret.setKeyGeneratorType(KeyGeneratorType.custom);
					ret.setKeyHandler(clazz.newInstance());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					logger.error(new StringBuffer(AutoMapperExceptionEnum.wrongCustomKeyGenerator.description())
							.append(originalSql).append(" because of ").append(e).toString());
				}
			}
		}
	}
}
package indi.mybatis.flying.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

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

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class FlyingManager {

	private FlyingManager() {

	}

	private static final Map<String, FlyingModel> flyingModelCache = new ConcurrentHashMap<>(128);

	private static final Map<String, JSONObject> flyingModel2ndCache = new ConcurrentHashMap<>(128);

	private static final Log logger = LogFactory.getLog(FlyingManager.class);

	public static FlyingModel getFlyingModelFromCache(String key) {
		return flyingModelCache.get(key);
	}

	public static FlyingModel fetchFlyingFeatureNew(String originalSql, Configuration configuration,
			MappedStatement mappedStatement) {
		String id = mappedStatement.getId();
		if (flyingModelCache.get(id) != null) {
			return flyingModelCache.get(id);
		}
		// 在CookOriginalSql中采用迭代的方式获取configuration中其它的元素的引用
		FlyingModel ret = new FlyingModel();
		try {
			JSONObject json = new JSONObject(originalSql);
			buildFlyingModel(ret, json, originalSql, id, true, null, null);
			dealInnerPropertiesIteration(id, json, configuration, ret);
			flyingModelCache.put(id, ret);
			return ret;
		} catch (Exception e) {
			// make sonar happy
			return fetchFlyingFeature(originalSql, id);
		}
	}

	public static String getStringSafe(JSONObject json, String name) {
		try {
			return json.getString(name);
		} catch (Exception e) {
			return null;
		}
	}

	public static JSONArray getJSONArraySafe(JSONObject json, String name) {
		try {
			return json.getJSONArray(name);
		} catch (Exception e) {
			return null;
		}
	}

	public static JSONObject getJSONObjectSafe(JSONObject json, String name) {
		try {
			JSONObject temp = json.getJSONObject(name);
			return temp;
		} catch (Exception e) {
			return null;
		}
	}

	private static void buildFlyingModel(FlyingModel flyingModel, JSONObject json, String originalSql, String id,
			boolean b, JSONObject innerJson, String outerPrefix) {
		if (b) {
			String temp = getStringSafe(json, FlyingKeyword.ACTION);
			if (temp.endsWith("?")) {
				temp = temp.substring(0, temp.length() - 1);
			}
			ActionType actionType = ActionType.forValue(temp);
			flyingModel.setActionType(actionType);
			dealKeyHandler(actionType, getStringSafe(json, FlyingKeyword.KEY_GENERATOR), originalSql, flyingModel);
		}
		flyingModel.setId(id);
		flyingModel.setHasFlyingFeature(true);
		if (innerJson == null) {
			flyingModel.setIgnoreTag(getStringSafe(json, FlyingKeyword.IGNORE));
			flyingModel.setWhiteListTag(getStringSafe(json, FlyingKeyword.WHITE_LIST));
			flyingModel.setIndex(getStringSafe(json, FlyingKeyword.INDEX));
		} else {
			// inner json need ignore tag and white list tag
			flyingModel.setIgnoreTag(getStringSafe(innerJson, FlyingKeyword.IGNORE));
			flyingModel.setWhiteListTag(getStringSafe(innerJson, FlyingKeyword.WHITE_LIST));
			// but not need index
		}
		flyingModel.setUnstablePrefix(getStringSafe(json, FlyingKeyword.PREFIX));
		flyingModel.setPrefix(outerPrefix == null ? (flyingModel.getUnstablePrefix())
				: (outerPrefix + flyingModel.getUnstablePrefix()));
		// 下一版特性：
//		if (getJSONObjectSafe(json, "aggregate") != null) {
//			Map<String, Object> temp = getJSONObjectSafe(json, "aggregate").toMap();
//			Map<String, Set<AggregateModel>> temp2 = new HashMap<>();
//			for (Map.Entry<String, Object> e : temp.entrySet()) {
//				if (e.getValue() != null) {
//					Map<String, Object> m = (HashMap<String, Object>) e.getValue();
//					AggregateModel am = new AggregateModel();
//					if (temp2.containsKey(m.get("column"))) {
//						am.setAlias(e.getKey());
//						am.setColumn(m.get("column").toString());
//						am.setFunction(AggregateFunction.forValue(m.get("function").toString()));
//						temp2.get(am.getColumn()).add(am);
//					} else {
//						Set<AggregateModel> set = new LinkedHashSet<>();
//						am.setAlias(e.getKey());
//						am.setColumn(m.get("column").toString());
//						am.setFunction(AggregateFunction.forValue(m.get("function").toString()));
//						set.add(am);
//						temp2.put(am.getColumn(), set);
//					}
//				}
//			}
//			flyingModel.getAggregate().putAll(temp2);
//		}
//		if (getJSONArraySafe(json, "groupBy") != null) {
//			List<Object> m = getJSONArraySafe(json, "groupBy").toList();
//			for (Object o : m) {
//				if (o != null) {
//					flyingModel.getGroupBy().add(o.toString());
//				}
//			}
//		}
	}

	private static JSONObject dealInnerPropertiesIteration(String id, JSONObject flyingJson,
			Configuration configuration, FlyingModel flyingModel) {
		if (flyingModel2ndCache.get(id) != null) {
			return flyingModel2ndCache.get(id);
		}
		JSONObject threshold = getJSONObjectSafe(flyingJson, "properties");
		if (threshold == null || threshold.isEmpty()) {
			return flyingJson;
		}
		for (Map.Entry<String, Object> e : threshold.toMap().entrySet()) {
			JSONObject json = (JSONObject) JSONObject.wrap(e.getValue());
			String innerId = getStringSafe(json, FlyingKeyword.ID);
			if (innerId != null) {
				if (innerId.indexOf('.') == -1 && id.indexOf('.') > -1) {
					innerId = new StringBuilder(id.substring(0, id.lastIndexOf('.') + 1)).append(innerId).toString();
				}
				String originalSql = configuration.getMappedStatement(innerId).getBoundSql(null).getSql();
				JSONObject innerJson = new JSONObject(originalSql);
				FlyingModel innerFlyingModel = new FlyingModel();
				buildFlyingModel(innerFlyingModel, json, originalSql, innerId, false, innerJson,
						flyingModel.getPrefix());
				dealInnerPropertiesIteration(innerId, innerJson, configuration, innerFlyingModel);
				flyingModel.getProperties().put(e.getKey(), innerFlyingModel);
			} else {
				FlyingModel innerFlyingModel = new FlyingModel();
				buildFlyingModel(innerFlyingModel, json, "", null, false, null, flyingModel.getPrefix());
				flyingModel.getProperties().put(e.getKey(), innerFlyingModel);
			}
		}
		return flyingJson;
	}

	public static FlyingModel fetchFlyingFeature(String originalSql, String id) {
		if (flyingModelCache.get(id) != null) {
			return flyingModelCache.get(id);
		}
		FlyingModel ret = new FlyingModel();
		String extension = null;
		if (null != originalSql && originalSql.startsWith(FlyingKeyword.FLYING) && originalSql.indexOf(':') > -1) {
			String s1 = null;
			if (originalSql.startsWith(FlyingKeyword.FLYING_LEFTBRACKET)
					|| originalSql.startsWith(FlyingKeyword.FLYING_QUESTIONMARK_LEFTBRACKET)) {
				s1 = originalSql.substring(0, originalSql.indexOf(':', originalSql.indexOf(')')));
			} else {
				s1 = originalSql.substring(0, originalSql.indexOf(':'));
			}
			if (FlyingKeyword.FLYING.equals(s1) || FlyingKeyword.FLYING_QUESTIONMARK.equals(s1)
					|| s1.startsWith(FlyingKeyword.FLYING_LEFTBRACKET)
					|| originalSql.startsWith(FlyingKeyword.FLYING_QUESTIONMARK_LEFTBRACKET)) {
				String s2 = null;
				if (s1.startsWith(FlyingKeyword.FLYING_LEFTBRACKET)
						|| originalSql.startsWith(FlyingKeyword.FLYING_QUESTIONMARK_LEFTBRACKET)) {
					s2 = originalSql.substring(originalSql.indexOf(':', originalSql.indexOf(')')) + 1,
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
				ActionType actionType = ActionType.forValue(actionTypeStr);
				if (actionType != null) {
					ret.setHasFlyingFeature(true);
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
					flyingModelCache.put(id, ret);
					return ret;
				}
			}
		}
		ret.setHasFlyingFeature(false);
		flyingModelCache.put(id, ret);
		return ret;
	}

	public static FlyingModel fetchFlyingFeature(String originalSql) {
		return fetchFlyingFeature(originalSql, originalSql);
	}

	private static void dealKeyHandler(ActionType actionType, String extension, String originalSql, FlyingModel ret) {
		if ((ActionType.INSERT.equals(actionType) || ActionType.INSERT_BATCH.equals(actionType)) && extension != null) {
			KeyGeneratorType keyGeneratorType = null;
			if (extension.indexOf('.') == -1) {
				try {
					keyGeneratorType = KeyGeneratorType.forValue(extension);
				} catch (IllegalArgumentException e) {
					logger.error(new StringBuilder(AutoMapperExceptionEnum.WRONG_KEY_GENERATOR_TYPE.description())
							.append(originalSql).append(" because of ").append(e).toString());
				}
				ret.setKeyGeneratorType(keyGeneratorType);
				if (keyGeneratorType != null) {
					KeyHandler keyHandler;
					switch (keyGeneratorType) {
					case UUID:
						keyHandler = UuidKeyHandler.getInstance();
						break;
					case UUID_NO_LINE:
						keyHandler = UuidWithoutLineKeyHandler.getInstance();
						break;
					case MILLISECOND:
						keyHandler = MilliSecondKeyHandler.getInstance();
						break;
					case SNOWFLAKE:
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
					ret.setKeyGeneratorType(KeyGeneratorType.CUSTOM);
					ret.setKeyHandler(clazz.newInstance());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					logger.error(new StringBuilder(AutoMapperExceptionEnum.WRONG_CUSTOM_KEY_GENERATOR.description())
							.append(originalSql).append(" because of ").append(e).toString());
				}
			}
		}
	}
}
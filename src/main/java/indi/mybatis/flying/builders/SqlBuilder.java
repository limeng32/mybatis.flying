package indi.mybatis.flying.builders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.PropertyUtils;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.QueryMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.models.ConditionMapper;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.FieldMapper;
import indi.mybatis.flying.models.Mapperable;
import indi.mybatis.flying.models.QueryMapper;
import indi.mybatis.flying.models.TableMapper;
import indi.mybatis.flying.models.TableName;
import indi.mybatis.flying.statics.ConditionType;
import indi.mybatis.flying.statics.HandlerPaths;
import indi.mybatis.flying.utils.ReflectHelper;

/**
 * 通过注解生成sql
 * 
 * @author david, limeng32
 * 
 */
public class SqlBuilder {
	/** 缓存TableMapper */
	private static Map<Class<?>, TableMapper> tableMapperCache = new ConcurrentHashMap<Class<?>, TableMapper>(128);
	/** 缓存QueryMapper */
	private static Map<Class<?>, QueryMapper> queryMapperCache = new ConcurrentHashMap<Class<?>, QueryMapper>(128);

	/**
	 * 由传入的dto对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
	 * 
	 * @param dtoClass
	 * @return TableMapper
	 */
	private static TableMapper buildTableMapper(Class<?> dtoClass) {

		Map<String, FieldMapper> fieldMapperCache = null;
		Field[] fields = null;

		FieldMapperAnnotation fieldMapperAnnotation = null;
		FieldMapper fieldMapper = null;
		TableMapper tableMapper = null;
		tableMapper = tableMapperCache.get(dtoClass);
		if (tableMapper != null) {
			return tableMapper;
		}
		tableMapper = new TableMapper();
		List<FieldMapper> uniqueKeyList = new ArrayList<FieldMapper>();
		List<FieldMapper> opVersionLockList = new ArrayList<FieldMapper>();
		Annotation[] classAnnotations = dtoClass.getDeclaredAnnotations();
		for (Annotation an : classAnnotations) {
			if (an instanceof TableMapperAnnotation) {
				tableMapper.setTableMapperAnnotation(an);
			}
		}
		fields = dtoClass.getDeclaredFields();
		fieldMapperCache = new HashMap<String, FieldMapper>();
		Annotation[] fieldAnnotations = null;
		for (Field field : fields) {
			fieldAnnotations = field.getDeclaredAnnotations();
			if (fieldAnnotations.length == 0) {
				continue;
			}
			for (Annotation an : fieldAnnotations) {
				if (an instanceof FieldMapperAnnotation) {
					fieldMapperAnnotation = (FieldMapperAnnotation) an;
					fieldMapper = new FieldMapper();
					fieldMapper.setFieldName(field.getName());
					fieldMapper.setDbFieldName(fieldMapperAnnotation.dbFieldName());
					fieldMapper.setJdbcType(fieldMapperAnnotation.jdbcType());
					fieldMapper.setUniqueKey(fieldMapperAnnotation.isUniqueKey());
					switch (fieldMapperAnnotation.opLockType()) {
					case Version:
						fieldMapper.setOpVersionLock(true);
						break;
					default:
						break;
					}
					fieldMapper.setIgnoredSelect(fieldMapperAnnotation.ignoredSelect());
					if (fieldMapperAnnotation.isUniqueKey()) {
						uniqueKeyList.add(fieldMapper);
					}
					if ("".equals(fieldMapperAnnotation.dbAssociationUniqueKey())) {
					} else {
						fieldMapper.setDbAssociationUniqueKey(fieldMapperAnnotation.dbAssociationUniqueKey());
						fieldMapper.setForeignKey(true);
					}
					if (fieldMapper.isForeignKey()) {
						if (!tableMapperCache.containsKey(field.getType())) {
							buildTableMapper(field.getType());
						}
						TableMapper tm = tableMapperCache.get(field.getType());
						String foreignFieldName = getFieldMapperByDbFieldName(tm.getFieldMapperCache(),
								fieldMapperAnnotation.dbAssociationUniqueKey()).getFieldName();
						fieldMapper.setForeignFieldName(foreignFieldName);
					}
					if (fieldMapper.isOpVersionLock()) {
						opVersionLockList.add(fieldMapper);
					}
					fieldMapperCache.put(field.getName(), fieldMapper);
				}
			}
		}
		tableMapper.setFieldMapperCache(fieldMapperCache);
		tableMapper.setUniqueKeyNames(uniqueKeyList.toArray(new FieldMapper[uniqueKeyList.size()]));
		tableMapper.setOpVersionLocks(opVersionLockList.toArray(new FieldMapper[opVersionLockList.size()]));
		tableMapperCache.put(dtoClass, tableMapper);
		return tableMapper;
	}

	/* 从newFieldMapperCache中获取已知dbFieldName的FieldMapper */
	private static Mapperable getFieldMapperByDbFieldName(Map<String, FieldMapper> newFieldMapperCache,
			String dbFieldName) {
		for (Mapperable mapper : newFieldMapperCache.values()) {
			if (dbFieldName.equalsIgnoreCase(mapper.getDbFieldName())) {
				return mapper;
			}
		}
		return null;
	}

	/**
	 * 由传入的dto对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
	 * 
	 * @param dtoClass
	 * @param pojoClass
	 * @return QueryMapper
	 */
	private static QueryMapper buildQueryMapper(Class<?> dtoClass, Class<?> pojoClass) {
		Map<String, ConditionMapper> conditionMapperCache = null;
		Field[] fields = null;

		ConditionMapperAnnotation conditionMapperAnnotation = null;
		ConditionMapper conditionMapper = null;
		QueryMapper queryMapper = null;
		queryMapper = queryMapperCache.get(dtoClass);
		if (queryMapper != null) {
			return queryMapper;
		}
		queryMapper = new QueryMapper();
		fields = dtoClass.getDeclaredFields();
		conditionMapperCache = new HashMap<>();
		Annotation[] conditionAnnotations = null;

		for (Field field : fields) {
			conditionAnnotations = field.getDeclaredAnnotations();
			if (conditionAnnotations.length == 0) {
				continue;
			}
			for (Annotation an : conditionAnnotations) {
				if (an instanceof ConditionMapperAnnotation) {
					conditionMapperAnnotation = (ConditionMapperAnnotation) an;
					conditionMapper = new ConditionMapper();
					conditionMapper.setFieldName(field.getName());
					conditionMapper.setDbFieldName(conditionMapperAnnotation.dbFieldName());
					conditionMapper.setConditionType(conditionMapperAnnotation.conditionType());
					for (Field pojoField : pojoClass.getDeclaredFields()) {
						for (Annotation oan : pojoField.getDeclaredAnnotations()) {
							if (oan instanceof FieldMapperAnnotation && ((FieldMapperAnnotation) oan).dbFieldName()
									.equalsIgnoreCase(conditionMapperAnnotation.dbFieldName())) {
								FieldMapperAnnotation fieldMapperAnnotation = (FieldMapperAnnotation) oan;
								conditionMapper.setJdbcType(fieldMapperAnnotation.jdbcType());
								if ("".equals(fieldMapperAnnotation.dbAssociationUniqueKey())) {
								} else {
									conditionMapper
											.setDbAssociationUniqueKey(fieldMapperAnnotation.dbAssociationUniqueKey());
									conditionMapper.setForeignKey(true);
								}
								if (conditionMapper.isForeignKey()
										&& (!ConditionType.NullOrNot.equals(conditionMapper.getConditionType()))) {
									if (!tableMapperCache.containsKey(pojoField.getType())) {
										buildTableMapper(pojoField.getType());
									}
									TableMapper tm = tableMapperCache.get(pojoField.getType());
									String foreignFieldName = tm.getFieldMapperCache()
											.get(fieldMapperAnnotation.dbAssociationUniqueKey()).getFieldName();
									conditionMapper.setForeignFieldName(foreignFieldName);
								}
							}
						}
					}
					conditionMapperCache.put(field.getName(), conditionMapper);
				}
			}
		}
		queryMapper.setConditionMapperCache(conditionMapperCache);
		queryMapperCache.put(dtoClass, queryMapper);
		return queryMapper;
	}

	/**
	 * 查找类clazz及其所有父类，直到找到一个拥有TableMapperAnnotation注解的类为止，
	 * 然后返回这个拥有TableMapperAnnotation注解的类
	 * 
	 * @param object
	 * @return
	 */
	private static Class<?> getTableMappedClass(Class<?> clazz) {
		Class<?> c = clazz;
		while (!interview(c) && !(c.equals(Object.class))) {
			c = c.getSuperclass();
		}
		if (c.equals(Object.class)) {
			throw new RuntimeException(
					"Class " + clazz.getName() + " and all its parents has no 'TableMapperAnnotation', "
							+ "which has the database table information," + " I can't build 'TableMapper' for it.");
		}
		return c;
	}

	/**
	 * 判断clazz是否符合条件，即是否存在TableMapperAnnotation类型的标注。如存在返回true，否则返回false。
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean interview(Class<?> clazz) {
		Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
		if (classAnnotations.length > 0) {
			for (Annotation an : classAnnotations) {
				if (an instanceof TableMapperAnnotation) {
					return true;
				}
			}
		}
		return false;
	}

	private static void dealConditionLike(StringBuffer whereSql, ConditionMapper conditionMapper, ConditionType type,
			TableName tableName, String fieldNamePrefix) {
		handleWhereSql(whereSql, conditionMapper, tableName, fieldNamePrefix);
		whereSql.append(" like #{");
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(".");
		}
		if (conditionMapper.isForeignKey()) {
			whereSql.append(conditionMapper.getFieldName()).append(".").append(conditionMapper.getForeignFieldName());
		} else {
			whereSql.append(conditionMapper.getFieldName());
		}
		whereSql.append(",").append("jdbcType=").append(conditionMapper.getJdbcType().toString())
				.append(",typeHandler=");
		switch (type) {
		case Like:
			whereSql.append(HandlerPaths.CONDITION_LIKE_HANDLER_PATH);
			break;
		case HeadLike:
			whereSql.append(HandlerPaths.CONDITION_HEAD_LIKE_HANDLER_PATH);
			break;
		case TailLike:
			whereSql.append(HandlerPaths.CONDITION_TAIL_LIKE_HANDLER_PATH);
			break;
		default:
			throw new RuntimeException("Sorry,I refuse to build sql for an ambiguous condition!");
		}
		whereSql.append("} and ");
	}

	private static void dealConditionInOrNot(Object value, StringBuffer whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix) {
		List<?> multiConditionC = (List<?>) value;
		if (multiConditionC.size() > 0) {
			StringBuffer tempWhereSql = new StringBuffer();
			handleWhereSql(tempWhereSql, conditionMapper, tableName, fieldNamePrefix);
			switch (type) {
			case In:
				break;
			case NotIn:
				tempWhereSql.append(" not");
				break;
			default:
				throw new RuntimeException("Sorry,I refuse to build sql for an ambiguous condition!");
			}
			tempWhereSql.append(" in(");
			int j = -1;
			boolean allNull = true;
			for (Object s : multiConditionC) {
				j++;
				if (s != null) {
					if (allNull) {
						allNull = false;
					}
					tempWhereSql.append("#{");
					if (fieldNamePrefix != null) {
						tempWhereSql.append(fieldNamePrefix).append(".");
					}
					if (conditionMapper.isForeignKey()) {
						tempWhereSql.append(conditionMapper.getFieldName()).append(".")
								.append(conditionMapper.getForeignFieldName()).append("[").append(j).append("]");
					} else {
						tempWhereSql.append(conditionMapper.getFieldName()).append("[").append(j).append("]");
					}
					tempWhereSql.append(",").append("jdbcType=").append(conditionMapper.getJdbcType().toString())
							.append("},");
				}
			}
			if (!allNull) {
				tempWhereSql.delete(tempWhereSql.lastIndexOf(","), tempWhereSql.lastIndexOf(",") + 1);
				tempWhereSql.append(") and ");
				whereSql.append(tempWhereSql);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void dealConditionMultiLike(Object value, StringBuffer whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix) {
		List<String> multiConditionList = (List<String>) value;
		if (multiConditionList.size() > 0) {
			StringBuffer tempWhereSql = new StringBuffer();
			tempWhereSql.append(" (");
			int j = -1;
			boolean allNull = true;
			for (String s : multiConditionList) {
				j++;
				if (s != null) {
					if (allNull) {
						allNull = false;
					}
					handleWhereSql(tempWhereSql, conditionMapper, tableName, fieldNamePrefix);
					tempWhereSql.append(" like #{");
					if (fieldNamePrefix != null) {
						tempWhereSql.append(fieldNamePrefix).append(".");
					}
					if (conditionMapper.isForeignKey()) {
						tempWhereSql.append(conditionMapper.getFieldName()).append(".")
								.append(conditionMapper.getForeignFieldName()).append("[").append(j).append("]");
					} else {
						tempWhereSql.append(conditionMapper.getFieldName()).append("[").append(j).append("]");
					}
					tempWhereSql.append(",").append("jdbcType=").append(conditionMapper.getJdbcType().toString())
							.append(",typeHandler=");
					switch (type) {
					case MultiLikeAND:
						tempWhereSql.append("ConditionLikeHandler");
						tempWhereSql.append("} and ");
						break;
					case MultiLikeOR:
						tempWhereSql.append("ConditionLikeHandler");
						tempWhereSql.append("} or ");
						break;
					default:
						throw new RuntimeException("Sorry,I refuse to build sql for an ambiguous condition!");
					}
				}
			}
			if (!allNull) {
				switch (type) {
				case MultiLikeAND:
					tempWhereSql.delete(tempWhereSql.lastIndexOf(" and "), tempWhereSql.lastIndexOf(" and ") + 5);
					break;
				case MultiLikeOR:
					tempWhereSql.delete(tempWhereSql.lastIndexOf(" or "), tempWhereSql.lastIndexOf(" or ") + 4);
					break;
				default:
				}
				tempWhereSql.append(") and ");
				whereSql.append(tempWhereSql);
			}
		}
	}

	private static void dealConditionEqual(Object object, StringBuffer whereSql, Mapperable mapper, TableName tableName,
			String fieldNamePrefix) {
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(" = #{");
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(".");
		}
		if (mapper.isForeignKey()) {
			whereSql.append(mapper.getFieldName()).append(".").append(mapper.getForeignFieldName());
		} else {
			whereSql.append(mapper.getFieldName());
		}
		if (mapper.getJdbcType() != null) {
			whereSql.append(",").append("jdbcType=").append(mapper.getJdbcType().toString());
		}
		whereSql.append("} and ");
	}

	private static void dealConditionNotEqual(StringBuffer whereSql, Mapperable mapper, ConditionType type,
			TableName tableName, String fieldNamePrefix) {
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		switch (type) {
		case GreaterThan:
			whereSql.append(" > ");
			break;
		case GreaterOrEqual:
			whereSql.append(" >= ");
			break;
		case LessThan:
			whereSql.append(" < ");
			break;
		case LessOrEqual:
			whereSql.append(" <= ");
			break;
		case NotEqual:
			whereSql.append(" <> ");
			break;
		default:
			throw new RuntimeException("Sorry,I refuse to build sql for an ambiguous condition!");
		}
		whereSql.append("#{");
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(".");
		}
		if (mapper.isForeignKey()) {
			whereSql.append(mapper.getFieldName()).append(".").append(mapper.getForeignFieldName());
		} else {
			whereSql.append(mapper.getFieldName());
		}
		if (mapper.getJdbcType() != null) {
			whereSql.append(",").append("jdbcType=").append(mapper.getJdbcType().toString());
		}
		whereSql.append("} and ");
	}

	private static void dealConditionNullOrNot(Object value, StringBuffer whereSql, Mapperable mapper,
			TableName tableName, String fieldNamePrefix) {
		Boolean isNull = (Boolean) value;
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(" is");
		if (!isNull) {
			whereSql.append(" not");
		}
		whereSql.append(" null").append(" and ");
	}

	private static void handleWhereSql(StringBuffer whereSql, Mapperable mapper, TableName tableName,
			String fieldNamePrefix) {
		if (tableName != null) {
			whereSql.append(tableName.sqlWhere());
		}
		whereSql.append(mapper.getDbFieldName());
	}

	/**
	 * 由传入的对象生成insert sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildInsertSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();

		String tableName = tma.tableName();
		StringBuffer tableSql = new StringBuffer();
		StringBuffer valueSql = new StringBuffer();

		tableSql.append("insert into ").append(tableName).append("(");
		valueSql.append("values(");

		boolean allFieldNull = true;
		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if ((value == null && !fieldMapper.isOpVersionLock())) {
				continue;
			} else if (((FieldMapper) fieldMapper).isOpVersionLock()) {
				value = 0;
				ReflectHelper.setValueByFieldName(object, fieldMapper.getFieldName(), value);
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(",");
			valueSql.append("#{");
			if (fieldMapper.isForeignKey()) {
				valueSql.append(fieldMapper.getFieldName()).append(".").append(fieldMapper.getForeignFieldName());
			} else {
				valueSql.append(fieldMapper.getFieldName());
			}
			valueSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString()).append("},");
		}
		if (allFieldNull) {
			throw new RuntimeException("Are you joking? Object " + object.getClass().getName()
					+ "'s all fields are null, how can i build sql for it?!");
		}

		tableSql.delete(tableSql.lastIndexOf(","), tableSql.lastIndexOf(",") + 1);
		valueSql.delete(valueSql.lastIndexOf(","), valueSql.lastIndexOf(",") + 1);
		return tableSql.append(") ").append(valueSql).append(")").toString();
	}

	/**
	 * 由传入的对象生成update sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildUpdateSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}

		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(" where ");

		tableSql.append("update ").append(tableName).append(" set ");

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append("=#{");
			if (fieldMapper.isForeignKey()) {
				tableSql.append(fieldMapper.getFieldName()).append(".").append(fieldMapper.getForeignFieldName());
			} else {
				tableSql.append(fieldMapper.getFieldName());
			}
			tableSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
			tableSql.append("}");
			if (fieldMapper.isOpVersionLock()) {
				tableSql.append("+1");
			}
			tableSql.append(",");
		}
		if (allFieldNull) {
			throw new RuntimeException("Are you joking? Object " + object.getClass().getName()
					+ "'s all fields are null, how can i build sql for it?!");
		}

		tableSql.delete(tableSql.lastIndexOf(","), tableSql.lastIndexOf(",") + 1);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new RuntimeException(
						"Unique key '" + fieldMapper.getDbFieldName() + "' can't be null, build update sql failed!");
			}
			whereSql.append("=#{").append(fieldMapper.getFieldName()).append(",").append("jdbcType=")
					.append(fieldMapper.getJdbcType().toString()).append("} and ");
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			whereSql.append(f.getDbFieldName()).append("=#{").append(f.getFieldName()).append("} and ");
		}
		whereSql.delete(whereSql.lastIndexOf("and"), whereSql.lastIndexOf("and") + 3);
		return tableSql.append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成update持久态对象的 sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildUpdatePersistentSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}

		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(" where ");

		tableSql.append("update ").append(tableName).append(" set ");

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append("=#{");
			if (fieldMapper.isForeignKey()) {
				tableSql.append(fieldMapper.getFieldName()).append(".").append(fieldMapper.getForeignFieldName());
			} else {
				tableSql.append(fieldMapper.getFieldName());
			}
			tableSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString()).append("}");
			if (fieldMapper.isOpVersionLock()) {
				tableSql.append("+1");
			}
			tableSql.append(",");
		}
		if (allFieldNull) {
			throw new RuntimeException("Are you joking? Object " + object.getClass().getName()
					+ "'s all fields are null, how can i build sql for it?!");
		}

		tableSql.delete(tableSql.lastIndexOf(","), tableSql.lastIndexOf(",") + 1);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new RuntimeException("Unique key '" + fieldMapper.getDbFieldName()
						+ "' can't be null, build updatePersistent sql failed!");
			}
			whereSql.append("=#{").append(fieldMapper.getFieldName()).append(",jdbcType=")
					.append(fieldMapper.getJdbcType().toString()).append("} and ");
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			whereSql.append(f.getDbFieldName()).append("=#{").append(f.getFieldName()).append("} and ");
		}
		whereSql.delete(whereSql.lastIndexOf("and"), whereSql.lastIndexOf("and") + 3);
		return tableSql.append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成delete sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildDeleteSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer sql = new StringBuffer();

		sql.append("delete from ").append(tableName).append(" where ");
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			sql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new RuntimeException(
						"Unique key '" + fieldMapper.getDbFieldName() + "' can't be null, build delete sql failed!");
			}
			sql.append("=#{").append(fieldMapper.getFieldName()).append(",").append("jdbcType=")
					.append(fieldMapper.getJdbcType().toString()).append("} and ");
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			sql.append(f.getDbFieldName()).append("=#{").append(f.getFieldName()).append("} and ");
		}
		sql.delete(sql.lastIndexOf("and"), sql.lastIndexOf("and") + 3);
		return sql.toString();
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param clazz
	 * @return sql
	 * @throws Exception
	 */
	public static String buildSelectSql(Class<?> clazz) throws Exception {
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(clazz));
		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer selectSql = new StringBuffer("select ");

		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			if (!fieldMapper.isIgnoredSelect()) {
				selectSql.append(fieldMapper.getDbFieldName()).append(",");
			}
		}

		if (selectSql.indexOf(",") > -1) {
			selectSql.delete(selectSql.lastIndexOf(","), selectSql.lastIndexOf(",") + 1);
		}

		selectSql.append(" from ").append(tableName);

		StringBuffer whereSql = new StringBuffer(" where ");
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			whereSql.append("=#{").append(fieldMapper.getFieldName()).append(",").append("jdbcType=")
					.append(fieldMapper.getJdbcType().toString()).append("} and ");
		}
		whereSql.delete(whereSql.lastIndexOf("and"), whereSql.lastIndexOf("and") + 3);
		return selectSql.append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildSelectAllSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}
		StringBuffer selectSql = new StringBuffer("select ");
		StringBuffer fromSql = new StringBuffer(" from ");
		StringBuffer whereSql = new StringBuffer(" where ");
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object, selectSql, fromSql, whereSql, null, null, null, ai);

		if (selectSql.indexOf(",") > -1) {
			selectSql.delete(selectSql.lastIndexOf(","), selectSql.lastIndexOf(",") + 1);
		}
		if (" where ".equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf("and") > -1) {
			whereSql.delete(whereSql.lastIndexOf("and"), whereSql.lastIndexOf("and") + 3);
		}
		return selectSql.append(fromSql).append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param clazz
	 * @return sql
	 * @throws Exception
	 */
	public static String buildSelectOneSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}
		if (object instanceof Conditionable) {
			((Conditionable) object).setLimiter(null);
		}
		StringBuffer selectSql = new StringBuffer("select ");
		StringBuffer fromSql = new StringBuffer(" from ");
		StringBuffer whereSql = new StringBuffer(" where ");
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object, selectSql, fromSql, whereSql, null, null, null, ai);

		if (selectSql.indexOf(",") > -1) {
			selectSql.delete(selectSql.lastIndexOf(","), selectSql.lastIndexOf(",") + 1);
		}
		if (" where ".equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf("and") > -1) {
			whereSql.delete(whereSql.lastIndexOf("and"), whereSql.lastIndexOf("and") + 3);
		}
		return selectSql.append(fromSql).append(whereSql).append(" limit 1").toString();
	}

	/**
	 * 由传入的对象生成count sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildCountSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
		}

		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		AtomicInteger ai = new AtomicInteger(0);
		TableName tableName = new TableName(
				((TableMapperAnnotation) tableMapper.getTableMapperAnnotation()).tableName(), 0);

		StringBuffer selectSql = new StringBuffer();
		selectSql.append("select count(").append(tableName.sqlWhere());
		/*
		 * 如果有且只有一个主键，采用select count("主键")的方式；如果无主键或有多个主键（联合主键），采用select
		 * count(*)的方式。
		 */
		if (tableMapper.getUniqueKeyNames().length == 1) {
			selectSql.append(tableMapper.getUniqueKeyNames()[0].getDbFieldName());
		} else {
			selectSql.append("*");
		}
		selectSql.append(")");

		StringBuffer fromSql = new StringBuffer(" from ");
		StringBuffer whereSql = new StringBuffer(" where ");

		dealMapperAnnotationIterationForCount(object, fromSql, whereSql, null, null, null, ai);

		if (selectSql.indexOf(",") > -1) {
			selectSql.delete(selectSql.lastIndexOf(","), selectSql.lastIndexOf(",") + 1);
		}
		if (" where ".equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf("and") > -1) {
			whereSql.delete(whereSql.lastIndexOf("and"), whereSql.lastIndexOf("and") + 3);
		}

		return selectSql.append(fromSql).append(whereSql).toString();
	}

	private static boolean hasTableMapperAnnotation(Class<?> clazz) {
		Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
		for (Annotation an : classAnnotations) {
			if (an instanceof TableMapperAnnotation) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasQueryMapperAnnotation(Class<?> clazz) {
		Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
		for (Annotation an : classAnnotations) {
			if (an instanceof QueryMapperAnnotation) {
				return true;
			}
		}
		return false;
	}

	private static void dealMapperAnnotationIterationForSelectAll(Object object, StringBuffer selectSql,
			StringBuffer fromSql, StringBuffer whereSql, TableName originTableName, Mapperable originFieldMapper,
			String fieldPerfix, AtomicInteger index) throws Exception {
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));
		TableName tableName = new TableName(
				((TableMapperAnnotation) tableMapper.getTableMapperAnnotation()).tableName(), index.getAndIncrement());

		/*
		 * 在第一次遍历中，处理好selectSql和fromSql。 如果originFieldMapper为null则可认为是第一次遍历
		 */
		if (originFieldMapper == null) {
			fromSql.append(tableName.sqlSelect());
			for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
				if (!fieldMapper.isIgnoredSelect()) {
					selectSql.append(tableName.sqlWhere()).append(fieldMapper.getDbFieldName()).append(",");
				}
			}
		}
		/*
		 * 在非第一次遍历中，处理fieldPerfix和fromSql。
		 * 如果originFieldMapper和originTableName均不为null则可认为是非第一次遍历
		 */
		String temp = null;
		if (originFieldMapper != null && originTableName != null) {
			/* 处理fieldPerfix */
			temp = originFieldMapper.getFieldName();
			if (fieldPerfix != null) {
				temp = fieldPerfix + "." + temp;
			}
			/* 处理fromSql */
			fromSql.append(" left join ").append(tableName.sqlSelect()).append(" on ")
					.append(originTableName.sqlWhere()).append(originFieldMapper.getDbFieldName()).append(" = ")
					.append(tableName.sqlWhere()).append(originFieldMapper.getDbAssociationUniqueKey());
		}

		/* 处理fieldMapper中的条件 */
		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}
			/* 此处当value拥有TableMapper或QueryMapper标注时，开始进行迭代 */
			if (hasTableMapperAnnotation(value.getClass()) || hasQueryMapperAnnotation(value.getClass())) {
				dealMapperAnnotationIterationForSelectAll(value, selectSql, fromSql, whereSql, tableName, fieldMapper,
						temp, index);
			} else {
				dealConditionEqual(value, whereSql, fieldMapper, tableName, temp);
			}
		}

		/* 处理queryMapper中的条件 */
		for (ConditionMapper conditionMapper : queryMapper.getConditionMapperCache().values()) {
			Object value = dtoFieldMap.get(conditionMapper.getFieldName());
			if (value == null) {
				continue;
			}
			switch (conditionMapper.getConditionType()) {
			case Equal:
				dealConditionEqual(value, whereSql, conditionMapper, tableName, temp);
				break;
			case Like:
				dealConditionLike(whereSql, conditionMapper, ConditionType.Like, tableName, temp);
				break;
			case HeadLike:
				dealConditionLike(whereSql, conditionMapper, ConditionType.HeadLike, tableName, temp);
				break;
			case TailLike:
				dealConditionLike(whereSql, conditionMapper, ConditionType.TailLike, tableName, temp);
				break;
			case GreaterThan:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GreaterThan, tableName, temp);
				break;
			case GreaterOrEqual:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GreaterOrEqual, tableName, temp);
				break;
			case LessThan:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LessThan, tableName, temp);
				break;
			case LessOrEqual:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LessOrEqual, tableName, temp);
				break;
			case NotEqual:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.NotEqual, tableName, temp);
				break;
			case MultiLikeAND:
				dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MultiLikeAND, tableName, temp);
				break;
			case MultiLikeOR:
				dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MultiLikeOR, tableName, temp);
				break;
			case In:
				dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.In, tableName, temp);
				break;
			case NotIn:
				dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.NotIn, tableName, temp);
				break;
			case NullOrNot:
				dealConditionNullOrNot(value, whereSql, conditionMapper, tableName, temp);
				break;
			default:
				break;
			}
		}
	}

	private static void dealMapperAnnotationIterationForCount(Object object, StringBuffer fromSql,
			StringBuffer whereSql, TableName originTableName, Mapperable originFieldMapper, String fieldPerfix,
			AtomicInteger index) throws Exception {
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));
		TableName tableName = new TableName(
				((TableMapperAnnotation) tableMapper.getTableMapperAnnotation()).tableName(), index.getAndIncrement());

		/*
		 * 在第一次遍历中，处理好fromSql。 如果originFieldMapper为null则可认为是第一次遍历
		 */
		if (originFieldMapper == null) {
			fromSql.append(tableName.sqlSelect());
		}

		/*
		 * 在非第一次遍历中，处理fieldPerfix和fromSql。
		 * 如果originFieldMapper和originTableName均不为null则可认为是非第一次遍历
		 */
		String temp = null;
		if (originFieldMapper != null && originTableName != null) {
			/* 处理fieldPerfix */
			temp = originFieldMapper.getFieldName();
			if (fieldPerfix != null) {
				temp = fieldPerfix + "." + temp;
			}
			/* 处理fromSql */
			fromSql.append(" left join ").append(tableName.sqlSelect()).append(" on ")
					.append(originTableName.sqlWhere()).append(originFieldMapper.getDbFieldName()).append(" = ")
					.append(tableName.sqlWhere()).append(originFieldMapper.getDbAssociationUniqueKey());
		}

		/* 处理fieldMapper中的条件 */
		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}
			/* 此处当value拥有TableMapper或QueryMapper标注时，开始进行迭代 */
			if (hasTableMapperAnnotation(value.getClass()) || hasQueryMapperAnnotation(value.getClass())) {
				dealMapperAnnotationIterationForCount(value, fromSql, whereSql, tableName, fieldMapper, temp, index);
			} else {
				dealConditionEqual(value, whereSql, fieldMapper, tableName, temp);
			}
		}

		/* 处理queryMapper中的条件 */
		for (ConditionMapper conditionMapper : queryMapper.getConditionMapperCache().values()) {
			Object value = dtoFieldMap.get(conditionMapper.getFieldName());
			if (value == null) {
				continue;
			}
			switch (conditionMapper.getConditionType()) {
			case Equal:
				dealConditionEqual(value, whereSql, conditionMapper, tableName, temp);
				break;
			case Like:
				dealConditionLike(whereSql, conditionMapper, ConditionType.Like, tableName, temp);
				break;
			case HeadLike:
				dealConditionLike(whereSql, conditionMapper, ConditionType.HeadLike, tableName, temp);
				break;
			case TailLike:
				dealConditionLike(whereSql, conditionMapper, ConditionType.TailLike, tableName, temp);
				break;
			case GreaterThan:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GreaterThan, tableName, temp);
				break;
			case GreaterOrEqual:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GreaterOrEqual, tableName, temp);
				break;
			case LessThan:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LessThan, tableName, temp);
				break;
			case LessOrEqual:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LessOrEqual, tableName, temp);
				break;
			case NotEqual:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.NotEqual, tableName, temp);
				break;
			case MultiLikeAND:
				dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MultiLikeAND, tableName, temp);
				break;
			case MultiLikeOR:
				dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MultiLikeOR, tableName, temp);
				break;
			case In:
				dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.In, tableName, temp);
				break;
			case NotIn:
				dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.NotIn, tableName, temp);
				break;
			case NullOrNot:
				dealConditionNullOrNot(value, whereSql, conditionMapper, tableName, temp);
				break;
			default:
				break;
			}
		}
	}
}

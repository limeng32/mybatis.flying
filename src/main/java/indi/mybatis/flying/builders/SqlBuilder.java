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
import indi.mybatis.flying.exception.BuildSqlException;
import indi.mybatis.flying.exception.BuildSqlExceptionEnum;
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

	/* 缓存TableMapper */
	private static Map<Class<?>, TableMapper> tableMapperCache = new ConcurrentHashMap<Class<?>, TableMapper>(128);
	/* 缓存QueryMapper */
	private static Map<Class<?>, QueryMapper> queryMapperCache = new ConcurrentHashMap<Class<?>, QueryMapper>(128);

	private static final String DOT = ".";
	private static final String COMMA = ",";

	private static final String JDBCTYPE_EQUAL = "jdbcType=";
	private static final String COMMA_TYPEHANDLER_EQUAL = ",typeHandler=";
	private static final String CLOSEPAREN_ = ") ";
	private static final String CLOSEBRACE_AND_ = "} and ";
	private static final String CLOSEBRACE_OR_ = "} or ";
	private static final String WHERE_ = " where ";
	private static final String POUND_OPENBRACE = "#{";
	private static final String OPENBRACKET = "[";
	private static final String CLOSEBRACKET = "]";
	private static final String CLOSEBRACE_COMMA = "},";
	private static final String CLOSEPAREN__AND_ = ") and ";
	private static final String AND = "and";
	private static final String FROM = " from ";
	private static final String CLOSEPAREN = ")";
	private static final String ASTERISK = "*";
	private static final String INSERT_INTO_ = "insert into ";
	private static final String SELECT_ = "select ";
	private static final String SELECT_COUNT_OPENPAREN = "select count(";
	private static final String EQUAL_POUND_OPENBRACE = "=#{";
	private static final String DELETE_FROM_ = "delete from ";
	private static final String COMMA_JDBCTYPE_EQUAL = ",jdbcType=";
	private static final String PLUS_1 = "+1";
	private static final String CLOSEBRACE = "}";
	private static final String CONDITIONLIKEHANDLER = "ConditionLikeHandler";
	private static final String VALUES_OPENPAREN = "values(";
	private static final String UPDATE_ = "update ";

	private static final String _AND_ = " and ";
	private static final String _EQUAL_ = " = ";
	private static final String _GREATER_ = " > ";
	private static final String _GREATER_EQUAL_ = " >= ";
	private static final String _IN_OPENPAREN = " in(";
	private static final String _IS = " is";
	private static final String _LEFT_JOIN_ = " left join ";
	private static final String _LESS_ = " < ";
	private static final String _LESS_EQUAL_ = " <= ";
	private static final String _LESS_GREATER_ = " <> ";
	private static final String _LIKE__POUND_OPENBRACE = " like #{";
	private static final String _LIMIT_1 = " limit 1";
	private static final String _NOT = " not";
	private static final String _NULL = " null";
	private static final String _ON_ = " on ";
	private static final String _OPENPAREN = " (";
	private static final String _OR_ = " or ";
	private static final String _SET_ = " set ";

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
					if (fieldMapperAnnotation.isUniqueKey()) {
						uniqueKeyList.add(fieldMapper);
					}

					if (fieldMapperAnnotation.ignoreTag().length > 0) {
						for (String t : fieldMapperAnnotation.ignoreTag()) {
							fieldMapper.getIgnoreTagSet().add(t);
						}
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
			throw new BuildSqlException(new StringBuffer(BuildSqlExceptionEnum.noTableMapperAnnotation.toString())
					.append(clazz.getName()).toString());
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
		whereSql.append(_LIKE__POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (conditionMapper.isForeignKey()) {
			whereSql.append(conditionMapper.getFieldName()).append(DOT).append(conditionMapper.getForeignFieldName());
		} else {
			whereSql.append(conditionMapper.getFieldName());
		}
		whereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(conditionMapper.getJdbcType().toString())
				.append(COMMA_TYPEHANDLER_EQUAL);
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
			throw new BuildSqlException(BuildSqlExceptionEnum.ambiguousCondition);
		}
		whereSql.append(CLOSEBRACE_AND_);
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
				tempWhereSql.append(_NOT);
				break;
			default:
				throw new BuildSqlException(BuildSqlExceptionEnum.ambiguousCondition);
			}
			tempWhereSql.append(_IN_OPENPAREN);
			int j = -1;
			boolean allNull = true;
			for (Object s : multiConditionC) {
				j++;
				if (s != null) {
					if (allNull) {
						allNull = false;
					}
					tempWhereSql.append(POUND_OPENBRACE);
					if (fieldNamePrefix != null) {
						tempWhereSql.append(fieldNamePrefix).append(DOT);
					}
					if (conditionMapper.isForeignKey()) {
						tempWhereSql.append(conditionMapper.getFieldName()).append(DOT)
								.append(conditionMapper.getForeignFieldName()).append(OPENBRACKET).append(j)
								.append(CLOSEBRACKET);
					} else {
						tempWhereSql.append(conditionMapper.getFieldName()).append(OPENBRACKET).append(j)
								.append(CLOSEBRACKET);
					}
					tempWhereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(conditionMapper.getJdbcType().toString())
							.append(CLOSEBRACE_COMMA);
				}
			}
			if (!allNull) {
				tempWhereSql.delete(tempWhereSql.lastIndexOf(COMMA), tempWhereSql.lastIndexOf(COMMA) + 1);
				tempWhereSql.append(CLOSEPAREN__AND_);
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
			tempWhereSql.append(_OPENPAREN);
			int j = -1;
			boolean allNull = true;
			for (String s : multiConditionList) {
				j++;
				if (s != null) {
					if (allNull) {
						allNull = false;
					}
					handleWhereSql(tempWhereSql, conditionMapper, tableName, fieldNamePrefix);
					tempWhereSql.append(_LIKE__POUND_OPENBRACE);
					if (fieldNamePrefix != null) {
						tempWhereSql.append(fieldNamePrefix).append(DOT);
					}
					if (conditionMapper.isForeignKey()) {
						tempWhereSql.append(conditionMapper.getFieldName()).append(DOT)
								.append(conditionMapper.getForeignFieldName()).append(OPENBRACKET).append(j)
								.append(CLOSEBRACKET);
					} else {
						tempWhereSql.append(conditionMapper.getFieldName()).append(OPENBRACKET).append(j)
								.append(CLOSEBRACKET);
					}
					tempWhereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(conditionMapper.getJdbcType().toString())
							.append(COMMA_TYPEHANDLER_EQUAL);
					switch (type) {
					case MultiLikeAND:
						tempWhereSql.append(CONDITIONLIKEHANDLER);
						tempWhereSql.append(CLOSEBRACE_AND_);
						break;
					case MultiLikeOR:
						tempWhereSql.append(CONDITIONLIKEHANDLER);
						tempWhereSql.append(CLOSEBRACE_OR_);
						break;
					default:
						throw new BuildSqlException(BuildSqlExceptionEnum.ambiguousCondition);
					}
				}
			}
			if (!allNull) {
				switch (type) {
				case MultiLikeAND:
					tempWhereSql.delete(tempWhereSql.lastIndexOf(_AND_), tempWhereSql.lastIndexOf(_AND_) + 5);
					break;
				case MultiLikeOR:
					tempWhereSql.delete(tempWhereSql.lastIndexOf(_OR_), tempWhereSql.lastIndexOf(_OR_) + 4);
					break;
				default:
				}
				tempWhereSql.append(CLOSEPAREN__AND_);
				whereSql.append(tempWhereSql);
			}
		}
	}

	private static void dealConditionEqual(Object object, StringBuffer whereSql, Mapperable mapper, TableName tableName,
			String fieldNamePrefix) {
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(EQUAL_POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (mapper.isForeignKey()) {
			whereSql.append(mapper.getFieldName()).append(DOT).append(mapper.getForeignFieldName());
		} else {
			whereSql.append(mapper.getFieldName());
		}
		if (mapper.getJdbcType() != null) {
			whereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(mapper.getJdbcType().toString());
		}
		whereSql.append(CLOSEBRACE_AND_);
	}

	private static void dealConditionNotEqual(StringBuffer whereSql, Mapperable mapper, ConditionType type,
			TableName tableName, String fieldNamePrefix) {
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		switch (type) {
		case GreaterThan:
			whereSql.append(_GREATER_);
			break;
		case GreaterOrEqual:
			whereSql.append(_GREATER_EQUAL_);
			break;
		case LessThan:
			whereSql.append(_LESS_);
			break;
		case LessOrEqual:
			whereSql.append(_LESS_EQUAL_);
			break;
		case NotEqual:
			whereSql.append(_LESS_GREATER_);
			break;
		default:
			throw new BuildSqlException(BuildSqlExceptionEnum.ambiguousCondition);
		}
		whereSql.append(POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (mapper.isForeignKey()) {
			whereSql.append(mapper.getFieldName()).append(DOT).append(mapper.getForeignFieldName());
		} else {
			whereSql.append(mapper.getFieldName());
		}
		if (mapper.getJdbcType() != null) {
			whereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(mapper.getJdbcType().toString());
		}
		whereSql.append(CLOSEBRACE_AND_);
	}

	private static void dealConditionNullOrNot(Object value, StringBuffer whereSql, Mapperable mapper,
			TableName tableName, String fieldNamePrefix) {
		Boolean isNull = (Boolean) value;
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(_IS);
		if (!isNull) {
			whereSql.append(_NOT);
		}
		whereSql.append(_NULL).append(_AND_);
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
	 *            pojo
	 * @return String
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildInsertSql(Object object) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();

		String tableName = tma.tableName();
		StringBuffer tableSql = new StringBuffer();
		StringBuffer valueSql = new StringBuffer();

		tableSql.append(INSERT_INTO_).append(tableName).append(_OPENPAREN);
		valueSql.append(VALUES_OPENPAREN);

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
			tableSql.append(fieldMapper.getDbFieldName()).append(COMMA);
			valueSql.append(POUND_OPENBRACE);
			if (fieldMapper.isForeignKey()) {
				valueSql.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
			} else {
				valueSql.append(fieldMapper.getFieldName());
			}
			valueSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString())
					.append(CLOSEBRACE_COMMA);
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullField);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		valueSql.delete(valueSql.lastIndexOf(COMMA), valueSql.lastIndexOf(COMMA) + 1);
		return tableSql.append(CLOSEPAREN_).append(valueSql).append(CLOSEPAREN).toString();
	}

	/**
	 * 由传入的对象生成update sql语句
	 * 
	 * @param object
	 *            pojo
	 * @return sql
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildUpdateSql(Object object) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}

		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(WHERE_);

		tableSql.append(UPDATE_).append(tableName).append(_SET_);

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE);
			if (fieldMapper.isForeignKey()) {
				tableSql.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
			} else {
				tableSql.append(fieldMapper.getFieldName());
			}
			tableSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
			tableSql.append(CLOSEBRACE);
			if (fieldMapper.isOpVersionLock()) {
				tableSql.append(PLUS_1);
			}
			tableSql.append(COMMA);
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullField);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new BuildSqlException(new StringBuffer(BuildSqlExceptionEnum.updateUniqueKeyIsNull.toString())
						.append(fieldMapper.getDbFieldName()).toString());
			}
			whereSql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA)
					.append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_);
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			whereSql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
					.append(CLOSEBRACE_AND_);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return tableSql.append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成update持久态对象的 sql语句
	 * 
	 * @param object
	 *            pojo
	 * @return sql
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildUpdatePersistentSql(Object object) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}

		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(WHERE_);

		tableSql.append(UPDATE_).append(tableName).append(_SET_);

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE);
			if (fieldMapper.isForeignKey()) {
				tableSql.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
			} else {
				tableSql.append(fieldMapper.getFieldName());
			}
			tableSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString())
					.append(CLOSEBRACE);
			if (fieldMapper.isOpVersionLock()) {
				tableSql.append(PLUS_1);
			}
			tableSql.append(COMMA);
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullField);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new BuildSqlException(
						new StringBuffer(BuildSqlExceptionEnum.updatePersistentUniqueKeyIsNull.toString())
								.append(fieldMapper.getDbFieldName()).toString());
			}
			whereSql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA_JDBCTYPE_EQUAL)
					.append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_);
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			whereSql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
					.append(CLOSEBRACE_AND_);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return tableSql.append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成delete sql语句
	 * 
	 * @param object
	 *            pojo
	 * @return sql
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildDeleteSql(Object object) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer sql = new StringBuffer();

		sql.append(DELETE_FROM_).append(tableName).append(WHERE_);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			sql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new BuildSqlException(new StringBuffer(BuildSqlExceptionEnum.deleteUniqueKeyIsNull.toString())
						.append(fieldMapper.getDbFieldName()).toString());
			}
			sql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_);
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			sql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
					.append(CLOSEBRACE_AND_);
		}
		sql.delete(sql.lastIndexOf(AND), sql.lastIndexOf(AND) + 3);
		return sql.toString();
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param clazz
	 *            pojo Class
	 * @return sql
	 */
	public static String buildSelectSql(Class<?> clazz, String ignoreTag) {
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(clazz));
		TableMapperAnnotation tma = (TableMapperAnnotation) tableMapper.getTableMapperAnnotation();
		String tableName = tma.tableName();

		StringBuffer selectSql = new StringBuffer(SELECT_);

		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			if (ignoreTag == null || (!fieldMapper.getIgnoreTagSet().contains(ignoreTag))) {
				selectSql.append(fieldMapper.getDbFieldName()).append(COMMA);
			}
		}

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}

		selectSql.append(FROM).append(tableName);

		StringBuffer whereSql = new StringBuffer(WHERE_);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			whereSql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA)
					.append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return selectSql.append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param object
	 *            pojo
	 * @return sql
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildSelectAllSql(Object object, String ignoreTag) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		StringBuffer selectSql = new StringBuffer(SELECT_);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object, selectSql, fromSql, whereSql, null, null, null, ai,
				ignoreTag);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_.equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}
		return selectSql.append(fromSql).append(whereSql).toString();
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param object
	 *            pojo
	 * @return sql
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildSelectOneSql(Object object, String ignoreTag) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		if (object instanceof Conditionable) {
			((Conditionable) object).setLimiter(null);
		}
		StringBuffer selectSql = new StringBuffer(SELECT_);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object, selectSql, fromSql, whereSql, null, null, null, ai,
				ignoreTag);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_.equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}
		return selectSql.append(fromSql).append(whereSql).append(_LIMIT_1).toString();
	}

	/**
	 * 由传入的对象生成count sql语句
	 * 
	 * @param object
	 *            pojo
	 * @return sql
	 * @throws Exception
	 *             RuntimeException
	 */
	public static String buildCountSql(Object object) throws Exception {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}

		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		AtomicInteger ai = new AtomicInteger(0);
		TableName tableName = new TableName(
				((TableMapperAnnotation) tableMapper.getTableMapperAnnotation()).tableName(), 0);

		StringBuffer selectSql = new StringBuffer();
		selectSql.append(SELECT_COUNT_OPENPAREN).append(tableName.sqlWhere());
		/*
		 * 如果有且只有一个主键，采用select count("主键")的方式；如果无主键或有多个主键（联合主键），采用select
		 * count(*)的方式。
		 */
		if (tableMapper.getUniqueKeyNames().length == 1) {
			selectSql.append(tableMapper.getUniqueKeyNames()[0].getDbFieldName());
		} else {
			selectSql.append(ASTERISK);
		}
		selectSql.append(CLOSEPAREN);

		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_);

		dealMapperAnnotationIterationForCount(object, fromSql, whereSql, null, null, null, ai);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_.equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
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
			String fieldPerfix, AtomicInteger index, String ignoreTag) throws Exception {
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
				if (ignoreTag == null || (!fieldMapper.getIgnoreTagSet().contains(ignoreTag))) {
					selectSql.append(tableName.sqlWhere()).append(fieldMapper.getDbFieldName()).append(COMMA);
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
				temp = fieldPerfix + DOT + temp;
			}
			/* 处理fromSql */
			fromSql.append(_LEFT_JOIN_).append(tableName.sqlSelect()).append(_ON_).append(originTableName.sqlWhere())
					.append(originFieldMapper.getDbFieldName()).append(_EQUAL_).append(tableName.sqlWhere())
					.append(originFieldMapper.getDbAssociationUniqueKey());
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
						temp, index, null);
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
				temp = fieldPerfix + DOT + temp;
			}
			/* 处理fromSql */
			fromSql.append(_LEFT_JOIN_).append(tableName.sqlSelect()).append(_ON_).append(originTableName.sqlWhere())
					.append(originFieldMapper.getDbFieldName()).append(_EQUAL_).append(tableName.sqlWhere())
					.append(originFieldMapper.getDbAssociationUniqueKey());
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
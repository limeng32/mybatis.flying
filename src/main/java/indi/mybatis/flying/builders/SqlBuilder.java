package indi.mybatis.flying.builders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.exception.BuildSqlException;
import indi.mybatis.flying.exception.BuildSqlExceptionEnum;
import indi.mybatis.flying.models.ConditionMapper;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.FieldMapper;
import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.models.ForeignAssociationMapper;
import indi.mybatis.flying.models.Mapperable;
import indi.mybatis.flying.models.OrMapper;
import indi.mybatis.flying.models.QueryMapper;
import indi.mybatis.flying.models.TableMapper;
import indi.mybatis.flying.models.TableName;
import indi.mybatis.flying.statics.ConditionType;
import indi.mybatis.flying.statics.HandlerPaths;
import indi.mybatis.flying.type.KeyHandler;
import indi.mybatis.flying.utils.ReflectHelper;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author david, 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 * @description Generate SQL through annotations.
 */
public class SqlBuilder {

	private SqlBuilder() {

	}

	/* Cache TableMapper */
	private static Map<Class<?>, TableMapper> tableMapperCache = new ConcurrentHashMap<Class<?>, TableMapper>(128);
	/* Cache QueryMapper */
	private static Map<Class<?>, QueryMapper> queryMapperCache = new ConcurrentHashMap<Class<?>, QueryMapper>(128);

	private static final String DOT = ".";
	private static final String COMMA = ",";

	private static final String JDBCTYPE_EQUAL = "jdbcType=";
	private static final String COMMA_TYPEHANDLER_EQUAL = ",typeHandler=";
	private static final String CLOSEPAREN_BLANK = ") ";
	private static final String CLOSEBRACE_AND_BLANK = "} and ";
	private static final String CLOSEBRACE_OR_BLANK = "} or ";
	private static final String WHERE_BLANK = " where ";
	private static final String POUND_OPENBRACE = "#{";
	private static final String OPENBRACKET = "[";
	private static final String CLOSEBRACKET = "]";
	private static final String CLOSEBRACE_COMMA = "},";
	private static final String CLOSEPAREN__AND_BLANK = ") and ";
	private static final String AND = "and";
	private static final String FROM = " from ";
	private static final String CLOSEPAREN = ")";
	private static final String ASTERISK = "*";
	private static final String INSERT_INTO_BLANK = "insert into ";
	private static final String SELECT_BLANK = "select ";
	private static final String SELECT_COUNT_OPENPAREN = "select count(";
	private static final String EQUAL_POUND_OPENBRACE = "=#{";
	private static final String DELETE_FROM_BLANK = "delete from ";
	private static final String COMMA_JAVATYPE_EQUAL = ",javaType=";
	private static final String COMMA_JDBCTYPE_EQUAL = ",jdbcType=";
	private static final String PLUS_1 = "+1";
	private static final String CLOSEBRACE = "}";
	private static final String CONDITIONLIKEHANDLER = "ConditionLikeHandler";
	private static final String VALUES_OPENPAREN = "values(";
	private static final String UPDATE_BLANK = "update ";

	private static final String BLANK_AND_BLANK = " and ";
	private static final String BLANK_EQUAL_BLANK = " = ";
	private static final String BLANK_GREATER_BLANK = " > ";
	private static final String BLANK_GREATER_EQUAL_BLANK = " >= ";
	private static final String BLANK_IN_OPENPAREN = " in(";
	private static final String BLANK_IS = " is";
	private static final String BLANK_LESS_BLANK = " < ";
	private static final String BLANK_LESS_EQUAL_BLANK = " <= ";
	private static final String BLANK_LESS_GREATER_BLANK = " <> ";
	private static final String BLANK_LIKE__POUND_OPENBRACE = " like #{";
	private static final String BLANK_LIMIT_1 = " limit 1";
	private static final String BLANK_NOT = " not";
	private static final String BLANK_NULL = " null";
	private static final String BLANK_ON_BLANK = " on ";
	private static final String BLANK_OPENPAREN = " (";
	private static final String BLANK_OR_BLANK = " or ";
	private static final String BLANK_SET_BLANK = " set ";

	/**
	 * The class of the incoming dto object builds the TableMapper object, and the
	 * constructed object is stored in the cache, which is retrieved directly from
	 * the cache later.
	 * 
	 * @param dtoClass
	 * @return TableMapper
	 */
	private static TableMapper buildTableMapper(Class<?> dtoClass) {

		Map<String, FieldMapper> fieldMapperCache = null;
		Field[] fields = dtoClass.getDeclaredFields();

		FieldMapper fieldMapper = null;
		TableMapper tableMapper = null;
		tableMapper = tableMapperCache.get(dtoClass);
		if (tableMapper != null) {
			return tableMapper;
		}
		tableMapper = new TableMapper();
		tableMapper.setClazz(dtoClass);
		List<FieldMapper> uniqueKeyList = new LinkedList<FieldMapper>();
		List<FieldMapper> opVersionLockList = new LinkedList<FieldMapper>();
		Annotation[] classAnnotations = dtoClass.getDeclaredAnnotations();
		for (Annotation an : classAnnotations) {
			if (an instanceof TableMapperAnnotation) {
				tableMapper.setTableMapperAnnotation((TableMapperAnnotation) an);
			} else if (an instanceof Table) {
				tableMapper.setTable((Table) an);
			}
		}
		fieldMapperCache = new WeakHashMap<String, FieldMapper>(16);
		for (Field field : fields) {
			fieldMapper = new FieldMapper();
			boolean b = fieldMapper.buildMapper(field);
			if (!b) {
				continue;
			}
			switch (fieldMapper.getOpLockType()) {
			case Version:
				fieldMapper.setOpVersionLock(true);
				break;
			default:
				break;
			}
			if (fieldMapper.isUniqueKey()) {
				uniqueKeyList.add(fieldMapper);
			}

			if (fieldMapper.getIgnoreTag().length > 0) {
				for (String t : fieldMapper.getIgnoreTag()) {
					fieldMapper.getIgnoreTagSet().add(t);
				}
			}

			if (!"".equals(fieldMapper.getDbAssociationUniqueKey())) {
				fieldMapper.setForeignKey(true);
			}

			if (fieldMapper.isForeignKey()) {
				if (!tableMapperCache.containsKey(field.getType())) {
					buildTableMapper(field.getType());
				}
				TableMapper tm = tableMapperCache.get(field.getType());
				String foreignFieldName = getFieldMapperByDbFieldName(tm.getFieldMapperCache(),
						fieldMapper.getDbAssociationUniqueKey()).getFieldName();
				fieldMapper.setForeignFieldName(foreignFieldName);
			}

			if (!"".equals(fieldMapper.getDbCrossedAssociationUniqueKey())) {
				fieldMapper.setCrossDbForeignKey(true);
			}

			if (fieldMapper.isCrossDbForeignKey()) {
				if (!tableMapperCache.containsKey(field.getType())) {
					buildTableMapper(field.getType());
				}
				TableMapper tm = tableMapperCache.get(field.getType());
				String foreignFieldName = getFieldMapperByDbFieldName(tm.getFieldMapperCache(),
						fieldMapper.getDbCrossedAssociationUniqueKey()).getFieldName();
				fieldMapper.setForeignFieldName(foreignFieldName);
			}

			if (fieldMapper.isOpVersionLock()) {
				opVersionLockList.add(fieldMapper);
			}
			fieldMapperCache.put(fieldMapper.getDbFieldName(), fieldMapper);
		}
		tableMapper.setFieldMapperCache(fieldMapperCache);
		tableMapper.setUniqueKeyNames(uniqueKeyList.toArray(new FieldMapper[uniqueKeyList.size()]));
		tableMapper.setOpVersionLocks(opVersionLockList.toArray(new FieldMapper[opVersionLockList.size()]));
		tableMapper.buildTableName();
		tableMapperCache.put(dtoClass, tableMapper);
		return tableMapper;
	}

	/* 从newFieldMapperCache中获取已知dbFieldName的FieldMapper */
	private static Mapperable getFieldMapperByDbFieldName(Map<String, FieldMapper> newFieldMapperCache,
			String dbFieldName) {
		return newFieldMapperCache.get(dbFieldName);
	}

	/**
	 * The class of the incoming dto object builds the QueryMapper object, and the
	 * constructed object is stored in the cache, which is retrieved directly from
	 * the cache later.
	 * 
	 * @param dtoClass
	 * @param pojoClass
	 * @return QueryMapper
	 */
	private static QueryMapper buildQueryMapper(Class<?> dtoClass, Class<?> pojoClass) {
		QueryMapper queryMapper = queryMapperCache.get(dtoClass);
		if (queryMapper != null) {
			return queryMapper;
		}
		Map<String, ConditionMapper> conditionMapperCache = new WeakHashMap<>(16);
		Map<String, OrMapper> orMapperCache = new WeakHashMap<>(4);
		Field[] fields = null;

		ConditionMapperAnnotation conditionMapperAnnotation = null;
		ConditionMapper conditionMapper = null;
		Or or = null;
		OrMapper orMapper = null;
		queryMapper = new QueryMapper();
		fields = dtoClass.getDeclaredFields();

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
					buildConditionMapper(conditionMapper, conditionMapperAnnotation, pojoClass, field);

					conditionMapperCache.put(field.getName(), conditionMapper);
				} else if (an instanceof Or) {
					or = (Or) an;
					orMapper = new OrMapper();
					orMapper.setFieldName(field.getName());
					ConditionMapper[] conditionMappers = new ConditionMapper[or.value().length];
					int i = 0;
					for (ConditionMapperAnnotation cma : or.value()) {
						conditionMappers[i] = new ConditionMapper();
						buildConditionMapper(conditionMappers[i], cma, pojoClass, field);
						i++;
					}
					orMapper.setConditionMappers(conditionMappers);
					orMapperCache.put(field.getName(), orMapper);
				}
			}
		}
		queryMapper.setConditionMapperCache(conditionMapperCache);
		queryMapper.setOrMapperCache(orMapperCache);
		queryMapperCache.put(dtoClass, queryMapper);
		return queryMapper;
	}

	private static void buildConditionMapper(ConditionMapper conditionMapper,
			ConditionMapperAnnotation conditionMapperAnnotation, Class<?> pojoClass, Field field) {
		conditionMapper.setFieldName(field.getName());
		conditionMapper.setDbFieldName(conditionMapperAnnotation.dbFieldName());
		conditionMapper.setConditionType(conditionMapperAnnotation.conditionType());
		conditionMapper.setSubTarget(conditionMapperAnnotation.subTarget());
		conditionMapper.setTypeHandlerPath(conditionMapperAnnotation.customTypeHandler());
		for (Field pojoField : pojoClass.getDeclaredFields()) {
			for (Annotation oan : pojoField.getDeclaredAnnotations()) {
				boolean b1 = oan instanceof FieldMapperAnnotation && ((FieldMapperAnnotation) oan).dbFieldName()
						.equalsIgnoreCase(conditionMapperAnnotation.dbFieldName());
				boolean b2 = oan instanceof Column && (FieldMapper.getColumnName((Column) oan, pojoField))
						.equalsIgnoreCase(conditionMapperAnnotation.dbFieldName());
				boolean b3 = (conditionMapper.getSubTarget() != null)
						&& (!Void.class.equals(conditionMapper.getSubTarget()));
				if (b1 || b2 || b3) {
					FieldMapper fieldMapper = new FieldMapper();
					if (b3) {
						if (!tableMapperCache.containsKey(conditionMapper.getSubTarget())) {
							buildTableMapper(conditionMapper.getSubTarget());
						}
						TableMapper tableMapper = tableMapperCache.get(conditionMapper.getSubTarget());
						Map<String, FieldMapper> fieldMapperCache = tableMapper.getFieldMapperCache();
						for (Map.Entry<String, FieldMapper> e : fieldMapperCache.entrySet()) {
							if (conditionMapper.getDbFieldName().equalsIgnoreCase(e.getValue().getDbFieldName())) {
								fieldMapper = e.getValue();
								break;
							}
						}
					} else {
						fieldMapper = new FieldMapper();
						fieldMapper.buildMapper(pojoField);
					}
					conditionMapper.setFieldType(fieldMapper.getFieldType());
					conditionMapper.setJdbcType(fieldMapper.getJdbcType());
					if (!"".equals(fieldMapper.getDbAssociationUniqueKey())) {
						conditionMapper.setDbAssociationUniqueKey(fieldMapper.getDbAssociationUniqueKey());
						conditionMapper.setForeignKey(true);
					}
					if (conditionMapper.isForeignKey()
							&& (!ConditionType.NullOrNot.equals(conditionMapper.getConditionType()))) {
						if (!tableMapperCache.containsKey(pojoField.getType())) {
							buildTableMapper(pojoField.getType());
						}
						TableMapper tm = tableMapperCache.get(pojoField.getType());
						String foreignFieldName = getFieldMapperByDbFieldName(tm.getFieldMapperCache(),
								fieldMapper.getDbAssociationUniqueKey()).getFieldName();
						conditionMapper.setForeignFieldName(foreignFieldName);
					}

					if (!"".equals(fieldMapper.getDbCrossedAssociationUniqueKey())) {
						conditionMapper
								.setDbCrossedAssociationUniqueKey(fieldMapper.getDbCrossedAssociationUniqueKey());
						fieldMapper.setCrossDbForeignKey(true);
					}

					if (fieldMapper.isCrossDbForeignKey()) {
						if (!tableMapperCache.containsKey(pojoField.getType())) {
							buildTableMapper(pojoField.getType());
						}
						TableMapper tm = tableMapperCache.get(pojoField.getType());
						String foreignFieldName = getFieldMapperByDbFieldName(tm.getFieldMapperCache(),
								fieldMapper.getDbCrossedAssociationUniqueKey()).getFieldName();

						conditionMapper.setForeignFieldName(foreignFieldName);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Find the class clazz and all the parent classes until you find a class with
	 * the TableMapperAnnotation or Table annotation, and then return the class with
	 * the TableMapperAnnotation annotation
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
	 * To determine whether clazz meets the criteria, whether there is a
	 * TableMapperAnnotation type annotation. If there is one return true, else
	 * return false
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean interview(Class<?> clazz) {
		Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
		if (classAnnotations.length > 0) {
			for (Annotation an : classAnnotations) {
				if (an instanceof TableMapperAnnotation || an instanceof Table) {
					return true;
				}
			}
		}
		return false;
	}

	private static void dealConditionLike(StringBuffer whereSql, ConditionMapper conditionMapper, ConditionType type,
			TableName tableName, String fieldNamePrefix, boolean isOr, int i) {
		handleWhereSql(whereSql, conditionMapper, tableName, fieldNamePrefix);
		whereSql.append(BLANK_LIKE__POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (conditionMapper.isForeignKey()) {
			whereSql.append(conditionMapper.getFieldName()).append(DOT).append(conditionMapper.getForeignFieldName());
		} else {
			whereSql.append(conditionMapper.getFieldName());
		}
		if (isOr) {
			whereSql.append(OPENBRACKET).append(i).append(CLOSEBRACKET);
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
		if (isOr) {
			whereSql.append(CLOSEBRACE_OR_BLANK);
		} else {
			whereSql.append(CLOSEBRACE_AND_BLANK);
		}
	}

	private static void dealConditionInOrNot(Object value, StringBuffer whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix, boolean isOr) {
		if (isOr) {
			throw new BuildSqlException(BuildSqlExceptionEnum.ThisConditionNotSupportOr);
		}
		handleWhereSql(whereSql, conditionMapper, tableName, fieldNamePrefix);
		switch (type) {
		case In:
			break;
		case NotIn:
			whereSql.append(BLANK_NOT);
			break;
		default:
			throw new BuildSqlException(BuildSqlExceptionEnum.ambiguousCondition);
		}
		whereSql.append(BLANK_IN_OPENPAREN);
		dealWhereSqlOfIn(value, whereSql, conditionMapper, fieldNamePrefix);
	}

	@SuppressWarnings("unchecked")
	private static void dealConditionMultiLike(Object value, StringBuffer whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix, boolean isOr) {
		if (isOr) {
			throw new BuildSqlException(BuildSqlExceptionEnum.ThisConditionNotSupportOr);
		}
		List<String> multiConditionList = (List<String>) value;
		if (multiConditionList.size() > 0) {
			StringBuffer tempWhereSql = new StringBuffer();
			tempWhereSql.append(BLANK_OPENPAREN);
			int j = -1;
			boolean allNull = true;
			for (String s : multiConditionList) {
				j++;
				if (s != null) {
					if (allNull) {
						allNull = false;
					}
					handleWhereSql(tempWhereSql, conditionMapper, tableName, fieldNamePrefix);
					tempWhereSql.append(BLANK_LIKE__POUND_OPENBRACE);
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
						tempWhereSql.append(CLOSEBRACE_AND_BLANK);
						break;
					case MultiLikeOR:
						tempWhereSql.append(CONDITIONLIKEHANDLER);
						tempWhereSql.append(CLOSEBRACE_OR_BLANK);
						break;
					default:
						throw new BuildSqlException(BuildSqlExceptionEnum.ambiguousCondition);
					}
				}
			}
			if (!allNull) {
				switch (type) {
				case MultiLikeAND:
					tempWhereSql.delete(tempWhereSql.lastIndexOf(BLANK_AND_BLANK),
							tempWhereSql.lastIndexOf(BLANK_AND_BLANK) + 5);
					break;
				case MultiLikeOR:
					tempWhereSql.delete(tempWhereSql.lastIndexOf(BLANK_OR_BLANK),
							tempWhereSql.lastIndexOf(BLANK_OR_BLANK) + 4);
					break;
				default:
				}
				tempWhereSql.append(CLOSEPAREN__AND_BLANK);
				whereSql.append(tempWhereSql);
			}
		}
	}

	private static void dealConditionEqual(StringBuffer whereSql, Mapperable mapper, TableName tableName,
			String fieldNamePrefix, boolean isOr, int i) {
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(EQUAL_POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (mapper.isForeignKey() || mapper.isCrossDbForeignKey()) {
			whereSql.append(mapper.getFieldName()).append(DOT).append(mapper.getForeignFieldName());
		} else {
			whereSql.append(mapper.getFieldName());
		}
		if (isOr) {
			whereSql.append(OPENBRACKET).append(i).append(CLOSEBRACKET);
		}
		if (mapper.getJdbcType() != null) {
			whereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(mapper.getJdbcType().toString());
		}
		if (mapper.getTypeHandlerPath() != null) {
			whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(mapper.getTypeHandlerPath());
		}
		if (isOr) {
			whereSql.append(COMMA_JAVATYPE_EQUAL).append(mapper.getFieldType().getName()).append(CLOSEBRACE_OR_BLANK);
		} else {
			whereSql.append(CLOSEBRACE_AND_BLANK);
		}
	}

	private static void dealConditionNotEqual(StringBuffer whereSql, Mapperable mapper, ConditionType type,
			TableName tableName, String fieldNamePrefix, boolean isOr, int i) {
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		switch (type) {
		case GreaterThan:
			whereSql.append(BLANK_GREATER_BLANK);
			break;
		case GreaterOrEqual:
			whereSql.append(BLANK_GREATER_EQUAL_BLANK);
			break;
		case LessThan:
			whereSql.append(BLANK_LESS_BLANK);
			break;
		case LessOrEqual:
			whereSql.append(BLANK_LESS_EQUAL_BLANK);
			break;
		case NotEqual:
			whereSql.append(BLANK_LESS_GREATER_BLANK);
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
		if (isOr) {
			whereSql.append(OPENBRACKET).append(i).append(CLOSEBRACKET);
		}
		if (mapper.getJdbcType() != null) {
			whereSql.append(COMMA).append(JDBCTYPE_EQUAL).append(mapper.getJdbcType().toString());
		}
		if (mapper.getTypeHandlerPath() != null) {
			whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(mapper.getTypeHandlerPath());
		}
		if (isOr) {
			whereSql.append(COMMA_JAVATYPE_EQUAL).append(mapper.getFieldType().getName()).append(CLOSEBRACE_OR_BLANK);
		} else {
			whereSql.append(CLOSEBRACE_AND_BLANK);
		}
	}

	private static void dealConditionNullOrNot(Object value, StringBuffer whereSql, Mapperable mapper,
			TableName tableName, String fieldNamePrefix, boolean isOr) {
		Boolean isNull = (Boolean) value;
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(BLANK_IS);
		if (!isNull) {
			whereSql.append(BLANK_NOT);
		}
		whereSql.append(BLANK_NULL);
		if (isOr) {
			whereSql.append(BLANK_OR_BLANK);
		} else {
			whereSql.append(BLANK_AND_BLANK);
		}
	}

	private static void handleWhereSql(StringBuffer whereSql, Mapperable mapper, TableName tableName,
			String fieldNamePrefix) {
		if (tableName != null) {
			if (mapper.getSubTarget() == null || Void.class.equals(mapper.getSubTarget())) {
				whereSql.append(tableName.sqlWhere());
			} else {
				TableName temp = tableName.getMap().get(mapper.getSubTarget());
				whereSql.append(new StringBuffer(temp.getTableMapper().getTableName()).append("_")
						.append(temp.getIndex()).append("."));
			}
		}
		whereSql.append(mapper.getDbFieldName());
	}

	/**
	 * The insert SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return String
	 * @throws IllegalArgumentException  Exception
	 * @throws NoSuchFieldException      Exception
	 * @throws SecurityException         Exception
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws RuntimeException          Exception
	 * @throws IllegalAccessException    Exception
	 */
	public static String buildInsertSql(Object object, FlyingModel flyingModel)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String ignoreTag = flyingModel.getIgnoreTag();
		KeyHandler keyHandler = flyingModel.getKeyHandler();
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		String tableName = tableMapper.getTableName();
		StringBuffer tableSql = new StringBuffer();
		StringBuffer valueSql = new StringBuffer();

		tableSql.append(INSERT_INTO_BLANK).append(tableName).append(BLANK_OPENPAREN);
		valueSql.append(VALUES_OPENPAREN);

		boolean allFieldNull = true;
		boolean uniqueKeyHandled = false;
		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (!fieldMapper.isInsertAble() || ((value == null && !fieldMapper.isOpVersionLock())
					|| (fieldMapper.getIgnoreTagSet().contains(ignoreTag)))) {
				continue;
			} else if (((FieldMapper) fieldMapper).isOpVersionLock()) {
				value = 0;
				ReflectHelper.setValueByFieldName(object, fieldMapper.getFieldName(), value);
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(COMMA);
			valueSql.append(POUND_OPENBRACE);
			if (fieldMapper.isForeignKey() || fieldMapper.isCrossDbForeignKey()) {
				valueSql.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
			} else {
				valueSql.append(fieldMapper.getFieldName());
			}
			valueSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
			if (fieldMapper.getTypeHandlerPath() != null) {
				valueSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
			}
			if (fieldMapper.isUniqueKey()) {
				uniqueKeyHandled = true;
				if (keyHandler != null) {
					handleInsertSql(keyHandler, valueSql, fieldMapper, object, uniqueKeyHandled);
				}
			}
			valueSql.append(CLOSEBRACE_COMMA);
		}
		if (keyHandler != null && !uniqueKeyHandled) {
			FieldMapper temp = tableMapper.getUniqueKeyNames()[0];
			tableSql.append(temp.getDbFieldName()).append(COMMA);
			handleInsertSql(keyHandler, valueSql, temp, object, uniqueKeyHandled);
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullField);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		valueSql.delete(valueSql.lastIndexOf(COMMA), valueSql.lastIndexOf(COMMA) + 1);
		return tableSql.append(CLOSEPAREN_BLANK).append(valueSql).append(CLOSEPAREN).toString();
	}

	private static void handleInsertSql(KeyHandler keyHandler, StringBuffer valueSql, FieldMapper fieldMapper,
			Object object, boolean uniqueKeyHandled) throws IllegalAccessException, NoSuchFieldException {
		if (!uniqueKeyHandled) {
			valueSql.append(POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_COMMA);
		}
		ReflectHelper.setValueByFieldName(object, fieldMapper.getFieldName(), keyHandler.getKey());
	}

	/**
	 * The update SQL statement is generated by the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return sql
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws IllegalAccessException    Exception
	 * @throws RuntimeException          Exception
	 */
	public static String buildUpdateSql(Object object, FlyingModel flyingModel)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		String ignoreTag = flyingModel.getIgnoreTag();
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));

		String tableName = tableMapper.getTableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(WHERE_BLANK);

		tableSql.append(UPDATE_BLANK).append(tableName).append(BLANK_SET_BLANK);

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (!fieldMapper.isUpdateAble() || (value == null || (fieldMapper.getIgnoreTagSet().contains(ignoreTag)))) {
				continue;
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE);
			if (fieldMapper.isForeignKey() || fieldMapper.isCrossDbForeignKey()) {
				tableSql.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
			} else {
				tableSql.append(fieldMapper.getFieldName());
			}
			tableSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
			if (fieldMapper.getTypeHandlerPath() != null) {
				tableSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
			}
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

		// Start processing queryMapper for batch update
		boolean useBatch = false;
		for (ConditionMapper conditionMapper : queryMapper.getConditionMapperCache().values()) {
			Object value = dtoFieldMap.get(conditionMapper.getFieldName());
			if (value == null) {
				continue;
			}
			dealBatchCondition(conditionMapper.getConditionType(), whereSql, conditionMapper, value);
			useBatch = true;
		}
		if (!useBatch) {
			for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
				Object value = dtoFieldMap.get(fieldMapper.getFieldName());
				if (value == null) {
					throw new BuildSqlException(new StringBuffer(BuildSqlExceptionEnum.updateUniqueKeyIsNull.toString())
							.append(fieldMapper.getDbFieldName()).toString());
				} else {
					whereSql.append(fieldMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE)
							.append(fieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
							.append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
				}
			}
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			whereSql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
					.append(CLOSEBRACE_AND_BLANK);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return tableSql.append(whereSql).toString();
	}

	private static void dealBatchCondition(ConditionType conditionType, StringBuffer whereSql,
			ConditionMapper conditionMapper, Object value) {
		switch (conditionType) {
		case Equal:
			whereSql.append(conditionMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
			break;
		case LessOrEqual:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LESS_EQUAL_BLANK).append(POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
			break;
		case LessThan:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LESS_BLANK).append(POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
			break;
		case GreaterOrEqual:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_GREATER_EQUAL_BLANK).append(POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
			break;
		case GreaterThan:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_GREATER_BLANK).append(POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
			break;
		case Like:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LIKE__POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
					.append(HandlerPaths.CONDITION_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
			break;
		case HeadLike:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LIKE__POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
					.append(HandlerPaths.CONDITION_HEAD_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
			break;
		case TailLike:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LIKE__POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
					.append(HandlerPaths.CONDITION_TAIL_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
			break;
		case NotEqual:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LESS_GREATER_BLANK).append(POUND_OPENBRACE)
					.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(conditionMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
			break;
		case NullOrNot:
			Boolean isNull = (Boolean) value;
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_IS);
			if (!isNull) {
				whereSql.append(BLANK_NOT);
			}
			whereSql.append(BLANK_NULL).append(BLANK_AND_BLANK);
			break;
		case In:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_IN_OPENPAREN);
			dealWhereSqlOfIn(value, whereSql, conditionMapper, null);
			break;
		case NotIn:
			whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_NOT).append(BLANK_IN_OPENPAREN);
			dealWhereSqlOfIn(value, whereSql, conditionMapper, null);
			break;
		default:
			throw new BuildSqlException(
					new StringBuffer(BuildSqlExceptionEnum.unkownConditionForBatchProcess.toString())
							.append(conditionMapper.getDbFieldName()).toString());
		}
	}

	@SuppressWarnings("unchecked")
	private static void dealWhereSqlOfIn(Object value, StringBuffer whereSql, ConditionMapper conditionMapper,
			String fieldNamePrefix) {
		int j = -1;
		boolean allNull = true;
		List<Object> multiConditionC = (List<Object>) value;
		for (Object s : multiConditionC) {
			j++;
			if (s != null) {
				if (allNull) {
					allNull = false;
				}
				whereSql.append(POUND_OPENBRACE);
				if (fieldNamePrefix != null) {
					whereSql.append(fieldNamePrefix).append(DOT);
				}
				whereSql.append(conditionMapper.getFieldName()).append(OPENBRACKET).append(j).append(CLOSEBRACKET)
						.append(COMMA).append(JDBCTYPE_EQUAL).append(conditionMapper.getJdbcType().toString());
				if (conditionMapper.getTypeHandlerPath() != null) {
					whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(conditionMapper.getTypeHandlerPath());
				}
				whereSql.append(CLOSEBRACE_COMMA);
			}
		}
		if (!allNull) {
			whereSql.delete(whereSql.lastIndexOf(COMMA), whereSql.lastIndexOf(COMMA) + 1);
		}
		whereSql.append(CLOSEPAREN__AND_BLANK);
	}

	/**
	 * Generate the SQL statement for the update persistent state object from the
	 * incoming object
	 * 
	 * @param object      pojo
	 * @param flyingModel FlyingModel
	 * @return sql
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws IllegalAccessException    Exception
	 * @throws RuntimeException          Exception
	 */
	public static String buildUpdatePersistentSql(Object object, FlyingModel flyingModel)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		String ignoreTag = flyingModel.getIgnoreTag();
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		// updatePersistent causes features that do not require batch update

		String tableName = tableMapper.getTableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(WHERE_BLANK);

		tableSql.append(UPDATE_BLANK).append(tableName).append(BLANK_SET_BLANK);

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			if (!fieldMapper.isUpdateAble() || (fieldMapper.getIgnoreTagSet().contains(ignoreTag))) {
				continue;
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE);
			if (fieldMapper.isForeignKey() || fieldMapper.isCrossDbForeignKey()) {
				tableSql.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
			} else {
				tableSql.append(fieldMapper.getFieldName());
			}
			tableSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
			if (fieldMapper.getTypeHandlerPath() != null) {
				tableSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
			}
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
				throw new BuildSqlException(
						new StringBuffer(BuildSqlExceptionEnum.updatePersistentUniqueKeyIsNull.toString())
								.append(fieldMapper.getDbFieldName()).toString());
			}
			whereSql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA_JDBCTYPE_EQUAL)
					.append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			whereSql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
					.append(CLOSEBRACE_AND_BLANK);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return tableSql.append(whereSql).toString();
	}

	/**
	 * The delete SQL statement is generated from the incoming object
	 * 
	 * @param object Object
	 * @return sql
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws IllegalAccessException    Exception
	 * @throws RuntimeException          Exception
	 */
	public static String buildDeleteSql(Object object)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));
		String tableName = tableMapper.getTableName();
		StringBuffer sql = new StringBuffer();
		sql.append(DELETE_FROM_BLANK).append(tableName).append(WHERE_BLANK);

		// Start processing queryMapper for batch delete
		boolean useBatch = false;
		for (ConditionMapper conditionMapper : queryMapper.getConditionMapperCache().values()) {
			Object value = dtoFieldMap.get(conditionMapper.getFieldName());
			if (value == null) {
				continue;
			}
			dealBatchCondition(conditionMapper.getConditionType(), sql, conditionMapper, value);
			useBatch = true;
		}
		if (!useBatch) {
			for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
				sql.append(fieldMapper.getDbFieldName());
				Object value = dtoFieldMap.get(fieldMapper.getFieldName());
				if (value == null) {
					throw new BuildSqlException(new StringBuffer(BuildSqlExceptionEnum.deleteUniqueKeyIsNull.toString())
							.append(fieldMapper.getDbFieldName()).toString());
				}
				sql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA)
						.append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString())
						.append(CLOSEBRACE_AND_BLANK);
			}
		}
		for (FieldMapper f : tableMapper.getOpVersionLocks()) {
			sql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
					.append(CLOSEBRACE_AND_BLANK);
		}
		sql.delete(sql.lastIndexOf(AND), sql.lastIndexOf(AND) + 3);
		return sql.toString();
	}

	/**
	 * The query SQL statement is generated from the incoming object
	 * 
	 * @param clazz       pojo Class
	 * @param flyingModel FlyingModel
	 * @return sql
	 */
	public static String buildSelectSql(Class<?> clazz, FlyingModel flyingModel)
			throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(clazz));
		StringBuffer selectSql = new StringBuffer(SELECT_BLANK);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_BLANK);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(clazz, null, selectSql, fromSql, whereSql, null, null, null, ai, null,
				flyingModel, null);
		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(tableMapper.getTableName()).append("_0.").append(fieldMapper.getDbFieldName());
			whereSql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA)
					.append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
			if (fieldMapper.getTypeHandlerPath() != null) {
				whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
			}
			whereSql.append(CLOSEBRACE_AND_BLANK);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return selectSql.append(fromSql).append(whereSql).toString();
	}

	/**
	 * The query SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return sql
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws IllegalAccessException    Exception
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws RuntimeException          Exception
	 */
	public static String buildSelectAllSql(Object object, FlyingModel flyingModel)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException,
			NoSuchFieldException, IllegalArgumentException, InstantiationException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		StringBuffer selectSql = new StringBuffer(SELECT_BLANK);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_BLANK);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object.getClass(), object, selectSql, fromSql, whereSql, null, null,
				null, ai, null, flyingModel, null);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_BLANK.equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}
		return selectSql.append(fromSql).append(whereSql).toString();
	}

	/**
	 * The query SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return sql
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws IllegalAccessException    Exception
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws RuntimeException          Exception
	 */
	public static String buildSelectOneSql(Object object, FlyingModel flyingModel)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException,
			NoSuchFieldException, IllegalArgumentException, InstantiationException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}
		if (object instanceof Conditionable) {
			((Conditionable) object).setLimiter(null);
		}
		StringBuffer selectSql = new StringBuffer(SELECT_BLANK);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_BLANK);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object.getClass(), object, selectSql, fromSql, whereSql, null, null,
				null, ai, null, flyingModel, null);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_BLANK.equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}
		return selectSql.append(fromSql).append(whereSql).append(BLANK_LIMIT_1).toString();
	}

	/**
	 * Generate the count SQL statement from the incoming object
	 * 
	 * @param object Object
	 * @return sql
	 * @throws NoSuchMethodException     Exception
	 * @throws InvocationTargetException Exception
	 * @throws IllegalAccessException    Exception
	 * @throws RuntimeException          Exception
	 */
	public static String buildCountSql(Object object)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.nullObject);
		}

		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		AtomicInteger ai = new AtomicInteger(0);
		TableName tableName = new TableName(tableMapper, 0, null);

		StringBuffer selectSql = new StringBuffer();
		selectSql.append(SELECT_COUNT_OPENPAREN).append(tableName.sqlWhere());
		/*
		 * If there is and only one primary key, select count(" primary key "); If there
		 * are no primary keys or multiple primary keys (co-primary keys), select
		 * count(*).
		 * 
		 */
		if (tableMapper.getUniqueKeyNames().length == 1) {
			selectSql.append(tableMapper.getUniqueKeyNames()[0].getDbFieldName());
		} else {
			selectSql.append(ASTERISK);
		}
		selectSql.append(CLOSEPAREN);

		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_BLANK);

		dealMapperAnnotationIterationForCount(object, fromSql, whereSql, null, null, null, ai, tableName);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_BLANK.equals(whereSql.toString())) {
			whereSql = new StringBuffer();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}

		return selectSql.append(fromSql).append(whereSql).toString();
	}

	@SuppressWarnings("unchecked")
	private static void dealMapperAnnotationIterationForSelectAll(Class<?> objectType, Object object,
			StringBuffer selectSql, StringBuffer fromSql, StringBuffer whereSql, TableName originTableName,
			Mapperable originFieldMapper, String fieldPerfix, AtomicInteger index, TableName lastTableName,
			FlyingModel flyingModel, Map<Mapperable, Integer> map)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException,
			NoSuchFieldException, IllegalArgumentException, InstantiationException {
		String ignoreTag = null;
		String prefix = null;
		if (flyingModel != null) {
			ignoreTag = flyingModel.getIgnoreTag();
			prefix = flyingModel.getPrefix();
		}
		Map<Object, Object> dtoFieldMap = object == null ? new HashMap<>(4) : PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(objectType));
		QueryMapper queryMapper = buildQueryMapper(objectType, getTableMappedClass(objectType));
		TableName tableName = null;

		int indexValue = (map == null || map.get(originFieldMapper) == null) ? (index.getAndIncrement())
				: (map.get(originFieldMapper));
		if (lastTableName == null) {
			tableName = new TableName(tableMapper, indexValue, null);
		} else {
			tableName = new TableName(tableMapper, indexValue, lastTableName.getMap());
		}
		/*
		 * If originFieldMapper is null, it is considered to be the first traversal. In
		 * the first iteration, handle fromSql.
		 * 
		 */
		if (originFieldMapper == null) {
			fromSql.append(tableName.sqlSelect());
		}

		if (flyingModel != null) {
			for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
				FlyingModel inner = flyingModel.getProperties().get(fieldMapper.getFieldName());
				if (inner != null) {
					if (dtoFieldMap.get(fieldMapper.getFieldName()) == null) {
						dtoFieldMap.put(fieldMapper.getFieldName(), fieldMapper.getFieldType().newInstance());
					}
					TableMapper innerTableMapper = buildTableMapper(fieldMapper.getFieldType());
					int indexValue2 = index.getAndIncrement();
					if (map == null) {
						map = new HashMap<Mapperable, Integer>(2);
					}
					map.put(fieldMapper, indexValue2);
					for (Map.Entry<String, FieldMapper> e : innerTableMapper.getFieldMapperCache().entrySet()) {
						if (fieldMapper.getFieldName().equals(e.getValue().getFieldName())
								&& (!e.getValue().getIgnoreTagSet().contains(inner.getIgnoreTag()))) {
							selectSql.append(innerTableMapper.getTableName()).append("_").append(indexValue2)
									.append(DOT).append(e.getValue().getDbFieldName()).append(" as ")
									.append(inner.getPrefix()).append(e.getValue().getDbFieldName()).append(COMMA);
						}
					}
				}
				if ((!fieldMapper.getIgnoreTagSet().contains(ignoreTag))) {
					selectSql.append(tableName.sqlWhere()).append(fieldMapper.getDbFieldName());
					if (prefix != null) {
						selectSql.append(" as ").append(prefix).append(fieldMapper.getDbFieldName());
					}
					selectSql.append(COMMA);
				}
			}
		}

		/*
		 * If the originFieldMapper and originTableName are not null, it can be
		 * considered a non-first traversal.In the non-first traversal, process
		 * fieldPerfix and fromSql.
		 * 
		 */
		String temp = null;
		if (originFieldMapper != null && originTableName != null) {
			/* Processing fieldPerfix */
			temp = originFieldMapper.getFieldName();
			if (fieldPerfix != null) {
				temp = fieldPerfix + DOT + temp;
			}
			/* Processing fromSql */
			fromSql.append(originFieldMapper.getAssociationType().value()).append(tableName.sqlSelect())
					.append(BLANK_ON_BLANK).append(originTableName.sqlWhere())
					.append(originFieldMapper.getDbFieldName()).append(BLANK_EQUAL_BLANK).append(tableName.sqlWhere())
					.append(originFieldMapper.getDbAssociationUniqueKey());
			ForeignAssociationMapper[] fams = originFieldMapper.getForeignAssociationMappers();
			if (fams != null && fams.length > 0) {
				for (ForeignAssociationMapper fam : fams) {
					fromSql.append(BLANK_AND_BLANK).append(originTableName.sqlWhere()).append(fam.getDbFieldName())
							.append(fam.getCondition().value()).append(tableName.sqlWhere())
							.append(fam.getDbAssociationFieldName());
				}
			}
		}

		/* Handle the conditions in the fieldMapper */
		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}
			if (fieldMapper.isForeignKey()) {
				dealMapperAnnotationIterationForSelectAll(value.getClass(), value, selectSql, fromSql, whereSql,
						tableName, fieldMapper, temp, index, tableName,
						flyingModel == null ? (null) : (flyingModel.getProperties().get(fieldMapper.getFieldName())),
						map);
			} else {
				dealConditionEqual(whereSql, fieldMapper, tableName, temp, false, 0);
			}
		}

		/* Handle the "and" condition in the queryMapper */
		for (ConditionMapper conditionMapper : queryMapper.getConditionMapperCache().values()) {
			Object value = dtoFieldMap.get(conditionMapper.getFieldName());
			if (value == null) {
				continue;
			}
			dealConditionMapper(conditionMapper, value, whereSql, tableName, temp, false, 0);
		}

		/* Handle the "or" condition in the queryMapper */
		for (OrMapper orMapper : queryMapper.getOrMapperCache().values()) {
			Object value = dtoFieldMap.get(orMapper.getFieldName());
			if (value == null) {
				continue;
			}
			dealConditionOrMapper(orMapper, value, whereSql, tableName, temp);
		}
	}

	private static void dealConditionOrMapper(OrMapper orMapper, Object value, StringBuffer whereSql,
			TableName tableName, String temp) {
		ConditionMapper[] conditionMappers = orMapper.getConditionMappers();
		Object[] os = (Object[]) value;
		int i = 0;
		whereSql.append("(");
		for (ConditionMapper cm : conditionMappers) {
			dealConditionMapper(cm, os[i], whereSql, tableName, temp, true, i);
			i++;
		}
		whereSql.delete(whereSql.lastIndexOf(BLANK_OR_BLANK), whereSql.lastIndexOf(BLANK_OR_BLANK) + 4)
				.append(") and ");
	}

	private static void dealConditionMapper(ConditionMapper conditionMapper, Object value, StringBuffer whereSql,
			TableName tableName, String temp, boolean isOr, int i) {
		switch (conditionMapper.getConditionType()) {
		case Equal:
			dealConditionEqual(whereSql, conditionMapper, tableName, temp, isOr, i);
			break;
		case Like:
			dealConditionLike(whereSql, conditionMapper, ConditionType.Like, tableName, temp, isOr, i);
			break;
		case HeadLike:
			dealConditionLike(whereSql, conditionMapper, ConditionType.HeadLike, tableName, temp, isOr, i);
			break;
		case TailLike:
			dealConditionLike(whereSql, conditionMapper, ConditionType.TailLike, tableName, temp, isOr, i);
			break;
		case GreaterThan:
			dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GreaterThan, tableName, temp, isOr, i);
			break;
		case GreaterOrEqual:
			dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GreaterOrEqual, tableName, temp, isOr, i);
			break;
		case LessThan:
			dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LessThan, tableName, temp, isOr, i);
			break;
		case LessOrEqual:
			dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LessOrEqual, tableName, temp, isOr, i);
			break;
		case NotEqual:
			dealConditionNotEqual(whereSql, conditionMapper, ConditionType.NotEqual, tableName, temp, isOr, i);
			break;
		case MultiLikeAND:
			dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MultiLikeAND, tableName, temp, isOr);
			break;
		case MultiLikeOR:
			dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MultiLikeOR, tableName, temp, isOr);
			break;
		case In:
			dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.In, tableName, temp, isOr);
			break;
		case NotIn:
			dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.NotIn, tableName, temp, isOr);
			break;
		case NullOrNot:
			dealConditionNullOrNot(value, whereSql, conditionMapper, tableName, temp, isOr);
			break;
		default:
			break;
		}
	}

	private static void dealMapperAnnotationIterationForCount(Object object, StringBuffer fromSql,
			StringBuffer whereSql, TableName originTableName, Mapperable originFieldMapper, String fieldPerfix,
			AtomicInteger index, TableName lastTableName)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<?, ?> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));
		TableName tableName = new TableName(tableMapper, index.getAndIncrement(), lastTableName.getMap());

		/*
		 * If originFieldMapper is null, it is considered to be the first traversal.In
		 * the first iteration, handle fromSql.
		 * 
		 */
		if (originFieldMapper == null) {
			fromSql.append(tableName.sqlSelect());
		}

		/*
		 * If the originFieldMapper and originTableName are not null, it can be
		 * considered a non-first traversal. In the non-first traversal, process
		 * fieldPerfix and fromSql.
		 * 
		 */
		String temp = null;
		if (originFieldMapper != null && originTableName != null) {
			/* Processing fieldPerfix */
			temp = originFieldMapper.getFieldName();
			if (fieldPerfix != null) {
				temp = fieldPerfix + DOT + temp;
			}
			/* Processing fromSql */
			fromSql.append(originFieldMapper.getAssociationType().value()).append(tableName.sqlSelect())
					.append(BLANK_ON_BLANK).append(originTableName.sqlWhere())
					.append(originFieldMapper.getDbFieldName()).append(BLANK_EQUAL_BLANK).append(tableName.sqlWhere())
					.append(originFieldMapper.getDbAssociationUniqueKey());
			ForeignAssociationMapper[] fams = originFieldMapper.getForeignAssociationMappers();
			if (fams != null && fams.length > 0) {
				for (ForeignAssociationMapper fam : fams) {
					fromSql.append(BLANK_AND_BLANK).append(originTableName.sqlWhere()).append(fam.getDbFieldName())
							.append(fam.getCondition().value()).append(tableName.sqlWhere())
							.append(fam.getDbAssociationFieldName());
				}
			}
		}

		/* Handle the conditions in the fieldMapper */
		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}
			if (fieldMapper.isForeignKey()) {
				dealMapperAnnotationIterationForCount(value, fromSql, whereSql, tableName, fieldMapper, temp, index,
						tableName);
			} else {
				dealConditionEqual(whereSql, fieldMapper, tableName, temp, false, 0);
			}
		}

		/*
		 * Handle the "and" condition in the queryMapper
		 */
		for (ConditionMapper conditionMapper : queryMapper.getConditionMapperCache().values()) {
			Object value = dtoFieldMap.get(conditionMapper.getFieldName());
			if (value == null) {
				continue;
			}
			dealConditionMapper(conditionMapper, value, whereSql, tableName, temp, false, 0);
		}
		/*
		 * Handle the "or" condition in the queryMapper
		 */
		for (OrMapper orMapper : queryMapper.getOrMapperCache().values()) {
			Object value = dtoFieldMap.get(orMapper.getFieldName());
			if (value == null) {
				continue;
			}
			dealConditionOrMapper(orMapper, value, whereSql, tableName, temp);
		}
	}

}
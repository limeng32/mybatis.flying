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
 * Generate SQL through annotations
 * 
 * @author david, limeng32
 * 
 */
public class SqlBuilder {

	/* Cache TableMapper */
	private static Map<Class<?>, TableMapper> tableMapperCache = new ConcurrentHashMap<Class<?>, TableMapper>(128);
	/* Cache QueryMapper */
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
	private static final String COMMA_JAVATYPE_EQUAL = ",javaType=";
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
		whereSql.append(_LIKE__POUND_OPENBRACE);
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
			whereSql.append(CLOSEBRACE_OR_);
		} else {
			whereSql.append(CLOSEBRACE_AND_);
		}
	}

	private static void dealConditionInOrNot(Object value, StringBuffer whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix, boolean isOr) {
		if (isOr) {
			throw new BuildSqlException(BuildSqlExceptionEnum.ThisConditionNotSupportOr);
		}
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
					tempWhereSql.append(conditionMapper.getFieldName()).append(OPENBRACKET).append(j)
							.append(CLOSEBRACKET).append(COMMA).append(JDBCTYPE_EQUAL)
							.append(conditionMapper.getJdbcType().toString());
					if (conditionMapper.getTypeHandlerPath() != null) {
						tempWhereSql.append(COMMA_TYPEHANDLER_EQUAL).append(conditionMapper.getTypeHandlerPath());
					}
					tempWhereSql.append(CLOSEBRACE_COMMA);
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
			ConditionType type, TableName tableName, String fieldNamePrefix, boolean isOr) {
		if (isOr) {
			throw new BuildSqlException(BuildSqlExceptionEnum.ThisConditionNotSupportOr);
		}
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
			whereSql.append(COMMA_JAVATYPE_EQUAL).append(mapper.getFieldType().getName()).append(CLOSEBRACE_OR_);
		} else {
			whereSql.append(CLOSEBRACE_AND_);
		}
	}

	private static void dealConditionNotEqual(StringBuffer whereSql, Mapperable mapper, ConditionType type,
			TableName tableName, String fieldNamePrefix, boolean isOr, int i) {
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
			whereSql.append(COMMA_JAVATYPE_EQUAL).append(mapper.getFieldType().getName()).append(CLOSEBRACE_OR_);
		} else {
			whereSql.append(CLOSEBRACE_AND_);
		}
	}

	private static void dealConditionNullOrNot(Object value, StringBuffer whereSql, Mapperable mapper,
			TableName tableName, String fieldNamePrefix, boolean isOr) {
		Boolean isNull = (Boolean) value;
		handleWhereSql(whereSql, mapper, tableName, fieldNamePrefix);
		whereSql.append(_IS);
		if (!isNull) {
			whereSql.append(_NOT);
		}
		whereSql.append(_NULL);
		if (isOr) {
			whereSql.append(_OR_);
		} else {
			whereSql.append(_AND_);
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

		tableSql.append(INSERT_INTO_).append(tableName).append(_OPENPAREN);
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
		return tableSql.append(CLOSEPAREN_).append(valueSql).append(CLOSEPAREN).toString();
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

		String tableName = tableMapper.getTableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(WHERE_);

		tableSql.append(UPDATE_).append(tableName).append(_SET_);

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

		String tableName = tableMapper.getTableName();

		StringBuffer tableSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer(WHERE_);

		tableSql.append(UPDATE_).append(tableName).append(_SET_);

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
		String tableName = tableMapper.getTableName();

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
	 * The query SQL statement is generated from the incoming object
	 * 
	 * @param clazz       pojo Class
	 * @param flyingModel FlyingModel
	 * @return sql
	 */
	public static String buildSelectSql(Class<?> clazz, FlyingModel flyingModel) {
		String ignoreTag = flyingModel.getIgnoreTag();
		String prefix = flyingModel.getPrefix();
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(clazz));
		String tableName = tableMapper.getTableName();

		StringBuffer selectSql = new StringBuffer(SELECT_);

		for (Mapperable fieldMapper : tableMapper.getFieldMapperCache().values()) {
			if ((!fieldMapper.getIgnoreTagSet().contains(ignoreTag))) {
				selectSql.append(fieldMapper.getDbFieldName());
				if (prefix != null) {
					selectSql.append(" as ").append(prefix).append(fieldMapper.getDbFieldName());
				}
				selectSql.append(COMMA);
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
					.append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
			if (fieldMapper.getTypeHandlerPath() != null) {
				whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
			}
			whereSql.append(CLOSEBRACE_AND_);
		}
		whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		return selectSql.append(whereSql).toString();
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
		StringBuffer selectSql = new StringBuffer(SELECT_);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object, selectSql, fromSql, whereSql, null, null, null, ai, null,
				flyingModel, null);

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
		StringBuffer selectSql = new StringBuffer(SELECT_);
		StringBuffer fromSql = new StringBuffer(FROM);
		StringBuffer whereSql = new StringBuffer(WHERE_);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(object, selectSql, fromSql, whereSql, null, null, null, ai, null,
				flyingModel, null);

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
		StringBuffer whereSql = new StringBuffer(WHERE_);

		dealMapperAnnotationIterationForCount(object, fromSql, whereSql, null, null, null, ai, tableName);

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

	@SuppressWarnings("unchecked")
	private static void dealMapperAnnotationIterationForSelectAll(Object object, StringBuffer selectSql,
			StringBuffer fromSql, StringBuffer whereSql, TableName originTableName, Mapperable originFieldMapper,
			String fieldPerfix, AtomicInteger index, TableName lastTableName, FlyingModel flyingModel,
			Map<Mapperable, Integer> map)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException,
			NoSuchFieldException, IllegalArgumentException, InstantiationException {
		String ignoreTag = null;
		String prefix = null;
		if (flyingModel != null) {
			ignoreTag = flyingModel.getIgnoreTag();
			prefix = flyingModel.getPrefix();
		}
		Map<Object, Object> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));
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
						selectSql.append(innerTableMapper.getTableName()).append("_").append(indexValue2).append(DOT)
								.append(e.getValue().getDbFieldName()).append(" as ").append(inner.getPrefix())
								.append(e.getValue().getDbFieldName()).append(COMMA);
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
			fromSql.append(originFieldMapper.getAssociationType().value()).append(tableName.sqlSelect()).append(_ON_)
					.append(originTableName.sqlWhere()).append(originFieldMapper.getDbFieldName()).append(_EQUAL_)
					.append(tableName.sqlWhere()).append(originFieldMapper.getDbAssociationUniqueKey());
			ForeignAssociationMapper[] fams = originFieldMapper.getForeignAssociationMappers();
			if (fams != null && fams.length > 0) {
				for (ForeignAssociationMapper fam : fams) {
					fromSql.append(_AND_).append(originTableName.sqlWhere()).append(fam.getDbFieldName())
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
				dealMapperAnnotationIterationForSelectAll(value, selectSql, fromSql, whereSql, tableName, fieldMapper,
						temp, index, tableName,
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
		whereSql.delete(whereSql.lastIndexOf(_OR_), whereSql.lastIndexOf(_OR_) + 4).append(") and ");
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
			fromSql.append(originFieldMapper.getAssociationType().value()).append(tableName.sqlSelect()).append(_ON_)
					.append(originTableName.sqlWhere()).append(originFieldMapper.getDbFieldName()).append(_EQUAL_)
					.append(tableName.sqlWhere()).append(originFieldMapper.getDbAssociationUniqueKey());
			ForeignAssociationMapper[] fams = originFieldMapper.getForeignAssociationMappers();
			if (fams != null && fams.length > 0) {
				for (ForeignAssociationMapper fam : fams) {
					fromSql.append(_AND_).append(originTableName.sqlWhere()).append(fam.getDbFieldName())
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
package indi.mybatis.flying.builders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.exception.AutoMapperException;
import indi.mybatis.flying.exception.AutoMapperExceptionEnum;
import indi.mybatis.flying.exception.BuildSqlException;
import indi.mybatis.flying.exception.BuildSqlExceptionEnum;
import indi.mybatis.flying.models.AggregateModel;
import indi.mybatis.flying.models.ConditionMapper;
import indi.mybatis.flying.models.FieldMapper;
import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.models.ForeignAssociationMapper;
import indi.mybatis.flying.models.Mapperable;
import indi.mybatis.flying.models.OrMapper;
import indi.mybatis.flying.models.QueryMapper;
import indi.mybatis.flying.models.TableMapper;
import indi.mybatis.flying.models.TableName;
import indi.mybatis.flying.pagination.Order;
import indi.mybatis.flying.statics.ConditionType;
import indi.mybatis.flying.statics.HandlerPaths;
import indi.mybatis.flying.statics.OpLockType;
import indi.mybatis.flying.type.KeyHandler;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author david, 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Generate SQL through annotations.
 */
public class SqlBuilder {

	private SqlBuilder() {

	}

	/* Cache TableMapper */
	private static final Map<Class<?>, TableMapper> tableMapperCache = new ConcurrentHashMap<Class<?>, TableMapper>(
			128);
	/* Cache QueryMapper */
	private static final Map<Class<?>, QueryMapper> queryMapperCache = new ConcurrentHashMap<Class<?>, QueryMapper>(
			128);

	private static String collectionObjectClassName = null;

	private static final String DOT = ".";
	private static final String COMMA = ",";
	private static final String CONCAT_WS_OPENPAREN = "CONCAT_WS('',";
	private static final String JDBCTYPE_EQUAL = "jdbcType=";
	private static final String COMMA_TYPEHANDLER_EQUAL = ",typeHandler=";
	private static final String CLOSEPAREN_BLANK = ") ";
	private static final String CLOSEBRACE_AND_BLANK = "} and ";
	private static final String CLOSEBRACE_OR_BLANK = "} or ";
	private static final String WHERE_BLANK = " where ";
	private static final String GROUP_BY_BLANK = " group by ";
	private static final String POUND_OPENBRACE = "#{";
	private static final String OPENBRACKET = "[";
	private static final String CLOSEBRACKET = "]";
	private static final String CLOSEBRACKET_DOT = "].";
	private static final String CLOSEBRACE_COMMA = "},";
	private static final String CLOSEPAREN_BLANK_AND_BLANK = ") and ";
	private static final String COLLECTION = "collection";
	private static final String COLLECTION_OPENBRACKET = "collection[";
	private static final String AND = "and";
	private static final String FROM = " from ";
	private static final String WHEN = " when ";
	private static final String THEN = " then ";
	private static final String ELSE = " else ";
	private static final String END = " end ";
	private static final String CLOSEPAREN = ")";
	private static final String CLOSEPAREN_CLOSEPAREN = "))";
	private static final String DEFAULT_COMMA = " default,";
	private static final String ASTERISK = "*";
	private static final String INSERT_INTO_BLANK = "insert into ";
	private static final String SELECT_BLANK = "select ";
	private static final String SELECT_COUNT_OPENPAREN = "select count(";
	private static final String EQUAL_POUND_OPENBRACE = "=#{";
	private static final String EQUAL = "=";
	private static final String DELETE_FROM_BLANK = "delete from ";
	private static final String COMMA_JAVATYPE_EQUAL = ",javaType=";
	private static final String COMMA_JDBCTYPE_EQUAL = ",jdbcType=";
	private static final String PLUS_1 = "+1";
	private static final String CLOSEBRACE = "}";
	private static final String CONDITIONLIKEHANDLER = "ConditionLikeHandler";
	private static final String VALUES_OPENPAREN = "values(";
	private static final String UPDATE_BLANK = "update ";
	private static final String NULL = "null";
	private static final String BLANK_AND_BLANK = " and ";
	private static final String BLANK_EQUAL_BLANK = " = ";
	private static final String BLANK_GREATER_BLANK = " > ";
	private static final String BLANK_GREATER_EQUAL_BLANK = " >= ";
	private static final String BLANK_IN_OPENPAREN = " in(";
	private static final String BLANK_IS = " is";
	private static final String BLANK_LESS_BLANK = " < ";
	private static final String BLANK_LESS_EQUAL_BLANK = " <= ";
	private static final String BLANK_LESS_GREATER_BLANK = " <> ";
	private static final String BLANK_LIKE_BLANK_POUND_OPENBRACE = " like #{";
	private static final String BLANK_LIMIT_1 = " limit 1";
	private static final String BLANK_NOT = " not";
	private static final String BLANK_NULL = " null";
	private static final String BLANK_ON_BLANK = " on ";
	private static final String BLANK_OPENPAREN = " (";
	private static final String BLANK_OR_BLANK = " or ";
	private static final String BLANK_SET_BLANK = " set ";
	private static final String AES_ENCRYPT_OPENPAREN = "aes_encrypt(";
	private static final String AES_DECRYPT_OPENPAREN = "aes_decrypt(";
	private static final String CLOSEPAREN_COMMA = "),";

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
		fieldMapperCache = new ConcurrentHashMap<String, FieldMapper>(16);
		for (Field field : fields) {
			fieldMapper = new FieldMapper();
			boolean b = fieldMapper.buildMapper(field);
			if (!b) {
				continue;
			}
			if (OpLockType.VERSION.equals(fieldMapper.getOpLockType()) && !fieldMapper.isDelegate()) {
				fieldMapper.setOpVersionLock(true);
			}
			if (fieldMapper.isUniqueKey() && !fieldMapper.isDelegate()) {
				uniqueKeyList.add(fieldMapper);
			}

			if (fieldMapper.getIgnoreTag().length > 0) {
				for (String t : fieldMapper.getIgnoreTag()) {
					fieldMapper.getIgnoreTagSet().add(t);
				}
			}

			if (fieldMapper.getWhiteListTag().length > 0) {
				for (String t : fieldMapper.getWhiteListTag()) {
					fieldMapper.getWhiteListTagSet().add(t);
				}
			}

			if (!"".equals(fieldMapper.getDbAssociationUniqueKey()) && !fieldMapper.isDelegate()) {
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

			if (fieldMapper.isOpVersionLock()) {
				opVersionLockList.add(fieldMapper);
			}
			// 需考虑delegate情况
			if (fieldMapperCache.containsKey(fieldMapper.getDbFieldName())) {
				FieldMapper temp = fieldMapperCache.get(fieldMapper.getDbFieldName());
				if (!temp.isDelegate() && fieldMapper.isDelegate()) {
					temp.setDelegate(fieldMapper);
					temp.setHasDelegate(true);
				} else if (temp.isDelegate() && !fieldMapper.isDelegate()) {
					fieldMapper.setDelegate(temp);
					fieldMapper.setHasDelegate(true);
					fieldMapperCache.put(fieldMapper.getDbFieldName(), fieldMapper);
				}
			} else {
				fieldMapperCache.put(fieldMapper.getDbFieldName(), fieldMapper);
			}
		}
		// 处理cryptKeyField
		for (Map.Entry<String, FieldMapper> e : fieldMapperCache.entrySet()) {
			String[] cryptKeyColumn = e.getValue().getCryptKeyColumn();
			if (cryptKeyColumn != null) {
				FieldMapper[] cryptKeyField = new FieldMapper[cryptKeyColumn.length];
				e.getValue().setCryptKeyField(cryptKeyField);
				int i = 0;
				for (String s : cryptKeyColumn) {
					if (fieldMapperCache.containsKey(s)) {
						FieldMapper cryptKeyFieldMapper = fieldMapperCache.get(s);
						cryptKeyField[i] = cryptKeyFieldMapper;
					}
					i++;
				}
			}
		}
		tableMapper.setFieldMapperCache(fieldMapperCache);
		tableMapper.setUniqueKeyNames(uniqueKeyList.toArray(new FieldMapper[uniqueKeyList.size()]));
		if (!uniqueKeyList.isEmpty()) {
			tableMapper.setUniqueKey(uniqueKeyList.get(0));
		} else {
			tableMapper.setUniqueKey(new FieldMapper());
		}
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

		// 处理cryptKeyField
		for (Map.Entry<String, ConditionMapper> e : conditionMapperCache.entrySet()) {
			String[] cryptKeyColumn = e.getValue().getCryptKeyColumn();
			if (cryptKeyColumn != null) {
				TableMapper tableMapper = tableMapperCache.get(getTableMappedClass(dtoClass));
				FieldMapper[] cryptKeyField = new FieldMapper[cryptKeyColumn.length];
				e.getValue().setCryptKeyField(cryptKeyField);
				int i = 0;
				for (String s : cryptKeyColumn) {
					if (tableMapper.getFieldMapperCache().containsKey(s)) {
						FieldMapper cryptKeyFieldMapper = tableMapper.getFieldMapperCache().get(s);
						cryptKeyField[i] = cryptKeyFieldMapper;
					}
					i++;
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
		conditionMapper.setDelegate(conditionMapperAnnotation.delegate());
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
					conditionMapper.setCryptKeyColumn(fieldMapper.getCryptKeyColumn());
					conditionMapper.setCryptKeyAddition(fieldMapper.getCryptKeyAddition());
					conditionMapper.setDbFieldNameForJoin(fieldMapper.getDbFieldNameForJoin());
					if (!"".equals(fieldMapper.getDbAssociationUniqueKey())) {
						conditionMapper.setDbAssociationUniqueKey(fieldMapper.getDbAssociationUniqueKey());
						conditionMapper.setForeignKey(true);
					}
					if (conditionMapper.isForeignKey()
							&& (!ConditionType.NULL_OR_NOT.equals(conditionMapper.getConditionType()))
							&& !fieldMapper.isDelegate()) {
						if (!tableMapperCache.containsKey(pojoField.getType())) {
							buildTableMapper(pojoField.getType());
						}
						TableMapper tm = tableMapperCache.get(pojoField.getType());
						String foreignFieldName = getFieldMapperByDbFieldName(tm.getFieldMapperCache(),
								fieldMapper.getDbAssociationUniqueKey()).getFieldName();
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
			throw new BuildSqlException(new StringBuilder(BuildSqlExceptionEnum.NO_TABLE_MAPPER_ANNOTATION.toString())
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

	private static void dealCryptKeyAddition(boolean b, StringBuilder stringBuilder, Mapperable mapper) {
		if (mapper.getCryptKeyAddition() != null) {
			if (b) {
				stringBuilder.append(COMMA);
			}
			stringBuilder.append(mapper.getCryptKeyAddition());
		}
	}

	private static void dealConditionLike(StringBuilder whereSql, ConditionMapper conditionMapper, ConditionType type,
			TableName tableName, String fieldNamePrefix, boolean isOr, int i) {
		handleWhereSql(whereSql, conditionMapper, tableName);
		switch (type) {
			case NOT_LIKE:
				whereSql.append(BLANK_NOT);
				break;
			case NOT_HEAD_LIKE:
				whereSql.append(BLANK_NOT);
				break;
			case NOT_TAIL_LIKE:
				whereSql.append(BLANK_NOT);
				break;
			default:
		}
		whereSql.append(BLANK_LIKE_BLANK_POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (conditionMapper.isForeignKey() && !conditionMapper.isDelegate()) {
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
			case LIKE:
				whereSql.append(HandlerPaths.CONDITION_LIKE_HANDLER_PATH);
				break;
			case NOT_LIKE:
				whereSql.append(HandlerPaths.CONDITION_LIKE_HANDLER_PATH);
				break;
			case HEAD_LIKE:
				whereSql.append(HandlerPaths.CONDITION_HEAD_LIKE_HANDLER_PATH);
				break;
			case NOT_HEAD_LIKE:
				whereSql.append(HandlerPaths.CONDITION_HEAD_LIKE_HANDLER_PATH);
				break;
			case TAIL_LIKE:
				whereSql.append(HandlerPaths.CONDITION_TAIL_LIKE_HANDLER_PATH);
				break;
			case NOT_TAIL_LIKE:
				whereSql.append(HandlerPaths.CONDITION_TAIL_LIKE_HANDLER_PATH);
				break;
			default:
				throw new BuildSqlException(BuildSqlExceptionEnum.AMBIGUOUS_CONDITION);
		}
		if (isOr) {
			whereSql.append(CLOSEBRACE_OR_BLANK);
		} else {
			whereSql.append(CLOSEBRACE_AND_BLANK);
		}
	}

	private static void dealConditionInOrNot(Object value, StringBuilder whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix, boolean isOr) {
		if (isOr) {
			throw new BuildSqlException(BuildSqlExceptionEnum.THIS_CONDITION_NOT_SUPPORT_OR);
		}
		switch (type) {
			case IN:
				dealWhereSqlOfIn(value, whereSql, conditionMapper, fieldNamePrefix, false, tableName);
				break;
			case NOT_IN:
				dealWhereSqlOfIn(value, whereSql, conditionMapper, fieldNamePrefix, true, tableName);
				break;
			default:
				throw new BuildSqlException(BuildSqlExceptionEnum.AMBIGUOUS_CONDITION);
		}
	}

	@SuppressWarnings("unchecked")
	private static void dealConditionMultiLike(Object value, StringBuilder whereSql, ConditionMapper conditionMapper,
			ConditionType type, TableName tableName, String fieldNamePrefix, boolean isOr) {
		if (isOr) {
			throw new BuildSqlException(BuildSqlExceptionEnum.THIS_CONDITION_NOT_SUPPORT_OR);
		}
		List<String> multiConditionList = (List<String>) value;
		if (!multiConditionList.isEmpty()) {
			StringBuilder tempWhereSql = new StringBuilder();
			tempWhereSql.append(BLANK_OPENPAREN);
			int j = -1;
			boolean allNull = true;
			for (String s : multiConditionList) {
				j++;
				if (s != null) {
					if (allNull) {
						allNull = false;
					}
					handleWhereSql(tempWhereSql, conditionMapper, tableName);
					tempWhereSql.append(BLANK_LIKE_BLANK_POUND_OPENBRACE);
					if (fieldNamePrefix != null) {
						tempWhereSql.append(fieldNamePrefix).append(DOT);
					}
					if (conditionMapper.isForeignKey() && !conditionMapper.isDelegate()) {
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
						case MULTI_LIKE_AND:
							tempWhereSql.append(CONDITIONLIKEHANDLER);
							tempWhereSql.append(CLOSEBRACE_AND_BLANK);
							break;
						case MULTI_LIKE_OR:
							tempWhereSql.append(CONDITIONLIKEHANDLER);
							tempWhereSql.append(CLOSEBRACE_OR_BLANK);
							break;
						default:
							throw new BuildSqlException(BuildSqlExceptionEnum.AMBIGUOUS_CONDITION);
					}
				}
			}
			if (!allNull) {
				switch (type) {
					case MULTI_LIKE_AND:
						tempWhereSql.delete(tempWhereSql.lastIndexOf(BLANK_AND_BLANK),
								tempWhereSql.lastIndexOf(BLANK_AND_BLANK) + 5);
						break;
					case MULTI_LIKE_OR:
						tempWhereSql.delete(tempWhereSql.lastIndexOf(BLANK_OR_BLANK),
								tempWhereSql.lastIndexOf(BLANK_OR_BLANK) + 4);
						break;
					default:
				}
				tempWhereSql.append(CLOSEPAREN_BLANK_AND_BLANK);
				whereSql.append(tempWhereSql);
			}
		}
	}

	private static void dealConditionEqual(StringBuilder whereSql, Mapperable mapper, TableName tableName,
			String fieldNamePrefix, boolean isOr, int i) {
		handleWhereSql(whereSql, mapper, tableName);
		whereSql.append(EQUAL_POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (mapper.isForeignKey() && !mapper.isDelegate()) {
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

	private static void dealConditionNotEqual(StringBuilder whereSql, Mapperable mapper, ConditionType type,
			TableName tableName, String fieldNamePrefix, boolean isOr, int i) {
		handleWhereSql(whereSql, mapper, tableName);
		switch (type) {
			case GREATER_THAN:
				whereSql.append(BLANK_GREATER_BLANK);
				break;
			case GREATER_OR_EQUAL:
				whereSql.append(BLANK_GREATER_EQUAL_BLANK);
				break;
			case LESS_THAN:
				whereSql.append(BLANK_LESS_BLANK);
				break;
			case LESS_OR_EQUAL:
				whereSql.append(BLANK_LESS_EQUAL_BLANK);
				break;
			case NOT_EQUAL:
				whereSql.append(BLANK_LESS_GREATER_BLANK);
				break;
			default:
				throw new BuildSqlException(BuildSqlExceptionEnum.AMBIGUOUS_CONDITION);
		}
		whereSql.append(POUND_OPENBRACE);
		if (fieldNamePrefix != null) {
			whereSql.append(fieldNamePrefix).append(DOT);
		}
		if (mapper.isForeignKey() && !mapper.isDelegate()) {
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

	private static void dealConditionNullOrNot(Object value, StringBuilder whereSql, Mapperable mapper,
			TableName tableName, boolean isOr) {
		Boolean isNull = (Boolean) value;
		handleWhereSql(whereSql, mapper, tableName);
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

	private static void handleWhereSql(StringBuilder whereSql, Mapperable mapper, TableName tableName) {
		if (mapper.getCryptKeyField() != null) {
			whereSql.append(AES_DECRYPT_OPENPAREN);
		}
		handleWhereSqlTableName(whereSql, mapper, tableName);
		whereSql.append(mapper.getDbFieldName());
		if (mapper.getCryptKeyField() != null) {
			whereSql.append(COMMA).append(CONCAT_WS_OPENPAREN);
			boolean b = false;
			for (String e : mapper.getCryptKeyColumn()) {
				if (b) {
					whereSql.append(COMMA);
				} else {
					b = true;
				}
				handleWhereSqlTableName(whereSql, mapper, tableName);
				whereSql.append(e);
			}
			dealCryptKeyAddition(b, whereSql, mapper);
			whereSql.append(CLOSEPAREN_CLOSEPAREN);
		}
	}

	private static void handleWhereSqlTableName(StringBuilder whereSql, Mapperable mapper, TableName tableName) {
		if (tableName != null) {
			if (mapper.getSubTarget() == null || Void.class.equals(mapper.getSubTarget())) {
				whereSql.append(tableName.sqlWhere());
			} else {
				TableName temp = tableName.getMap().get(mapper.getSubTarget());
				whereSql.append(new StringBuilder(temp.getTableMapper().getTableName()).append("_")
						.append(temp.getIndex()).append("."));
			}
		}
	}

	/**
	 * The insert SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return String
	 * @throws NoSuchFieldException      Exception
	 * @throws IllegalAccessException    Exception
	 * @throws InvocationTargetException Exception
	 * @throws NoSuchMethodException     Exception
	 */
	public static String buildInsertSql(Object object, FlyingModel flyingModel)
			throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String ignoreTag = flyingModel.getIgnoreTag();
		KeyHandler keyHandler = flyingModel.getKeyHandler();
		String whiteListTag = flyingModel.getWhiteListTag();
		boolean useWhiteList = whiteListTag == null ? false : true;
		Map<String, Object> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		String tableName = tableMapper.getTableName();
		StringBuilder tableSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();

		tableSql.append(INSERT_INTO_BLANK).append(tableName).append(BLANK_OPENPAREN);
		valueSql.append(VALUES_OPENPAREN);

		boolean allFieldNull = true;
		boolean uniqueKeyHandled = false;
		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());

			if (value == null && fieldMapper.isHasDelegate()) {
				value = dtoFieldMap.get(fieldMapper.getDelegate().getFieldName());
				fieldMapper = fieldMapper.getDelegate();
			}

			if (!isAble(fieldMapper.isInsertAble(), value == null, useWhiteList, fieldMapper, whiteListTag,
					ignoreTag)) {
				continue;
			}
			allFieldNull = false;
			tableSql.append(fieldMapper.getDbFieldName()).append(COMMA);
			if (((FieldMapper) fieldMapper).isOpVersionLock()) {
				value = 0;
				valueSql.append("'0',");
			} else {
				if (fieldMapper.getCryptKeyField() != null) {
					valueSql.append(AES_ENCRYPT_OPENPAREN);
				}
				valueSql.append(POUND_OPENBRACE);
				dealForeignKey(valueSql, fieldMapper).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(fieldMapper.getJdbcType().toString());
				if (fieldMapper.getTypeHandlerPath() != null) {
					valueSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
				}
				if (fieldMapper.isUniqueKey()) {
					uniqueKeyHandled = true;
					if (keyHandler != null) {
						handleInsertSql(keyHandler, valueSql, fieldMapper, object, uniqueKeyHandled, null);
					}
				}
				valueSql.append(CLOSEBRACE_COMMA);
				if (fieldMapper.getCryptKeyField() != null) {
					handleCryptKey(valueSql, fieldMapper, null);
				}
			}
		}
		if (keyHandler != null && !uniqueKeyHandled) {
			FieldMapper temp = tableMapper.getUniqueKeyNames()[0];
			tableSql.append(temp.getDbFieldName()).append(COMMA);
			handleInsertSql(keyHandler, valueSql, temp, object, uniqueKeyHandled, null);
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_FIELD);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		valueSql.delete(valueSql.lastIndexOf(COMMA), valueSql.lastIndexOf(COMMA) + 1);
		return tableSql.append(CLOSEPAREN_BLANK).append(valueSql).append(CLOSEPAREN).toString();
	}

	private static void handleCryptKey(StringBuilder valueSql, FieldMapper fieldMapper, Integer batchIndex) {
		valueSql.append(CONCAT_WS_OPENPAREN);
		FieldMapper[] cryptKeyFieldArray = fieldMapper.getCryptKeyField();
		boolean b = false;
		for (FieldMapper e : cryptKeyFieldArray) {
			if (b) {
				valueSql.append(COMMA);
			} else {
				b = true;
			}
			valueSql.append(POUND_OPENBRACE);
			if (batchIndex != null) {
				valueSql.append(COLLECTION_OPENBRACKET).append(batchIndex).append(CLOSEBRACKET_DOT);
			}
			valueSql.append(e.getFieldName()).append(COMMA_JDBCTYPE_EQUAL).append(e.getJdbcType());
			if (e.getTypeHandlerPath() != null) {
				valueSql.append(COMMA_TYPEHANDLER_EQUAL).append(e.getTypeHandlerPath());
			}
			valueSql.append(CLOSEBRACE);
		}
		dealCryptKeyAddition(b, valueSql, fieldMapper);
		valueSql.append(CLOSEPAREN).append(CLOSEPAREN_COMMA);
	}

	private static void handleInsertSql(KeyHandler keyHandler, StringBuilder valueSql, FieldMapper fieldMapper,
			Object object, boolean uniqueKeyHandled, Integer batchIndex)
			throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {
		if (!uniqueKeyHandled) {
			valueSql.append(POUND_OPENBRACE);
			if (batchIndex != null) {
				valueSql.append(COLLECTION_OPENBRACKET).append(batchIndex).append(CLOSEBRACKET_DOT);
			}
			valueSql.append(fieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
					.append(fieldMapper.getJdbcType()).append(CLOSEBRACE_COMMA);
		}
		BeanUtils.setProperty(object, fieldMapper.getFieldName(), keyHandler.getKey());
	}

	private static StringBuilder dealForeignKey(StringBuilder stringBuilder, FieldMapper fieldMapper) {
		if (fieldMapper.isForeignKey()) {
			stringBuilder.append(fieldMapper.getFieldName()).append(DOT).append(fieldMapper.getForeignFieldName());
		} else {
			stringBuilder.append(fieldMapper.getFieldName());
		}
		return stringBuilder;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private static HashMap<String, Object> getCollectionObjectMap(Object objectC) {
		HashMap<String, Object> valueC = null;
		if (collectionObjectClassName == null) {
			collectionObjectClassName = objectC.getClass().getSimpleName();
		}
		switch (collectionObjectClassName) {
			case "StrictMap":
				valueC = (DefaultSqlSession.StrictMap<Object>) objectC;
				break;
			default:
				valueC = (MapperMethod.ParamMap<Object>) objectC;
				break;
		}
		return valueC;
	}

	/**
	 * The insert SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return String
	 * @throws NoSuchFieldException      Exception
	 * @throws IllegalAccessException    Exception
	 * @throws InvocationTargetException Exception
	 * @throws NoSuchMethodException     Exception
	 */
	@SuppressWarnings("unchecked")
	public static String buildInsertBatchSql(Object objectC, FlyingModel flyingModel)
			throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		HashMap<String, Object> valueC = getCollectionObjectMap(objectC);
		StringBuilder tableSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();
		String ignoreTag = flyingModel.getIgnoreTag();
		String whiteListTag = flyingModel.getWhiteListTag();
		boolean useWhiteList = whiteListTag == null ? false : true;
		TableMapper tableMapper = null;
		Map<String, Object> dtoFieldMap = null;
		boolean allFieldNull = true;
		int i = 0;
		Collection<Object> c = (Collection<Object>) (valueC.get(COLLECTION));
		if (c.isEmpty()) {
			throw new AutoMapperException(
					new StringBuilder(AutoMapperExceptionEnum.INSERT_BATCH_PARAMETER_OBJECT_IS_EMPTY.toString())
							.append(" of ").append(flyingModel.getId()).toString());
		}
		for (Object object : c) {
			KeyHandler keyHandler = flyingModel.getKeyHandler();
			dtoFieldMap = PropertyUtils.describe(object);
			if (i == 0) {
				tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
				String tableName = tableMapper.getTableName();
				tableSql.append(INSERT_INTO_BLANK).append(tableName).append(BLANK_OPENPAREN);
				valueSql.append("values");
			}
			valueSql.append("(");
			for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
				Object value = dtoFieldMap.get(fieldMapper.getFieldName());

				if (value == null && fieldMapper.isHasDelegate()) {
					value = dtoFieldMap.get(fieldMapper.getDelegate().getFieldName());
					fieldMapper = fieldMapper.getDelegate();
				}

				if (!isAble(fieldMapper.isInsertAble(), false, useWhiteList, fieldMapper, whiteListTag, ignoreTag)) {
					continue;
				}
				allFieldNull = false;
				if (i == 0) {
					tableSql.append(fieldMapper.getDbFieldName()).append(COMMA);
				}
				if (((FieldMapper) fieldMapper).isOpVersionLock()) {
					value = 0;
					valueSql.append("'0',");
				} else if (value == null) {
					if (fieldMapper.isUniqueKey() && keyHandler != null) {
						handleInsertSql(keyHandler, valueSql, fieldMapper, object, false, i);
					} else {
						valueSql.append(DEFAULT_COMMA);
					}
				} else {
					if (fieldMapper.getCryptKeyField() != null) {
						valueSql.append(AES_ENCRYPT_OPENPAREN);
					}
					valueSql.append(POUND_OPENBRACE);
					valueSql.append(COLLECTION_OPENBRACKET).append(i).append(CLOSEBRACKET_DOT);
					dealForeignKey(valueSql, fieldMapper);
					valueSql.append(COMMA).append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());
					if (fieldMapper.getTypeHandlerPath() != null) {
						valueSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
					}
					if (fieldMapper.isUniqueKey()) {
						if (keyHandler != null) {
							handleInsertSql(keyHandler, valueSql, fieldMapper, object, false, i);
						}
					}
					valueSql.append(CLOSEBRACE_COMMA);
					if (fieldMapper.getCryptKeyField() != null) {
						handleCryptKey(valueSql, fieldMapper, i);
					}
				}
			}
			if (allFieldNull) {
				throw new BuildSqlException(BuildSqlExceptionEnum.NULL_FIELD);
			}
			valueSql.delete(valueSql.lastIndexOf(COMMA), valueSql.lastIndexOf(COMMA) + 1);
			valueSql.append(CLOSEPAREN).append(COMMA);
			i++;
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		valueSql.delete(valueSql.lastIndexOf(COMMA), valueSql.lastIndexOf(COMMA) + 1);
		return tableSql.append(CLOSEPAREN_BLANK).append(valueSql).toString();

	}

	/**
	 * The insert SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return String
	 * @throws NoSuchFieldException      Exception
	 * @throws IllegalAccessException    Exception
	 * @throws InvocationTargetException Exception
	 * @throws NoSuchMethodException     Exception
	 */
	@SuppressWarnings("unchecked")
	public static String buildUpdateBatchSql(Object objectC, FlyingModel flyingModel)
			throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		HashMap<String, Object> valueC = getCollectionObjectMap(objectC);
		StringBuilder tableSql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder();
		String ignoreTag = flyingModel.getIgnoreTag();
		String whiteListTag = flyingModel.getWhiteListTag();
		boolean useWhiteList = whiteListTag == null ? false : true;
		TableMapper tableMapper = null;
		Map<String, Object> dtoFieldMap = null;
		boolean allFieldNull = true;
		int i = 0;
		Collection<Object> c = (Collection<Object>) (valueC.get(COLLECTION));
		if (c.isEmpty()) {
			throw new AutoMapperException(
					new StringBuilder(AutoMapperExceptionEnum.UPDATE_BATCH_PARAMETER_OBJECT_IS_EMPTY.toString())
							.append(" of ").append(flyingModel.getId()).toString());
		}
		Map<FieldMapper, StringBuilder> m = null;
		FieldMapper uniqueFieldMapper = null;

		for (Object object : c) {
			dtoFieldMap = PropertyUtils.describe(object);
			if (i == 0) {
				tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
				uniqueFieldMapper = tableMapper.getUniqueKey();
				String tableName = tableMapper.getTableName();
				tableSql.append(UPDATE_BLANK).append(tableName).append(BLANK_SET_BLANK);
				whereSql.append(WHERE_BLANK).append(uniqueFieldMapper.getDbFieldName()).append(BLANK_IN_OPENPAREN);
			}
			for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {

				Object value = dtoFieldMap.get(fieldMapper.getFieldName());

				if (value == null && fieldMapper.isHasDelegate()) {
					value = dtoFieldMap.get(fieldMapper.getDelegate().getFieldName());
					fieldMapper = fieldMapper.getDelegate();
				}

				if (!isAble(fieldMapper.isUpdateAble(), value == null, useWhiteList, fieldMapper, whiteListTag,
						ignoreTag)) {
					continue;
				}
				if (m == null) {
					m = new HashMap<>();
				}
				FieldMapper maybeDelegate = fieldMapper.isHasDelegate() ? fieldMapper.getDelegate() : fieldMapper;
				if (fieldMapper.isOpVersionLock()) {
					if (!m.containsKey(maybeDelegate)) {

						StringBuilder temp = new StringBuilder(fieldMapper.getDbFieldName()).append(EQUAL)
								.append(fieldMapper.getDbFieldName()).append(PLUS_1);
						m.put(maybeDelegate, temp);
					}
				} else if (fieldMapper != tableMapper.getUniqueKey()) {
					allFieldNull = false;
					if (!m.containsKey(maybeDelegate)) {
						StringBuilder temp = new StringBuilder(fieldMapper.getDbFieldName()).append(" = CASE ")
								.append(uniqueFieldMapper.getDbFieldName());
						handlerUpdateBatch(temp, uniqueFieldMapper, fieldMapper, i);
						m.put(maybeDelegate, temp);
					} else {
						handlerUpdateBatch(m.get(maybeDelegate), uniqueFieldMapper, fieldMapper, i);
					}

				} else {
					whereSql.append(POUND_OPENBRACE).append(COLLECTION_OPENBRACKET).append(i).append(CLOSEBRACKET_DOT)
							.append(uniqueFieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
							.append(uniqueFieldMapper.getJdbcType().toString()).append(CLOSEBRACE).append(COMMA);
				}
			}
			if (allFieldNull) {
				throw new BuildSqlException(BuildSqlExceptionEnum.NULL_FIELD);
			}
			i++;
		}
		for (Map.Entry<FieldMapper, StringBuilder> e : m.entrySet()) {
			tableSql.append(e.getValue());
			if (!e.getKey().isOpVersionLock()) {
				tableSql.append(ELSE).append(e.getKey().getDbFieldName()).append(END);
			}
			tableSql.append(COMMA);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		whereSql.delete(whereSql.lastIndexOf(COMMA), whereSql.lastIndexOf(COMMA) + 1);
		whereSql.append(")");
		return tableSql.append(whereSql).toString();
	}

	private static void handlerUpdateBatch(StringBuilder stringBuilder, FieldMapper uniqueFieldMapper,
			FieldMapper fieldMapper, int i) {
		stringBuilder.append(WHEN).append(POUND_OPENBRACE).append(COLLECTION_OPENBRACKET).append(i)
				.append(CLOSEBRACKET_DOT).append(uniqueFieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
				.append(uniqueFieldMapper.getJdbcType().toString()).append(CLOSEBRACE).append(THEN);
		if (fieldMapper.getCryptKeyField() != null) {
			stringBuilder.append(AES_ENCRYPT_OPENPAREN);
		}
		stringBuilder.append(POUND_OPENBRACE).append(COLLECTION_OPENBRACKET).append(i).append(CLOSEBRACKET_DOT);
		dealForeignKey(stringBuilder, fieldMapper).append(COMMA).append(JDBCTYPE_EQUAL)
				.append(fieldMapper.getJdbcType().toString());
		if (fieldMapper.getTypeHandlerPath() != null) {
			stringBuilder.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
		}
		stringBuilder.append(CLOSEBRACE);
		if (fieldMapper.getCryptKeyField() != null) {
			stringBuilder.append(COMMA).append(CONCAT_WS_OPENPAREN);
			boolean b = false;
			for (String e : fieldMapper.getCryptKeyColumn()) {
				if (b) {
					stringBuilder.append(COMMA);
				} else {
					b = true;
				}
				stringBuilder.append(e);
			}
			dealCryptKeyAddition(b, stringBuilder, fieldMapper);
			stringBuilder.append(CLOSEPAREN_CLOSEPAREN);
		}
	}

	private static boolean isAble(boolean excuteAble, boolean valueIsNull, boolean useWhiteList,
			FieldMapper fieldMapper, String whiteListTag, String ignoreTag) {
		if (!excuteAble
				|| (!fieldMapper.isOpVersionLock()
						&& (valueIsNull || (useWhiteList && !fieldMapper.getWhiteListTagSet().contains(whiteListTag))))
				|| fieldMapper.getIgnoreTagSet().contains(ignoreTag) || fieldMapper.isOnlyForJoin()) {
			return false;
		}
		return true;
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
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_OBJECT);
		}
		String ignoreTag = flyingModel.getIgnoreTag();
		String whiteListTag = flyingModel.getWhiteListTag();
		boolean useWhiteList = whiteListTag == null ? false : true;
		Map<String, Object> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));

		String tableName = tableMapper.getTableName();

		StringBuilder tableSql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder(WHERE_BLANK);

		tableSql.append(UPDATE_BLANK).append(tableName).append(BLANK_SET_BLANK);

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {

			Object value = dtoFieldMap.get(fieldMapper.getFieldName());

			if (value == null && fieldMapper.isHasDelegate()) {
				value = dtoFieldMap.get(fieldMapper.getDelegate().getFieldName());
				fieldMapper = fieldMapper.getDelegate();
			}

			if (!isAble(fieldMapper.isUpdateAble(), value == null, useWhiteList, fieldMapper, whiteListTag,
					ignoreTag)) {
				continue;
			}

			if (fieldMapper.isOpVersionLock()) {
				tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL).append(fieldMapper.getDbFieldName())
						.append(PLUS_1).append(COMMA);
			} else {
				allFieldNull = false;

				tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL);
				if (fieldMapper.getCryptKeyField() != null) {
					tableSql.append(AES_ENCRYPT_OPENPAREN);
				}
				tableSql.append(POUND_OPENBRACE);
				dealForeignKey(tableSql, fieldMapper).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(fieldMapper.getJdbcType().toString());
				if (fieldMapper.getTypeHandlerPath() != null) {
					tableSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
				}
				tableSql.append(CLOSEBRACE).append(COMMA);

				if (fieldMapper.getCryptKeyField() != null) {
					tableSql.append(CONCAT_WS_OPENPAREN);
					boolean b = false;
					for (String e : fieldMapper.getCryptKeyColumn()) {
						if (b) {
							tableSql.append(COMMA);
						} else {
							b = true;
						}
						tableSql.append(e);
					}
					dealCryptKeyAddition(b, tableSql, fieldMapper);
					tableSql.append(CLOSEPAREN).append(CLOSEPAREN_BLANK).append(COMMA);
				}
			}
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_FIELD);
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
					throw new BuildSqlException(
							new StringBuilder(BuildSqlExceptionEnum.UPDATE_UNIQUE_KEY_IS_NULL.toString())
									.append(fieldMapper.getDbFieldName()).toString());
				} else {
					whereSql.append(fieldMapper.getDbFieldName()).append(EQUAL_POUND_OPENBRACE)
							.append(fieldMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
							.append(fieldMapper.getJdbcType().toString()).append(CLOSEBRACE_AND_BLANK);
				}
			}
			for (FieldMapper f : tableMapper.getOpVersionLocks()) {
				whereSql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
						.append(CLOSEBRACE_AND_BLANK);
			}
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
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_OBJECT);
		}
		String ignoreTag = flyingModel.getIgnoreTag();
		String whiteListTag = flyingModel.getWhiteListTag();
		boolean useWhiteList = whiteListTag == null ? false : true;
		Map<String, Object> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));

		// updatePersistent causes features that do not require batch update

		String tableName = tableMapper.getTableName();

		StringBuilder tableSql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder(WHERE_BLANK);

		tableSql.append(UPDATE_BLANK).append(tableName).append(BLANK_SET_BLANK);

		boolean allFieldNull = true;

		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {

			Object value = dtoFieldMap.get(fieldMapper.getFieldName());

			if (value == null && fieldMapper.isHasDelegate()) {
				value = dtoFieldMap.get(fieldMapper.getDelegate().getFieldName());
				fieldMapper = fieldMapper.getDelegate();
			}

			if (!isAble(fieldMapper.isUpdateAble(), false, useWhiteList, fieldMapper, whiteListTag, ignoreTag)) {
				continue;
			}

			if (fieldMapper.isOpVersionLock()) {
				tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL).append(fieldMapper.getDbFieldName())
						.append(PLUS_1).append(COMMA);
			} else {
				allFieldNull = false;
				tableSql.append(fieldMapper.getDbFieldName()).append(EQUAL);
				if (fieldMapper.getCryptKeyField() != null) {
					tableSql.append(AES_ENCRYPT_OPENPAREN);
				}
				tableSql.append(POUND_OPENBRACE);
				dealForeignKey(tableSql, fieldMapper).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(fieldMapper.getJdbcType().toString());
				if (fieldMapper.getTypeHandlerPath() != null) {
					tableSql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
				}
				tableSql.append(CLOSEBRACE).append(COMMA);
				if (fieldMapper.getCryptKeyField() != null) {
					tableSql.append(CONCAT_WS_OPENPAREN);
					boolean b = false;
					for (String e : fieldMapper.getCryptKeyColumn()) {
						if (b) {
							tableSql.append(COMMA);
						} else {
							b = true;
						}
						tableSql.append(e);
					}
					dealCryptKeyAddition(b, tableSql, fieldMapper);
					tableSql.append(CLOSEPAREN).append(CLOSEPAREN_BLANK).append(COMMA);
				}
			}
		}
		if (allFieldNull) {
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_FIELD);
		}

		tableSql.delete(tableSql.lastIndexOf(COMMA), tableSql.lastIndexOf(COMMA) + 1);
		for (FieldMapper fieldMapper : tableMapper.getUniqueKeyNames()) {
			whereSql.append(fieldMapper.getDbFieldName());
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				throw new BuildSqlException(
						new StringBuilder(BuildSqlExceptionEnum.UPDATE_PERSISTENT_UNIQUE_KEY_IS_NULL.toString())
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

	private static void dealBatchWhere(StringBuilder whereSql, ConditionMapper conditionMapper) {
		whereSql.append(POUND_OPENBRACE).append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
				.append(conditionMapper.getJdbcType().toString());
		if (conditionMapper.getTypeHandlerPath() != null) {
			whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(conditionMapper.getTypeHandlerPath());
		}
		whereSql.append(CLOSEBRACE_AND_BLANK);
	}

	private static void dealBatchCondition(ConditionType conditionType, StringBuilder whereSql,
			ConditionMapper conditionMapper, Object value) {
		switch (conditionType) {
			case EQUAL:
				whereSql.append(conditionMapper.getDbFieldName()).append(EQUAL);
				dealBatchWhere(whereSql, conditionMapper);
				break;
			case LESS_OR_EQUAL:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LESS_EQUAL_BLANK);
				dealBatchWhere(whereSql, conditionMapper);
				break;
			case LESS_THAN:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LESS_BLANK);
				dealBatchWhere(whereSql, conditionMapper);
				break;
			case GREATER_OR_EQUAL:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_GREATER_EQUAL_BLANK);
				dealBatchWhere(whereSql, conditionMapper);
				break;
			case GREATER_THAN:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_GREATER_BLANK);
				dealBatchWhere(whereSql, conditionMapper);
				break;
			case NOT_EQUAL:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LESS_GREATER_BLANK);
				dealBatchWhere(whereSql, conditionMapper);
				break;
			case LIKE:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LIKE_BLANK_POUND_OPENBRACE)
						.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
						.append(HandlerPaths.CONDITION_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
				break;
			case HEAD_LIKE:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LIKE_BLANK_POUND_OPENBRACE)
						.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
						.append(HandlerPaths.CONDITION_HEAD_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
				break;
			case TAIL_LIKE:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_LIKE_BLANK_POUND_OPENBRACE)
						.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
						.append(HandlerPaths.CONDITION_TAIL_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
				break;
			case NOT_LIKE:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_NOT)
						.append(BLANK_LIKE_BLANK_POUND_OPENBRACE)
						.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
						.append(HandlerPaths.CONDITION_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
				break;
			case NOT_HEAD_LIKE:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_NOT)
						.append(BLANK_LIKE_BLANK_POUND_OPENBRACE)
						.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
						.append(HandlerPaths.CONDITION_HEAD_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
				break;
			case NOT_TAIL_LIKE:
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_NOT)
						.append(BLANK_LIKE_BLANK_POUND_OPENBRACE)
						.append(conditionMapper.getFieldName()).append(COMMA).append(JDBCTYPE_EQUAL)
						.append(conditionMapper.getJdbcType().toString()).append(COMMA_TYPEHANDLER_EQUAL)
						.append(HandlerPaths.CONDITION_TAIL_LIKE_HANDLER_PATH).append(CLOSEBRACE_AND_BLANK);
				break;
			case NULL_OR_NOT:
				Boolean isNull = (Boolean) value;
				whereSql.append(conditionMapper.getDbFieldName()).append(BLANK_IS);
				if (!isNull) {
					whereSql.append(BLANK_NOT);
				}
				whereSql.append(BLANK_NULL).append(BLANK_AND_BLANK);
				break;
			case IN:
				dealWhereSqlOfIn(value, whereSql, conditionMapper, null, false, null);
				break;
			case NOT_IN:
				dealWhereSqlOfIn(value, whereSql, conditionMapper, null, true, null);
				break;
			default:
				throw new BuildSqlException(
						new StringBuilder(BuildSqlExceptionEnum.UNKOWN_CONDITION_FOR_BATCH_PROCESS.toString())
								.append(conditionMapper.getDbFieldName()).toString());
		}
	}

	@SuppressWarnings("unchecked")
	private static void dealWhereSqlOfIn(Object value, StringBuilder whereSql, ConditionMapper conditionMapper,
			String fieldNamePrefix, boolean notIn, TableName tableName) {
		Collection<Object> multiConditionC = (Collection<Object>) value;
		if (multiConditionC.isEmpty() && notIn) {
			return;
		} else {
			int j = -1;
			boolean allNull = true;
			if (tableName != null) {
				handleWhereSql(whereSql, conditionMapper, tableName);
			} else {
				whereSql.append(conditionMapper.getDbFieldName());
			}
			if (notIn) {
				whereSql.append(BLANK_NOT).append(BLANK_IN_OPENPAREN);
			} else {
				whereSql.append(BLANK_IN_OPENPAREN);
			}
			if (multiConditionC.isEmpty()) {
				// use "in (null)" on this condition
				whereSql.append(NULL);
			} else {
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
						whereSql.append(conditionMapper.getFieldName()).append(OPENBRACKET).append(j)
								.append(CLOSEBRACKET).append(COMMA).append(JDBCTYPE_EQUAL)
								.append(conditionMapper.getJdbcType().toString());
						if (conditionMapper.getTypeHandlerPath() != null) {
							whereSql.append(COMMA_TYPEHANDLER_EQUAL).append(conditionMapper.getTypeHandlerPath());
						}
						whereSql.append(CLOSEBRACE_COMMA);
					}
				}
				if (!allNull) {
					whereSql.delete(whereSql.lastIndexOf(COMMA), whereSql.lastIndexOf(COMMA) + 1);
				}
			}

		}
		whereSql.append(CLOSEPAREN_BLANK_AND_BLANK);
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
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_OBJECT);
		}
		Map<String, Object> dtoFieldMap = PropertyUtils.describe(object);
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(object.getClass()));
		String tableName = tableMapper.getTableName();
		StringBuilder sql = new StringBuilder();
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
					throw new BuildSqlException(
							new StringBuilder(BuildSqlExceptionEnum.DELETE_UNIQUE_KEY_IS_NULL.toString())
									.append(fieldMapper.getDbFieldName()).toString());
				}
				sql.append(EQUAL_POUND_OPENBRACE).append(fieldMapper.getFieldName()).append(COMMA)
						.append(JDBCTYPE_EQUAL).append(fieldMapper.getJdbcType().toString());

				if (fieldMapper.getTypeHandlerPath() != null) {
					sql.append(COMMA_TYPEHANDLER_EQUAL).append(fieldMapper.getTypeHandlerPath());
				}
				sql.append(CLOSEBRACE_AND_BLANK);
			}
			for (FieldMapper f : tableMapper.getOpVersionLocks()) {
				sql.append(f.getDbFieldName()).append(EQUAL_POUND_OPENBRACE).append(f.getFieldName())
						.append(CLOSEBRACE_AND_BLANK);
			}
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
	public static String buildSelectSql(Class<?> clazz, FlyingModel flyingModel) throws InstantiationException,
			IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
		TableMapper tableMapper = buildTableMapper(getTableMappedClass(clazz));
		StringBuilder selectSql = new StringBuilder(SELECT_BLANK);
		StringBuilder fromSql = new StringBuilder(FROM);
		StringBuilder whereSql = new StringBuilder(WHERE_BLANK);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(true, clazz, selectSql, fromSql, whereSql, null, ai, flyingModel,
				null, null, null, null, Collections.emptyList());
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

	public static String buildSelectAllSql(Object object, FlyingModel flyingModel) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
		return buildSelectAllSql(object, flyingModel, null);
	}

	/**
	 * The query SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return sql
	 */
	public static String buildSelectAllSql(Object object, FlyingModel flyingModel, List<Order> orders)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException,
			InstantiationException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_OBJECT);
		}
		StringBuilder selectSql = new StringBuilder(SELECT_BLANK);
		StringBuilder fromSql = new StringBuilder(FROM);
		StringBuilder whereSql = new StringBuilder(WHERE_BLANK);
		StringBuilder groupBySql = new StringBuilder();
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(false, object, selectSql, fromSql, whereSql, groupBySql, ai,
				flyingModel, null, null, null, null, orders);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_BLANK.equals(whereSql.toString())) {
			whereSql = new StringBuilder();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}
		if (groupBySql.length() > 0 && groupBySql.indexOf(COMMA) > -1) {
			groupBySql.delete(groupBySql.lastIndexOf(COMMA), groupBySql.lastIndexOf(COMMA) + 1);
			// prepare for v1.1.0 System.out.println("group by::" + groupBySql);
		}
		return selectSql.append(fromSql).append(whereSql).append(groupBySql).toString();
	}

	/**
	 * The query SQL statement is generated from the incoming object
	 * 
	 * @param object      Object
	 * @param flyingModel FlyingModel
	 * @return sql
	 */
	public static String buildSelectOneSql(Object object, FlyingModel flyingModel, List<Order> orders) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_OBJECT);
		}
		StringBuilder selectSql = new StringBuilder(SELECT_BLANK);
		StringBuilder fromSql = new StringBuilder(FROM);
		StringBuilder whereSql = new StringBuilder(WHERE_BLANK);
		AtomicInteger ai = new AtomicInteger(0);
		dealMapperAnnotationIterationForSelectAll(false, object, selectSql, fromSql, whereSql, null, ai, flyingModel,
				null, null, null, null, orders);

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_BLANK.equals(whereSql.toString())) {
			whereSql = new StringBuilder();
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
	 * @throws InstantiationException
	 * @throws RuntimeException          Exception
	 */
	public static String buildCountSql(Object object, FlyingModel flyingModel)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		if (null == object) {
			throw new BuildSqlException(BuildSqlExceptionEnum.NULL_OBJECT);
		}

		TableMapper tableMapper = buildTableMapper(getTableMappedClass(object.getClass()));
		AtomicInteger ai = new AtomicInteger(0);
		TableName tableName = new TableName(tableMapper, 0, null);

		StringBuilder selectSql = new StringBuilder();
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

		StringBuilder[] sqlBuilders = new StringBuilder[] { new StringBuilder(FROM), new StringBuilder(WHERE_BLANK) };
		StringBuilder fromSql = sqlBuilders[0];
		StringBuilder whereSql = sqlBuilders[1];
		dealMapperAnnotationIterationForCount(false, object, sqlBuilders, null, null, null, ai, tableName,
				flyingModel.getIndex());

		if (selectSql.indexOf(COMMA) > -1) {
			selectSql.delete(selectSql.lastIndexOf(COMMA), selectSql.lastIndexOf(COMMA) + 1);
		}
		if (WHERE_BLANK.equals(whereSql.toString())) {
			whereSql = new StringBuilder();
		} else if (whereSql.indexOf(AND) > -1) {
			whereSql.delete(whereSql.lastIndexOf(AND), whereSql.lastIndexOf(AND) + 3);
		}

		return selectSql.append(fromSql).append(whereSql).toString();
	}

	private static void dealMapperAnnotationIterationForSelectAll(boolean objectIsClass, Object object,
			StringBuilder selectSql, StringBuilder fromSql, StringBuilder whereSql, StringBuilder groupBySql,
			AtomicInteger index, FlyingModel flyingModel, TableName tn, Mapperable originFieldMapper,
			String fieldPerfix, TableName lastTableName, List<Order> orders)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, NoSuchFieldException, InstantiationException {
		Class<?> objectType = objectIsClass ? (Class<?>) object : object.getClass();
		String ignoreTag = null;
		String prefix = null;
		String indexStr = null;
		String whiteListTag = null;
		Map<String, Set<AggregateModel>> aggregateMap = null;
		Set<String> groupBySet = null;
		boolean useWhiteList = false;
		if (flyingModel != null) {
			ignoreTag = flyingModel.getIgnoreTag();
			prefix = flyingModel.getPrefix();
			indexStr = flyingModel.getIndex();
			whiteListTag = flyingModel.getWhiteListTag();
			if (whiteListTag != null) {
				useWhiteList = true;
			}
			if (!flyingModel.getAggregate().isEmpty()) {
				aggregateMap = flyingModel.getAggregate();
			}
			if (!flyingModel.getGroupBy().isEmpty()) {
				groupBySet = flyingModel.getGroupBy();
			}
		}

		// if (aggregateMap != null) {
		// prepare for v1.1.0 System.out.println("aggregateJson::" +
		// JSONObject.toJSONString(aggregateMap));
		// }
		// if (groupBySet != null) {
		// prepare for v1.1.0 System.out.println("groupBySet::" +
		// JSONObject.toJSONString(groupBySet));
		// }

		Map<String, Object> dtoFieldMap = objectIsClass ? Collections.emptyMap() : PropertyUtils.describe(object);
		Class<?> tempClass = getTableMappedClass(objectType);
		TableMapper tableMapper = buildTableMapper(tempClass);
		QueryMapper queryMapper = buildQueryMapper(objectType, tempClass);
		TableName tableName = null;

		int indexValue = index.getAndIncrement();
		if (lastTableName == null) {
			tableName = new TableName(tableMapper, indexValue, null);
		} else {
			tableName = new TableName(tableMapper, indexValue, lastTableName.getMap());
		}

		if (orders != null) {
			for (Order o : orders) {
				if (o.getTableName() == null && o.getObject() == object) {
					o.setTableName(tableName);
				}
			}
		}

		/*
		 * If originFieldMapper is null, it is considered to be the first traversal. In
		 * the first iteration, handle fromSql.
		 * 
		 */
		if (originFieldMapper == null) {
			fromSql.append(tableName.sqlSelect());
			if (indexStr != null) {
				fromSql.append(" ").append(indexStr);
			}
		}

		if (flyingModel != null) {
			for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
				if ((!useWhiteList || fieldMapper.getWhiteListTagSet().contains(whiteListTag))
						&& (!fieldMapper.getIgnoreTagSet().contains(ignoreTag))) {
					dtoFieldMap = dealSelectSql(flyingModel, fieldMapper, dtoFieldMap, index, selectSql, tableName,
							prefix);
				}
				if (aggregateMap != null) {
					// TODO 在这里处理聚合函数
					if (aggregateMap.containsKey(fieldMapper.getDbFieldName())) {
						Set<AggregateModel> set = aggregateMap.get(fieldMapper.getDbFieldName());
						for (AggregateModel am : set) {
							selectSql.append(am.getFunction()).append("(").append(tableName.sqlWhere())
									.append(am.getColumn()).append(") as ").append(am.getAlias()).append(COMMA);
						}
					}
				}
				if (groupBySet != null) {
					// TODO 在这里处理group by
					if (groupBySet.contains(fieldMapper.getDbFieldName())) {
						if (groupBySql.length() == 0) {
							groupBySql.append(GROUP_BY_BLANK);
						}
						groupBySql.append(tableName.sqlWhere()).append(fieldMapper.getDbFieldName()).append(COMMA);
					}
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
		if (originFieldMapper != null && tn != null) {
			/* Processing fieldPerfix */
			if (fieldPerfix == null) {
				temp = originFieldMapper.getFieldName();
			} else {
				temp = new StringBuilder(fieldPerfix).append(DOT).append(originFieldMapper.getFieldName()).toString();
			}
			/* Processing fromSql */
			fromSql.append(originFieldMapper.getAssociationType().value()).append(tableName.sqlSelect())
					.append(BLANK_ON_BLANK).append(tn.sqlWhere()).append(originFieldMapper.getDbFieldNameForJoin())
					.append(BLANK_EQUAL_BLANK).append(tableName.sqlWhere())
					.append(originFieldMapper.getDbAssociationUniqueKey());
			ForeignAssociationMapper[] fams = originFieldMapper.getForeignAssociationMappers();
			if (fams != null && fams.length > 0) {
				for (ForeignAssociationMapper fam : fams) {
					fromSql.append(BLANK_AND_BLANK).append(tn.sqlWhere()).append(fam.getDbFieldName())
							.append(fam.getCondition().value()).append(tableName.sqlWhere())
							.append(fam.getDbAssociationFieldName());
				}
			}
		}

		/* Handle the conditions in the fieldMapper */
		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			if (fieldMapper.isHasDelegate() && dtoFieldMap.get(fieldMapper.getDelegate().getFieldName()) != null) {
				dealConditionEqual(whereSql, fieldMapper.getDelegate(), tableName, temp, false, 0);
				continue;
			}
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (value == null) {
				continue;
			}

			if (fieldMapper.isForeignKey()) {
				dealMapperAnnotationIterationForSelectAll(value instanceof Class<?>, value, selectSql, fromSql,
						whereSql, null, index,
						flyingModel == null ? (null) : (flyingModel.getProperties().get(fieldMapper.getFieldName())),
						tableName, fieldMapper, temp, tableName, orders);
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

	private static Map<String, Object> dealSelectSql(FlyingModel flyingModel, FieldMapper fieldMapper,
			Map<String, Object> dtoFieldMap, AtomicInteger index, StringBuilder selectSql, TableName tableName,
			String prefix) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		FlyingModel inner = flyingModel.getProperties().get(fieldMapper.getFieldName());
		if (inner != null) {
			if (dtoFieldMap.get(fieldMapper.getFieldName()) == null) {
				if (dtoFieldMap.isEmpty()) {
					dtoFieldMap = new HashMap<>();
				}
				dtoFieldMap.put(fieldMapper.getFieldName(), fieldMapper.getFieldType());
			}
		}

		if (!fieldMapper.isOnlyForJoin()) {
			// 在此处处理crypt方法
			if (fieldMapper.getCryptKeyField() != null) {
				selectSql.append(AES_DECRYPT_OPENPAREN);
			}

			selectSql.append(tableName.sqlWhere()).append(fieldMapper.getDbFieldName());

			if (fieldMapper.getCryptKeyField() != null) {
				selectSql.append(COMMA).append(CONCAT_WS_OPENPAREN);
				boolean b = false;
				for (String e : fieldMapper.getCryptKeyColumn()) {
					if (b) {
						selectSql.append(COMMA);
					} else {
						b = true;
					}
					selectSql.append(tableName.sqlWhere()).append(e);
				}
				dealCryptKeyAddition(b, selectSql, fieldMapper);
				selectSql.append(CLOSEPAREN_CLOSEPAREN);
			}

			if (prefix != null) {
				selectSql.append(" as ").append(prefix).append(fieldMapper.getDbFieldName());
			} else if (fieldMapper.getCryptKeyField() != null) {
				selectSql.append(" as ").append(fieldMapper.getDbFieldName());
			}
			selectSql.append(COMMA);
		}
		return dtoFieldMap;
	}

	private static void dealConditionOrMapper(OrMapper orMapper, Object value, StringBuilder whereSql,
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

	private static void dealConditionMapper(ConditionMapper conditionMapper, Object value, StringBuilder whereSql,
			TableName tableName, String temp, boolean isOr, int i) {
		switch (conditionMapper.getConditionType()) {
			case EQUAL:
				dealConditionEqual(whereSql, conditionMapper, tableName, temp, isOr, i);
				break;
			case LIKE:
				dealConditionLike(whereSql, conditionMapper, ConditionType.LIKE, tableName, temp, isOr, i);
				break;
			case NOT_LIKE:
				dealConditionLike(whereSql, conditionMapper, ConditionType.NOT_LIKE, tableName, temp, isOr, i);
				break;
			case HEAD_LIKE:
				dealConditionLike(whereSql, conditionMapper, ConditionType.HEAD_LIKE, tableName, temp, isOr, i);
				break;
			case NOT_HEAD_LIKE:
				dealConditionLike(whereSql, conditionMapper, ConditionType.NOT_HEAD_LIKE, tableName, temp, isOr, i);
				break;
			case TAIL_LIKE:
				dealConditionLike(whereSql, conditionMapper, ConditionType.TAIL_LIKE, tableName, temp, isOr, i);
				break;
			case NOT_TAIL_LIKE:
				dealConditionLike(whereSql, conditionMapper, ConditionType.NOT_TAIL_LIKE, tableName, temp, isOr, i);
				break;
			case GREATER_THAN:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GREATER_THAN, tableName, temp, isOr, i);
				break;
			case GREATER_OR_EQUAL:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.GREATER_OR_EQUAL, tableName, temp, isOr,
						i);
				break;
			case LESS_THAN:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LESS_THAN, tableName, temp, isOr, i);
				break;
			case LESS_OR_EQUAL:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.LESS_OR_EQUAL, tableName, temp, isOr, i);
				break;
			case NOT_EQUAL:
				dealConditionNotEqual(whereSql, conditionMapper, ConditionType.NOT_EQUAL, tableName, temp, isOr, i);
				break;
			case MULTI_LIKE_AND:
				dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MULTI_LIKE_AND, tableName, temp,
						isOr);
				break;
			case MULTI_LIKE_OR:
				dealConditionMultiLike(value, whereSql, conditionMapper, ConditionType.MULTI_LIKE_OR, tableName, temp,
						isOr);
				break;
			case IN:
				dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.IN, tableName, temp, isOr);
				break;
			case NOT_IN:
				dealConditionInOrNot(value, whereSql, conditionMapper, ConditionType.NOT_IN, tableName, temp, isOr);
				break;
			case NULL_OR_NOT:
				dealConditionNullOrNot(value, whereSql, conditionMapper, tableName, isOr);
				break;
			default:
				break;
		}
	}

	private static void dealMapperAnnotationIterationForCount(boolean objectIsClass, Object object,
			StringBuilder[] sqlBuilders, TableName originTableName, Mapperable originFieldMapper, String fieldPerfix,
			AtomicInteger index, TableName lastTableName, String indexStr)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		Class<?> objectType = objectIsClass ? (Class<?>) object : object.getClass();
		Map<String, Object> dtoFieldMap = objectIsClass ? Collections.emptyMap() : PropertyUtils.describe(object);

		TableMapper tableMapper = buildTableMapper(getTableMappedClass(objectType));
		QueryMapper queryMapper = buildQueryMapper(object.getClass(), getTableMappedClass(objectType));
		TableName tableName = new TableName(tableMapper, index.getAndIncrement(), lastTableName.getMap());
		StringBuilder fromSql = sqlBuilders[0];
		StringBuilder whereSql = sqlBuilders[1];
		/*
		 * If originFieldMapper is null, it is considered to be the first traversal.In
		 * the first iteration, handle fromSql.
		 * 
		 */
		if (originFieldMapper == null) {
			fromSql.append(tableName.sqlSelect());
			if (indexStr != null) {
				fromSql.append(" ").append(indexStr);
			}
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
				temp = new StringBuilder(fieldPerfix).append(DOT).append(temp).toString();
			}
			/* Processing fromSql */
			fromSql.append(originFieldMapper.getAssociationType().value()).append(tableName.sqlSelect())
					.append(BLANK_ON_BLANK).append(originTableName.sqlWhere())
					.append(originFieldMapper.getDbFieldNameForJoin()).append(BLANK_EQUAL_BLANK)
					.append(tableName.sqlWhere()).append(originFieldMapper.getDbAssociationUniqueKey());
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
		for (FieldMapper fieldMapper : tableMapper.getFieldMapperCache().values()) {
			if (fieldMapper.isHasDelegate() && dtoFieldMap.get(fieldMapper.getDelegate().getFieldName()) != null) {
				dealConditionEqual(whereSql, fieldMapper.getDelegate(), tableName, temp, false, 0);
				continue;
			}
			Object value = dtoFieldMap.get(fieldMapper.getFieldName());
			if (fieldMapper.isForeignKey()) {
				boolean b = false;
				if (value == null) {
					value = fieldMapper.getFieldType();
					b = true;
				}
				dealMapperAnnotationIterationForCount(b, value, sqlBuilders, tableName, fieldMapper, temp, index,
						tableName, indexStr);
			} else {
				if (value == null) {
					continue;
				}
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
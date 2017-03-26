package indi.mybatis.flying.interceptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import indi.mybatis.flying.builders.SqlBuilder;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.utils.ReflectHelper;

/**
 * 通过拦截StatementHandler的prepare方法，根据参数parameterObject配置的注解信息，自动生成sql语句。
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class AutoMapperInterceptor implements Interceptor {
	private static final Log logger = LogFactory.getLog(AutoMapperInterceptor.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static String dialect = "";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY);
		// 分离代理对象链
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		}
		// 分离最后一个代理对象的目标类
		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		}
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");

		if (null == originalSql || "".equals(originalSql) || "?".equals(originalSql)) {
			String newSql = "";
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			String id = mappedStatement.getId();
			id = id.substring(id.lastIndexOf(".") + 1);
			ActionType actionType = ActionType.valueOf(id);
			switch (actionType) {
			case count:
				newSql = SqlBuilder.buildCountSql(parameterObject);
				break;
			case delete:
				newSql = SqlBuilder.buildDeleteSql(parameterObject);
				break;
			case insert:
				newSql = SqlBuilder.buildInsertSql(parameterObject);
				break;
			case select:
				newSql = SqlBuilder.buildSelectSql(mappedStatement.getResultMaps().get(0).getType());
				break;
			case selectAll:
				newSql = SqlBuilder.buildSelectAllSql(parameterObject);
				break;
			case selectOne:
				newSql = SqlBuilder.buildSelectOneSql(parameterObject);
				break;
			case update:
				newSql = SqlBuilder.buildUpdateSql(parameterObject);
				break;
			case updatePersistent:
				newSql = SqlBuilder.buildUpdatePersistentSql(parameterObject);
				break;
			}
			logger.warn(new StringBuffer("Auto generated sql:").append(newSql).toString());
			SqlSource sqlSource = buildSqlSource(configuration, newSql, parameterObject.getClass());
			List<ParameterMapping> parameterMappings = sqlSource.getBoundSql(parameterObject).getParameterMappings();
			metaStatementHandler.setValue("delegate.boundSql.sql", sqlSource.getBoundSql(parameterObject).getSql());
			metaStatementHandler.setValue("delegate.boundSql.parameterMappings", parameterMappings);
		}

		// 开始处理分页问题
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			// RoutingStatementHandler statementHandler =
			// (RoutingStatementHandler) invocation.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
					"mappedStatement");
			BoundSql boundSql = delegate.getBoundSql();
			// Object parameterObject = boundSql.getParameterObject();
			if (parameterObject == null) {
				throw new NullPointerException("parameterObject error");
			} else if (parameterObject instanceof Conditionable) {
				Conditionable condition = (Conditionable) parameterObject;
				String sql = boundSql.getSql();
				if (condition.getLimiter() != null) {
					Connection connection = (Connection) invocation.getArgs()[0];
					String countSql = "select count(0) from (" + sql + ") myCount";
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
							boundSql.getParameterMappings(), parameterObject);
					setParameters(countStmt, mappedStatement, countBS, parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					condition.getLimiter().setTotalCount(count);
				}
				String pageSql = generatePageSql(sql, condition);
				ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
			} else {
			}
		}
		// 调用原始statementHandler的prepare方法完成原本的逻辑
		statementHandler = (StatementHandler) metaStatementHandler.getOriginalObject();
		statementHandler.prepare((Connection) invocation.getArgs()[0]);
		// 传递给下一个拦截器处理
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		dialect = properties.getProperty("dialect");
		if (dialect == null || "".equals(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}

	private SqlSource buildSqlSource(Configuration configuration, String originalSql, Class<?> parameterType) {
		SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
		return builder.parse(originalSql, parameterType, null);
	}

	@SuppressWarnings("unchecked")
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler<Object> typeHandler = (TypeHandler<Object>) parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	private String generatePageSql(String sql, Conditionable condition) {
		if (condition != null && (dialect != null || !dialect.equals(""))) {
			StringBuffer pageSql = new StringBuffer();
			switch (dialect) {
			case "mysql":
				if (condition.getSorter() == null) {
					pageSql.append(sql);
				} else {
					if (sql.endsWith(" limit 1")) {
						pageSql.append(sql.substring(0, sql.length() - 8));
						pageSql.append(condition.getSorter().toSql());
						pageSql.append(" limit 1");
					} else {
						pageSql.append(sql);
						pageSql.append(condition.getSorter().toSql());
					}
				}
				if (condition.getLimiter() != null) {
					pageSql.append(" limit " + condition.getLimiter().getLimitFrom() + ","
							+ condition.getLimiter().getPageSize());
				}
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}
}

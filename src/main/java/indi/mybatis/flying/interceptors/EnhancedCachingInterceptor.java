package indi.mybatis.flying.interceptors;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.TransactionalCacheManager;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.alibaba.fastjson.JSON;

import indi.mybatis.flying.cache.CacheKeysPool;
import indi.mybatis.flying.cache.EnhancedCachingManager;
import indi.mybatis.flying.cache.EnhancedCachingManagerImpl;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.utils.CookOriginalSql;

@Intercepts(value = {
		@Signature(args = { MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }, method = "query", type = Executor.class),
		@Signature(args = { MappedStatement.class, Object.class }, method = "update", type = Executor.class),
		@Signature(args = { boolean.class }, method = "commit", type = Executor.class),
		@Signature(args = { boolean.class }, method = "rollback", type = Executor.class),
		@Signature(args = { boolean.class }, method = "close", type = Executor.class) })
public class EnhancedCachingInterceptor implements Interceptor {
	private CacheKeysPool queryCacheOnCommit = new CacheKeysPool();
	private Set<String> updateStatementOnCommit = new HashSet<String>();
	EnhancedCachingManager cachingManager = EnhancedCachingManagerImpl.getInstance();
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		String name = invocation.getMethod().getName();
		Object result = null;
		if ("query".equals(name)) {
			result = this.processQuery(invocation);
		} else if ("update".equals(name)) {
			result = this.processUpdate(invocation);
		} else if ("commit".equals(name)) {
			result = this.processCommit(invocation);
		} else if ("rollback".equals(name)) {
			result = this.processRollback(invocation);
		} else if ("close".equals(name)) {
			result = this.processClose(invocation);
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * when executing a query operation 1. record this statement's id and it's
	 * corresponding Cache Object into Global Caching Manager; 2. record this
	 * statement's id and
	 * 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	protected Object processQuery(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		if (cachingManager.isCacheEnabled()) {
			Object[] args = invocation.getArgs();
			MappedStatement mappedStatement = (MappedStatement) args[0];

			/*
			 * The querCacheOnCommit cache is cleared if the query statement
			 * represented by this article statementd is configured with
			 * flushCache=true.
			 */
			if (mappedStatement.isFlushCacheRequired()) {
				queryCacheOnCommit.clear();
			}
			/*
			 * If the query statement represented by this article statement-d is
			 * configured to use the cache, and the second level cache is not
			 * empty, then the statement-d and the corresponding secondary cache
			 * object mapping relationship are added to the global cache mapping
			 * manager.
			 */
			if (mappedStatement.isUseCache() && mappedStatement.getCache() != null) {
				cachingManager.appendStatementCacheMap(mappedStatement.getId(), mappedStatement.getCache());
			}

			Object parameter = args[1];
			RowBounds rowBounds = (RowBounds) args[2];
			Executor executor = (Executor) invocation.getTarget();
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			FlyingModel flyingModel = CookOriginalSql.fetchFlyingFeature(boundSql.getSql());
			/* Record the CacheKey generated by this query. */
			CacheKey cacheKey = createCacheKey(mappedStatement, parameter, rowBounds, boundSql, executor);

			if (flyingModel.isHasFlyingFeature()) {
				switch (flyingModel.getActionType()) {
				case count:
					cacheKey.update(DigestUtils.md5Hex(JSON.toJSONString(parameter)));
					break;
				case selectAll:
					cacheKey.update(DigestUtils.md5Hex(JSON.toJSONString(parameter)));
					break;
				case selectOne:
					cacheKey.update(DigestUtils.md5Hex(JSON.toJSONString(parameter)));
					break;
				default:
					break;
				}
			}
			queryCacheOnCommit.putElement(mappedStatement.getId(), cacheKey);

			/* Handle paging */
			ResultHandler resultHandler = (ResultHandler) args[3];
			Executor executorProxy = (Executor) invocation.getTarget();
			MetaObject metaParameter = MetaObject.forObject(parameter, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			MetaObject metaExecutor = MetaObject.forObject(executorProxy, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			/*
			 * When paging queries are required, page information is added to
			 * the cache.
			 */
			Cache cache = mappedStatement.getCache();
			Object value = cache.getObject(cacheKey);
			if (metaExecutor.hasGetter("delegate")) {
				TransactionalCacheManager tcm = (TransactionalCacheManager) metaExecutor.getValue("tcm");
				Executor delegate = (Executor) metaExecutor.getValue("delegate");

				Object list = delegate.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);

				if (value != null) {
					return value;
				} else {
					tcm.putObject(cache, cacheKey, list);
					return list;
				}
			}
		}

		return result;
	}

	protected Object processUpdate(Invocation invocation) throws Throwable {

		Object result = invocation.proceed();
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		updateStatementOnCommit.add(mappedStatement.getId());
		return result;
	}

	protected Object processCommit(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		refreshCache();
		return result;
	}

	protected Object processRollback(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		clearSessionData();
		return result;
	}

	protected Object processClose(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		boolean forceRollback = (Boolean) invocation.getArgs()[0];
		if (forceRollback) {
			clearSessionData();
		} else {
			refreshCache();
		}
		return result;
	}

	/**
	 * When the SqlSession executes the commit(), rollback(), close() method,
	 * the CacheKey set generated by the Session level query statement and the
	 * set of updated statements executed should be cleared.
	 * 
	 */
	private synchronized void clearSessionData() {
		queryCacheOnCommit.clear();
		updateStatementOnCommit.clear();
	}

	/**
	 * refresh the session cache,there are three things have to do: 1. add this
	 * session scope query logs to global cache Manager 2. clear the related
	 * caches according to the update statements as configured in "dependency"
	 * file 3. clear the session data
	 */
	private synchronized void refreshCache() {
		cachingManager.refreshCacheKey(queryCacheOnCommit);
		// clear the related caches
		cachingManager.clearRelatedCaches(updateStatementOnCommit);
		clearSessionData();
	}

	@Override
	public void setProperties(Properties properties) {

		if (!cachingManager.isInitialized()) {
			cachingManager.initialize(properties);
		}
	}

	private CacheKey createCacheKey(MappedStatement mappedStatement, Object parameter, RowBounds rowBounds,
			BoundSql boundSql, Executor executor) {
		MetaObject metaObject = MetaObject.forObject(parameter, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
				DEFAULT_REFLECTOR_FACTORY);
		/*
		 * When paging queries are required, the current page and page number
		 * parameter is added to the cachekey.
		 */
		if (metaObject.getOriginalObject() instanceof Conditionable) {
			CacheKey cacheKey = new CacheKey();
			cacheKey.update(mappedStatement.getId());
			cacheKey.update(rowBounds.getOffset());
			cacheKey.update(rowBounds.getLimit());
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			cacheKey.update(boundSql.getSql());

			if (parameterMappings.size() > 0 && parameter != null) {
				TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
				if (typeHandlerRegistry.hasTypeHandler(parameter.getClass())) {
					cacheKey.update(parameter);
				} else {
					for (ParameterMapping parameterMapping : parameterMappings) {
						String propertyName = parameterMapping.getProperty();
						if (metaObject.hasGetter(propertyName)) {
							cacheKey.update(metaObject.getValue(propertyName));
						} else if (boundSql.hasAdditionalParameter(propertyName)) {
							cacheKey.update(boundSql.getAdditionalParameter(propertyName));
						}
					}
				}
			}
			Sortable sorter = ((Conditionable) metaObject.getOriginalObject()).getSorter();
			if (sorter != null) {
				cacheKey.update(sorter.toSql());
			}
			Limitable limiter = ((Conditionable) metaObject.getOriginalObject()).getLimiter();
			if (limiter != null) {
				cacheKey.update(limiter.getPageNo());
				cacheKey.update(limiter.getPageSize());
			}
			return cacheKey;
		} else {
			return executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
		}
	}

}

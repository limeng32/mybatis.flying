package indi.mybatis.flying.interceptors;

import java.sql.SQLException;
import java.util.HashMap;
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
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.statics.ActionType;

@Intercepts(value = {
		@Signature(args = { MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }, method = "query", type = Executor.class),
		@Signature(args = { MappedStatement.class, Object.class }, method = "update", type = Executor.class),
		@Signature(args = { boolean.class }, method = "commit", type = Executor.class),
		@Signature(args = { boolean.class }, method = "rollback", type = Executor.class),
		@Signature(args = { boolean.class }, method = "close", type = Executor.class) })
public class EnhancedCachingInterceptor2 implements Interceptor {
	private static CacheKeysPool queryCacheOnCommit = new CacheKeysPool();
	private static Set<String> updateStatementOnCommit = new HashSet<String>(16);
	private static EnhancedCachingManager cachingManager = EnhancedCachingManagerImpl.getInstance();
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

	private static final String FLYING = "_flying_";

	private static final String CLOSE = "close";
	private static final String COMMIT = "commit";
	private static final String QUERY = "query";
	private static final String ROLLBACK = "rollback";
	private static final String UPDATE = "update";

	private static final String H = "h";
	private static final String TARGET = "target";

	private static final String DELEGATE = "delegate";
	private static final String TCM = "tcm";
	private static final String DIRTY = "dirty";
	private static final String LIMITER = "limiter";
	private static final String LIST = "list";

	private static final String DOT = ".";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		String name = invocation.getMethod().getName();
		Object result = null;
		switch (name) {
		case CLOSE:
			result = processClose(invocation);
			break;
		case COMMIT:
			result = processCommit(invocation);
			break;
		case QUERY:
			result = processQuery(invocation);
			break;
		case ROLLBACK:
			result = processRollback(invocation);
			break;
		case UPDATE:
			result = processUpdate(invocation);
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
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
	private Object processQuery(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		if (cachingManager.isCacheEnabled()) {
			Object[] args = invocation.getArgs();
			MappedStatement mappedStatement = (MappedStatement) args[0];

			/*
			 * 如果本条statementId表示的查询语句配置了 flushCache=true，则清空querCacheOnCommit缓存
			 */
			if (mappedStatement.isFlushCacheRequired()) {
				queryCacheOnCommit.clear();
			}
			/*
			 * 如果本条statementId表示的查询语句配置了使用缓存，并且二级缓存不为空，
			 * 则将StatementId和对应的二级缓存对象映射关系添加到全局缓存映射管理器中
			 */
			if (mappedStatement.isUseCache() && mappedStatement.getCache() != null) {
				cachingManager.appendStatementCacheMap(mappedStatement.getId(), mappedStatement.getCache());
			}

			Object parameter = args[1];
			RowBounds rowBounds = (RowBounds) args[2];
			Executor executor = (Executor) invocation.getTarget();
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);

			// 记录本次查询所产生的CacheKey
			CacheKey cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
			queryCacheOnCommit.putElement(mappedStatement.getId(), cacheKey);

			Executor executorProxy = (Executor) invocation.getTarget();
			MetaObject metaExecutor = MetaObject.forObject(executorProxy, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			/* 分离代理对象链 */
			while (metaExecutor.hasGetter(H)) {
				Object object = metaExecutor.getValue(H);
				metaExecutor = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
						DEFAULT_REFLECTOR_FACTORY);
			}
			/* 分离最后一个代理对象的目标类 */
			while (metaExecutor.hasGetter(TARGET)) {
				Object object = metaExecutor.getValue(TARGET);
				metaExecutor = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
						DEFAULT_REFLECTOR_FACTORY);
			}

			return this.query(metaExecutor, args);
		}

		return result;
	}

	private Object processUpdate(Invocation invocation) throws Throwable {

		Object result = invocation.proceed();
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		updateStatementOnCommit.add(mappedStatement.getId());
		return result;
	}

	private Object processCommit(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		refreshCache();
		return result;
	}

	private Object processRollback(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		clearSessionData();
		return result;
	}

	private Object processClose(Invocation invocation) throws Throwable {
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
	 * when the sqlSession has been committed,rollbacked,or closed, session
	 * buffer query CacheKeys and update Statement collections should be
	 * cleared.
	 * 
	 * 当SqlSession 执行了commit()、rollback()、close()方法，
	 * Session级别的查询语句产生的CacheKey集合以及 执行的更新语句集合应该被清空
	 */
	private synchronized void clearSessionData() {
		queryCacheOnCommit.clear();
		updateStatementOnCommit.clear();
	}

	/**
	 * refresh the session cache,there are two things have to do: 1. add this
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

	/**
	 * Executor插件配置信息加载点 properties中有 "dependency" 属性来指示
	 * 配置的缓存依赖配置信息，读取文件，初始化EnhancedCacheManager
	 */
	@Override
	public void setProperties(Properties properties) {

		if (!cachingManager.isInitialized()) {
			cachingManager.initialize(properties);
		}
	}

	private <E> List<E> query(MetaObject metaExecutor, Object[] args) throws SQLException {
		MappedStatement mappedStatement = (MappedStatement) args[0];
		Object parameter = args[1];
		RowBounds rowBounds = (RowBounds) args[2];
		ResultHandler resultHandler = (ResultHandler) args[3];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		CacheKey cacheKey = createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
		return this.query(metaExecutor, mappedStatement, cacheKey, parameter, rowBounds, resultHandler, boundSql);
	}

	@SuppressWarnings("unchecked")
	private <E> List<E> query(MetaObject metaExecutor, MappedStatement mappedStatement, CacheKey cacheKey,
			Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
		MetaObject metaParameter = MetaObject.forObject(parameter, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		/* 当需要分页查询时，缓存里加入page信息 */
		if (metaParameter.getOriginalObject() instanceof Conditionable) {
			Cache cache = mappedStatement.getCache();
			if (cache != null && metaExecutor.hasGetter(DELEGATE)) {
				if (mappedStatement.isUseCache() && resultHandler == null && cache.getReadWriteLock() != null) {
					if (!mappedStatement.isFlushCacheRequired()) {
						// tcm.clear(cache);
						cache.getReadWriteLock().readLock().lock();
						try {
							synchronized (cache) {
								Object value = cache.getObject(cacheKey);
								if (value != null) {
									HashMap<String, Object> cachedMap = (HashMap<String, Object>) value;
									Limitable cachedPage = (Limitable) cachedMap.get(LIMITER);
									Limitable originalPage = ((Conditionable) metaParameter.getOriginalObject())
											.getLimiter();
									if (null != originalPage && null != cachedPage) {
										originalPage.setTotalCount(cachedPage.getTotalCount());
										return (List<E>) cachedMap.get(LIST);
									}
								} else {
								}
							}
						} finally {
							cache.getReadWriteLock().readLock().unlock();
						}
					}
				}
				TransactionalCacheManager tcm = (TransactionalCacheManager) metaExecutor.getValue(TCM);
				Executor delegate = (Executor) metaExecutor.getValue(DELEGATE);
				List<E> list = delegate.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
				HashMap<String, Object> cachedMap = new HashMap<String, Object>();
				cachedMap.put(LIMITER, metaParameter.getValue(LIMITER));
				cachedMap.put(LIST, list);
				tcm.putObject(cache, cacheKey, cachedMap);
				return list;
			}
		}
		Executor executor = (Executor) metaExecutor.getOriginalObject();
		return executor.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
	}

	private boolean isSelectType(String statementId) {
		boolean ret = false;
		String id = statementId.substring(statementId.lastIndexOf(DOT) + 1);
		ActionType actionType = ActionType.valueOf(id);
		switch (actionType) {
		case count:
			ret = true;
			break;
		case select:
			ret = true;
			break;
		case selectAll:
			ret = true;
			break;
		case selectOne:
			ret = true;
			break;
		default:
		}
		return ret;
	}

	private CacheKey createCacheKey(MappedStatement mappedStatement, Object parameter, RowBounds rowBounds,
			BoundSql boundSql) {
		CacheKey cacheKey = new CacheKey();
		cacheKey.update(mappedStatement.getId());
		cacheKey.update(rowBounds.getOffset());
		cacheKey.update(rowBounds.getLimit());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		cacheKey.update(boundSql.getSql());
		MetaObject metaObject = MetaObject.forObject(parameter, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
				DEFAULT_REFLECTOR_FACTORY);

		if (parameterMappings.size() > 0 && parameter != null) {
			TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameter.getClass())) {
				cacheKey.update(parameter);
			} else {
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (FLYING.equals(propertyName) || propertyName == null || "id".equals(propertyName)) {
						if (isSelectType(mappedStatement.getId())) {
							String _cacheKey = DigestUtils.md5Hex(JSON.toJSONString(parameter));
							cacheKey.update(_cacheKey);
						}
					} else if (metaObject.hasGetter(propertyName)) {
						cacheKey.update(metaObject.getValue(propertyName));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						cacheKey.update(boundSql.getAdditionalParameter(propertyName));
					}
				}
			}
		}
		/* 当需要分页查询时，将page参数里的当前页和每页数加到cachekey里 */
		if (metaObject.getOriginalObject() instanceof Conditionable) {
			Sortable sorter = ((Conditionable) metaObject.getOriginalObject()).getSorter();
			if (sorter != null) {
				cacheKey.update(sorter.toSql());
			}
			Limitable limiter = ((Conditionable) metaObject.getOriginalObject()).getLimiter();
			if (limiter != null) {
				cacheKey.update(limiter.getPageNo());
				cacheKey.update(limiter.getPageSize());
			}
		}
		return cacheKey;
	}
}

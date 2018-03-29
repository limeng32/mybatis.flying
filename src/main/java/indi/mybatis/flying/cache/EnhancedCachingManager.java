package indi.mybatis.flying.cache;

import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.cache.Cache;

/**
 * Global Cache Manager for MyBatis, manages all Secondary Caches in MyBatis.
 * The manager maintains the CacheKey collection generated by all queries from
 * MyBatis. When an update or delete operation is executed, the associated cache
 * is removed according to the state corresponding to this operation.
 * 
 * @author louluan,limeng32
 */
public interface EnhancedCachingManager {

	public boolean isInitialized();

	/**
	 * Whether MyBatis open the secondary cache or not, namely {property
	 * name="cacheEnabled" value="true"/}
	 * 
	 * @return boolean
	 */
	public boolean isCacheEnabled();

	/**
	 * @param properties
	 *            Properties contain at least two properties:annotationPackage
	 *            and cacheEnabled.
	 * 
	 *            annotationPackage: the package name where the pojomapper.java
	 *            file is located, and multiple packages can be separated by
	 *            English commas.
	 * 
	 *            cacheEnbled: "true" or "false", indicates whether the cache is
	 *            used.
	 */
	public void initialize(Properties properties);

	/**
	 * Update the data in the CacheKey buffer pool from the Session level to the
	 * global CacheKey buffer pool.
	 * 
	 * @param sessionCacheKeysPool
	 * 
	 */
	public void refreshCacheKey(CacheKeysPool sessionCacheKeysPool);

	/**
	 * The core of the entire plug-in, based on the specified update or delete
	 * statementId updates associated with the second level cache,
	 * EnhancedCachingManager will go to see if by querying the corresponding
	 * statementId configured the dependencies, if any, will remove the query
	 * cache.
	 * 
	 * @param set
	 */
	public void clearRelatedCaches(Set<String> set);

	/**
	 * If MyBatis opens the second level Cache, and the query StatementId USES
	 * the second level Cache, and the corresponding Mapper configuration Cache,
	 * this StatementId and Cache in the form of key-value pairs stored in the
	 * global Cache manager.
	 * 
	 * @param statementId
	 * 
	 * @param cache
	 */
	public void appendStatementCacheMap(String statementId, Cache cache);

}

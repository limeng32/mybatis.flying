package indi.mybatis.flying.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheKey缓冲池 记录MyBatis的每次查询的查询所产生的CacheKey 对于由某个特定的statementId
 * 所执行的查询，其查询所产生的CacheKey存放到一个Set{Ojbect}集合中， 以{statementId
 * ,Set{Object}}的形式存储到CacheKey缓冲池中
 * 
 * @author louluan,limeng32
 */
public class CacheKeysPool {
	private Map<String, Set<Object>> pool = new ConcurrentHashMap<String, Set<Object>>();

	public Set<Object> get(String key) {
		if (pool.get(key) == null) {
			pool.put(key, new HashSet<Object>());
		}
		return pool.get(key);
	}

	public Set<Object> put(String key, Set<Object> value) {
		return pool.put(key, value);
	}

	public void putElement(String key, Object element) {
		if (pool.get(key) == null) {
			pool.put(key, new HashSet<Object>());
		}
		pool.get(key).add(element);
	}

	public Set<Object> remove(String key) {
		return pool.remove(key);
	}

	public void clear() {
		pool.clear();
	}

	public Set<String> keySet() {
		return pool.keySet();
	}

	public Map<String, Set<Object>> getOriginalPool() {
		return pool;
	}

	public Set<Entry<String, Set<Object>>> entrySet() {
		return this.pool.entrySet();
	}

	public void putAll(CacheKeysPool pool) {
		for (Entry<String, Set<Object>> entry : pool.entrySet()) {
			for (Object item : entry.getValue()) {
				this.putElement(entry.getKey(), item);
			}
		}
	}
}

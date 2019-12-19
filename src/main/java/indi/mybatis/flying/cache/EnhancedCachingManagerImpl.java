package indi.mybatis.flying.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.ibatis.cache.Cache;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.utils.ReflectHelper;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class EnhancedCachingManagerImpl implements EnhancedCachingManager {

	private Map<String, Set<String>> observers = new ConcurrentHashMap<>();
	private Map<Class<?>, Set<Method>> triggerMethods = new ConcurrentHashMap<>();
	private ConcurrentSkipListMap<Class<?>, Set<Method>> observerMethods = new ConcurrentSkipListMap<>(
			new ClassComparator());
	private ConcurrentSkipListMap<Class<?>, Set<Method>> observerMethodsNew = new ConcurrentSkipListMap<>(
			new ClassComparator());
	private Map<Class<?>, Set<Class<?>>> observerClasses = new ConcurrentHashMap<>();
	private Map<Class<?>, Set<Class<?>>> triggerClasses = new ConcurrentHashMap<>();

	private Map<Class<?>, Set<Class<?>>> observersClassesNew = new ConcurrentHashMap<>();

	private CacheKeysPool sharedCacheKeysPool = new CacheKeysPool();
	private Map<String, Cache> holds = new ConcurrentHashMap<String, Cache>();
	private boolean initialized = false;
	private boolean cacheEnabled = false;

	private static EnhancedCachingManagerImpl enhancedCacheManager;

	private EnhancedCachingManagerImpl() {
	}

	public static EnhancedCachingManagerImpl getInstance() {
		if (enhancedCacheManager == null) {
			enhancedCacheManager = new EnhancedCachingManagerImpl();
		}
		return enhancedCacheManager;
	}

	@Override
	public void refreshCacheKey(CacheKeysPool keysPool) {
		sharedCacheKeysPool.putAll(keysPool);
	}

	@Override
	public void clearRelatedCaches(final Set<String> set) {
		for (String observable : set) {
			Set<String> relatedStatements = observers.get(observable);
			if (relatedStatements != null) {
				for (String statementId : relatedStatements) {
					Cache cache = holds.get(statementId);
					Set<Object> cacheKeys = sharedCacheKeysPool.get(statementId);
					for (Object cacheKey : cacheKeys) {
						cache.removeObject(cacheKey);
					}
				}
			}
			/* clear shared cacheKey Pool width specific key */
			sharedCacheKeysPool.remove(observable);
		}
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void initialize(Properties properties) {
		initialized = true;
		if ("true".equals(properties.getProperty("cacheEnabled", "true"))) {
			this.cacheEnabled = true;
		}
		String annotationPackages = properties.getProperty("annotationPackage");
		String[] annotationPackageNames = annotationPackages.split(",");
		Set<Class<?>> classes = new HashSet<>();
		for (String annotationPackageName : annotationPackageNames) {
			Package annotationPackage = Package.getPackage(annotationPackageName);
			if (annotationPackage != null) {
				classes.addAll(ReflectHelper.getClasses(annotationPackageName));
			}
		}

		dealPackageInit(classes);
		dealObserverClasses();
		dealPackageInit2(classes);
	}

	/* expand the value in observerClasses */
	private void dealObserverClasses() {
		for (Entry<Class<?>, Set<Class<?>>> e : observerClasses.entrySet()) {
			Set<Class<?>> set = new HashSet<Class<?>>();
			observerClassesFission(e.getKey(), set);
			observersClassesNew.put(e.getKey(), set);
		}
	}

	private void observerClassesFission(Class<?> clazz, Set<Class<?>> set) {
		if (observerClasses.containsKey(clazz)) {
			Set<Class<?>> set2 = observerClasses.get(clazz);
			for (Class<?> e : set2) {
				Class<?> e2 = getKeyFormValue(e);
				set.add(e);
				if (observerClasses.get(e2) != null && !observerClasses.get(e2).isEmpty()) {
					observerClassesFission(e2, set);
				}
			}
		}
	}

	/* get the key through value in triggerClasses */
	private Class<?> getKeyFormValue(Class<?> clazz) {
		for (Entry<Class<?>, Set<Class<?>>> e : triggerClasses.entrySet()) {
			if (e.getValue().contains(clazz)) {
				return e.getKey();
			}
		}
		return null;
	}

	private void dealPackageInit(Set<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
			for (Annotation an : classAnnotations) {
				if (an instanceof CacheRoleAnnotation) {
					if (!observerClasses.containsKey(clazz)) {
						observerClasses.put(clazz, new HashSet<Class<?>>());
					}
					if (!triggerClasses.containsKey(clazz)) {
						triggerClasses.put(clazz, new HashSet<Class<?>>());
					}
					CacheRoleAnnotation cacheRoleAnnotation = (CacheRoleAnnotation) an;
					for (Class<?> clazz1 : cacheRoleAnnotation.observerClass()) {
						observerClasses.get(clazz).add(clazz1);
					}
					for (Class<?> clazz1 : cacheRoleAnnotation.triggerClass()) {
						triggerClasses.get(clazz).add(clazz1);
					}
				}
			}
		}
	}

	private void dealPackageInit2(Set<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			for (Method method : clazz.getDeclaredMethods()) {
				CacheAnnotation cacheAnnotation = method.getAnnotation(CacheAnnotation.class);
				if (cacheAnnotation != null) {
					dealPackageInit21(clazz, method, cacheAnnotation);
				}
			}
		}
		observerMethodsFission(observerMethods, null, observerMethodsNew);
		buildObservers(triggerMethods, observerMethodsNew);
	}

	private void dealPackageInit21(Class<?> clazz, Method method, CacheAnnotation cacheAnnotation) {
		switch (cacheAnnotation.role()) {
		case Observer:
			for (Class<?> clazz1 : observersClassesNew.get(clazz)) {
				if (!observerMethods.containsKey(clazz1)) {
					observerMethods.put(clazz1, new HashSet<Method>());
				}
				observerMethods.get(clazz1).add(method);
			}
			break;
		case Trigger:
			for (Class<?> clazz1 : triggerClasses.get(clazz)) {
				if (!triggerMethods.containsKey(clazz1)) {
					triggerMethods.put(clazz1, new HashSet<Method>());
				}
				triggerMethods.get(clazz1).add(method);
			}
			break;
		default:
			break;
		}
	}

	private void observerMethodsFission(ConcurrentSkipListMap<Class<?>, Set<Method>> observerMethodMap,
			Entry<Class<?>, Set<Method>> currentE2, ConcurrentSkipListMap<Class<?>, Set<Method>> observerMethodMapNew) {
		Entry<Class<?>, Set<Method>> currentE = currentE2;
		if (!observerMethodMap.isEmpty()) {
			if (currentE == null) {
				currentE = observerMethodMap.firstEntry();
			}
			observerMethodMapNew.put(currentE.getKey(), currentE.getValue());
			observerMethodsFissionFission(observerMethodMap, observerMethodMapNew);
			Entry<Class<?>, Set<Method>> nextE = observerMethodMap.higherEntry(currentE.getKey());
			if (nextE != null) {
				observerMethodsFission(observerMethodMap, nextE, observerMethodMapNew);
			}
		}
	}

	private void observerMethodsFissionFission(ConcurrentSkipListMap<Class<?>, Set<Method>> observerMethodMap,
			ConcurrentSkipListMap<Class<?>, Set<Method>> observerMethodMapNew) {
		Set<Method> tempSet = new LinkedHashSet<>();
		Entry<Class<?>, Set<Method>> lastE = observerMethodMapNew.lastEntry();
		int size1 = lastE.getValue().size();
		for (Method methob : lastE.getValue()) {
			if ("select".equals(methob.getName())) {
				Class<?> clazz = methob.getReturnType();
				if (observerMethodMap.containsKey(clazz)) {
					tempSet.addAll(observerMethodMap.get(clazz));
				}
			}
		}
		lastE.getValue().addAll(tempSet);
		int size2 = lastE.getValue().size();
		if (size1 != size2) {
			observerMethodsFissionFission(observerMethodMap, observerMethodMapNew);
		}
	}

	@Override
	public void appendStatementCacheMap(String statementId, Cache cache) {
		if (holds.containsKey(statementId) && holds.get(statementId) != null) {
			return;
		}
		holds.put(statementId, cache);
	}

	@Override
	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	private void buildObservers(Map<Class<?>, Set<Method>> triggerMethodMap,
			Map<Class<?>, Set<Method>> observerMethodMap) {
		for (Map.Entry<Class<?>, Set<Method>> e : triggerMethodMap.entrySet()) {
			Set<Method> observerMethods2 = observerMethodMap.get(e.getKey());
			if (observerMethods2 != null) {
				for (Method triggerMethod : triggerMethodMap.get(e.getKey())) {
					String triggerFullName = triggerMethod.getDeclaringClass().getName() + "."
							+ triggerMethod.getName();
					if (!observers.containsKey(triggerFullName)) {
						observers.put(triggerFullName, new HashSet<String>());
					}
					for (Method observerMethod : observerMethods2) {
						String observerFullName = observerMethod.getDeclaringClass().getName() + "."
								+ observerMethod.getName();
						observers.get(triggerFullName).add(observerFullName);
					}
				}
			}
		}
	}
}

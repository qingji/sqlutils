package im.qingji.sqlutil.mapping.cache;

import im.qingji.sqlutil.mapping.MappingMetadata;
import im.qingji.sqlutil.mapping.load.MetadataLoader;
import im.qingji.sqlutil.mapping.load.impl.AnnotationMappingLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-19 下午02:53:09
 */
public class MetadataCache {

	private static final Map<Class<?>, MappingMetadata> CACHE = new ConcurrentHashMap<Class<?>, MappingMetadata>();
	private static MetadataLoader metaDataLoader = new AnnotationMappingLoader();
	
	public static MappingMetadata cache(Class<?> clazz) {
		MappingMetadata result = CACHE.get(clazz);
		if(result == null) {
			result = metaDataLoader.load(clazz);
			if(result != null) {
				CACHE.put(clazz, result);
			}
		}
		return result;
	}
	
	public static void clear() {
		CACHE.clear();
	}
	
	public static void remove(Object key) {
		CACHE.remove(key);
	}
	
	private MetadataCache() {}
	
	static class MappingCacheManager {
		
		
	}
}

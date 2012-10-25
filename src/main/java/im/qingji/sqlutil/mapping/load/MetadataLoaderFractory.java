package im.qingji.sqlutil.mapping.load;

import im.qingji.sqlutil.mapping.load.impl.AnnotationMappingLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author qingji
 * <br>Created on 2011-7-21 下午04:08:11
 */
public class MetadataLoaderFractory {
	
	static final Logger logger = LoggerFactory.getLogger(MetadataLoaderFractory.class);

	private static MetadataLoader DEFAULT_LOADER = new AnnotationMappingLoader();
	private static Map<String, MetadataLoader> LOADER_MAP = new ConcurrentHashMap<String, MetadataLoader>();
	
	/**
	 * 获取默认的MetadataLoader
	 * @return
	 */
	public static MetadataLoader getLoader() {
		return DEFAULT_LOADER;
	}
	/**
	 * 根据className获取MetadataLoader
	 * @return
	 */
	public static MetadataLoader getLoader(String className) {
		MetadataLoader loader = LOADER_MAP.get(className);
		if( loader == null) {
			try {
				Object obj = Class.forName(className).newInstance();
				if(obj instanceof MetadataLoader) {
					loader = (MetadataLoader)obj;
					registe(className, loader);
				}
				return loader;
			} catch (Exception e) {
				logger.warn("加载{}失败。", className,e);
			}
		}
		return getDefaultLoader();
	}
	
	 /**
	  * 设置默认的加载器
	  * @param loader
	  */
	public static void setDefaultLoader(MetadataLoader loader) {
		DEFAULT_LOADER = loader;
	}
	/**
	 * 获取默认的加载器
	 * @return
	 */
	public static MetadataLoader getDefaultLoader() {
		return DEFAULT_LOADER;
	}
	
	/**
	 * 注册加载器
	 * @param className
	 * @param loader
	 * @return
	 */
	public static MetadataLoader registe(String className, MetadataLoader loader) {
		return LOADER_MAP.put(className, loader);
	}
}

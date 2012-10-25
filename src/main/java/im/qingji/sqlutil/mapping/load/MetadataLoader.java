package im.qingji.sqlutil.mapping.load;

import im.qingji.sqlutil.mapping.MappingMetadata;



/**
 * 映射元数据加载器。
 * @author qingji
 * <br>Created on 2011-7-21 下午02:39:58
 */
public interface MetadataLoader {

	/**
	 * 加载该类的映射元数据。
	 * @param clazz
	 * @return
	 */
	public MappingMetadata load(Class<?> clazz);
}

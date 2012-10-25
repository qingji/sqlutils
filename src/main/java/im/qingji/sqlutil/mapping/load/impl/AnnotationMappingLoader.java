package im.qingji.sqlutil.mapping.load.impl;

import im.qingji.sqlutil.mapping.FieldColumnPair;
import im.qingji.sqlutil.mapping.FieldMap;
import im.qingji.sqlutil.mapping.MappingMetadata;
import im.qingji.sqlutil.mapping.cache.MetadataCache;
import im.qingji.sqlutil.mapping.load.MetadataLoader;

import java.util.Map;

/**
 * 
 * @author qingji
 */
public class AnnotationMappingLoader implements MetadataLoader {

	/**
	 * (non-Javadoc)
	 * 
	 * @see im.qingji.sqlutil.mapping.MetadataLoader.MetaDataLoader#load(java.lang.Class)
	 */
	@Override
	public MappingMetadata load(Class<?> clazz) {
		AnnotationParser parser = new AnnotationParser(clazz);

		Class<?> superClass = clazz.getSuperclass();
		MappingMetadata parentMetadata = null;
		MappingMetadata metadata = new MappingMetadata();
		if (superClass != null && !Object.class.equals(superClass)) {
			//TODO 直接从缓存中加载父类信息？
			parentMetadata = MetadataCache.cache(superClass);
			if (parentMetadata == null) {
				metadata.setParentclass(superClass);
				parentMetadata = load(superClass);
			}
		}
		metadata.setSelfClass(clazz);

		String tableName = parser.loadTableName();
		metadata.setTableName(tableName);

		FieldMap fieldMap = new FieldMap();
		// 当发现父类annotations不为空，加载父类的annotations
		if (parentMetadata != null) {
			Map<String, FieldColumnPair> tmpFieldMap = getFields(parentMetadata);
			fieldMap.putAll(tmpFieldMap);
		}
		Map<String, FieldColumnPair> tmpFieldMap = parser.loadFieldMap();
		fieldMap.putAll(tmpFieldMap);
		metadata.setFieldMap(fieldMap);

		return metadata;
	}
	
	/**
	 * 加载父类的annotations
	 * 
	 * @param parentMetadata
	 * @param metadata
	 */
	private static Map<String, FieldColumnPair> getFields(MappingMetadata metadata) {
		if (metadata == null) {
			throw new NullPointerException("metadata is null");
		}
		if (metadata.getFieldMap() == null) {
			throw new NullPointerException("FieldMap is null");
		}
		return metadata.getFieldMap().getFields();
	}

}

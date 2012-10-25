package im.qingji.sqlutil.mapping;


/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 下午03:51:49
 */
public class MappingMetadata {

	private Class<?> selfClass;
	private FieldMap fieldMap;
	private Class<?> parentClass;
	private String tableName;
	
	
	/**
	 * 获取self class
	 * @return the selfClass
	 */
	public Class<?> getSelfClass() {
		return selfClass;
	}
	public FieldMap getFieldMap() {
		return fieldMap;
	}
	public Class<?> getParentclass() {
		return parentClass;
	}
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * 设置自身的class
	 * @param selfClass the selfClass to set
	 */
	public void setSelfClass(Class<?> selfClass) {
		this.selfClass = selfClass;
	}
	public void setFieldMap(FieldMap fieldMap) {
		this.fieldMap = fieldMap;
	}
	public void setParentclass(Class<?> parentClass) {
		this.parentClass = parentClass;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}

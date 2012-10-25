package im.qingji.sqlutil.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * 字段、数据库列对。
 * <p>
 * </p>
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 下午03:08:21
 */
public class FieldColumnPair {

	private Field field;
	private String columnName;
	private Class<?> columnType;
	
	private Method setter;
	private Method getter;
	
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	/**
	 * @return the column Name
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @return the column Type
	 */
	public Class<?> getColumnType() {
		return columnType;
	}
	/**
	 * @param columnName the column Name to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @param columnType the column Type to set
	 */
	public void setColumnType(Class<?> columnType) {
		this.columnType = columnType;
	}
	
	/**
	 * 获取Setter方法
	 * @return
	 */
	public Method getSetter() {
		return setter;
	}
	/**
	 * 获取Getter方法
	 * @return
	 */
	public Method getGetter() {
		return getter;
	}
	/**
	 * 设置Setter方法
	 * @return
	 */
	public void setSetter(Method setter) {
		this.setter = setter;
	}
	/**
	 * 设置Getter方法
	 * @return
	 */
	public void setGetter(Method getter) {
		this.getter = getter;
	}
}

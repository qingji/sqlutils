package im.qingji.sqlutil.mapping.load.impl;

import im.qingji.sqlutil.mapping.FieldColumnPair;
import im.qingji.sqlutil.mapping.annotation.Column;
import im.qingji.sqlutil.mapping.annotation.Table;
import im.qingji.sqlutil.support.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-19 下午03:46:28
 */
class AnnotationParser {
	private Class<?> clazz;

	public AnnotationParser(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public String loadTableName() {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			String classname = clazz.getName();
			return classname.substring(classname.lastIndexOf(".") + 1);
		} else {
			return table.name();
		}
	}

	public Map<String, FieldColumnPair> loadFieldMap() {
		Field[] fields = clazz.getDeclaredFields();
		Map<String, FieldColumnPair> fieldMap = new HashMap<String, FieldColumnPair>(fields.length);
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column == null) {
				continue;
			}
			FieldColumnPair pair = loadFieldColumnPair(clazz, field, column);
			//以ColumnName小写为KEY
			fieldMap.put(pair.getColumnName().toLowerCase(), pair);
		}
		return fieldMap;
	}

	/**
	 * @param clazz
	 * @param field
	 * @param column
	 * @return
	 */
	private FieldColumnPair loadFieldColumnPair(Class<?> clazz, Field field, Column column) {
		FieldColumnPair pair = new FieldColumnPair();

		pair.setField(field);
		if ("".equals(column.name()) || (column.name() == null)) {
			pair.setColumnName(field.getName());
		} else {
			pair.setColumnName(column.name());
		}
		//Class.class 为默认类型，故忽略。
		if (Class.class.equals(column.type()) || column.type() == null) {
			pair.setColumnType(field.getType());
		} else {
			pair.setColumnType(column.type());
		}

		Method getter = ReflectUtil.getGettersMethod(clazz, field);
		pair.setGetter(getter);

		Method setter = ReflectUtil.getSettersMethod(clazz, field);
		pair.setSetter(setter);
		return pair;
	}

}

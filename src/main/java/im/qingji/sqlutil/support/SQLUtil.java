package im.qingji.sqlutil.support;

import im.qingji.sqlutil.mapping.FieldColumnPair;
import im.qingji.sqlutil.mapping.MappingMetadata;
import im.qingji.sqlutil.mapping.cache.MetadataCache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * SQL语句生成工具
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-20 下午02:33:52
 */
public class SQLUtil {

	/**
	 * @see #insertSQL(Class, String)
	 * @param clazz
	 * @return
	 */
	public static String insertSQL(Class<?> clazz) {
		return insertSQL(clazz, "");
	}
	
	public static String simpleSQL(Class<?> clazz) {
		MappingMetadata metadata = MetadataCache.cache(clazz);
		StringBuilder sql = new StringBuilder("select ");
		List<FieldColumnPair> list = metadata.getFieldMap().getOrderedFields();
		for (FieldColumnPair pair : list) {
			sql.append(pair.getColumnName()).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(" from ").append(metadata.getTableName());
		return sql.toString();
	}
	
	/**
	 * 拼接INSERT语句。
	 * <p>
	 *   根据数据库字段名的ASCII码升序的方式排列(忽略大小写),具体代码如下:
	 *   <li>
	 *   <i>
	 *   insert into tablename(column_a, column_b, column_c,....,column_z) values (?,?,?,....,?)
	 *   </i>
	 *   </li>
	 * </p>
	 * @param clazz
	 * @param tableName
	 * @return
	 */
	public static String insertSQL(Class<?> clazz, String tableName) {
		MappingMetadata metadata = MetadataCache.cache(clazz);
		if (metadata == null) {
			throw new NullPointerException("没有找到"+ clazz + "相应的元信息，无法拼接SQL语句。");
		}
		List<FieldColumnPair> list = metadata.getFieldMap().getOrderedFields();

		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append(" insert into ");
		if( tableName == null || tableName.equals("")) {
			sb1.append(metadata.getTableName());
		} else {
			sb1.append(tableName);
		}
		sb1.append(" ( ");
		sb2.append(" values( ");

		for (int i = 0; i < list.size(); i++) {
			FieldColumnPair pair = list.get(i);
			sb1.append(pair.getColumnName()).append(Separator.COMMA);
			sb2.append(Separator.QUESTION).append(Separator.COMMA);
		}
		sb1.append(" ) ");
		sb2.append(" )");
		deleteLastComma(sb1);
		deleteLastComma(sb2);

		return sb1.append(sb2.toString()).toString().toUpperCase();
	}
	
	private static void deleteLastComma(StringBuilder sb) {
		if (sb == null || sb.length() <= 0) {
			throw new NullPointerException();
		}
		int flag = sb.lastIndexOf(Separator.COMMA);
		if (flag == -1) {
			throw new UnsupportedOperationException(sb.toString() + "中不包含逗号。");
		}
		sb.deleteCharAt(flag);
	}
	
	public static <E> Object[] getOrderedParams(E e) {
		Class<?> clazz = e.getClass();
		MappingMetadata metadata = MetadataCache.cache(clazz);
		if (metadata == null) {
			throw new NullPointerException("没有找到"+ clazz + "相应的元信息，无法拼接SQL语句。");
		}
		List<FieldColumnPair> list = metadata.getFieldMap().getOrderedFields();
		Object[] params = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			FieldColumnPair pair = list.get(i);
			Method getter = pair.getGetter();
			Object param = invokeGetter(e, getter);
			//类型转换
			if(param != null 
					&& (!pair.getColumnType().equals(param.getClass()))) {
				DataTypeUtil.convert(param, pair.getColumnType());
			}
			params[i] = param;
		}
		return params;
	}
	
	private static <E> Object invokeGetter(E bean, Method getter) {
		try {
			return getter.invoke(bean);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("参数" + bean + "不符合相对应的getter方法要求.", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}

package im.qingji.sqlutil.support;

import im.qingji.sqlutil.convert.Converter;
import im.qingji.sqlutil.convert.ConverterFactory;
import im.qingji.sqlutil.convert.UnsupportedTypeException;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 数据类型映射转换工具。
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 下午03:02:50
 */
public final class DataTypeUtil {

	/**
	 * type 集合
	 */
	private static final Map<Class<?>, Integer> TYPE_MAP = new HashMap<Class<?>, Integer>();

	static {
		registe(Array.class, Types.ARRAY);
		registe(Byte.class, Types.TINYINT);
		registe(Short.class, Types.SMALLINT);
		registe(Integer.class, Types.INTEGER);
		registe(Long.class, Types.BIGINT);
		registe(Float.class, Types.FLOAT);
		registe(Double.class, Types.DOUBLE);
		registe(BigDecimal.class, Types.NUMERIC);
		registe(Character.class, Types.CHAR);
		registe(Boolean.class, Types.BOOLEAN);
		registe(String.class, Types.VARCHAR);
		registe(Date.class, Types.DATE);
		registe(Time.class, Types.TIME);
		registe(Timestamp.class, Types.TIMESTAMP);
		registe(Clob.class, Types.CLOB);
		registe(Blob.class, Types.BLOB);
		registe(NClob.class, Types.NCLOB);
		registe(Ref.class, Types.REF);
		registe(RowId.class, Types.ROWID);
		registe(null, Types.NULL);
	}
	
	/**
	 * 注册关联SQL与Java的类型
	 * @param typeClass
	 * @param typeID
	 * @return
	 */
	public static synchronized Integer registe(Class<?> typeClass, Integer typeID) {
		return TYPE_MAP.put(typeClass, typeID);
	}
	
	/**
	 * 将数据转换成的字段的类型。
	 * @param data - 数据
	 * @param targetType - 字段的类型
	 * @return 当数据或fieldType为空时，返回null, 其余返回fieldType的类型数据。
	 */
	public static final Object convert(Object data, Class<?> targetType) {
		if(data == null || targetType == null) {
			return null;
		}
		if(data.getClass().equals(targetType)) {
			return data;
		}
		Converter converter = ConverterFactory.getConverter(data.getClass());
		if(converter == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("不支持")
					.append(data.getClass())
					.append("类型转换至")
					.append(targetType)
					.append("类型,请在").append(ConverterFactory.class.getName())
					.append("中注册相应的").append(Converter.class.getName());

			throw new UnsupportedTypeException( sb.toString());
		}
		return converter.convert(targetType, data);
	}
	
	/**
	 * 根据Class获取{@link Types}
	 * @param type
	 * @return Types
	 */
	public static final int getType(Object param) {
		if(!param.getClass().equals(Class.class)) {
			param = param.getClass();
		}
		return getType(param);
	}
	
	/**
	 * 根据Class获取{@link Types}
	 * @param type
	 * @return Types
	 */
	public static int getType(Class<?> clazz) {
		Integer sqlType = TYPE_MAP.get(clazz);
		if(sqlType != null) {
			return sqlType;
		}
		throw new IllegalArgumentException("不支持的类型" + clazz);
	}
	
}

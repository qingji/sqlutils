package im.qingji.sqlutil.convert;

import im.qingji.sqlutil.convert.impl.BigDecimalConverter;
import im.qingji.sqlutil.convert.impl.BooleanConverter;
import im.qingji.sqlutil.convert.impl.ClobConverter;
import im.qingji.sqlutil.convert.impl.DateConverter;
import im.qingji.sqlutil.convert.impl.NumberConverter;
import im.qingji.sqlutil.convert.impl.StringConverter;
import im.qingji.sqlutil.convert.impl.TimestampConverter;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 上午11:46:52
 */
public class ConverterFactory {

	private static Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>();
	final static Logger LOGGER = LoggerFactory.getLogger(ConverterFactory.class);
	static{
		registe(Date.class, new DateConverter());
		registe(Timestamp.class, new TimestampConverter());
		registe(BigDecimal.class, new BigDecimalConverter());
		registe(String.class, new StringConverter());
		
		registe(Boolean.class, new BooleanConverter());
		registe(Double.class, new NumberConverter());
		registe(Float.class, new NumberConverter());
		registe(Long.class, new NumberConverter());
		registe(Integer.class, new NumberConverter());
		registe(Short.class, new NumberConverter());
		registe(Byte.class, new NumberConverter());
		registe(boolean.class, new BooleanConverter());
		registe(double.class, new NumberConverter());
		registe(float.class, new NumberConverter());
		registe(long.class, new NumberConverter());
		registe(int.class, new NumberConverter());
		registe(short.class, new NumberConverter());
		registe(byte.class, new NumberConverter());
		registe(Clob.class, new ClobConverter());
	}
	
	public static Converter getConverter(Class<?> type) {
		
		Converter converter = converters.get(type);
		if(converter == null) {
			Class<?> [] clazzs = type.getDeclaredClasses();
			for (Class<?> cls : clazzs) {
				converter = converters.get(cls);
				if(converter != null) {
					return converter;
				}
			}
			clazzs = type.getInterfaces();
			for (Class<?> cls : clazzs) {
				converter = converters.get(cls);
				if(converter != null) {
					return converter;
				}
			}
		}
		return converter;
	}
	
	public static Converter registe(Class<?> sqlTypes, Converter converter) {
		return converters.put(sqlTypes, converter);
	}
	
}

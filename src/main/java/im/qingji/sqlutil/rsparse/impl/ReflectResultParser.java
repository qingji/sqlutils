package im.qingji.sqlutil.rsparse.impl;

import im.qingji.sqlutil.mapping.FieldColumnPair;
import im.qingji.sqlutil.mapping.MappingMetadata;
import im.qingji.sqlutil.mapping.cache.MetadataCache;
import im.qingji.sqlutil.rsparse.ResultSetParser;
import im.qingji.sqlutil.support.DataTypeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-4-25 下午01:02:01
 */
public class ReflectResultParser<E> implements ResultSetParser<E> {
	/**
	 * 反射的方式解析数据库行数据并生成Java对象
	 * 
	 * @see cn.com.ccxe.datafeed.common.util.jdbc.ResultSetParser#parse(java.sql.ResultSet,
	 *      java.lang.Class)
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public E parse(ResultSet rs, Class<E> clazz) throws SQLException {
		//FIXME 在此处理不可创建类型的类
		E bean = null;
		if(clazz == Long.class) {
			Object result = rs.getLong(1);
			return (E) result;
		}
		MappingMetadata metadata = MetadataCache.cache(clazz);
		if (metadata == null) {
			throw new RuntimeException("无法在缓存中找到" + clazz + "对应的映射信息.");
		}
		bean = newInstance(clazz, bean);
		if (bean != null) {
			ResultSetMetaData rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				Object parame = rs.getObject(i);
				String columnLabel = rsm.getColumnName(i);
				// 根据小写格式的字段名从缓存中获取FieldColumnPair
				columnLabel = columnLabel.toLowerCase();
				FieldColumnPair pair = metadata.getFieldMap().get(columnLabel);
				if(pair != null) {
					setParam(bean, pair, parame);
				}
			}
		}
		return bean;
	}

	private E newInstance(Class<E> clazz, E bean) throws SQLException {
		try {
			//FIXME 在newInstance之前需要检测类是否为final，abstract, interface等
			bean = clazz.newInstance();
		} catch (IllegalAccessException e) {
			throw new SQLException(e);
		} catch (InstantiationException e) {
			throw new SQLException(e);
		}
		return bean;
	}

	private void setParam(E bean, FieldColumnPair pair, Object param) {
		if(param == null ) {
			return;
		}
		final Field field = pair.getField();
		Class<?> targetType = field.getType();
		
		if (param != null && !targetType.equals(param.getClass())) {
			try {
				param = DataTypeUtil.convert(param, targetType);
			}catch (Exception e) {
				throw new RuntimeException("set Paramter失败："+field, e);
			}
		}
		Method setter = pair.getSetter();
		if (setter != null) {
			try {
				setter.invoke(bean, param);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("参数类型不匹配：'" + param + "'("+param.getClass()+") -> " + field + ".", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("反射执行" + field + "的setter方法出现错误.", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}

package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-21 上午10:42:57
 */
public class NumberConverter extends AbstractConverter {

	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {
		try {
			if(targetType.equals(obj.getClass())) {
				return obj;
			}
			if( obj instanceof Number) {
				Number data = (Number)obj;
				if( isSame(Double.class, targetType)){
					return data;
				} else if(isSame(String.class, targetType)){
					return String.valueOf(data);
				} else if(isSame(Long.class, targetType) || "long".equals(targetType.getName())) {
					return Long.valueOf(data.longValue());
				} else if(isSame(Integer.class, targetType) || "int".equals(targetType.getName())) {
					return Integer.valueOf(data.intValue());
				} else if(isSame(Short.class, targetType) || "short".equals(targetType.getName())) {
					return data.shortValue();
				} else if(isSame(Byte.class, targetType) || "byte".equals(targetType.getName())) {
					return data.byteValue();
				} else if(isSame(Double.class, targetType) || "double".equals(targetType.getName())) {
					return data.doubleValue();
				} else if(isSame(Float.class, targetType) || "float".equals(targetType.getName())) {
					return data.floatValue();
				} 
			}
			throw unsupport(targetType, obj);
		} catch (Exception e) {
			throw caseThrowable(targetType, obj, e);
		}
	}
	
}

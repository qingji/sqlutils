package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;

/**
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-21 上午09:55:57
 */
public class StringConverter extends AbstractConverter {

	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {
		try {
			if(targetType.equals(obj.getClass())) {
				return obj;
			}
			if (String.class.equals(obj.getClass())) {
				String data = (String) obj;
				if (String.class.equals(targetType)) {
					return data;
				} 
				if (isSame(Long.class, targetType)) {
					return Long.valueOf(data);
				} 
				if (isSame(Integer.class, targetType)) {
					return Integer.valueOf(data);
				} 
				if (isSame(Short.class, targetType)) {
					return Short.valueOf(data);
				} 
				if (isSame(Double.class, targetType)) {
					return Double.valueOf(data);
				} 
				if (isSame(Float.class, targetType)) {
					return Float.valueOf(data);
				} 
				if(isSame(Boolean.class, targetType)) {
					return Boolean.valueOf(data);
				} 
				
				if (isSame(long.class, targetType)) {
					return Long.valueOf(data);
				} 
				if (isSame(int.class, targetType)) {
					return Integer.valueOf(data);
				} 
				if (isSame(short.class, targetType)) {
					return Short.valueOf(data);
				} 
				if (isSame(double.class, targetType)) {
					return Double.valueOf(data);
				} 
				if (isSame(float.class, targetType)) {
					return Float.valueOf(data);
				} 
				if(isSame(boolean.class, targetType)) {
					return Boolean.valueOf(data);
				} 
				
			}
			throw unsupport(targetType, obj);
		} catch (Exception e) {
			throw caseThrowable(targetType, obj, e);
		}
	}

}

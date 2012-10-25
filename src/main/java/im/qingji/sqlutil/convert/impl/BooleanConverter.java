package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-21 上午10:43:09
 */
public class BooleanConverter extends AbstractConverter {

	/**
	 * 
	 * @see im.qingji.sqlutil.convert.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {
		try {
			if(targetType.equals(obj.getClass())) {
				return obj;
			}
			if( isSame(Boolean.class, obj.getClass())) {
				Boolean data = (Boolean)obj;
				if(isSame(Boolean.class, targetType) || "boolean".equals(targetType.getName())) {
					return data;
				} else if( isSame(String.class, targetType)) {
					return String.valueOf(data);
				}
			}
			throw unsupport(targetType, obj);
		} catch (Exception e) {
			throw caseThrowable(targetType, obj, e);
		}
	}

}

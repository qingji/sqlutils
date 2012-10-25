package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-21 上午10:42:57
 */
public class CharacterConverter extends AbstractConverter {

	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {
		try {
			if(targetType.equals(obj.getClass())) {
				return obj;
			}
			if( obj instanceof Character) {
				Character data = (Character)obj;
				if( isSame(Character.class, targetType) || "char".equals(targetType.getName())){
					return data;
				} else if(isSame(String.class, targetType)){
					return String.valueOf(data);
				}
			}
			throw unsupport(targetType, obj);
		} catch (Exception e) {
			throw caseThrowable(targetType, obj, e);
		}
	}
	
}

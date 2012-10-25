package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;
import im.qingji.sqlutil.convert.Converter;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-21 上午10:06:50
 */
public abstract class AbstractConverter implements Converter {

	protected boolean isSame(Class<?> clazz, Class<?> targetType) {
		return clazz.equals(targetType);
	}
	
	/**
	 * 
	 * @param targetType
	 * @param obj
	 * @return
	 */
	protected ConvertException unsupport(Class<?> targetType, Object obj) {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName())
				.append("不支持将") 
				.append(obj.getClass().getName())
				.append("转换成")
				.append(targetType )
				.append("类型,建议实现并注册")
				.append(Converter.class.getName());
		return new ConvertException( builder.toString());
	}
	
	/**
	 * 将在进行类型转换时抛出的异常包装成ConvertException
	 * @param targetType
	 * @param obj
	 * @param throwable
	 * @return
	 */
	protected ConvertException caseThrowable(Class<?> targetType, Object obj, Throwable throwable) {
		
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName())
				.append("无法将'")
				.append(obj)
				.append("'(").append(obj.getClass().getName()).append("类型)")
				.append("转换成").append(targetType ).append("类型.");
		return new ConvertException( builder.toString(), throwable);
	}
}

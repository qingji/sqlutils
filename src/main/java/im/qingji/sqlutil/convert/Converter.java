package im.qingji.sqlutil.convert;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 上午10:52:59
 */
public interface Converter {
	
	/**
	 * 类型转换
	 * @param targetType 目标类型
	 * @param data 需要转换的值
	 * 
	 * @return 所需的值
	 */
	 public Object convert(Class<?> targetType, Object obj) throws ConvertException;
}

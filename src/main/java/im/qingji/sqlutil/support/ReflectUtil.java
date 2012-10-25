package im.qingji.sqlutil.support;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 下午03:07:37
 */
public class ReflectUtil {

	


	static <E> Object invokeGetter(E bean, Method getter) {
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
	
	/**
	 * @param clazz
	 * @param field
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getGettersMethod(Class<?> clazz, Field field) {
		String methodName = toMethodName(field.getName(), "get");
		try {
			return clazz.getMethod(methodName);

		} catch (NoSuchMethodException e) {
			StringBuilder sb = new StringBuilder("无法找到");
			sb.append(clazz.getName()).append(".").append(methodName).append("(")
					.append(field.getType().getName()).append(") 方法。请确认是否符合JavaBean规范。");
			throw new RuntimeException(sb.toString(), e);
		}
	}
	
	/**
	 * @param clazz
	 * @param field
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getSettersMethod(Class<?> clazz, Field field) {
		String methodName = toMethodName(field.getName(), "set");
		try {
			return clazz.getMethod(methodName, field.getType());

		} catch (NoSuchMethodException e) {
			StringBuilder sb = new StringBuilder("无法找到");
			sb.append(clazz.getName()).append(".").append(methodName).append("(")
					.append(field.getType().getName()).append(") 方法。请确认是否符合JavaBean规范。");
			throw new RuntimeException(sb.toString(), e);
		}
	}
	
	/**
	 * 根据fieldName和methodType拼接方法。
	 * <p>
	 * 	
	 * </p>
	 * @param fieldName
	 * @param methodType - 方法类别:set 或  get 
	 * @return
	 */
	static String toMethodName(String fieldName, String methodType) {
		StringBuilder sb = new StringBuilder(methodType);
		char[] charArray = fieldName.toCharArray();
		int foo = (int) charArray[0];
		if (foo >= 97 && foo <= 122) {
			char tmp = (char) (foo - 32);
			charArray[0] = tmp;
		}
		sb.append(charArray);
		return sb.toString();
	}
	

	/**
	 * 获取getter方法
	 * @param <E> 
	 * @param bean
	 * @param field
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public <E> Method getGetterMethod(E bean, Field field) 
		throws NoSuchMethodException,
			SecurityException {
		
		final Class<? extends Object> clazz = bean.getClass();
		String methodName = toMethodName(field.getName(), "get"); 
		try {
			return clazz.getMethod(methodName);
		
		} catch (NoSuchMethodException e) {
			StringBuilder sb = new StringBuilder();
			sb.append("无法找到").append(clazz.getName()).append(".")
				.append(methodName).append("(")
				.append( field.getType().getName()).append(") 方法。");
			
			throw new NoSuchMethodException(sb.toString());
		}
	}
	

}

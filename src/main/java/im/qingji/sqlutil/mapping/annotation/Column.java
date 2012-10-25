package im.qingji.sqlutil.mapping.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 与数据库中的字段一一对应
 * <p>
 * 	Column.type()默认返回Class.class。
 * </p>
 * @author qingji
 * @version 0.1
 */
@Documented 
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 
@Inherited
public @interface Column {

	/**
	 * 数据库字段名，未作定义时，解析器将返回字段名。
	 * @return 
	 */
	String name() default "";
	
	/**
	 * 数据库的数据类型，未做定义，解析器将返回字段的类型。
	 * @return
	 */
	Class<?> type() default Class.class;
	
}

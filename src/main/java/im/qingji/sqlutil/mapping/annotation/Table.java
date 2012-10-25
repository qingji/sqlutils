package im.qingji.sqlutil.mapping.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented 
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE) 
@Inherited
public @interface Table {
	
	/**
	 * 数据库表名，当为空时解析器将默认返回为类名。
	 * @return
	 */
	String name() default "";
}

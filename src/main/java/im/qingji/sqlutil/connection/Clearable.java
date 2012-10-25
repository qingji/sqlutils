package im.qingji.sqlutil.connection;

/**
 * Clearable 是可以清理的数据源或目标。调用 close 方法可释放对象保存的资源。 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-21 上午11:35:03
 */
public interface Clearable {

	/**
	 * 清理与释放相关的所有系统资源。
	 */
	public void clear();
}

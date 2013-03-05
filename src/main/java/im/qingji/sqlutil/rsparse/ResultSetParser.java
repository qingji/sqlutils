package im.qingji.sqlutil.rsparse;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库{@link ResultSet}解析接口。
 *
 */
public interface ResultSetParser<E> {

	/**
	 * 将结果集{@link ResultSet}中的数据解析够成对象{@linkplain E}。
	 * <p>
	 * </p>
	 * @param rs 结果集
	 * @return
	 * @throws SQLException
	 */
	public E parse(ResultSet rs, Class<E> e) throws SQLException;
	
}

package im.qingji.sqlutil.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * java.sql.Connection连接器，用于创建Connection。
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-20 下午07:09:05
 */
public interface Connector extends Clearable{

	/**
	 * 创建连接
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	Connection connect(String url, String user, String password) throws SQLException;
	
}

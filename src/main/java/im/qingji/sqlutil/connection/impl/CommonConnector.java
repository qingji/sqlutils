package im.qingji.sqlutil.connection.impl;

import im.qingji.sqlutil.support.StringUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 通用的JDBC直连接器
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-20 下午07:10:58
 */
public class CommonConnector extends AbstractConnector {

	public Connection connect(String url, String username, String password)throws SQLException {
		try {
			if(StringUtil.isEmpty(url)) {
				throw new NullPointerException("连接描述符为空。");
			}
			if(StringUtil.isEmpty(username)|| StringUtil.isEmpty(password)) {
				DriverManager.getConnection(url);
			}
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			throw new SQLException("url:"+ url +",username:" + username + ",password:" + password, e);
		}
	}

}

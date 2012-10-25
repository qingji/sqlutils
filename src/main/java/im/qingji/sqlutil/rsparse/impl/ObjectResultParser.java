package im.qingji.sqlutil.rsparse.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import im.qingji.sqlutil.rsparse.ResultSetParser;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-20 下午02:06:27
 */
public class ObjectResultParser implements ResultSetParser<Object>{

	/**
	 * 
	 * @see ResultSetParser#parse(java.sql.ResultSet, java.lang.Class)
	 * @return Object - Object[]，对象数组中包含 ResultSet中相应列
	 */
	@Override
	public Object parse(ResultSet rs, Class<Object> e) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		final int columnCount = rsmd.getColumnCount();
		Object[] obj = new Object[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			obj[i-1] = rs.getObject(i);
		}
		return obj;
	}


}

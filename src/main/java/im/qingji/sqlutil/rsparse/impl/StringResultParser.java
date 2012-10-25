package im.qingji.sqlutil.rsparse.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import im.qingji.sqlutil.rsparse.ResultSetParser;

public class StringResultParser implements ResultSetParser<String>{

	@Override
	public String parse(ResultSet rs, Class<String> e) throws SQLException {
		return rs.getString(1);
	}

}

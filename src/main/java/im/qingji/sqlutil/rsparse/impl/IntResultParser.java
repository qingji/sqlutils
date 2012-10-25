package im.qingji.sqlutil.rsparse.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import im.qingji.sqlutil.rsparse.ResultSetParser;

public class IntResultParser implements ResultSetParser<Integer>{

	@Override
	public Integer parse(ResultSet rs, Class<Integer> e) throws SQLException {
		return rs.getInt(1);
	}

}

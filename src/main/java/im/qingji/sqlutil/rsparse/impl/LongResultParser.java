package im.qingji.sqlutil.rsparse.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import im.qingji.sqlutil.rsparse.ResultSetParser;

public class LongResultParser implements ResultSetParser<Long>{

	@Override
	public Long parse(ResultSet rs, Class<Long> e) throws SQLException {
		return rs.getLong(1);
	}

}

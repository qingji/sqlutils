package im.qingji.sqlutil.rsparse.impl;

import im.qingji.sqlutil.rsparse.ResultSetParser;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapResultParser implements ResultSetParser<Map<String, Object>>{

	@Override
    public Map<String, Object> parse(ResultSet rs, Class<Map<String, Object>> e) throws SQLException {
		ResultSetMetaData metadata = rs.getMetaData();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 1; i <= metadata.getColumnCount(); i++) {
	        String label = metadata.getColumnLabel(i);
	        Object value = rs.getObject(i);
	        map.put(label, value);
        }
	    return map;
    }

}

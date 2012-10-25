package im.qingji.sqlutil.connection.impl;

import im.qingji.sqlutil.config.ConnectionPoolConfig;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * 
 * @author qingji
 *
 */
public class BoneCPDataSourceConnector extends AbstractConnector {

	private static BoneCPDataSource ds;
	
	@Override
	public Connection connect(String url, String user, String password)
			throws SQLException {
		if(ds == null) {
			ds = initDataSource(url, user, password);	
		}
		return ds.getConnection();
	}
	
	private BoneCPDataSource initDataSource(String url, String user, String password) {
		BoneCPDataSource ds = new BoneCPDataSource();  // create a new datasource object
		ds.setJdbcUrl(url);		// set the JDBC url
		ds.setUsername(user);				// set the username
		ds.setPassword(password);
		ds.setPartitionCount(1);
		ds.setMinConnectionsPerPartition(ConnectionPoolConfig.getMinConnections());
		ds.setMaxConnectionsPerPartition(ConnectionPoolConfig.getMaxConnections());
		return ds;
	}
	
	@Override
	public void clear() {
		if(ds != null) {
			ds.close();
		}
		ds = null;
	}
	
}

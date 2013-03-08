package im.qingji.sqlutil.connection.impl;

import im.qingji.sqlutil.config.ConnectionPoolConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * 
 * @author qingji
 *
 */
public class BoneCPDataSourceConnector extends AbstractConnector {
	
	private static final Object lock = new Object();
	private static BoneCPDataSource ds;
	
	@Override
	public Connection connect(String url, String user, String password)
			throws SQLException {
		synchronized (lock) {
			if(ds == null) {
				ds = initDataSource(url, user, password);	
			}
		}
		return ds.getConnection();
	}
	
	private synchronized BoneCPDataSource initDataSource(String url, String user, String password) {
		BoneCPDataSource ds = new BoneCPDataSource();  // create a new datasource object
		ds.setJdbcUrl(url);
		ds.setUsername(user);
		ds.setPassword(password);
		ds.setConnectionTimeout(3, TimeUnit.MINUTES);
//		config.setPartitionCount(1);
		ds.setMinConnectionsPerPartition(ConnectionPoolConfig.getMinConnections());
		System.out.println("BoneCPPoolConnector.initConnectionPool - MinConnections:"+ConnectionPoolConfig.getMinConnections());
		ds.setMaxConnectionsPerPartition(ConnectionPoolConfig.getMaxConnections());
		System.out.println("BoneCPPoolConnector.initConnectionPool - MaxConnections:"+ConnectionPoolConfig.getMaxConnections());
//		config.setMaxConnectionAge(10, TimeUnit.HOURS);
		ds.setLogStatementsEnabled(true);
		ds.setCloseConnectionWatch(ConnectionPoolConfig.getTestModel());
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

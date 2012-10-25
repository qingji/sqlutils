package im.qingji.sqlutil.connection.impl;

import im.qingji.sqlutil.config.ConnectionPoolConfig;
import im.qingji.sqlutil.connection.Connector;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * 
 * @author qingji
 *
 */
public class BoneCPPoolConnector implements Connector {

	private static BoneCP connectionPool = null;

	@Override
	public Connection connect(String url, String user, String password)
			throws SQLException {
		if(connectionPool == null) {
			connectionPool = initConnectionPool(url, user, password);
		}
		return connectionPool.getConnection();
	}

	private BoneCP initConnectionPool(String url, String user, String password)
			throws SQLException {
			// setup the connection pool
			BoneCPConfig config = new BoneCPConfig();
			// jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
			config.setJdbcUrl(url); 
			config.setUsername(user);
			config.setPassword(password);
			config.setPartitionCount(1);
			config.setMinConnectionsPerPartition(ConnectionPoolConfig.getMinConnections());
			config.setMaxConnectionsPerPartition(ConnectionPoolConfig.getMaxConnections());
			return new BoneCP(config); // setup the connection pool
	}
	
	@Override
	public void clear() {
		if(connectionPool != null) {
			connectionPool.shutdown();
		}
		connectionPool = null;
	}

}

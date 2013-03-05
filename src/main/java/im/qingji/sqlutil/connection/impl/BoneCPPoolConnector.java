package im.qingji.sqlutil.connection.impl;

import im.qingji.sqlutil.config.ConnectionPoolConfig;
import im.qingji.sqlutil.connection.Connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * 
 * @author qingji
 * 
 */
public class BoneCPPoolConnector implements Connector {

	private static final Object lock = new Object();
	private static BoneCP connectionPool = null;

	@Override
	public Connection connect(String url, String user, String password) throws SQLException {
		synchronized (lock) {
			if (connectionPool == null) {
				connectionPool = initConnectionPool(url, user, password);
			}
			return connectionPool.getConnection();
		}
	}

	private synchronized BoneCP initConnectionPool(String url, String user, String password) throws SQLException {
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		// jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
		config.setJdbcUrl(url);
		config.setUsername(user);
		config.setPassword(password);
		config.setPartitionCount(1);
		config.setMinConnectionsPerPartition(ConnectionPoolConfig.getMinConnections());
		System.out.println("BoneCPPoolConnector.initConnectionPool - MinConnections:"+ConnectionPoolConfig.getMinConnections());
		config.setMaxConnectionsPerPartition(ConnectionPoolConfig.getMaxConnections());
		System.out.println("BoneCPPoolConnector.initConnectionPool - MaxConnections:"+ConnectionPoolConfig.getMaxConnections());
		config.setMaxConnectionAge(10, TimeUnit.MINUTES);
		return new BoneCP(config); // setup the connection pool
	}

	@Override
	public void clear() {
		if (connectionPool != null) {
			connectionPool.shutdown();
		}
		connectionPool = null;
	}

}

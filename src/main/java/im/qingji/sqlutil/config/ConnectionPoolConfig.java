package im.qingji.sqlutil.config;

public class ConnectionPoolConfig {

	private static final int DEFUALT_MIN_CONNECTIONS = 2;
	private static final int DEFUALT_MAX_CONNECTIONS = 5;
	
	public ConnectionPoolConfig() {
		load();
	}
	
	public static void load() {
	}
	
	public static int getMinConnections() {
		return SQLUtilConfigurator.getInt("connectionManager.minConnections", DEFUALT_MIN_CONNECTIONS);
	}
	public static int getMaxConnections() {
		return SQLUtilConfigurator.getInt("connectionManager.MaxConnections", DEFUALT_MAX_CONNECTIONS);
	}
	
	
}

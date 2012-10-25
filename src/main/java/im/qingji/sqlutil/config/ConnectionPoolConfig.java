package im.qingji.sqlutil.config;

public class ConnectionPoolConfig {

	private static int minConnections;
	private static int MaxConnections;
	
	private static final int DEFUALT_MIN_CONNECTIONS = 2;
	private static final int DEFUALT_MAX_CONNECTIONS = 5;
	
	public ConnectionPoolConfig() {
		load();
	}
	
	public static void load() {
		minConnections = SQLUtilConfigurator.getInt("connectionManager.minConnections", DEFUALT_MIN_CONNECTIONS);
		MaxConnections = SQLUtilConfigurator.getInt("connectionManager.MaxConnections", DEFUALT_MAX_CONNECTIONS);

	}
	public static int getMinConnections() {
		return minConnections;
	}
	public static int getMaxConnections() {
		return MaxConnections;
	}
	
	
}

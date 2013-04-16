package im.qingji.sqlutil.config;

public class ConnectionPoolConfig {

	private static final int DEFUALT_MIN_CONNECTIONS = 2;
	private static final int DEFUALT_MAX_CONNECTIONS = 5;
	private static final boolean DEFUALT_TEST_MODEL = false;
	
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
	
	public static boolean getTestModel() {
		return SQLUtilConfigurator.getBoolean("connectionManager.testModel", DEFUALT_TEST_MODEL);
	}
	
}

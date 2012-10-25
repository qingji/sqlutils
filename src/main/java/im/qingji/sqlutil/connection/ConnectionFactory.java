package im.qingji.sqlutil.connection;

import im.qingji.sqlutil.config.SQLUtilConfigurator;
import im.qingji.sqlutil.connection.impl.CommonConnector;
import im.qingji.sqlutil.support.ConfigPrefix;
import im.qingji.sqlutil.support.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory {

	static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	static final String DRIVER = "driver";
	static final String URL = "url";
	static final String USERNAME = "username";
	static final String PASSWORD = "password";
	static final String USE_DATASOURCE = "useDatasource";
	static final String CONNECTOR = "connector";
	static final String CONNECTION_PREFIX = "connectionPrefix";
	static final String MAX_CONNECT_COUNT = "maxConnectCount";
	
	private static Connector DEFAULT_CONNECTOR = new CommonConnector();
	private static String DEFAULT_PREFIX = ConfigPrefix.DERBY;

	private static int maxConnectCount = 3;
	/**
	 * 获取默认的数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(){
		String prefix = SQLUtilConfigurator.getString(CONNECTION_PREFIX);
		if (!StringUtil.isEmpty(prefix)) {
			return getConnection(prefix);
		}
		return getConnection(DEFAULT_PREFIX);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String prefix) {
		Properties propertis = SQLUtilConfigurator.getPropertyGroup(prefix, true);
		Connection conn = null;
		if (propertis.size() <= 0) {
			throw new RuntimeException("没有找到前缀为“" + prefix + "”的配置，请确认与配置文件的数据库连接前缀相匹配。");
		}
		String url = propertis.getProperty(URL);
		String driver = propertis.getProperty(DRIVER);
		String username = propertis.getProperty(USERNAME);
		String password = propertis.getProperty(PASSWORD);
		// 注册驱动器
		registeDriver(driver);
		
		// 获取连接器. 如果失败,使用默认连接器
		loadConnector();
		loadMaxConnectCount();
		try {
			conn = DEFAULT_CONNECTOR.connect(url, username, password);
		} catch (SQLException e) {
			conn = reconnect(url, username, password, e);
		}
		if(conn != null) {
			return conn;
		}else {
			throw new RuntimeException("暂时无法获取连接，请确认服务器连接状况、数据库配置或稍后再试。");
		}
	}

	/**
	 * 
	 */
	private static void loadMaxConnectCount() {
		SQLUtilConfigurator.getInt(MAX_CONNECT_COUNT, getMaxConnectCount());
		
	}

	private static Connection reconnect(String url, String username, String password, Throwable throwable) {
		for (int currentCount = 1; currentCount <= maxConnectCount; currentCount++) {
			logger.warn("第{}次请求连接失败,"+url+";username:"+username+";password:"+password, currentCount, throwable);
			try {
				try {
					TimeUnit.SECONDS.sleep(currentCount);
				} catch (InterruptedException e) {
				}
				return DEFAULT_CONNECTOR.connect(url, username, password);
			} catch (SQLException e1) {
				logger.warn("",e1);
			}
		}
		throw new RuntimeException("连续请求"+getMaxConnectCount() +"次，无法连接。"+url+";username:"+username+";password:"+password, throwable);
	}
	
	private static void loadConnector(){
		loadConnector(false);
	}

	private static void loadConnector(boolean isReconnect) {
		String connectorName = SQLUtilConfigurator.getString(CONNECTOR);
		if (!StringUtil.isEmpty(connectorName)) {
			if(!isReconnect && connectorName.equalsIgnoreCase( getDefaultConnector().getClass().getName())) {
				return;
			}
			try {
				Class<?> connClass = Class.forName(connectorName);
				Object connector = connClass.newInstance();
				if (connector instanceof Connector) {
					setDefaultConnector((Connector) connector);
				}
			} catch (ClassNotFoundException e) {
				logger.debug("",e);
			} catch (InstantiationException e) {
				logger.debug("",e);
			} catch (IllegalAccessException e) {
				logger.debug("",e);
			}
		}
	}

	public synchronized static void setDefaultPrefix(String prefix) {
		DEFAULT_PREFIX = prefix;
	}

	/**
	 * 设置默认的连接器
	 * 
	 * @param connector
	 */
	public synchronized static void setDefaultConnector(Connector connector) {
		DEFAULT_CONNECTOR = connector;
	}
	
	/**
	 * 设置默认的连接器
	 * 
	 * @param connector
	 */
	public synchronized static Connector getDefaultConnector() {
		return DEFAULT_CONNECTOR;
	}
	
	public static void setMaxConnectCount(int count) {
		maxConnectCount = count;
	}
	
	public static int getMaxConnectCount() {
		return maxConnectCount;
	}

	public static void registeDriver(String driver) {
		try {
			if (StringUtil.isEmpty(driver)) {
				throw new NullPointerException("驱动名称为空。");
			}
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(driver, e);
		}
	}

}

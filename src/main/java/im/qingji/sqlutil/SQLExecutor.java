package im.qingji.sqlutil;

import im.qingji.sqlutil.rsparse.ResultSetParser;
import im.qingji.sqlutil.rsparse.impl.ObjectResultParser;
import im.qingji.sqlutil.rsparse.impl.ReflectResultParser;
import im.qingji.sqlutil.support.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SQL执行器.
 * <p>
 * 在{@link SQLExecutor}中，对于一些资源的申请与管理的原则是何处申请，何处回收。
 * 但是对于构建SQLExecutor传入的Connection，在调用SQLExecutor@close()，Connection将会被关掉。
 * <p>
 * </p>
 * SQLExecutor本身支持提供Statement复用，这代表着将会缓存每个执行的SQL的Statement,
 * 在下次使用这个statement时，将会将其保存的参数清理。 而对于外部传入的statement，将不纳入管理范围。
 * <p>
 * </p>
 * statement是与线程无关的。 </p>
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-13 上午09:10:26
 */
public class SQLExecutor {

	private Connection connection;
	private Map<String, PreparedStatement> stateMap = new HashMap<String, PreparedStatement>();

	/**
	 * 根据Connection 生成执行器。 当执行器关闭时，也就代表着Connection关闭。
	 * 
	 * @param connection
	 */
	public SQLExecutor(Connection connection) {
		if (connection == null) {
			throw new NullPointerException("Connection is Null!");
		}
		try {
			if (connection.isClosed()) {
				throw new IllegalArgumentException("connection is closed");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
		this.connection = connection;
	}

	public <E> int insert(E e) throws SQLException {
		return insert(e, null);
	}

	public <E> int insert(PreparedStatement ps, E e) throws SQLException {
		Object[] parametes = SQLUtil.getOrderedParams(e);
		return executeUpdate(ps, parametes);
	}

	public <E> int insert(E e, String tableName) throws SQLException {
		String sql = null;
		if (tableName != null) {
			sql = SQLUtil.insertSQL(e.getClass(), tableName);
		} else {
			sql = SQLUtil.insertSQL(e.getClass());
		}
		Object[] parametes = SQLUtil.getOrderedParams(e);
		return executeUpdate(sql, parametes);
	}

	public <E> int insert(PreparedStatement ps, Object[] parametes) throws SQLException {
		return executeUpdate(ps, parametes);
	}

	public boolean execute(String sql) throws SQLException {
		return execute(sql, null);
	}

	public boolean execute(String sql, Object[] parametes) throws SQLException {
		PreparedStatement ps = getStatement(sql);
		try {
			return execute(ps, parametes);
		} finally {
			try {
				ps.clearParameters();
				ps.clearBatch();
			} catch (Exception e) {
				stateMap.remove(sql);
			}
		}
	}

	public boolean execute(PreparedStatement ps, Object[] parametes) throws SQLException {
		injectParam(ps, parametes);
		return ps.execute();
	}

	public int executeUpdate(String sql) throws SQLException {
		return executeUpdate(sql, null);
	}

	public int executeUpdate(String sql, Object[] parametes) throws SQLException {
		PreparedStatement ps = getStatement(sql);
		try {
			return executeUpdate(ps, parametes);
		} finally {
			try {
				ps.clearParameters();
				ps.clearBatch();
			} catch (Exception e) {
				stateMap.remove(sql);
			}
		}
	}

	public int executeUpdate(PreparedStatement ps, Object[] parametes) throws SQLException {
		injectParam(ps, parametes);
		return ps.executeUpdate();
	}

	/**
	 * @see PreparedStatement#addBatch()
	 * @param ps
	 * @param parametes
	 *            待输入的参数
	 * @throws SQLException
	 */
	public void addBatch(PreparedStatement ps, Object[] parametes) throws SQLException {
		injectParam(ps, parametes);
		ps.addBatch();
	}

	/**
	 * @see PreparedStatement#addBatch(String)
	 * @param ps
	 * @param sql
	 * @throws SQLException
	 */
	public void addBatch(PreparedStatement ps, String sql) throws SQLException {
		ps.addBatch(sql);
	}

	public int[] executeBatch(PreparedStatement ps) throws SQLException {
		return ps.executeBatch();
	}

	public ResultSet query(String sql) throws SQLException {
		return query(sql, null);
	}

	public ResultSet query(String sql, Object[] parametes) throws SQLException {
		PreparedStatement ps = getStatement(sql);
		return query(ps, parametes);
	}

	public ResultSet query(PreparedStatement ps, Object[] parametes) throws SQLException {
		injectParam(ps, parametes);
		return ps.executeQuery();
	}

	/**
	 * 查询对象
	 * 
	 * @see ObjectResultParser#parse(ResultSet, Class)
	 * @see #queryObject(String, Object[], Class, ResultSetParser)
	 * @return Object - Object[]
	 */
	public Object queryObject(String sql) throws SQLException {
		return queryObject(sql, null, Object.class, new ObjectResultParser());
	}

	/**
	 * 查询对象
	 * 
	 * @see #queryObject(String, Object[], Class, ResultSetParser)
	 */
	public <E> E queryObject(String sql, Class<E> clazz) throws SQLException {
		return queryObject(sql, null, clazz, new ReflectResultParser<E>());
	}

	/**
	 * 查询对象
	 * 
	 * @see #queryObject(String, Object[], Class, ResultSetParser)
	 */
	public <E> E queryObject(String sql, Object[] parameters, Class<E> clazz) throws SQLException {
		return queryObject(sql, parameters, clazz, new ReflectResultParser<E>());
	}

	/**
	 * 查询对象
	 * 
	 * @param <E>
	 * @param sql
	 *            - SQL语句
	 * @param parameters
	 *            - SQL参数列表
	 * @param clazz
	 *            - 返回对象的Class
	 * @param parser
	 *            - 解析器。
	 * @return 如果数据库中没有相应数据，返回null，否则返回E
	 * @throws SQLException
	 */
	public <E> E queryObject(String sql, Object[] parameters, Class<E> clazz, ResultSetParser<E> parser)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getStatement(sql);
			rs = query(ps, parameters);
			E e = null;
			if (rs.next()) {
				e = parser.parse(rs, clazz);
			}
			return e;
		} finally {
			close(rs);
			try {
				ps.clearParameters();
			} catch (Exception e) {
				stateMap.remove(sql);
			}
		}
	}

	/**
	 * @param sql
	 * @see ObjectResultParser#parse(ResultSet, Class)
	 * @return List中的Object为Object[]
	 * @throws SQLException
	 */
	public List<Object> queryList(String sql) throws SQLException {
		return queryList(sql, null, Object.class, new ObjectResultParser());
	}

	public <E> List<E> queryList(String sql, Class<E> clazz) throws SQLException {
		return queryList(sql, null, clazz, new ReflectResultParser<E>());
	}

	public <E> List<E> queryList(String sql, Object[] parameters, Class<E> clazz) throws SQLException {
		return queryList(sql, parameters, clazz, new ReflectResultParser<E>());
	}

	public <E> List<E> queryList(String sql, Object[] parameters,  ResultSetParser<E> parser) throws SQLException {
		return queryList(sql, parameters, null, parser);
	}

	/**
	 * 获取List结果集。
	 * <p>
	 * <b>当返回集合强制转换时，集合中的类型由{@link ResultSetParser}确定。</b>
	 * </p>
	 * 
	 * @param sql
	 * @param parameters
	 *            - SQL参数列表
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> queryList(String sql, Object[] parameters, Class<E> clazz, ResultSetParser<E> parser)
			throws SQLException {
		PreparedStatement ps = getStatement(sql);
		ResultSet rs = query(ps, parameters);
		List<E> list = new ArrayList<E>();
		while (rs.next()) {
			E e = parser.parse(rs, clazz);
			list.add(e);
		}
		return list;
	}

	/**
	 * @see java.sql.Connection#prepareStatement(String)
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement state = getStatement(sql);
		putStatement(sql, state);
		return state;
	}

	final Connection getConnection() {
		if (connection == null) {
			stateMap.clear();
			throw new NullPointerException("Connection is Null!");
		}
		try {
			if (connection.isClosed()) {
				stateMap.clear();
				throw new RuntimeException("connection is closed");
			}
		} catch (SQLException e) {
			stateMap.clear();
			throw new RuntimeException(e);
		}
		return connection;
	}

	final void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
		stateMap.clear();
	}

	PreparedStatement putStatement(String sqlKey, PreparedStatement state) {
		return stateMap.put(sqlKey, state);
	}

	/**
	 * 如果在Statement已经存在并且属于PreparedStatement，就会调用PreparedStatement#
	 * clearParameters()
	 * 
	 * @param sqlKey
	 * @return
	 * @throws SQLException
	 */
	PreparedStatement getStatement(String sqlKey) throws SQLException {
		PreparedStatement state = stateMap.get(sqlKey);
		if (state == null || state.isClosed()) {
			state = createStatement(sqlKey);
		}
		state.clearParameters();
		return state;
	}

	PreparedStatement createStatement(String sqlKey) throws SQLException {
		PreparedStatement state = getConnection().prepareStatement(sqlKey);
		putStatement(sqlKey, state);
		return state;
	}

	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 关闭执行器，执行器关闭也将关闭Connection.
	 * 
	 * @throws SQLException
	 */
	public void abort() throws SQLException {
		for (Entry<String, PreparedStatement> entry : stateMap.entrySet()) {
			closeStatement(entry.getKey());
		}
		stateMap.clear();
		closeConnection();
	}

	void closeStatement(String sqlKey) throws SQLException {
		Statement state = stateMap.get(sqlKey);
		if (state != null) {
			state.close();
		}
	}

	/**
	 * 构建SQL语句的日志。
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 */
	static String genratorSQLLog(String sql, Object[] parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append("SQL:").append(sql);
		if (parameters != null && parameters.length > 0) {
			sb.append("Params[");
			for (Object obj : parameters) {
				sb.append(obj).append(",");
			}
			sb.append("]");
		}

		return sb.toString();
	}

	/**
	 * @param ps
	 * @param parametes
	 * @throws SQLException
	 */
	private void injectParam(PreparedStatement ps, Object[] parametes) throws SQLException {
		if (parametes != null && parametes.length > 0) {
			for (int i = 0; i < parametes.length; i++) {
				int parameterIndex = i + 1;
				Object param = parametes[i];
				try {
					ps.setObject(parameterIndex, param);
				} catch (Exception e) {
					throw new SQLException(ps.toString()+",\nParaes:"+param.getClass()+",", e);
				}
			}
		}
	}
}

package im.qingji.sqlutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import im.qingji.sqlutil.connection.ConnectionFactory;
import im.qingji.sqlutil.examples.User;
import im.qingji.sqlutil.rsparse.ResultSetParser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * SQL执行器
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-13 上午09:10:26
 */
public class SQLExecutorTest {

	private SQLExecutor executor;

	@Before
	public void setUp() throws Exception {
		Connection connection = ConnectionFactory.getConnection();
		connection.setAutoCommit(false);
		executor = new SQLExecutor(connection);
		fillData();
	}

	@Test
	public void fillData() throws SQLException {
		User e = new User();
		e.setId(1);
		e.setName("test");
		e.setPassword("test");
		executor.insert(e, "test_user");
	}

	@Ignore
	public void testInsert() throws SQLException {
		User e = new User();
		e.setId(1);
		e.setName("test");
		e.setPassword("test");
		int result = executor.insert(e, "test_user");
		assertEquals(result, 1);
	}

	@Ignore
	public void testQuery() throws Exception {
		String sql = "select * from test_user";
		// 需保证数据库中有数据
		Object o = executor.queryList(sql);
		assertNotNull(o);

		List<User> users = null;

		users = executor.queryList(sql, User.class);
		
		assertNotNull(users);
		assertEquals(users.size() > 1, true);

		users = executor.queryList(sql, null, User.class,
				new ResultSetParser<User>() {
					@Override
					public User parse(ResultSet rs, Class<User> e)
							throws SQLException {
						User user = new User();
						user.setId(rs.getInt("id"));
						user.setName(rs.getString("name"));
						user.setPassword("password");
						return user;
					}
				});

		assertNotNull(users);
		assertEquals(users.size() > 1, true);

	}

}

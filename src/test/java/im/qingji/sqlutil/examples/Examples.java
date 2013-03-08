package im.qingji.sqlutil.examples;

import im.qingji.sqlutil.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Test;


public class Examples {
	@Test
	public void dropUserTable() throws SQLException {
		String sql = "drop table tbl_user";
		System.out.println(sql);
		Connection conn = ConnectionFactory.getConnection();
		conn.createStatement().execute(sql);
		
	}
	@Test
	public void createUserTable() throws SQLException {
		
		 String sql = 
			"CREATE TABLE tbl_user (" +
		 		"id int NOT NULL , " +
		 		"name VARCHAR(45) NOT NULL, " +
		 		"pwd VARCHAR(45) NOT NULL " +
		 	")";
		System.out.println(sql);
		Connection conn = ConnectionFactory.getConnection();
		conn.createStatement().execute(sql);
	}
	
	@Test
	public void insertUser() throws SQLException {
		Connection conn = ConnectionFactory.getConnection();

		String sql = "insert into tbl_user(id,name,pwd) values(?,?,?)";
		System.out.println(sql);
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 1; i < 10; i++) {
			ps.setInt(1, i);
			ps.setString(2, "user" + i);
			ps.setString(3, "pwd" + i);
			ps.addBatch();
		}
		int [] result = null;
		try {
			result = ps.executeBatch();
			System.out.println("result:"+Arrays.toString(result));
		}catch (Exception e) {
			System.out.println(Arrays.toString(result));
		throw new RuntimeException(e);
		}finally {
			ps.close();
			conn.close();
			ConnectionFactory.getDefaultConnector().clear();
		}
		System.out.println("==========insertUsers ps.executeBatch() begin======");
		for (int i : result) {
			System.out.print(i + " ");
		}
		System.out.println("\n==========insertUsers ps.executeBatch() end======");
	}
	
	@Test
	public void updateUser() throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "update tbl_user set name=? where id = ?";
		System.out.println(sql);
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 1; i < 10; i++) {
			ps.setString(1, "U_user" + i);
			if(i % 2 == 0)
				ps.setInt(2, i);
			else
				ps.setInt(2, 0);
			ps.addBatch();
		}
		int [] result = null;
		try {
			result = ps.executeBatch();
			System.out.println("result:"+Arrays.toString(result));
		}catch (Exception e) {
		throw new RuntimeException(e);
		}
		System.out.println("==========updateUser ps.executeBatch() begin======");
		for (int i : result) {
			System.out.print(i + " ");
		}
		System.out.println("\n==========updateUser ps.executeBatch() end======");
	}

}

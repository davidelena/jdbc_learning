package com.mainrun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.math.NumberUtils;

import com.commons.ConfigUtils;
import com.commons.ConnectionFactory;
import com.mysql.jdbc.Driver;

public class MainRun
{
	private static final String DB_DRIVER = "db.driver";
	private static final String DB_PASSWORD = "db.password";
	private static final String DB_URL = "db.url";
	private static final String DB_USERNAME = "db.username";
	private static ConfigUtils configUtils = new ConfigUtils("/dbconfig.properties");
	private static String dbDriver = "org.gjt.mm.mysql.Driver";
	private static String dbStr = "jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8";
	private static String dbUserName = "root";
	private static String dbPassword = "123456";

	static
	{
		dbDriver = configUtils.getProperty(DB_DRIVER).isEmpty() ? dbDriver : configUtils.getProperty(DB_DRIVER);
		dbStr = configUtils.getProperty(DB_URL).isEmpty() ? dbStr : configUtils.getProperty(DB_URL);
		dbUserName = configUtils.getProperty(DB_USERNAME).isEmpty() ? dbUserName : configUtils.getProperty(DB_USERNAME);
		dbPassword = configUtils.getProperty(DB_PASSWORD).isEmpty() ? dbPassword : configUtils.getProperty(DB_PASSWORD);
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		testGetDataFromDB();
	}

	public static void test(Statement stm) throws SQLException
	{
		// 发送sql语句
		String sql = "SELECT 1";
		boolean result = stm.execute(sql);
		System.err.println("执行结果: " + result);
	}

	public static void insert(Statement stm) throws SQLException
	{
		String insertSql = "INSERT INTO STUDENT (NAME, CREATETIME) VALUES ('新增数据','2014-08-26')";
		int icount = stm.executeUpdate(insertSql);
		System.err.println("新增：" + icount + "条数据");
	}

	public static void update(Statement stm) throws SQLException
	{
		String updateSql = "UPDATE STUDENT SET NAME = '修改数据' WHERE ID = 11";
		int ucount = stm.executeUpdate(updateSql);
		System.err.println("修改：" + ucount + "条数据");
	}

	public static void selectAll(ResultSet rs, Statement stm) throws SQLException
	{
		String selectSql = "SELECT * FROM STUDENT";
		rs = stm.executeQuery(selectSql);
		while (rs.next())
		{
			String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
					String.valueOf(rs.getString("Name")));
			System.err.println(info);
		}
	}

	public static void selectByIdWithStatement(ResultSet rs, Statement stm, int id) throws SQLException
	{
		String selectSql = "SELECT * FROM STUDENT WHERE ID=" + id;
		rs = stm.executeQuery(selectSql);
		while (rs.next())
		{
			String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
					String.valueOf(rs.getString("Name")));
			System.err.println(info);
		}
	}

	public static void selectByIdWithPreStatement(Connection conn, ResultSet rs, int id) throws SQLException
	{
		String selectSql = "SELECT * FROM STUDENT WHERE ID = ?";
		PreparedStatement pstm = conn.prepareStatement(selectSql);
		pstm.setLong(1, id);
		// pstm.setString(1, "daviddai");
		rs = pstm.executeQuery();
		// System.out.println(pstm.getQueryTimeout());
		while (rs.next())
		{
			String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
					String.valueOf(rs.getString("Name")));
			System.err.println(info);
		}
	}

	/**
	 * 测试获取数据库连接
	 */
	public static void testGetDataFromDB()
	{
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			conn = ConnectionFactory.getConnection();
			stm = conn.createStatement();

			// 加载驱动
			Driver driver = new Driver();
			// Connection conn = driver.connect(dbStr, prop);

			System.err.println(conn);

			// test(stm);
			// insert(stm);
			// update(stm);
			// selectAll(rs, stm);

			// selectByIdWithPreStatement(conn, rs, 3);
			 selectByIdWithPreStatement(conn, rs, 1);

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			ConnectionFactory.close(conn, stm, rs);
		}
	}
}

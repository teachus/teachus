package dk.teachus.backend.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

public class CreateMysqlTestDatabase {
	
	public CreateMysqlTestDatabase() {
		// Create connection
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost/teachus_test?allowMultiQueries=true", "teachus_test", "test_teachus");

			// Drop existing tables
			dropTable(connection, "booking");
			dropTable(connection, "message_recipient");
			dropTable(connection, "message");
			dropTable(connection, "application_configuration");
			dropTable(connection, "period");
			dropTable(connection, "teacher_attribute");
			
			// To drop the person table we first need to remove all person references
			try {
				executeUpdateSql(connection, "UPDATE person SET teacher_id = NULL");
			} catch (SQLException e) {
				// The person table might not exist, so it's ok
			}
			dropTable(connection, "person");
			
			// Create new tables based on the schema
			String schemaSql = IOUtils.toString(getClass().getResourceAsStream("/mysql.sql"), "UTF-8");
			System.out.println(schemaSql);
			executeSql(connection, schemaSql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public void dropTable(Connection connection, String table) throws SQLException {
		executeSql(connection, "DROP TABLE IF EXISTS "+table);
	}
	
	public void executeSql(Connection connection, CharSequence sql) throws SQLException {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			statement.execute(sql.toString());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public void executeUpdateSql(Connection connection, CharSequence sql) throws SQLException {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql.toString());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}

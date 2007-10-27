/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

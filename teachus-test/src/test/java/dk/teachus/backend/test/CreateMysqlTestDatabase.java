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
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.Resource;

public class CreateMysqlTestDatabase {
	
	public CreateMysqlTestDatabase(Resource property) {
		System.out.print("Ensuring database exists... ");
		// Create connection
		Connection connection = null;
		try {
			
			Properties properties = new Properties();
			properties.load(property.getInputStream());
			
			String driverClass = properties.getProperty("db.driverClassName");
			String jdbcUrl = properties.getProperty("db.url");
			jdbcUrl += "?allowMultiQueries=true";
			String jdbcUser = properties.getProperty("db.username");
			String jdbcPass = properties.getProperty("db.password");
			String jdbcHost = properties.getProperty("db.host");
			String jdbcDatabase = properties.getProperty("db.database");
			
			Class.forName(driverClass);
			
			// Create database if not exists
			connection = DriverManager.getConnection("jdbc:mysql://"+jdbcHost+"/"+jdbcDatabase, jdbcUser, jdbcPass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP DATABASE "+jdbcDatabase);
			statement.close();
			statement = connection.createStatement();
			statement.executeUpdate("CREATE DATABASE "+jdbcDatabase);
			statement.close();
			connection.close();
			
			// Connect to database
			connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);

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
			System.out.println("Done");
		}
	}
	
	public void dropTable(Connection connection, String table) throws SQLException {
		executeSql(connection, "DROP TABLE IF EXISTS "+table);
	}
	
	public void executeSql(Connection connection, CharSequence sql) throws SQLException {
		List<String> statements = parseSqlIntoSingleStatements(sql);
		
		for (String sqlStatement : statements) {
			Statement statement = null;
			
			try {
				statement = connection.createStatement();
				statement.execute(sqlStatement);
			} catch (SQLSyntaxErrorException e) {
				System.err.println(sqlStatement);
				throw e;
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
	
	public void executeUpdateSql(Connection connection, CharSequence sql) throws SQLException {
		List<String> statements = parseSqlIntoSingleStatements(sql);
		
		for (String sqlStatement : statements) {
			Statement statement = null;
			
			try {
				statement = connection.createStatement();
				statement.executeUpdate(sqlStatement);
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
	
	private List<String> parseSqlIntoSingleStatements(CharSequence sql) {
		List<String> statements = new ArrayList<String>();
		
		StringBuilder statement = new StringBuilder();
		boolean escape = false;
		boolean quote = false;
		for (int n = 0; n < sql.length(); n++) {
			char currentChar = sql.charAt(n);
			
			switch (currentChar) {
				case '"':
				case '`':
				case '\'':
					if (escape == false) {
						quote = quote == false;
					}
					escape = false;
					statement.append(currentChar);
					break;
				case '\\':
					escape = escape == false;
					statement.append(currentChar);
					break;
				case ';':
					if (quote == false) {
						statements.add(statement.toString());
						statement = new StringBuilder();
					}
					break;
				default:
					statement.append(currentChar);
			}
		}
		
		String sqlStatement = statement.toString();
		sqlStatement = sqlStatement.trim();
		
		if (sqlStatement.length() > 0) {
			statements.add(sqlStatement);
		}
		
		return statements;
	}
	
}

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
package dk.teachus.database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public class StaticDataImport {
	
	public StaticDataImport() throws Exception {
		this("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/teachus", "root", "");
	}
	
	public StaticDataImport(String driverClass, String url, String username, String password) throws Exception {
		// database connection
		Class.forName(driverClass);
		Connection jdbcConnection = DriverManager.getConnection(url, username, password);
		
		doImport(jdbcConnection);
	}
	
	public StaticDataImport(Connection jdbcConnection) throws Exception {
		doImport(jdbcConnection);
	}
	
	private void doImport(Connection jdbcConnection) throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		// Create dataset
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		ReplacementDataSet dataSet = new ReplacementDataSet(new FilteredDataSet(filter, new FlatXmlDataSet(new FileInputStream("src/test/resources/full.xml"))));
		dataSet.addReplacementSubstring("\\n", "\n");
		
		// Clean and insert
		try {
			Statement statement = jdbcConnection.createStatement();
			statement.executeUpdate("UPDATE person SET teacher_id = NULL");
			statement.close();
			
			DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
			DatabaseOperation.INSERT.execute(connection, dataSet);
		} finally {
			connection.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new StaticDataImport();
	}
}

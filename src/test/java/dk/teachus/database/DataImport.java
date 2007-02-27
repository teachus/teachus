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

public class DataImport {
	
	public DataImport() throws Exception {
		this("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/teachus", "root", "");
	}
	
	public DataImport(String driverClass, String url, String username, String password) throws Exception {
		// database connection
		Class.forName(driverClass);
		Connection jdbcConnection = DriverManager.getConnection(url, username, password);
		
		doImport(jdbcConnection);
	}
	
	public DataImport(Connection jdbcConnection) throws Exception {
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
		new DataImport();
	}
}

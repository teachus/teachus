package dk.frankbille.teachus.utils;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;

public class DataExport {
	public static void main(String[] args) throws Exception {
		// database connection
		Class.forName("com.mysql.jdbc.Driver");
		Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost/teachus", "root", "");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		// Create dataset
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		IDataSet dataset = new FilteredDataSet(filter, connection.createDataSet());
		
        // write DTD file
        FlatDtdDataSet.write(dataset, new FileOutputStream("src/test/resources/full.dtd"));
		
		// full database export
		FlatXmlWriter datasetWriter = new FlatXmlWriter( new FileOutputStream("src/test/resources/full.xml")); 
	    datasetWriter.setDocType("src/test/resources/full.dtd"); 
	    datasetWriter.write(dataset);
	}
}

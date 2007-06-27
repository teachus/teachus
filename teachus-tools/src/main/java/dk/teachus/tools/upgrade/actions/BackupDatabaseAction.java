package dk.teachus.tools.upgrade.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.DatabaseNode;

public class BackupDatabaseAction implements Action {
	private static final Log log = LogFactory.getLog(BackupDatabaseAction.class);
	
	private DatabaseNode database;
	private File destination;

	public BackupDatabaseAction(DatabaseNode database, File destination) {
		this.database = database;
		this.destination = destination;
	}

	public void execute() throws Exception {
		log.info("Backing up database: "+database.getJdbcUrl());
		
		FileWriter writer = new FileWriter(destination);
		
		List<String> command = new ArrayList<String>();
		command.add("mysqldump");
		command.add("-h");
		command.add(database.getHost());
		command.add("-u");
		command.add(database.getUsername());
		if (database.getPassword() != null && database.getPassword().length() > 0) {
			command.add("--password="+database.getPassword());
		}
		command.add("-c");
		command.add("--delayed-insert");
		command.add("--add-locks");
		command.add("--add-drop-table");
		command.add("--disable-keys");
		command.add("--single-transaction");
		command.add(database.getDatabase());
		
		ProcessBuilder pb = new ProcessBuilder(command);
	
		Process databaseBackup = pb.start();
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(databaseBackup.getInputStream()));
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			writer.append(line).append("\n");
		}
		
		BufferedReader errorStream = new BufferedReader(new InputStreamReader(databaseBackup.getErrorStream()));
		line = null;
		while ((line = errorStream.readLine()) != null) {
			System.out.println(line);
		}
		
		if (databaseBackup.waitFor() != 0) {
			throw new RuntimeException("Database backup failed.");
		}
		
		writer.close();
	}

}

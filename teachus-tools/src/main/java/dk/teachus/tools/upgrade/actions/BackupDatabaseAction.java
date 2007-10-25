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
import dk.teachus.tools.upgrade.config.SshNode;

public class BackupDatabaseAction implements Action {
	private static final Log log = LogFactory.getLog(BackupDatabaseAction.class);
	
	private DatabaseNode database;
	private File destination;

	private SshTunnelAction dbTunnel;

	public BackupDatabaseAction(SshNode tunnelHost, DatabaseNode database, File destination) {
		this.database = database;
		this.destination = destination;
		
		dbTunnel = new SshTunnelAction(tunnelHost, 13306, database.getHost(), database.getPort());
	}

	public void execute() throws Exception {		
		// Start tunnel
		dbTunnel.execute();
		
		FileWriter writer = new FileWriter(destination);
		
		List<String> command = new ArrayList<String>();
		command.add("mysqldump");
		command.add("-h");
		command.add("127.0.0.1");
		command.add("-P");
		command.add(""+13306);
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

		log.info("Backing up database: "+database.getJdbcUrl());
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

		dbTunnel.cleanup();
	}
	
	public void check() throws Exception {
		dbTunnel.check();
	}
	
	public void cleanup() throws Exception {
		dbTunnel.cleanup();
	}

}

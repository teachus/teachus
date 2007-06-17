package dk.teachus.tools.upgrade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * Upgrade application for TeachUs 1.37 or newer.
 */
public class Upgrade {
	
	private static final String PROPERTY_MAVEN_PATH = "maven.path";
	private static final String PROPERTY_SVN_RELEASE = "svn.release";

	private static final String PROPERTY_MAIN_TOMCAT = "main.tomcat";
	private static final String PROPERTY_DEMO_TOMCAT = "demo.tomcat";
	private static final String PROPERTY_BACKUP_DIR = "backup.dir";
	
	private static final String PROPERTY_OLD_VERSION = "version.old";
	private static final String PROPERTY_NEW_VERSION = "version.new";
	
	private static final String PROPERTY_MAIN_DB_HOSTNAME = "main.db.host";
	private static final String PROPERTY_MAIN_DB_DATABASE = "main.db.database";
	private static final String PROPERTY_MAIN_DB_USERNAME = "main.db.username";
	private static final String PROPERTY_MAIN_DB_PASSWORD = "main.db.password";
	
	private static final String PROPERTY_DEMO_DB_HOSTNAME = "demo.db.host";
	private static final String PROPERTY_DEMO_DB_DATABASE = "demo.db.database";
	private static final String PROPERTY_DEMO_DB_USERNAME = "demo.db.username";
	private static final String PROPERTY_DEMO_DB_PASSWORD = "demo.db.password";
	
	private static final String CONFIGURATION_DB_URL = "db.url";
	private static final String CONFIGURATION_DB_USERNAME = "db.username";
	private static final String CONFIGURATION_DB_PASSWORD = "db.password";
	
	public Upgrade() throws Exception {
		// Get properties
		Properties properties = getProperties();
		
		// Stop both tomcats
		stopTomcat(properties.getProperty(PROPERTY_MAIN_TOMCAT));
		stopTomcat(properties.getProperty(PROPERTY_DEMO_TOMCAT));
		
		// Get us a temporary folder
		File workingDirectory = createWorkingDirectory();
		
		// Checkout release version
		checkoutVersion(workingDirectory, properties);
		
		// Backup database
//		backupDatabase(properties);
		
		// Main
		modifyMainConfiguration(workingDirectory, properties);
		File warFile = createWarFile(workingDirectory, properties);
		deploy(warFile, properties.getProperty(PROPERTY_MAIN_TOMCAT));
		
		// Demo
		modifyDemoConfiguration(workingDirectory, properties);
		warFile = createWarFile(workingDirectory, properties);
		deploy(warFile, properties.getProperty(PROPERTY_DEMO_TOMCAT));
        
		// Start both tomcats
		startTomcat(properties.getProperty(PROPERTY_MAIN_TOMCAT));
		startTomcat(properties.getProperty(PROPERTY_DEMO_TOMCAT));
		
		cleanup(workingDirectory);
	}
	
	private void backupDatabase(Properties properties) throws Exception {
		String version = properties.getProperty(PROPERTY_OLD_VERSION);
		String backupFile = new File(properties.getProperty(PROPERTY_BACKUP_DIR), "teachus-"+version+".sql").getAbsolutePath();
		
		ProcessBuilder pb = new ProcessBuilder("mysqldump"
				, "-h", properties.getProperty(PROPERTY_MAIN_DB_HOSTNAME)
				, "-u", properties.getProperty(PROPERTY_MAIN_DB_USERNAME)
				, "-p", properties.getProperty(PROPERTY_MAIN_DB_PASSWORD)
				, "-c", "--delayed-insert", "--add-locks", "--add-drop-table"
				, "--disable-keys", "--single-transaction"
				, properties.getProperty(PROPERTY_MAIN_DB_DATABASE)
				, ">", backupFile);
		
		Process databaseBackup = pb.start();
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(databaseBackup.getInputStream()));
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			System.out.println(line);
		}
		
		if (databaseBackup.waitFor() != 0) {
			throw new RuntimeException("Database backup failed.");
		}
	}
	
	private void deploy(File warFile, String tomcatHome) throws Exception {
		// First delete the old ROOT.war and folder
		File deployWar = new File(tomcatHome, "webapps/ROOT.war");
		deployWar.delete();
		FileUtils.deleteDirectory(new File(tomcatHome, "webapps/ROOT"));
		
		FileUtils.copyFile(warFile, deployWar);
	}
	
	private void stopTomcat(String tomcatHome) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(tomcatHome+"/bin/shutdown.sh");
		pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
		Process tomcatShutdown = pb.start();
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(tomcatShutdown.getInputStream()));
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			System.out.println(line);
		}
		
		if (tomcatShutdown.waitFor() != 0) {
			throw new RuntimeException("Tomcat didn't shutdown correctly: "+tomcatHome);
		}
	}
	
	private void startTomcat(String tomcatHome) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(tomcatHome+"/bin/startup.sh");
		pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
		Process tomcatStartup = pb.start();
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(tomcatStartup.getInputStream()));
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			System.out.println(line);
		}
		
		if (tomcatStartup.waitFor() != 0) {
			throw new RuntimeException("Tomcat didn't startup correctly: "+tomcatHome);
		}
	}
	
	private void modifyMainConfiguration(File workingDirectory, Properties properties) throws Exception {
		String dbHost = properties.getProperty(PROPERTY_MAIN_DB_HOSTNAME);
		String dbDatabase = properties.getProperty(PROPERTY_MAIN_DB_DATABASE);
		String dbUsername = properties.getProperty(PROPERTY_MAIN_DB_USERNAME);
		String dbPassword = properties.getProperty(PROPERTY_MAIN_DB_PASSWORD);
		
		modifyConfiguration(workingDirectory, dbHost, dbDatabase, dbUsername, dbPassword);
	}
	
	private void modifyDemoConfiguration(File workingDirectory, Properties properties) throws Exception {
		String dbHost = properties.getProperty(PROPERTY_DEMO_DB_HOSTNAME);
		String dbDatabase = properties.getProperty(PROPERTY_DEMO_DB_DATABASE);
		String dbUsername = properties.getProperty(PROPERTY_DEMO_DB_USERNAME);
		String dbPassword = properties.getProperty(PROPERTY_DEMO_DB_PASSWORD);
		
		modifyConfiguration(workingDirectory, dbHost, dbDatabase, dbUsername, dbPassword);
	}
	
	private void modifyConfiguration(File workingDirectory, String dbHost, String dbDatabase, String dbUsername, String dbPassword) throws Exception {
		// Load the configuration file
		File configurationFile = new File(workingDirectory, "teachus-frontend/src/main/webapp/WEB-INF/teachus.properties");
		
		Properties configuration = new Properties();
		configuration.load(new FileInputStream(configurationFile));
		
		String dbUrl = "jdbc:mysql://"+dbHost+"/"+dbDatabase;
		configuration.setProperty(CONFIGURATION_DB_URL, dbUrl);
		configuration.setProperty(CONFIGURATION_DB_USERNAME, dbUsername);
		configuration.setProperty(CONFIGURATION_DB_PASSWORD, dbPassword);
		
		configuration.store(new FileOutputStream(configurationFile), null);
	}
	
	private void cleanup(File workingDirectory) throws Exception {
		FileUtils.forceDelete(workingDirectory);
	}
	
	private Properties getProperties() throws Exception {
		Properties properties = new Properties();
		
		File propertyFile = new File(System.getProperty("user.home"), ".teachus/upgrade.properties");
		propertyFile.getParentFile().mkdir();
		
		if (propertyFile.exists()) {
			properties.load(new FileInputStream(propertyFile));
		}
		
		checkProperty(properties, PROPERTY_MAVEN_PATH, "Absolute path to maven binary: ");
		checkProperty(properties, PROPERTY_SVN_RELEASE, "Subversion release url (%r is replace by version number): ");
		
		checkProperty(properties, PROPERTY_BACKUP_DIR, "Database backup directory: ");
		
		checkProperty(properties, PROPERTY_MAIN_TOMCAT, "Main Tomcat home: ");
		checkProperty(properties, PROPERTY_DEMO_TOMCAT, "Demo Tomcat home: ");
		
		checkProperty(properties, PROPERTY_MAIN_DB_HOSTNAME, "Main database host: ");
		checkProperty(properties, PROPERTY_MAIN_DB_DATABASE, "Main database name: ");
		checkProperty(properties, PROPERTY_MAIN_DB_USERNAME, "Main database username: ");
		checkProperty(properties, PROPERTY_MAIN_DB_PASSWORD, "Main database password: ");
		
		checkProperty(properties, PROPERTY_DEMO_DB_HOSTNAME, "Demo database host: ");
		checkProperty(properties, PROPERTY_DEMO_DB_DATABASE, "Demo database name: ");
		checkProperty(properties, PROPERTY_DEMO_DB_USERNAME, "Demo database username: ");
		checkProperty(properties, PROPERTY_DEMO_DB_PASSWORD, "Demo database password: ");
		
		properties.store(new FileOutputStream(propertyFile), "Configuration for upgrading teachus.");
		
		checkProperty(properties, PROPERTY_OLD_VERSION, "Old version: ");
		checkProperty(properties, PROPERTY_NEW_VERSION, "New version: ");
		
		return properties;
	}

	private File createWorkingDirectory() throws IOException {
		File tempDir = File.createTempFile("teachus", "teachus");
		tempDir.delete();
		tempDir.mkdir();
		return tempDir;
	}

	private File createWarFile(File tempDir, Properties properties) throws Exception {
		File parentDir = new File(tempDir, "teachus-parent");
		
		ProcessBuilder processBuilder = new ProcessBuilder(properties.getProperty(PROPERTY_MAVEN_PATH), "clean", "package");
		processBuilder.directory(parentDir);
		processBuilder.environment().put("JAVA_HOME", System.getProperty("java.home"));
		Process maven = processBuilder.start();
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(maven.getInputStream()));
		
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			System.out.println(line);
		}
		
		if (maven.waitFor() != 0) {
			throw new RuntimeException("Maven didn't complete correctly");
		}
        
        return new File(tempDir, "teachus-frontend/target/teachus-frontend-"+properties.getProperty(PROPERTY_NEW_VERSION)+".war");
	}

	private void checkoutVersion(File workingDir, Properties properties) throws SVNException {
		String svnRelease = properties.getProperty(PROPERTY_SVN_RELEASE);
		svnRelease = svnRelease.replace("%r", properties.getProperty(PROPERTY_NEW_VERSION));
		
		DAVRepositoryFactory.setup();
		SVNURL url = SVNURL.parseURIDecoded(svnRelease);
		SVNUpdateClient updateClient = new SVNUpdateClient(null, null);
		updateClient.doCheckout(url, workingDir, SVNRevision.HEAD, SVNRevision.HEAD, true);
	}
	
	private void checkProperty(Properties properties, String property, String label) {
		String value = properties.getProperty(property);
		
		while (value == null) {
			value = getInput(label);
		}
		
		properties.setProperty(property, value);
	}

	private String getInput(String label) {
		System.out.print(label);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = null;
		
		try {
			line = br.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return line;
	}
	
	public static void main(String[] args) throws Exception {
		new Upgrade();
	}

}

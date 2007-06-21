package dk.teachus.tools.upgrade.actions;

import java.io.File;
import java.util.Properties;

import dk.teachus.tools.upgrade.config.DatabaseNode;

public class ConfigureDemoDatabaseAction extends AbstractConfigurePropertiesAction {
	
	private File projectDirectory;
	private DatabaseNode database;

	public ConfigureDemoDatabaseAction(File projectDirectory, DatabaseNode database) {
		this.projectDirectory = projectDirectory;
		this.database = database;
	}

	@Override
	protected void configureProperties(Properties properties) {
		properties.setProperty("db.url", database.getJdbcUrl());
		properties.setProperty("db.username", database.getUsername());
		properties.setProperty("db.password", database.getPassword());
	}

	@Override
	protected File getPropertiesFile() {
		return new File(projectDirectory, "teachus-backend/src/test/java/dk/teachus/backend/database/dynamicDataImport.properties");
	}

}

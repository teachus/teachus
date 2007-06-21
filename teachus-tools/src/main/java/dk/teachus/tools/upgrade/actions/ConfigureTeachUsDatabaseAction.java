package dk.teachus.tools.upgrade.actions;

import java.io.File;
import java.util.Properties;

import dk.teachus.tools.upgrade.config.DatabaseNode;

public class ConfigureTeachUsDatabaseAction extends AbstractConfigureTeachUsPropertiesAction {
	
	private DatabaseNode database;

	public ConfigureTeachUsDatabaseAction(File projectDirectory, DatabaseNode database) {
		super(projectDirectory);
		this.database = database;
	}
	
	@Override
	protected void configureProperties(Properties properties) {
		properties.setProperty("db.url", database.getJdbcUrl());
		properties.setProperty("db.username", database.getUsername());
		properties.setProperty("db.password", database.getPassword());
	}

}

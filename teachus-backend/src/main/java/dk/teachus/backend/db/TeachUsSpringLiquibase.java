package dk.teachus.backend.db;

import liquibase.database.typeconversion.TypeConverter;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.servicelocator.ServiceLocator;

/**
 * This class extends {@link SpringLiquibase} to register the customer {@link TypeConverter} that we use to correctly
 * translate the column types to correct mysql data types.
 */
public class TeachUsSpringLiquibase extends SpringLiquibase {
	
	public TeachUsSpringLiquibase() {
		ServiceLocator.getInstance().addPackageToScan("dk.teachus.backend.db");
	}
	
}

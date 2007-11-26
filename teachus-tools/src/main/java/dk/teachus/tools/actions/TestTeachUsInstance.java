package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;
import dk.teachus.tools.config.SubversionTrunkNode;
import dk.teachus.tools.config.TestDeploymentNode;

public class TestTeachUsInstance extends AbstractTeachUsInstance {

	private ConfigureMailBeanAction configureMailBean;
	private LoadTestDataAction loadTestData;
	private final SubversionTrunkNode subversion;
	private DropAllTablesAction dropAllTables;
	private LoadSchemaAction loadSchema;
	private GetVersionAction getVersion;
	private SetDatabaseVersionAction setDatabaseVersion;

	public TestTeachUsInstance(MavenNode maven, File workingDirectory,
			TestDeploymentNode deployment, SshNode databaseHost,
			SubversionTrunkNode subversion) throws Exception {
		super(maven, workingDirectory, deployment, databaseHost, null);
		this.subversion = subversion;
	}

	@Override
	protected AbstractSubversionCheckoutAction getCheckoutAction() {
		return new SubversionCheckoutTrunkAction(projectDirectory, subversion);
	}

	public String getInstanceName() {
		return "test";
	}	
	
	@Override
	public void init() throws Exception {
		super.init();

		dropAllTables = new DropAllTablesAction(databaseHost, deployment.getDatabase());
		dropAllTables.init();
		loadSchema = new LoadSchemaAction(databaseHost, deployment.getDatabase(), projectDirectory);
		loadSchema.init();
		configureMailBean = new ConfigureMailBeanAction(projectDirectory);
		configureMailBean.init();
		getVersion = new GetVersionAction(projectDirectory);
		getVersion.init();
		loadTestData = new LoadTestDataAction(databaseHost, projectDirectory, deployment.getDatabase(), maven);
		loadTestData.init();
		VersionProvider versionProvider = new VersionProvider() {
			public String getVersion() {
				return version;
			}
		};		
		setDatabaseVersion = new SetDatabaseVersionAction(databaseHost, deployment.getDatabase(), versionProvider);
		setDatabaseVersion.init();
	}
	
	@Override
	public void check() throws Exception {
		super.check();
		
		dropAllTables.check();
		loadSchema.check();
		configureMailBean.check();
		getVersion.check();
		loadTestData.check();
		setDatabaseVersion.check();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		dropAllTables.cleanup();
		loadSchema.cleanup();
		configureMailBean.cleanup();
		getVersion.cleanup();
		loadTestData.cleanup();
		setDatabaseVersion.cleanup();
	}
	
	@Override
	protected void beforePackage() throws Exception {
		configureMailBean.execute();
		
		getVersion.execute();
		
		version = getVersion.getVersion();
	}

	public void onAfterUpgradeDatabase() throws Exception {
		loadTestData.execute();
		
		setDatabaseVersion.execute();
	}

	public void upgradeDatabase() throws Exception {
		dropAllTables.execute();
		
		loadSchema.execute();
	}

	public void onBeforeUpgradeDatabase() throws Exception {
	}

}

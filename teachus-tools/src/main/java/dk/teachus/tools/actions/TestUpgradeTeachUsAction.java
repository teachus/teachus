package dk.teachus.tools.actions;

import java.io.File;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import dk.teachus.tools.config.DemoDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionTrunkNode;
import dk.teachus.tools.config.TomcatNode;

public class TestUpgradeTeachUsAction extends UpgradeTeachUsAction {

	private ConfigureMailBeanAction configureMailBean;
	private LoadTestDataAction loadTestData;
	private final SubversionTrunkNode subversion;
	private DropAllTablesAction dropAllTables;
	private LoadSchemaAction loadSchema;
	private GetVersionAction getVersion;
	private SetDatabaseVersionAction setDatabaseVersion;

	public TestUpgradeTeachUsAction(MavenNode maven, SubversionTrunkNode subversion, File workingDirectory, DemoDeploymentNode deployment, TomcatNode tomcat) throws Exception {
		super(maven, workingDirectory, deployment, tomcat, null);
		this.subversion = subversion;
	}

	@Override
	protected void doDatabase() throws Exception {
		dropAllTables.execute();
		
		loadSchema.execute();
	}
	
	@Override
	protected AbstractSubversionCheckoutAction getCheckoutAction() {
		return new SubversionCheckoutTrunkAction(projectDirectory, subversion);
	}
	
	@Override
	public void init() throws Exception {
		super.init();

		dropAllTables = new DropAllTablesAction(tomcat.getHost(), deployment.getDatabase());
		dropAllTables.init();
		loadSchema = new LoadSchemaAction(tomcat.getHost(), deployment.getDatabase(), projectDirectory);
		loadSchema.init();
		configureMailBean = new ConfigureMailBeanAction(projectDirectory);
		configureMailBean.init();
		getVersion = new GetVersionAction(projectDirectory);
		getVersion.init();
		loadTestData = new LoadTestDataAction(tomcat.getHost(), projectDirectory, deployment.getDatabase(), maven);
		loadTestData.init();
		VersionProvider versionProvider = new VersionProvider() {
			public String getVersion() {
				return version;
			}
		};		
		setDatabaseVersion = new SetDatabaseVersionAction(tomcat.getHost(), deployment.getDatabase(), versionProvider);
		setDatabaseVersion.init();
	}
	
	@Override
	protected void doCheck() throws Exception {
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
	protected String getName() {
		return "test";
	}
	
	@Override
	protected void beforePackage() throws Exception {
		configureMailBean.execute();
		
		getVersion.execute();
		
		version = getVersion.getVersion();
		version = version.replace("SNAPSHOT", ISODateTimeFormat.dateTime().print(new DateTime()));
	}
	
	@Override
	protected void afterDeployment() throws Exception {
		loadTestData.execute();
		
		setDatabaseVersion.execute();
	}

}

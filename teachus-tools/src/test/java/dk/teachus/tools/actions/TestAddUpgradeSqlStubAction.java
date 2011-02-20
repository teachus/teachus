package dk.teachus.tools.actions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.teachus.tools.Workflow;

@RunWith(JMock.class)
public class TestAddUpgradeSqlStubAction {

	private File projectDirectory;
	
	private Mockery context = new JUnit4Mockery();

	@Before
	public void createStubProjectDirectory() throws Exception {
		projectDirectory = File.createTempFile("teachus", "test");
		projectDirectory.delete();
		projectDirectory.mkdirs();
		
		// Create folder where the upgrade file is
		new File(projectDirectory, "teachus-backend/src/main/database/upgrade/").mkdirs();
		
		// Create database stub file
		InputStream input = TestAddUpgradeSqlStubAction.class.getResourceAsStream("/mysql.sql");
		FileWriter output = new FileWriter(new File(projectDirectory, "teachus-backend/src/main/database/mysql.sql"));
		IOUtils.copy(input, output);
		output.close();
		input.close();
	}
	
	@After
	public void deleteStubProjectDirectory() throws Exception {
		FileUtils.deleteDirectory(projectDirectory);
	}
	
	@Test
	public void testCreateDatabaseStubs() throws Exception {
		final ScmClient scmClient = context.mock(ScmClient.class);
		context.checking(new Expectations() {{
			oneOf(scmClient).commit(with(projectDirectory), with("Creating stub for database upgrade sql file."), with(new BaseMatcher<File[]>() {
				public boolean matches(Object item) {
					File[] files = (File[]) item;
					
					assertEquals(2, files.length);
					
					boolean evolutionFileFound = false;
					boolean dbSchemaFileFound = false;
					for (File file : files) {
						if (file.getName().equals("1.57.sql")) {
							// Check that it looks correct
							assertThat(readFileToString(file), containsString("1.57"));
							assertThat(readFileToString(file), not(containsString("1.56")));
							
							evolutionFileFound = true;
						} else if (file.getName().equals("mysql.sql")) {
							// Check that it looks correct
							assertThat(readFileToString(file), containsString("1.57"));
							assertThat(readFileToString(file), not(containsString("1.56")));
							
							dbSchemaFileFound = true;
						} else {
							fail("Found an unexpected file.");
						}
					}
					
					return evolutionFileFound && dbSchemaFileFound;
				}

				public void describeTo(Description description) {
				}
			}));
        }});
		
		AddUpgradeSqlStubAction action = new AddUpgradeSqlStubAction(projectDirectory, scmClient);
		action.setVersion("1.57-SNAPSHOT");

		Workflow workflow = new Workflow();
		workflow.setFailOnError(true);
		workflow.addAction(action);
		workflow.start();
	}
	
	private String readFileToString(File file) {
		try {
			return FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}

package dk.teachus.backend.database;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class TestSchemaUpgrade extends TestCase {

	public void testUpgradeScriptExists() throws Exception {
		// First find version of current development version
		File pomFile = new File("pom.xml");
		assertTrue(pomFile.exists());
		
		Pattern verPat = Pattern.compile(".*<parent>.*?<version>([^<]+)</version>.*", Pattern.DOTALL);
		Matcher matcher = verPat.matcher(FileUtils.readFileToString(pomFile, "UTF-8"));
		assertTrue(matcher.matches());
		
		String version = matcher.group(1);
		
		// Strip the -SNAPSHOT from the version
		version = version.replace("-SNAPSHOT", "");
		
		// Check if the file exists
		File sqlUpgradeFile = new File("src/main/database/upgrade/"+version+".sql");
		assertTrue("The sql upgrade file "+sqlUpgradeFile.getName()+" doesn't exist.", sqlUpgradeFile.exists());
	}
	
}

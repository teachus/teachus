package dk.teachus.tools.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class DetermineVersionAction implements Action {
	private static final Pattern VERSION_PATTERN = Pattern
			.compile("^([0-9]+)\\.([0-9]+)(\\.([0-9]+))?$");

	private File projectDirectory;

	private String version;

	private String nextVersion;

	public DetermineVersionAction(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}

	public String getNextVersion() {
		return nextVersion;
	}

	public String getVersion() {
		return version;
	}
	
	public void check() throws Exception {
		if (projectDirectory == null) {
			throw new IllegalStateException("Project directory must not be null");
		}
		
		if (projectDirectory.exists() == false) {
			throw new IllegalStateException("Project directory doesn't exist");
		}
		
		if (projectDirectory.isDirectory() == false) {
			throw new IllegalStateException("Project directory is not a directory");
		}
	}
	
	public void cleanup() throws Exception {
	}

	public void execute() throws Exception {
		File parentPom = new File(projectDirectory, "teachus-parent/pom.xml");

		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(parentPom);

		XPath xpath = XPath.newInstance("/maven:project/maven:version");
		xpath.addNamespace("maven", "http://maven.apache.org/POM/4.0.0");
		Element element = (Element) xpath.selectSingleNode(document);
		version = element.getValue();

		// Remove any suffixes (like -SNAPSHOT)
		version = version.replaceAll("-SNAPSHOT$", "");

		/*
		 * Try to find out next version
		 */

		Matcher matcher = VERSION_PATTERN.matcher(version);
		if (matcher.matches()) {
			int majorVersion = getVersionPart(matcher.group(1));
			int minorVersion = getVersionPart(matcher.group(2));
			int bugfixVersion = getVersionPart(matcher.group(4));

			// What to increase
			if (bugfixVersion > -1) {
				bugfixVersion++;
			} else {
				minorVersion++;
			}

			StringBuilder nv = new StringBuilder();
			
			nv.append(majorVersion);
			nv.append(".");
			nv.append(minorVersion);
			if (bugfixVersion > -1) {
				nv.append(".");
				nv.append(bugfixVersion);
			}
			nv.append("-SNAPSHOT");
			
			nextVersion = nv.toString();
		}
		
		/*
		 * Now ask for new versions, and surgest the versions determined
		 */
		String inputVersion = getInput("Release version ["+version+"]: ");
		String inputNextVersion = getInput("Next version ["+nextVersion+"]: ");
		
		version = inputVersion != null && inputVersion.length() > 0 ? inputVersion : version;
		nextVersion = inputNextVersion != null && inputNextVersion.length() > 0 ? inputNextVersion : nextVersion;
	}
	
	private static String getInput(String label) {
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

	private int getVersionPart(String versionPartString) {
		int versionPart = -1;
		try {
			versionPart = Integer.parseInt(versionPartString);
		} catch (NumberFormatException e) {
		}
		return versionPart;
	}

}
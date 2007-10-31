package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class GetVersionAction implements Action {
	private static final Log log = LogFactory.getLog(GetVersionAction.class);

	private final File projectDirectory;
	private String version;

	public GetVersionAction(File projectDirectory) {
		this.projectDirectory = projectDirectory;
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
		
		log.info("Current version is: "+version);
	}
	
	public String getVersion() {
		return version;
	}

	public void init() throws Exception {
	}

}

package dk.teachus.tools.actions;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class ModifyPomVersionAction implements Action {
	private static final Log log = LogFactory.getLog(ModifyPomVersionAction.class);

	private File projectDirectory;
	private String version;
	
	public ModifyPomVersionAction(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}
	
	public void setVersion(String version) {
		if (version == null || version.length() == 0) {
			throw new IllegalArgumentException("The version can not be empty");
		}
		
		this.version = version;
	}

	public void execute() throws Exception {
		log.info("Upgrading pom files to: "+version);
		
		modifyParentPom("teachus-parent/pom.xml");
		
		modifyModulePom("teachus-backend/pom.xml");
		modifyModulePom("teachus-frontend/pom.xml");
		modifyModulePom("teachus-test/pom.xml");
		modifyModulePom("teachus-ws/pom.xml");
		
		SubversionCommitAction subversionCommit = new SubversionCommitAction("Upgrading to "+version, 
				getFiles(
				"teachus-parent/pom.xml",
				"teachus-backend/pom.xml",
				"teachus-frontend/pom.xml",
				"teachus-ws/pom.xml",
				"teachus-test/pom.xml"));
		subversionCommit.execute();
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
	
	private File[] getFiles(String... files) {
		List<File> fileList = new ArrayList<File>();
		
		for (String file : files) {
			File f = new File(projectDirectory, file);
			fileList.add(f);
		}
		
		return fileList.toArray(new File[fileList.size()]);
	}

	private void modifyModulePom(String pomFile) throws Exception {
		File parentPom = new File(projectDirectory, pomFile);
		
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(parentPom);

		XPath xpath = XPath.newInstance("/maven:project/maven:parent/maven:version");
		xpath.addNamespace("maven", "http://maven.apache.org/POM/4.0.0");
		Element element = (Element) xpath.selectSingleNode(document);
		element.setText(version);
						
		FileWriter writer = new FileWriter(parentPom);
		Format format = Format.getRawFormat();
		format.setLineSeparator(System.getProperty("line.separator"));
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(document, writer);
		writer.close();
	}

	private void modifyParentPom(String pomFile) throws Exception {
		File parentPom = new File(projectDirectory, pomFile);
		
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(parentPom);

		XPath xpath = XPath.newInstance("/maven:project/maven:version");
		xpath.addNamespace("maven", "http://maven.apache.org/POM/4.0.0");
		Element element = (Element) xpath.selectSingleNode(document);
		element.setText(version);
						
		FileWriter writer = new FileWriter(parentPom);
		Format format = Format.getRawFormat();
		format.setLineSeparator(System.getProperty("line.separator"));
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(document, writer);
		writer.close();
	}

}

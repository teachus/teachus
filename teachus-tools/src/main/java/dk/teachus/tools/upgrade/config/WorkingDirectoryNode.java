package dk.teachus.tools.upgrade.config;

import java.io.File;

import nanoxml.XMLElement;

public class WorkingDirectoryNode extends AbstractNode {
	
	private String workingDirectory;
	
	public WorkingDirectoryNode() {
		setWorkingDirectory(null);
	}
	
	
	public WorkingDirectoryNode(String workingDirectory) {
		setWorkingDirectory(workingDirectory);
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}
	
	public File getWorkingDirectoryFile() {
		return new File(workingDirectory);
	}

	public void setWorkingDirectory(String workingDirectory) {
		if (workingDirectory == null) {
			workingDirectory = "";
		}
		this.workingDirectory = workingDirectory;
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("workingdirectory")) {
			workingDirectory = element.getContent();
		}
	}

	public void requestValue() {
		if (isEmpty(workingDirectory)) {
			workingDirectory = getInput("Working directory: ");
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("workingdirectory");
		element.setContent(workingDirectory);
		return element;
	}

}

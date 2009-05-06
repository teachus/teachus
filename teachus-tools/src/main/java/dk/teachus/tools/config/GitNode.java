package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class GitNode extends AbstractNode {
	
	private StringNode remoteGitUrl;
	private StringNode committerName;
	private StringNode committerEmail;
	
	public GitNode() {
		remoteGitUrl = new StringNode("remoteurl");
		committerName = new StringNode("name");
		committerEmail = new StringNode("email");
	}
	
	public String getRemoteGitUrl() {
		return remoteGitUrl.getValue();
	}
	
	public String getCommitterName() {
		return committerName.getValue();
	}
	
	public String getCommitterEmail() {
		return committerEmail.getValue();
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("git")) {
			remoteGitUrl = new StringNode();
			remoteGitUrl.deserialize(findChild(element, "remoteurl"));

			committerName = new StringNode();
			committerName.deserialize(findChild(element, "name"));

			committerEmail = new StringNode();
			committerEmail.deserialize(findChild(element, "email"));
		}
	}

	public void requestValue() {
		getInputForString(remoteGitUrl, "Git remote URL: ");
		getInputForString(committerName, "Committer/Author Name: ");
		getInputForString(committerEmail, "Committer/Author Email: ");
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("git");
		element.addChild(remoteGitUrl.serialize());
		element.addChild(committerName.serialize());
		element.addChild(committerEmail.serialize());
		return element;
	}

}

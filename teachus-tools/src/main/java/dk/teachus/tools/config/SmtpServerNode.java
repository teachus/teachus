package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class SmtpServerNode extends AbstractNode {
	
	private StringNode smtpServer;
	
	public SmtpServerNode() {
		this(null);
	}
	
	public SmtpServerNode(String smtpServer) {
		this.smtpServer = new StringNode("server", smtpServer);
	}
	
	public String getSmtpServer() {
		return smtpServer.getValue();
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("smtp")) {
			smtpServer = new StringNode();
			smtpServer.deserialize(findChild(element, "server"));
		}
	}

	public void requestValue() {
		getInputForString(smtpServer, "SMTP server: ");
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("smtp");
		element.addChild(smtpServer.serialize());
		return element;
	}

}

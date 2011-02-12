package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class DatabaseBackupNode extends AbstractNode {

	private StringNode mysqlDumpBinary;
	
	public DatabaseBackupNode() {
		this(null);
	}
	
	public DatabaseBackupNode(String mysqlDumpBinary) {
		this.mysqlDumpBinary = new StringNode("mysqldumpbinary", mysqlDumpBinary);
	}

	public String getMysqlDumpBinary() {
		return mysqlDumpBinary.getValue();
	}
	
	public void setMysqlDumpBinary(String mysqlDumpBinary) {
		this.mysqlDumpBinary.setValue(mysqlDumpBinary);
	}
	
	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("databasebackup");
		element.addChild(mysqlDumpBinary.serialize());
		return element;
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("databasebackup")) {
			mysqlDumpBinary = new StringNode();
			mysqlDumpBinary.deserialize(findChild(element, "mysqldumpbinary"));
		}
	}

	public void requestValue() {
		getInputForString(mysqlDumpBinary, "Mysqldump (bin): ");
	}

}

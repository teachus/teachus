package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public interface Node {
	
	XMLElement serialize();
	
	void deserialize(XMLElement element);
	
	void requestValue();
	
}

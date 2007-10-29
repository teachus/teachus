package dk.teachus.tools.config;

import nanoxml.XMLElement;

public interface Node {
	
	XMLElement serialize();
	
	void deserialize(XMLElement element);
	
	void requestValue();
	
}

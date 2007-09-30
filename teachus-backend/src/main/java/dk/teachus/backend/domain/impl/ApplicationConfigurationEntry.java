package dk.teachus.backend.domain.impl;

import java.io.Serializable;

public class ApplicationConfigurationEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int version;
	
	private String key;
	private String value;

	protected ApplicationConfigurationEntry() {
	}

	public ApplicationConfigurationEntry(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

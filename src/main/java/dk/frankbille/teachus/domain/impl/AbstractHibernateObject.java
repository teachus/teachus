package dk.frankbille.teachus.domain.impl;

import java.io.Serializable;

public abstract class AbstractHibernateObject implements Serializable {
	private Long id;
	private int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

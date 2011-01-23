/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.domain.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import dk.teachus.backend.domain.ApplicationConfiguration;

public class ApplicationConfigurationImpl implements ApplicationConfiguration {
	private static final long serialVersionUID = 1L;

	private List<ApplicationConfigurationEntry> entries;
	private List<PropertyChangeListener> listeners;
	
	public ApplicationConfigurationImpl(List<ApplicationConfigurationEntry> entries) {
		this.entries = entries;
	}
	
	public boolean hasConfiguration(String configurationKey) {
		boolean hasConfiguration = false;
		
		ApplicationConfigurationEntry foundEntry = findConfigurationEntry(configurationKey);
		
		if (foundEntry != null) {
			if (foundEntry.getValue() != null) {
				hasConfiguration = true;
			}
		}
		
		return hasConfiguration;
	}

	public String getConfiguration(String configurationKey) {
		String configurationValue = null;

		ApplicationConfigurationEntry foundEntry = findConfigurationEntry(configurationKey);

		if (foundEntry != null) {
			configurationValue = foundEntry.getValue();
		}
		
		return configurationValue;
	}

	public int getConfigurationInteger(String configurationKey) {
		int intValue = -1;
		
		String configurationValue = getConfiguration(configurationKey);
		
		if (configurationValue != null && configurationValue.length() > 0) {
			intValue = Integer.parseInt(configurationValue);
		}
		
		return intValue;
	}

	public void setConfiguration(String configurationKey, String configurationValue) {
		if (configurationKey == null) {
			throw new IllegalArgumentException("Configuration key must not be null");
		}
		
		if (entries == null) {
			entries = new ArrayList<ApplicationConfigurationEntry>();
		}
		
		ApplicationConfigurationEntry foundEntry = findConfigurationEntry(configurationKey);
		
		String oldValue = null;
		if (foundEntry != null) {
			oldValue = foundEntry.getValue();
			foundEntry.setValue(configurationValue);
		} else {
			entries.add(new ApplicationConfigurationEntry(configurationKey, configurationValue));
		}
		
		if (String.valueOf(oldValue).equals(configurationValue) == false) {
			firePropertyChangeEvent("configuration", oldValue, configurationValue);
		}
	}

	public void setConfigurationInteger(String configurationKey, int configurationValue) {
		setConfiguration(configurationKey, ""+configurationValue);
	}
	
	public List<ApplicationConfigurationEntry> getEntries() {
		return entries;
	}
	
	public void addPropertyListener(PropertyChangeListener listener) {
		if (listener != null) {
			if (listeners == null) {
				listeners = new ArrayList<PropertyChangeListener>();
				
				listeners.add(listener);
			}
		}
	}
	
	public void removePropertyListener(PropertyChangeListener listener) {
		if (listener != null) {
			if (listeners != null) {
				listeners.remove(listener);
			}
		}
	}

	private ApplicationConfigurationEntry findConfigurationEntry(String configurationKey) {
		ApplicationConfigurationEntry foundEntry = null;
		if (entries != null) {
			for (ApplicationConfigurationEntry entry : entries) {
				if (entry.getKey().equals(configurationKey)) {
					foundEntry = entry;
					break;
				}
			}
		}
		return foundEntry;
	}
	
	private void firePropertyChangeEvent(String property, Object oldValue, Object newValue) {
		if (listeners != null) {
			PropertyChangeEvent e = new PropertyChangeEvent(this, property, oldValue, newValue);
			for (PropertyChangeListener listener : listeners) {
				listener.propertyChange(e);
			}
		}
	}

}

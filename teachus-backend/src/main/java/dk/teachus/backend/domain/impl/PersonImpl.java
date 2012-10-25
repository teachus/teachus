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

import java.util.Locale;

import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Theme;

public abstract class PersonImpl extends AbstractJpaObject implements Person {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String phoneNumber;
	
	private Locale locale;
	
	private Theme theme;
	
	private boolean active = true;
	
	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public Locale getLocale() {
		return locale;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(final boolean active) {
		this.active = active;
	}
	
	@Override
	public void setEmail(final String email) {
		this.email = email;
	}
	
	@Override
	public void setLocale(final Locale locale) {
		this.locale = locale;
	}
	
	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public void setPassword(final String password) {
		this.password = password;
	}
	
	@Override
	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public void setUsername(final String username) {
		this.username = username;
	}
	
	@Override
	public Theme getTheme() {
		return theme;
	}
	
	@Override
	public void setTheme(final Theme theme) {
		this.theme = theme;
	}
}

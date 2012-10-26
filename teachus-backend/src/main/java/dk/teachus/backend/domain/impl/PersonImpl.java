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

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Theme;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class PersonImpl extends AbstractJdoObject implements Person {
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private String name;
	
	@Persistent
	private String username;
	
	@Persistent
	private String password;
	
	@Persistent
	private String email;
	
	@Persistent(column = "phone_number")
	private String phoneNumber;
	
	@Persistent
	private String locale;
	
	@Persistent
	private Theme theme;
	
	@Persistent
	private boolean active = true;
	
	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public Locale getLocale() {
		return new Locale(locale);
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
		this.locale = locale.toString();
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

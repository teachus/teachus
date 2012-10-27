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
package dk.teachus.backend.domain;

import java.io.Serializable;
import java.util.Locale;

public interface Person extends Serializable {
	Long getId();
	
	String getName();
	
	void setName(String name);
	
	String getUsername();
	
	void setUsername(String username);
	
	String getPassword();
	
	void setPassword(String password);
	
	String getHashedPassword();
	
	String getEmail();
	
	void setEmail(String email);
	
	String getPhoneNumber();
	
	void setPhoneNumber(String phoneNumber);
	
	Locale getLocale();
	
	void setLocale(Locale locale);
	
	Theme getTheme();
	
	void setTheme(Theme theme);
	
	boolean isActive();
	
	void setActive(boolean active);
}

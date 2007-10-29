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
import java.util.Date;
import java.util.Set;

public interface Message extends Serializable {
	
	Long getId();
	
	Date getCreateDate();
	void setCreateDate(Date createDate);
	
	boolean isSent();
	void setSent(boolean sent);
	
	Date getSentDate();
	void setSentDate(Date sentDate);
	
	Person getSender();
	void setSender(Person sender);
	
	Set<Person> getRecipients();
	void setRecipients(Set<Person> recipients);
	void addRecipient(Person recipient);
		
	String getSubject();
	void setSubject(String subject);
	
	String getBody();
	void setBody(String body);
	
}

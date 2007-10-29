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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.Person;

public abstract class AbstractMessage extends AbstractHibernateObject implements Message {
	
	private Date createDate;
	private String subject;
	private String body;
	private Person sender;
	private Set<Person> recipients;
	private boolean sent;
	private Date sentDate;
	
	public String getBody() {
		return body;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public Set<Person> getRecipients() {
		return recipients;
	}
	
	public Person getSender() {
		return sender;
	}
	
	public Date getSentDate() {
		return sentDate;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public boolean isSent() {
		return sent;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setRecipients(Set<Person> recipients) {
		this.recipients = recipients;
	}
	
	public void addRecipient(Person recipient) {
		if (recipients == null) {
			recipients = new HashSet<Person>();
		}
		
		recipients.add(recipient);
	}
	
	public void setSender(Person sender) {
		this.sender = sender;
	}
	
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
}

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

import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.backend.domain.Person;

public abstract class AbstractMessage extends AbstractHibernateObject implements Message {
	private static final long serialVersionUID = 1L;
	
	private Date createDate;
	private String subject;
	private String body;
	private Person sender;
	private Person recipient;
	private MessageState state;
	private Date processingDate;
	
	public AbstractMessage() {
	}
	
	protected AbstractMessage(AbstractMessage m) {
		super((AbstractHibernateObject) m);
		
		if (m.body != null) {
			this.body = m.body.intern();
		}
		
		if (m.createDate != null) {
			this.createDate = new Date(m.createDate.getTime());
		}
		
		// We don't make a copy of the sender and recipient object.
		this.recipient = m.recipient;
		this.sender = m.sender;
		
		this.state = m.state;
		
		if (m.processingDate != null) {
			this.processingDate = new Date(m.processingDate.getTime());
		}
		
		if (m.subject != null) {
			this.subject = m.subject.intern();
		}
	}
	
	public String getBody() {
		return body;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public Date getProcessingDate() {
		return processingDate;
	}
	
	public Person getRecipient() {
		return recipient;
	}
	
	public Person getSender() {
		return sender;
	}
	
	public MessageState getState() {
		return state;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setProcessingDate(Date processingDate) {
		this.processingDate = processingDate;
	}

	public void setRecipient(Person recipient) {
		this.recipient = recipient;
	}

	public void setSender(Person sender) {
		this.sender = sender;
	}
	
	public void setState(MessageState state) {
		this.state = state;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}

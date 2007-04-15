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
package dk.teachus.bean.impl;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;

import dk.teachus.bean.MailBean;
import dk.teachus.bean.VelocityBean;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;

/**
 * Mail implementation which doesn't send mails.
 */
public class DummyMailBean implements MailBean {
	private static final long serialVersionUID = 1L;
	
	public DummyMailBean(JavaMailSender mailSender, VelocityBean velocityBean) {
	}

	public void sendNewBookingsMail(Teacher teacher, List<PupilBooking> pupilBookings) {
	}

	public void sendWelcomeMail(Pupil pupil, String introMessage, String serverName) {
	}

}

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
package dk.teachus.frontend;

import javax.servlet.http.Cookie;

import org.apache.wicket.Application;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.settings.ISecuritySettings;
import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.ICryptFactory;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.InfoPage;
import dk.teachus.frontend.pages.UnAuthenticatedBasePage;

public class TeachUsAuthentication extends SimplePageAuthorizationStrategy {
	private static final Logger log = LoggerFactory.getLogger(TeachUsAuthentication.class);

	public TeachUsAuthentication() {
		super(AuthenticatedBasePage.class, InfoPage.class);
	}

	@Override
	protected boolean isAuthorized() {
		WebRequestCycle requestCycle = (WebRequestCycle) RequestCycle.get();
		WebRequest request = requestCycle.getWebRequest();
				
		boolean isAuthorized = TeachUsSession.get().isAuthenticated();
		
		if (isAuthorized == false) {
			String username = null;
			String password = null;
			
			Cookie[] cookies = request.getCookies();

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (UnAuthenticatedBasePage.USERNAME_PATH.equals(cookie.getName())) {
						username = cookie.getValue();
					}
					if (UnAuthenticatedBasePage.PASSWORD_PATH.equals(cookie.getName())) {
						password = cookie.getValue();
						
						ISecuritySettings securitySettings = Application.get().getSecuritySettings();
						ICryptFactory cryptFactory = securitySettings.getCryptFactory();
						ICrypt crypt = cryptFactory.newCrypt();
						password = crypt.decryptUrlSafe(password);
					}
				}
			}
			
			if (Strings.isEmpty(username) == false) {
				log.info("Found username in cookie: "+username);
				
				TeachUsSession.get().signIn(username, password);
				
				isAuthorized = TeachUsSession.get().isAuthenticated();
			} else {
				log.info("Didn't find a username in cookie");
			}
		}
		
		return isAuthorized;
	}

}

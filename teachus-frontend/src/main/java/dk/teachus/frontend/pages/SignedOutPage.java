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
package dk.teachus.frontend.pages;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import dk.teachus.frontend.TeachUsSession;

public class SignedOutPage extends SystemBasePage {
	private static final long serialVersionUID = 1L;

	public SignedOutPage() {
		TeachUsSession.get().signOut();
		
		WebComponent refresh = new WebComponent("refresh"); //$NON-NLS-1$
		StringBuilder content = new StringBuilder();
		content.append("1; url="); //$NON-NLS-1$
		content.append(getRequestCycle().urlFor(Application.get().getHomePage(), null));
		refresh.add(AttributeModifier.replace("content", content)); //$NON-NLS-1$
		add(refresh);
		
		add(new Label("signedOutText", TeachUsSession.get().getString("SignedOutPage.youAreNowLoggedOutOfTheSystem"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		Link<Void> homePageLink= new BookmarkablePageLink<Void>("homePageLink", Application.get().getHomePage()); //$NON-NLS-1$
		add(homePageLink);
		homePageLink.add(new Label("homePageLabel", TeachUsSession.get().getString("SignedOutPage.clickToGoToFrontPage"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}

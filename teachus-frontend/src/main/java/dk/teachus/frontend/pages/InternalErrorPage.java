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

import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class InternalErrorPage extends SystemBasePage {
	private static final long serialVersionUID = 1L;

	public InternalErrorPage() {
		add(new Label("header", TeachUsSession.get().getString("InternalErrorPage.header")));
		
		add(new MultiLineLabel("errorDescription", TeachUsSession.get().getString("InternalErrorPage.discription")));
		
		Link link = new BookmarkablePageLink("link", TeachUsApplication.get().getHomePage());
		add(link);
		link.add(new Label("label", TeachUsSession.get().getString("InternalErrorPage.homePageLink")));
	}
	
}

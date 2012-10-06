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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.RepeatingView;

import dk.teachus.frontend.TeachUsSession;

public class InfoPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public InfoPage() {		
		add(new MultiLineLabel("intro1", TeachUsSession.get().getString("InfoPage.intro1"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		createFeatures();
		
		createOpenSourceDescription();
	}

	private void createOpenSourceDescription() {
		add(new Label("openSourceHeader", TeachUsSession.get().getString("InfoPage.openSourceHeader"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		List<String> openSource = new ArrayList<String>();
		
		openSource.add(TeachUsSession.get().getString("InfoPage.openSource1")); //$NON-NLS-1$
		openSource.add(TeachUsSession.get().getString("InfoPage.openSource2")); //$NON-NLS-1$
		
		RepeatingView openSourceContainer = new RepeatingView("openSource"); //$NON-NLS-1$
		add(openSourceContainer);
		
		for (String feature : openSource) {
			openSourceContainer.add(new MultiLineLabel(openSourceContainer.newChildId(), feature));
		}
		
		add(new Label("sourceCode", TeachUsSession.get().getString("InfoPage.sourceCode"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void createFeatures() {
		add(new Label("featuresHeader", TeachUsSession.get().getString("InfoPage.features"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		List<String> features = new ArrayList<String>();
		
		features.add(TeachUsSession.get().getString("InfoPage.feature1")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature2")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature3")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature4")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature5")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature6")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature7")); //$NON-NLS-1$
		features.add(TeachUsSession.get().getString("InfoPage.feature8")); //$NON-NLS-1$
		
		RepeatingView featuresContainer = new RepeatingView("features"); //$NON-NLS-1$
		add(featuresContainer);
		
		for (String feature : features) {
			featuresContainer.add(new MultiLineLabel(featuresContainer.newChildId(), feature));
		}
	}

	@Override
	protected String getPagePath() {
		return "/info";
	}

	@Override
	protected UnAuthenticatedPageCategory getPageCategory() {
		return UnAuthenticatedPageCategory.INFO;
	}
}

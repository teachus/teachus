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

import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.repeater.RepeatingView;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.imagebox.ImageBox;
import dk.teachus.frontend.components.imagebox.ImageBox.ImageResource;
import dk.teachus.frontend.utils.Resources;

public class InfoPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public InfoPage() {		
		add(new MultiLineLabel("intro1", TeachUsSession.get().getString("InfoPage.intro1")));
		
		createFeatures();
		
		createScreenShots();
		
		createOpenSourceDescription();
	}

	private void createOpenSourceDescription() {
		add(new Label("openSourceHeader", TeachUsSession.get().getString("InfoPage.openSourceHeader")));
		
		List<String> openSource = new ArrayList<String>();
		
		openSource.add(TeachUsSession.get().getString("InfoPage.openSource1"));
		openSource.add(TeachUsSession.get().getString("InfoPage.openSource2"));
		
		RepeatingView openSourceContainer = new RepeatingView("openSource");
		add(openSourceContainer);
		
		for (String feature : openSource) {
			openSourceContainer.add(new MultiLineLabel(openSourceContainer.newChildId(), feature));
		}
	}

	private void createScreenShots() {
		add(new Label("screenshotsHeader", TeachUsSession.get().getString("InfoPage.screenshots")));

		List<ImageResource> images = new ArrayList<ImageResource>();
		
		images.add(new ImageResource(Resources.SCREENSHOT_11, Resources.SCREENSHOT_11_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_2, Resources.SCREENSHOT_2_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_13, Resources.SCREENSHOT_13_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_4, Resources.SCREENSHOT_4_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_10, Resources.SCREENSHOT_10_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_14, Resources.SCREENSHOT_14_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_12, Resources.SCREENSHOT_12_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_7, Resources.SCREENSHOT_7_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_8, Resources.SCREENSHOT_8_THUMB));
		images.add(new ImageResource(Resources.SCREENSHOT_9, Resources.SCREENSHOT_9_THUMB));
		
		add(new ImageBox("screenshots", images));
	}

	private void createFeatures() {
		add(new Label("featuresHeader", TeachUsSession.get().getString("InfoPage.features")));
		
		List<String> features = new ArrayList<String>();
		
		features.add(TeachUsSession.get().getString("InfoPage.feature1"));
		features.add(TeachUsSession.get().getString("InfoPage.feature2"));
		features.add(TeachUsSession.get().getString("InfoPage.feature3"));
		features.add(TeachUsSession.get().getString("InfoPage.feature4"));
		features.add(TeachUsSession.get().getString("InfoPage.feature5"));
		features.add(TeachUsSession.get().getString("InfoPage.feature6"));
		features.add(TeachUsSession.get().getString("InfoPage.feature7"));
		features.add(TeachUsSession.get().getString("InfoPage.feature8"));
		
		RepeatingView featuresContainer = new RepeatingView("features");
		add(featuresContainer);
		
		for (String feature : features) {
			featuresContainer.add(new MultiLineLabel(featuresContainer.newChildId(), feature));
		}
	}
	
	@Override
	protected UnAuthenticatedPageCategory getPageCategory() {
		return UnAuthenticatedPageCategory.INFO;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.info");
	}

}

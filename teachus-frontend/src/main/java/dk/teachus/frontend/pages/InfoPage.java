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
import dk.teachus.frontend.components.fancybox.FancyBox;
import dk.teachus.frontend.components.fancybox.FancyBox.ImageResource;
import dk.teachus.frontend.utils.Resources;

public class InfoPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public InfoPage() {		
		add(new MultiLineLabel("intro1", TeachUsSession.get().getString("InfoPage.intro1"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		createFeatures();
		
		createScreenShots();
		
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
	}

	private void createScreenShots() {
		add(new Label("screenshotsHeader", TeachUsSession.get().getString("InfoPage.screenshots"))); //$NON-NLS-1$ //$NON-NLS-2$

		List<ImageResource> images = new ArrayList<ImageResource>();
		
		images.add(new ImageResource(Resources.SCREENSHOT_11, Resources.SCREENSHOT_11_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle1"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_2, Resources.SCREENSHOT_2_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle2"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_13, Resources.SCREENSHOT_13_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle3"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_4, Resources.SCREENSHOT_4_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle4"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_10, Resources.SCREENSHOT_10_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle5"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_14, Resources.SCREENSHOT_14_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle6"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_12, Resources.SCREENSHOT_12_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle7"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_7, Resources.SCREENSHOT_7_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle8"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_8, Resources.SCREENSHOT_8_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle9"))); //$NON-NLS-1$
		images.add(new ImageResource(Resources.SCREENSHOT_9, Resources.SCREENSHOT_9_THUMB, TeachUsSession.get().getString("InfoPage.screenshotTitle10"))); //$NON-NLS-1$
		
		add(new FancyBox("screenshots", images)); //$NON-NLS-1$
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
	protected UnAuthenticatedPageCategory getPageCategory() {
		return UnAuthenticatedPageCategory.INFO;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.info"); //$NON-NLS-1$
	}

}

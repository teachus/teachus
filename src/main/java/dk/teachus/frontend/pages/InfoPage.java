package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wicket.ResourceReference;
import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.link.PopupSettings;
import wicket.markup.repeater.RepeatingView;
import dk.teachus.frontend.TeachUsSession;
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

		Map<ResourceReference, ResourceReference> screenshots = new HashMap<ResourceReference, ResourceReference>();
		
		screenshots.put(Resources.SCREENSHOT_1_THUMB, Resources.SCREENSHOT_1);
		screenshots.put(Resources.SCREENSHOT_2_THUMB, Resources.SCREENSHOT_2);
		screenshots.put(Resources.SCREENSHOT_3_THUMB, Resources.SCREENSHOT_3);
		screenshots.put(Resources.SCREENSHOT_4_THUMB, Resources.SCREENSHOT_4);
		screenshots.put(Resources.SCREENSHOT_5_THUMB, Resources.SCREENSHOT_5);
		screenshots.put(Resources.SCREENSHOT_6_THUMB, Resources.SCREENSHOT_6);
		screenshots.put(Resources.SCREENSHOT_7_THUMB, Resources.SCREENSHOT_7);
		screenshots.put(Resources.SCREENSHOT_8_THUMB, Resources.SCREENSHOT_8);
		screenshots.put(Resources.SCREENSHOT_9_THUMB, Resources.SCREENSHOT_9);
		
		RepeatingView screenshotsContainer = new RepeatingView("screenshots");
		add(screenshotsContainer);
		
		for (ResourceReference reference : screenshots.keySet()) {
			CharSequence url = getRequestCycle().urlFor(screenshots.get(reference));
			
			ExternalLink link = new ExternalLink(screenshotsContainer.newChildId(), url.toString());
			link.setPopupSettings(new PopupSettings());
			screenshotsContainer.add(link);
			
			link.add(new Image("screenshot", reference));
		}
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

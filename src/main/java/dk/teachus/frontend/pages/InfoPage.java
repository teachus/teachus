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
import wicket.model.Model;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.IntegerFieldElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.utils.Resources;

public class InfoPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public InfoPage() {
		add(new MultiLineLabel("intro1", TeachUsSession.get().getString("InfoPage.intro1")));
		
		createFeatures();
		
		add(new Label("screenshotsHeader", TeachUsSession.get().getString("InfoPage.screenshots")));
		
		FormPanel form = new FormPanel("form");
		add(form);
		
		TextFieldElement textFieldElement = new TextFieldElement("Navn", new Model(""), true);
		form.addElement(textFieldElement);
		form.addElement(new IntegerFieldElement("Pris", new Model(100)));
		List<Integer> choices = new ArrayList<Integer>();
		choices.add(1);
		choices.add(2);
		choices.add(3);
		form.addElement(new DropDownElement("Choice", new Model(), choices, true));

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

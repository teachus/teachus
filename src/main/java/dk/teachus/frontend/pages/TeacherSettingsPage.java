package dk.teachus.frontend.pages;

import java.util.List;

import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.model.Model;
import wicket.model.PropertyModel;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherAttribute;
import dk.teachus.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public class TeacherSettingsPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private List<TeacherAttribute> attributes;

	public TeacherSettingsPage() {
		super(UserLevel.TEACHER, true);
		
		// Introduction mail text
		final WelcomeIntroductionTeacherAttribute attribute = getAttribute(WelcomeIntroductionTeacherAttribute.class);
		add(new Label("introductionMailText", TeachUsSession.get().getString("TeacherSettingsPage.introductionMailText")));
		
		Form form = new Form("form");
		add(form);
		
		form.add(new TextArea("introductionMailText", new PropertyModel(attribute, "value")));
		form.add(new Button("save", new Model(TeachUsSession.get().getString("General.save"))) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				TeachUsApplication.get().getPersonDAO().saveAttribute(attribute);
				getRequestCycle().setResponsePage(TeacherSettingsPage.class);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private <A extends TeacherAttribute> A getAttribute(Class<A> attributeClass) {
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		if (attributes == null) {
			attributes = TeachUsApplication.get().getPersonDAO().getAttributes(teacher);
		}
		
		A attribute = null;
		
		for (TeacherAttribute att : attributes) {
			if (attributeClass.isInstance(att)) {
				attribute = (A) att;
			}
		}
		
		if (attribute == null) {
			try {
				attribute = attributeClass.newInstance();
				attribute.setTeacher(teacher);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return attribute;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.settings");
	}

}

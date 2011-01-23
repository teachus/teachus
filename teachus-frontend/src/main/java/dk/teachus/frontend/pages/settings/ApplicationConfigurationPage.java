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
package dk.teachus.frontend.pages.settings;

import java.util.TimeZone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.models.ApplicationConfigurationModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public class ApplicationConfigurationPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private Label saveLabel;
	
	public ApplicationConfigurationPage() {
		super(UserLevel.ADMIN);
		
		saveLabel = new Label("saveLabel", ""); //$NON-NLS-1$ //$NON-NLS-2$
		saveLabel.setOutputMarkupId(true);
		add(saveLabel);
		
		FormPanel form = new FormPanel("settingsForm"); //$NON-NLS-1$
		add(form);
		
		form.addElement(new TextFieldElement(TeachUsSession.get().getString("ApplicationConfigurationPage.serverUrl"), new ApplicationConfigurationModel(ApplicationConfiguration.SERVER_URL), true, 40)); //$NON-NLS-1$
		
		form.addElement(new TextFieldElement(TeachUsSession.get().getString("ApplicationConfigurationPage.googleAnalyticsWebPropertyId"), new ApplicationConfigurationModel(ApplicationConfiguration.GOOGLE_ANALYTICS_WEB_PROPERTY_ID), false, 15)); //$NON-NLS-1$
		
		IModel timeZoneModel = new ApplicationConfigurationModel(ApplicationConfiguration.DEFAULT_TIMEZONE) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Object convertConfigurationValue(String configurationValue) {
				return Strings.isEmpty(configurationValue) == false ? TimeZone.getTimeZone(configurationValue) : null;
			}
			
			@Override
			protected String convertModelValue(Object modelValue) {
				return modelValue instanceof TimeZone ? ((TimeZone) modelValue).getID() : null;
			}
		};
		form.addElement(DropDownElement.createTimeZoneElement(TeachUsSession.get().getString("General.timeZone"), timeZoneModel, false)); //$NON-NLS-1$
		
		form.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel() {
				setResponsePage(ApplicationConfigurationPage.class);
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				saveLabel.setModelObject(TeachUsSession.get().getString("ApplicationConfigurationPage.settingsSaved")); //$NON-NLS-1$
				target.addComponent(saveLabel);
			}			
		});
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.GLOBAL_CONFIGURATION;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.globalConfiguration"); //$NON-NLS-1$
	}

}

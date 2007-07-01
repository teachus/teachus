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
package dk.teachus.frontend.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.joda.time.DateTime;

import dk.teachus.frontend.TeachUsSession;

public abstract class GeneratePasswordElement extends FormElement {
	
	public GeneratePasswordElement() {
		this(""); //$NON-NLS-1$
	}
	
	public GeneratePasswordElement(String label) {
		this(label, "password"); //$NON-NLS-1$
	}
	
	public GeneratePasswordElement(String label, final String seed) {
		add(new Label("label", label).setRenderBodyOnly(true)); //$NON-NLS-1$
		
		AjaxLink generatePasswordLink = new AjaxLink("generatePasswordLink") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				String password = seed;
				
				if (password == null) {
					password = ""; //$NON-NLS-1$
				}
				
				password += new DateTime().getMillisOfSecond();
				
				passwordGenerated(target, password);
			}			
		};
		add(generatePasswordLink);
		
		generatePasswordLink.add(new Label("generatePasswordLabel", TeachUsSession.get().getString("GeneratePasswordElement.generate")).setRenderBodyOnly(true)); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	protected abstract void passwordGenerated(AjaxRequestTarget target, String password);
}

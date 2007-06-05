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
package dk.teachus.frontend.components;

import wicket.Component;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;
import wicket.util.string.Strings;

public class RenderingLabel extends Label {
	private static final long serialVersionUID = 1L;

	private RenderingModel renderingModel;
	private IChoiceRenderer renderer;
	
	public RenderingLabel(String id, IChoiceRenderer renderer) {
		super(id);
		this.renderer = renderer;
	}
	
	public RenderingLabel(String id, IModel model, IChoiceRenderer renderer) {
		super(id);
		renderingModel = new RenderingModel(model);
		this.renderer = renderer;
	}
	
	@Override
	protected IModel initModel() {
		if (renderingModel == null) {
			IModel nestedModel = super.initModel();
			renderingModel = new RenderingModel(nestedModel);
		}
		
		return renderingModel;
	}
	
	private class RenderingModel extends AbstractReadOnlyModel {
		private static final long serialVersionUID = 1L;

		private IModel nestedModel;
		
		private RenderingModel(IModel nestedModel) {
			this.nestedModel = nestedModel;
		}

		@Override
		public Object getObject(Component component) {
			Object displayValue;
			if (renderer != null) {
				displayValue = renderer.getDisplayValue(nestedModel.getObject(component));
			} else {
				displayValue = nestedModel.getObject(component);
			}
			
			if (displayValue == null || Strings.isEmpty(displayValue.toString())) {
				displayValue = "&nbsp;";
				component.setEscapeModelStrings(false);
			}
			
			return displayValue;
		}
		
	}

}

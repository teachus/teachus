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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

public class RenderingLabel<T> extends Label {
	private static final long serialVersionUID = 1L;

	private RenderingModel renderingModel;
	
	public RenderingLabel(String id, IModel<T> model, IChoiceRenderer<T> renderer) {
		super(id);
		setDefaultModel(new RenderingModel(model, renderer));
	}
	
	@Override
	protected IModel<String> initModel() {
		return renderingModel;
	}
	
	private class RenderingModel extends AbstractReadOnlyModel<String> {
		private static final long serialVersionUID = 1L;

		private IModel<T> nestedModel;
		private IChoiceRenderer<T> renderer;
		
		private RenderingModel(IModel<T> nestedModel, IChoiceRenderer<T> renderer) {
			this.nestedModel = nestedModel;
			this.renderer = renderer;
		}

		@Override
		public String getObject() {
			Object displayValue;
			if (renderer != null) {
				displayValue = renderer.getDisplayValue(nestedModel.getObject());
			} else {
				displayValue = nestedModel.getObject();
			}
			
			if (displayValue == null || Strings.isEmpty(displayValue.toString())) {
				displayValue = "&nbsp;";
				setEscapeModelStrings(false);
			}
			
			return String.valueOf(displayValue);
		}
		
	}

}

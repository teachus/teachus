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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.JavascriptUtils;
import org.apache.wicket.util.string.Strings;

public class ConfirmClickBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;
	private final String confirmScript;

	public ConfirmClickBehavior(String confirmText) {
		if (Strings.isEmpty(confirmText) == false) {
			StringBuilder b = new StringBuilder();
			b.append("return confirm('");
			b.append(JavascriptUtils.escapeQuotes(confirmText));
			b.append("');");
			
			this.confirmScript = b.toString();
		} else {
			this.confirmScript = null;
		}
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		tag.addBehavior(new AttributeModifier("onclick", true, new Model(confirmScript)));
	}
	
	@Override
	public boolean isEnabled(Component component) {
		return Strings.isEmpty(confirmScript) == false;
	}
}

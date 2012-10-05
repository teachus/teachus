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
package dk.teachus.frontend.components.list;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public abstract class DefaultFunctionItem<T> implements FunctionItem<T> {
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	public DefaultFunctionItem() {
	}

	public DefaultFunctionItem(String title) {
		this.title = title;
	}

	public abstract void onEvent(T object);
	
	public abstract Component createLabelComponent(String wicketId, T object);
	
	public Component createComponent(String wicketId, IModel<T> rowModel) {
		return new DefaultFunctionPanel<T>(wicketId, this, rowModel);
	}
	
	public String getClickConfirmText(T object) {
		return null;
	}
	
	public boolean isEnabled(T object) {
		return true;
	}
	
	public String getTitle(T rowObject) {
		return title;
	}

	public void modifyLink(Link<T> link) {
		// Do nothing per default
	}
	
}
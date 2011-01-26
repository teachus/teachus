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
package dk.teachus.frontend.components.jquery;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;

public class JQueryBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_JQUERY = new JavascriptResourceReference(JQueryBehavior.class, "jquery-1.4.4.min.js"); //$NON-NLS-1$
	
	@Override
	public final void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JS_JQUERY);
		
		onRenderHead(response);
	}
	
	public void onRenderHead(IHeaderResponse response) {
	}
	
}

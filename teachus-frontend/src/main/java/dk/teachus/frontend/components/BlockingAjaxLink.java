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

import wicket.ajax.AbstractDefaultAjaxBehavior;
import wicket.ajax.IAjaxCallDecorator;
import wicket.ajax.calldecorator.AjaxCallDecorator;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.IHeaderResponse;
import dk.teachus.frontend.components.jquery.JQueryBehavior;

public abstract class BlockingAjaxLink extends AjaxLink {

	public BlockingAjaxLink(String id) {
		super(id);
		
		add(new JQueryBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onRenderHead(IHeaderResponse response) {
				StringBuilder b = new StringBuilder();
				
				b.append("function blockOnClick(elm) {");
				b.append("var e = $(elm);");
				b.append("var id = e.attr('id');");
				b.append("var img = $('<img>').attr('id', id).attr('style', 'margin-top: 1px').attr('src', '");
				b.append(getRequestCycle().urlFor(AbstractDefaultAjaxBehavior.INDICATOR));
				b.append("');");
				b.append("e.after(img).remove();");
				b.append("}");
				
				response.renderJavascript(b, "blockOnClick");
			}
		});
		
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxCallDecorator() {
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence decorateScript(CharSequence script) {
				StringBuilder b = new StringBuilder();
				
				b.append("blockOnClick(this);");
				b.append(script);
				
				return b;
			}
		};
	}
}

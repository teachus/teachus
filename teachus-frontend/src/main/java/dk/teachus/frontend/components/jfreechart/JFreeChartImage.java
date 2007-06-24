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
package dk.teachus.frontend.components.jfreechart;

import wicket.ajax.AbstractDefaultAjaxBehavior;
import wicket.markup.ComponentTag;
import wicket.markup.html.image.NonCachingImage;

public class JFreeChartImage extends NonCachingImage {
	private static final long serialVersionUID = 1L;

	private JFreeChartResource jFreeChartResource;
	
	public JFreeChartImage(String id, JFreeChartResource jFreeChartResource) {
		super(id, jFreeChartResource);
		this.jFreeChartResource = jFreeChartResource;
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		tag.put("width", jFreeChartResource.getWidth()); //$NON-NLS-1$
		tag.put("height", jFreeChartResource.getHeight()); //$NON-NLS-1$
		
		StringBuilder style = new StringBuilder();
		style.append("background: url('");
		style.append(getRequestCycle().urlFor(AbstractDefaultAjaxBehavior.INDICATOR));
		style.append("') no-repeat center center");
		tag.put("style", style);
	}

}

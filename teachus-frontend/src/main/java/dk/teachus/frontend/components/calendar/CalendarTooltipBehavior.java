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
package dk.teachus.frontend.components.calendar;

import org.apache.wicket.markup.html.IHeaderResponse;

import dk.teachus.frontend.components.jquery.cluetip.AbstractJQueryCluetipBehavior;

public class CalendarTooltipBehavior extends AbstractJQueryCluetipBehavior {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);
		
		StringBuilder tipConf = new StringBuilder();
		tipConf.append("$('.daytimelesson[rel]').cluetip({");
		tipConf.append("	local: true, ");
		tipConf.append("	showTitle: false, ");
		tipConf.append("	positionBy: 'auto', ");
		tipConf.append("	width: 300, ");
		tipConf.append("	cluetipClass: 'calendar', ");
		tipConf.append("	arrows: true");
		tipConf.append("})");
		
		response.renderOnDomReadyJavascript(tipConf.toString());
	}
}

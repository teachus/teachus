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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import dk.teachus.frontend.TeachUsSession;

public abstract class JFreeChartResource extends RenderedDynamicImageResource {
	private static final long serialVersionUID = 1L;

	public JFreeChartResource(int width, int height) {
		super(width, height);
	}

	@Override
	protected boolean render(Graphics2D graphics) {		
		Plot plot = getPlot();
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(JFreeChart.DEFAULT_BACKGROUND_PAINT);
		plot.setNoDataMessage(TeachUsSession.get().getString("Stats.noData"));
		
		JFreeChart chart = new JFreeChart(null, null, plot, getCreateLegend());
		chart.setBackgroundPaint(Color.WHITE);
		
		chart.draw(graphics, new Rectangle2D.Double(0, 0, getWidth(), getHeight()));		
		
		return true;
	}

	protected abstract Plot getPlot();
	
	protected boolean getCreateLegend() {
		return false;
	}
	
}

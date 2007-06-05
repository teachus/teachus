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

import java.awt.Paint;
import java.text.NumberFormat;

import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.CategoryDataset;

import dk.teachus.frontend.utils.Formatters;

public class BarChartResource extends JFreeChartResource {
	private static final long serialVersionUID = 1L;
	
	private CategoryDataset dataset;
	
	private String xLabel;
	
	private String yLabel;

	public BarChartResource(int width, int height, CategoryDataset dateset) {
		this(width, height, dateset, null, null);
	}
	
	public BarChartResource(int width, int height, CategoryDataset dateset, String xLabel, String yLabel) {
		super(width, height);
		
		this.dataset = dateset;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	@Override
	protected Plot getPlot() {
		CategoryAxis3D categoryAxis = new CategoryAxis3D(xLabel);
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		StackedBarRenderer3D barRenderer3D = new StackedBarRenderer3D(8, 8) {
			private static final long serialVersionUID = 1L;

			@Override
			public Paint getSeriesPaint(int series) {
				Paint thePaint = null;
				
				if (dataset instanceof PaintedDefaultCategoryDataset) {
					PaintedDefaultCategoryDataset paintedDefaultCategoryDataset = (PaintedDefaultCategoryDataset) dataset;
					
					thePaint = paintedDefaultCategoryDataset.getPaint(series);
				}
				
				if (thePaint == null) {
					thePaint = super.getSeriesPaint(series);
				}
				
				return thePaint;
			}
		};
		
		NumberAxis3D yAxis = new NumberAxis3D(yLabel);
		yAxis.setNumberFormatOverride(getYAxisNumberFormat());
		
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, yAxis, barRenderer3D);
		
		return plot;
	}
	
	protected NumberFormat getYAxisNumberFormat() {
		return Formatters.getFormatCurrency();
	}

}

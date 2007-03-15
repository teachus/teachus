package dk.teachus.frontend.components.jfreechart;

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
	
	private CategoryDataset dateset;
	
	private String xLabel;
	
	private String yLabel;

	public BarChartResource(int width, int height, CategoryDataset dateset) {
		this(width, height, dateset, null, null);
	}
	
	public BarChartResource(int width, int height, CategoryDataset dateset, String xLabel, String yLabel) {
		super(width, height);
		
		this.dateset = dateset;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	@Override
	protected Plot getPlot() {
		CategoryAxis3D categoryAxis = new CategoryAxis3D(xLabel);
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		StackedBarRenderer3D barRenderer3D = new StackedBarRenderer3D(8, 8);
		
		NumberAxis3D yAxis = new NumberAxis3D(yLabel);
		yAxis.setNumberFormatOverride(Formatters.getFormatCurrency());
		
		CategoryPlot plot = new CategoryPlot(dateset, categoryAxis, yAxis, barRenderer3D);
		
		return plot;
	}

}

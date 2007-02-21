package dk.frankbille.teachus.frontend.resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import wicket.markup.html.image.resource.RenderedDynamicImageResource;

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
		
		JFreeChart chart = new JFreeChart(plot);
		chart.setBackgroundPaint(Color.WHITE);
		
		chart.draw(graphics, new Rectangle2D.Double(0, 0, getWidth(), getHeight()));		
		
		return true;
	}

	protected abstract Plot getPlot();
	
}

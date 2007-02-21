package dk.frankbille.teachus.frontend.resources;

import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.PieDataset;

public class PieChartResource extends JFreeChartResource {
	private static final long serialVersionUID = 1L;

	private PieDataset pieDataset;
	
	public PieChartResource(int width, int height, PieDataset pieDataset) {
		super(width, height);
		this.pieDataset = pieDataset;
	}

	@Override
	protected Plot getPlot() {
		return new PiePlot3D(pieDataset);
	}

}

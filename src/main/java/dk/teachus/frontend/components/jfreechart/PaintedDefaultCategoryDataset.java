package dk.teachus.frontend.components.jfreechart;

import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

public class PaintedDefaultCategoryDataset extends DefaultCategoryDataset {
	private static final long serialVersionUID = 1L;

	private Map<Comparable, Paint> paints = new HashMap<Comparable, Paint>();

	public void addValue(double value, Comparable rowKey, Comparable columnKey, Paint paint) {
		super.addValue(value, rowKey, columnKey);
		
		paints.put(rowKey, paint);
	}

	public void addValue(Number value, Comparable rowKey, Comparable columnKey, Paint paint) {
		super.addValue(value, rowKey, columnKey);
		
		paints.put(rowKey, paint);
	}

	public Paint getPaint(int row) {
		return paints.get(getRowKey(row));
	}

	public Paint getPaint(Comparable rowKey) {
		return paints.get(rowKey);
	}
}

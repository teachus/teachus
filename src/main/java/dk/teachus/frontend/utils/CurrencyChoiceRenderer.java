package dk.teachus.frontend.utils;

import wicket.markup.html.form.ChoiceRenderer;

public class CurrencyChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = ""; //$NON-NLS-1$
		
		if (object != null) {
			if (object instanceof Double) {
				Double price = (Double) object;
				display = Formatters.getFormatCurrency().format(price);
			}
		}
		
		return display;
	}
}

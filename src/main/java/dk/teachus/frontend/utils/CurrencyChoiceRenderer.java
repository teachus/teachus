package dk.teachus.frontend.utils;

import java.util.Currency;

import wicket.Session;
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
			} else if (object instanceof Currency) {
				Currency currency = (Currency) object;
				display = currency.getSymbol(Session.get().getLocale());
			}
		}
		
		return display;
	}
}

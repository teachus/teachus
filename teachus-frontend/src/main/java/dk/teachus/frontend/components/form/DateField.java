package dk.teachus.frontend.components.form;

import java.util.Calendar;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.joda.time.ReadableInstant;

import dk.teachus.frontend.TeachUsSession;

public class DateField<D extends ReadableInstant> extends TextField<D> {
	private static final long serialVersionUID = 1L;

	public DateField(String id, IModel<D> dateModel) {
		super(id, dateModel);
		add(new InputMaskBehavior("9999-99-99"));
		add(AttributeModifier.replace("rel", "datepicker"));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		StringBuilder b = new StringBuilder();
		b.append("$(\"*[rel=datepicker]\").datepicker({");
		b.append("format: 'yyyy-mm-dd',");
		Locale locale = TeachUsSession.get().getLocale();
		Calendar calendar = Calendar.getInstance(locale);
		int firstDayOfWeek = calendar.getFirstDayOfWeek()-1;
		b.append("weekStart: "+firstDayOfWeek);
		b.append("});");
		response.renderOnDomReadyJavaScript(b.toString());
	}
	
}

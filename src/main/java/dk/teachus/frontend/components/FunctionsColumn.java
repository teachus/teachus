package dk.teachus.frontend.components;

import java.util.List;

import wicket.behavior.SimpleAttributeModifier;
import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.Item;
import wicket.markup.repeater.RepeatingView;
import wicket.model.IModel;
import wicket.util.string.JavascriptUtils;
import wicket.util.string.Strings;
import dk.teachus.domain.Person;
import dk.teachus.frontend.pages.persons.PersonsPage;

public class FunctionsColumn<P extends Person> extends AbstractColumn {
	private static final long serialVersionUID = 1L;

	private List<PersonsPage<P>.FunctionItem> functions;
	
	public FunctionsColumn(IModel displayModel, List<PersonsPage<P>.FunctionItem> functions) {
		super(displayModel);
		this.functions = functions;
	}

	@SuppressWarnings("unchecked")
	public void populateItem(Item cellItem, String componentId, IModel rowModel) {
		final P person = (P) rowModel.getObject(cellItem);
		
		cellItem.add(new SimpleAttributeModifier("class", "functions"));
		
		LinkPropertyColumnPanel linkPropertyColumnPanel = new LinkPropertyColumnPanel(componentId);
		linkPropertyColumnPanel.setRenderBodyOnly(true);
		cellItem.add(linkPropertyColumnPanel);
		
		RepeatingView links = new RepeatingView("link");
		linkPropertyColumnPanel.add(links);
		
		for (final PersonsPage<P>.FunctionItem function : functions) {
			Link link = new Link(links.newChildId()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					function.onEvent(person);
				}
			};
			
			String clickConfirmText = function.getClickConfirmText(person);
			if (Strings.isEmpty(clickConfirmText) == false) {
				StringBuilder confirmJavascript = new StringBuilder();
				confirmJavascript.append("return confirm('");
				confirmJavascript.append(JavascriptUtils.escapeQuotes(clickConfirmText));
				confirmJavascript.append("');");
				SimpleAttributeModifier onClickModifier = new SimpleAttributeModifier("onclick", confirmJavascript.toString());
				link.add(onClickModifier);
			}
			
			link.add(new Label("label", function.getLabel()).setRenderBodyOnly(true));
			
			links.add(link);
		}
	}

}

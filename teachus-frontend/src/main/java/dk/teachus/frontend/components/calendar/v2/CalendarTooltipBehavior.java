package dk.teachus.frontend.components.calendar.v2;

import org.apache.wicket.markup.html.IHeaderResponse;

import dk.teachus.frontend.components.jquery.cluetip.AbstractJQueryCluetipBehavior;

public class CalendarTooltipBehavior extends AbstractJQueryCluetipBehavior {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);
		
		StringBuilder tipConf = new StringBuilder();
		tipConf.append("$('.daytimelesson[rel]').cluetip({");
		tipConf.append("	local: true, ");
		tipConf.append("	showTitle: false, ");
		tipConf.append("	positionBy: 'auto', ");
		tipConf.append("	width: 300, ");
		tipConf.append("	cluetipClass: 'calendar', ");
		tipConf.append("	arrows: true");
		tipConf.append("})");
		
		response.renderOnDomReadyJavascript(tipConf.toString());
	}
}

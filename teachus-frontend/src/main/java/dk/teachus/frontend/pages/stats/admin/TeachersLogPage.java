package dk.teachus.frontend.pages.stats.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.frontend.TeachUsSession;

public class TeachersLogPage extends AbstractAdminStatisticsPage {
	private static final long serialVersionUID = 1L;
	
	public TeachersLogPage() {
		final IModel dateEntriesModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				List<Entry> entries = new ArrayList<Entry>();
				
				List<LogProvider> providers = new ArrayList<LogProvider>();
				providers.add(new BookingLogProvider());
				
				TeachUsDate now = TeachUsSession.get().createNewDate(new DateMidnight());
				TeachUsDate fromDate = now.minusMonths(1);
				TeachUsDate toDate = now;
				
				for (LogProvider logProvider : providers) {
					logProvider.appendEntries(entries, fromDate, toDate);
				}
				
				return entries;
			}
		};
		
		IModel dateModel = new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public Object getObject() {
				List<DateMidnight> dates = new ArrayList<DateMidnight>();
				
				List<Entry> entries = (List<Entry>) dateEntriesModel.getObject();
				for (Entry entry : entries) {
					if (dates.contains(entry.getDate()) == false) {
						dates.add(entry.getDate());
					}
				}
				
				Collections.sort(dates);
				Collections.reverse(dates);
				
				return dates;
			}
		};
		
		add(new ListView("dates", dateModel) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(ListItem item) {
				final DateMidnight date = (DateMidnight) item.getModelObject();
				
				DateTimeFormatter formatter = DateTimeFormat.forPattern(TeachUsSession.get().getString("TeachersLogPage.dateHeader"));
				formatter = formatter.withLocale(TeachUsSession.get().getLocale());
				item.add(new Label("date", formatter.print(date)));
				
				IModel entriesModel =  new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						List<Entry> filteredEntries = new ArrayList<Entry>();
						
						List<Entry> entries = (List<Entry>) dateEntriesModel.getObject();
						for (Entry entry : entries) {
							if (entry.getDate().equals(date)) {
								filteredEntries.add(entry);
							}
						}
						
						return filteredEntries;
					}
				};
				
				item.add(new ListView("entries", entriesModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem item) {
						Entry entry = (Entry) item.getModelObject();
						
						item.add(new Label("text", entry.getText()));
					}
				});
			}
		});
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.teachersLog");
	}
	
}

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

import dk.teachus.frontend.TeachUsSession;

public class TeachersLogPage extends AbstractAdminStatisticsPage {
	private static final long serialVersionUID = 1L;
	
	public TeachersLogPage() {
		final IModel<List<Entry>> dateEntriesModel = new LoadableDetachableModel<List<Entry>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Entry> load() {
				List<Entry> entries = new ArrayList<Entry>();
				
				List<LogProvider> providers = new ArrayList<LogProvider>();
				providers.add(new BookingLogProvider());
				
				DateMidnight now = new DateMidnight();
				DateMidnight fromDate = now.minusMonths(1);
				DateMidnight toDate = now;
				
				for (LogProvider logProvider : providers) {
					logProvider.appendEntries(entries, fromDate, toDate);
				}
				
				return entries;
			}
		};
		
		IModel<List<DateMidnight>> dateModel = new AbstractReadOnlyModel<List<DateMidnight>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<DateMidnight> getObject() {
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
		
		add(new ListView<DateMidnight>("dates", dateModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DateMidnight> item) {
				final DateMidnight date = item.getModelObject();
				
				DateTimeFormatter formatter = DateTimeFormat.forPattern(TeachUsSession.get().getString("TeachersLogPage.dateHeader"));
				formatter = formatter.withLocale(TeachUsSession.get().getLocale());
				item.add(new Label("date", formatter.print(date)));
				
				IModel<List<Entry>> entriesModel =  new AbstractReadOnlyModel<List<Entry>>() {
					private static final long serialVersionUID = 1L;

					@Override
					public List<Entry> getObject() {
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
				
				item.add(new ListView<Entry>("entries", entriesModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<Entry> item) {
						Entry entry = item.getModelObject();
						
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

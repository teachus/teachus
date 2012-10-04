package dk.teachus.frontend.pages.messages;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;

import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.impl.AbstractMessage;
import dk.teachus.backend.domain.impl.PersonImpl;
import dk.teachus.frontend.components.list.DateFilter;
import dk.teachus.frontend.components.list.DateTimeComparator;
import dk.teachus.frontend.components.list.MessageStateComparator;
import dk.teachus.frontend.components.list.SameObjectFilter;
import dk.teachus.frontend.components.list.StringComparator;
import dk.teachus.frontend.components.list.StringFilter;
import dk.teachus.frontend.components.list.TeachUsFilteredSortableDataProvider;

public class SentMessagesDataProvider extends TeachUsFilteredSortableDataProvider<Message> {
	private static final long serialVersionUID = 1L;

	public SentMessagesDataProvider(IModel<List<Message>> listModel) {
		super(listModel);
		
		StringComparator stringComparator = new StringComparator();
		addComparator("recipient.name", stringComparator); //$NON-NLS-1$
		addComparator("processingDate", new DateTimeComparator()); //$NON-NLS-1$
		addComparator("state", new MessageStateComparator()); //$NON-NLS-1$
		addComparator("subject", stringComparator); //$NON-NLS-1$
		
		setSort("processingDate", SortOrder.DESCENDING); //$NON-NLS-1$
		
		addFilter("recipient.name", new StringFilter());
		addFilter("state", new SameObjectFilter());
		addFilter("subject", new StringFilter());
		addFilter("processingDate", new DateFilter());
	}

	@Override
	protected Message createStateObject() {
		AbstractMessage message = new AbstractMessage() {
			private static final long serialVersionUID = 1L;

			public Message copy() {
				return this;
			}
		};
		message.setRecipient(new PersonImpl() {
			private static final long serialVersionUID = 1L;
		});
		return message;
	}
	
}

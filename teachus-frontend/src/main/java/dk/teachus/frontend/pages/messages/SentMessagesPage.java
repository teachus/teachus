package dk.teachus.frontend.pages.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.DateMidnight;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.backend.domain.Person;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.Toolbar.BookmarkableToolbarItem;
import dk.teachus.frontend.components.Toolbar.ToolbarItemInterface;
import dk.teachus.frontend.components.list.LinkPropertyColumn;
import dk.teachus.frontend.components.list.ListPanel;
import dk.teachus.frontend.components.list.RendererPropertyColumn;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.DateChoiceRenderer;
import dk.teachus.frontend.utils.MessageStateChoiceRenderer;

public class SentMessagesPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public SentMessagesPage() {
		super(UserLevel.TEACHER, true);
		
		List<ToolbarItemInterface> items = new ArrayList<ToolbarItemInterface>();
		items.add(new BookmarkableToolbarItem(TeachUsSession.get().getString("Messages.createMessage"), CreateMessagePage.class)); //$NON-NLS-1$
		
		add(new Toolbar("toolbar", items)); //$NON-NLS-1$
		
		final IModel messagesModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				Person person = TeachUsSession.get().getPerson();
				
				MessageDAO messageDAO = TeachUsApplication.get().getMessageDAO();
				return messageDAO.getMessages(person);
			}
		};
		
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new LinkPropertyColumn(new Model(TeachUsSession.get().getString("Messages.subject")), "subject", "subject") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(Object rowModelObject) {
					Message message = (Message) rowModelObject;
					getRequestCycle().setResponsePage(new EditMessagePage(message));
				}
				
				@Override
				public Component getFilter(String componentId, FilterForm form) {
					return new TextFilter(componentId, new PropertyModel(form.getModel(), "subject"), form);
				}
			});
		columns.add(createRecipientColumn(messagesModel));
		columns.add(createProcessingDateColumn(messagesModel));
		columns.add(createMessageStateColumn(messagesModel));
		
		SentMessagesDataProvider dataProvider = new SentMessagesDataProvider(messagesModel);
		
		add(new ListPanel("sentMessages", columns, dataProvider, dataProvider)); //$NON-NLS-1$
	}

	private RendererPropertyColumn createMessageStateColumn(final IModel messagesModel) {
		return new RendererPropertyColumn(new Model(TeachUsSession.get().getString("Messages.state")), "state", "state", new MessageStateChoiceRenderer()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getFilter(String componentId, FilterForm form) {
				IModel statesModel = new LoadableDetachableModel() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					@Override
					protected Object load() {
						List<Message> messages = (List<Message>) messagesModel.getObject();
						Set<MessageState> states = new HashSet<MessageState>();
						
						for (Message message : messages) {
							states.add(message.getState());
						}
						
						List<MessageState> statesList = new ArrayList<MessageState>(states);
						Collections.sort(statesList);
						return statesList;
					}
				};
				
				return new ChoiceFilter(componentId, new PropertyModel(form.getModel(), "state"), form, statesModel, new MessageStateChoiceRenderer(), true);
			}
		};
	}

	private IColumn createRecipientColumn(final IModel messagesModel) {
		return new FilteredPropertyColumn(new Model(TeachUsSession.get().getString("Messages.recipient")), "recipient.name", "recipient.name") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			private static final long serialVersionUID = 1L;

			public Component getFilter(String componentId, FilterForm form) {
				IModel recipientsModel = new LoadableDetachableModel() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					@Override
					protected Object load() {
						List<Message> messages = (List<Message>) messagesModel.getObject();
						Set<String> recipients = new HashSet<String>();
						
						for (Message message : messages) {
							recipients.add(message.getRecipient().getName());
						}
						
						List<String> recipientsList = new ArrayList<String>(recipients);
						Collections.sort(recipientsList);
						return recipientsList;
					}
				};
				
				return new ChoiceFilter(componentId, new PropertyModel(form.getModel(), "recipient.name"), form, recipientsModel, true);
			}
		};
	}

	private IColumn createProcessingDateColumn(final IModel messagesModel) {		
		return new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.date")), "processingDate", "processingDate", new DateChoiceRenderer()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm form) {
				IModel datesModel = new LoadableDetachableModel() {
					private static final long serialVersionUID = 1L;
					
					@SuppressWarnings("unchecked")
					@Override
					protected Object load() {
						List<Message> messages = (List<Message>) messagesModel.getObject();
						Set<Date> dates = new HashSet<Date>();
						for (Message message : messages) {
							dates.add(new DateMidnight(message.getProcessingDate()).toDate());
						}
						List<Date> datesList = new ArrayList<Date>(dates);
						Collections.sort(datesList);
						return datesList;
					}			
				};
				
				return new ChoiceFilter(componentId, new PropertyModel(form.getModel(), "processingDate"), form, datesModel, true);
			}
		};
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.MESSAGES;
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.messages"); //$NON-NLS-1$
	}
	
}

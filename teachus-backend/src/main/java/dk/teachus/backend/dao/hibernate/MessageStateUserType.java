package dk.teachus.backend.dao.hibernate;

import dk.teachus.backend.domain.MessageState;

public class MessageStateUserType extends EnumUserType<MessageState> {

	public MessageStateUserType() {
		super(MessageState.class);
	}
	
}

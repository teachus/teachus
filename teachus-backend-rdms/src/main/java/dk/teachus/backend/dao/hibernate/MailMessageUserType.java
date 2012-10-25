package dk.teachus.backend.dao.hibernate;

import dk.teachus.backend.domain.impl.MailMessage.Type;

public class MailMessageUserType extends EnumUserType<dk.teachus.backend.domain.impl.MailMessage.Type> {
	
	public MailMessageUserType() {
		super(Type.class);
	}
	
}

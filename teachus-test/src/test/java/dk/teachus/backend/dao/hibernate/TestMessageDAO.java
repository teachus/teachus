package dk.teachus.backend.dao.hibernate;

import java.util.List;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.backend.domain.impl.MailMessage.Type;
import dk.teachus.backend.test.SpringTestCase;

public class TestMessageDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;
	
	public void testSaveMailMessage() {
		MailMessage mail = new MailMessage();
		mail.setSubject("Test subject");
		mail.setBody("Test body");
		mail.setSender(getPersonDAO().getPerson(4L));
		mail.addRecipient(getPersonDAO().getPerson(5L));
		
		MessageDAO messageDAO = getMessageDAO();
		messageDAO.save(mail);
		
		List<Message> messages = messageDAO.getMessages(getPersonDAO().getPerson(4L));
		assertEquals(1, messages.size());
		
		Message message = messages.get(0);
		
		assertTrue(message instanceof MailMessage);
		MailMessage checkMail = (MailMessage) message;
		
		assertEquals(mail.getSubject(), checkMail.getSubject());
		assertEquals(Type.PLAIN, checkMail.getType());
		
		assertEquals(1, checkMail.getRecipients().size());
		Person recipient = checkMail.getRecipients().iterator().next();
		assertEquals(new Long(5), recipient.getId());
		
	}
	
}

package dk.teachus.backend;

import junit.framework.TestCase;

public class TestMailException extends TestCase {
	
	public void testConstructors() {
		new MailException();
		
		new MailException("Some message");
		
		new MailException(new RuntimeException());
		
		new MailException("Some message", new RuntimeException());
	}
	
}

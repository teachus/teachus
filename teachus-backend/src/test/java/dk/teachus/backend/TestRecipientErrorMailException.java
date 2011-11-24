package dk.teachus.backend;

import junit.framework.TestCase;

public class TestRecipientErrorMailException extends TestCase {
	
	public void testConstructors() {
		new RecipientErrorMailException();
		
		new RecipientErrorMailException("Some message");
		
		new RecipientErrorMailException(new RuntimeException());
		
		new RecipientErrorMailException("Some message", new RuntimeException());
	}
	
}

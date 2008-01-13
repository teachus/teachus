package dk.teachus.backend;

public class RecipientErrorMailException extends MailException {
	private static final long serialVersionUID = 1L;

	public RecipientErrorMailException() {
	}
	
	public RecipientErrorMailException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RecipientErrorMailException(String message) {
		super(message);
	}
	
	public RecipientErrorMailException(Throwable cause) {
		super(cause);
	}
	
}

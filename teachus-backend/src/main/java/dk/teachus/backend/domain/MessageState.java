package dk.teachus.backend.domain;

public enum MessageState {
	/**
	 * The message is still being edited and shouldn't be used in any contexts besides editing
	 */
	DRAFT,
	/**
	 * The message is ready to be sent
	 */
	FINAL,
	/**
	 * The message has been sent. The {@link Message#getProcessingDate()} has been updated with the sending date.
	 */
	SENT,
	/**
	 * The message couldn't be sent. The reason is that the recipient is invalid. The
	 * {@link Message#getProcessingDate()} has been updated with the date the message has been tried to be sent.
	 */
	ERROR_SENDING_INVALID_RECIPIENT,
	/**
	 * The message couldn't be sent. The reason is unknown and further investigation is needed. The
	 * {@link Message#getProcessingDate()} has been updated with the date the message has been tried to be sent.
	 */
	ERROR_SENDING_UNKNOWN
}

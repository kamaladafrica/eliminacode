package it.kamaladafrica.eliminacode.service;

public class ExpiredTagException extends InvalidTagException {

	private static final long serialVersionUID = 1L;

	public ExpiredTagException() {
		super("Unable to create QRCode");
	}

	public ExpiredTagException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpiredTagException(String message) {
		super(message);
	}

	public ExpiredTagException(Throwable cause) {
		super(cause);
	}

}

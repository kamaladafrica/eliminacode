package it.kamaladafrica.eliminacode.service;

public class InvalidTagException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTagException() {
		super("Unable to create QRCode");
	}

	public InvalidTagException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTagException(String message) {
		super(message);
	}

	public InvalidTagException(Throwable cause) {
		super(cause);
	}

}

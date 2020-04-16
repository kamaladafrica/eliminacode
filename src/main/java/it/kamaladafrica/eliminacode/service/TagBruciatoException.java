package it.kamaladafrica.eliminacode.service;

public class TagBruciatoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TagBruciatoException() {
		super("Unable to create QRCode");
	}

	public TagBruciatoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TagBruciatoException(String message) {
		super(message);
	}

	public TagBruciatoException(Throwable cause) {
		super(cause);
	}

}

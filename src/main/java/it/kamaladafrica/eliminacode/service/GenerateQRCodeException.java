package it.kamaladafrica.eliminacode.service;

public class GenerateQRCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GenerateQRCodeException() {
		super("Unable to create QRCode");
	}

	public GenerateQRCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenerateQRCodeException(String message) {
		super(message);
	}

	public GenerateQRCodeException(Throwable cause) {
		super(cause);
	}

}

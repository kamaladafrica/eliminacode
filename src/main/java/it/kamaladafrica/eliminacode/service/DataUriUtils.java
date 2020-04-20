package it.kamaladafrica.eliminacode.service;

import javax.xml.bind.DatatypeConverter;

public class DataUriUtils {

	private DataUriUtils() {
	}

	public static String toDataURI(byte[] data, String contentType) {
		String content = DatatypeConverter.printBase64Binary(data);
		StringBuilder sb = new StringBuilder();
		sb.append("data:");
		sb.append(contentType);
		sb.append(";base64,");
		sb.append(content);
		return sb.toString();
	}

}

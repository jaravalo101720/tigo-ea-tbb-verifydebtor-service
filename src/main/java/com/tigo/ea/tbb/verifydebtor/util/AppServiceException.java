package com.tigo.ea.tbb.verifydebtor.util;

public class AppServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String code;

	public AppServiceException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
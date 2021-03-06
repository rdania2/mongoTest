package com.blazemeter.demo.exception;

public class GradeNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String msg;

	public GradeNotFoundException(String message) {
		super(message);
		this.msg = message;
	}

	public String getMsg() {
		return this.msg;
	}

}

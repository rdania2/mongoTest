package com.blazemeter.demo.exception;

public class CourseNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String msg;

	public CourseNotFoundException(String message) {
		super(message);
		this.msg = message;
	}

	public String getMsg() {
		return this.msg;
	}

}

package com.chockolate.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -5566944608394995655L;

	private Exception except;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(String msg, Exception e) {
		super(msg);
		except = e;
	}

	public Exception getExcept() {
		return except;
	}
}

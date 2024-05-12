package com.jammeraal.sffoodtruck.exceptions;

/**
 * The purpose of this Exception is to wrap a bunch of exceptions into a
 * RuntimeException.<br/>
 * <br/>
 * The reason for a separate class from RuntimeException is that when the
 * generic exception handler deals with this, it will log the stack trace of the
 * original exception. This is a big deal as to not obfuscate the actual
 * exception.
 */
public class UnableToLoadDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Exception originalException;

	public UnableToLoadDataException(String msg, Exception e) {
		super(msg + ", original msg: " + e.getMessage());
		this.originalException = e;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return originalException.getStackTrace();
	}
}

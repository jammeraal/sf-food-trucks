package com.jammeraal.sffoodtruck.exceptions;

/**
 * Used when unable to find a specific truck
 */
public class TruckNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TruckNotFoundException(String msg) {
		super(msg);
	}
}

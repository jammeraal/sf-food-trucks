package com.jammeraal.sffoodtruck.exceptions;

/**
 * Currently only support data load once. When the data load can be done
 * multiple times this Exception will be able to be removed
 */
public class DataAlreadyLoadedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}

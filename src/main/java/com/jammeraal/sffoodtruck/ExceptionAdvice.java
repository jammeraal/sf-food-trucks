package com.jammeraal.sffoodtruck;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jammeraal.sffoodtruck.exceptions.DataAlreadyLoadedException;
import com.jammeraal.sffoodtruck.exceptions.TruckNotFoundException;

/**
 * This currently handles all the exceptions for the entire application. This is
 * great for a small app but likely we would want to have smaller handlers in
 * specific places.
 */
@ControllerAdvice
public class ExceptionAdvice {
	private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

	/**
	 * Handler for dealing with not found in database. NoSuchElementException is a
	 * catch all where as TruckNotFoundException is more specific. The more specific
	 * version allows a better error message in the logs for debugging
	 * 
	 * @param ex
	 */
	@ResponseBody
	@ExceptionHandler({ NoSuchElementException.class, TruckNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void notFoundHandler(RuntimeException ex) {
		// This is a debug because people will search for stuff that isn't in the data
		// set and that is okay.
		// a logging tool like datadog could be used to alert if we are seeing an
		// abnormally high amount of these calls
		log.debug("Not found error message: " + ex.getMessage(), ex);
	}

	@ResponseBody
	@ExceptionHandler({ DataAlreadyLoadedException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String alreadyLoaded(DataAlreadyLoadedException ex) {
		log.info("Unable to load data more than once");
		return "Unable to load data as it has already been loaded";
	}

	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String runtimeHandler(RuntimeException ex) {
		log.error("Something went wrong: " + ex.getMessage(), ex);
		return "Error in service. People have been notified and will be looking into it. Please try again later";
	}

}

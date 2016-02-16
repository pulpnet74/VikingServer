/**
 * com.lodbrok.exception package contains the custom exceptions
 */
package com.lodbrok.exception;

/**
 * InvalidBodyException is thrown if a HTTP body doesn't contains expected info
 *
 * @author Fabio Riberto
 */
public class InvalidBodyException extends Exception {

	private static final long serialVersionUID = 9007507076091049745L;

	/**
	 * InvalidBodyException constructor
	 */
	public InvalidBodyException() {
		super();
	}

	/**
	 * InvalidBodyException constructor with parameters
	 *
	 * @param info
	 */
	public InvalidBodyException(String info) {
		super(info);
	}
}
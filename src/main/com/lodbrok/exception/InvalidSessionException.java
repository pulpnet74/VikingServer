/**
 * com.lodbrok.exception package contains the custom exceptions
 */
package com.lodbrok.exception;

/**
 * InvalidSessionException is thrown if a sent session key is not valid
 * 
 * @author Fabio Riberto
 *
 */
public class InvalidSessionException extends Exception {

	private static final long serialVersionUID = 3878622774041009478L;

	/**
	 * InvalidSessionException constructor
	 *
	 */
	public InvalidSessionException() {
		super();
	}

	/**
	 * InvalidSessionException constructor with parameters
	 *
	 * @param info
	 */
	public InvalidSessionException(String info) {
		super(info);
	}
}

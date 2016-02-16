/**
 * com.lodbrok.exception package contains the custom exceptions
 */
package com.lodbrok.exception;

/**
 * InvalidRequestException is thrown if a URI doesn't contain a valid request
 *
 * @author Fabio Riberto
 */
public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = -7021413062975115615L;

	/**
	 * InvalidRequestException constructor
	 */
	public InvalidRequestException() {
		super();
	}

	/**
	 * InvalidRequestException constructor with parameters
	 * 
	 * @param info
	 */
	public InvalidRequestException(String info) {
		super(info);
	}
}
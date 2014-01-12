/**
 * The MIT License
 * Copyright (c) 2013 Pierre-Denis Vanduynslager
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.syncthemall.diffbot.exception;

/**
 * Wraps any non-200 HTTP responses from an API call. You'll typically only want to handle a few specific error codes
 * and show some kind of generic error or retry for the rest.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class DiffbotServerException extends DiffbotException {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 7905062136219550521L;
	private final int errorCode;

	/**
	 * Constructs a new exception with the specified error code and detail message. The cause is not initialized, and
	 * may subsequently be initialized by a call to {@link #initCause}.
	 * 
	 * @param errorCode HTTP error code
	 * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
	 *            method.
	 */
	public DiffbotServerException(final int errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @return the HTTP error associated with the exception
	 */
	public final int getErrorCode() {
		return errorCode;
	}

}

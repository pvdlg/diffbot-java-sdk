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
 * Represents an internal error in the Diffbot API (on Diffbot servers). For example:
 * <ul>
 * <li>The url of the article to extract is not accessible by Diffbot servers</li>
 * <li>The url of the article to extract doesn't point to an article page and cannot be processed</li>
 * </ul>
 * 
 * @see <a href="http://www.diffbot.com/our-apis/article">Diffbot Aticle API documentation</a>
 * @see <a href="http://www.diffbot.com/our-apis/frontpage">Diffbot Fontpage API documentation</a>
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class DiffbotAPIException extends DiffbotException {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -5083459104543875149L;
	private final int errorCode;

	/**
	 * Constructs a new exception with the specified error code, detail message and cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
	 * exception's detail message.
	 * 
	 * @param errorCode Diffbot API error code
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
	 *            value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public DiffbotAPIException(final int errorCode, final String message, final Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * Constructs a new exception with the specified error code and detail message. The cause is not initialized, and
	 * may subsequently be initialized by a call to {@link #initCause}.
	 * 
	 * @param errorCode Diffbot API eror code
	 * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
	 *            method.
	 */
	public DiffbotAPIException(final int errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @return the Diffbot API eror code
	 */
	public final int getErrorCode() {
		return errorCode;
	}

}

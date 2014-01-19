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
 * Wraps any {@code DiffbotAPIException} for a Batch API call. It's used to differentiate error in the batch request
 * itself from the error of a sub-request.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class DiffbotBatchException extends DiffbotException {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 3732579759373158746L;

	/**
	 * Constructs a new exception with the specified cause.
	 * 
	 * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
	 *            value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public DiffbotBatchException(final Throwable cause) {
		super(cause);
	}

}

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
/**
 * 
 */
package com.syncthemall.diffbot;

/**
 * Constant class.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Constants {

	/** User agent. Written in the header of the generated HTML. */
	public static final String USER_AGENT = "diffbot-java-sdk/1.1.0";

	/** Batch API URL */
	public static final String BATCH_URL = "http://www.diffbot.com/api/batch";

	/** HTTP return code for OK (200) */
	public static final int HTTP_OK = 200;

	/** HTTP return code for Unauthorized (401) */
	public static final int HTTP_UNAUTHORIZED = 401;

	/** HTTP method GET */
	public static final String GET = "GET";

	/** The attribute {@code batch}. */
	public static final String BATCH = "batch";

	/** The attribute {@code "application/json"}. */
	public static final String APPLICATION_JSON = "application/json";

	/** The attribute {@code Content-Type}. */
	public static final String CONTENT_TYPE = "Content-Type";

	/** The attribute {@code statusCode}. */
	public static final String STATUS_CODE = "statusCode";

	/** The attribute {@code message}. */
	public static final String MESSAGE = "message";

	/** The attribute {@code errorCode}. */
	public static final String ERROR_CODE = "errorCode";

	/** The attribute {@code error}. */
	public static final String ERROR = "error";

	private Constants() {
		super();
	}

}

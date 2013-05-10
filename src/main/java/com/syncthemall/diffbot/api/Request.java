/**
 * Copyright (c) 2013 Pierre-Denis Vanduynslager, https://github.com/vanduynslagerp
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.syncthemall.diffbot.api;

import java.io.Serializable;
import java.net.URISyntaxException;

import com.google.api.client.http.GenericUrl;

/**
 * Parent class used to creates request to the Diffbot API.
 */
public abstract class Request extends GenericUrl implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -398029585831057600L;

	/**
	 * Diffbot API type.
	 */
	public enum ApiType {
		/** Article API. **/
		ARTICLE,
		/** Frontpage API. **/
		FRONTPAGE
	};

	/**
	 * @return the type of API used by this request.
	 */
	public abstract ApiType getApiType();

	/**
	 * Constructs from an encoded URL.
	 * 
	 * <p>
	 * Any known query parameters with pre-defined fields as data keys will be parsed based on their data type. Any
	 * unrecognized query parameter will always be parsed as a string.
	 * </p>
	 * 
	 * <p>
	 * Any {@link URISyntaxException} is wrapped in an {@link IllegalArgumentException}.
	 * </p>
	 * 
	 * @param encodedUrl encoded URL, including any existing query parameters that should be parsed
	 * @throws IllegalArgumentException if URL has a syntax error
	 */
	public Request(final String encodedUrl) {
		super(encodedUrl);
	}

}

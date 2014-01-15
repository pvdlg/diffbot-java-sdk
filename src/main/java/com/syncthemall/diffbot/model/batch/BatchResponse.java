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
package com.syncthemall.diffbot.model.batch;

import java.io.Serializable;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Sub-response of a batch request.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class BatchResponse extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -675826711458122919L;

	@Key
	private String body;
	@Key
	private Header[] headers;
	@Key
	private int code;
	@Key(value = "relative_url")
	private String relativeUrl;

	/**
	 * Gets the first header with the given name.
	 * 
	 * <p>
	 * Header name comparison is case insensitive.
	 * 
	 * @param name the name of the header to get
	 * @return the first header or <code>null</code>
	 */
	public Header getFirstHeader(final String name) {
		for (int i = 0; i < headers.length; i++) {
			final Header header = headers[i];
			if (header.getName() != null && header.getName().equalsIgnoreCase(name)) {
				return header;
			}
		}
		return null;
	}

	/**
	 * @return the {@code Header}s for the this {@code BatchResponse}
	 */
	public Header[] getHeaders() {
		if (headers != null) {
		return headers.clone();
		}
		return new Header[0];
	}

	/**
	 * @return the body content of the response
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return HTTP response code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the relative URL of the {@code BatchRequest} to witch this {@code BatchResponse} corresponds
	 */
	public String getRelativeUrl() {
		return relativeUrl;
	}

}

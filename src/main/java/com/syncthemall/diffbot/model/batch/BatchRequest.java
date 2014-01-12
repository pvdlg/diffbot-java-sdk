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

import com.google.api.client.util.Key;

/**
 * Sub-request of a batch request. Used to serialize the subrequest to JSON.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class BatchRequest implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -3087506508790686402L;

	@Key
	private String method;

	@Key(value = "relative_url")
	private String relativeUrl;

	/**
	 * @param method method used to request the Diffbot API for this sub-request. Either 'GET' or 'POST'.
	 * @param relativeUrl the relative url of the Diffbot API for this sub-request, i.e. '/api/article?....'.
	 */
	public BatchRequest(final String method, final String relativeUrl) {
		super();
		this.method = method;
		this.relativeUrl = relativeUrl;
	}

	@Override
	public String toString() {
		return "BatchRequest - relative_url:" + relativeUrl;
	}

}

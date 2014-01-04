/**
 * The MIT License
 * Copyright (c) ${year} Pierre-Denis Vanduynslager
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
package com.syncthemall.diffbot.api;

import com.syncthemall.diffbot.model.frontpage.Frontpage;

/**
 * Allows to create a request to the Diffbot Frontpage API.
 */
public class FrontpageRequest extends Request {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -7762898255387814976L;

	/**
	 * Creates a {@code Request} to extract the elements of a front page.
	 * 
	 * @param url URL to extract the {@link Frontpage} from.
	 */
	public FrontpageRequest(final String url) {
		super(DiffbotAPI.API_SERVER + DiffbotAPI.FRONTPAGE_URI);
		set("url", url);
		set("format", "xml");
	}

	@Override
	public final ApiType getApiType() {
		return ApiType.FRONTPAGE;
	}

}
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
package com.syncthemall.diffbot.model;

import java.io.Serializable;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.syncthemall.diffbot.model.article.Article;

/**
 * Meta information extracted from an {@link Article} by Diffbot (Article API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Meta extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -3043119813174203709L;
	
	@Key
	private String viewport;
	@Key(value = "application-name")
	private String applicationName;
	@Key
	private String title;
	@Key
	private OpenGraph og;
	@Key
	private Twitter twitter;
	
	/**
	 * @return the viewport value meta tag
	 */
	public String getViewport() {
		return viewport;
	}
	
	/**
	 * @return the application-name value meta tag
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @return the title value meta tag
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the Open Graph set of meta tags
	 * @see <a href="http://ogp.me">OpenGraph tags</a>
	 */
	public OpenGraph getOg() {
		return og;
	}

	/**
	 * @return the Twitter set of meta tags
	 * @see <a href="https://dev.twitter.com/docs/cards/markup-reference">Twitter Card metadata</a>
	 */
	public Twitter getTwitter() {
		return twitter;
	}

	@Override
	public String toString() {
		return "Meta - " + super.toString();
	}

}

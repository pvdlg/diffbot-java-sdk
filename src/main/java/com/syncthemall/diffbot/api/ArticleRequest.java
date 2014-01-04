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

import com.syncthemall.diffbot.model.article.Article;

/**
 * Allows to create a request to the Diffbot Article API.
 */
public class ArticleRequest extends Request {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -6036271145970514649L;

	/**
	 * Creates a {@code Request} to extract an article of a given URL.
	 * 
	 * @param url URL to extract the {@link Article} from.
	 */
	public ArticleRequest(final String url) {
		super(DiffbotAPI.API_SERVER + DiffbotAPI.ARTICLE_URI);
		set("url", url);
	}

	/**
	 * Extract html instead of plain text.
	 * 
	 * @return the instance of this request
	 */
	public final ArticleRequest asHtml() {
		set("html", "");
		return this;
	}

	/**
	 * Don't strip any inline ads.
	 * 
	 * @return the instance of this request
	 */
	public final ArticleRequest dontStripAds() {
		set("dontStripAds", "");
		return this;
	}

	/**
	 * Generate tags for the extracted story.
	 * 
	 * @return the instance of this request
	 */
	public final ArticleRequest withTags() {
		set("tags", "");
		return this;
	}

	/**
	 * Find the comments and identify count (experimental).
	 * 
	 * @return the instance of this request
	 */
	public final ArticleRequest withComments() {
		set("comments", "");
		return this;
	}

	/**
	 * Extract a summary text.
	 * 
	 * @return the instance of this request
	 */
	public final ArticleRequest withSummary() {
		set("summary", "");
		return this;
	}
	
	/**
	 * Overrides the default API timeout of 5000ms.
	 * @param timeout The timeout in milliseconds.
	 * 
	 * @return the instance of this request
	 */
	public final ArticleRequest withTimeout(final long timeout) {
		set("timeout", String.valueOf(timeout));
		return this;
	}

	@Override
	public final ApiType getApiType() {
		return ApiType.ARTICLE;
	}

}
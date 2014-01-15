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
package com.syncthemall.diffbot.model.products;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.syncthemall.diffbot.Diffbot.Products.Analyze;
import com.syncthemall.diffbot.model.Meta;
import com.syncthemall.diffbot.model.Model;
import com.syncthemall.diffbot.model.PageType;

/**
 * The result of a products extraction by Diffbot (Product API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Products extends Model implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 6871009685641061551L;

	@Key
	private String url;
	@Key(value = "resolved_url")
	private String resolvedUrl;
	@Key
	private Meta meta;
	@Key
	private List<String> links;
	@Key
	private List<String> breadcrumb;
	@Key
	private List<Product> products;
	@Key
	private PageType type;
	@Key
	private GenericJson querystring;

	@Override
	public PageType getType() {
		return type;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return String.format("Products [url=%s]", getUrl());
	}

	/**
	 * @return the resolving URL if it is different from the submitted URL (e.g., link shortening services)
	 */
	public String getResolvedUrl() {
		return resolvedUrl;
	}

	/**
	 * @return all links (anchor tag href values) found on the page (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public List<String> getLinks() {
		return links;
	}

	/**
	 * @return the full contents of page meta tags, including sub-arrays for OpenGraph tags, Twitter Card metadata,
	 *         schema.org microdata, and -- if available -- oEmbed metadata (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 * @see <a href="https://dev.twitter.com/docs/cards/markup-reference">Twitter Card metadata</a>
	 * @see <a href="http://ogp.me">OpenGraph tags</a>
	 * @see <a href="http://www.oembed.com">oEmbed metadata</a>
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * @return if available, an array of link URLs and link text from page breadcrumbs
	 */
	public List<String> getBreadcrumb() {
		return breadcrumb;
	}

	/**
	 * @return an array of products contained on the page
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * @return returns the key/value pairs of the URL querystring, if present. Items without a value will be returned as
	 *         "true." (returned if referenced in {@link Analyze#withFields(String)})
	 */
	public GenericJson getQuerystring() {
		return querystring;
	}

}
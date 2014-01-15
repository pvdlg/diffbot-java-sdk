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
package com.syncthemall.diffbot.model.classifier;

import java.io.IOException;
import java.io.Serializable;

import com.google.api.client.util.Key;
import com.syncthemall.diffbot.Diffbot.Article.Analyze;
import com.syncthemall.diffbot.exception.DiffbotParseException;
import com.syncthemall.diffbot.model.Model;
import com.syncthemall.diffbot.model.PageType;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.images.Images;
import com.syncthemall.diffbot.model.products.Products;

/**
 * The result of a classifier extraction by Diffbot (Clasifier API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Classified extends Model implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 7502076031673451491L;

	@Key
	private String title;
	@Key(value = "resolved_url")
	private String resolvedUrl;
	@Key
	private String url;
	@Key(value = "human_language")
	private String humanLanguage;
	@Key
	private PageType type;
	@Key
	private Stats stats;

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
		return String.format("Classified [url=%s]", url);
	}
	
	/**
	 * The Page Classifier API will fully extract pages that match an existing <a
	 * href="http://www.diffbot.com/products/automatic">Diffbot Automatic API</a>.
	 * 
	 * This method will parse and return this {@code Classified} object as an {@code Article}. This method is relevant
	 * to use if {@code this#getType()#equals(PageType#ARTICLE))}
	 * 
	 * @return this {@code Classified} object as an {@code Article}
	 * @throws DiffbotParseException if there is an error during the parsing
	 */
	public Article asArticle() throws DiffbotParseException {
		try {
			return this.getFactory().createJsonParser(super.toString()).parse(Article.class);
		} catch (IOException e) {
			throw new DiffbotParseException("The classified object cannot be parsed as an Article.", e);
		}
	}

	/**
	 * The Page Classifier API will fully extract pages that match an existing <a
	 * href="http://www.diffbot.com/products/automatic">Diffbot Automatic API</a>.
	 * 
	 * This method will parse and return this {@code Classified} object as an {@code Images}. This method is relevant
	 * to use if {@code this#getType()#equals(PageType#IMAGE))}
	 * 
	 * @return this {@code Classified} object as an {@code Images}
	 * @throws DiffbotParseException if there is an error during the parsing
	 */
	public Images asImages() throws DiffbotParseException {
		try {
			return this.getFactory().createJsonParser(super.toString()).parse(Images.class);
		} catch (IOException e) {
			throw new DiffbotParseException("The classified object cannot be parsed as an Images.", e);
		}
	}

	/**
	 * The Page Classifier API will fully extract pages that match an existing <a
	 * href="http://www.diffbot.com/products/automatic">Diffbot Automatic API</a>.
	 * 
	 * This method will parse and return this {@code Classified} object as an {@code Products}. This method is relevant
	 * to use if {@code this#getType()#equals(PageType#PRODUCT))}
	 * 
	 * @return this {@code Classified} object as an {@code Products}
	 * @throws DiffbotParseException if there is an error during the parsing
	 */
	public Products asProducts() throws DiffbotParseException {
		try {
			return this.getFactory().createJsonParser(super.toString()).parse(Products.class);
		} catch (IOException e) {
			throw new DiffbotParseException("The classified object cannot be parsed as a Products.", e);
		}
	}

	/**
	 * @return the resolving URL if it is different from the submitted URL (e.g., link shortening services)
	 */
	public String getResolvedUrl() {
		return resolvedUrl;
	}

	/**
	 * @return the page title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the (spoken/human) language of the submitted URL, using two-letter ISO 639-1 nomenclature (returned if
	 *         referenced in {@link Analyze#withFields(String)})
	 * @see <a href="http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 nomenclature</a>
	 */
	public String getHumanLanguage() {
		return humanLanguage;
	}

	/**
	 * @return statistics on page classification and extraction, including an array of individual page-types and the
	 *         Diffbot-determined score (likelihood) for each type.
	 */
	public Stats getStats() {
		return stats;
	}

}
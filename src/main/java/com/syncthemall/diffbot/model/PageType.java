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
package com.syncthemall.diffbot.model;

import com.google.api.client.util.Value;

/**
 * Page-type of the submitted URL.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public enum PageType {
	/** A news article, blog post or other primarily-text page */
	@Value("article")
	ARTICLE("article"),
	/** A music or audio player */
	@Value("audio")
	AUDIO("audio"),
	/** A graph or chart, typically financial */
	@Value("chart")
	CHART("chart"),
	/** Specific forum, group or discussion topic */
	@Value("discussion")
	DISCUSSION("discussion"),
	/** An embedded or downloadable document or slideshow */
	@Value("document")
	DOCUMENT("document"),
	/** A downloadable file */
	@Value("download")
	DOWNLOAD("download"),
	/** Error page, e.g. 404 */
	@Value("error")
	ERROR("error"),
	/** A page detailing specific event information, e.g. time/date/location */
	@Value("event")
	EVENT("event"),
	/** A page of multiple frequently asked questions, or a single FAQ entry */
	@Value("faq")
	FAQ("faq"),
	/** A news- or blog-style home page, with links to myriad sections and items */
	@Value("frontpage")
	FRONTPAGE("frontpage"),
	/** A playable game */
	@Value("game")
	GAME("game"),
	/** An image or photo page */
	@Value("image")
	IMAGE("image"),
	/** A job posting */
	@Value("job")
	JOB("job"),
	/** A page detailing location information, typically including an address and/or map */
	@Value("location")
	LOCATION("location"),
	/** Returned if the result is below a certain confidence threshold */
	@Value("other")
	OTHER("other"),
	/** A product page, typically of a product for purchase */
	@Value("product")
	PRODUCT("product"),
	/** A person or user profile page */
	@Value("profile")
	PROFILE("profile"),
	/** Page detailing recipe instructions and ingredients */
	@Value("recipe")
	RECIPE("recipe"),
	/** A list of user reviews */
	@Value("reviewslist")
	REVIEWLIST("reviewslist"),
	/** A Search Engine Results Page */
	@Value("serp")
	SERP("serp"),
	/** An individual video */
	@Value("video")
	VIDEO("video");

	private String key;

	PageType(final String key) {
		this.key = key;
	}

	/**
	 * @return the key of the {@code PageType}
	 */
	public String getKey() {
		return this.key;
	}

}

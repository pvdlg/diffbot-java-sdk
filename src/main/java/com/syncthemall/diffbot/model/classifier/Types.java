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

import java.io.Serializable;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Types elements extracted from a {@link Stats} by Diffbot (Classifier API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Types extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -4604447877717933892L;

	@Key
	private float recipe;
	@Key
	private float discussion;
	@Key
	private float audio;
	@Key
	private float error;
	@Key
	private float location;
	@Key
	private float faq;
	@Key
	private float image;
	@Key
	private float job;
	@Key
	private float download;
	@Key
	private float game;
	@Key
	private float product;
	@Key
	private float frontpage;
	@Key
	private float document;
	@Key
	private float article;
	@Key
	private float event;
	@Key
	private float chart;
	@Key
	private float serp;
	@Key
	private float reviewslist;
	@Key
	private float video;
	@Key
	private float profile;

	/**
	 * @return Diffbot-determined score (likelihood) of page detailing recipe instructions and ingredients
	 */
	public float getRecipe() {
		return recipe;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of specific forum, group or discussion topic
	 */
	public float getDiscussion() {
		return discussion;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a music or audio player
	 */
	public float getAudio() {
		return audio;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of an error page
	 */
	public float getError() {
		return error;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a page detailing location information, typically including an address and/or map
	 */
	public float getLocation() {
		return location;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a page of multiple frequently asked questions, or a single FAQ entry
	 */
	public float getFaq() {
		return faq;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of an image or photo page
	 */
	public float getImage() {
		return image;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a job posting
	 */
	public float getJob() {
		return job;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a downloadable file
	 */
	public float getDownload() {
		return download;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a playable game
	 */
	public float getGame() {
		return game;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a product page, typically of a product for purchase
	 */
	public float getProduct() {
		return product;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a news- or blog-style home page, with links to myriad sections and items
	 */
	public float getFrontpage() {
		return frontpage;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of an embedded or downloadable document or slideshow
	 */
	public float getDocument() {
		return document;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a news article, blog post or other primarily-text page
	 */
	public float getArticle() {
		return article;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a page detailing specific event information, e.g. time/date/location
	 */
	public float getEvent() {
		return event;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a graph or chart, typically financial
	 */
	public float getChart() {
		return chart;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a Search Engine Results Page
	 */
	public float getSerp() {
		return serp;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a list of user reviews
	 */
	public float getReviewslist() {
		return reviewslist;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of an individual video
	 */
	public float getVideo() {
		return video;
	}
	/**
	 * @return Diffbot-determined score (likelihood) of a person or user profile page
	 */
	public float getProfile() {
		return profile;
	}

	@Override
	public String toString() {
		return "Types - " + super.toString();
	}
}
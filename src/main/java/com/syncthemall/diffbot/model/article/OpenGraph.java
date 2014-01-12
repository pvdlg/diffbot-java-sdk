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
package com.syncthemall.diffbot.model.article;

import java.io.Serializable;

import com.google.api.client.util.Key;
import com.syncthemall.diffbot.model.Model;

/**
 * Open Graph {@link Meta} information extracted from an {@link Article} by Diffbot (Article API).
 * 
 * @see <a href="http://ogp.me">OpenGraph tags</a>
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class OpenGraph extends Model implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 5858650905042339573L;

	@Key(value = "og:title")
	private String title;
	@Key(value = "og:type")
	private String type;
	@Key(value = "og:image")
	private String image;
	@Key(value = "og:url")
	private String url;
	@Key(value = "og:audio")
	private String audio;
	@Key(value = "og:description")
	private String description;
	@Key(value = "og:determiner")
	private String determiner;
	@Key(value = "og:locale")
	private String locale;
	@Key(value = "og:site_name")
	private String siteName;
	@Key(value = "og:locale:alternate")
	private String localeAlternate;
	@Key(value = "og:video")
	private String video;

	/**
	 * Default constructor.
	 */
	public OpenGraph() {
		super();
	}

	/**
	 * @return the title of your object as it should appear within the graph, e.g., "The Rock"
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the type of your object, e.g., "video.movie"
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return an image URL which should represent your object within the graph
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the canonical URL of your object that will be used as its permanent ID in the graph, e.g.,
	 *         "http://www.imdb.com/title/tt0117500/"
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return a URL to an audio file to accompany this object
	 */
	public String getAudio() {
		return audio;
	}

	/**
	 * @return a one to two sentence description of your object
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return The word that appears before this object's title in a sentence. An enum of (a, an, the, "", auto). If
	 *         auto is chosen, the consumer of your data should chose between "a" or "an". Default is "" (blank)
	 */
	public String getDeterminer() {
		return determiner;
	}

	/**
	 * @return the locale these tags are marked up in. Of the format language_TERRITORY. Default is en_US
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @return if your object is part of a larger web site, the name which should be displayed for the overall site. e.g., "IMDb"
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @return an array of other locales this page is available in
	 */
	public String getLocaleAlternate() {
		return localeAlternate;
	}

	/**
	 * @return a URL to a video file that complements this object
	 */
	public String getVideo() {
		return video;
	}

	@Override
	public String toString() {
		return "OpenGraph - " + super.toString();
	}

}

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
import java.util.List;

import com.google.api.client.util.Key;
import com.syncthemall.diffbot.Diffbot.Articles.Get;
import com.syncthemall.diffbot.model.Model;

/**
 * The result of an article extraction by Diffbot (Article API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Article extends Model implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 7531133216091403402L;
	@Key
	private String text;
	@Key
	private String title;
	@Key
	private String date;
	@Key
	private String author;
	@Key
	private List<Video> videos;
	@Key
	private List<Image> images;
	@Key
	private String url;
	@Key(value = "resolved_url")
	private String resolvedUrl;
	@Key
	private String icon;
	@Key
	private String html;
	@Key
	private String[] tags;
	@Key
	private String summary;
	@Key
	private Categories categories;
	@Key
	private String type;
	@Key
	private List<String> links;
	@Key
	private String humanLanguage;
	@Key
	private Meta meta;
	@Key
	private int numPages;

	/**
	 * Default constructor.
	 */
	public Article() {
		super();
	}

	/**
	 * @return the plain-text content of the extracted article
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the title of extracted article
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the article date (if detected)
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the article author (if detected)
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return the videos items, if detected
	 */
	public List<Video> getVideos() {
		return videos;
	}

	/**
	 * @return the images items, if detected
	 */
	public List<Image> getImages() {
		return images;
	}

	/**
	 * @return the submitted URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the resolving URL if it is different from the submitted URL (e.g., link shortening services)
	 */
	public String getResolvedUrl() {
		return resolvedUrl;
	}

	/**
	 * @return the article page favicon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the HTML of the extracted article
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @return the tags of the extracted article (returned if referenced in {@link Get#withFields(String)})
	 */
	public String[] getTags() {
		return (String[]) tags.clone();
	}

	/**
	 * @return the summary text of the extracted article
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the categories score of the extracted article (returned if referenced in {@link Get#withFields(String)})
	 */
	public Categories getCategories() {
		return categories;
	}

	/**
	 * @return the type of page -- always article
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return all links (anchor tag href values) found on the page (returned if referenced in
	 *         {@link Get#withFields(String)})
	 */
	public List<String> getLinks() {
		return links;
	}

	/**
	 * @return the (spoken/human) language of the submitted URL, using two-letter ISO 639-1 nomenclature (returned if
	 *         referenced in {@link Get#withFields(String)})
	 * @see <a href="http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 nomenclature</a>
	 */
	public String getHumanLanguage() {
		return humanLanguage;
	}

	/**
	 * @return the full contents of page meta tags, including sub-arrays for OpenGraph tags, Twitter Card metadata,
	 *         schema.org microdata, and -- if available -- oEmbed metadata (returned if referenced in
	 *         {@link Get#withFields(String)})
	 * @see <a href="https://dev.twitter.com/docs/cards/markup-reference">Twitter Card metadata</a>
	 * @see <a href="http://ogp.me">OpenGraph tags</a>
	 * @see <a href="http://www.oembed.com">oEmbed metadata</a>
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * @return number of pages automatically concatenated to form the text or html response (By default, Diffbot will
	 *         automatically concatenate multiple-page articles.)
	 */
	public int getNumPages() {
		return numPages;
	}

	@Override
	public String toString() {
		return String.format("Article [title=%s]", title);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Article)) {
			return false;
		}
		Article other = (Article) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

}
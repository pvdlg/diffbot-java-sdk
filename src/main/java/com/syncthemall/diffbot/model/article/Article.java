/**
 * Copyright (c) 2013 Pierre-Denis Vanduynslager, https://github.com/vanduynslagerp
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.syncthemall.diffbot.model.article;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * The result of an article extraction by Diffbot (Article API).
 */
public final class Article extends GenericJson implements Serializable {

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
	private List<Media> media;
	@Key
	private String url;
	@Key
	private String resolvedUrl;
	@Key
	private String xpath;
	@Key
	private String icon;
	@Key
	private String html;
	@Key
	private String[] tags;
	@Key
	private String summary;
	@Key
	private Comments comments;

	/**
	 * Default constructor.
	 */
	public Article() {
		super();
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
	 * @return the media items (images or videos), if detected and extracted
	 */
	public List<Media> getMedia() {
		return media;
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
	 * @return the XPath expression identifying the node containing the article contents
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * @return the article page favicon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the HTML of the extracted article (returned in place of text if the html parameter is used)
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @return the tags of the extracted article(returned if tags parameter is used)
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * @return the summary text of the extracted article (returned if summary parameter is used)
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the comments count of the extracted article (returned if comments parameter is used)
	 */
	public Comments getComments() {
		return comments;
	}

}
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
package com.syncthemall.diffbot.model.frontpage;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.syncthemall.diffbot.converter.DateXmlAdapter;

/**
 * Element extracted from a front page by Diffbot (Frontpage API).
 */
@XmlAccessorType(XmlAccessType.NONE)
public final class Item implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 7744306540133027033L;

	/**
	 * Type of item extracted form a page.
	 */
	public enum Type {
		/** Image type. **/
		IMAGE("IMAGE"),
		/** Link type. **/
		LINK("LINK"),
		/** Story type. **/
		STORY("STORY"),
		/** Chunk type. **/
		CHUNK("CHUNK");

		private String text;

		Type(final String text) {
			this.text = text;
		}

		/**
		 * @return the text associated with the {@code Type} as it is returned by Diffbot.
		 */
		public String getText() {
			return text;
		}
	}

	@XmlAttribute(name = "id")
	private long id;
	@XmlElement(name = "title")
	private String title;
	@XmlElement(name = "description")
	private String description;
	@XmlAttribute(name = "xroot")
	private String xroot;
	@XmlElement(name = "pubDate")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date pubDate;
	@XmlElement(name = "link")
	private String link;
	@XmlAttribute(name = "type")
	private Type type;
	@XmlAttribute(name = "img")
	private String img;
	@XmlElement(name = "textSummary")
	private String textSummary;
	@XmlAttribute(name = "sp")
	private Float spamScore;
	@XmlAttribute(name = "sr")
	private Float staticRank;
	@XmlAttribute(name = "fresh")
	private Float freshScore;

	/**
	 * Default Constructor.
	 */
	public Item() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
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
		if (!(obj instanceof Item)) {
			return false;
		}
		Item other = (Item) obj;
		if (link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!link.equals(other.link)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Item [title=%s]", title);
	}

	/**
	 * @return the unique identifier of the item (created by Diffbot)
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the title of the item
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description of the item
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the XPath expression identifying the node containing the item contents
	 */
	public String getXroot() {
		return xroot;
	}

	/**
	 * @return the publication date of the item
	 */
	public Date getPubDate() {
		return pubDate;
	}

	/**
	 * @return the link of the item
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @return the URL of the image associated to the item
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @return the summary of the item
	 */
	public String getTextSummary() {
		return textSummary;
	}

	/**
	 * @return the probability that the item is spam/ad
	 */
	public Float getSpamScore() {
		return spamScore;
	}

	/**
	 * @return the quality score of the item on a 1 to 5 scale
	 */
	public Float getStaticRank() {
		return staticRank;
	}

	/**
	 * @return the percentage of the item that has changed compared to the previous crawl
	 */
	public Float getFreshScore() {
		return freshScore;
	}

	/**
	 * @return the {@code Type} of the item
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param id the unique identifier of the item (created by Diffbot)
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @param title the title of the item
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @param description the description of the item
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @param xroot the XPath expression identifying the node containing the item contents
	 */
	public void setXroot(final String xroot) {
		this.xroot = xroot;
	}

	/**
	 * @param pubDate the publication date of the item
	 */
	public void setPubDate(final Date pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * @param link the link of the item
	 */
	public void setLink(final String link) {
		this.link = link;
	}

	/**
	 * @param type the {@code Type} of the item
	 */
	public void setType(final Type type) {
		this.type = type;
	}

	/**
	 * @param img the URL of the image associated to the item
	 */
	public void setImg(final String img) {
		this.img = img;
	}

	/**
	 * @param textSummary the summary of the item
	 */
	public void setTextSummary(final String textSummary) {
		this.textSummary = textSummary;
	}

	/**
	 * @param spamScore the probability that the item is spam/ad
	 */
	public void setSpamScore(final Float spamScore) {
		this.spamScore = spamScore;
	}

	/**
	 * @param staticRank the quality score of the item on a 1 to 5 scale
	 */
	public void setStaticRank(final Float staticRank) {
		this.staticRank = staticRank;
	}

	/**
	 * @param freshScore the percentage of the item that has changed compared to the previous crawl
	 */
	public void setFreshScore(final Float freshScore) {
		this.freshScore = freshScore;
	}

}
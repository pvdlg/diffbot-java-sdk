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
package com.syncthemall.diffbot.model.frontpage;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * The informations related to a front page extraction by Diffbot (Frontpage API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Info implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -6618447661476661893L;
	private String title;
	private String sourceURL;
	private String icon;
	private String sourceType;
	private int numItems;
	private int numSpamItems;

	/**
	 * Default constructor.
	 */
	public Info() {
		super();
	}

	@Override
	public String toString() {
		return String.format("Info [title=%s]", title);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceURL == null) ? 0 : sourceURL.hashCode());
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
		if (!(obj instanceof Info)) {
			return false;
		}
		Info other = (Info) obj;
		if (sourceURL == null) {
			if (other.sourceURL != null) {
				return false;
			}
		} else if (!sourceURL.equals(other.sourceURL)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the title of the extracted page
	 */
	@XmlElement
	public String getTitle() {
		return title;
	}

	/**
	 * @return the submitted URL
	 */
	@XmlElement
	public String getSourceURL() {
		return sourceURL;
	}

	/**
	 * @return the page favicon
	 */
	@XmlElement
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the source type of the extracted page (usually 'html')
	 */
	@XmlElement
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @return the number of items extracted
	 */
	@XmlElement
	public int getNumItems() {
		return numItems;
	}

	/**
	 * @return the number of extracted item identified as spam
	 */
	@XmlElement
	public int getNumSpamItems() {
		return numSpamItems;
	}

	/**
	 * @param title the title of the extracted page
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @param sourceURL the submitted URL
	 */
	public void setSourceURL(final String sourceURL) {
		this.sourceURL = sourceURL;
	}

	/**
	 * @param icon the page favicon
	 */
	public void setIcon(final String icon) {
		this.icon = icon;
	}

	/**
	 * @param sourceType the source type of the extracted page (usually 'html')
	 */
	public void setSourceType(final String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @param numItems the number of items extracted
	 */
	public void setNumItems(final int numItems) {
		this.numItems = numItems;
	}

	/**
	 * @param numSpamItems the number of extracted item identified as spam
	 */
	public void setNumSpamItems(final int numSpamItems) {
		this.numSpamItems = numSpamItems;
	}

}

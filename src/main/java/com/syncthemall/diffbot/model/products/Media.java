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

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Media element (video or image) extracted from a product by Diffbot {@link Products} by Diffbot (Product API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Media extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 3985048229156856713L;
	
	@Key
	private String type;
	@Key
	private String link;
	@Key
	private int height;
	@Key
	private int width;
	@Key
	private String caption;
	@Key
	private boolean primary;
	@Key
	private String xpath;

	/**
	 * @return true if this media is the primary one of the extracted article
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @return media height, in pixels.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return media width, in pixels.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return the type of media identified (image or video)
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return direct (fully resolved) link to image or video content
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @return the Diffbot-determined best caption for the image
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @return the full document Xpath to the media item
	 */
	public String getXpath() {
		return xpath;
	}

	@Override
	public String toString() {
		return "Media - " + super.toString();
	}

}
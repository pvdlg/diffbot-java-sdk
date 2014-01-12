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

/**
 * Image element extracted from an {@link Article} by Diffbot (Article API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Image extends Media implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 4011356665293136585L;

	@Key
	private String caption;
	
	/**
	 * Default constructor.
	 */
	public Image() {
		super();
	}

	/**
	 * @return Diffbot-determined best caption for the image, if detected
	 */
	public  String getCaption() {
		return caption;
	}
	
	@Override
	public String toString() {
		return "Image - " + super.toString();
	}
}
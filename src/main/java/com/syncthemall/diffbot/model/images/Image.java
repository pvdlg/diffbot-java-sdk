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
package com.syncthemall.diffbot.model.images;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.syncthemall.diffbot.Diffbot.Images.Analyze;

/**
 * Image element extracted from an {@link Images} by Diffbot (Image API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Image extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -3025881494361488074L;

	@Key
	private String url;
	@Key
	private String anchorUrl;
	@Key
	private String mime;
	@Key
	private String caption;
	@Key
	private String attrAlt;
	@Key
	private String attrTitle;
	@Key
	private String date;
	@Key
	private int size;
	@Key
	private int pixelHeight;
	@Key
	private int pixelWidth;
	@Key
	private int displayHeight;
	@Key
	private int displayWidth;
	@Key
	private List<String> meta;
	@Key
	private String ocr;
	@Key
	private List<String> colors;
	@Key
	private String xpath;
	@Key
	private String faces;

	/**
	 * @return the url of the media
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return media height, in pixels.
	 */
	public Integer getPixelHeight() {
		return pixelHeight;
	}

	/**
	 * @return media width, in pixels.
	 */
	public Integer getPixelWidth() {
		return pixelWidth;
	}

	/**
	 * @return ff the image is wrapped by an anchor a tag, the anchor location as defined by the href attribute
	 */
	public String getAnchorUrl() {
		return anchorUrl;
	}

	/**
	 * @return MIME type, if available, as specified by "Content-Type" of the image (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public String getMime() {
		return mime;
	}

	/**
	 * @return the best caption for this image
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @return contents of the alt attribute, if available within the HTML IMG tag (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public String getAttrAlt() {
		return attrAlt;
	}

	/**
	 * @return contents of the title attribute, if available within the HTML IMG tag (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public String getAttrTitle() {
		return attrTitle;
	}

	/**
	 * @return date of image upload or creation if available in page metadata
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return size in bytes of image file
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return height of image as rendered on page, if different from actual (pixel) height (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public int getDisplayHeight() {
		return displayHeight;
	}

	/**
	 * @return width of image as rendered on page, if different from actual (pixel) width (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public int getDisplayWidth() {
		return displayWidth;
	}

	/**
	 * @return comma-separated list of image-embedded metadata (e.g., EXIF, XMP, ICC Profile), if available within the
	 *         image file (returned if referenced in {@link Analyze#withFields(String)})
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Exchangeable_image_file_format">EXIF</a>
	 * @see <a href="http://en.wikipedia.org/wiki/Extensible_Metadata_Platform">XMP</a>
	 * @see <a href="http://en.wikipedia.org/wiki/ICC_profile">ICC Profile</a>
	 */
	public List<String> getMeta() {
		return meta;
	}

	/**
	 * @return if text is identified within the image, we will attempt to recognize the text string (returned if
	 *         referenced in {@link Analyze#withFields(String)})
	 */
	public String getOcr() {
		return ocr;
	}

	/**
	 * @return an array of hex values of the dominant colors within the image (returned if referenced in
	 *         {@link Analyze#withFields(String)})
	 */
	public List<String> getColors() {
		return colors;
	}

	/**
	 * @return XPath expression identifying the node containing the image
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * @return the x, y, height, width of coordinates of human faces. Null, if no faces were found (returned if
	 *         referenced in {@link Analyze#withFields(String)})
	 */
	public String getFaces() {
		return faces;
	}

	@Override
	public String toString() {
		return "Image - " + super.toString();
	}
}
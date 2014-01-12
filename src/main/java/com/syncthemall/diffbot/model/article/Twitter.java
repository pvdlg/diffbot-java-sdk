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
 * Twitter {@link Meta} information extracted from an {@link Article} by Diffbot (Article API).
 * 
 * @see <a href="https://dev.twitter.com/docs/cards/markup-reference">Twitter Card metadata</a>
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Twitter extends Model implements Serializable {
	
	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -2053689342457455549L;
	
	@Key(value = "twitter:card")
	private String card;
	@Key(value = "twitter:site")
	private String site;
	@Key(value = "twitter:site:id")
	private String siteId;
	@Key(value = "twitter:creator")
	private String creator;
	@Key(value = "twitter:creator:id")
	private String creatorId;
	@Key(value = "twitter:description")
	private String description;
	@Key(value = "twitter:title")
	private String title;
	@Key(value = "twitter:image:src")
	private String imageSrc;
	@Key(value = "twitter:image:width")
	private String imageWidth;
	@Key(value = "twitter:image:height")
	private String imageHeight;
	@Key(value = "twitter:image")
	private String image;
	@Key(value = "twitter:image0")
	private String image0;
	@Key(value = "twitter:image1")
	private String image1;
	@Key(value = "twitter:image2")
	private String image2;
	@Key(value = "twitter:image3")
	private String image3;
	@Key(value = "twitter:player")
	private String player;
	@Key(value = "twitter:player:width")
	private String playerWidth;
	@Key(value = "twitter:player:height")
	private String playerHeight;
	@Key(value = "twitter:player:stream")
	private String playerStream;
	@Key(value = "twitter:data1")
	private String data1;
	@Key(value = "twitter:label1")
	private String label1;
	@Key(value = "twitter:data2")
	private String data2;
	@Key(value = "twitter:label2")
	private String label2;
	@Key(value = "twitter:app:name:iphone")
	private String appNameIphone;
	@Key(value = "twitter:app:id:iphone")
	private String appIdIphone;
	@Key(value = "twitter:app:url:iphone")
	private String appUrlIphone;
	@Key(value = "twitter:app:name:ipad")
	private String appNameIpad;
	@Key(value = "twitter:app:id:ipad")
	private String appIdIpad;
	@Key(value = "twitter:app:url:ipad")
	private String appUrlIpad;
	@Key(value = "twitter:app:name:googleplay")
	private String appNameGoogleplay;
	@Key(value = "twitter:app:id:googleplay")
	private String appIdGoogeplay;
	@Key(value = "twitter:app:url:googleplay")
	private String appUrlGoogleplay;
	@Key(value = "twitter:account_id")
	private String accountId;

	/**
	 * Default constructor.
	 */
	public Twitter() {
		super();
	}

	/**
	 * @return summary, summary_large_image, photo, gallery, product, app or player
	 */
	public String getCard() {
		return card;
	}

	/**
	 * @return '@username' of website
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @return same as twitter:site, but the user's Twitter ID
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * @return '@username' of content creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @return Twitter user ID of content creator
	 */
	public String getCreatorId() {
		return creatorId;
	}

	/**
	 * @return description of content (maximum 200 characters)
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return title of content (max 70 characters)
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return URL of image to use in the card. Image must be less than 1MB in size.
	 */
	public String getImageSrc() {
		return imageSrc;
	}

	/**
	 * @return width of image in pixels
	 */
	public String getImageWidth() {
		return imageWidth;
	}

	/**
	 * @return height of image in pixels
	 */
	public String getImageHeight() {
		return imageHeight;
	}

	/**
	 * @return 1st image in the gallery. Images must be less than 1MB in size.
	 */
	public String getImage0() {
		return image0;
	}

	/**
	 * @return 2nd image in the gallery. Images must be less than 1MB in size.
	 */
	public String getImage1() {
		return image1;
	}

	/**
	 * @return 3rd image in the gallery. Images must be less than 1MB in size.
	 */
	public String getImage2() {
		return image2;
	}

	/**
	 * @return 4th image in the gallery. Images must be less than 1MB in size.
	 */
	public String getImage3() {
		return image3;
	}

	/**
	 * @return HTTPS URL of player iframe
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * @return width of iframe in pixels
	 */
	public String getPlayerWidth() {
		return playerWidth;
	}

	/**
	 * @return height of iframe in pixels
	 */
	public String getPlayerHeight() {
		return playerHeight;
	}

	/**
	 * @return URL to raw video or audio stream
	 */
	public String getPlayerStream() {
		return playerStream;
	}

	/**
	 * @return top customizable data field, can be a relatively short string (ie "$3.99")
	 */
	public String getData1() {
		return data1;
	}

	/**
	 * @return customizable label or units for the information in twitter:data1 (best practice: use all caps)
	 */
	public String getLabel1() {
		return label1;
	}

	/**
	 * @return bottom customizable data field, can be a relatively short string (ie "Seattle, WA")
	 */
	public String getData2() {
		return data2;
	}

	/**
	 * @return customizable label or units for the information in twitter:data1 (best practice: use all caps)
	 */
	public String getLabel2() {
		return label2;
	}

	/**
	 * @return name of your iPhone app
	 */
	public String getAppNameIphone() {
		return appNameIphone;
	}

	/**
	 * @return your app ID in the iTunes App Store (Note: NOT your bundle ID)
	 */
	public String getAppIdIphone() {
		return appIdIphone;
	}

	/**
	 * @return your app's custom URL scheme (you must include "://" after your scheme name)
	 */
	public String getAppUrlIphone() {
		return appUrlIphone;
	}

	/**
	 * @return name of your iPad optimized app
	 */
	public String getAppNameIpad() {
		return appNameIpad;
	}

	/**
	 * @return your app ID in the iTunes App Store
	 */
	public String getAppIdIpad() {
		return appIdIpad;
	}

	/**
	 * @return your app's custom URL scheme
	 */
	public String getAppUrlIpad() {
		return appUrlIpad;
	}

	/**
	 * @return name of your Android app
	 */
	public String getAppNameGoogleplay() {
		return appNameGoogleplay;
	}

	/**
	 * @return your app ID in the Google Play Store
	 */
	public String getAppIdGoogeplay() {
		return appIdGoogeplay;
	}

	/**
	 * @return your app's custom URL scheme
	 */
	public String getAppUrlGoogleplay() {
		return appUrlGoogleplay;
	}

	/**
	 * @return image in the gallery
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return account id
	 */
	public String getAccountId() {
		return accountId;
	}

	@Override
	public String toString() {
		return "Twitter - " + super.toString();
	}

}

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
import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.syncthemall.diffbot.Diffbot.Products.Analyze;

/**
 * Product element extracted from an {@link Products} by Diffbot (Product API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Product extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 1699832882116245869L;

	@Key
	private String title;
	@Key
	private String description;
	@Key
	private String brand;
	@Key
	private List<Media> media;
	@Key
	private String offerPrice;
	@Key
	private String regularPrice;
	@Key
	private String saveAmount;
	@Key
	private String shippingAmount;
	@Key
	private String productId;
	@Key
	private String upc;
	@Key
	private String prefixCode;
	@Key
	private String productOrigin;
	@Key
	private String isbn;
	@Key
	private String sku;
	@Key
	private String mpn;
	@Key
	private boolean availability;
	@Key
	private Price regularPriceDetails;
	@Key
	private Price saveAmountDetails;
	@Key
	private Price offerPriceDetails;
	@Key
	private Price shippingAmountDetails;

	/**
	 * @return the name of the product
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description, if available, of the product.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the brand, if available, of the product (returned if referenced in {@link Analyze#withFields(String)})
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @return the identified offer or actual/'final' price of the product
	 */
	public String getOfferPrice() {
		return offerPrice;
	}

	/**
	 * @return the regular or original price of the product, if available
	 */
	public String getRegularPrice() {
		return regularPrice;
	}

	/**
	 * @return the discount or amount saved, if available
	 */
	public String getSaveAmount() {
		return saveAmount;
	}

	/**
	 * @return the shipping price, if available
	 */
	public String getShippingAmount() {
		return shippingAmount;
	}

	/**
	 * @return A Diffbot-determined unique product ID. If upc, isbn, mpn or sku are identified on the page, productId
	 *         will select from these values in the above order. Otherwise Diffbot will attempt to derive the best
	 *         unique value for the product
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @return the universal Product Code (UPC/EAN), if available
	 */
	public String getUpc() {
		return upc;
	}

	/**
	 * @return the GTIN prefix code, typically the country of origin as identified by UPC/ISBN
	 */
	public String getPrefixCode() {
		return prefixCode;
	}

	/**
	 * @return if available, the two-character ISO country code where the product was produced
	 */
	public String getProductOrigin() {
		return productOrigin;
	}

	/**
	 * @return the International Standard Book Number (ISBN), if available
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @return the Stock Keeping Unit -- store/vendor inventory number -- if available (returned if referenced in {@link Analyze#withFields(String)})
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @return the Manufacturer's Product Number, if available (returned if referenced in {@link Analyze#withFields(String)})
	 */
	public String getMpn() {
		return mpn;
	}

	/**
	 * @return the item's availability
	 */
	public boolean isAvailability() {
		return availability;
	}

	/**
	 * @return the list of media items (images or videos) of the product
	 */
	public List<Media> getMedia() {
		return media;
	}

	/**
	 * @return the regular or original price detail of the product, if available
	 */
	public Price getRegularPriceDetails() {
		return regularPriceDetails;
	}

	/**
	 * @return the details of discount or amount saved, if available
	 */
	public Price getSaveAmountDetails() {
		return saveAmountDetails;
	}

	/**
	 * @return the details of the identified offer or actual/'final' price of the product
	 */
	public Price getOfferPriceDetails() {
		return offerPriceDetails;
	}

	/**
	 * @return the shipping price details, if available
	 */
	public Price getShippingAmountDetails() {
		return shippingAmountDetails;
	}

	@Override
	public String toString() {
		return "Product - " + super.toString();
	}
}
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
package com.syncthemall.diffbot;

import static com.syncthemall.diffbot.Constants.FIELDS;
import static com.syncthemall.diffbot.Constants.FORMAT;
import static com.syncthemall.diffbot.Constants.MODE;
import static com.syncthemall.diffbot.Constants.STATS;
import static com.syncthemall.diffbot.Constants.TIMEOUT;
import static com.syncthemall.diffbot.Constants.TOKEN;
import static com.syncthemall.diffbot.Constants.URL;
import static com.syncthemall.diffbot.Constants.USER_AGENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.syncthemall.diffbot.model.Model;
import com.syncthemall.diffbot.model.PageType;
import com.syncthemall.diffbot.model.classifier.Classified;

/**
 * This is the main class of the SDK and allows to call:
 * <ul>
 * <li>The Article API to extract informations (title, images, main content etc...) from an article web page.</li>
 * <li>The Frontpage API to extract the elements (articles, posts, etc...) from the home page of a website.</li>
 * <li>The Image API to extract the images from a web page.</li>
 * <li>The Product API to extract the products from a web page.</li>
 * <li>The Classifier API that takes any web link and automatically determines what type of page it is.</li>
 * <li>The batch API allowing execute multiple Article, Frontpage, Image or Product requests at once.</li>
 * </ul>
 * <p>
 * This class is thread-safe and should be instantiated only once. If the batch API is used this class <strong>has
 * to</strong> be instantiated only once as it store the queue of request to execute in a batch.
 * <p>
 * The underlying implementations for the HTTP client (Apache, GAE url fetch, JVM default) and the JSON parser (Gson or
 * Jackson) can be chosen during construction {@link Diffbot#Diffbot(HttpTransport, JsonFactory, String)}.
 * <p>
 * For example to use Apache and Jackson:
 * 
 * <pre>
 * private static Diffbot diffbot;
 * ...
 * diffbot = new Diffbot(new ApacheHttpTransport(), new JacksonFactory(), &quot;your dev token&quot;);
 * </pre>
 * 
 * Note: google-http-client-jackson2 has to be in the classpath.
 * <p>
 * <strong>The Article API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Article article = diffbot.article().analyze(&quot;web page URL&quot;).withFields(&quot;*&quot;).execute();
 * </pre>
 * <p>
 * <strong>The Frontpage API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Frontpage frontpage = diffbot.frontpage().analyze(&quot;web page URL&quot;).execute();
 * </pre>
 * 
 * <strong>The Image API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Images images = diffbot.images().analyze(&quot;web page URL&quot;).withFields(&quot;*&quot;).execute();
 * </pre>
 * <p>
 * 
 * <strong>The Product API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Products products = diffbot.products().analyze(&quot;web page URL&quot;).withFields(&quot;*&quot;).execute();
 * </pre>
 * 
 * <strong>The Classifier API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Classified classified = diffbot.classifier().analyze(&quot;web page URL&quot;).withFields(&quot;*&quot;).execute();
 * </pre>
 * 
 * If the {@code Classified} resulting object is of {@link PageType} {@link PageType#ARTICLE}, {@link PageType#IMAGE} or
 * {@link PageType#PRODUCT} it can be parsed as respectively an {@link com.syncthemall.diffbot.model.article.Article},
 * an {@link com.syncthemall.diffbot.model.images.Images} or a {@link com.syncthemall.diffbot.model.products.Products}:
 * 
 * <pre>
 * if (classified.getType().equals(PageType.ARTICLE)) {
 * 	Article article = classified.asArticle();
 * }
 * </pre>
 * <p>
 * 
 * <strong>The Batch API</strong> <br>
 * The batch API allows to prepare multiple Article, Frontpage, Image, Product and/or Classifier request and to send
 * them all at once. The batch API:
 * <ul>
 * <li>limits the number of API calls and reduces the network load, by avoiding http overhead for every request</li>
 * <li>slightly improve the performances if a lot of requests are executed</li>
 * <li>limits the usage of the Diffbot quota</li>
 * </ul>
 * To prepare requests to be executed in batch:
 * 
 * <pre>
 * Future&lt;Article&gt; fArticle = diffbot.article().analyze(&quot;web page URL&quot;).queue();
 * Future&lt;Frontpage&gt; fFrontpage = diffbot.frontpage().analyze(&quot;web page URL&quot;).queue();
 * Future&lt;Images&gt; fImages = diffbot.images().analyze(&quot;web page URL&quot;).queue();
 * Future&lt;Products&gt; fProducts = diffbot.products().analyze(&quot;web page URL&quot;).queue();
 * Future&lt;Classified&gt; fClassified = diffbot.classifier().analyze(&quot;web page URL&quot;).queue();
 * </pre>
 * <p>
 * Note that this can be done concurrently by multiple threads. The {@code Diffbot} class is fully thread-safe.
 * <p>
 * At this point no call has been done to Diffbot. To obtain the results:
 * 
 * <pre>
 * Article article = fArticle.get();
 * Frontpage frontpage = fFrontpage.get();
 * Images images = fImages.get();
 * Products products = fProducts.get();
 * Classified classified = fClassified.get();
 * </pre>
 * 
 * The first line trigger a batch request that retrieves the results for all the requests added since the last call to
 * {@code Future#get()}. The second line doesn't need to do any API call as the result was retrieve during the execution
 * of the fist line.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class Diffbot {

	private ResourceBundle bundle = ResourceBundle.getBundle("com.syncthemall.diffbot.messages.Messages");
	private JAXBContext jAXBContext;
	private JsonFactory jsonFactory;
	private HttpRequestFactory requestFactory;
	private List<Future<? extends Model>> futures = new ArrayList<Future<? extends Model>>();
	private int maxBatchRequest = 25;
	private int batchRequestTimeout = 300000;

	/**
	 * Constructor allowing to set the Diffbot developer token and the underlying implementations for the HTTP client
	 * and the JSON parser.
	 * <p>
	 * The HTTP implementation can be:
	 * <ul>
	 * <li> {@code ApacheHttpTransport}: <a href="http://hc.apache.org/">Apache HTTP client</a>.</li>
	 * <li> {@code NetHttpTransport}: Default JVM HTTP client.</li>
	 * <li> {@code UrlFetchTransport}: <a href="https://developers.google.com/appengine/docs/java/urlfetch/">URL
	 * Fetch</a> to use with Google App Engine.</li>
	 * </ul>
	 * The JSON parser implementation can be:
	 * <ul>
	 * <li>{@code GsonFactory}: <a href="https://code.google.com/p/google-gson">Gson</a>.</li>
	 * <li>{@code JsonFactory}: <a href="http://jackson.codehaus.org">Jackson 2</a>.</li>
	 * </ul>
	 * 
	 * @param httpTransport an implementation of {@code HttpTransport}
	 * @param jsonFactory an implementation of {@code JsonFactory}
	 * @param token your Diffbot developer token
	 * @throws JAXBException if JAXB is not in the classpath
	 */
	public Diffbot(final HttpTransport httpTransport, final JsonFactory jsonFactory, final String token)
			throws JAXBException {
		if (token == null || token.isEmpty()) {
			throw new IllegalArgumentException(bundle.getString("token.empty"));
		}
		if (httpTransport == null) {
			throw new IllegalArgumentException(bundle.getString("transport.null"));
		}
		if (jsonFactory == null) {
			throw new IllegalArgumentException(bundle.getString("jsonfactory.null"));
		}
		this.jsonFactory = jsonFactory;
		jAXBContext = JAXBContext.newInstance(com.syncthemall.diffbot.model.frontpage.Frontpage.class);
		this.requestFactory = httpTransport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(final HttpRequest request) {
				request.setParser(new JsonObjectParser(jsonFactory));
				request.getHeaders().setUserAgent(USER_AGENT);
				request.setInterceptor(new HttpExecuteInterceptor() {
					@Override
					public void intercept(final HttpRequest request) throws IOException {
						request.getUrl().set(TOKEN, token);
					}
				});
			}
		});
	}

	/**
	 * @return the JAXB Context.
	 **/
	public final JAXBContext getJAXBContext() {
		return jAXBContext;
	}

	/**
	 * @return the JSON Factory.
	 **/
	public final JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	/**
	 * @return the HTTP Request Factory.
	 **/
	public final HttpRequestFactory getRequestFactory() {
		return requestFactory;
	}

	/**
	 * @return the list of {@code Future<Model>} currently in the batch queue.
	 */
	public final List<Future<? extends Model>> getFutures() {
		return futures;
	}

	/**
	 * An accessor for creating requests from the {@link Frontpage} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Frontpage.Analyze request = diffboat.frontpage().analyze(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Frontpage frontpage() {
		return new Frontpage();
	}

	/**
	 * The "frontpage" collection of methods.
	 */
	public final class Frontpage {

		/**
		 * Get a frontpage analyzed by Diffbot.
		 * 
		 * Create a request for the method "frontpage".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Analyze#execute()} method to invoke the remote operation or {@link Analyze#queue()} to add it to
		 * the batch queue.
		 * 
		 * @param url URL to extract the {@link Frontpage} from.
		 * @return the request
		 */
		public Analyze analyze(final String url) {
			return new Analyze(url);
		}

		/**
		 * Request to the Diffbot Frontpage API.
		 */
		public class Analyze extends DiffbotRequest<com.syncthemall.diffbot.model.frontpage.Frontpage> {

			private static final String FRONTPAGE_URI = "http://www.diffbot.com/api/frontpage";

			/**
			 * Creates a {@code DiffbotRequest} to extract the elements of a front page.
			 * 
			 * @param url URL to extract the {@link Frontpage} from.
			 */
			protected Analyze(final String url) {
				super(Diffbot.this, com.syncthemall.diffbot.model.frontpage.Frontpage.class, FRONTPAGE_URI);
				set(URL, url);
				set(FORMAT, "xml");
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.FRONTPAGE;
			}
		}
	}

	/**
	 * An accessor for creating requests from the {@link Article} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Article.Analyze request = diffboat.article().analyze(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Article article() {
		return new Article();
	}

	/**
	 * The "articles" collection of methods.
	 */
	public final class Article {

		/**
		 * Get an article analyzed by Diffbot.
		 * 
		 * Create a request for the method "article".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Analyze#execute()} method to invoke the remote operation or {@link Analyze#queue()} to add it to
		 * the batch queue.
		 * 
		 * @param url URL to extract the {@link Article} from.
		 * @return the request
		 */
		public Analyze analyze(final String url) {
			return new Analyze(url);
		}

		/**
		 * Request to the Diffbot Article API.
		 */
		public class Analyze extends DiffbotRequest<com.syncthemall.diffbot.model.article.Article> {

			private static final String ARTICLE_URI = "http://api.diffbot.com/v2/article";

			/**
			 * Creates a {@code DiffbotRequest} to extract an article of a given URL.
			 * 
			 * @param url URL to extract the {@link Article} from.
			 */
			protected Analyze(final String url) {
				super(Diffbot.this, com.syncthemall.diffbot.model.article.Article.class, ARTICLE_URI);
				set(URL, url);
			}

			/**
			 * The fields parameter allows to limit or expand which fields are returned in the response. For nested
			 * arrays, use parentheses to retrieve specific fields, or * to return all sub-fields.
			 * <p>
			 * For example: meta,links,images(*)
			 * 
			 * @param fields information returned by the API
			 * @return the instance of this request
			 * 
			 * @see <a href="http://www.diffbot.com/dev/docs/article">Diffbot - Article API Documentation</a>
			 */
			public final Analyze withFields(final String fields) {
				set(FIELDS, fields);
				return this;
			}

			/**
			 * Overrides the default API timeout of 5000ms.
			 * 
			 * @param timeout The timeout in milliseconds
			 * 
			 * @return the instance of this request
			 */
			public final Analyze withTimeout(final int timeout) {
				set(TIMEOUT, String.valueOf(timeout));
				setReadTimeout(timeout);
				return this;
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.ARTICLE;
			}
		}
	}

	/**
	 * An accessor for creating requests from the {@link Images} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Images.Analyze request = diffboat.images().analyze(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Images images() {
		return new Images();
	}

	/**
	 * The "images" collection of methods.
	 */
	public final class Images {

		/**
		 * Get images analyzed by Diffbot.
		 * 
		 * Create a request for the method "image".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Analyze#execute()} method to invoke the remote operation or {@link Analyze#queue()} to add it to
		 * the batch queue.
		 * 
		 * @param url URL to extract the {@link Images} from.
		 * @return the request
		 */
		public Analyze analyze(final String url) {
			return new Analyze(url);
		}

		/**
		 * Request to the Diffbot Image API.
		 */
		public class Analyze extends DiffbotRequest<com.syncthemall.diffbot.model.images.Images> {

			private static final String IMAGE_URI = "http://api.diffbot.com/v2/image";

			/**
			 * Creates a {@code DiffbotRequest} to extract an article of a given URL.
			 * 
			 * @param url URL to extract the {@link Images} from.
			 */
			protected Analyze(final String url) {
				super(Diffbot.this, com.syncthemall.diffbot.model.images.Images.class, IMAGE_URI);
				set(URL, url);
			}

			/**
			 * The fields parameter allows to limit or expand which fields are returned in the response. For nested
			 * arrays, use parentheses to retrieve specific fields, or * to return all sub-fields.
			 * <p>
			 * For example: meta,links,images(*)
			 * 
			 * @param fields information returned by the API
			 * @return the instance of this request
			 * 
			 * @see <a href="http://www.diffbot.com/dev/docs/image">Diffbot - Image API Documentation</a>
			 */
			public final Analyze withFields(final String fields) {
				set(FIELDS, fields);
				return this;
			}

			/**
			 * Overrides the default API timeout of 5000ms.
			 * 
			 * @param timeout The timeout in milliseconds
			 * 
			 * @return the instance of this request
			 */
			public final Analyze withTimeout(final int timeout) {
				set(TIMEOUT, String.valueOf(timeout));
				setReadTimeout(timeout);
				return this;
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.IMAGE;
			}
		}
	}

	/**
	 * An accessor for creating requests from the {@link Products} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Products.Analyze request = diffboat.products().analyze(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Products products() {
		return new Products();
	}

	/**
	 * The "products" collection of methods.
	 */
	public final class Products {

		/**
		 * Get product webpage analyzed by Diffbot.
		 * 
		 * Create a request for the method "product".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Analyze#execute()} method to invoke the remote operation or {@link Analyze#queue()} to add it to
		 * the batch queue.
		 * 
		 * @param url URL to extract the {@link Products} from.
		 * @return the request
		 */
		public Analyze analyze(final String url) {
			return new Analyze(url);
		}

		/**
		 * Request to the Diffbot Product API.
		 */
		public class Analyze extends DiffbotRequest<com.syncthemall.diffbot.model.products.Products> {

			private static final String PRODUCT_URI = "http://api.diffbot.com/v2/product";

			/**
			 * Creates a {@code DiffbotRequest} to extract an article of a given URL.
			 * 
			 * @param url URL to extract the {@link Products} from.
			 */
			protected Analyze(final String url) {
				super(Diffbot.this, com.syncthemall.diffbot.model.products.Products.class, PRODUCT_URI);
				set(URL, url);
			}

			/**
			 * The fields parameter allows to limit or expand which fields are returned in the response. For nested
			 * arrays, use parentheses to retrieve specific fields, or * to return all sub-fields.
			 * <p>
			 * For example: meta,links,images(*)
			 * 
			 * @param fields information returned by the API
			 * @return the instance of this request
			 * 
			 * @see <a href="http://www.diffbot.com/dev/docs/product">Diffbot - Product API Documentation</a>
			 */
			public final Analyze withFields(final String fields) {
				set(FIELDS, fields);
				return this;
			}

			/**
			 * Overrides the default API timeout of 5000ms.
			 * 
			 * @param timeout The timeout in milliseconds
			 * 
			 * @return the instance of this request
			 */
			public final Analyze withTimeout(final int timeout) {
				set(TIMEOUT, String.valueOf(timeout));
				setReadTimeout(timeout);
				return this;
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.PRODUCT;
			}
		}
	}

	/**
	 * An accessor for creating requests from the {@link Classifier} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Classifiers.Analyze request = diffboat.classifier().analyze(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Classifier classifier() {
		return new Classifier();
	}

	/**
	 * The "classifier" collection of methods.
	 */
	public final class Classifier {

		/**
		 * Get product webpage analyzed by Diffbot.
		 * 
		 * Create a request for the method "product".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Analyze#execute()} method to invoke the remote operation or {@link Analyze#queue()} to add it to
		 * the batch queue.
		 * 
		 * @param url URL to extract the {@link Classified} from.
		 * @return the request
		 */
		public Analyze analyze(final String url) {
			return new Analyze(url);
		}

		/**
		 * Request to the Diffbot Product API.
		 */
		public class Analyze extends DiffbotRequest<com.syncthemall.diffbot.model.classifier.Classified> {

			private static final String CLASSIFIER_URI = "http://api.diffbot.com/v2/analyze";

			/**
			 * Creates a {@code DiffbotRequest} to extract an article of a given URL.
			 * 
			 * @param url URL to extract the {@link Products} from.
			 */
			protected Analyze(final String url) {
				super(Diffbot.this, com.syncthemall.diffbot.model.classifier.Classified.class, CLASSIFIER_URI);
				set(URL, url);
			}

			/**
			 * The fields parameter allows to limit or expand which fields are returned in the response. For nested
			 * arrays, use parentheses to retrieve specific fields, or * to return all sub-fields.
			 * <p>
			 * For example: meta,links,images(*)
			 * 
			 * @param fields information returned by the API
			 * @return the instance of this request
			 * 
			 * @see <a href="http://www.diffbot.com/dev/docs/product">Diffbot - Product API Documentation</a>
			 */
			public final Analyze withFields(final String fields) {
				set(FIELDS, fields);
				return this;
			}

			/**
			 * Overrides the default API timeout of 5000ms.
			 * 
			 * @param timeout The timeout in milliseconds
			 * 
			 * @return the instance of this request
			 */
			public final Analyze withTimeout(final int timeout) {
				set(TIMEOUT, String.valueOf(timeout));
				setReadTimeout(timeout);
				return this;
			}

			/**
			 * By default the Page Classifier API will fully extract pages that match an existing Diffbot Automatic API.
			 * Set mode to a specific {@code PageType} to extract content only from that particular {@code PageType}.
			 * All others will simply return the page classification information.
			 * 
			 * @param type the {@code PageType} for which to extract content
			 * 
			 * @return the instance of this request
			 */
			public final Analyze withMode(final PageType type) {
				set(MODE, type.getKey());
				return this;
			}

			/**
			 * Returns statistics on page classification and extraction, including an array of individual page-types and
			 * the Diffbot-determined score (likelihood) for each type.
			 * 
			 * @return the instance of this request
			 */
			public final Analyze withStats() {
				set(STATS, "");
				return this;
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.CLASSIFIER;
			}
		}
	}

	/**
	 * @return the maximum number of request send in one batch
	 */
	public final int getMaxBatchRequest() {
		return maxBatchRequest;
	}

	/**
	 * @param maxBatchRequest maximum number of requests send in one batch (minimum 1, maximum 50, default 25)
	 */
	public final void setMaxBatchRequest(final int maxBatchRequest) {
		if (maxBatchRequest <= 0 || maxBatchRequest > 50) {
			throw new IllegalArgumentException(bundle.getString("max.batch.request"));
		}
		this.maxBatchRequest = maxBatchRequest;
	}

	/**
	 * @return the timeout of a batch request
	 */
	public final int getBatchRequestTimeout() {
		return batchRequestTimeout;
	}

	/**
	 * @param batchRequestTimeout the timeout of a batch request (the default value is 5 minutes) or {@code 0} for an
	 *            infinite timeout
	 */
	public final void setBatchRequestTimeout(final int batchRequestTimeout) {
		this.batchRequestTimeout = batchRequestTimeout;
	}

}
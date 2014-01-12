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

import static com.syncthemall.diffbot.Constants.USER_AGENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.frontpage.Frontpage;

/**
 * This is the main class of the SDK and allows to call:
 * <ul>
 * <li>The Article API to extract informations (title, images, main content etc...) from an article web page.</li>
 * <li>The Frontpage API to extract the elements (articles, posts, etc...) from the home page of a website.</li>
 * <li>The batch API allowing execute multiple Article or Frontpage requests at once.</li>
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
 * Article article = diffbot.articles().get(&quot;http://www.cbs.com/shows/how_i_met_your_mother/barneys_blog/1000461/&quot;)
 * 		.withTags().withComments().withSummary().execute();
 * </pre>
 * <p>
 * <strong>The Frontpage API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Frontpage frontpage = diffbot.frontpages().get(&quot;http://theverge.com&quot;).execute();
 * </pre>
 * 
 * <strong>The Batch API</strong> <br>
 * The batch API allows to prepare multiple Article and/or Frontpage request and to send them all at once. The batch
 * API:
 * <ul>
 * <li>limits the number of API calls and reduces the network load, by avoiding http overhead for every request</li>
 * <li>slightly improve the performances if a lot of requests are executed</li>
 * <li>limits the usage of the Diffbot quota</li>
 * </ul>
 * To prepare requests to be executed in batch:
 * 
 * <pre>
 * Future&lt;Article&gt; fArticle = diffbot.articles()
 * 		.get(&quot;http://www.cbs.com/shows/how_i_met_your_mother/barneys_blog/1000461/&quot;).withTags().withComments()
 * 		.withSummary().queue();
 * Future&lt;Frontpage&gt; fFrontpage = diffbot.frontpages().get(&quot;http://theverge.com&quot;).queue();
 * </pre>
 * 
 * Note that this can be done concurrently by multiple threads. The {@code Diffbot} class is fully thread-safe. At this
 * point no call has been done to Diffbot. To obtain the results:
 * 
 * <pre>
 * Article article = fArticle.get();
 * Frontpage frontpage = fFrontpage.get();
 * </pre>
 * 
 * The first line trigger a batch request that retrieves the results for all the requests added since the last call to
 * {@code Future#get()}. The second line doesn't need to do any API call as the result was retrieve during the execution
 * of the fist line.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class Diffbot {

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
			throw new IllegalArgumentException("Token cannot be null or empty.");
		}
		if (httpTransport == null) {
			throw new IllegalArgumentException("HttpTransport cannot be null.");
		}
		if (jsonFactory == null) {
			throw new IllegalArgumentException("JsonFactory cannot be null.");
		}
		this.jsonFactory = jsonFactory;
		jAXBContext = JAXBContext.newInstance(Frontpage.class);
		this.requestFactory = httpTransport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(final HttpRequest request) {
				request.setParser(new JsonObjectParser(jsonFactory));
				request.getHeaders().setUserAgent(USER_AGENT);
				request.setInterceptor(new HttpExecuteInterceptor() {
					@Override
					public void intercept(final HttpRequest request) throws IOException {
						request.getUrl().set("token", token);
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
	 * An accessor for creating requests from the {@link Frontpages} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Frontpages.Get request = diffboat.frontpages().get(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Frontpages frontpages() {
		return new Frontpages();
	}

	/**
	 * The "frontpages" collection of methods.
	 */
	public final class Frontpages {

		/**
		 * Get a frontpage analyzed by Diffbot.
		 * 
		 * Create a request for the method "frontpage".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Get#execute()} method to invoke the remote operation or {@link Get#queue()} to add it to the bach
		 * queue.
		 * 
		 * @param url URL to extract the {@link Frontpage} from.
		 * @return the request
		 */
		public Get get(final String url) {
			return new Get(url);
		}

		/**
		 * Request to the Diffbot Frontpage API.
		 */
		public class Get extends DiffbotRequest<Frontpage> {

			private static final String FRONTPAGE_URI = "http://www.diffbot.com/api/frontpage";

			/**
			 * Creates a {@code Request} to extract the elements of a front page.
			 * 
			 * @param url URL to extract the {@link Frontpage} from.
			 */
			protected Get(final String url) {
				super(Diffbot.this, Frontpage.class, FRONTPAGE_URI);
				set("url", url);
				set("format", "xml");
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.FRONTPAGE;
			}
		}
	}

	/**
	 * An accessor for creating requests from the {@link Articles} collection.
	 * 
	 * <p>
	 * The typical use is:
	 * </p>
	 * 
	 * <pre>
	 *   {@code Diffbot diffbot = new Diffboat(...);}
	 *   {@code Diffboat.Articles.Get request = diffboat.articles().get(parameters ...)}
	 * </pre>
	 * 
	 * @return the resource collection
	 */
	public final Articles articles() {
		return new Articles();
	}

	/**
	 * The "articles" collection of methods.
	 */
	public final class Articles {

		/**
		 * Get an article analyzed by Diffbot.
		 * 
		 * Create a request for the method "article".
		 * 
		 * This request holds the parameters needed by the diffbot server. After setting any optional parameters, call
		 * the {@link Get#execute()} method to invoke the remote operation or {@link Get#queue()} to add it to the bach
		 * queue.
		 * 
		 * @param url URL to extract the {@link Article} from.
		 * @return the request
		 */
		public Get get(final String url) {
			return new Get(url);
		}

		/**
		 * Request to the Diffbot Article API.
		 */
		public class Get extends DiffbotRequest<Article> {

			private static final String ARTICLE_URI = "http://api.diffbot.com/v2/article";

			/**
			 * Creates a {@code Request} to extract an article of a given URL.
			 * 
			 * @param url URL to extract the {@link Article} from.
			 */
			protected Get(final String url) {
				super(Diffbot.this, Article.class, ARTICLE_URI);
				set("url", url);
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
			 * @see <a href="http://www.diffbot.com/dev/docs/article/">Diffbot - Article API Documentation</a>
			 */
			public final Get withFields(final String fields) {
				set("fields", fields);
				return this;
			}

			/**
			 * Overrides the default API timeout of 5000ms.
			 * 
			 * @param timeout The timeout in milliseconds.
			 * 
			 * @return the instance of this request
			 */
			public final Get withTimeout(final long timeout) {
				set("timeout", String.valueOf(timeout));
				return this;
			}

			@Override
			protected final ApiType getApiType() {
				return ApiType.ARTICLE;
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
			throw new IllegalArgumentException("The maz batch request number must be between 0 and 50.");
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
	 * @param batchRequestTimeout the timeout of a batch request (the default value is 5 minutes)
	 */
	public final void setBatchRequestTimeout(final int batchRequestTimeout) {
		this.batchRequestTimeout = batchRequestTimeout;
	}

}
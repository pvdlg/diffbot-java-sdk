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
package com.syncthemall.diffbot.api;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.syncthemall.diffbot.api.Request.ApiType;
import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotIOException;
import com.syncthemall.diffbot.exception.DiffbotParseException;
import com.syncthemall.diffbot.exception.DiffbotServerException;
import com.syncthemall.diffbot.exception.DiffbotUnauthorizedException;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.batch.BatchRequest;
import com.syncthemall.diffbot.model.batch.BatchResponse;
import com.syncthemall.diffbot.model.batch.Header;
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
 * to</strong> be instantiated only once as it store the list request to execute in a batch.
 * <p>
 * The underlying implementations for the HTTP client (Apache, GAE url fetch, JVM default) and the JSON parser (Gson or
 * Jackson) can be chosen during construction {@link DiffbotAPI#DiffbotAPI(HttpTransport, JsonFactory, String)}.
 * <p>
 * For example to use Apache and Jackson:
 * 
 * <pre>
 * private static DiffbotAPI api;
 * ...
 * api = new DiffbotAPI(new ApacheHttpTransport(), new JacksonFactory(), &quot;your dev token&quot;);
 * </pre>
 * 
 * Note: google-http-client-jackson2 has to be in the classpath.
 * <p>
 * <strong>The Article API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Article article = api
 * 		.article(new ArticleRequest(&quot;http://www.cbs.com/shows/how_i_met_your_mother/barneys_blog/1000461/&quot;).withTags()
 * 				.withComments().withSummary());
 * </pre>
 * <p>
 * <strong>The Frontpage API</strong> <br>
 * Calling the API is as simple as :
 * 
 * <pre>
 * Frontpage frontpage = api.frontpage(new FrontpageRequest(&quot;http://theverge.com&quot;));
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
 * Future&lt;Article&gt; fArticle = api
 * 		.batch(new ArticleRequest(&quot;http://www.cbs.com/shows/how_i_met_your_mother/barneys_blog/1000461/&quot;).withTags()
 * 				.withComments().withSummary());
 * Future&lt;Frontpage&gt; fFrontpage = api.batch(new FrontpageRequest(&quot;http://theverge.com&quot;));
 * </pre>
 * 
 * Note that this can be done by multiple thread. The DiffbotAPI class is fully thread-safe. At this point no call has
 * been done to Diffbot. To obtain the results:
 * 
 * <pre>
 * Article article = fArticle.get();
 * Frontpage frontpage = fFrontpage.get();
 * </pre>
 * 
 * The first line trigger a batch request that retrieves the results for all the requests added since the last call to
 * {@code Future#get()}. The second line doesn't need to do any API call as the result was retrieve during the execution
 * of the fist line.
 */
public class DiffbotAPI implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = -657775976265652256L;

	protected static final String BATCH_URI = "/api/batch";
	protected static final String API_SERVER = "http://www.diffbot.com";
	protected static final String ARTICLE_URI = "/api/article";
	protected static final String FRONTPAGE_URI = "/api/frontpage";

	private String token;
	private static final String USER_AGENT = "diffbot-java-sdk/1.0";
	private static final int MAX_BATCH_REQUEST = 50;
	private static final int HTTP_OK = 200;
	private static final int HTTP_UNAUTHORIZED = 401;

	private JAXBContext jc;
	private JsonFactory jsonFactory;

	private HttpRequestFactory requestFactory;

	@SuppressWarnings("rawtypes")
	private List<Future> futures = new ArrayList<Future>();

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
	public DiffbotAPI(final HttpTransport httpTransport, final JsonFactory jsonFactory, final String token)
			throws JAXBException {
		if (token == null || token.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.jsonFactory = jsonFactory;
		jc = JAXBContext.newInstance(Frontpage.class);
		this.token = token;
		this.requestFactory = httpTransport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(final HttpRequest request) {
				request.setParser(new JsonObjectParser(jsonFactory));
				request.getHeaders().setUserAgent(USER_AGENT);
			}
		});
	}

	/**
	 * Call the Diffbot Article API.
	 * 
	 * @param articleRequest a {@code ArticleRequest} filled with the parameters to call the Article API
	 * @return the {@code Article} representing the Diffbot API result
	 * @throws DiffbotUnauthorizedException if the developer token is not recognized or revoked
	 * @throws DiffbotServerException if a HTTP error occurs on the Diffbot server
	 * @throws DiffbotIOException if an IO error (usually network related) occur during the API call
	 * @throws DiffbotAPIException if an API error occur on Diffbot servers while processing the request
	 */
	public final Article article(final ArticleRequest articleRequest) throws DiffbotUnauthorizedException,
			DiffbotServerException, DiffbotIOException, DiffbotAPIException {

		articleRequest.set("token", this.token);

		try {
			HttpRequest request = requestFactory.buildGetRequest(articleRequest);
			com.google.api.client.http.HttpResponse response = request.execute();
			if (response.getStatusCode() == HTTP_UNAUTHORIZED) {
				throw new DiffbotUnauthorizedException("Not authorized API token.");
			} else if (response.getStatusCode() != HTTP_OK) {
				throw new DiffbotServerException(response.getStatusCode(), response.getStatusMessage());
			}
			Article result = response.parseAs(Article.class);
			BigDecimal errorCode = (BigDecimal) result.get("errorCode");
			if (errorCode != null) {
				throw new DiffbotAPIException(errorCode.intValue(), (String) result.get("error"));
			}
			return result;
		} catch (IOException e) {
			throw new DiffbotIOException(e.getMessage(), e);
		}
	}

	/**
	 * Call the Diffbot Frontpage API.
	 * 
	 * @param frontpageRequest a {@code FrontpageRequest} filled with the parameters to call the Frontpage API
	 * @return the {@code Frontpage} representing the Diffbot API result
	 * @throws DiffbotUnauthorizedException if the developer token is not recognized or revoked
	 * @throws DiffbotServerException if a HTTP error occurs on the Diffbot server
	 * @throws DiffbotIOException if an IO error (usually network related) occur during the API call
	 * @throws DiffbotAPIException if an API error occur on Diffbot servers while processing the request
	 * @throws DiffbotParseException if the Diffbot response cannot be parsed
	 */
	public final Frontpage frontpage(final FrontpageRequest frontpageRequest) throws DiffbotUnauthorizedException,
			DiffbotServerException, DiffbotIOException, DiffbotAPIException, DiffbotParseException {

		frontpageRequest.set("token", this.token);
		try {
			HttpRequest request = requestFactory.buildGetRequest(frontpageRequest);
			com.google.api.client.http.HttpResponse response = request.execute();
			if (response.getStatusCode() == HTTP_UNAUTHORIZED) {
				throw new DiffbotUnauthorizedException("Not authorized API token.");
			} else if (response.getStatusCode() != HTTP_OK) {
				throw new DiffbotServerException(response.getStatusCode(), response.getStatusMessage());
			}
			if (response.getContentType() != null && !response.getContentType().isEmpty()
					&& response.getContentType().contains("application/json")) {
				GenericJson error = response.parseAs(GenericJson.class);

				BigDecimal errorCode = (BigDecimal) error.get("statusCode");
				throw new DiffbotAPIException(errorCode.intValue(), ((String) error.get("message")));
			}
			return (Frontpage) createUnmarshaller().unmarshal(response.getContent());
		} catch (JAXBException e) {
			throw new DiffbotParseException("The DML response from Diffbot cannot be parsed.", e);
		} catch (IOException e) {
			throw new DiffbotIOException(e.getMessage(), e);
		}
	}

	/**
	 * Add an {@code FrontpageRequest} to the batch list.
	 * 
	 * @param frontpageRequest the {@code FrontpageRequest} to add to the list
	 * @return a {@code Future<Frontpage>} that will be filled with an {@code Frontpage} on the next batch call
	 */
	public final Future<Frontpage> batch(final FrontpageRequest frontpageRequest) {
		Future<Frontpage> future = new Future<Frontpage>(frontpageRequest, this);
		synchronized (futures) {
			futures.add(future);
		}
		return future;
	}

	/**
	 * Add an {@code ArticleRequest} to the batch list.
	 * 
	 * @param articleRequest the {@code ArticleRequest} to add to the list
	 * @return a {@code Future<Article>} that will be filled with an {@code Article} on the next batch call
	 */
	public final Future<Article> batch(final ArticleRequest articleRequest) {
		Future<Article> future = new Future<Article>(articleRequest, this);
		synchronized (futures) {
			futures.add(future);
		}
		return future;
	}

	protected final void executeBatch(final Future<? extends Object> initiator) throws DiffbotUnauthorizedException,
			DiffbotServerException, DiffbotIOException {
		if (futures.contains(initiator)) {
			@SuppressWarnings("rawtypes")
			List<Future> batchList = new ArrayList<Future>();
			synchronized (futures) {
				batchList.add(initiator);
				futures.remove(initiator);
				int batchSize = (futures.size() > MAX_BATCH_REQUEST - 1) ? MAX_BATCH_REQUEST - 1 : futures.size();
				@SuppressWarnings("rawtypes")
				List<Future> subList = futures.subList(0, batchSize);
				batchList.addAll(subList);
				subList.clear();
			}
			BatchResponse[] responses;
			try {
				responses = executeBatchRequest(batchList);
			} catch (DiffbotUnauthorizedException | DiffbotServerException | DiffbotIOException e) {
				synchronized (futures) {
					futures.addAll(batchList);
				}
				throw e;
			}
			parseBatchResponses(responses, batchList);
		}
	}

	/**
	 * Parse the batch responses and fill the {@code Future}s that initiated the batch request with the answer or error
	 * from every sub-answers.
	 * <p>
	 * <b><strong>WARNING:strong</strong> Diffbot doesn't provide any way to match the sub-request in a batch with the
	 * sub-results (like an id shared by a sub-request and a sub-answer). The SDK assumes Diffbot answers a batch
	 * request with one answer for <strong>every</strong> sub-request and keep the sub-answers in the same order as the
	 * sub-requests.</b>
	 * 
	 * @param responses the array of responses from the batch API
	 * @param results the {@code List<Future>} that initiated the batch call. Will be filled with the answers.
	 */
	@SuppressWarnings("unchecked")
	private void parseBatchResponses(final BatchResponse[] responses,
			@SuppressWarnings("rawtypes") final List<Future> results) {
		for (int i = 0; i < responses.length; i++) {
			String responseContent = responses[i].getBody();
			if (results.get(i).getRequest().getApiType() == ApiType.ARTICLE) {
				try {
					Article article = jsonFactory.createJsonParser(responseContent).parseAndClose(Article.class, null);
					results.get(i).setResult(article).setExecuted(true);
					BigDecimal errorCode = (BigDecimal) article.get("errorCode");
					if (errorCode != null) {
						throw new DiffbotAPIException(errorCode.intValue(), (String) article.get("error"));
					}
				} catch (IOException e) {
					results.get(i)
							.setError(
									new DiffbotParseException(
											"The JSON Article response from Diffbot cannot be parsed.", e))
							.setExecuted(true);
				} catch (DiffbotAPIException e) {
					results.get(i).setError(e).setExecuted(true);
				}
			} else if (results.get(i).getRequest().getApiType() == ApiType.FRONTPAGE) {
				try {

					Header contentType = responses[i].getFirstHeader("Content-Type");
					if (contentType != null && contentType.getValue() != null
							&& contentType.getValue().contains("application/json")) {
						GenericJson error = jsonFactory.createJsonParser(responseContent).parseAndClose(
								GenericJson.class, null);
						BigDecimal errorCode = (BigDecimal) error.get("statusCode");
						throw new DiffbotAPIException(errorCode.intValue(), ((String) error.get("message")));
					}
					Frontpage frontpage = (Frontpage) createUnmarshaller().unmarshal(new StringReader(responseContent));
					results.get(i).setResult(frontpage).setExecuted(true);
				} catch (JAXBException e) {
					results.get(i)
							.setError(
									new DiffbotParseException(
											"The DML Frontpage response from Diffbot cannot be parsed.", e))
							.setExecuted(true);
				} catch (IOException e) {

					results.get(i)
							.setError(
									new DiffbotParseException(
											"The Frontpage API returned an error and the JSON error message cannot be parsed",
											e)).setExecuted(true);
				} catch (DiffbotAPIException e) {
					results.get(i).setError(e).setExecuted(true);
				}
			}
		}
	}

	private BatchResponse[] executeBatchRequest(@SuppressWarnings("rawtypes") final List<Future> futureRequests)
			throws DiffbotUnauthorizedException, DiffbotServerException, DiffbotIOException {
		List<BatchRequest> requests = new ArrayList<BatchRequest>();
		GenericUrl batchUrl = new GenericUrl(API_SERVER + BATCH_URI).set("token", this.token);
		for (Future<? extends Object> future : futureRequests) {
			requests.add(new BatchRequest("GET", future.getRequest().buildRelativeUrl()));
		}
		try {
			batchUrl.set("batch", jsonFactory.toString(requests));
			HttpRequest request = requestFactory.buildPostRequest(batchUrl, null);
			com.google.api.client.http.HttpResponse response = request.execute();
			if (response.getStatusCode() == HTTP_UNAUTHORIZED) {
				throw new DiffbotUnauthorizedException("Not authorized API token.");
			} else if (response.getStatusCode() != HTTP_OK) {
				throw new DiffbotServerException(response.getStatusCode(), response.getStatusMessage());
			}
			return response.parseAs(BatchResponse[].class);
		} catch (IOException e) {
			throw new DiffbotIOException(e.getMessage(), e);
		}

	}

	private Unmarshaller createUnmarshaller() {
		try {
			return jc.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(
					"Cannot instantiate a JAXB Unmarshaller. Please verify that JAXB API is in the classpath", e);
		}
	}

}
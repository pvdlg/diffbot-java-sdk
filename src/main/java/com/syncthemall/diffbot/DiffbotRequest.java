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

import static com.syncthemall.diffbot.Constants.BATCH;
import static com.syncthemall.diffbot.Constants.BATCH_URL;
import static com.syncthemall.diffbot.Constants.ERROR;
import static com.syncthemall.diffbot.Constants.GET;
import static com.syncthemall.diffbot.Constants.HTTP_OK;
import static com.syncthemall.diffbot.Constants.HTTP_UNAUTHORIZED;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.GenericJson;
import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotBatchException;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.exception.DiffbotParseException;
import com.syncthemall.diffbot.exception.DiffbotServerException;
import com.syncthemall.diffbot.exception.DiffbotUnauthorizedException;
import com.syncthemall.diffbot.exception.JAXBInitializationException;
import com.syncthemall.diffbot.exception.UnknownRequestAPITypeException;
import com.syncthemall.diffbot.model.Model;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.batch.BatchRequest;
import com.syncthemall.diffbot.model.batch.BatchResponse;
import com.syncthemall.diffbot.model.classifier.Classified;
import com.syncthemall.diffbot.model.frontpage.Frontpage;
import com.syncthemall.diffbot.model.images.Images;
import com.syncthemall.diffbot.model.products.Products;

/**
 * Parent class used to creates request to the Diffbot API.
 * 
 * @param <T> The type of {@code Model} this request returns.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public abstract class DiffbotRequest<T extends Model> {

	private Diffbot client;
	private ResourceBundle bundle = ResourceBundle.getBundle("com.syncthemall.diffbot.messages.Messages");
	private Class<T> responseClass;
	private int maxBatchRequest;
	private int readTimeout = 20000;
	private int batchRequestTimeout;
	private int concurrentBatchRequest;
	private GenericUrl url;

	/**
	 * Diffbot API type.
	 */
	public enum ApiType {
		/** Article API. **/
		ARTICLE,
		/** Frontpage API. **/
		FRONTPAGE,
		/** Image API. **/
		IMAGE,
		/** Product API. **/
		PRODUCT,
		/** Classifier API. **/
		CLASSIFIER
	};

	/**
	 * Default constructor
	 * 
	 * @param client Diffbot client
	 * @param responseClass response class to parse into
	 * @param encodedUrl encoded URL, including any existing query parameters that should be parsed
	 */
	public DiffbotRequest(final Diffbot client, final Class<T> responseClass, final String encodedUrl) {
		super();
		this.client = client;
		this.responseClass = responseClass;
		this.maxBatchRequest = client.getMaxBatchRequest();
		this.batchRequestTimeout = client.getBatchRequestTimeout();
		this.concurrentBatchRequest = client.getConcurrentBatchRequest();
		this.url = new GenericUrl(encodedUrl);
	}

	/**
	 * @return the type of API used by this request.
	 */
	protected abstract ApiType getApiType();

	private String buildRelativeUrl() {
		return this.url.buildRelativeUrl();
	}

	protected final GenericUrl set(final String fieldName, final Object value) {
		return url.set(fieldName, value);
	}

	/**
	 * Sends the metadata request to the server and returns the parsed metadata response.
	 * 
	 * <p>
	 * Subclasses may override by calling the super implementation.
	 * </p>
	 * 
	 * @return the {@code Model} representing the Diffbot API parsed HTTP response
	 * @throws DiffbotUnauthorizedException if the developer token is not recognized or revoked
	 * @throws DiffbotServerException if a HTTP error occurs on the Diffbot server
	 * @throws DiffbotAPIException if an API error occur on Diffbot servers while processing the request
	 * @throws DiffbotParseException if the Diffbot response cannot be parsed
	 * @throws DiffbotException for any other unknown errors. This is also a superclass of all other Diffbot exceptions,
	 *             so you may want to only catch this exception if not interested in the cause of the error.
	 */
	public final T execute() throws DiffbotException {
		try {
			HttpRequest request = client.getRequestFactory().buildGetRequest(this.url);
			request.setReadTimeout(readTimeout);
			HttpResponse response = request.execute();
			// Specific case for Frontpage has the response is either in JSON (error case) or XML (success case)
			if (responseClass.equals(Frontpage.class)) {
				return parseFrontpage(response);
			} else {
				return parseModel(response);
			}
		} catch (HttpResponseException e) {
			parseAPIError(e.getStatusCode(), e.getContent());
			return null;
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("request.not.executed"), e);
		}
	}

	@SuppressWarnings("unchecked")
	private T parseFrontpage(final HttpResponse response) throws DiffbotAPIException, DiffbotParseException,
			DiffbotServerException {
		try {
			return (T) createUnmarshaller().unmarshal(response.getContent());
		} catch (JAXBException e) {
			throw new DiffbotParseException(bundle.getString("dml.not.parsed"), e);
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("response.not.read"), e);
		}
	}

	private T parseModel(final HttpResponse response) throws DiffbotAPIException, DiffbotParseException {
		try {
			return response.parseAs(responseClass);
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("response.not.parsed"), e);
		}
	}

	/**
	 * Add this {@code Request} to the batch list.
	 * 
	 * @return a {@code Future<T>} that will be filled with the result {@code Model} on the next batch call
	 */
	public final Future<T> queue() {
		Future<T> future = new Future<T>(this, client);
		synchronized (client.getFutures()) {
			client.getFutures().add(future);
		}
		return future;
	}

	/**
	 * Run the batch request with all the requests in the queue and fill the {@code Future}s that initiated the batch
	 * request with the answer or error from every sub-answers.
	 * <p>
	 * <b><strong>WARNING:strong</strong> Diffbot doesn't provide any way to match the sub-request in a batch with the
	 * sub-results (like an id shared by a sub-request and a sub-answer). The SDK assumes Diffbot answers a batch
	 * request with one answer for <strong>every</strong> sub-request and keep the sub-answers in the same order as the
	 * sub-requests.</b>
	 * 
	 * @param initiator the{@code Future} that initiated the processing of the batch
	 * @throws DiffbotBatchException wraps any error that occurs during the batch API call
	 */
	protected final void runBatch(final Future<T> initiator) throws DiffbotBatchException {
		if (concurrentBatchRequest > 1) {
			runAsyncBatch(initiator);
		} else {
			runSyncBatch(initiator);
		}
	}

	private void runSyncBatch(final Future<T> initiator) throws DiffbotBatchException {
		if (client.getFutures().contains(initiator)) {
			List<Future<? extends Model>> batchList = new ArrayList<Future<? extends Model>>();
			synchronized (client.getFutures()) {
				batchList.add(initiator);
				client.getFutures().remove(initiator);
				int batchSize = (client.getFutures().size() > maxBatchRequest - 1) ? maxBatchRequest - 1 : client
						.getFutures().size();

				List<Future<? extends Model>> subList = client.getFutures().subList(0, batchSize);
				batchList.addAll(subList);
				subList.clear();
			}
			try {
				HttpResponse response = executeBatchRequest(batchList);
				BatchResponse[] responses = parseBatchResponse(response);
				parseBatchSubResponses(responses, batchList);
			} catch (DiffbotUnauthorizedException | DiffbotServerException | DiffbotParseException
					| DiffbotAPIException e) {
				synchronized (client.getFutures()) {
					client.getFutures().addAll(batchList);
				}
				throw new DiffbotBatchException(e);
			}
		}
	}

	private void runAsyncBatch(final Future<T> initiator) throws DiffbotBatchException {
		List<List<Future<? extends Model>>> batchCalls = new ArrayList<List<Future<? extends Model>>>();
		int callsToMake = 0;
		synchronized (client.getFutures()) {
			client.getFutures().remove(initiator);
			int batchSize = (client.getFutures().size() > maxBatchRequest - 1) ? maxBatchRequest - 1 : client
					.getFutures().size();
			List<Future<? extends Model>> subList = client.getFutures().subList(0, batchSize);
			subList.add(initiator);
			List<Future<? extends Model>> batchList = new ArrayList<Future<? extends Model>>();
			batchList.addAll(subList);
			batchCalls.add(batchList);
			callsToMake++;
			subList.clear();
			while (!client.getFutures().isEmpty() && callsToMake < concurrentBatchRequest) {
				batchSize = (client.getFutures().size() > maxBatchRequest) ? maxBatchRequest : client.getFutures()
						.size();
				subList = client.getFutures().subList(0, batchSize);
				batchList = new ArrayList<Future<? extends Model>>();
				batchList.addAll(subList);
				batchCalls.add(batchList);
				callsToMake++;
				subList.clear();
			}
		}

		DiffbotBatchException errorOnInitiator = null;
		Map<List<Future<? extends Model>>, java.util.concurrent.Future<HttpResponse>> asyncResponses = new HashMap<List<Future<? extends Model>>, java.util.concurrent.Future<HttpResponse>>();
		for (List<Future<? extends Model>> call : batchCalls) {
			try {
				asyncResponses.put(call, executeBatchAsyncRequest(call));
			} catch (DiffbotServerException | DiffbotParseException e) {
				synchronized (client.getFutures()) {
					client.getFutures().addAll(call);
				}
				if (call.contains(initiator)) {
					errorOnInitiator = new DiffbotBatchException(e);
				}
			}
		}

		for (Entry<List<Future<? extends Model>>, java.util.concurrent.Future<HttpResponse>> entry : asyncResponses
				.entrySet()) {
			try {
				BatchResponse[] responses = parseBatchResponse(entry.getValue().get());
				parseBatchSubResponses(responses, entry.getKey());
			} catch (DiffbotParseException | InterruptedException e) {
				synchronized (client.getFutures()) {
					client.getFutures().addAll(entry.getKey());
				}
				if (entry.getKey().contains(initiator)) {
					errorOnInitiator = new DiffbotBatchException(e);
				}
			} catch (ExecutionException e) {
				synchronized (client.getFutures()) {
					client.getFutures().addAll(entry.getKey());
				}
				if (entry.getKey().contains(initiator)) {
					if (e.getCause() instanceof HttpResponseException) {
						try {
							parseAPIError(((HttpResponseException) e.getCause()).getStatusCode(),
									((HttpResponseException) e.getCause()).getContent());
						} catch (DiffbotUnauthorizedException | DiffbotParseException | DiffbotAPIException e1) {
							errorOnInitiator = new DiffbotBatchException(e1);
						}
					} else {
						errorOnInitiator = new DiffbotBatchException(e.getCause());
					}
				}
			}
		}
		if (errorOnInitiator != null) {
			throw errorOnInitiator;
		}
	}

	private HttpResponse executeBatchRequest(final Collection<Future<? extends Model>> futureRequests)
			throws DiffbotParseException, DiffbotServerException, DiffbotUnauthorizedException, DiffbotAPIException {
		try {
			HttpRequest request = client.getRequestFactory().buildPostRequest(buildBatchRequest(futureRequests), null);
			request.setReadTimeout(batchRequestTimeout);
			return request.execute();
		} catch (HttpResponseException e) {
			parseAPIError(e.getStatusCode(), e.getContent());
			return null;
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("request.not.executed"), e);
		}
	}

	private java.util.concurrent.Future<HttpResponse> executeBatchAsyncRequest(
			final Collection<Future<? extends Model>> futureRequests) throws DiffbotParseException,
			DiffbotServerException {
		try {
			HttpRequest request = client.getRequestFactory().buildPostRequest(buildBatchRequest(futureRequests), null);
			request.setReadTimeout(batchRequestTimeout);
			return request.executeAsync();
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("request.not.executed"), e);
		}
	}

	private GenericUrl buildBatchRequest(final Collection<Future<? extends Model>> futureRequests)
			throws DiffbotParseException {
		List<BatchRequest> requests = new ArrayList<BatchRequest>();
		GenericUrl batchUrl = new GenericUrl(BATCH_URL);
		for (Future<? extends Object> future : futureRequests) {
			requests.add(new BatchRequest(GET, future.getRequest().buildRelativeUrl()));
		}
		batchUrl.set(BATCH, buildBatchQuery(requests));
		return batchUrl;
	}

	private String buildBatchQuery(final List<BatchRequest> requests) throws DiffbotParseException {
		try {
			return client.getJsonFactory().toString(requests);
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("batch.request.not.built"), e);
		}
	}

	private BatchResponse[] parseBatchResponse(final HttpResponse response) throws DiffbotParseException {
		try {
			return response.parseAs(BatchResponse[].class);
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("response.not.parsed"), e);
		}
	}

	private void parseBatchSubResponses(final BatchResponse[] responses, final List<Future<? extends Model>> results) {
		for (int i = 0; i < responses.length; i++) {
			for (Future<? extends Model> future : results) {
				if (future.getRequest().buildRelativeUrl().equals(responses[i].getRelativeUrl())
						&& !future.isExecuted()) {
					Future<? extends Model> result = future;
					try {
						if (responses[i].getCode() != HTTP_OK) {
							throw new DiffbotAPIException(responses[i].getCode(), responses[i].getBody());
						}
						if (result.getRequest().getApiType() != ApiType.FRONTPAGE) {
							parseModelBatchResponses(responses[i], result);
						} else {
							parseFrontpageBatchResponses(responses[i], result);
						}
					} catch (DiffbotAPIException | DiffbotParseException e) {
						result.setError(e);
					}
				}
			}
		}
	}

	private void parseModelBatchResponses(final BatchResponse response, final Future<? extends Model> result)
			throws DiffbotParseException, DiffbotAPIException {
		try {
			Model model;
			if (result.getRequest().getApiType() == ApiType.ARTICLE) {
				model = client.getJsonFactory().createJsonParser(response.getBody()).parseAndClose(Article.class, null);
			} else if (result.getRequest().getApiType() == ApiType.IMAGE) {
				model = client.getJsonFactory().createJsonParser(response.getBody()).parseAndClose(Images.class, null);
			} else if (result.getRequest().getApiType() == ApiType.PRODUCT) {
				model = client.getJsonFactory().createJsonParser(response.getBody())
						.parseAndClose(Products.class, null);
			} else if (result.getRequest().getApiType() == ApiType.CLASSIFIER) {
				model = client.getJsonFactory().createJsonParser(response.getBody())
						.parseAndClose(Classified.class, null);
			} else {
				throw new UnknownRequestAPITypeException(bundle.getString("batch.unknow.type"), result.getRequest()
						.getApiType());
			}
			result.setResult(model);
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("model.not.parsed"), e);
		}
	}

	private void parseFrontpageBatchResponses(final BatchResponse response, final Future<? extends Model> result)
			throws DiffbotAPIException, DiffbotParseException {
		try {
			Frontpage frontpage = (Frontpage) createUnmarshaller().unmarshal(new StringReader(response.getBody()));
			result.setResult(frontpage);
		} catch (JAXBException e) {
			throw new DiffbotParseException(bundle.getString("dml.not.parsed"), e);
		}
	}

	private Unmarshaller createUnmarshaller() {
		try {
			return client.getJAXBContext().createUnmarshaller();
		} catch (JAXBException e) {
			throw new JAXBInitializationException(bundle.getString("jaxb.not.instanciated"), e);
		}
	}

	private void parseAPIError(final int statusCode, final String responseContent) throws DiffbotUnauthorizedException,
			DiffbotParseException, DiffbotAPIException {
		if (statusCode == HTTP_UNAUTHORIZED) {
			throw new DiffbotUnauthorizedException(bundle.getString("token.not.authorized"));
		} else {
			try {
				throw new DiffbotAPIException(statusCode, client.getJsonFactory().createJsonParser(responseContent)
						.parse(GenericJson.class).get(ERROR).toString());
			} catch (IOException e) {
				throw new DiffbotParseException(bundle.getString("error.not.parsed"), e);
			}
		}
	}

	/**
	 * @param readTimeout the timeout of the request (the default value is 20 seconds) or {@code 0} for an infinite
	 *            timeout
	 */
	protected final void setReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
	}

}

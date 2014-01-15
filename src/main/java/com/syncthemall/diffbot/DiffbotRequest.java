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

import static com.syncthemall.diffbot.Constants.APPLICATION_JSON;
import static com.syncthemall.diffbot.Constants.BATCH;
import static com.syncthemall.diffbot.Constants.BATCH_URL;
import static com.syncthemall.diffbot.Constants.CONTENT_TYPE;
import static com.syncthemall.diffbot.Constants.ERROR;
import static com.syncthemall.diffbot.Constants.ERROR_CODE;
import static com.syncthemall.diffbot.Constants.GET;
import static com.syncthemall.diffbot.Constants.HTTP_OK;
import static com.syncthemall.diffbot.Constants.HTTP_UNAUTHORIZED;
import static com.syncthemall.diffbot.Constants.MESSAGE;
import static com.syncthemall.diffbot.Constants.STATUS_CODE;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.syncthemall.diffbot.exception.DiffbotAPIException;
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
public abstract class DiffbotRequest<T extends Model> extends GenericUrl {

	private ResourceBundle bundle = ResourceBundle.getBundle("com.syncthemall.diffbot.messages.Messages");
	private HttpRequestFactory requestFactory;
	private List<Future<? extends Model>> futures;
	private JAXBContext jc;
	private JsonFactory jsonFactory;
	private Class<T> responseClass;
	private int maxBatchRequest = 20;
	private int batchRequestTimeout = 360000;
	private int readTimeout = 20000;

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
		super(encodedUrl);
		this.requestFactory = client.getRequestFactory();
		this.jc = client.getJAXBContext();
		this.jsonFactory = client.getJsonFactory();
		this.futures = client.getFutures();
		this.responseClass = responseClass;
		this.maxBatchRequest = client.getMaxBatchRequest();
		this.batchRequestTimeout = client.getBatchRequestTimeout();
	}

	/**
	 * @return the type of API used by this request.
	 */
	protected abstract ApiType getApiType();

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
			HttpRequest request = requestFactory.buildGetRequest(this);
			request.setReadTimeout(readTimeout);
			HttpResponse response = request.execute();
			// Specific case for Frontpage has the response is either in JSON (error case) or XML (success case)
			if (responseClass.equals(Frontpage.class)) {
				return executeFrontpage(response);
			} else {
				return executeModel(response);
			}
		} catch (HttpResponseException e) {
			if (e.getStatusCode() == HTTP_UNAUTHORIZED) {
				throw new DiffbotUnauthorizedException(bundle.getString("token.not.authorized"));
			} else {
				throw new DiffbotServerException(e.getStatusCode(), e.getStatusMessage());
			}
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("request.not.executed"), e);
		}
	}

	@SuppressWarnings("unchecked")
	private T executeFrontpage(final HttpResponse response) throws DiffbotAPIException, DiffbotParseException,
			DiffbotServerException {
		if (response.getContentType() != null && !response.getContentType().isEmpty()
				&& response.getContentType().contains(APPLICATION_JSON)) {
			GenericJson error;
			try {
				error = response.parseAs(GenericJson.class);
				BigDecimal errorCode = (BigDecimal) error.get(STATUS_CODE);
				throw new DiffbotAPIException(errorCode.intValue(), (String) error.get(MESSAGE));
			} catch (IOException e) {
				throw new DiffbotParseException(bundle.getString("frontpage.error.not.parsed"), e);
			}
		}
		try {
			return (T) createUnmarshaller().unmarshal(response.getContent());
		} catch (JAXBException e) {
			throw new DiffbotParseException(bundle.getString("dml.not.parsed"), e);
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("response.not.read"), e);
		}
	}

	@SuppressWarnings("unchecked")
	private T executeModel(final HttpResponse response) throws DiffbotAPIException, DiffbotParseException {
		GenericJson result;
		try {
			result = (GenericJson) response.parseAs(responseClass);
			BigDecimal errorCode = (BigDecimal) result.get(ERROR_CODE);
			if (errorCode != null) {
				throw new DiffbotAPIException(errorCode.intValue(), (String) result.get(ERROR));
			}
			return (T) result;
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
		Future<T> future = new Future<T>(this);
		synchronized (futures) {
			futures.add(future);
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
	 * @throws DiffbotUnauthorizedException if the developer token is not recognized or revoked
	 * @throws DiffbotServerException if a HTTP error occurs on the Diffbot server
	 * @throws DiffbotParseException if the Diffbot response cannot be parsed
	 */
	protected final void runBatch(final Future<T> initiator) throws DiffbotUnauthorizedException,
			DiffbotServerException, DiffbotParseException {
		if (futures.contains(initiator)) {
			List<Future<? extends Model>> batchList = new ArrayList<Future<? extends Model>>();
			synchronized (futures) {
				batchList.add(initiator);
				futures.remove(initiator);
				int batchSize = (futures.size() > maxBatchRequest - 1) ? maxBatchRequest - 1 : futures.size();

				List<Future<? extends Model>> subList = futures.subList(0, batchSize);
				batchList.addAll(subList);
				subList.clear();
			}
			try {
				BatchResponse[] responses = executeBatchRequest(batchList);
				parseBatchResponses(responses, batchList);
			} catch (DiffbotUnauthorizedException | DiffbotServerException e) {
				synchronized (futures) {
					futures.addAll(batchList);
				}
				throw e;
			}
		}
	}

	private BatchResponse[] executeBatchRequest(final Collection<Future<? extends Model>> futureRequests)
			throws DiffbotUnauthorizedException, DiffbotServerException, DiffbotParseException {
		List<BatchRequest> requests = new ArrayList<BatchRequest>();
		GenericUrl batchUrl = new GenericUrl(BATCH_URL);
		for (Future<? extends Object> future : futureRequests) {
			requests.add(new BatchRequest(GET, future.getRequest().buildRelativeUrl()));
		}
		batchUrl.set(BATCH, buildBatchQuery(requests));
		try {
			HttpRequest request = requestFactory.buildPostRequest(batchUrl, null);
			request.setReadTimeout(batchRequestTimeout);
			HttpResponse response = request.execute();
			if (response.getStatusCode() == HTTP_UNAUTHORIZED) {
				throw new DiffbotUnauthorizedException(bundle.getString("token.not.authorized"));
			} else if (response.getStatusCode() != HTTP_OK) {
				throw new DiffbotServerException(response.getStatusCode(), response.getStatusMessage());
			}
			return parseBatchResponse(response);
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("request.not.executed"), e);
		}
	}

	private String buildBatchQuery(final List<BatchRequest> requests) throws DiffbotParseException {
		try {
			return jsonFactory.toString(requests);
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("batch.request.not.built"), e);
		}
	}

	private BatchResponse[] parseBatchResponse(final HttpResponse response) throws DiffbotServerException {
		try {
			return response.parseAs(BatchResponse[].class);
		} catch (IOException e) {
			throw new DiffbotServerException(bundle.getString("response.not.parsed"), e);
		}
	}

	private void parseBatchResponses(final BatchResponse[] responses, final List<Future<? extends Model>> results) {
		for (int i = 0; i < responses.length; i++) {
			String responseContent = responses[i].getBody();
			for (Future<? extends Model> future : results) {
				if (future.getRequest().buildRelativeUrl().equals(responses[i].getRelativeUrl())
						&& !future.isExecuted()) {
					Future<? extends Model> result = future;
					try {
						if (responses[i].getCode() != HTTP_OK) {
							throw new DiffbotServerException(responses[i].getCode(), responseContent);
						}
						if (responses[i].getHeaders().length == 0) {
							throw new DiffbotParseException(bundle.getString("batch.response.no.header"));
						}
						if (result.getRequest().getApiType() != ApiType.FRONTPAGE) {
							parseModelBatchResponses(responses[i], result);
						} else {
							parseFrontpageBatchResponses(responses[i], result);
						}
					} catch (DiffbotAPIException | DiffbotServerException | DiffbotParseException e) {
						result.setError(e).setExecuted(true);
					}
				}
			}
		}
	}

	private void parseModelBatchResponses(final BatchResponse response, final Future<? extends Model> result)
			throws DiffbotParseException, DiffbotAPIException {
		if (response.getFirstHeader(CONTENT_TYPE) == null || response.getFirstHeader(CONTENT_TYPE).getValue() == null) {
			throw new DiffbotParseException(bundle.getString("batch.response.no.contenttype"));
		}
		try {
			Model model;
			if (result.getRequest().getApiType() == ApiType.ARTICLE) {
				model = jsonFactory.createJsonParser(response.getBody()).parseAndClose(Article.class, null);
			} else if (result.getRequest().getApiType() == ApiType.IMAGE) {
				model = jsonFactory.createJsonParser(response.getBody()).parseAndClose(Images.class, null);
			} else if (result.getRequest().getApiType() == ApiType.PRODUCT) {
				model = jsonFactory.createJsonParser(response.getBody()).parseAndClose(Products.class, null);
			} else if (result.getRequest().getApiType() == ApiType.CLASSIFIER) {
				model = jsonFactory.createJsonParser(response.getBody()).parseAndClose(Classified.class, null);
			} else {
				throw new UnknownRequestAPITypeException(bundle.getString("batch.unknow.type"), result.getRequest()
						.getApiType());
			}
			result.setResult(model).setExecuted(true);
			BigDecimal errorCode = (BigDecimal) model.get(ERROR_CODE);
			if (errorCode != null) {
				throw new DiffbotAPIException(errorCode.intValue(), (String) model.get(ERROR));
			}
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("model.not.parsed"), e);
		}
	}

	private void parseFrontpageBatchResponses(final BatchResponse response, final Future<? extends Model> result)
			throws DiffbotAPIException, DiffbotParseException {
		try {
			if (response.getFirstHeader(CONTENT_TYPE) != null
					&& response.getFirstHeader(CONTENT_TYPE).getValue() != null
					&& response.getFirstHeader(CONTENT_TYPE).getValue().contains(APPLICATION_JSON)) {
				GenericJson error = jsonFactory.createJsonParser(response.getBody()).parseAndClose(GenericJson.class,
						null);
				BigDecimal errorCode = (BigDecimal) error.get(STATUS_CODE);
				throw new DiffbotAPIException(errorCode.intValue(), (String) error.get(MESSAGE));
			}
			Frontpage frontpage = (Frontpage) createUnmarshaller().unmarshal(new StringReader(response.getBody()));
			result.setResult(frontpage).setExecuted(true);
		} catch (JAXBException e) {
			throw new DiffbotParseException(bundle.getString("dml.not.parsed"), e);
		} catch (IOException e) {
			throw new DiffbotParseException(bundle.getString("frontpage.error.not.parsed"), e);
		}
	}

	private Unmarshaller createUnmarshaller() {
		try {
			return jc.createUnmarshaller();
		} catch (JAXBException e) {
			throw new JAXBInitializationException(bundle.getString("jaxb.not.instanciated"), e);
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

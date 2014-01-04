/**
 * The MIT License
 * Copyright (c) ${year} Pierre-Denis Vanduynslager
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
package com.syncthemall.diffbot.api;

import java.io.Serializable;

import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.exception.DiffbotIOException;
import com.syncthemall.diffbot.exception.DiffbotParseException;
import com.syncthemall.diffbot.exception.DiffbotServerException;
import com.syncthemall.diffbot.exception.DiffbotUnauthorizedException;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.frontpage.Frontpage;

/**
 * Wrapper used with the Diffbot batch API. Represents a future response from Diffbot.
 * 
 * A {@code Future} contains :
 * <ul>
 * <li>A {@code Request} used to call the Diffbot API and obtain a result</li>
 * <li>A callback reference to the {@code DiffbotAPI} that created this {@code Future}. It is used to trigger the batch
 * API call.</li>
 * <li>A {@code T} result representing the result of the call corresponding to the {@code Request}.</li>
 * <li>A {@code DiffbotException} if the call resulted in an error. If an {@code DiffbotException} exist it will be
 * thrown when trying to access the result.</li>
 * </ul>
 * 
 * @param <T> the type of Diffbot API result, either {@link Article} or {@link Frontpage}
 */
public class Future<T extends Object> implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 6079999701417072567L;
	private Request request;
	private T result;
	private DiffbotException error;

	private DiffbotAPI callback;

	private boolean executed = false;

	/**
	 * Default constructor.
	 * 
	 * @param request {@code Request} used to call the Diffbot API and obtain a result
	 * @param callback a reference to the {@code DiffbotAPI} that created this {@code Future}
	 */
	protected Future(final Request request, final DiffbotAPI callback) {
		super();
		this.request = request;
		this.callback = callback;
	}

	protected final Request getRequest() {
		return request;
	}

	protected final Future<T> setResult(final T result) {
		this.result = result;
		return this;
	}

	protected final Future<T> setExecuted(final boolean executed) {
		this.executed = executed;
		return this;
	}

	protected final Future<T> setError(final DiffbotException error) {
		this.error = error;
		return this;
	}

	/**
	 * If a batch request containing the {@code Request} of this {@code Future} has already been executed, return the
	 * result obtained. If not, executes the batch request and returns the result for the {@code Request} of this
	 * {@code Future}.
	 * 
	 * @return the {@code Article} or {@code Frontpage} obtained for this {@code Future}
	 * @throws DiffbotAPIException
	 * @throws DiffbotIOException
	 * @throws DiffbotParseException
	 * 
	 * @throws DiffbotUnauthorizedException if the developer token is not recognized or revoked
	 * @throws DiffbotServerException if a HTTP error occurs on the Diffbot server
	 * @throws DiffbotIOException if an IO error (usually network related) occur during the API call
	 * @throws DiffbotAPIException if an API error occur on Diffbot servers while processing the request
	 */
	public final T get() throws DiffbotAPIException, DiffbotIOException, DiffbotUnauthorizedException,
			DiffbotServerException, DiffbotParseException {
		if (!executed) {
			callback.executeBatch(this);
		}
		if (error != null) {
			if (error instanceof DiffbotAPIException) {
				throw (DiffbotAPIException) error;
			} else if (error instanceof DiffbotIOException) {
				throw (DiffbotIOException) error;
			} else if (error instanceof DiffbotParseException) {
				throw (DiffbotParseException) error;
			} else if (error instanceof DiffbotServerException) {
				throw (DiffbotServerException) error;
			} else if (error instanceof DiffbotUnauthorizedException) {
				throw (DiffbotUnauthorizedException) error;
			}
		}
		return result;
	}

}

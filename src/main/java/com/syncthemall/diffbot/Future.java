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

import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotBatchException;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.exception.DiffbotParseException;
import com.syncthemall.diffbot.exception.DiffbotServerException;
import com.syncthemall.diffbot.exception.DiffbotUnauthorizedException;
import com.syncthemall.diffbot.model.Model;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.frontpage.Frontpage;

/**
 * Wrapper used with the Diffbot batch API. Represents a future response from Diffbot.
 * 
 * A {@code Future} contains :
 * <ul>
 * <li>A {@code Request} used to call the Diffbot API and obtain a result. It is also used to trigger the batch API call
 * </li>
 * <li>A {@code T} result representing the result of the call corresponding to the {@code Request}.</li>
 * <li>A {@code DiffbotException} if the call resulted in an error. If an {@code DiffbotException} exist it will be
 * thrown when trying to access the result.</li>
 * </ul>
 * 
 * @param <T> the type of Diffbot API result, ie {@link Article} or {@link Frontpage}
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class Future<T extends Model> {

	private Diffbot client;
	private DiffbotRequest<T> request;
	private Model result;
	private DiffbotException error;
	private boolean executed = false;

	/**
	 * Default constructor.
	 * 
	 * @param request {@code Request} used to call the Diffbot API and obtain a result
	 * @param client the {@code Diffbot} client that initiated this {@code Future}
	 */
	protected Future(final DiffbotRequest<T> request, final Diffbot client) {
		super();
		this.request = request;
		this.client = client;
	}

	protected final DiffbotRequest<T> getRequest() {
		return request;
	}

	protected final Future<T> setResult(final Model result) {
		this.result = result;
		this.executed = true;
		return this;
	}

	protected final Future<T> setError(final DiffbotException error) {
		this.error = error;
		this.executed = true;
		return this;
	}

	protected final boolean isExecuted() {
		return executed;
	}

	/**
	 * If a batch request containing the {@code Request} of this {@code Future} has already been executed, return the
	 * result obtained. If not, executes the batch request and returns the result for the {@code Request} of this
	 * {@code Future}.
	 * 
	 * @return the {@code Article} or {@code Frontpage} obtained for this {@code Future}
	 * @throws DiffbotBatchException if an error happens in the batch request call itself (wraps any error that occurs
	 *             during the batch API call)
	 * @throws DiffbotUnauthorizedException if the developer token is not recognized or revoked for the sub-request
	 *             associated to this {@code Future}
	 * @throws DiffbotServerException if a HTTP error occurs on the Diffbot server for the sub-request associated to
	 *             this {@code Future}
	 * @throws DiffbotAPIException if an API error occur on Diffbot servers while processing the request for the
	 *             sub-request associated to this {@code Future}
	 * @throws DiffbotException for any other unknown errors. This is also a superclass of all other Diffbot exceptions,
	 *             so you may want to only catch this exception if not interested in the cause of the error.
	 */
	public final T get() throws DiffbotException {
		if (executed) {
			return getResult();
		} else {
			synchronized (client) {
				if (!executed) {
					request.runBatch(this);
					return getResult();
				} else {
					return getResult();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private final T getResult() throws DiffbotException {
		if (error != null) {
			if (error instanceof DiffbotAPIException) {
				throw (DiffbotAPIException) error;
			} else if (error instanceof DiffbotParseException) {
				throw (DiffbotParseException) error;
			} else if (error instanceof DiffbotServerException) {
				throw (DiffbotServerException) error;
			} else if (error instanceof DiffbotUnauthorizedException) {
				throw (DiffbotUnauthorizedException) error;
			}
		}
		return (T) result;
	}

}

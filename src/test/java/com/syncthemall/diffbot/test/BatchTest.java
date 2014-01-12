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
/**
 * 
 */
package com.syncthemall.diffbot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.syncthemall.diffbot.Future;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.exception.DiffbotServerException;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.frontpage.Frontpage;

/**
 * Test for the batch API.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class BatchTest extends DiffbotTest {

	/**
	 * Test a working case of a batch call.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Ignore
	// The Batch API keep failing
	@Test
	public final void testWorkingBatch() throws DiffbotException {

		Future<Article> fArticle = diffbot.articles().get(articleTestURL).withFields("*").queue();
		Future<Frontpage> fFrontpage = diffbot.frontpages().get(frontpageTestURL).queue();
		Future<Article> fArticle2 = diffbot.articles().get(articleTestURL).withFields("*").queue();
		Future<Frontpage> fFrontpage2 = diffbot.frontpages().get(frontpageTestURL).queue();

		assertEquals(4, diffbot.getFutures().size());

		Article article = fArticle.get();
		Frontpage frontpage = fFrontpage.get();
		Article article2 = fArticle2.get();
		Frontpage frontpage2 = fFrontpage2.get();

		assertEquals("Two articles retrieve from the same URL should be equal", article, article2);
		assertEquals("Two frontpages retrieve from the same URL should be equal", frontpage, frontpage2);

		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("An Frontpage should have been retrieved from the call", frontpage);
		assertEquals(0, diffbot.getFutures().size());
	}

	/**
	 * Test a batch call with an article request supposed to worked and one with a malformed URL. The first result
	 * should be retrievable and the second one should throw a {@code DiffbotServerException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Ignore
	// The Batch API keep failing
	@Test
	public final void testMalFormedArticleURL() throws DiffbotException {
		Future<Article> fArticle = diffbot.articles().get(articleTestURL).withFields("*").queue();
		Future<Article> malformedFArticle = diffbot.articles().get(malFormedTestURL).withFields("*").queue();
		Article article = fArticle.get();
		assertNotNull("An Article should have been retrieved from the call", article);
		assertEquals("An Article should have been retrieved from the call", 0, diffbot.getFutures().size());
		try {
			malformedFArticle.get();
			fail("Retrieving the result of an Article malformed request in a the batch results should throw a DiffbotServerException.");
		} catch (DiffbotServerException e) {
			// This is expected
		}
	}

	/**
	 * Test a batch call with an article request supposed to worked and one with a non existing URL. The first result
	 * should be retrievable and the second one should throw a {@code DiffbotServerException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Ignore
	// The Batch API keep failing
	@Test
	public final void testNonExistingArticleURL() throws DiffbotException {
		Future<Article> fArticle = diffbot.articles().get(articleTestURL).withFields("*").queue();
		Future<Article> malformedFArticle = diffbot.articles().get(nonExixtingTestURL).withFields("*").queue();
		Article article = fArticle.get();
		assertNotNull("An Article should have been retrieved from the call", article);
		assertEquals("An Article should have been retrieved from the call", 0, diffbot.getFutures().size());
		try {
			malformedFArticle.get();
			fail("Retrieving the result of an Article non exixting request in a the batch results should throw a DiffbotServerException.");
		} catch (DiffbotServerException e) {
			// This is expected
		}
	}

	/**
	 * Test a batch call with a number of request larger than the max number allowed. The first call to
	 * {@code Future#get()} should call the batch API with the max number of request allowed. the Second call to
	 * {@code Future#get()} should call the batch API for the remaining requests.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Ignore
	// The Batch API keep failing
	@Test
	public final void testLargeBatch() throws DiffbotException {

		List<Future<Article>> requests = new ArrayList<Future<Article>>();
		diffbot.setMaxBatchRequest(5);
		diffbot.setBatchRequestTimeout(300000);
		for (int i = 0; i < 6; i++) {
			requests.add(diffbot.articles().get(articleTestURL).withFields("*").queue());
		}
		Future<Frontpage> fFrontpage = diffbot.frontpages().get(frontpageTestURL).queue();

		assertEquals(7, diffbot.getFutures().size());

		Article article = requests.get(0).get();
		assertNotNull("An Article should have been retrieved from the call", article);

		assertEquals("The future list size should be <total requests> - <max batch request>", 2, diffbot.getFutures()
				.size());

		Frontpage frontpage = fFrontpage.get();
		assertNotNull("An Frontapge should have been retrieved from the call", frontpage);

		assertEquals("The future list should be empty after a call", 0, diffbot.getFutures().size());

		for (Future<Article> future : requests) {
			assertNotNull("An Article should have been retrieved from the call", future.get());
		}

	}

}

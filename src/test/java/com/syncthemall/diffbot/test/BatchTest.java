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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.syncthemall.diffbot.Diffbot;
import com.syncthemall.diffbot.Future;
import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.model.article.Article;
import com.syncthemall.diffbot.model.classifier.Classified;
import com.syncthemall.diffbot.model.frontpage.Frontpage;
import com.syncthemall.diffbot.model.images.Images;
import com.syncthemall.diffbot.model.products.Products;

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
	@Test
	public final void testWorkingBatch() throws DiffbotException {

		Future<Classified> fClassified = diffbot.classifier().analyze(articleTestURL).withFields("*").queue();
		Future<Classified> fClassified2 = diffbot.classifier().analyze(articleTestURL).withFields("*").queue();
		Future<Article> fArticle = diffbot.article().analyze(articleTestURL).withFields("*").queue();
		Future<Frontpage> fFrontpage = diffbot.frontpage().analyze(frontpageTestURL).queue();
		Future<Article> fArticle2 = diffbot.article().analyze(articleTestURL).withFields("*").queue();
		Future<Frontpage> fFrontpage2 = diffbot.frontpage().analyze(frontpageTestURL).queue();
		Future<Images> fImages = diffbot.images().analyze(imageTestURL).withFields("*").withTimeout(10000).queue();
		Future<Images> fImages2 = diffbot.images().analyze(imageTestURL).withFields("*").withTimeout(10000).queue();
		Future<Products> fProducts = diffbot.products().analyze(productTestURL).withFields("*").withTimeout(10000)
				.queue();
		Future<Products> fProducts2 = diffbot.products().analyze(productTestURL).withFields("*").withTimeout(10000)
				.queue();

		Future<Images> fArticleAsImages = diffbot.images().analyze(articleTestURL).withFields("*").withTimeout(10000)
				.queue();

		assertEquals(11, diffbot.getFutures().size());

		Article article = fArticle.get();
		Article article2 = fArticle2.get();
		Frontpage frontpage = fFrontpage.get();
		Frontpage frontpage2 = fFrontpage2.get();
		Images images = fImages.get();
		Images images2 = fImages2.get();
		Classified classified = fClassified.get();
		Classified classified2 = fClassified2.get();
		Products products = fProducts.get();
		Products products2 = fProducts2.get();

		Images articleAsimages = fArticleAsImages.get();

		assertEquals("Two articles retrieve from the same URL should be equal", article, article2);
		assertEquals("Two frontpages retrieve from the same URL should be equal", frontpage, frontpage2);
		assertEquals("Two images retrieve from the same URL should be equal", images, images2);
		assertEquals("Two classified retrieve from the same URL should be equal", classified, classified2);
		assertEquals("Two products retrieve from the same URL should be equal", products, products2);
		assertEquals("An Article and a Classified should be equal if they have the same URL", article, classified);
		assertNotEquals("An Article and an Article as an Images shouldn't be equal even if they have the same URL",
				article, articleAsimages);
		assertEquals("An Article and a Classified as an Image should be equal if they have the same URL and same type",
				article, classified.asImages());

		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("An Frontpage should have been retrieved from the call", frontpage);
		assertNotNull("An Images should have been retrieved from the call", images);
		assertNotNull("A Classified should have been retrieved from the call", classified);
		assertNotNull("A Products should have been retrieved from the call", products);

		assertEquals(0, diffbot.getFutures().size());
	}

	/**
	 * Test a batch call with an article request supposed to worked and one with a malformed URL. The first result
	 * should be retrievable and the second one should throw a {@code DiffbotServerException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testMalFormedArticleURL() throws DiffbotException {
		Future<Article> fArticle = diffbot.article().analyze(articleTestURL).withFields("*").queue();
		Future<Article> malformedFArticle = diffbot.article().analyze(malFormedTestURL).withFields("*").queue();
		Future<Frontpage> fFrontpage = diffbot.frontpage().analyze(frontpageTestURL).queue();

		Article article = fArticle.get();
		Frontpage frontpage = fFrontpage.get();

		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("A Frontpage should have been retrieved from the call", frontpage);
		assertEquals(0, diffbot.getFutures().size());
		try {
			malformedFArticle.get();
			fail("Retrieving the result of an Article malformed request in a the batch results should throw a DiffbotServerException.");
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertEquals(e.getMessage(), "Could not connect.");
		}
	}

	/**
	 * Test a batch call with an article request supposed to worked and one with a non existing URL. The first result
	 * should be retrievable and the second one should throw a {@code DiffbotServerException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testNonExistingArticleURL() throws DiffbotException {
		Future<Article> fArticle = diffbot.article().analyze(articleTestURL).withFields("*").queue();
		Future<Article> nonExistingFArticle = diffbot.article().analyze(nonExixtingTestURL).withFields("*").queue();
		Future<Frontpage> fFrontpage = diffbot.frontpage().analyze(frontpageTestURL).queue();

		Article article = fArticle.get();
		Frontpage frontpage = fFrontpage.get();

		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("A Frontpage should have been retrieved from the call", frontpage);
		assertEquals("An Article should have been retrieved from the call", 0, diffbot.getFutures().size());
		try {
			nonExistingFArticle.get();
			fail("Retrieving the result of an Article non exixting request in a the batch results should throw a DiffbotServerException.");
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertEquals(e.getMessage(), "Could not connect.");
		}
	}

	/**
	 * Test a batch call with a frontpage request supposed to worked and one with a malformed URL. The first result
	 * should be retrievable and the second one should throw a {@code DiffbotServerException}.
	 * <p>
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testMalFormedFrontpageURL() throws DiffbotException {
		Future<Article> fArticle = diffbot.article().analyze(articleTestURL).withFields("*").queue();
		Future<Frontpage> malformedFFrontpage = diffbot.frontpage().analyze(malFormedTestURL).queue();
		Future<Images> fImages = diffbot.images().analyze(imageTestURL).withFields("*").queue();

		Article article = fArticle.get();
		Images images = fImages.get();

		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("A Images should have been retrieved from the call", images);
		assertEquals(0, diffbot.getFutures().size());
		try {
			malformedFFrontpage.get();
			fail("Retrieving the result of an Frontpage malformed request in a the batch results should throw a DiffbotServerException.");
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertEquals(e.getMessage(), "Could not connect.");
		}
	}

	/**
	 * Test a batch call with a frontpage request supposed to worked and one with a non existing URL. The first result
	 * should be retrievable and the second one should throw a {@code DiffbotServerException}.
	 * <p>
	 * The behavior of the API is not consistent in that case. Sometimes return with title = URL, sometimes title = "404
	 * not found" and sometimes return an error 500.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testNonExistingFrontpageURL() throws DiffbotException {
		Future<Article> fArticle = diffbot.article().analyze(articleTestURL).withFields("*").queue();
		Future<Frontpage> malformedFFrontpage = diffbot.frontpage().analyze(nonExixtingTestURL).queue();
		Future<Images> fImages = diffbot.images().analyze(imageTestURL).withFields("*").queue();

		Article article = fArticle.get();
		Images images = fImages.get();

		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("A Images should have been retrieved from the call", images);
		assertEquals(0, diffbot.getFutures().size());
		try {
			Frontpage frontpage = malformedFFrontpage.get();
			assertNull("Frontpage API shouldn't return items with a non existing URL", frontpage.getItems());
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertNotNull(e.getMessage());
		}
	}

	/**
	 * Test a batch call with a number of request larger than the max number allowed. The first call to
	 * {@code Future#get()} should call the batch API with the max number of request allowed. the Second call to
	 * {@code Future#get()} should call the batch API for the remaining requests.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testLargeBatch() throws DiffbotException {

		List<Future<Article>> requests = new ArrayList<Future<Article>>();
		diffbot.setMaxBatchRequest(5);
		diffbot.setBatchRequestTimeout(300000);
		for (int i = 0; i < 6; i++) {
			requests.add(diffbot.article().analyze(articleTestURL).withFields("*").queue());
		}
		Future<Frontpage> fFrontpage = diffbot.frontpage().analyze(frontpageTestURL).queue();

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
	
	/**
	 * Test a batch call with a number of request larger than the max number allowed with asynchronous calls. The first call to
	 * {@code Future#get()} should call the batch API twice with the max number of request allowed. The Second call to
	 * {@code Future#get()} should call the batch API for the remaining requests.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException 
	 */
	@Test
	public final void testLargeAsynchronousBatch() throws DiffbotException, JAXBException {
		List<Future<Article>> requests = new ArrayList<Future<Article>>();
		diffbot.setMaxBatchRequest(5);
		diffbot.setBatchRequestTimeout(300000);
		diffbot.setConcurrentBatchRequest(2);
		
		
		for (int i = 0; i < 14; i++) {
			requests.add(diffbot.article().analyze(articleTestURL).withFields("*").queue());
		}
		Future<Frontpage> fFrontpage = diffbot.frontpage().analyze(frontpageTestURL).queue();

		assertEquals(15, diffbot.getFutures().size());

		Article article = requests.get(0).get();
		
		assertNotNull("An Article should have been retrieved from the call", article);

		assertEquals("The future list size should be <total requests> - <max batch request> * <concurrent requests>", 5, diffbot.getFutures()
				.size());

		Frontpage frontpage = fFrontpage.get();
		assertNotNull("An Frontapge should have been retrieved from the call", frontpage);

		assertEquals("The future list should be empty after a call", 0, diffbot.getFutures().size());

		for (Future<Article> future : requests) {
			assertNotNull("An Article should have been retrieved from the call", future.get());
		}
	}
		
}

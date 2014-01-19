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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.model.article.Article;

/**
 * Test for the article API.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class ArticleTest extends DiffbotTest {

	/**
	 * Test a working case of a article call.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testWorkingArticle() throws DiffbotException {
		Article article = diffbot.article().analyze(articleTestURL).withFields("*").withTimeout(30000).execute();
		assertNotNull("An Article should have been retrieved from the call", article);
		assertNotNull("Article API returned an item with no title", article.getTitle());
		assertNotNull("Article API returned an item with no text", article.getText());
		assertNotEquals("An Article should have a hashCodedifferent than 0", 0, article.hashCode());
	}

	/**
	 * Test an article call with a malformed URL. Should throw a {@code DiffbotServerException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testMalFormedURL() throws DiffbotException {
		try {
			diffbot.article().analyze(malFormedTestURL).execute();
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertNotNull(e.getMessage());
		}
	}

	/**
	 * Test an article call with a non exixting URL.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testNonExistingURL() throws DiffbotException {
		try {
		diffbot.article().analyze(nonExixtingTestURL).execute();
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertNotNull(e.getMessage());
		}
	}

}

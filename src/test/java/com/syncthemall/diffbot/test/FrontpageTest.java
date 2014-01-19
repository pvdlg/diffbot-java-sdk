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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.syncthemall.diffbot.exception.DiffbotAPIException;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.model.frontpage.Frontpage;

/**
 * Test for the frontpage API.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class FrontpageTest extends DiffbotTest {

	/**
	 * Test a working case of a frontpage call.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testWorkingFrontpage() throws DiffbotException {

		Frontpage frontpage = diffbot.frontpage().analyze(frontpageTestURL).execute();

		assertNotNull("A Frontapge should have been retrieved from the call", frontpage);
		assertNotNull(frontpage.getInfo());
		assertNotEquals("Frontpage API returned an Info part with 0 items", 0, frontpage.getInfo().getNumItems());
		assertNotEquals("Frontpage API returned an different source URL than the one in parameter", frontpageTestURL,
				frontpage.getInfo().getSourceURL());
		assertNotNull("Frontpage API returned an Info part with no title", frontpage.getInfo().getTitle());
		assertNotNull("Frontpage API returned no items", frontpage.getItems());
		assertNotEquals("Frontpage API returned no items", 0, frontpage.getItems().size());
		assertNotNull("Frontpage API returned an item with no title", frontpage.getItems().get(0).getTitle());
		assertNotNull("Frontpage API returned an item with no description", frontpage.getItems().get(0)
				.getDescription());
		assertNotNull("Frontpage API returned an item with no type", frontpage.getItems().get(0).getType());
	}

	/**
	 * Test a frontpage call with a malformed URL. Should throw a {@code DiffbotAPIException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testMalFormedTURL() throws DiffbotException {
		try {
			diffbot.frontpage().analyze(malFormedTestURL).execute();
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertNotNull(e.getMessage());
		}
	}

	/**
	 * Test a frontpage call with a non exixting URL.
	 * <p>
	 * The behavior of the API is not consistent in that case. Sometimes return with title = URL, sometimes title = "404
	 * not found" and sometimes return an error 500.
	 * 
	 * @throws DiffbotException means the test is failed
	 */
	@Test
	public final void testNonExistingURL() throws DiffbotException {
		try {
			Frontpage frontpage = diffbot.frontpage().analyze(nonExixtingTestURL).execute();
			assertNull("Frontpage API shouldn't return items with a non existing URL", frontpage.getItems());
		} catch (DiffbotAPIException e) {
			assertNotEquals(0, e.getErrorCode());
			assertNotNull(e.getMessage());
		}
	}

}

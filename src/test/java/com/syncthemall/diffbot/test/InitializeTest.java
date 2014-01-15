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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.syncthemall.diffbot.Diffbot;
import com.syncthemall.diffbot.exception.DiffbotException;
import com.syncthemall.diffbot.exception.DiffbotUnauthorizedException;

/**
 * Initialization tests.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class InitializeTest extends DiffbotTest {

	/**
	 * Verify the URL for the test suite are accessible.
	 */
	@Test
	public final void testInitialize() {
		try {
			assertTrue("The diffbot API URL is not accessible", ping(diffbotAPIURL, 30000));
			assertTrue("The article test URL is not accessible", ping(articleTestURL, 30000));
			assertTrue("The frontpage test URL is not accessible", ping(frontpageTestURL, 30000));
			assertTrue("The image test URL is not accessible", ping(imageTestURL, 30000));
			assertTrue("The product test URL is not accessible", ping(productTestURL, 30000));
		} catch (IOException e) {
			fail("One of the accessible test URL is not accessible.");
		}

		try {
			ping(nonExixtingTestURL, 30000);
			fail("The non existing test URL is accessible.");
		} catch (UnknownHostException expectedException) {
			// This is expected
		} catch (MalformedURLException e) {
			fail("The non existing test URL is malformed.");
		} catch (IOException e) {
			fail("Accessing the non existing test URL doesn't throw an UnknownHostException.");
		}

		try {
			ping(malFormedTestURL, 30000);
			fail("The malformed test URL is accessible.");
		} catch (MalformedURLException expectedException) {
			// This is expected
		} catch (IOException e) {
			fail("The wrongly formatted test URL is not wrongly formatted.");
		}
	}

	/**
	 * Test a call to the article API with a wrong token. Should throw a {@code DiffbotUnauthorizedException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException means the test is failed
	 */
	@Test(expected = DiffbotUnauthorizedException.class)
	public final void testUnauthorized() throws DiffbotException, JAXBException {
		Diffbot diffbot = new Diffbot(new ApacheHttpTransport(), new GsonFactory(), "fake API key");
		diffbot.article().analyze(articleTestURL).execute();
	}

	/**
	 * Test to instanciate {@link Diffbot} with an empty token. Should throw a {@code IllegalArgumentException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException means the test is failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testEmptyToken() throws DiffbotException, JAXBException {
		new Diffbot(new ApacheHttpTransport(), new GsonFactory(), "");
	}

	/**
	 * Test to instanciate {@link Diffbot} with a null {@link HttpTransport}. Should throw a
	 * {@code IllegalArgumentException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException means the test is failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testNullHttpTransport() throws DiffbotException, JAXBException {
		new Diffbot(null, new GsonFactory(), "fake API key");
	}

	/**
	 * Test to instanciate {@link Diffbot} with a null {@link JsonFactory}. Should throw a
	 * {@code IllegalArgumentException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException means the test is failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testNullJSonFactory() throws DiffbotException, JAXBException {
		new Diffbot(new ApacheHttpTransport(), null, "fake API key");
	}

	/**
	 * Test to instanciate {@link Diffbot} with a max batch request number > 50. Should throw a
	 * {@code IllegalArgumentException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException means the test is failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testMaxBatchRequest() throws DiffbotException, JAXBException {
		diffbot.setMaxBatchRequest(51);
	}

	/**
	 * Test to instanciate {@link Diffbot} with a max batch request number = 0. Should throw a
	 * {@code IllegalArgumentException}.
	 * 
	 * @throws DiffbotException means the test is failed
	 * @throws JAXBException means the test is failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testMinBatchRequest() throws DiffbotException, JAXBException {
		diffbot.setMaxBatchRequest(0);
	}

}

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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Before;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.syncthemall.diffbot.Diffbot;

/**
 * Parent class for all test cases.
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public class DiffbotTest {

	protected static Diffbot diffbot;

	protected static String diffbotAPIURL = "http://diffbot.com";
	protected static String articleTestURL = "http://www.huffingtonpost.com/2014/01/11/fast-track-trade-democrats_n_4580720.html";
	protected static String frontpageTestURL = "http://theverge.com";
	protected static String nonExixtingTestURL = "http://a.abc";
	protected static String malFormedTestURL = "fake url";

	private static HttpTransport httpTransport = new ApacheHttpTransport();
	private static JsonFactory jsonFactory = new GsonFactory();

	/**
	 * Instanciate a {@link Diffbot}.
	 */
	@Before
	public final void setUp() {
		try {
			DiffbotTest.diffbot = new Diffbot(httpTransport, jsonFactory, System.getProperty("diffbotApiKey"));
		} catch (JAXBException e) {
			fail("JAXB is not in the classpath.");
		}
	}

	/**
	 * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
	 * the 200-399 range.
	 * 
	 * @param url The HTTP URL to be pinged.
	 * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that the
	 *            total timeout is effectively two times the given timeout.
	 * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
	 *         given timeout, otherwise <code>false</code>.
	 * @throws IOException if the url is not accessible
	 */
	public static final boolean ping(final String url, final int timeout) throws IOException {
		String httpUrl = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL
															// certificates.
		HttpURLConnection connection = (HttpURLConnection) new URL(httpUrl).openConnection();
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
		connection.setRequestMethod("HEAD");
		int responseCode = connection.getResponseCode();
		return (200 <= responseCode && responseCode <= 399);
	}

	protected DiffbotTest() {
		super();
	}

}

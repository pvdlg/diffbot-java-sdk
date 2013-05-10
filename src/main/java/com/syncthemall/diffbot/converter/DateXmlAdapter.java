/**
 * Copyright (c) 2013 Pierre-Denis Vanduynslager, https://github.com/vanduynslagerp
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.syncthemall.diffbot.converter;

import java.util.Date;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * {@code XmlAdapter} to convert {@code String} from Diffbot Frontpage results to {@code Date}.
 */
public class DateXmlAdapter extends XmlAdapter<String, Date> {

	private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
			.withLocale(Locale.US);

	@Override
	public final String marshal(final Date v) throws Exception {
		return dateFormatter.print(v.getTime());
	}

	@Override
	public final Date unmarshal(final String v) throws Exception {
		return dateFormatter.parseDateTime(v).toDate();
	}
}

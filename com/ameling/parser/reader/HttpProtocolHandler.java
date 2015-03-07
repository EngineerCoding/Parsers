package com.ameling.parser.reader;

/*******************************************************************************
 * Copyright 2015 Wesley Ameling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpProtocolHandler extends ProtocolHandler {

	public static enum HttpMethod {
		POST,
		GET;
	}

	private static final String PATTERN_CHARSET = ".*charset=(.*)";
	private static final String PATTERN_REPLACE = "$1";

	private static final String STRING_DEFAULT_CHARSET = "ISO-8859-1";
	private static final String STRING_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String STRING_HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String[] STRING_PROTOCOLS = { "http", "https" };

	private static final byte[] BYTE_DEFAULT_CHARSET = STRING_DEFAULT_CHARSET.getBytes();

	protected static final ProtocolHandler instance = new HttpProtocolHandler();

	private HttpProtocolHandler() {}

	@Override
	public String[] getProtocols() {
		return STRING_PROTOCOLS;
	}

	@Override
	protected Reader getContents(final URL url) {
		try {
			return getContents(url, HttpMethod.GET, false);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO: retrieve the content-type
	public Reader getContents(URL url, final HttpMethod method, final boolean followRedirect) throws IOException {
		final String query = url.getQuery();
		if (method == HttpMethod.POST)
			url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath());

		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method.name());
		connection.setInstanceFollowRedirects(followRedirect);

		if (method == HttpMethod.POST && query != null && !query.isEmpty()) {
			connection.setRequestProperty(STRING_HEADER_CONTENT_LENGTH, Integer.toString(BYTE_DEFAULT_CHARSET.length));
			connection.setDoOutput(true);

			final OutputStream outputStreamWriter = connection.getOutputStream();
			outputStreamWriter.write(BYTE_DEFAULT_CHARSET);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		}

		return new InputStreamReader(connection.getInputStream());
	}

}

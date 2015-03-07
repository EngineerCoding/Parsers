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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ProtocolHandler {

	private static List<ProtocolHandler> protocols;

	static {
		protocols = new ArrayList<ProtocolHandler>();
		protocols.add(HttpProtocolHandler.instance);
		protocols.add(FileProtocolHandler.instance);
	}

	protected static Reader getReader(final URL url) {
		final String protocol = url.getProtocol();
		for (final ProtocolHandler protocolHandler : protocols) {
			for (final String supportedProtocol : protocolHandler.getProtocols()) {
				if (supportedProtocol.equalsIgnoreCase(protocol)) {
					final Reader reader = protocolHandler.getContents(url);
					if (reader != null)
						return reader;
				}
			}
		}

		try {
			return new InputStreamReader(url.openStream());
		} catch (final IOException e) {
			return null;
		}
	}


	protected abstract String[] getProtocols();

	protected abstract Reader getContents(final URL url);
}

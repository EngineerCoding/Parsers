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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public class FileProtocolHandler extends ProtocolHandler {

	private static final String[] STRING_PROTOCOLS = { "file" };
	protected static final ProtocolHandler instance = new FileProtocolHandler();

	private FileProtocolHandler() {
	}

	@Override
	protected String[] getProtocols() {
		return STRING_PROTOCOLS;
	}

	@Override
	protected Reader getContents(final URL url) {
		final File file = new File(url.getFile());
		if (file.exists() && file.canRead()) {
			try {
				return new FileReader(file);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
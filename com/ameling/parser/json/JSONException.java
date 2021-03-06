package com.ameling.parser.json;

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

import com.ameling.parser.SyntaxException;

public class JSONException extends SyntaxException {
	private static final long serialVersionUID = 1L;

	/**
	 * Generate a new instance with the given message
	 *
	 * @param message - The message
	 */
	public JSONException(final String message) {
		super(message);
	}

	/**
	 * A message which is formatted with {@link String#format(String, Object[])}
	 *
	 * @param format  String format
	 * @param objects The objects matching with the format
	 * @throws NullPointerException     If the format is null
	 * @throws IllegalArgumentException When the format does not match the objects
	 */
	public JSONException(final String format, final Object... objects) {
		super(format, objects);
	}
}

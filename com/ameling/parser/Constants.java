/*
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
 */

package com.ameling.parser;

/**
 * This class is here for pure efficiency, it holds all String and char constants to optimize the code, especially in parsers and also for exception throwing <br/>
 * This class is final because this is useless to inherit in the first place. Also it has a private constructor because this is just a holder class.
 *
 * @author Wesley A
 */
public final class Constants {
	// Not intended as object
	private Constants () {
	}

	// All used chars
	public static final char CHAR_JSON_OBJECT_START = '{';
	public static final char CHAR_JSON_OBJECT_END = '}';
	public static final char CHAR_JSON_ARRAY_START = '[';
	public static final char CHAR_JSON_ARRAY_END = ']';

	public static final char CHAR_QUOTE_DOUBLE = '"';
	public static final char CHAR_COLON = ':';
	public static final char CHAR_COMMA = ',';
	public static final char CHAR_PLUS = '+';

	public static final char CHAR_BRACKET_CLOSE = ')';
	public static final char CHAR_BRACKET_OPEN = '(';

	// All formats to be used in Exceptions
	public static final String FORMAT_EXPECTED_CHAR = "Expected '%s'";
	public static final String FORMAT_EXPECTED_VALUE = "Key '%s' is not a %s";
	public static final String FORMAT_EXPECTED_EXISTING_KEY = "Key '%s' does not exist in %s!";

	// All non-format Exceptions
	public static final String EXCEPTION_VALUE_KEY_NULL = "Value or key is null!";

	// All types of values
	public static final String TYPE_LONG = "long";
	public static final String TYPE_INT = "int";
	public static final String TYPE_SHORT = "short";
	public static final String TYPE_BYTE = "byte";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_JSON_OBJECT = "JSONObject";
	public static final String TYPE_JSON_ARRAY = "JSONArray";
}

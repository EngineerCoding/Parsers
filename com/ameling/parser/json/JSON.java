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

package com.ameling.parser.json;

import com.ameling.parser.Parser;
import com.ameling.parser.Tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import static com.ameling.parser.Constants.CHAR_JSON_ARRAY_START;
import static com.ameling.parser.Constants.CHAR_JSON_OBJECT_START;

/**
 * The main super class of both {@link JSONObject} and {@link JSON}. As an user this class is useless to you since this handles all
 * the inner parsing, it has not set/get methods whatsoever
 *
 * @author Wesley A
 */
public abstract class JSON extends Parser {

	// All constants which are just used within this class
	private static final String TYPE_NULL = "null";


	/**
	 * An enumeration which contains types of all the values, used by {@link JSON#getType(Object)}
	 *
	 * @author Wesley A
	 */
	public static enum Type {
		/**
		 * An actual {@link JSONObject}
		 */
		JSONObject,

		/**
		 * An actual {@link JSONArray}
		 */
		JSONArray,

		/**
		 * A number type, ranging from long to float
		 */
		Number,

		/**
		 * A boolean flag: true or false
		 */
		Boolean,

		/**
		 * A simple {@link String}
		 */
		String,

		/**
		 * An object of {@link Null}
		 */
		DummyNull,

		/**
		 * just null like you frequently use in your code
		 */
		Null
	}

	/**
	 * An object that is the same as null (for the sake of having a difference between an actual null or a null in the JSON string)
	 *
	 * @author Wesley A
	 */
	private static class Null {
		/**
		 * Only reachable in {@link JSON} where the only Null object is stored
		 */
		private Null () {
		}

		/**
		 * Makes sure to return null (called by {@link JSONWriter}
		 */
		@Override
		public String toString () {
			return TYPE_NULL;
		}
	}

	/**
	 * The one and only NULL object
	 *
	 * @see {@link JSON.Null}
	 */
	protected static final Null NULL = new Null();

	/**
	 * A standard constructor, it sets the argument to an inner variable
	 *
	 * @param tokenizer The tokenizer to use for this object
	 */
	protected JSON (Tokenizer tokenizer) {
		super(tokenizer);
	}

	@Override
	protected final Object parseValue () throws RuntimeException {
		tokenizer.skipBlanks();
		Object obj = super.parseValue();
		if (obj == null) {
			Character character = tokenizer.peek();
			if (character != null) {
				if (character == CHAR_JSON_ARRAY_START)
					return new JSONArray(tokenizer);
				if (character == CHAR_JSON_OBJECT_START)
					return new JSONObject(tokenizer);

				return parseNull();
			}
		}

		return obj;
	}

	/**
	 * Tries to parse a null value, it actually parses the string "null"
	 *
	 * @return Null object, to distinguish between a real and non-real Null object
	 */
	private Null parseNull () {
		tokenizer.skipBlanks();
		if (tokenizer.peek() == TYPE_NULL.charAt(0)) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < TYPE_NULL.length() && tokenizer.peek() != null; i++)
				sb.append(tokenizer.pop());

			return (TYPE_NULL.equals(sb.toString()) ? NULL : null);
		}
		return null;
	}

	/**
	 * Makes from this object a readable String. Calls {@link #toString(boolean)} with false
	 *
	 * @return String representing this object
	 */
	@Override
	public String toString () {
		return toString(false);
	}

	/**
	 * Makes from this object a readable string using {@link JSONWriter} with a {@link StringWriter}
	 *
	 * @param indent Whether to indent and add line ends
	 * @return String representing this JSON object
	 */
	public String toString (final boolean indent) {
		final StringWriter sw = new StringWriter();
		final JSONWriter writer = new JSONWriter(sw, indent);

		if (this instanceof JSONObject) {
			try {
				writer.append((JSONObject) this);
				return sw.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (this instanceof JSONArray) {
			try {
				writer.append((JSONArray) this);
				return sw.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Checks the difference between a standard null value and a key-Null value (so when it is in the JSON)
	 *
	 * @param object - to check if this object is a dummy null value
	 * @return Whether it is our dummy null value (from the JSON)
	 */
	protected static boolean isNullValue (final Object object) {
		return object instanceof Null;
	}

	/**
	 * Gets the {@link Type} of the object
	 *
	 * @param object The object to check
	 * @return Type the type of this object, when it is not recognised or null, it will return {@link Type#Null}
	 */
	protected static Type getType (final Object object) {
		if (object instanceof JSON)
			return (object instanceof JSONObject ? Type.JSONObject : Type.JSONArray);
		if (object instanceof Number)
			return Type.Number;
		if (object instanceof Boolean)
			return Type.Boolean;
		if (object instanceof String)
			return Type.String;
		if (isNullValue(object))
			return Type.DummyNull;

		return Type.Null;
	}

	/**
	 * Use this method to parse a JSON string when you don't know what type it is (Object or Array). Otherwise it is advised to directly
	 * create a new method on JSONObject or Array.
	 *
	 * @param reader The output of a reader to parse
	 * @return A corresponding object:
	 * <ul>
	 * <li>{@link JSONObject} when the string starts with '{'</li>
	 * <li>{@link JSONArray} when the string starts with '['</li>
	 * <li>Null when it starts with neither of these</li>
	 * </ul>
	 */
	public static JSON parseJSON (final Reader reader) {
		final Tokenizer tokenizer = new Tokenizer(reader);
		tokenizer.skipBlanks();

		final Character character = tokenizer.peek();
		if (character != null) {
			if (character == CHAR_JSON_ARRAY_START)
				return new JSONArray(tokenizer);
			if (character == CHAR_JSON_OBJECT_START)
				return new JSONObject(tokenizer);
		}
		return null;
	}
}

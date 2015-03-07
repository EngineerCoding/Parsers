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

import com.ameling.parser.Constants;
import com.ameling.parser.json.JSON.Type;

import java.io.IOException;
import java.io.Writer;

import static com.ameling.parser.Constants.CHAR_JSON_ARRAY_START;
import static com.ameling.parser.Constants.CHAR_JSON_ARRAY_END;
import static com.ameling.parser.Constants.CHAR_JSON_OBJECT_START;
import static com.ameling.parser.Constants.CHAR_JSON_OBJECT_END;
import static com.ameling.parser.Constants.CHAR_COLON;
import static com.ameling.parser.Constants.CHAR_COMMA;

/**
 * This class is used to write to a {@link Writer} object. Then you can call
 *
 * @author Wesley A
 */
public class JSONWriter {

	// All constants used within this class only
	private static final char CHAR_TAB = '\t';
	private static final String STRING_SPACE = " ";
	private static final String STRING_WRITER_NULL = "Writer is null";


	/**
	 * {@link Writer} to be used for this class
	 */
	private final Writer writer;

	/**
	 * Boolean whether to decide if we should add indentation and line-ends
	 */
	private final boolean indent;

	/**
	 * Current indentation level
	 */
	private int tabs = 0;

	/**
	 * Creates a new instance of this class using {@link #JSONWriter(Writer, boolean)} with argument true
	 *
	 * @param writer - The writer to use in this object
	 */
	public JSONWriter(Writer writer) {
		this(writer, true);
	}

	/**
	 * Creates a new instance of this class
	 *
	 * @param writer - The writer to use in this object
	 * @param indent - True to indent and add line-ends or false to have plain text
	 */
	public JSONWriter(final Writer writer, final boolean indent) {
		this.writer = writer;
		this.indent = indent;
		if (writer == null)
			throw new NullPointerException(STRING_WRITER_NULL);
	}

	/**
	 * Appends the writer with this object
	 *
	 * @param parser - The object to write to the {@link #writer}
	 * @throws IOException when the writer throws one
	 */
	public synchronized void append(final JSONArray parser) throws IOException {
		writer.write(CHAR_JSON_ARRAY_START);

		tabs += 1;
		int maxIndex = parser.getSize();
		for (int i = 0; i < maxIndex; i++) {
			markLineEnd();
			writeValue(parser.get(i), i != maxIndex - 1);
		}

		tabs -= 1;
		if (maxIndex != 0)
			markLineEnd();
		writer.write(CHAR_JSON_ARRAY_END);
		writer.flush();
	}

	/**
	 * Appends the writer with this object
	 *
	 * @param parser - The object to write to the {@link #writer}
	 * @throws IOException when the {@link #writer} throws one
	 */
	public synchronized void append(final JSONObject parser) throws IOException {
		writer.write(CHAR_JSON_OBJECT_START);

		tabs += 1;
		String[] keys = parser.getKeys();
		for (int i = 0; i < keys.length; i++) {
			markLineEnd();
			writeString(keys[i]);
			writer.write(CHAR_COLON + (indent ? STRING_SPACE : ""));
			writeValue(parser.get(keys[i]), i != keys.length - 1);
		}

		tabs -= 1;
		//if(keys.length != 0)
		markLineEnd();
		writer.write(CHAR_JSON_OBJECT_END);
		writer.flush();
	}

	/**
	 * Marks the line end using {@link System#lineSeparator()} and indents afterwards.<br/>
	 * This only happens when {@link #indent} is set to true
	 *
	 * @throws IOException when the {@link #writer} throws one
	 */
	private void markLineEnd() throws IOException {
		if (indent) {
			writer.write(System.lineSeparator());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < tabs; i++)
				sb.append(CHAR_TAB);
			writer.write(sb.toString());
		}
	}

	/**
	 * Writes the value separately
	 *
	 * @param object  - The object to write
	 * @param hasNext - Whether have a ',' after the value
	 * @throws IOException when the {@link #writer} throws one
	 */
	private void writeValue(final Object object, final boolean hasNext) throws IOException {
		Type type = JSON.getType(object);
		if (type != Type.Null) {
			if (type == Type.String) {
				writeString((String) object);
			} else if (type == Type.JSONArray) {
				append((JSONArray) object);
			} else if (type == Type.JSONObject) {
				append((JSONObject) object);
			} else {
				writer.write(object.toString());
			}

			if (hasNext)
				writer.write(CHAR_COMMA);
		}
	}

	/**
	 * Write the string with the correct quotes
	 *
	 * @param string - The string in question
	 * @throws IOException when the {@link #writer} throws one
	 */
	private void writeString(final String string) throws IOException {
		if (string != null) {
			writer.write(Constants.CHAR_QUOTE_DOUBLE);
			writer.write(string);
			writer.write(Constants.CHAR_QUOTE_DOUBLE);
		}
	}
}

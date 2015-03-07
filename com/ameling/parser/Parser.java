package com.ameling.parser;

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

import com.ameling.parser.reader.Tokenizer;

import static com.ameling.parser.Constants.CHAR_DASH;
import static com.ameling.parser.Constants.CHAR_PLUS;
import static com.ameling.parser.Constants.CHAR_QUOTE_DOUBLE;
import static com.ameling.parser.Constants.CHAR_QUOTE_SINGLE;

/**
 * This class is the base class of any parser. At least, any parsers which needs basic parsing method, because this class has methods to parse:
 * <ul>
 * <li>Strings</li>
 * <li>Numbers</li>
 * <li>Booleans</li>
 * </ul>
 * Also it contains a method to parse all of those values, but the beauty of it is that it can be overridden to add your own values. You do not have to necessarily call the super method,
 * but it is advised.<br/>
 * Only use this class if you are going to make use of the methods which parses those values. It is pointless to extend this class then.
 *
 * @author Wesley A
 */
public abstract class Parser {

	// All constants used within this class only
	private static final char CHAR_DOT = '.';
	private static final char CHAR_E_LOWER = 'e';
	private static final char CHAR_E_UPPER = 'E';
	private static final char CHAR_F = 'f';
	private static final char CHAR_SLASH_BACK = '\\';
	private static final char CHAR_T = 't';

	private static final String EXCEPTION_MULTIPLE_DOTS = "Multiple dots have been found";
	private static final String EXCEPTION_UNFINISHED_STRING = "Unfinished string";
	private static final String FORMAT_PARSE_BOOLEAN = "Tried to parse to '%s', got %s";


	/**
	 * The tokenizer that is used for this object
	 */
	protected Tokenizer tokenizer;

	/**
	 * Creates a new instance of a parser with the tokenizer in mind
	 *
	 * @param tokenizer This should be used in {@link #parseValue()} method and in any other parsing part
	 */
	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	/**
	 * This should call all other value parsers when the state is appropriate. <br/>
	 * Of course you can differ from that, since not all parsers need these value types.<br/>
	 * The default value parsers  are:
	 * <ul>
	 * <li>{@link #parseString()}</li>
	 * <li>{@link #parseNumber(boolean)}</li>
	 * <li>{@link #parseBoolean()}</li>
	 * </ul><br/>
	 * By default it already checks for those values, but you obviously can override it if you wish.
	 *
	 * @return the appropriate object for the next value
	 * @throws SyntaxException when a value parser throws one, it will simply get back to the user
	 */
	protected Object parseValue() throws SyntaxException {
		tokenizer.skipBlanks();

		Character character = tokenizer.peek();
		if (character != null) {
			if (character == CHAR_QUOTE_SINGLE || character == CHAR_QUOTE_DOUBLE) // check for a string
				return parseString();
			else if (Character.isDigit(character) || character == CHAR_DASH || character == Constants.CHAR_PLUS) // check for a number
				return parseNumber(true);
			else if (character == CHAR_T || character == CHAR_F) // check for boolean
				return parseBoolean();
		}
		return null; // no standard value has been detected
	}

	/**
	 * Attempts to parse a string with the {@link #tokenizer}
	 *
	 * @return {@link String} when it successfully parsed or null when it failed
	 * @throws SyntaxException when a syntax error occurred
	 */
	protected final String parseString() throws SyntaxException {
		tokenizer.skipBlanks();
		Character character = tokenizer.peek();

		// Check if the string starts with a single quote or double quote
		final boolean singleQuote = character == CHAR_QUOTE_SINGLE;
		final boolean doubleQuote = character == CHAR_QUOTE_DOUBLE;

		if (singleQuote || doubleQuote) { // Only continue when some sort of quote has been found
			tokenizer.pop();

			boolean backslash = false; // When a backslash is found, it will ignore the next character. This is a boolean value to check for that
			final StringBuilder builder = new StringBuilder(); // The string that has been found
			while ((character = tokenizer.peek()) != null) {
				if (backslash) { // if a backslash is found, ignore the next character and append the builder with it
					backslash = false;
					builder.append(tokenizer.pop());
				} else {
					if (character == (singleQuote ? CHAR_QUOTE_SINGLE : CHAR_QUOTE_DOUBLE)) {
						// when  the next character is not ignored, and it is the matching quote which opened the string, return the built string but
						// first get rid of the quote, we have no use for that.
						tokenizer.pop();
						return builder.toString();
					} else if (character == CHAR_SLASH_BACK) {
						// A backslash has been found, set to boolean value to true to ignore the next character
						backslash = true;
						builder.append(tokenizer.pop()); // append the backslash, it is part of the string
					} else {
						// no special circumstance, simply append the builder
						builder.append(tokenizer.pop());
					}
				}
			}
			// Nothing is returned, that means that the string is unfinished. Notify the user of that
			throw new SyntaxException(EXCEPTION_UNFINISHED_STRING);
		}
		// No quote has been found which even starts a string
		return null;
	}

	/**
	 * Attempts to parse a number with the {@link #tokenizer}
	 *
	 * @param parseE Whether to parse the 'e' part of a number. This is used internally, but can be used externally. <br/>
	 * @return {@link Number} object when it successfully parsed or null when it failed
	 * @throws SyntaxException when a syntax error occurred
	 */
	protected final Double parseNumber (boolean parseE) throws SyntaxException {
		tokenizer.skipBlanks();

		Character character;
		boolean parsedDot = false; // boolean value to determine if a dot already has been parsed or not
		final StringBuilder builder = new StringBuilder();

		while ((character = tokenizer.peek()) != null) {
			if (Character.isDigit(character)) {
				// The next character is a digit, simply append
				builder.append(tokenizer.pop());
			} else if (character == CHAR_DASH || character == CHAR_PLUS) {
				// when a '+' or '-' is the character, it will check if the builder has any digits yet.
				// When it hasn't got any digits, it is a unary minus or plus, when it already has digits, it could be an operator.
				// We will stop this loop because it is treated as an unknown character
				if (builder.length() == 0) {
					builder.append(tokenizer.pop());
				} else {
					break;
				}
			} else if (character == CHAR_DOT) {
				// The next character is a dot, check if it has been parsed yet. When it hasn't been parsed, just append it, otherwise notify the user
				if (!parsedDot) {
					parsedDot = true;
					builder.append(tokenizer.pop());
				} else {
					throw new SyntaxException(EXCEPTION_MULTIPLE_DOTS);
				}
			} else if ((character == CHAR_E_LOWER || character == CHAR_E_UPPER)) {
				// When we are allowed to parse e, and the builder has digits then parse a new number which does not have e, and multiply it with the original number
				if (parseE && builder.length() != 0) {
					tokenizer.pop();

					if (builder.length() == 1 && (builder.charAt(0) == CHAR_DASH || builder.charAt(0) == CHAR_PLUS)) {
						builder.append(1);
					}

					final Number number = parseNumber(false);
					if (number != null)
						return Double.parseDouble(builder.toString()) * Math.pow(10D, number.doubleValue());
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (builder.length() == 1 && (builder.charAt(0) == CHAR_DASH || builder.charAt(0) == CHAR_PLUS)) {
			tokenizer.inject(String.valueOf(builder.charAt(0)));
			return null;
		}

		// Return the number when the builder has any characters
		if (builder.length() != 0)
			return Double.parseDouble(builder.toString());
		// Return nothing; no number found
		return null;
	}

	/**
	 * Attempts to parse a boolean with the {@link #tokenizer}
	 *
	 * @return {@link Boolean} when it successfully parsed or null when it failed
	 * @throws SyntaxException when a syntax error occurred
	 */
	protected final Boolean parseBoolean() throws SyntaxException {
		tokenizer.skipBlanks();
		Character character = tokenizer.peek();

		// When the next character is 't' or 'f' it tries to parse 'true' or 'false'
		if (character != null && (character == CHAR_T || character == CHAR_F)) {
			final boolean parseTrue = character == CHAR_T; // determine whether it is true or false to parse
			final StringBuilder builder = new StringBuilder();

			// when we are parsing 'true' we need 4 characters, 5 for 'false'
			// for loop is based on this principe:
			// for( <action>; <boolean to continue>; <action after> ) {}
			// We make great use of that ability
			for (int i = 0; i < (parseTrue ? 4 : 5) && (character = tokenizer.peek()) != null; i++) {
				builder.append(character);
			}

			try {
				return Boolean.parseBoolean(builder.toString());
				// Boolean.parseBoolean(String) throws an exception when the string is not 'true' or 'false'
				// We catch the exception and throw our own instead (so no argue can escape that this code does not work because it throws exceptions
			} catch (Exception e) {
				throw new SyntaxException(FORMAT_PARSE_BOOLEAN, parseTrue, builder.toString());
			}
		}
		// Nothing has been found
		return null;
	}
}

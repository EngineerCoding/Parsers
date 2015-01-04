package com.ameling.parser.math;

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

import com.ameling.parser.Parser;
import com.ameling.parser.SyntaxException;
import com.ameling.parser.Tokenizer;
import com.ameling.parser.math.functions.Function;

import java.util.ArrayList;
import java.util.List;

import static com.ameling.parser.Constants.CHAR_BRACKET_CLOSE;
import static com.ameling.parser.Constants.CHAR_BRACKET_OPEN;
import static com.ameling.parser.Constants.CHAR_COMMA;
import static com.ameling.parser.Constants.FORMAT_EXPECTED_CHAR;
import static com.ameling.parser.Constants.STRING_EMPTY;

public final class MathVariable extends Parser implements IComponent {

	/**
	 * A holder class so the parent class only has to handle with an {@link IComponent}
	 */
	private class Component implements IComponent {

		private static final String EXCEPTION_NO_VALID_DATA = "No valid data has been found";

		/**
		 * The variable name
		 */
		private final String variable;
		/**
		 * The array that gets returned in {@link #getVariables()}
		 */
		private final String[] variables;
		/**
		 * The number the variable will multiply with. Also holds the number when this instance is created with {@link #Component(double)}
		 */
		private final Double number;
		/**
		 * The variable value, which standards to 1.0 to avoid issues with dividing (yet it can be set to 0.0)
		 */
		private double value;

		/**
		 * Creates a new component which simulates a IComponent
		 *
		 * @param variable The variable name
		 * @param number   The value the variable multiplies with
		 * @throws SyntaxException When both variable and number are null, no valid data has been parsed then
		 */
		public Component(final String variable, final Double number) throws SyntaxException {
			this.variable = variable;
			this.number = number;

			variables = (variable != null ? new String[]{ variable } : new String[0]);

			if (variable == null && number == null)
				throw new SyntaxException(EXCEPTION_NO_VALID_DATA);
		}

		/**
		 * Creates a new component which simulates as a {@link IComponent} and only holds a number
		 *
		 * @param number The number to hold
		 */
		public Component(final double number) {
			variable = null;
			variables = new String[0];
			this.number = number;
		}

		@Override
		public boolean hasVariable() {
			return variable != null;
		}

		@Override
		public String[] getVariables() {
			return variables;
		}

		@Override
		public void setVariable(final String variable, final double value) {
			if (hasVariable() && this.variable.equals(variable))
				this.value = value;
		}

		@Override
		public double value() {
			if (hasVariable())
				return (number != null ? value * number : value);
			return number;
		}
	}


	// Constants for this class
	private static final String FIRST_CHAR_MATCH = "[a-zA-Z_]";
	private static final String MATCH_VARIABLE = "\\w";
	// End constants

	/**
	 * The component this class is
	 */
	private final IComponent component;

	/**
	 * Tries to parse either of the following:<br/>
	 * <ul>
	 * <li>A number</li>
	 * <li>A variable</li>
	 * <li>A number with a variable (for instance 5z)</li>
	 * </ul>
	 *
	 * @param tokenizer The tokenizer this component will use
	 * @throws SyntaxException when nothing is parsed. (it was not there)
	 */
	protected MathVariable(final Tokenizer tokenizer) throws SyntaxException {
		super(tokenizer);

		if (tokenizer.isNext(CHAR_BRACKET_OPEN)) {
			component = new MathExpression(tokenizer);
			if (!tokenizer.isNext(CHAR_BRACKET_CLOSE))
				throw new SyntaxException(FORMAT_EXPECTED_CHAR, CHAR_BRACKET_CLOSE);
		} else {
			// Parse the number to multiply with
			final Double number = parseNumber(false);

			// Parse the variable name
			Character character;
			final StringBuilder sb = new StringBuilder();
			while ((character = tokenizer.peek()) != null) {
				final String c = character.toString();
				if (sb.length() == 0 ? c.matches(FIRST_CHAR_MATCH) : c.matches(MATCH_VARIABLE)) {
					sb.append(tokenizer.pop());
				} else {
					break;
				}
			}

			if (tokenizer.isNext(CHAR_BRACKET_OPEN) && sb.length() != 0) {
				final Function function = Function.getFunction(sb.toString());
				final List<IComponent> expressions = new ArrayList<IComponent>();
				expressions.add(new MathExpression(tokenizer));

				while (tokenizer.isNext(CHAR_COMMA))
					expressions.add(new MathExpression(tokenizer));

				if (!tokenizer.isNext(CHAR_BRACKET_CLOSE))
					throw new SyntaxException(FORMAT_EXPECTED_CHAR, CHAR_BRACKET_CLOSE);

				component = new MathFunction(function, expressions.toArray(new IComponent[expressions.size()]));
				return;
			}

			final String variable = sb.toString();
			component = new Component(STRING_EMPTY.equals(variable) ? null : variable, number);
		}
	}

	/**
	 * Constructor which is just a placeholder for an ordinary number
	 *
	 * @param value The value it represents in the {@link #value()} method
	 */
	public MathVariable(double value) {
		super(null);
		component = new Component(value);
	}

	@Override
	public boolean hasVariable() {
		return component.hasVariable();
	}

	@Override
	public String[] getVariables() {
		return component.getVariables();
	}

	@Override
	public void setVariable(final String variable, final double value) {
		component.setVariable(variable, value);
	}

	@Override
	public double value() {
		return component.value();
	}
}

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

import com.ameling.parser.SyntaxException;
import com.ameling.parser.math.functions.Function;

import java.util.ArrayList;
import java.util.List;

public final class MathFunction implements IComponent {

	private static final String EXCEPTION_NONEXISTENT_FUNCTION = "Nonexistent function";
	private static final String EXCEPTION_OUT_OF_RANGE = "Length of arguments: %d out of range (length >= %d && %d <= length)";

	private final Function function;
	private final IComponent[] arguments;
	private final String[] variables;

	protected MathFunction(final Function function, final IComponent[] arguments) {
		if (function == null)
			throw new SyntaxException(EXCEPTION_NONEXISTENT_FUNCTION);

		this.function = function;
		this.arguments = arguments;

		final List<String> variables = new ArrayList<String>();
		for (final IComponent component : arguments) {
			for (final String var : component.getVariables()) {
				if (!variables.contains(var))
					variables.add(var);
			}
		}
		this.variables = variables.toArray(new String[variables.size()]);

		final int length = arguments.length;
		if (!(length >= function.getMin() && length <= function.getMax()))
			throw new SyntaxException(EXCEPTION_OUT_OF_RANGE, length, function.getMin(), function.getMax());
	}

	@Override
	public boolean hasVariable() {
		return variables.length > 0;
	}

	@Override
	public String[] getVariables() {
		return variables;
	}

	@Override
	public void setVariable(final String variable, final double value) {
		if (variable != null) {
			for (final IComponent component : arguments) {
				if (component.hasVariable()) {
					// Simply set the variable, if the component contains the variable, it will set that variable.
					// When not available, it should do nothing
					component.setVariable(variable, value);
					// Don't break because multiple components can contain the same variable
				}
			}
		}
	}

	@Override
	public double value() {
		final double[] values = new double[arguments.length];
		for (int i = 0; i < arguments.length; i++)
			values[i] = arguments[i].value();

		return function.calculate(values);
	}
}

package com.ameling.parser.grade;

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

/**
 * Holds a grade and the value of it. This is based on weighting, and this is the object the
 * parser will parse into.
 *
 * @author Wesley A
 */
public class Grade implements Cloneable {

	/**
	 * The name of this grade
	 */
	public final String name;

	/**
	 * The weighting of this grade
	 */
	public final int weighting;

	/**
	 * Creates a new grade with the name and weighting
	 *
	 * @param name      The name of this grade
	 * @param weighting The weighting of this grade
	 */
	public Grade (final String name, final int weighting) {
		this.name = name;
		this.weighting = weighting;
	}

	/**
	 * This value is used {@link Calculator}
	 */
	protected double value;

	/**
	 * A boolean flag whether it is set or not. Used in {@link #reset}, {@link #setValue(double)} and {@link Calculator}
	 */
	protected boolean isSet = false;

	/**
	 * Sets the grade value to this value.
	 *
	 * @param grade The value to set to
	 */
	public void setValue (final double grade) {
		value = grade;
		if (!isSet)
			isSet = true;
	}

	/**
	 * Returns the current grade value
	 *
	 * @return The grade value or 0 when it is not set. To check properly use {@link #hasValue}
	 */
	public double getValue () {
		return value;
	}

	/**
	 * Returns if the {@link #value} field is set through {@link #setValue(double)}
	 *
	 * @return If this object has a grade value
	 */
	public boolean hasValue () {
		return isSet;
	}

	/**
	 * Resets this grade's value (it is not set after calling this)
	 */
	public void reset () {
		isSet = false;
	}

	@Override
	public Grade clone () {
		final Grade grade = new Grade(name, weighting);
		if (hasValue())
			grade.setValue(getValue());
		return grade;
	}
}

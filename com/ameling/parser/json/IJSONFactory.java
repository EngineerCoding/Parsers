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

/**
 * This factory is intended to be used by users. In the future it can be expected that this interface actually has a function in this package, but for now it is just a nice
 * place to create JSON objects and your wished objects. It is recommended to make an implementation of this in a singleton class.
 *
 * @author Wesley A
 */
public interface IJSONFactory<T> {

	/**
	 * Create a JSON object/array from the given class
	 *
	 * @param object The object to make a JSON object/array from
	 * @return A JSON object/array which can be turned into the original object
	 */
	public JSON createJSON (final T object);

	/**
	 * Creates an instance from the JSON object/array returned by {@link #createJSON(T)}
	 *
	 * @param json The JSON to use
	 * @return A object which is similar to input object of {@link #createJSON(T)}
	 * @throws JSONException when the JSON object/array is invalid
	 */
	public T createInstance (final JSON json) throws JSONException;

}

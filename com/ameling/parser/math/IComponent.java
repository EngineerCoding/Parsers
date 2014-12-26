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

package com.ameling.parser.math;

/**
 * This interface will make communicating with variables bearable
 */
public interface IComponent {

    /**
     * Checks if the component has a variable
     * @return Whether the component has a variable
     */
    public boolean hasVariable();

    /**
     * Get the variables for this component
     * @return the variables of the component
     */
    public String[] getVariables();

    /**
     * Sets the variable value
     * @param variable Which variable it is
     * @param value The new value of said variable
     */
    public void setVariable(final String variable, final double value);

    /**
     * Gets the value for this component, when a variable is not set, 0 should be assumed
     * @return the value of this component
     */
    public double value();

}

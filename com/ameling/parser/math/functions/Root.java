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

package com.ameling.parser.math.functions;

/**
 * One can calculate the root of a number<br/>
 * With one argument it calculates the square root of a number, with two  arguments it calculates the root with the second number
 */
public class Root extends Function {

    protected Root() {
        super("root", 1, 2);
    }

    @Override
    public double calculate(final double ... args) {
        if(args.length == 1)
            return calculate(new double[] {args[0], 2});
        return Math.pow(args[0], 1 / args[1]);
    }

}

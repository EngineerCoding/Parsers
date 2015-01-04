package com.ameling.parser.math.functions;

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
 * This class holds all trigonometry functions:
 * <ul>
 * <li>Sin</li>
 * <li>Sin inverse</li>
 * <li>Cos</li>
 * <li>Cos inverse</li>
 * <li>Tan</li>
 * <li>Tan inverse</li>
 * </ul>
 * All of those functions are in radian. When one needs this when using degrees, use {@link Trigonometry#Rad}
 */
public abstract class Trigonometry extends Function {

	private Trigonometry(final String name) {
		super(name, 1, 1);
	}

	/**
	 * The sine
	 */
	protected static final Function Sin = new Trigonometry("sin") {
		@Override
		public double calculate(double... args) {
			return Math.sin(args[0]);
		}
	};

	/**
	 * The sine inverse
	 */
	protected static final Function SinInverse = new Trigonometry("asin") {
		@Override
		public double calculate(double... args) {
			return Math.asin(args[0]);
		}
	};

	/**
	 * The cosine
	 */
	protected static final Function Cos = new Trigonometry("cos") {
		@Override
		public double calculate(double... args) {
			return Math.cos(args[0]);
		}
	};

	/**
	 * The cosine inverse
	 */
	protected static final Function CosInverse = new Trigonometry("acos") {
		@Override
		public double calculate(double... args) {
			return Math.acos(args[0]);
		}
	};

	/**
	 * The tangent
	 */
	protected static final Function Tan = new Trigonometry("tan") {
		@Override
		public double calculate(double... args) {
			return Math.tan(args[0]);
		}
	};

	/**
	 * The tangent inverse
	 */
	protected static final Function TanInverse = new Trigonometry("atan") {
		@Override
		public double calculate(double... args) {
			return Math.atan(args[0]);
		}
	};

	/**
	 * Converts degrees to radian
	 */
	protected static final Function Rad = new Trigonometry("rad") {
		@Override
		public double calculate(double... args) {
			return args[0] * Math.PI / 180;
		}
	};

	/**
	 * Converts radians into degrees
	 */
	protected static final Function Deg = new Trigonometry("deg") {
		@Override
		public double calculate(double... args) {
			return args[0] * 180 / Math.PI;
		}
	};
}

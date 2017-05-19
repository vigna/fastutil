package it.unimi.dsi.fastutil;

/*
 * Copyright (C) 2010-2017 Sebastiano Vigna
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

public final class SafeMath {
	private SafeMath() {}

	public static char safeIntToChar(final int value) {
		if (value < (int) Character.MIN_VALUE || (int) Character.MAX_VALUE < value) {
			throw new IllegalArgumentException(value + " can't be represented as char");
		}
		return (char) value;
	}

	public static byte safeIntToByte(final int value) {
		if (value < (int) Byte.MIN_VALUE || (int) Byte.MAX_VALUE < value) {
			throw new IllegalArgumentException(value + " can't be represented as byte (out of range)");
		}
		return (byte) value;
	}

	public static short safeIntToShort(final int value) {
		if (value < (int) Short.MIN_VALUE || (int) Short.MAX_VALUE < value) {
			throw new IllegalArgumentException(value + " can't be represented as short (out of range)");
		}
		return (short) value;
	}

	public static int safeLongToInt(final long value) {
		if (value < (long) Integer.MIN_VALUE || (long) Integer.MAX_VALUE < value ) {
			throw new IllegalArgumentException(value + " can't be represented as int (out of range)");
		}
		return (int) value;
	}

	public static float safeDoubleToFloat(final double value) {
		if (Double.isNaN(value)) {
			return Float.NaN;
		}
		if (Double.isInfinite(value)) {
			return value < 0.0d ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
		}
		if (value < (double) Float.MIN_VALUE || (double) Float.MAX_VALUE < value) {
			throw new IllegalArgumentException(value + " can't be represented as float (out of range)");
		}
		final float floatValue = (float) value;
		if (((double) floatValue) != value) {
			throw new IllegalArgumentException(value + " can't be represented as float (imprecise)");
		}
		return floatValue;
	}
}

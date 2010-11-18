package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2002-2010 Sebastiano Vigna 
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


/** Common code for all hash-based classes.
 *
 * All hashing in <code>fastutil</code> is performed starting from a 32-bit integer
 * associated to a key or value. For all integer types smaller than <code>long</code>, we
 * just cast. In all other cases, we do some conversion using static code in this
 * class. Note that we follow the conventions established by the various classes
 * associated to primitive types ({@link Boolean}, {@link Double}, etc.).
 */

public class HashCommon {

	/** This reference is used to fill keys and values of removed entries (if
		they are objects). <code>null</code> cannot be used as it would confuse the
		search algorithm in the presence of an actual <code>null</code> key. */ 
	public static final Object REMOVED = new Object();

	private HashCommon() {};

	/** Returns the hash code that would be returned by {@link Float#hashCode()}.
	 *
	 * @return the same code as {@link Float#hashCode() new Float(f).hashCode()}.
	 */

	final public static int float2int( final float f ) {
		return Float.floatToRawIntBits( f );
	}

	/** Returns the hash code that would be returned by {@link Double#hashCode()}.
	 *
	 * @return the same code as {@link Double#hashCode() new Double(f).hashCode()}.
	 */

	final public static int double2int( final double d ) {
		final long l = Double.doubleToRawLongBits( d );
		return (int)( l ^ ( l >>> 32 ) );
	}

	/** Returns the hash code that would be returned by {@link Long#hashCode()}.
	 *
	 * @return the same code as {@link Long#hashCode() new Long(f).hashCode()}.
	 */
	final public static int long2int( final long l ) {
		return (int)( l ^ ( l >>> 32 ) );
	}
}

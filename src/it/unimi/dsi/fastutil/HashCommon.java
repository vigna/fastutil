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

	/** Avalanches the bits of an integer.
	 * 
	 * <p>This function implements the finalisation step of Austin Appleby's <a href="http://sites.google.com/site/murmurhash/">MurmurHash3</a>.
	 * Its purpose is to avalanche the bits of the argument to within 0.25% bias. It is used, among other things, to scramble quickly (but deeply) the hash
	 * values returned by {@link Object#hashCode()}.
	 * 
	 * @param x an integer.
	 * @return a hash value with good avalanche properties.
	 */	
	public final static int avalanche( int x ) {
		x ^= x >>> 16;
		x *= 0x85ebca6b;
		x ^= x >>> 13;
		x *= 0xc2b2ae35;
		x ^= x >>> 16;
		return x;
	}

	
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

	/** Returns a hash code obtained by avalanching the 32 high bits of the argument, and XORing them with the 32 low bits.
	 * 
	 * <p><strong>Warning:</strong> this method up to <code>fastutil</code> 6.0.0
	 * used to return the same code as {@link Long#hashCode() new Long(f).hashCode()}.
	 * However, that hash code has worst statistical properties: I have personally once used a hash table of longs to keep track of pairs
	 * of integers (in high/low bits), and that hash code (i.e., <code>(int)( l ^ ( l >>> 32 ) )</code>) would have given hash 0
	 * to all pairs &lt;<var>x</var>, <var>x</var>>.
	 *
	 * @return <code>(int)( l ^ avalanche( (int)( l >>> 32 ) ) </code>.
	 */
	final public static int long2int( final long l ) {
		return (int)( l ^ avalanche( (int)( l >>> 32 ) ) );
	}
	
	/** Return the least power of two greater than or equal to the specified value.
	 * 
	 * <p>Note that this function will return 1 when the argument is 0.
	 * 
	 * @param x an integer.
	 * @return the least power of two greater than or equal to the specified value.
	 */
	public static int nextPowerOfTwo( int x ) {
		if ( x == 0 ) return 1;
		x--;
		x |= x >> 1;
		x |= x >> 2;
		x |= x >> 4;
		x |= x >> 8;
		x |= x >> 16;
		x++;
		return x;
	}

	/** Return the least power of two greater than or equal to the specified value.
	 * 
	 * <p>Note that this function will return 1 when the argument is 0.
	 * 
	 * @param x a long integer.
	 * @return the least power of two greater than or equal to the specified value.
	 */
	public static long nextPowerOfTwo( long x ) {
		if ( x == 0 ) return 1;
		x--;
		x |= x >> 1;
		x |= x >> 2;
		x |= x >> 4;
		x |= x >> 8;
		x |= x >> 16;
		x |= x >> 32;
		x++;
		return x;
	}
}
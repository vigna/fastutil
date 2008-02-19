package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2002-2008 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
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

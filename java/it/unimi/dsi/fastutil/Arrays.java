/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2002, 2003 Sebastiano Vigna 
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

package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.shorts.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.objects.*;

import it.unimi.dsi.fastutil.Iterators;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.NoSuchElementException;

/** A class providing static methods that do useful things with arrays.
 *
 * @see Arrays
 */

public class Arrays {
	
	private Arrays() {}

	/** Ensures that a range given by its first (inclusive) and last (exclusive) elements fits an array of given length.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param arrayLength an array length.
	 * @param from a start index (inclusive).
	 * @param to an end index (inclusive).
	 * @throws IllegalArgumentException if <code>from</code> is greater than <code>to</code>.
	 * @throws ArrayIndexOutOfBoundsException if <code>from</code> or <code>to</code> are greater than <code>arrayLength</code> or negative.
	 */
	public static void ensureFromTo( final int arrayLength, final int from, final int to ) {
		if ( from < 0 ) throw new ArrayIndexOutOfBoundsException( "Start index (" + from + ") is negative" );
		if ( from > to ) throw new IllegalArgumentException( "Start index (" + from + ") is greater than end index (" + to + ")" );
		if ( to > arrayLength ) throw new ArrayIndexOutOfBoundsException( "End index (" + to + ") is greater than array length (" + arrayLength + ")" );
	}

	/** Ensures that a range given by an offset and a length fits an array of given length.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param arrayLength an array length.
	 * @param offset a start index for the fragment
	 * @param length a length (the number of elements in the fragment).
	 * @throws IllegalArgumentException if <code>length</code> is negative.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset</code> is negative or <code>offset</code>+<code>length</code> is greater than <code>arrayLength</code>.
	 */
	public static void ensureOffsetLength( final int arrayLength, final int offset, final int length ) {
		if ( offset < 0 ) throw new ArrayIndexOutOfBoundsException( "Offset (" + offset + ") is negative" );
		if ( length < 0 ) throw new IllegalArgumentException( "Length (" + length + ") is negative" );
		if ( offset + length > arrayLength ) throw new ArrayIndexOutOfBoundsException( "Last index (" + ( offset + length ) + ") is greater than array length (" + arrayLength + ")" );
	}

}

// Local Variables:
// mode: jde
// tab-width: 4
// End:

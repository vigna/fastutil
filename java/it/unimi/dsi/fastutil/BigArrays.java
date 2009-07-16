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


/** A class providing static methods and objects that do useful things with big arrays.
 *
 * @see it.unimi.dsi.fastutil.Arrays
 */

public class BigArrays {
	public final static int SEGMENT_SHIFT = 30;
	public final static int SEGMENT_SIZE =  1 << SEGMENT_SHIFT;
	public final static int SEGMENT_MASK = SEGMENT_SIZE - 1;
	
	public static int segment( final long index ) {
		return (int)( index >> SEGMENT_SHIFT );
	}
	
	public static int offset( final long index ) {
		return (int)( index & SEGMENT_MASK );
	}
	
	
	private BigArrays() {}

	/** Ensures that a range given by its first (inclusive) and last (exclusive) elements fits an array of given length.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param bigArrayLength a big-array length.
	 * @param from a start index (inclusive).
	 * @param to an end index (inclusive).
	 * @throws IllegalArgumentException if <code>from</code> is greater than <code>to</code>.
	 * @throws ArrayIndexOutOfBoundsException if <code>from</code> or <code>to</code> are greater than <code>bigArrayLength</code> or negative.
	 */
	public static void ensureFromTo( final long bigArrayLength, final long from, final long to ) {
		if ( from < 0 ) throw new ArrayIndexOutOfBoundsException( "Start index (" + from + ") is negative" );
		if ( from > to ) throw new IllegalArgumentException( "Start index (" + from + ") is greater than end index (" + to + ")" );
		if ( to > bigArrayLength ) throw new ArrayIndexOutOfBoundsException( "End index (" + to + ") is greater than big-array length (" + bigArrayLength + ")" );
	}

	/** Ensures that a range given by an offset and a length fits an array of given length.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param bigArrayLength a big-array length.
	 * @param offset a start index for the fragment
	 * @param length a length (the number of elements in the fragment).
	 * @throws IllegalArgumentException if <code>length</code> is negative.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset</code> is negative or <code>offset</code>+<code>length</code> is greater than <code>bigArrayLength</code>.
	 */
	public static void ensureOffsetLength( final long bigArrayLength, final long offset, final long length ) {
		if ( offset < 0 ) throw new ArrayIndexOutOfBoundsException( "Offset (" + offset + ") is negative" );
		if ( length < 0 ) throw new IllegalArgumentException( "Length (" + length + ") is negative" );
		if ( offset + length > bigArrayLength ) throw new ArrayIndexOutOfBoundsException( "Last index (" + ( offset + length ) + ") is greater than big-array length (" + bigArrayLength + ")" );
	}

}

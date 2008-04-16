package it.unimi.dsi.fastutil.io;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */

import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.io.IOException;
import java.io.OutputStream;

/** Simple, fast byte-array output stream that exposes the backing array.
 *
 * <P>{@link java.io.ByteArrayOutputStream} is nice, but to get its content you
 * must generate each time a new object. This doesn't happen here.
 *
 * <P>This class will automatically enlarge the backing array, doubling its
 * size whenever new space is needed. The {@link #reset()} method will
 * mark the content as empty, but will not decrease the capacity: use 
 * {@link #trim()} for that purpose.
 *
 * @author Sebastiano Vigna
 */

public class FastByteArrayOutputStream extends OutputStream implements RepositionableStream {

	/** The array backing the output stream. */
	public final static int DEFAULT_INITIAL_CAPACITY = 16;

	/** The array backing the output stream. */
	public byte[] array;

	/** The number of valid bytes in {@link #array}. */
	public int length;

	/** The current writing position. */
	private int position;

	/** Creates a new array output stream with an initial capacity of {@link #DEFAULT_INITIAL_CAPACITY} bytes. */
	public FastByteArrayOutputStream() {
		this( DEFAULT_INITIAL_CAPACITY );
	}

	/** Creates a new array output stream with a given initial capacity.
	 *
	 * @param initialCapacity the initial length of the backing array.
	 */
	public FastByteArrayOutputStream( final int initialCapacity ) {
		array = new byte[ initialCapacity ];
	}

	/** Creates a new array output stream wrapping a given byte array.
	 *
	 * @param a the byte array to wrap.
	 */
	public FastByteArrayOutputStream( final byte[] a ) {
		array = a;
	}

	/** Marks this array output stream as empty. */
	public void reset() {
		length = 0;
		position = 0;
	}

	/** Ensures that the length of the backing array is equal to {@link #length}. */
	public void trim() {
		array = ByteArrays.trim( array, length );
	}

	public void write( final int b ) {
		if ( position == length ) {
			length++;
			if ( position == array.length ) array = ByteArrays.grow( array, length );
		}
		array[ position++ ] = (byte)b;
	}

	public void write( final byte[] b, final int off, final int len ) throws IOException {
		ByteArrays.ensureOffsetLength( b, off, len );
		if ( position + len > array.length ) array = ByteArrays.grow( array, position + len, position );
		System.arraycopy( b, off, array, position, len );
		if ( position + len > length ) length = position += len;
	}

	public void position( long newPosition ) {
		if ( position > Integer.MAX_VALUE ) throw new IllegalArgumentException( "Position too large: " + newPosition );
		position = (int)newPosition;
	}

	public long position() {
		return position;
	}
}

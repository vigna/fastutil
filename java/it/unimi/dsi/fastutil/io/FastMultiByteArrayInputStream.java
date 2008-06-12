package it.unimi.dsi.fastutil.io;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Paolo Boldi and Sebastiano Vigna 
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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


/** Simple, fast and repositionable byte array input stream that multiplexes its content among several arrays.
 * 
 * This class is significantly slower than {@link FastByteArrayInputStream},
 * but it can hold 256 PiB of data. The relevant constructor is {@link #FastMultiByteArrayInputStream(InputStream, long)},
 * which fetches a stream and loads it into a sequence of byte arrays.
 *
 * @author Sebastiano Vigna
 * @author Paolo Boldi
s */

public class FastMultiByteArrayInputStream extends java.io.InputStream implements RepositionableStream {

	/** The number of bits of an array slice index. */
	public final static int SLICE_BITS = 30;

	/** The maximum length of an array slice. */
	public final static int SLICE_SIZE = 1 << SLICE_BITS;
	
	/** The mask to retrieve a slice offset. */
	public final static int SLICE_MASK = SLICE_SIZE - 1;

	/** The array of arrays backing the input stream. */
	public byte[][] array;

	/** The number of valid bytes in {@link #array}. */
	public long length;

	/** The current position. */
	private long position;

	/** The current mark, or -1 if no mark exists. */
	private long mark;

	/** Creates a new multi-array input stream loading it from a measurable input stream.
	 *
	 * @param is the input stream that will fill the array.
	 */
	
	public FastMultiByteArrayInputStream( final MeasurableInputStream is ) throws IOException {
		this( is, is.length() );
	}

	/** Creates a new multi-array input stream loading it from an input stream.
	 *
	 * @param is the input stream that will fill the array.
	 * @param size the number of bytes to be read from <code>is</code>.
	 */
	
	public FastMultiByteArrayInputStream( final InputStream is, long size ) throws IOException {
		length = size;
		array = new byte[ (int)( ( size + SLICE_SIZE - 1 ) / SLICE_SIZE ) ][];

		for( int i = 0; i < array.length; i++ ) {
			array[ i ] = new byte[ size >= SLICE_SIZE ? SLICE_SIZE : (int)size ];
			if ( is.read( array[ i ] ) != array[ i ].length ) throw new EOFException();
			size -= array[ i ].length;
		}
	}

	/** Creates a new multi-array input stream sharing the backing arrays of another multi-array input stream. 
	 *
	 * @param is the multi-array input stream to replicate.
	 */
	public FastMultiByteArrayInputStream( final FastMultiByteArrayInputStream is ) {
		this.array = is.array;
		this.length = is.length;
	}


	/** Creates a new multi-array input stream using a given array. 
	 *
	 * @param array the backing array.
	 */
	public FastMultiByteArrayInputStream( final byte[] array ) {
		this.array = new byte[ 1 ][];
		this.array[ 0 ] = array;
		this.length = array.length;
	}



	public boolean markSupported() {
		return true;
	}

	public void reset() {
		position = mark;
	}

	/** Closing a fast byte array input stream has no effect. */
	public void close() {}

	public void mark( final int dummy ) {
		mark = position;
	}

	/** Returns the number of bytes that can be read (or skipped over) from this input stream without blocking. 
	 *
	 * <P>Note that this number may be smaller than the number of bytes actually
	 * available from the stream if this number exceeds {@link Integer#MAX_VALUE}.
	 *
	 * @return the minimum among the number of available bytes and {@link Integer#MAX_VALUE}.
	 */

	public int available() {
		if ( length - position > Integer.MAX_VALUE ) return Integer.MAX_VALUE;
		return (int)( length - position );
	}

	public long skip( long n ) {
		if ( n <= length - position ) {
			position += n;

			return n;
		}
		n = length - position;
		position = length;
		return n;
	}

	public int read() {
		if ( length == position ) return -1;
		return array[ (int)( position >> SLICE_BITS ) ][ (int)( position++ & SLICE_MASK ) ] & 0xFF;
	}

	public int read( final byte[] b, int offset, final int length ) {
		if ( this.length == this.position ) return length == 0 ? 0 : -1;
		int res, n = (int)Math.min( length, this.length - this.position ), m = n;

		do {
			res = Math.min( n, array[ (int)( position >> SLICE_BITS ) ].length - (int)( position & SLICE_MASK ) );
			System.arraycopy( array[ (int)( position >> SLICE_BITS ) ], (int)( position & SLICE_MASK ),
				b, offset, res );
				
			n -= res;
			offset += res;
			position += res;
			
		} while( n > 0 );

		return m;
	}

	public long position() {
		return position;
	}

	public void position( final long newPosition ) {
		position = Math.min( newPosition, length );
	}
}

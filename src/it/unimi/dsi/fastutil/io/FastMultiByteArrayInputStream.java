package it.unimi.dsi.fastutil.io;

/*		 
 * Copyright (C) 2005-2014 Sebastiano Vigna 
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

public class FastMultiByteArrayInputStream extends MeasurableInputStream implements RepositionableStream {

	/** The number of bits of an array slice index. */
	public final static int SLICE_BITS = 10;

	/** The maximum length of an array slice. */
	public final static int SLICE_SIZE = 1 << SLICE_BITS;
	
	/** The mask to retrieve a slice offset. */
	public final static int SLICE_MASK = SLICE_SIZE - 1;

	/** The array of arrays backing the input stream, plus an additional {@code null} entry. */
	public byte[][] array;

	/** The current array. */
	public byte[] current;

	/** The number of valid bytes in {@link #array}. */
	public long length;

	/** The current position. */
	private long position;

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
		array = new byte[ (int)( ( size + SLICE_SIZE - 1 ) / SLICE_SIZE ) + 1 ][];

		for( int i = 0; i < array.length - 1; i++ ) {
			array[ i ] = new byte[ size >= SLICE_SIZE ? SLICE_SIZE : (int)size ];
			// It is important *not* to use is.read() directly because of bug #6478546
			if ( BinIO.loadBytes( is, array[ i ] ) != array[ i ].length ) throw new EOFException();
			size -= array[ i ].length;
		}

		current = array[ 0 ];
	}

	/** Creates a new multi-array input stream sharing the backing arrays of another multi-array input stream. 
	 *
	 * @param is the multi-array input stream to replicate.
	 */
	public FastMultiByteArrayInputStream( final FastMultiByteArrayInputStream is ) {
		this.array = is.array;
		this.length = is.length;
		this.current = array[ 0 ];
	}


	/** Creates a new multi-array input stream using a given array. 
	 *
	 * @param array the backing array.
	 */
	public FastMultiByteArrayInputStream( final byte[] array ) {
		if ( array.length == 0 ) this.array = new byte[ 1 ][];
		else {
			this.array = new byte[ 2 ][];
			this.array[ 0 ] = array;
			this.length = array.length;
			this.current = array;
		}
	}

	/** Returns the number of bytes that can be read (or skipped over) from this input stream without blocking. 
	 *
	 * <P>Note that this number may be smaller than the number of bytes actually
	 * available from the stream if this number exceeds {@link Integer#MAX_VALUE}.
	 *
	 * @return the minimum among the number of available bytes and {@link Integer#MAX_VALUE}.
	 */

	public int available() {
		return (int)Math.min( Integer.MAX_VALUE, length - position );
	}

	public long skip( long n ) {
		if ( n > length - position ) n = length - position;
		position += n;
		updateCurrent();
		return n;
	}

	public int read() {
		if ( length == position ) return -1;
		final int disp = (int)( position++ & SLICE_MASK );
		if ( disp == 0 ) updateCurrent();
		return current[ disp ] & 0xFF;
	}

	public int read( final byte[] b, int offset, final int length ) {
		final long remaining = this.length - position;
		if ( remaining == 0 ) return length == 0 ? 0 : -1;
		int n = (int)Math.min( length, remaining );
		final int m = n;

		for(;;) {
			final int disp = (int)( position & SLICE_MASK );
			if ( disp == 0 ) updateCurrent();
			final int res = Math.min( n, current.length - disp );
			System.arraycopy( current, disp, b, offset, res );
				
			n -= res;
			position += res;
			if ( n == 0 ) return m;
			offset += res;
		}
	}

	private void updateCurrent() {
		current = array[ (int)( position >>> SLICE_BITS ) ];
	}
	
	public long position() {
		return position;
	}

	public void position( final long newPosition ) {
		position = Math.min( newPosition, length );
		updateCurrent();
	}

	@Override
	public long length() throws IOException {
		return length;
	}

	/** NOP. */
	public void close() {}

	public boolean markSupported() {
		return false;
	}

	public void mark( final int dummy ) {
		throw new UnsupportedOperationException();
	}

	public void reset() {
		throw new UnsupportedOperationException();
	}
}

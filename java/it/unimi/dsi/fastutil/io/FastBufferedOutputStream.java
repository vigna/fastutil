/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2005 Sebastiano Vigna 
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


package it.unimi.dsi.fastutil.io;

import java.io.IOException;
import java.io.OutputStream;

/** Lightweight, unsynchronised, aligned output stream buffering class.
 *
 * <P>This class provides buffering for output streams, but it does so with 
 * purposes and an internal logic that are radically different from the ones
 * adopted in {@link java.io.BufferedOutputStream}.
 * 
 * <P>All methods are unsychronised. Moreover,
 * it is guaranteed that
 * <em>all writes performed by this class will be
 * multiples of the given buffer size</em>.  If, for instance, you use the
 * default buffer size, writes will be performed on the underlying input stream
 * in multiples of 16384 bytes. This is very important on operating systems
 * that optimise disk reads on disk block boundaries. If you {@link #flush()} the stream,
 * the buffer will be emptied, but it will realign again as soon as possible.
 *
 * @since 4.4
 */

public class FastBufferedOutputStream extends OutputStream {

	/** The default size of the internal buffer in bytes (8Ki). */
	public final static int DEFAULT_BUFFER_SIZE = 8 * 1024;

	/** The internal buffer. */
	protected byte buffer[];

	/** The current position in the buffer. */
	protected int pos;

	/** The number of buffer bytes available starting from {@link #pos}. Note that in case
	 * {@link #flush()} has been called, the number of available buffer bytes might be less
	 * than {@link #buffer buffer.length}&minus;{@link #pos}. */
	protected int avail;

	/** The underlying output stream. */
	protected OutputStream os;

	/** Creates a new fast buffered output stream by wrapping a given output stream with a given buffer size. 
	 *
	 * @param os an output stream to wrap.
	 * @param bufSize the size in bytes of the internal buffer.
	 */

	public FastBufferedOutputStream( final OutputStream os, final int bufSize ) {
		this.os = os;
		buffer = new byte[ bufSize ];
		avail = bufSize;
	}

	/** Creates a new fast buffered ouptut stream by wrapping a given output stream with a buffer of {@link #DEFAULT_BUFFER_SIZE} bytes. 
	 *
	 * @param os an output stream to wrap.
	 */
	public FastBufferedOutputStream( final OutputStream os ) {
		this( os, DEFAULT_BUFFER_SIZE );
	}

	private void dumpBufferIfFull() throws IOException {
		if ( avail == 0 ) {
			os.write( buffer, 0, pos );
			pos = 0;
			avail = buffer.length;
		}
	}

	public void write( final int b ) throws IOException {
		avail--;
		buffer[ pos++ ] = (byte)b;
		dumpBufferIfFull();
	}


	public void write( final byte b[], int offset, int length ) throws IOException {
		if ( length <= avail ) {
			System.arraycopy( b, offset, buffer, pos, length );
			pos += length;
			avail -= length;
			dumpBufferIfFull();
			return;
		}
	
		System.arraycopy( b, offset, buffer, pos, avail );
		os.write( buffer, 0, pos + avail );

		offset += avail;
		length -= avail;

		final int residual = length % buffer.length;

		os.write( b, offset, length - residual ); 
		System.arraycopy( b, offset + length - residual, buffer, 0, residual );
		pos = residual;
		avail = buffer.length - residual;
	}

	public void flush() throws IOException {
		if ( pos != 0 ) os.write( buffer, 0, pos );
		// Note that avail is unchanged, so we will realign at the next dump.
		pos = 0;
		os.flush();
	}
	
	public void close() throws IOException {
		if ( os == null ) return;
		if ( pos != 0 ) os.write( buffer, 0, pos );
		if ( os != System.out ) os.close();
		os = null;
		buffer = null;
	}

}
	

// Local Variables:
// mode: jde
// tab-width: 4
// End:

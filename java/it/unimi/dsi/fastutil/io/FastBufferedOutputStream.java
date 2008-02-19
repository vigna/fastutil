package it.unimi.dsi.fastutil.io;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2005-2008 Sebastiano Vigna 
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

import java.io.IOException;
import java.io.OutputStream;

/** Lightweight, unsynchronized output stream buffering class.
 *
 * <P>This class provides buffering for output streams, but all methods are unsynchronised.
 * 
 * @since 4.4
 */

public class FastBufferedOutputStream extends OutputStream {
	private static final boolean ASSERTS = false;
	
	/** The default size of the internal buffer in bytes (8Ki). */
	public final static int DEFAULT_BUFFER_SIZE = 8 * 1024;

	/** The internal buffer. */
	protected byte buffer[];

	/** The current position in the buffer. */
	protected int pos;

	/** The number of buffer bytes available starting from {@link #pos} 
	 * (it must be always equal to <code>buffer.length - pos</code>). */
	protected int avail;

	/** The underlying output stream. */
	protected OutputStream os;

	/** Creates a new fast buffered output stream by wrapping a given output stream with a given buffer size. 
	 *
	 * @param os an output stream to wrap.
	 * @param bufferSize the size in bytes of the internal buffer.
	 */

	public FastBufferedOutputStream( final OutputStream os, final int bufferSize ) {
		this.os = os;
		buffer = new byte[ bufferSize ];
		avail = bufferSize;
	}

	/** Creates a new fast buffered ouptut stream by wrapping a given output stream with a buffer of {@link #DEFAULT_BUFFER_SIZE} bytes. 
	 *
	 * @param os an output stream to wrap.
	 */
	public FastBufferedOutputStream( final OutputStream os ) {
		this( os, DEFAULT_BUFFER_SIZE );
	}

	private void dumpBuffer( final boolean ifFull ) throws IOException {
		if ( ! ifFull || avail == 0 ) {
			os.write( buffer, 0, pos );
			pos = 0;
			avail = buffer.length;
		}
	}

	public void write( final int b ) throws IOException {
		if ( ASSERTS ) assert avail > 0;
		avail--;
		buffer[ pos++ ] = (byte)b;
		dumpBuffer( true );
	}


	public void write( final byte b[], final int offset, final int length ) throws IOException {
		if ( length >= buffer.length ) {
			dumpBuffer( false );
			os.write( b, offset, length );
			return;
		}
		
		if ( length <= avail ) {
			// Copy in buffer
			System.arraycopy( b, offset, buffer, pos, length );
			pos += length;
			avail -= length;
			dumpBuffer( true );
			return;
		}
		
		dumpBuffer( false );
		System.arraycopy( b, offset, buffer, 0, length );
		pos = length;
		avail -= length;
	}

	public void flush() throws IOException {
		dumpBuffer( false );
		os.flush();
	}
	
	public void close() throws IOException {
		if ( os == null ) return;
		flush();
		if ( os != System.out ) os.close();
		os = null;
		buffer = null;
	}
}

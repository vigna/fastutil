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
import java.io.InputStream;
import java.nio.channels.FileChannel;

/** Lightweight, unsynchronised, aligned input stream buffering class.
 *
 * <P>This class provides buffering for input streams, but it does so with 
 * purposes and an internal logic that are radically different from the ones
 * adopted in {@link java.io.BufferedInputStream}.
 * 
 * <P>There is no support for marking. All methods are unsychronised. Moreover,
 * it is guaranteed that <em>all reads performed by this class will be
 * multiples of the given buffer size</em>.  If, for instance, you use the
 * default buffer size, reads will be performed on the underlying input stream
 * in multiples of 16384 bytes. This is very important on operating systems
 * that optimise disk reads on disk block boundaries.
 * 
 * <P>As an additional feature, this class implements the {@link
 * RepositionableStream} interface.  An instance of this class will try to cast
 * the underlying byte stream to a {@link RepositionableStream} and to fetch by
 * reflection the {@link java.nio.channels.FileChannel} underlying the given
 * output stream, in this order.  If either reference can be successfully
 * fetched, you can use {@link #position(long)} to reposition the stream.
 * Note that even in this case, it is still guaranteed that all reads will
 * be performed on buffer boundaries, that is, as if the stream was divided
 * in blocks of the size of the buffer.
 *
 * @since 4.4
 */

public class FastBufferedInputStream extends InputStream implements RepositionableStream {

	/** The default size of the internal buffer in bytes (8Ki). */
	public final static int DEFAULT_BUFFER_SIZE = 8 * 1024;

	/** The underlying input stream. */
	protected InputStream is;

	/** The internal buffer. */
	protected byte buffer[];

	/** The current position in the buffer. */
	protected int pos;

	/** The number of buffer bytes available starting from {@link #pos}. */
	protected int avail;

	/** The cached file channel underlying {@link #is}, if any. */
	private FileChannel fileChannel;

	/** {@link #is} cast to a positionable stream, if possible. */
	private RepositionableStream rs;


	/** Creates a new fast buffered input stream by wrapping a given input stream with a given buffer size. 
	 *
	 * @param is an input stream to wrap.
	 * @param bufSize the size in bytes of the internal buffer.
	 */

	public FastBufferedInputStream( final InputStream is, final int bufSize ) {
		this.is = is;
		buffer = new byte[ bufSize ];

		if ( is instanceof RepositionableStream ) rs = (RepositionableStream)is;
			
		if ( rs == null ) {
				
			try {
				fileChannel = (FileChannel)( is.getClass().getMethod( "getChannel", new Class[] {} ) ).invoke( is, new Object[] {} );
			}
			catch( IllegalAccessException e ) {}
			catch( IllegalArgumentException e ) {}
			catch( NoSuchMethodException e ) {}
			catch( java.lang.reflect.InvocationTargetException e ) {}
			catch( ClassCastException e ) {}
		}
	}

	/** Creates a new fast buffered input stream by wrapping a given input stream with a buffer of {@link #DEFAULT_BUFFER_SIZE} bytes. 
	 *
	 * @param is an input stream to wrap.
	 */
	public FastBufferedInputStream( final InputStream is ) {
		this( is, DEFAULT_BUFFER_SIZE );
	}

	public int read() throws IOException {
		if ( avail == 0 ) {
			avail = is.read( buffer );
			if ( avail <= 0 ) {
				avail = 0;
				return -1;
			}
			pos = 0;
		}
		avail--;
		return buffer[ pos++ ] & 0xFF;
	}


	public int read( final byte b[], int offset, int length ) throws IOException {
		if ( length <= avail ) {
			System.arraycopy( buffer, pos, b, offset, length );
			pos += length;
			avail -= length;
			return length;
		}
	
		final int head = avail;
		System.arraycopy( buffer, pos, b, offset, head );
		offset += head;
		length -= head;
		avail = 0;

		final int residual = length % buffer.length;
		int result;

		if ( ( result = is.read( b, offset, length - residual ) ) < length - residual ) 
			return result < 0 
				? ( head != 0 ? head : -1 ) 
				: result + head;

		avail = is.read( buffer );
		if ( avail < 0 ) {
			avail = 0;
			return result + head > 0 ? result + head : -1;
		}
		pos = Math.min( avail, residual );
		System.arraycopy( buffer, 0, b, offset + length - residual, pos );
		avail -= pos;
		return result + head + pos;
	}

	public void position( long newPosition ) throws IOException {

		final long position = position();

		if ( newPosition <= position + avail && newPosition >= position - pos ) {
			pos += newPosition - position;
			avail -= newPosition - position;
			return;
		}

		final int residual = (int)( newPosition % buffer.length );

		if ( rs != null ) rs.position( newPosition - residual );
		else if ( fileChannel != null ) fileChannel.position( newPosition - residual );
		else throw new UnsupportedOperationException( "position() can only be called if the underlying byte stream implements the RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel" );

		avail = Math.max( 0, is.read( buffer ) );
		pos = Math.min( residual, avail );
		avail -= pos;
	}

	public long position() throws IOException {
		if ( rs != null ) return rs.position() - avail;
		else if ( fileChannel != null ) return fileChannel.position() - avail;
		else throw new UnsupportedOperationException( "position() can only be called if the underlying byte stream implements the RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel" );
	}


	public long skip( long n ) throws IOException {
		if ( n <= avail ) {
			final int m = (int)n;
			pos += m;
			avail -= m;
			return n;
		}

		final int head = avail;
		n -= head;
		avail = 0;

		final int residual = (int)( n % buffer.length );
		long result;
		if ( ( result = is.skip( n - residual ) ) < n - residual ) {
			avail = 0;
			return result + head;
		}

		avail = Math.max( is.read( buffer ), 0 );
		pos = Math.min( residual, avail );
		avail -= pos;
		return result + head + pos;
	}


	public int available() throws IOException {
		return is.available() + avail;
	}

	public void close() throws IOException {
		if ( is == null ) return;
		if ( is != System.in ) is.close();
		is = null;
		buffer = null;
	}

	/** Resets the internal logic of this fast buffered input stream, clearing the buffer. 
	 *
	 *  <p>The underlying input stream is not modified, but its position cannot be easily
	 *  predicted, due to buffering. This method is thus mainly useful for reading files
	 *  that are being written by other process (in practice, it makes the current buffer invalid). */

	public void reset() {
		if ( is == null ) return;
		avail = pos = 0;
	}
}
	

// Local Variables:
// mode: jde
// tab-width: 4
// End:

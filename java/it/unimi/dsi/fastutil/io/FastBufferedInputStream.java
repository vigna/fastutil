package it.unimi.dsi.fastutil.io;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2005, 2006 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.io.MeasurableInputStream;
import it.unimi.dsi.fastutil.io.RepositionableStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.EnumSet;

/** Lightweight, unsynchronized, aligned input stream buffering class with
 *  {@linkplain MeasurableInputStream measurability}, 
 *  {@linkplain RepositionableStream repositionability} 
 *  and {@linkplain #readLine(byte[], int, int, EnumSet) line reading} support.
 *
 * <P>This class provides buffering for input streams, but it does so with 
 * purposes and an internal logic that are radically different from the ones
 * adopted in {@link java.io.BufferedInputStream}.
 * 
 * <P>There is no support for marking. All methods are unsychronized. Moreover,
 * we try to guarantee that in case of sequential access
 * <em>all reads performed by this class will be
 * of the given buffer size</em>.  If, for instance, you use the
 * default buffer size, reads will be performed on the underlying input stream
 * in multiples of 16384 bytes. This is very important on operating systems
 * that optimize disk reads on disk block boundaries.
 * 
 * <P>As an additional feature, this class implements the {@link
 * RepositionableStream} interface and extends {@link MeasurableInputStream}.  
 * An instance of this class will try to cast
 * the underlying byte stream to a {@link RepositionableStream} and to fetch by
 * reflection the {@link java.nio.channels.FileChannel} underlying the given
 * output stream, in this order. If either reference can be successfully
 * fetched, you can use {@link #position(long)} to reposition the stream.
 * Note that in this case we do not guarantee that all reads will
 * be performed on buffer boundaries.
 * 
 * <p>This class keeps also track of the number of bytes read so far, so
 * to be able to implemented {@link MeasurableInputStream#position()}
 * independently of underlying input stream.
 *
 * <P>If, on the other hand, the underlying byte stream can be cast to a 
 * {@link MeasurableInputStream}, then also 
 * {@link MeasurableInputStream#length()} will work as expected.
 * 
 * <p>This class has limited support for 
 * {@linkplain #readLine(byte[], int, int, EnumSet) &ldquo;reading a line&rdquo;}
 * (whatever that means) from the underlying input stream. You can choose the set of
 * {@linkplain FastBufferedInputStream.LineTerminator line terminators} that
 * delimit lines.
 *
 * @since 4.4
 */

public class FastBufferedInputStream extends MeasurableInputStream implements RepositionableStream {

	/** The default size of the internal buffer in bytes (8Ki). */
	public final static int DEFAULT_BUFFER_SIZE = 8 * 1024;

	/** An enumeration of the supported line terminators. */
	public static enum LineTerminator {
		/** A carriage return (CR, ASCII 13). */
		CR,
		/** A line feed (LF, ASCII 10). */
		LF,
		/** A carriage return followed by a line feed (CR/LF, ASCII 13/10). */
		CR_LF
	}

	/** A set containing <em>all available</em> line terminators. */
	public final static EnumSet<LineTerminator> ALL_TERMINATORS = EnumSet.allOf( LineTerminator.class );
	
	/** The underlying input stream. */
	protected InputStream is;

	/** The internal buffer. */
	protected byte buffer[];

	/** The current position in the buffer. */
	protected int pos;

	/** The number of bytes ever read (reset upon a call to {@link #position(long)}).
	 * In particular, this will always represent the index (in the underlying input stream)
	 * of the first available byte in the buffer. */
	protected long readBytes;

	/** The number of buffer bytes available starting from {@link #pos}. */
	protected int avail;

	/** The cached file channel underlying {@link #is}, if any. */
	private FileChannel fileChannel;

	/** {@link #is} cast to a positionable stream, if possible. */
	private RepositionableStream rs;

	/** {@link #is} cast to a measurable input stream, if possible. */
	private MeasurableInputStream ms;

	/** Creates a new fast buffered input stream by wrapping a given input stream with a given buffer size. 
	 *
	 * @param is an input stream to wrap.
	 * @param bufSize the size in bytes of the internal buffer.
	 */

	public FastBufferedInputStream( final InputStream is, final int bufSize ) {
		this.is = is;
		buffer = new byte[ bufSize ];

		if ( is instanceof RepositionableStream ) rs = (RepositionableStream)is;
		if ( is instanceof MeasurableInputStream ) ms = (MeasurableInputStream)is;
			
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

	/** Checks whether no more bytes will be returned.
	 * 
	 * <p>This method will refill the internal buffer.
	 * 
	 * @return true if there are no characters in the internal buffer and
	 * the underlying reader is exhausted.
	 */
	
	protected boolean noMoreCharacters() throws IOException {
		if ( avail == 0 ) {
			avail = is.read( buffer );
			if ( avail <= 0 ) {
				avail = 0;
				return true;
			}
			pos = 0;
		}
		return false;
	}
	

	
	public int read() throws IOException {
		if ( noMoreCharacters() ) return -1;
		avail--;
		readBytes++;
		return buffer[ pos++ ] & 0xFF;
	}


	public int read( final byte b[], int offset, int length ) throws IOException {
		if ( length <= avail ) {
			System.arraycopy( buffer, pos, b, offset, length );
			pos += length;
			avail -= length;
			readBytes += length;
			return length;
		}
	
		final int head = avail;
		System.arraycopy( buffer, pos, b, offset, head );
		offset += head;
		length -= head;
		avail = 0;

		final int residual = length % buffer.length;
		int result;

		if ( ( result = is.read( b, offset, length - residual ) ) < length - residual ) {
			final int t = result < 0 
				? ( head != 0 ? head : -1 ) 
				: result + head;
			if ( t > 0 ) readBytes += t;	
			return t;
		}

		avail = is.read( buffer );
		if ( avail < 0 ) {
			avail = pos = 0;
			final int t = result + head > 0 ? result + head : -1;
			if ( t > 0 ) readBytes += t;
			return t;
		}
		pos = Math.min( avail, residual );
		System.arraycopy( buffer, 0, b, offset + length - residual, pos );
		avail -= pos;
		final int t = result + head + pos;
		readBytes += t;
		return t;
	}

	/** Reads a line into the given byte array using {@linkplain #ALL_TERMINATORS all terminators}.
	 *
	 * @param array byte array where the next line will be stored.
	 * @return the number of bytes actually placed in <code>array</code>, or -1 at end of file.
	 * @see #readLine(byte[], int, int, EnumSet)
	 */

	public int readLine( final byte[] array ) throws IOException {
		return readLine( array, 0, array.length, ALL_TERMINATORS );
	}

	/** Reads a line into the given byte array.
	 *
	 * @param array byte array where the next line will be stored.
	 * @param terminators a set containing the line termination sequences that we want
	 * to consider as valid.
	 * @return the number of bytes actually placed in <code>array</code>, or -1 at end of file.
	 * @see #readLine(byte[], int, int, EnumSet)
	 */

	public int readLine( final byte[] array, final EnumSet<LineTerminator> terminators ) throws IOException {
		return readLine( array, 0, array.length, terminators );
	}

	/** Reads a line into the given byte-array fragment	using {@linkplain #ALL_TERMINATORS all terminators}.
	 *
	 * @param array byte array where the next line will be stored.
	 * @param off the first byte to use in <code>array</code>.
	 * @param len the maximum number of bytes to read.
	 * @return the number of bytes actually placed in <code>array</code>, or -1 at end of file.
	 * @see #readLine(byte[], int, int, EnumSet)
	 */
	public int readLine( final byte[] array, final int off, final int len ) throws IOException {
		return readLine( array, off, len, ALL_TERMINATORS );
	}

	/** Reads a line into the given byte-array fragment.
	 *
	 * <P>Reading lines (i.e., characters) out of a byte stream is not always sensible
	 * (methods available to that purpose in old versions of Java have been mercilessly deprecated).
	 * Nonetheless, in several situations, such as when decoding network protocols or headers
	 * known to be ASCII, it is very useful to be able to read a line from a byte stream.
	 * 
	 * <p>This method will attempt to read the next line into <code>array</code> starting at <code>off</code>,
	 * reading at most <code>len</code> bytes. The read, however, will be stopped by the end of file or
	 * when meeting a {@linkplain LineTerminator <em>line terminator</em>}. Of course, for this operation
	 * to be sensible the encoding of the text contained in the stream, if any, must not generate spurious
	 * carriage returns or line feeds. Note that the termination detection uses a maximisation
	 * criterion, so if you specify both {@link LineTerminator#CR} and
	 * {@link LineTerminator#CR_LF} meeting a pair CR/LF will consider the whole pair a terminator.
	 * 
	 * <p>Terminators are <em>not</em> copied into <em>array</em> or included in the returned count. The
	 * returned integer can be used to check whether the line is complete: if it is smaller than
	 * <code>len</code>, then more bytes might be available, but note that this method (contrarily
	 * to {@link #readLine(byte[], int, int)}) can legitimately return zero when <code>len</code>
	 * is nonzero just because a terminator was found as the first character. Thus, the intended
	 * usage of this method is to call it on a given array, check whether <code>len</code> bytes
	 * have been read, and if so try again (possibly extending the array) until a number of read bytes
	 * strictly smaller than <code>len</code> (possibly, -1) is returned.
	 *
	 * @param array byte array where the next line will be stored.
	 * @param off the first byte to use in <code>array</code>.
	 * @param len the maximum number of bytes to read.
	 * @param terminators a set containing the line termination sequences that we want
	 * to consider as valid.
	 * @return the number of bytes actually placed in <code>array</code>, or -1 at end of file.
	 * Note that the returned number will be <code>len</code> if no line termination sequence 
	 * specified in <code>terminators</code> has been met before scanning <code>len</code> byte,
	 * and if also we did not meet the end of file. 
	 */

	public int readLine( final byte[] array, final int off, final int len, final EnumSet<LineTerminator> terminators ) throws IOException {
		ByteArrays.ensureOffsetLength( array ,off, len );
		if ( len == 0 ) return 0; // 0-length reads always return 0
		if ( noMoreCharacters() ) return -1;
		int i, k = 0, remaining = len, read = 0; // The number of bytes still to be read
		for(;;) {
			for( i = 0; i < avail && i < remaining && ( k = buffer[ pos + i ] ) != '\n' && k != '\r' ; i++ );
			System.arraycopy( buffer, pos, array, off + read, i );
			pos += i; 
			avail -= i;
			read += i;
			remaining -= i;
			if ( remaining == 0 ) {
				readBytes += read;
				return read; // We did not stop because of a terminator
			}
			
			if ( avail > 0 ) { // We met a terminator
				if ( k == '\n' ) { // LF first
					pos++;
					avail--;
					if ( terminators.contains( LineTerminator.LF ) ) {
						readBytes += read + 1;
						return read;
					}
					else {
						array[ off + read++ ] = '\n';
						remaining--;
					}
				}
				else if ( k == '\r' ) { // CR first
					pos++;
					avail--;
					
					if ( terminators.contains( LineTerminator.CR_LF ) ) {
						if ( avail > 0 ) {
							if ( buffer[ pos ] == '\n' ) { // CR/LF with LF already in the buffer.
								pos ++;
								avail--;
								readBytes += read + 2;
								return read;
							}
						}
						else { // We must search for the LF.
							if ( noMoreCharacters() ) {
								// Not found a matching LF because of end of file, will return CR in buffer if not a terminator

								if ( ! terminators.contains( LineTerminator.CR ) ) {
									array[ off + read++ ] = '\r';
									remaining--;
									readBytes += read;
								}
								else readBytes += read + 1;
								
								return read;
							}
							if ( buffer[ 0 ] == '\n' ) {
								// Found matching LF, won't return terminators in the buffer
								pos++;
								avail--;
								readBytes += read + 2;
								return read;
							}
						}
					}
					
					if ( terminators.contains( LineTerminator.CR ) ) {
						readBytes += read + 1;
						return read;
					}
					
					array[ off + read++ ] = '\r';
					remaining--;
				}
			}
			else if ( noMoreCharacters() ) {
				readBytes += read;
				return read;
			}
		}
	}


	public void position( long newPosition ) throws IOException {

		final long position = readBytes;

		/** Note that this check will succeed also in the case of
		 * an empty buffer and position == newPosition. This behaviour is
		 * intentional, as it delays buffering to when it is actually
		 * necessary and avoids useless class the underlying stream. */
		
		if ( newPosition <= position + avail && newPosition >= position - pos ) {
			pos += newPosition - position;
			avail -= newPosition - position;
			readBytes = newPosition;
			return;
		}

		if ( rs != null ) rs.position( newPosition  );
		else if ( fileChannel != null ) fileChannel.position( newPosition );
		else throw new UnsupportedOperationException( "position() can only be called if the underlying byte stream implements the RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel" );
		readBytes = newPosition;

		avail = Math.max( 0, is.read( buffer ) );
		pos = 0;
	}

	public long position() throws IOException {
		return readBytes;
	}

	/** Returns the length of the underlying input stream, if it is {@linkplain MeasurableInputStream measurable}.
	 *
	 * @return the length of the underlying input stream.
	 * @throws UnsupportedOperationException if the underlying input stream is not {@linkplain MeasurableInputStream measurable}.
	 */

	public long length() throws IOException {
		if ( ms == null ) throw new UnsupportedOperationException();
		return ms.length();
	}


	public long skip( long n ) throws IOException {
		if ( n <= avail ) {
			final int m = (int)n;
			pos += m;
			avail -= m;
			readBytes += n;
			return n;
		}

		final int head = avail;
		n -= head;
		avail = 0;

		final int residual = (int)( n % buffer.length );
		long result;
		if ( ( result = is.skip( n - residual ) ) < n - residual ) {
			avail = 0;
			readBytes += result + head;
			return result + head;
		}

		avail = Math.max( is.read( buffer ), 0 );
		pos = Math.min( residual, avail );
		avail -= pos;
		final long t = result + head + pos;
		readBytes += t;
		return t;
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
	 * <p>All buffering information is discarded, and the number of bytes read so far
	 * (and thus, also the {@linkplain #position() current position})
	 * is adjusted to reflect this fact. 
	 *  
	 * <p>This method is mainly useful for re-reading 
	 * files that are have been overwritten externally. 
	 */

	public void flush() {
		if ( is == null ) return;
		readBytes += avail; 
		avail = pos = 0;
	}

	/** Resets the internal logic of this fast buffered input stream.
	 * 
	 * @deprecated As of <samp>fastutil</samp> 5.0.4, replaced by {@link #flush()}. The old
	 * semantics of this method does not contradict {@link InputStream}'s contract, as
	 * the semantics of {@link #reset()} is undefined if {@link InputStream#markSupported()}
	 * returns false. On the other hand, the name was really a poor choice.
	 */
	@Deprecated
	public void reset() {
		flush();
	}
}

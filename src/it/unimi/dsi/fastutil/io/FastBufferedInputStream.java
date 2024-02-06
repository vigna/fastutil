/*
 * Copyright (C) 2005-2024 Sebastiano Vigna
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

package it.unimi.dsi.fastutil.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.EnumSet;

import it.unimi.dsi.fastutil.bytes.ByteArrays;

/** Lightweight, unsynchronized, aligned input stream buffering class with
 *  {@linkplain #skip(long) true skipping},
 *  {@linkplain MeasurableStream measurability},
 *  {@linkplain RepositionableStream repositionability}
 *  and {@linkplain #readLine(byte[], int, int, EnumSet) line reading} support.
 *
 * <p>This class provides buffering for input streams, but it does so with
 * purposes and an internal logic that are radically different from the ones
 * adopted in {@link java.io.BufferedInputStream}. The main features follow.
 *
 * <ul>
 * <li><p>There is no support for marking. All methods are unsychronized.
 *
 * <li><p>As an additional feature, this class implements the {@link
 * RepositionableStream} and {@link MeasurableStream} interfaces.
 * An instance of this class will try to cast
 * the underlying byte stream to a {@link RepositionableStream} and to fetch by
 * reflection the {@link java.nio.channels.FileChannel} underlying the given
 * output stream, in this order. If either reference can be successfully
 * fetched, you can use {@link #position(long)} to reposition the stream.
 * Much in the same way, an instance of this class will try to cast the
 * the underlying byte stream to a {@link MeasurableStream}, and if this
 * operation is successful, or if a {@link java.nio.channels.FileChannel} can
 * be detected, then {@link #position()} and {@link #length()} will work as expected.
 *
 *
 * <li><p>Due to erratic and unpredictable behaviour of {@link InputStream#skip(long)},
 * which does not correspond to its specification and which Sun refuses to fix
 * (see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6222822">bug 6222822</a>;
 * don't be fooled by the &ldquo;closed, fixed&rdquo; label),
 * this class peeks at the underlying stream and if it is {@link System#in} it uses
 * repeated reads instead of calling {@link InputStream#skip(long)} on the underlying stream; moreover,
 * skips and reads are tried alternately, so to guarantee that skipping
 * less bytes than requested can be caused only by reaching the end of file.
 *
 * <li><p>This class keeps also track of the number of bytes read so far, so
 * to be able to implement {@link MeasurableStream#position()}
 * independently of underlying input stream.
 *
 * <li><p>This class has limited support for
 * {@linkplain #readLine(byte[], int, int, EnumSet) &ldquo;reading a line&rdquo;}
 * (whatever that means) from the underlying input stream. You can choose the set of
 * {@linkplain FastBufferedInputStream.LineTerminator line terminators} that
 * delimit lines.
 *
 * </ul>
 *
 * <p><strong>Warning:</strong> Since {@code fastutil} 6.0.0, this class detects
 * a implementations of {@link MeasurableStream} instead of subclasses {@code MeasurableInputStream} (which is deprecated).
 *
 * @since 4.4
 */

public class FastBufferedInputStream extends MeasurableInputStream implements RepositionableStream {

	/** The default size of the internal buffer in bytes (8Ki). */
	public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

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
	public static final EnumSet<LineTerminator> ALL_TERMINATORS = EnumSet.allOf(LineTerminator.class);

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
	private RepositionableStream repositionableStream;

	/** {@link #is} cast to a measurable stream, if possible. */
	private MeasurableStream measurableStream;

	private static int ensureBufferSize(final int bufferSize) {
		if (bufferSize <= 0) throw new IllegalArgumentException("Illegal buffer size: " + bufferSize);
		return bufferSize;
	}

	/** Creates a new fast buffered input stream by wrapping a given input stream with a given buffer.
	 *
	 * @param is an input stream to wrap.
	 * @param buffer a buffer of positive length.
	 */
	public FastBufferedInputStream(final InputStream is, final byte[] buffer) {
		this.is = is;
		ensureBufferSize(buffer.length);
		this.buffer = buffer;

		if (is instanceof RepositionableStream) repositionableStream = (RepositionableStream)is;
		if (is instanceof MeasurableStream) measurableStream = (MeasurableStream)is;

		if (repositionableStream == null) {

			try {
				fileChannel = (FileChannel)(is.getClass().getMethod("getChannel", new Class<?>[] {})).invoke(is);
			}
			catch(final IllegalAccessException e) {}
			catch(final IllegalArgumentException e) {}
			catch(final NoSuchMethodException e) {}
			catch(final java.lang.reflect.InvocationTargetException e) {}
			catch(final ClassCastException e) {}
		}
	}

	/** Creates a new fast buffered input stream by wrapping a given input stream with a given buffer size.
	 *
	 * @param is an input stream to wrap.
	 * @param bufferSize the size in bytes of the internal buffer (greater than zero).
	 */
	public FastBufferedInputStream(final InputStream is, final int bufferSize) {
		this(is, new byte[ensureBufferSize(bufferSize)]);
	}

	/** Creates a new fast buffered input stream by wrapping a given input stream with a buffer of {@link #DEFAULT_BUFFER_SIZE} bytes.
	 *
	 * @param is an input stream to wrap.
	 */
	public FastBufferedInputStream(final InputStream is) {
		this(is, DEFAULT_BUFFER_SIZE);
	}

	/** Checks whether no more bytes will be returned.
	 *
	 * <p>This method will refill the internal buffer.
	 *
	 * @return true if there are no characters in the internal buffer and
	 * the underlying reader is exhausted.
	 */

	protected boolean noMoreCharacters() throws IOException {
		if (avail == 0) {
			avail = is.read(buffer);
			if (avail <= 0) {
				avail = 0;
				return true;
			}
			pos = 0;
		}
		return false;
	}

	@Override
	public int read() throws IOException {
		if (noMoreCharacters()) return -1;
		avail--;
		readBytes++;
		return buffer[pos++] & 0xFF;
	}

	@Override
	public int read(final byte b[], final int offset, final int length) throws IOException {
		if (length <= avail) {
			System.arraycopy(buffer, pos, b, offset, length);
			pos += length;
			avail -= length;
			readBytes += length;
			return length;
		}

		final int head = avail;

		System.arraycopy(buffer, pos, b, offset, head);
		pos = avail = 0;
		readBytes += head;

		if (length > buffer.length) {
			// We read directly into the destination
			final int result = is.read(b, offset + head, length - head);
			if (result > 0) readBytes += result;
			return result < 0 ? (head == 0 ? -1 : head) : result + head;
		}

		if (noMoreCharacters()) return head == 0 ? -1 : head;

		final int toRead = Math.min(length - head, avail);
		readBytes += toRead;
		System.arraycopy(buffer, 0, b, offset + head, toRead);
		pos = toRead;
		avail -= toRead;

		// Note that head >= 0, and necessarily toRead > 0
		return toRead + head;
	}

	/** Reads a line into the given byte array using {@linkplain #ALL_TERMINATORS all terminators}.
	 *
	 * @param array byte array where the next line will be stored.
	 * @return the number of bytes actually placed in {@code array}, or -1 at end of file.
	 * @see #readLine(byte[], int, int, EnumSet)
	 */

	public int readLine(final byte[] array) throws IOException {
		return readLine(array, 0, array.length, ALL_TERMINATORS);
	}

	/** Reads a line into the given byte array.
	 *
	 * @param array byte array where the next line will be stored.
	 * @param terminators a set containing the line termination sequences that we want
	 * to consider as valid.
	 * @return the number of bytes actually placed in {@code array}, or -1 at end of file.
	 * @see #readLine(byte[], int, int, EnumSet)
	 */

	public int readLine(final byte[] array, final EnumSet<LineTerminator> terminators) throws IOException {
		return readLine(array, 0, array.length, terminators);
	}

	/** Reads a line into the given byte-array fragment	using {@linkplain #ALL_TERMINATORS all terminators}.
	 *
	 * @param array byte array where the next line will be stored.
	 * @param off the first byte to use in {@code array}.
	 * @param len the maximum number of bytes to read.
	 * @return the number of bytes actually placed in {@code array}, or -1 at end of file.
	 * @see #readLine(byte[], int, int, EnumSet)
	 */
	public int readLine(final byte[] array, final int off, final int len) throws IOException {
		return readLine(array, off, len, ALL_TERMINATORS);
	}

	/** Reads a line into the given byte-array fragment.
	 *
	 * <p>Reading lines (i.e., characters) out of a byte stream is not always sensible
	 * (methods available to that purpose in old versions of Java have been mercilessly deprecated).
	 * Nonetheless, in several situations, such as when decoding network protocols or headers
	 * known to be ASCII, it is very useful to be able to read a line from a byte stream.
	 *
	 * <p>This method will attempt to read the next line into {@code array} starting at {@code off},
	 * reading at most {@code len} bytes. The read, however, will be stopped by the end of file or
	 * when meeting a {@linkplain LineTerminator <em>line terminator</em>}. Of course, for this operation
	 * to be sensible the encoding of the text contained in the stream, if any, must not generate spurious
	 * carriage returns or line feeds. Note that the termination detection uses a maximisation
	 * criterion, so if you specify both {@link LineTerminator#CR} and
	 * {@link LineTerminator#CR_LF} meeting a pair CR/LF will consider the whole pair a terminator.
	 *
	 * <p>Terminators are <em>not</em> copied into <em>array</em> or included in the returned count. The
	 * returned integer can be used to check whether the line is complete: if it is smaller than
	 * {@code len}, then more bytes might be available, but note that this method (contrarily
	 * to {@link #read(byte[], int, int)}) can legitimately return zero when {@code len}
	 * is nonzero just because a terminator was found as the first character. Thus, the intended
	 * usage of this method is to call it on a given array, check whether {@code len} bytes
	 * have been read, and if so try again (possibly extending the array) until a number of read bytes
	 * strictly smaller than {@code len} (possibly, -1) is returned.
	 *
	 * <p>If you need to guarantee that a full line is read, use the following idiom:
	 * <pre>
	 * int start = off, len;
	 * while((len = fbis.readLine(array, start, array.length - start, terminators)) == array.length - start) {
	 *     start += len;
	 *     array = ByteArrays.grow(array, array.length + 1);
	 * }
	 * </pre>
	 *
	 * <p>At the end of the loop, the line will be placed in {@code array} starting at
	 * {@code off} (inclusive) and ending at {@code start + Math.max(len, 0)} (exclusive).
	 *
	 * @param array byte array where the next line will be stored.
	 * @param off the first byte to use in {@code array}.
	 * @param len the maximum number of bytes to read.
	 * @param terminators a set containing the line termination sequences that we want
	 * to consider as valid.
	 * @return the number of bytes actually placed in {@code array}, or -1 at end of file.
	 * Note that the returned number will be {@code len} if no line termination sequence
	 * specified in {@code terminators} has been met before scanning {@code len} byte,
	 * and if also we did not meet the end of file.
	 */

	public int readLine(final byte[] array, final int off, final int len, final EnumSet<LineTerminator> terminators) throws IOException {
		ByteArrays.ensureOffsetLength(array ,off, len);
		if (len == 0) return 0; // 0-length reads always return 0
		if (noMoreCharacters()) return -1;
		int i, k = 0, remaining = len, read = 0; // The number of bytes still to be read
		for(;;) {
			for(i = 0; i < avail && i < remaining && (k = buffer[pos + i]) != '\n' && k != '\r' ; i++);
			System.arraycopy(buffer, pos, array, off + read, i);
			pos += i;
			avail -= i;
			read += i;
			remaining -= i;
			if (remaining == 0) {
				readBytes += read;
				return read; // We did not stop because of a terminator
			}

			if (avail > 0) { // We met a terminator
				if (k == '\n') { // LF first
					pos++;
					avail--;
					if (terminators.contains(LineTerminator.LF)) {
						readBytes += read + 1;
						return read;
					}
					else {
						array[off + read++] = '\n';
						remaining--;
					}
				}
				else if (k == '\r') { // CR first
					pos++;
					avail--;

					if (terminators.contains(LineTerminator.CR_LF)) {
						if (avail > 0) {
							if (buffer[pos] == '\n') { // CR/LF with LF already in the buffer.
								pos ++;
								avail--;
								readBytes += read + 2;
								return read;
							}
						}
						else { // We must search for the LF.
							if (noMoreCharacters()) {
								// Not found a matching LF because of end of file, will return CR in buffer if not a terminator

								if (! terminators.contains(LineTerminator.CR)) {
									array[off + read++] = '\r';
									remaining--;
									readBytes += read;
								}
								else readBytes += read + 1;

								return read;
							}
							if (buffer[0] == '\n') {
								// Found matching LF, won't return terminators in the buffer
								pos++;
								avail--;
								readBytes += read + 2;
								return read;
							}
						}
					}

					if (terminators.contains(LineTerminator.CR)) {
						readBytes += read + 1;
						return read;
					}

					array[off + read++] = '\r';
					remaining--;
				}
			}
			else if (noMoreCharacters()) {
				readBytes += read;
				return read;
			}
		}
	}

	@Override
	public void position(final long newPosition) throws IOException {

		final long position = readBytes;

		/** Note that this check will succeed also in the case of
		 * an empty buffer and position == newPosition. This behaviour is
		 * intentional, as it delays buffering to when it is actually
		 * necessary and avoids useless class the underlying stream. */

		if (newPosition <= position + avail && newPosition >= position - pos) {
			pos += newPosition - position;
			avail -= newPosition - position;
			readBytes = newPosition;
			return;
		}

		if (repositionableStream != null) repositionableStream.position(newPosition);
		else if (fileChannel != null) fileChannel.position(newPosition);
		else throw new UnsupportedOperationException("position() can only be called if the underlying byte stream implements the RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel");
		readBytes = newPosition;

		avail = pos = 0;
	}

	@Override
	public long position() throws IOException {
		return readBytes;
	}

	/** Returns the length of the underlying input stream, if it is {@linkplain MeasurableStream measurable}.
	 *
	 * @return the length of the underlying input stream.
	 * @throws UnsupportedOperationException if the underlying input stream is not {@linkplain MeasurableStream measurable} and
	 * cannot provide a {@link FileChannel}.
	 */

	@Override
	public long length() throws IOException {
		if (measurableStream != null) return measurableStream.length();
		if (fileChannel != null) return fileChannel.size();
		throw new UnsupportedOperationException();
	}

	/** Skips the given amount of bytes by repeated reads.
	 *
	 * <strong>Warning</strong>: this method uses destructively the internal buffer.
	 *
	 * @param n the number of bytes to skip.
	 * @return the number of bytes actually skipped.
	 * @see InputStream#skip(long)
	 */

	private long skipByReading(final long n) throws IOException {
		long toSkip = n;
		int len;
		while(toSkip > 0) {
			len = is.read(buffer, 0, (int)Math.min(buffer.length, toSkip));
			if (len > 0) toSkip -= len;
			else break;
		}

		return n - toSkip;
	}

	/** Skips over and discards the given number of bytes of data from this fast buffered input stream.
	 *
	 * <p>As explained in the {@linkplain FastBufferedInputStream class documentation}, the semantics
	 * of {@link InputStream#skip(long)} is fatally flawed. This method provides additional semantics as follows:
	 * it will skip the provided number of bytes, unless the end of file has been reached.
	 *
	 * <p>Additionally, if the underlying input stream is {@link System#in} this method will use
	 * repeated reads instead of invoking {@link InputStream#skip(long)}.
	 *
	 * @param n the number of bytes to skip.
	 * @return the number of bytes actually skipped; it can be smaller than {@code n}
	 * only if the end of file has been reached.
	 * @see InputStream#skip(long)
	 */

	@Override
	public long skip(final long n) throws IOException {
		if (n <= avail) {
			final int m = (int)n;
			pos += m;
			avail -= m;
			readBytes += n;
			return n;
		}

		long toSkip = n - avail, result = 0;
		avail = 0;

		while (toSkip != 0 && (result = is == System.in ? skipByReading(toSkip) : is.skip(toSkip)) < toSkip) {
			if (result == 0) {
				if (is.read() == -1) break;
				toSkip--;
			}
			else toSkip -= result;
		}

		final long t = n - (toSkip - result);
		readBytes += t;
		return t;
	}

	@Override
	public int available() throws IOException {
		return (int)Math.min(is.available() + (long)avail, Integer.MAX_VALUE);
	}

	@Override
	public void close() throws IOException {
		if (is == null) return;
		if (is != System.in) is.close();
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
	 * files that have been overwritten externally.
	 */

	public void flush() {
		if (is == null) return;
		readBytes += avail;
		avail = pos = 0;
	}

	/** Resets the internal logic of this fast buffered input stream.
	 *
	 * @deprecated As of {@code fastutil} 5.0.4, replaced by {@link #flush()}. The old
	 * semantics of this method does not contradict {@link InputStream}'s contract, as
	 * the semantics of {@link #reset()} is undefined if {@link InputStream#markSupported()}
	 * returns false. On the other hand, the name was really a poor choice.
	 */
	@Override
	@Deprecated
	public void reset() {
		flush();
	}
}

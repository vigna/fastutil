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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import it.unimi.dsi.fastutil.bytes.ByteArrays;

/** A {@linkplain RepositionableStream repositionable} {@link MeasurableInputStream} based on
 * cached data received by a {@link WritableByteChannel} whose first bytes can be inspected directly.
 *
 * <p>An instance of this class acts as a buffer holding the bytes written through its
 * {@link WritableByteChannel} interface (which can be easily turned into an {@link OutputStream} using
 * {@link Channels#newOutputStream(WritableByteChannel)}). The data can be discarded at any time using
 * {@link #clear()}. The first {@link #inspectable} bytes of {@link #buffer} contains the first
 * bytes written. When {@link #buffer} is full, the bytes are written to an <em>overflow
 * file</em>.
 *
 * <p>At any time, the stream of bytes written since creation (or since the last {@link #clear()})
 * are available as a fully implemented {@link MeasurableInputStream} which also implements
 * {@link RepositionableStream} and {@linkplain #mark(int) supports marking}.
 * Note that you must arbitrate carefully write and read accesses,
 * as it is always possible to call {@link #write(ByteBuffer)}
 * and thus modify the {@linkplain #length() length} of the {@link MeasurableInputStream}.
 *
 * <p>The method {@link #close()} makes the {@link MeasurableInputStream} and {@link WritableByteChannel} state-changing methods temporarily throw an {@link IOException}, but
 * does not otherwise modify the state of the stream. You can {@linkplain #reopen() reopen} the stream
 * later, or {@linkplain #clear() clear} it.
 * The method {@link #dispose()} can be used to release
 * the resources associated with the stream.
 *
 * <h2>Buffering</h2>
 *
 * <p>This class provides no form of buffering except for the memory buffer described above, both
 * when reading and when writing. Users should consider wrapping instances of this class with a
 * {@link FastBufferedInputStream}, as reads after the buffer has been exhausted will be performed
 * directly on a {@link RandomAccessFile}. */
public class InspectableFileCachedInputStream extends MeasurableInputStream implements RepositionableStream, WritableByteChannel {
	public static final boolean DEBUG = false;

	/** The default buffer size (64KiB). */
	public static final int DEFAULT_BUFFER_SIZE = 64 * 1024;

	/** The inspection buffer. The first {@link #inspectable} bytes contain the first part of the input stream.
	 * The buffer is available for inspection, but users should not modify its content. */
	public final byte[] buffer;

	/** The number of valid bytes currently in {@link #buffer}. */
	public int inspectable;

	/** The overflow file used by this stream: it is created at construction time, and deleted on {@link #close()}. */
	private final File overflowFile;

	/** The random access file used to access the overflow file. */
	private final RandomAccessFile randomAccessFile;

	/** {@link #randomAccessFile randomAccessFile#getChannel()}, cached. */
	private final FileChannel fileChannel;

	/** The position on this stream (i.e., the index of the next byte to be returned). */
	private long position;

	/** The {@linkplain #mark(int) mark}, if set, or -1. */
	private long mark;

	/** The write position of the {@link #randomAccessFile overflow file}. When {@link #inspectable} is equal
	 * to {@link #buffer buffer.length}, the length of the stream is {@link #inspectable} + {@link #writePosition}.  */
	private long writePosition;


	/** Creates a new instance with specified buffer size and overlow-file directory.
	 *
	 * @param bufferSize the buffer size, in bytes.
	 * @param overflowFile the directory where the overflow file should be created, or {@code null} for the default temporary directory.
	 */
	public InspectableFileCachedInputStream(final int bufferSize, final File overflowFile) throws IOException {
		if (bufferSize <= 0) throw new IllegalArgumentException("Illegal buffer size " + bufferSize);
		if (overflowFile != null) this.overflowFile = overflowFile;
		else (this.overflowFile = File.createTempFile(getClass().getSimpleName(), "overflow")).deleteOnExit();
		buffer = new byte[bufferSize];
		randomAccessFile = new RandomAccessFile(this.overflowFile, "rw");
		fileChannel = randomAccessFile.getChannel();
		mark = -1;
	}

	/** Creates a new instance with specified buffer size and default overflow-file directory.
	 *
	 * @param bufferSize the buffer size, in bytes.
	 */
	public InspectableFileCachedInputStream(final int bufferSize) throws IOException {
		this(bufferSize, null);
	}

	/** Creates a new instance with default buffer size and overflow-file directory. */
	public InspectableFileCachedInputStream() throws IOException {
		this(DEFAULT_BUFFER_SIZE);
	}

	private void ensureOpen() throws IOException {
		if (position == -1) throw new IOException("This " + getClass().getSimpleName() + " is closed");
	}

	/** Clears the content of this {@link InspectableFileCachedInputStream}, zeroing the length of the represented
	 * stream. */
	public void clear() throws IOException {
		if (! fileChannel.isOpen()) throw new IOException("This " + getClass().getSimpleName() + " is closed");
		writePosition = position = inspectable = 0;
		mark = -1;
	}

	/** Appends the content of a specified buffer to the end of the currently represented stream.
	 *
	 * @param byteBuffer a byte buffer.
	 * @return the number of bytes appended (i.e., {@link ByteBuffer#remaining() byteBuffer.remaining()}).
	 */
	@Override
	public int write(final ByteBuffer byteBuffer) throws IOException {
		ensureOpen();
		final int remaining = byteBuffer.remaining();

		if (inspectable < buffer.length) {
			// Still some space in the inspectable buffer.
			final int toBuffer = Math.min(buffer.length - inspectable, remaining);
			byteBuffer.get(buffer, inspectable, toBuffer);
			inspectable += toBuffer;
		}

		if (byteBuffer.hasRemaining()) {
			fileChannel.position(writePosition);
			writePosition += fileChannel.write(byteBuffer);
		}

		return remaining;
	}

	/** Truncates the overflow file to a given size if possible.
	 *
	 * @param size the new size; the final size is the maximum between the current write position (i.e., the length
	 * of the represented stream minus the length of the inspection buffer) and this value.
	 */
	public void truncate(final long size) throws FileNotFoundException, IOException {
		fileChannel.truncate(Math.max(size, writePosition));
	}

	/** Makes the stream unreadable until the next {@link #clear()}.
	 *
	 * @see #reopen()
	 */
	@Override
	public void close() {
		position = -1;
	}

	/** Makes the stream readable again after a {@link #close()}.
	 *
	 * @see #close()
	 */
	public void reopen() throws IOException {
		if (! fileChannel.isOpen()) throw new IOException("This " + getClass().getSimpleName() + " is closed");
		position = 0;
	}

	/** Disposes this stream, deleting the overflow file. After that, the stream is unusable. */
	public void dispose() throws IOException {
		position = -1;
		randomAccessFile.close();
		overflowFile.delete();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			dispose();
		}
		finally {
			super.finalize();
		}
	}

	@Override
	public int available() throws IOException {
		ensureOpen();
		return (int)Math.min(Integer.MAX_VALUE, length() - position);
	}

	@Override
	public int read(final byte[] b, int offset, int length) throws IOException {
		ensureOpen();
		if (length == 0) return 0;
		if (position == length()) return -1; // Nothing to read.
		ByteArrays.ensureOffsetLength(b, offset, length);
		int read = 0;

		if (position < inspectable) {
			/* The first min(inspectable - readPosition, length) bytes should be taken from the buffer. */
			final int toCopy = Math.min(inspectable - (int)position, length);
			System.arraycopy(buffer, (int)position, b, offset, toCopy);
			length -= toCopy;
			offset += toCopy;
			position += toCopy;
			read = toCopy;
		}

		if (length > 0) { // We want to read more.
			if (position == length()) return read != 0 ? read : -1; // There's nothing more to read.
			fileChannel.position(position - inspectable);
			final int toRead = (int)Math.min(length() - position, length);
			// This is *intentionally* not a readFully(). Let the language to its stuff.
			final int t = randomAccessFile.read(b, offset, toRead);
			position += t;
			read += t;
		}

		return read;
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public long skip(final long n) throws IOException {
		ensureOpen();
		final long toSkip = Math.min(n, length() - position);
		position += toSkip;
		return toSkip;
	}

	@Override
	public int read() throws IOException {
		ensureOpen();
		if (position == length()) return -1; // Nothing to read
		if (position < inspectable) return buffer[(int)position++] & 0xFF;
		fileChannel.position(position - inspectable);
		position++;
		return randomAccessFile.read();
	}

	@Override
	public long length() throws IOException {
		ensureOpen();
		return inspectable + writePosition;
	}

	@Override
	public long position() throws IOException {
		ensureOpen();
		return position;
	}

	/** Positions the input stream.
	 *
	 * @param position the new position (will be minimized with {@link #length()}).
	 */
	@Override
	public void position(final long position) throws IOException {
		this.position = Math.min(position, length());
	}

	@Override
	public boolean isOpen() {
		return position != -1;
	}

	@Override
	public void mark(final int readlimit) {
		mark = position;
	}

	@Override
	public void reset() throws IOException {
		ensureOpen();
		if (mark == -1) throw new IOException("Mark has not been set");
		position(mark);
	}

	@Override
	public boolean markSupported() {
		return true;
	}
}

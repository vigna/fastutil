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

/** Simple, fast and repositionable byte-array input stream.
 *
 * <p><strong>Warning</strong>: this class implements the correct semantics
 * of {@link #read(byte[], int, int)} as described in {@link java.io.InputStream}.
 * The implementation given in {@link java.io.ByteArrayInputStream} is broken,
 * but it will never be fixed because it's too late.
 *
 * @author Sebastiano Vigna
 */

public class FastByteArrayInputStream extends MeasurableInputStream implements RepositionableStream {

	/** The array backing the input stream. */
	public byte[] array;

	/** The first valid entry. */
	public int offset;

	/** The number of valid bytes in {@link #array} starting from {@link #offset}. */
	public int length;

	/** The current position as a distance from {@link #offset}. */
	private int position;

	/** The current mark as a position, or -1 if no mark exists. */
	private int mark;

	/** Creates a new array input stream using a given array fragment.
	 *
	 * @param array the backing array.
	 * @param offset the first valid entry of the array.
	 * @param length the number of valid bytes.
	 */
	public FastByteArrayInputStream(final byte[] array, final int offset, final int length) {
		this.array = array;
		this.offset = offset;
		this.length = length;
	}

	/** Creates a new array input stream using a given array.
	 *
	 * @param array the backing array.
	 */
	public FastByteArrayInputStream(final byte[] array) {
		this(array, 0, array.length);
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public void reset() {
		position = mark;
	}

	/** Closing a fast byte array input stream has no effect. */
	@Override
	public void close() {}

	@Override
	public void mark(final int dummy) {
		mark = position;
	}

	@Override
	public int available() {
		return length - position;
	}

	@Override
	public long skip(long n) {
		if (n <= length - position) {
			position += (int)n;
			return n;
		}
		n = length - position;
		position = length;
		return n;
	}

	@Override
	public int read() {
		if (length == position) return -1;
		return array[offset + position++] & 0xFF;
	}

	/** Reads bytes from this byte-array input stream as
	 * specified in {@link java.io.InputStream#read(byte[], int, int)}.
	 * Note that the implementation given in {@link java.io.ByteArrayInputStream#read(byte[], int, int)}
	 * will return -1 on a zero-length read at EOF, contrarily to the specification. We won't.
	 */

	@Override
	public int read(final byte b[], final int offset, final int length) {
		if (this.length == this.position) return length == 0 ? 0 : -1;
		final int n = Math.min(length, this.length - this.position);
		System.arraycopy(array, this.offset + this.position, b, offset, n);
		this.position += n;
		return n;
	}

	@Override
	public long position() {
		return position;
	}

	@Override
	public void position(final long newPosition) {
		position = (int)Math.min(newPosition, length);
	}

	@Override
	public long length() {
		return length;
	}
}

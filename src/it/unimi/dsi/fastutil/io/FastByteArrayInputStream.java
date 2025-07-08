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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.UTFDataFormatException;
import java.io.UncheckedIOException;

/** Simple, fast and repositionable byte-array input stream.
 *
 * <p><strong>Warning</strong>: this class implements the correct semantics
 * of {@link #read(byte[], int, int)} as described in {@link java.io.InputStream}.
 * The implementation given in {@link java.io.ByteArrayInputStream} is broken,
 * but it will never be fixed because it's too late.
 *
 * @author Sebastiano Vigna
 */
public class FastByteArrayInputStream extends MeasurableInputStream implements RepositionableStream, ObjectInput {

	/** The array backing the input stream. */
	public byte[] array;

	/** The first valid entry. */
	public int offset;

	/** The number of valid bytes in {@link #array} starting from {@link #offset}. */
	public int length;

	/** The current position as a distance from {@link #offset}. */
	protected int position;

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
		this.length = Math.min(length, array.length - offset);
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
	public int read(final byte[] b, final int offset, final int length) {
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

	@Override
	public byte[] readAllBytes() {
		return readNBytes(available());
	}

	@Override
	public int readNBytes(byte[] b, int off, int len) {
		int n = read(b, off, len);
		return n == -1 ? 0 : n;
	}

	@Override
	public int read (byte[] b) {
		return read(b, 0, b.length);
	}

	@Override
	public byte[] readNBytes (int len) {
		int n = Math.min(len, available());
		byte[] result = new byte[n];
		read(result);
		return result;
	}

	@Override
	public void skipNBytes (long n) {
		skip(n);
	}

	public int peek() {
		if (length <= position()) return -1;
		return array[(int)(offset + position())] & 0xFF;
	}


	@Override
	public void readFully (byte[] b) {
		read(b);
	}

	@Override
	public void readFully (byte[] b, int off, int len) {
		read(b, off, len);
	}

	@Override
	public int skipBytes (int n) {
		return (int) skip(n);
	}

	@Override
	public boolean readBoolean () {
		return read() != 0;
	}

	@Override
	public byte readByte () {
		return (byte) read();
	}

	@Override
	public int readUnsignedByte () {
		return read() & 0xFF;
	}

	@Override
	public short readShort() {
		return (short)((read() << 8)|(read() & 0xFF));
	}

	@Override
	public int readUnsignedShort() {
		return ((read() & 0xFF) << 8)|(read() & 0xFF);
	}

	@Override
	public char readChar() {
		return (char)(((read() & 0xFF) << 8)|(read() & 0xFF));
	}

	@Override
	public int readInt() {
		return read() << 24 | ((read() & 0xFF) << 16) | ((read() & 0xFF) << 8) | (read() & 0xFF);
	}

	@Override
	public long readLong () {
		return (long) readInt() << 32 | (readInt() & 0xFFFF_FFFFL);
	}

	@Override
	public float readFloat () {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble () {
		return Double.longBitsToDouble(readLong());
	}

	@Override  @Deprecated
	public String readLine () {
		StringBuilder sb = new StringBuilder(99);
loop:
		for (int c;;){
			switch (c = read()){
			case -1:
				break loop;// eof

			case '\n':
				return sb.toString();
			case '\r':
				if (peek() == '\n'){
					read();
				}
				return sb.toString();

			default:
				sb.append((char) c);
			}
		}
		return sb.isEmpty() ? null : sb.toString();
	}

	@Override
	public String readUTF () throws UTFDataFormatException {
		try {
			return available() > 0 ? DataInputStream.readUTF(this) : null;
		} catch (UTFDataFormatException badBinaryFormatting){
			throw badBinaryFormatting;
		} catch (IOException e){
			throw new UncheckedIOException("readUTF @ "+this, e);
		}
	}

	/// not efficient! Only added to support custom {@link java.io.Externalizable}
	@Override
	public Object readObject () throws ClassNotFoundException, IOException {
		try (ObjectInputStream ois = new ObjectInputStream(this)){
			return ois.readObject();
		}
	}
}
/*
 * Copyright (C) 2005-2025 Sebastiano Vigna
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

import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/** Simple, fast byte-array output stream that exposes the backing array.
 *
 * <p>{@link java.io.ByteArrayOutputStream} is nice, but to get its content you
 * must generate each time a new object. This doesn't happen here.
 *
 * <p>This class will automatically enlarge the backing array, doubling its
 * size whenever new space is needed. The {@link #reset()} method will
 * mark the content as empty, but will not decrease the capacity: use
 * {@link #trim()} for that purpose.
 *
 * @author Sebastiano Vigna
 */
public class FastByteArrayOutputStream extends MeasurableOutputStream implements RepositionableStream, ObjectOutput {

	/** The array backing the output stream. */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	/** The array backing the output stream. */
	public byte[] array;

	/** The number of valid bytes in {@link #array}. */
	public int length;

	/** The current writing position. */
	protected int position;

	/** Creates a new array output stream with an initial capacity of {@link #DEFAULT_INITIAL_CAPACITY} bytes. */
	public FastByteArrayOutputStream() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/** Creates a new array output stream with a given initial capacity.
	 *
	 * @param initialCapacity the initial length of the backing array.
	 */
	public FastByteArrayOutputStream(final int initialCapacity) {
		array = new byte[initialCapacity];
	}

	/** Creates a new array output stream wrapping a given byte array.
	 *
	 * @param a the byte array to wrap.
	 */
	public FastByteArrayOutputStream(final byte[] a) {
		array = a;
	}

	/** Marks this array output stream as empty. */
	public void reset() {
		length = 0;
		position = 0;
	}

	/** Ensures that the length of the backing array is equal to {@link #length}. */
	public void trim() {
		array = ByteArrays.trim(array, length);
	}

	@Override
	public void write(final int b) {
		if (position >= array.length) array = ByteArrays.grow(array, position + 1, length);
		array[position++] = (byte)b;
		if (length < position) length = position;
	}

	@Override
	public void write(final byte[] b, final int off, final int len) {
		ByteArrays.ensureOffsetLength(b, off, len);
		if (position + len > array.length) array = ByteArrays.grow(array, position + len, position);
		System.arraycopy(b, off, array, position, len);
		if (position + len > length) length = position += len;
	}

	@Override
	public void position(final long newPosition) {
		if (newPosition > Integer.MAX_VALUE) throw new IllegalArgumentException("Position too large: " + newPosition);
		position = (int)newPosition;
	}

	@Override
	public long position() {
		return position;
	}

	@Override
	public long length() {
		return length;
	}

	/** @see java.io.ByteArrayOutputStream#toByteArray() */
	public byte[] toByteArray () {
		return ByteArrays.copy(array, 0, length);
	}

	@Override
	public void close () {
		// NOP: only to force no exception
	}

	@Override
	public void write(final byte[] b) {
		// Only to force no exception
		write(b, 0, b.length);
	}

	/** @see java.io.ByteArrayOutputStream#toString(Charset) */
	public String toString(Charset charset) {
		return new String(array, 0, length, charset);
	}

	/** @see java.io.ByteArrayOutputStream#writeTo(OutputStream) */
	public synchronized void writeTo(OutputStream out) throws IOException {
		out.write(array, 0, length);
	}


	@Override
	public void writeBoolean(boolean v) {
		write(v?1:0);
	}

	@Override
	public void writeByte(int v) {
		write(v);
	}

	@Override
	public void writeShort(int v) {
		write(v >> 8);
		write(v);
	}

	@Override
	public void writeChar(int v) {
		write(v >> 8);
		write(v);
	}

	@Override
	public void writeInt(int v) {
		write(v >> 24);
		write(v >> 16);
		write(v >> 8);
		write(v);
	}

	@Override
	public void writeLong(long v) {
		writeInt((int)(v >> 32));
		writeInt((int) v);
	}

	@Override
	public void writeFloat(float v) {
		writeInt(Float.floatToIntBits(v));
	}

	@Override
	public void writeDouble(double v) {
		writeLong(Double.doubleToLongBits(v));
	}

	/**
	 * @deprecated This method is dangerous as it discards the high byte of every character. For UTF-8, use {@link #writeUTF(String)} or {@link #write(byte[]) @code write(s.getBytes(UTF_8))}.
	 * @see java.io.DataOutputStream#writeBytes(String)
	 */
	@Override
	public void writeBytes(String s) {
		for (int i = 0, len = s.length(); i < len; i++){
			write((byte)s.charAt(i));
		}
	}

	@Override
	public void writeChars(String s) {
		for (int i = 0, len = s.length(); i < len; i++){
			int v = s.charAt(i);
			writeChar(v);
		}
	}

	@Override
	public void writeUTF (String s) {
		int savePos = position;
		writeShort(0);// len place holder
		for (int i = 0, len = s.length(); i < len; i++){
			writeUtf8Char(s.charAt(i));
			if (position - savePos > 0xFF_FF + 2){
				length = position = savePos;// rollback
				throw new IllegalArgumentException("UTF encoded string too long: %d: %s".formatted(s.length(), s.substring(0, 99)));
			}
		}
		int len = position - savePos - 2;
		array[savePos] = (byte)(len >> 8);
		array[savePos+1] = (byte)len;
	}

	/** @see java.io.DataOutputStream#writeUTF(String,DataOutput) */
	public int writeUtf8Char(char c) {
		if (c != 0 && c < 0x80){
			write(c);
			return 1;
		} else if (c >= 0x800){
			write(0xE0 | c >> 12 & 0x0F);
			write(0x80 | c >> 6  & 0x3F);
			write(0x80 | c       & 0x3F);
			return 3;
		} else {
			write(0xC0 | c >> 6 & 0x1F);
			write(0x80 | c      & 0x3F);
			return 2;
		}
	}

	/// not efficient! Only added to support custom {@link java.io.Externalizable}
	@Override
	public void writeObject(Object obj) throws IOException {
		try (ObjectOutputStream oout = new ObjectOutputStream(this)){
			oout.writeObject(obj);
			oout.flush();
		}
	}
}
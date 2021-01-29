/*
 * Copyright (C) 2017-2021 Sebastiano Vigna
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream.LineTerminator;

public class FastBufferedInputStreamTest {
	private static final boolean DEBUG = false;

	/** A byte array input stream that will return its data in small chunks,
	 * even it could actually return more data, and skips less bytes than it could.
	 */

	private static class BastardByteArrayInputStream extends ByteArrayInputStream {
		private static final long seed = System.currentTimeMillis();
		private static final Random r = new Random(seed);
		static {
			System.err.println("Seed: " + seed);
		}

		public BastardByteArrayInputStream(final byte[] array) {
			super(array);
		}

		@Override
		public int read(final byte[] buffer, final int offset, final int length) {
			final int k = r.nextInt(2) + 1;
			return super.read(buffer, offset, length < k ? length : k);
		}

		@Override
		public long skip(final long n) {
			final int k = r.nextInt(2);
			return super.skip(n < k ? n : k);
		}

	}

	@SuppressWarnings("resource")
	public void testReadline(final int bufferSize) throws IOException {
		FastBufferedInputStream stream;
		byte[] b;

		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);

		b = new byte[4];
		stream.readLine(b, 0, b.length, EnumSet.of(LineTerminator.CR));
		assertTrue(Arrays.toString(b), Arrays.equals(b, new byte[] { 'A', 'B', 'C', 0 }));
		assertEquals(4, stream.position());
		assertEquals(-1, stream.readLine(b, 0, b.length, EnumSet.of(LineTerminator.CR)));

		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);
		assertEquals(4, stream.readLine(b, 0, b.length, EnumSet.of(LineTerminator.LF)));
		assertEquals(4, stream.position());

		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);
		assertEquals(4, stream.readLine(b, 0, b.length, EnumSet.of(LineTerminator.LF)));
		assertEquals(4, stream.position());

		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);
		assertEquals(4, stream.readLine(b, 0, b.length, EnumSet.of(LineTerminator.CR_LF)));
		assertEquals(4, stream.position());

		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);
		assertEquals(4, stream.readLine(b, 0, b.length, EnumSet.of(LineTerminator.CR_LF)));
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', '\r' }));
		assertEquals(4, stream.position());

		b = new byte[4];
		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);
		stream.readLine(b, 0, 2, EnumSet.of(LineTerminator.CR));
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 0, 0 }));
		assertEquals(2, stream.position());

		// Reads with only LF as terminator
		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' }), bufferSize);
		assertEquals(4, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.LF)));
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', '\r' }));
		assertEquals(4, stream.position());
		assertEquals(0, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.LF)));
		assertEquals(5, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', '\r' }));
		assertEquals(1, stream.readLine(b, 2, 2, EnumSet.of(LineTerminator.LF)));
		assertEquals(6, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'D', '\r' }));

		// Reads with both LF and CR/LF as terminators
		b = new byte[4];
		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' }), bufferSize);
		assertEquals(3, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.CR, LineTerminator.CR_LF)));
		assertEquals(5, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', 0 }));
		assertEquals(1, stream.readLine(b, 2, 2, EnumSet.of(LineTerminator.CR, LineTerminator.CR_LF)));
		assertEquals(6, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'D', 0 }));

		// Reads with only CR as terminator
		b = new byte[4];
		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' }), bufferSize);
		assertEquals(3, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.CR)));
		assertEquals(4, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', 0 }));
		assertEquals(2, stream.readLine(b, 2, 2, EnumSet.of(LineTerminator.CR)));
		assertEquals(6, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', '\n', 'D' }));

		// Reads with only CR/LF as terminator
		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' }), bufferSize);
		b = new byte[4];
		assertEquals(3, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.CR_LF)));
		assertEquals(5, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', 0 }));
		assertEquals(1, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.CR_LF)));
		assertEquals(6, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'D', 'B', 'C', 0 }));
		assertEquals(-1, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.CR_LF)));

		// Reads with both CR and CR/LF as terminator

		// CR at end-of-file
		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r' }), bufferSize);
		b = new byte[4];
		assertEquals(3, stream.readLine(b, 0, 4, EnumSet.of(LineTerminator.CR_LF, LineTerminator.CR)));
		assertEquals(4, stream.position());
		assertTrue(Arrays.equals(b, new byte[] { 'A', 'B', 'C', 0 }));

	}

	@Test
	public void testReadLine() throws IOException {
		testReadline(1);
		testReadline(2);
		testReadline(3);
		testReadline(4);
		testReadline(5);
		testReadline(6);
		testReadline(7);
		testReadline(100);
	}

	@SuppressWarnings("resource")
	public void testSkip(final int bufferSize) throws IOException {
		FastBufferedInputStream stream;

		stream = new FastBufferedInputStream(new BastardByteArrayInputStream(new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' }), bufferSize);
		assertEquals(2, stream.skip(2));
		assertEquals(2, stream.position());
		assertEquals(1, stream.skip(1));
		assertEquals(3, stream.position());
		assertEquals(3, stream.skip(4));
		assertEquals(6, stream.position());
		assertEquals(0, stream.skip(1));
		assertEquals(6, stream.position());
	}

	@Test
	public void testSkip() throws IOException {
		testSkip(1);
		testSkip(2);
		testSkip(3);
		testSkip(4);
		testSkip(5);
		testSkip(6);
		testSkip(7);
		testSkip(100);
	}

	@Test
	public void testPosition() throws IOException {
		final File temp = File.createTempFile(this.getClass().getSimpleName(), ".tmp");
		temp.deleteOnExit();
		final FileOutputStream fos = new FileOutputStream(temp);
		fos.write(new byte[] { 0, 1, 2, 3, 4 });
		fos.close();

		FastBufferedInputStream stream = new FastBufferedInputStream(new FileInputStream(temp), 2);
		byte[] b = new byte[2];
		stream.read(b);
		stream.flush();
		stream.position(0);
		assertEquals(0, stream.read());
		stream.close();

		stream = new FastBufferedInputStream(new FileInputStream(temp));
		b = new byte[1];
		stream.read(b);
		stream.flush();
		stream.position(0);
		assertEquals(0, stream.read());
		stream.close();

		stream = new FastBufferedInputStream(new FileInputStream(temp));
		b = new byte[5];
		stream.read(b);
		stream.flush();
		assertEquals(-1, stream.read());
		stream.position(5);
		assertEquals(-1, stream.read());
		stream.position(0);
		assertEquals(0, stream.read());
		stream.position(1);
		assertEquals(1, stream.read());
		stream.position(3);
		assertEquals(3, stream.read());
		stream.position(1);
		assertEquals(1, stream.read());
		stream.position(0);
		assertEquals(0, stream.read());
		stream.close();
	}

	@Test
	public void testRead() throws IOException {
		// Reads with length larger than buffer size

		// No head, no stream
		InputStream stream = new FastBufferedInputStream(new ByteArrayInputStream(new byte[] {}), 1);
		byte[] b = new byte[4];

		assertEquals(-1, stream.read(b, 0, 2));

		// Some head, no stream
		stream = new FastBufferedInputStream(new ByteArrayInputStream(new byte[] { 'A', 'B' }), 2);
		b = new byte[4];

		assertEquals(1, stream.read(b, 0, 1));
		assertEquals(1, stream.read(b, 0, 3));

		// Some head, some stream
		stream = new FastBufferedInputStream(new ByteArrayInputStream(new byte[] { 'A', 'B', 'C', 'D' }), 2);
		b = new byte[4];

		assertEquals(1, stream.read(b, 0, 1));
		assertEquals(3, stream.read(b, 0, 3));

		// No head, some stream
		stream = new FastBufferedInputStream(new ByteArrayInputStream(new byte[] { 'A', 'B', 'C', 'D' }), 2);
		b = new byte[4];

		assertEquals(3, stream.read(b, 0, 3));

		// Reads with length smaller than or equal to buffer size

		// No head, no stream
		stream = new FastBufferedInputStream(new ByteArrayInputStream(new byte[] {}), 4);
		b = new byte[4];

		assertEquals(-1, stream.read(b, 0, 2));

	}

	@SuppressWarnings("resource")
	public void testRandom(final int bufferSize) throws IOException {
		final File temp = File.createTempFile(this.getClass().getSimpleName(), "tmp");
		temp.deleteOnExit();

		// Create temp random file
		final FileOutputStream out = new FileOutputStream(temp);
		final Random random = new Random();
		final int length = 100000 + random.nextInt(10000);
		for(int i = 0; i < length; i++) out.write(random.nextInt());
		out.close();

		final FastBufferedInputStream bis = new FastBufferedInputStream(new FileInputStream(temp), bufferSize);
		FileInputStream test = new FileInputStream(temp);
		FileChannel fc = test.getChannel();
		int a1, a2, off, len, pos;
		final byte b1[] = new byte[32768];
		final byte b2[] = new byte[32768];

		while(true) {

			switch(random.nextInt(6)) {

			case 0:
				if (DEBUG) System.err.println("read()");
				a1 = bis.read();
				a2 = test.read();
				assertEquals(a1, a2);
				if (a1 == -1) return;
				break;

			case 1:
				off = random.nextInt(b1.length);
				len = random.nextInt(b1.length - off + 1);
				a1 = bis.read(b1, off, len);
				a2 = test.read(b2, off, len);
				if (DEBUG) System.err.println("read(b, " + off + ", " + len + ")");

				assertEquals(a1, a2);

				for(int i = off; i < off + len; i++) assertEquals("Position " + i, b1[i], b2[i]);
				break;

			case 2:
				if (DEBUG) System.err.println("available()");
				assertEquals(bis.available(), test.available());
				break;

			case 3:
				if (DEBUG) System.err.println("position()");
				pos = (int)bis.position();
				assertEquals((int)fc.position(), pos);
				break;

			case 4:
				pos = random.nextInt(length);
				bis.position(pos);
				if (DEBUG) System.err.println("position(" + pos + ")");
				(test = new FileInputStream(temp)).skip(pos);
				fc = test.getChannel();
				break;

			case 5:
				pos = random.nextInt((int)(length - bis.position() + 1));
				a1 = (int)bis.skip(pos);
				a2 = (int)test.skip(pos);
				if (DEBUG) System.err.println("skip(" + pos + ")");
				assertEquals(a1, a2);
				break;
			}
		}

	}

	@Test
	public void testRandom() throws IOException {
		testRandom(1);
		testRandom(2);
		testRandom(3);
		testRandom(100);
		testRandom(2048);
	}
}


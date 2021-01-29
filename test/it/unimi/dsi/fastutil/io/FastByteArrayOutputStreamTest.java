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

import java.io.IOException;

import org.junit.Test;

public class FastByteArrayOutputStreamTest {

	@SuppressWarnings("resource")
	@Test
	public void testWrite() {
		FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		fbaos.write(1);
		fbaos.write(2);
		assertEquals(1, fbaos.array[0]);
		assertEquals(2, fbaos.array[1]);
		assertEquals(2, fbaos.length);
		assertEquals(2, fbaos.position());
		fbaos.position(1);
		fbaos.write(3);
		assertEquals(2, fbaos.position());
		assertEquals(2, fbaos.length);
		assertEquals(3, fbaos.array[1]);
		fbaos.write(4);
		assertEquals(3, fbaos.length);
		assertEquals(4, fbaos.array[2]);

		for(int i = 0; i < 14; i++) fbaos.write(i + 10);
		assertEquals(17, fbaos.length);
		for(int i = 0; i < 14; i++) assertEquals(i + 10, fbaos.array[3 + i]);
	}

	@SuppressWarnings("resource")
	@Test
	public void testWriteArray() throws IOException {
		FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		fbaos.write(1);
		fbaos.write(2);
		fbaos.write(3);

		byte[] a = new byte[14];
		for(int i = 0; i < 14; i++) a[i] = (byte)(i + 10);
		fbaos.write(a);
		assertEquals(17, fbaos.length);
		assertEquals(1, fbaos.array[0]);
		assertEquals(2, fbaos.array[1]);
		assertEquals(3, fbaos.array[2]);
		for(int i = 0; i < 14; i++) assertEquals(i + 10, fbaos.array[3 + i]);

		fbaos.write(a);
		assertEquals(31, fbaos.length);
		for(int i = 0; i < 14; i++) assertEquals(i + 10, fbaos.array[17 + i]);

		fbaos = new FastByteArrayOutputStream();
		fbaos.write(1);
		fbaos.write(2);
		fbaos.write(3);
		fbaos.position(2);

		fbaos.write(a);
		assertEquals(16, fbaos.length);
		assertEquals(1, fbaos.array[0]);
		assertEquals(2, fbaos.array[1]);
		for(int i = 0; i < 14; i++) assertEquals(i + 10, fbaos.array[2 + i]);

		fbaos = new FastByteArrayOutputStream();
		fbaos.write(1);
		fbaos.write(2);
		fbaos.write(3);
		fbaos.write(4);
		fbaos.position(3);

		fbaos.write(a);
		assertEquals(17, fbaos.length);
		assertEquals(1, fbaos.array[0]);
		assertEquals(2, fbaos.array[1]);
		assertEquals(3, fbaos.array[2]);
		for(int i = 0; i < 14; i++) assertEquals(i + 10, fbaos.array[3 + i]);
	}

	@SuppressWarnings("resource")
	@Test
	public void testPositionWrite() {
		FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		fbaos.position(1);
		fbaos.write(1);
		assertEquals(2, fbaos.length);
	}

	@SuppressWarnings("resource")
	@Test
	public void testPositionWrite2() {
		FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		fbaos.position(fbaos.array.length + 2);
		fbaos.write(1);
	}
}

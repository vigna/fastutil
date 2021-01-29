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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class FastBufferedOutputStreamTest {

	private static final boolean DEBUG = false;

	@Test
	public void testWriteEqualToBufferSize() throws IOException {
		@SuppressWarnings("resource")
		final FastBufferedOutputStream fbos = new FastBufferedOutputStream(new ByteArrayOutputStream(), 4);
		fbos.write(0);
		fbos.write(new byte[4]);
		fbos.write(0);
	}

	public void testRandom(int bufSize) throws FileNotFoundException, IOException {

		File file = File.createTempFile(getClass().getSimpleName(), "test");
		file.deleteOnExit();
		FastBufferedOutputStream fbos = new FastBufferedOutputStream(new FileOutputStream(file + "1"), bufSize);
		FileOutputStream bos = new FileOutputStream(file + "2");
		FileChannel fc = bos.getChannel();
		Random r = new Random();
		long pos, len;

		int j = r.nextInt(10000);
		while(j-- != 0) {
			switch(r.nextInt(6)) {

			case 0:
				int x = (byte)r.nextInt();
				fbos.write(x);
				bos.write(x);
				break;

			case 1:
				byte[] b  = new byte[r.nextInt(32768) + 16];
				for(int i = 0; i < b.length; i++) b[i] = (byte)r.nextInt();
				int offset = r.nextInt(b.length / 4);
				int length = r.nextInt(b.length - offset);
				fbos.write(b, offset, length);
				bos.write(b, offset, length);
				break;

			case 2:
				fbos.flush();
				break;

			case 3:
				if (DEBUG) System.err.println("position()");
				pos = (int)fbos.position();
				assertEquals((int)fc.position(), pos);
				break;

			case 4:
				assertEquals(fc.size(), len = fbos.length());
				pos = len != 0 ? r.nextInt((int)len) : 0;
				fbos.position(pos);
				fc.position(pos);
				if (DEBUG) System.err.println("position(" + pos + ")");
				break;
			}
		}

		fbos.close();
		bos.close();
		assertTrue(Arrays.equals(BinIO.loadBytes(file + "1"), BinIO.loadBytes(file + "2")));
	}

	@Test
	public void testRandom() throws FileNotFoundException, IOException {
		testRandom(1);
		testRandom(2);
		testRandom(3);
		testRandom(1024);
	}
}


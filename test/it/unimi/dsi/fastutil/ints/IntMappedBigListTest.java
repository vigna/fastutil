/*
 * Copyright (C) 2017-2022 Sebastiano Vigna
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

package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;
import java.util.SplittableRandom;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;

public class IntMappedBigListTest {
	@Test
	public void testReadWrite() throws IOException {
		final File file = File.createTempFile(this.getClass().getName(), ".bin");
		file.deleteOnExit();
		SplittableRandom r = new SplittableRandom(0);
		final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
		for (long i = 0; i < IntMappedBigList.CHUNK_SIZE + 10; i++) dos.writeInt(r.nextInt());
		dos.close();

		final IntMappedBigList list = IntMappedBigList.map(FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE), ByteOrder.BIG_ENDIAN, MapMode.READ_WRITE);
		r = new SplittableRandom(0);
		for (long i = 0; i < IntMappedBigList.CHUNK_SIZE + 10; i++) assertEquals(Long.toString(i), r.nextInt(), list.getInt(i));

		r = new SplittableRandom(0);
		for (long i = 0; i < IntMappedBigList.CHUNK_SIZE - 10; i++) r.nextInt();
		final int[] a = new int[20];
		list.getElements(IntMappedBigList.CHUNK_SIZE - 10, a, 0, 20);
		for (long i = IntMappedBigList.CHUNK_SIZE - 10; i < IntMappedBigList.CHUNK_SIZE + 10; i++) assertEquals(Long.toString(i), r.nextInt(), a[(int)(i - (IntMappedBigList.CHUNK_SIZE - 10))]);

		r = new SplittableRandom(0);
		list.getElements(1, a, 1, 19);
		r.nextInt();
		for (int i = 1; i < 20; i++) assertEquals(Long.toString(i), r.nextInt(), a[i]);

		r = new SplittableRandom(1);
		for (long i = 0; i < IntMappedBigList.CHUNK_SIZE + 10; i++) list.set(i, r.nextInt());

		r = new SplittableRandom(1);
		for (long i = 0; i < IntMappedBigList.CHUNK_SIZE + 10; i++) assertEquals(Long.toString(i), r.nextInt(), list.getInt(i));


		file.delete();

	}

	@Test
	public void testSmallEndian() throws IOException {
		final File file = File.createTempFile(this.getClass().getName(), ".bin");
		file.deleteOnExit();
		final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(10000);

		buffer.order(ByteOrder.LITTLE_ENDIAN);
		SplittableRandom r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) buffer.putInt(r.nextInt());
		buffer.flip();

		final FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
		channel.write(buffer);
		channel.close();

		final IntMappedBigList list = IntMappedBigList.map(FileChannel.open(file.toPath()), ByteOrder.LITTLE_ENDIAN);
		r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) assertEquals(Long.toString(i), r.nextInt(), list.getInt(i));

		final int[] a = new int[1];
		r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) {
			list.getElements(i, a, 0, 1);
			assertEquals(Long.toString(i), r.nextInt(), a[0]);
		}

		final IntMappedBigList copy = list.copy();
		r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) assertEquals(Long.toString(i), r.nextInt(), copy.getInt(i));
		file.delete();
	}

	@Test
	public void testBigEndian() throws IOException {
		final File file = File.createTempFile(this.getClass().getName(), ".bin");
		file.deleteOnExit();
		final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(10000);

		buffer.order(ByteOrder.BIG_ENDIAN);
		SplittableRandom r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) buffer.putInt(r.nextInt());
		buffer.flip();

		final FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
		channel.write(buffer);
		channel.close();

		final IntMappedBigList list = IntMappedBigList.map(FileChannel.open(file.toPath()), ByteOrder.BIG_ENDIAN);
		r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) assertEquals(Long.toString(i), r.nextInt(), list.getInt(i));

		final int[] a = new int[1];
		r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) {
			list.getElements(i, a, 0, 1);
			assertEquals(Long.toString(i), r.nextInt(), a[0]);
		}

		final IntMappedBigList copy = list.copy();
		r = new SplittableRandom(0);
		for (long i = 0; i < 100; i++) assertEquals(Long.toString(i), r.nextInt(), copy.getInt(i));
		file.delete();
	}
}

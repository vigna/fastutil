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

public class MappedIntBigListTest {
	@Test
	public void testReadWrite() throws IOException {
		final File file = File.createTempFile(this.getClass().getName(), ".bin");
		file.deleteOnExit();
		SplittableRandom r = new SplittableRandom(0);
		final DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
		for (int i = 0; i < MappedIntBigList.CHUNK_SIZE + 10; i++) dos.writeInt(r.nextInt());
		dos.close();

		final MappedIntBigList list = MappedIntBigList.map(FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE), ByteOrder.BIG_ENDIAN, MapMode.READ_WRITE);
		r = new SplittableRandom(0);
		for (int i = 0; i < MappedIntBigList.CHUNK_SIZE + 10; i++) assertEquals(Integer.toString(i), r.nextInt(), list.getInt(i));

		r = new SplittableRandom(1);
		for (int i = 0; i < MappedIntBigList.CHUNK_SIZE + 10; i++) list.set(i, r.nextInt());

		r = new SplittableRandom(1);
		for (int i = 0; i < MappedIntBigList.CHUNK_SIZE + 10; i++) assertEquals(Integer.toString(i), r.nextInt(), list.getInt(i));

		file.delete();

	}

	@Test
	public void testSmallEndian() throws IOException {
		final File file = File.createTempFile(this.getClass().getName(), ".bin");
		file.deleteOnExit();
		final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(10000);

		buffer.order(ByteOrder.LITTLE_ENDIAN);
		SplittableRandom r = new SplittableRandom(0);
		for (int i = 0; i < 100; i++) buffer.putInt(r.nextInt());
		buffer.flip();

		final FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
		channel.write(buffer);
		channel.close();

		final MappedIntBigList list = MappedIntBigList.map(FileChannel.open(file.toPath()), ByteOrder.LITTLE_ENDIAN);
		r = new SplittableRandom(0);
		for (int i = 0; i < 100; i++) assertEquals(Integer.toString(i), r.nextInt(), list.getInt(i));

		file.delete();
	}
}

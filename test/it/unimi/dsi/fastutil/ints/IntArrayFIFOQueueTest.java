package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertArrayEquals;

/*
 * Copyright (C) 2017-2020 Sebastiano Vigna
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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

public class IntArrayFIFOQueueTest {

	@Test
	public void testEnqueueDequeue() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(i, q.firstInt());
			assertEquals(i, q.dequeueInt());
			if (i != 99) assertEquals(99, q.lastInt());
		}

		q = new IntArrayFIFOQueue(10);
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(i, q.firstInt());
			assertEquals(i, q.dequeueInt());
			if (i != 99) assertEquals(99, q.lastInt());
		}

		q = new IntArrayFIFOQueue(200);
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(i, q.firstInt());
			assertEquals(i, q.dequeueInt());
			if (i != 99) assertEquals(99, q.lastInt());
		}
	}

	@Test
	public void testPeek()
	{
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		for(int i = 0;i<100;i++)
		{
			assertEquals(i, q.peekInt(i));
		}
	}

	@Test
	public void removeTest() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		q.removeInt(40);
		assertEquals(q.size(), 99);
		for(int i = 0;i<99;i++) {
			if(i >= 40) assertEquals(i + 1, q.dequeueInt());
			else assertEquals(i, q.dequeueInt());
		}
		q = new IntArrayFIFOQueue();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		q.removeInt(60);
		assertEquals(q.size(), 99);
		for(int i = 0;i<99;i++) {
			if(i >= 60) assertEquals(i + 1, q.dequeueInt());
			else assertEquals(i, q.dequeueInt());
		}
		int[][] expectedResults = new int[][]{
			{50, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{50, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 47, 48, 49, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
		IntList index = IntArrayList.wrap(new int[]{0});
		q = new IntArrayFIFOQueue(100) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean removeInt(int x) {
				if(super.removeInt(x))
				{
					assertArrayEquals(expectedResults[index.getInt(0)], array);
					return true;
				}
				return false;
			}
		};
		for(int i = 0; i < 75; i++) {
			q.enqueue(i);
		}
		for(int i = 0; i < 50; i++) {
			q.dequeueInt();
		}
		for(int i = 0; i < 50; i++) {
			q.enqueue(i);
		}
		q.removeInt(60);
		index.set(0, 1);
		q.removeInt(40);
		assertEquals(q.size(), 73);
		for(int i = 0;i<73;i++) {
			if(i >= 24) assertEquals(i - (i < 64 ? 24 : 23), q.dequeueInt());
			else assertEquals(i + (i >= 10 ? 51 : 50), q.dequeueInt());
		}
		
	}

	@Test
	public void testArray()
	{
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		Integer[] ref = new Integer[100];
		Integer[] shiftArray = new Integer[100];
		int[] primRef = new int[100];
		int[] shiftPrimArray = new int[100];
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
			ref[i] = Integer.valueOf(i);
			primRef[i] = i;
			shiftPrimArray[(i+80) % 100] = i;
			shiftArray[(i+80) % 100] = Integer.valueOf(i);
		}
		assertArrayEquals(q.toArray(), ref);
		assertArrayEquals(q.toArray(new Integer[100]), ref);
		assertArrayEquals(q.toArray(null), ref);
		assertArrayEquals(q.toIntArray(), primRef);
		assertArrayEquals(q.toIntArray(new int[100]), primRef);
		assertArrayEquals(q.toIntArray(null), primRef);
		IntArrayFIFOQueue other = new IntArrayFIFOQueue(q.toIntArray());
		for(int i = 0;i<100;i++) {
			assertEquals(other.peekInt(i), primRef[i]);
		}
		for(int i = 0;i<20;i++) {
			other.dequeueInt();
			other.enqueue(i);
		}
		assertArrayEquals(other.toIntArray(), shiftPrimArray);
		assertArrayEquals(other.toIntArray(new int[100]), shiftPrimArray);
		assertArrayEquals(other.toArray(), shiftArray);
	}

	@Test
	public void testMix() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		for(int i = 0, p = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				assertEquals(j + i * 20, q.lastInt());
			}
			for(int j = 0; j < 10; j++) assertEquals(p++, q.dequeueInt());
		}

		q = new IntArrayFIFOQueue(10);
		for(int i = 0, p = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				assertEquals(j + i * 20, q.lastInt());
			}
			for(int j = 0; j < 10; j++) assertEquals(p++, q.dequeueInt());
		}

		q = new IntArrayFIFOQueue(200);
		for(int i = 0, p = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				assertEquals(j + i * 20, q.lastInt());
			}
			for(int j = 0; j < 10; j++) assertEquals(p++, q.dequeueInt());
		}
	}

	@Test
	public void testWrap() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue(30);
		for(int i = 0; i < 20; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		for(int j = 0; j < 10; j++) assertEquals(j, q.dequeueInt());
		for(int i = 0; i < 15; i++) {
			q.enqueue(i);
			assertEquals(i, q.lastInt());
		}
		for(int j = 10; j < 20; j++) assertEquals(j, q.dequeueInt());
		for(int j = 0; j < 15; j++) assertEquals(j, q.dequeueInt());
	}

	@Test
	public void testTrim() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue(30);
		for(int j = 0; j < 20; j++) q.enqueue(j);
		for(int j = 0; j < 10; j++) assertEquals(j, q.dequeueInt());
		for(int j = 0; j < 15; j++) q.enqueue(j);

		q.trim();
		for(int j = 10; j < 20; j++) assertEquals(j, q.dequeueInt());
		for(int j = 0; j < 15; j++) assertEquals(j, q.dequeueInt());

		q = new IntArrayFIFOQueue(30);
		for(int j = 0; j < 20; j++) q.enqueue(j);
		q.trim();
		for(int j = 0; j < 20; j++) assertEquals(j, q.dequeueInt());
	}

	@Test
	public void testDeque() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue(4);
		q.enqueue(0);
		q.enqueue(1);
		q.enqueue(2);
		assertEquals(q.dequeueInt(), 0);
		assertEquals(q.dequeueInt(), 1);
		q.enqueue(3);
		assertEquals(q.dequeueLastInt(), 3);
		assertEquals(q.dequeueLastInt(), 2);
		q.enqueueFirst(1);
		q.enqueueFirst(0);
		assertEquals(0, q.dequeueInt());
		assertEquals(1, q.dequeueInt());



		q = new IntArrayFIFOQueue(4);
		q.enqueueFirst(0);
		q.enqueueFirst(1);
		assertEquals(1, q.dequeueInt());
		assertEquals(0, q.dequeueInt());
		q.enqueueFirst(0);
		q.enqueueFirst(1);
		q.enqueueFirst(2);
		q.enqueueFirst(3);
		assertEquals(3, q.dequeueInt());
		assertEquals(2, q.dequeueInt());
		assertEquals(1, q.dequeueInt());
		assertEquals(0, q.dequeueInt());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testImmediateReduce() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		q.enqueue(0);
		q.dequeue();
	}

	@SuppressWarnings("deprecation")
	private static final void assertSameQueue(IntArrayFIFOQueue a, IntArrayFIFOQueue b) {
		assertEquals(a.size(), b.size());
		while(! a.isEmpty() && ! b.isEmpty()) assertEquals(a.dequeue(), b.dequeue());
		assertEquals(Boolean.valueOf(a.isEmpty()) , Boolean.valueOf(b.isEmpty()));
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		File temp = File.createTempFile(IntArrayFIFOQueueTest.class.getSimpleName() + "-", "-test");
		temp.deleteOnExit();
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		BinIO.storeObject(q, temp);
		assertSameQueue(q, (IntArrayFIFOQueue)BinIO.loadObject(temp));

		for(int i = 0; i < 100; i++) q.enqueue(i);
		BinIO.storeObject(q, temp);
		assertSameQueue(q, (IntArrayFIFOQueue)BinIO.loadObject(temp));

		q.trim();
		for(int i = 0; i < 128; i++) q.enqueue(i);
		BinIO.storeObject(q, temp);
		assertSameQueue(q, (IntArrayFIFOQueue)BinIO.loadObject(temp));

		temp.delete();
	}
}
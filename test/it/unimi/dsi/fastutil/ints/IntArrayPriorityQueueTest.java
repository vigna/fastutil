package it.unimi.dsi.fastutil.ints;


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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

@SuppressWarnings("deprecation")
public class IntArrayPriorityQueueTest {

	@Test
	public void removeTest() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		IntHeapPriorityQueue h = new IntHeapPriorityQueue();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		q.removeInt(50);
		h.removeInt(50);
		assertEquals(q.size(), 99);
		for(int i = 0;i<99;i++) {
			if(i >= 50){
				assertEquals(i + 1, q.dequeueInt());
				assertEquals(i + 1, h.dequeueInt());
			}
			else {
				assertEquals(i, q.dequeueInt());
				assertEquals(i, h.dequeueInt());
			}
		}
	}

	@Test
	public void testToArray()
	{
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		IntHeapPriorityQueue h = new IntHeapPriorityQueue();
		Integer[] ref = new Integer[100];
		int[] primRef = new int[100];
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
			ref[i] = Integer.valueOf(i);
			primRef[i] = i;
		}
		Integer[] qArray = q.toArray();
		assertArrayEquals(qArray, ref);
		assertArrayEquals(qArray, q.toArray(new Integer[100]));
		assertArrayEquals(qArray, q.toArray(null));
		assertArrayEquals(qArray, h.toArray());
		assertArrayEquals(qArray, h.toArray(new Integer[100]));
		assertArrayEquals(qArray, h.toArray(null));
		int[] qPrimArray = q.toIntArray();
		assertArrayEquals(qPrimArray, primRef);
		assertArrayEquals(qPrimArray, h.toIntArray(new int[100]));
		assertArrayEquals(qPrimArray, h.toIntArray(null));
		assertArrayEquals(qPrimArray, h.toIntArray());
		assertArrayEquals(qPrimArray, h.toIntArray(new int[100]));
		assertArrayEquals(qPrimArray, h.toIntArray(null));
	}

	@Test
	public void testEnqueueDequeue() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		IntHeapPriorityQueue h = new IntHeapPriorityQueue();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new IntArrayPriorityQueue(10);
		h.clear();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new IntArrayPriorityQueue(200);
		h.clear();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}
	}

	@Test
	public void testEnqueueDequeueComp() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue(IntComparators.OPPOSITE_COMPARATOR);
		IntHeapPriorityQueue h = new IntHeapPriorityQueue(IntComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new IntArrayPriorityQueue(10, IntComparators.OPPOSITE_COMPARATOR);
		h.clear();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new IntArrayPriorityQueue(200, IntComparators.OPPOSITE_COMPARATOR);
		h.clear();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}
	}

	@Test
	public void testMix() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		IntHeapPriorityQueue h = new IntHeapPriorityQueue();
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeueInt(), q.dequeueInt());
		}

		q = new IntArrayPriorityQueue(10);
		h = new IntHeapPriorityQueue();
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * -20);
				h.enqueue(j + i * -20);
				q.first();
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeueInt(), q.dequeueInt());
		}

		q = new IntArrayPriorityQueue(200);
		h = new IntHeapPriorityQueue();
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeueInt(), q.dequeueInt());
		}
	}

	@Test
	public void testMixComp() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue(IntComparators.OPPOSITE_COMPARATOR);
		IntHeapPriorityQueue h = new IntHeapPriorityQueue(IntComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeueInt(), q.dequeueInt());
		}

		q = new IntArrayPriorityQueue(10, IntComparators.OPPOSITE_COMPARATOR);
		h = new IntHeapPriorityQueue(IntComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * -20);
				h.enqueue(j + i * -20);
				q.first();
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeueInt(), q.dequeueInt());
		}

		q = new IntArrayPriorityQueue(200, IntComparators.OPPOSITE_COMPARATOR);
		h = new IntHeapPriorityQueue(IntComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeueInt(), q.dequeueInt());
		}
	}

	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		for(int i = 0; i < 100; i++) q.enqueue(i);

		File file = File.createTempFile(getClass().getPackage().getName() + "-", "-tmp");
		file.deleteOnExit();
		BinIO.storeObject(q, file);
		IntArrayPriorityQueue r = (IntArrayPriorityQueue)BinIO.loadObject(file);
		file.delete();
		for(int i = 0; i < 100; i++) {
			assertEquals(q.first(), r.first());
			assertEquals(q.dequeue(), r.dequeue());
		}
	}
}


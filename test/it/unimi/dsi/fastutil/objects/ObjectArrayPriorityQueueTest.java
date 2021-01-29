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

package it.unimi.dsi.fastutil.objects;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

@SuppressWarnings({"boxing","unchecked"})
public class ObjectArrayPriorityQueueTest {

	@Test
	public void testEnqueueDequeue() {
		ObjectArrayPriorityQueue<Integer> q = new ObjectArrayPriorityQueue<>();
		ObjectHeapPriorityQueue<Integer> h = new ObjectHeapPriorityQueue<>();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<>(10);
		h.clear();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<>(200);
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
		ObjectArrayPriorityQueue<Integer> q = new ObjectArrayPriorityQueue<Integer>(ObjectComparators.OPPOSITE_COMPARATOR);
		ObjectHeapPriorityQueue<Integer> h = new ObjectHeapPriorityQueue<Integer>(ObjectComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<Integer>(10, ObjectComparators.OPPOSITE_COMPARATOR);
		h.clear();
		for(int i = 0; i < 100; i++) {
			q.enqueue(i);
			h.enqueue(i);
		}
		for(int i = 0; i < 100; i++) {
			assertEquals(h.first(), q.first());
			assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<Integer>(200, ObjectComparators.OPPOSITE_COMPARATOR);
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
		ObjectArrayPriorityQueue<Integer> q = new ObjectArrayPriorityQueue<>();
		ObjectHeapPriorityQueue<Integer> h = new ObjectHeapPriorityQueue<>();
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<>(10);
		h = new ObjectHeapPriorityQueue<>();
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * -20);
				h.enqueue(j + i * -20);
				q.first();
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<>(200);
		h = new ObjectHeapPriorityQueue<>();
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeue(), q.dequeue());
		}
	}

	@Test
	public void testMixComp() {
		ObjectArrayPriorityQueue<Integer> q = new ObjectArrayPriorityQueue<Integer>(ObjectComparators.OPPOSITE_COMPARATOR);
		ObjectHeapPriorityQueue<Integer> h = new ObjectHeapPriorityQueue<Integer>(ObjectComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<Integer>(10, ObjectComparators.OPPOSITE_COMPARATOR);
		h = new ObjectHeapPriorityQueue<Integer>(ObjectComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * -20);
				h.enqueue(j + i * -20);
				q.first();
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeue(), q.dequeue());
		}

		q = new ObjectArrayPriorityQueue<Integer>(200, ObjectComparators.OPPOSITE_COMPARATOR);
		h = new ObjectHeapPriorityQueue<Integer>(ObjectComparators.OPPOSITE_COMPARATOR);
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 20; j++) {
				q.enqueue(j + i * 20);
				h.enqueue(j + i * 20);
			}
			for(int j = 0; j < 10; j++) assertEquals(h.dequeue(), q.dequeue());
		}
	}

	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		ObjectArrayPriorityQueue<Integer> q = new ObjectArrayPriorityQueue<>();
		for(int i = 0; i < 100; i++) q.enqueue(i);

		File file = File.createTempFile(getClass().getPackage().getName() + "-", "-tmp");
		file.deleteOnExit();
		BinIO.storeObject(q, file);
		ObjectArrayPriorityQueue<Integer> r = (ObjectArrayPriorityQueue<Integer>)BinIO.loadObject(file);
		file.delete();
		for(int i = 0; i < 100; i++) {
			assertEquals(q.first(), r.first());
			assertEquals(q.dequeue(), r.dequeue());
		}
	}
}


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
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class IntArrayIndirectPriorityQueueTest {

	@Test
	public void testFront() {
		int refArray[] = { 4, 3, 2, 1, 0, 3, 2, 1, 0, 2, 1, 0, 1, 0, 0 };
		int tops[] = new int[refArray.length];
		final IntArrayIndirectPriorityQueue queue = new IntArrayIndirectPriorityQueue(refArray);
		for (int i = refArray.length; i-- != 0;)
			queue.enqueue(i);

		assertEquals(5, queue.front(tops));
		assertEquals(new IntOpenHashSet(new int[] { 4, 8, 11, 13, 14 }), new IntOpenHashSet(tops, 0, 5));
		for (int i = 4; i-- != 0;) {
			queue.dequeue();
			assertEquals(i + 1, queue.front(tops));
		}
		queue.dequeue();

		assertEquals(4, queue.front(tops));
		assertEquals(new IntOpenHashSet(new int[] { 3, 7, 10, 12 }), new IntOpenHashSet(tops, 0, 4));
		for (int i = 3; i-- != 0;) {
			queue.dequeue();
			assertEquals(i + 1, queue.front(tops));
		}
		queue.dequeue();

		assertEquals(3, queue.front(tops));
		assertEquals(new IntOpenHashSet(new int[] { 2, 6, 9 }), new IntOpenHashSet(tops, 0, 3));
		for (int i = 2; i-- != 0;) {
			queue.dequeue();
			assertEquals(i + 1, queue.front(tops));
		}
		queue.dequeue();

		assertEquals(2, queue.front(tops));
		assertEquals(new IntOpenHashSet(new int[] { 1, 5 }), new IntOpenHashSet(tops, 0, 2));
		queue.dequeue();
		assertEquals(1, queue.front(tops));
		queue.dequeue();

		assertEquals(1, queue.front(tops));
	}


	private int[] ref;

	private boolean heapEqual(int[] a, int[] b, int sizea, int sizeb) {
		if (sizea != sizeb) return false;
		int[] aa = new int[sizea];
		int[] bb = new int[sizea];
		for (int i = 0; i < sizea; i++) {
			aa[i] = ref[a[i]];
			bb[i] = ref[b[i]];
		}
		java.util.Arrays.sort(aa);
		java.util.Arrays.sort(bb);
		while (sizea-- != 0)
			if (!((aa[sizea]) == (bb[sizea]))) return false;
		return true;
	}

	public void test(int n, IntComparator comparator) {
		Exception mThrowsIllegal, tThrowsIllegal, mThrowsOutOfBounds, tThrowsOutOfBounds, mThrowsNoElement, tThrowsNoElement;
		int rm = 0, rt = 0;
		Random r = new Random(0);
		ref = new int[n];

		for (int i = 0; i < n; i++) ref[i] = r.nextInt();

		IntArrayIndirectPriorityQueue m = new IntArrayIndirectPriorityQueue(ref, comparator);
		IntHeapIndirectPriorityQueue t = new IntHeapIndirectPriorityQueue(ref, comparator);

		/* We add pairs to t. */
		for (int i = 0; i < n / 2; i++) {
			t.enqueue(i);
			m.enqueue(i);
		}

		assertTrue("Error: m and t differ after creation (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

		/* Now we add and remove random data in m and t, checking that the result is the same. */

		for (int i = 0; i < 2 * n; i++) {
			if (r.nextDouble() < 0.01) {
				t.clear();
				m.clear();
				for (int j = 0; j < n / 2; j++) {
					t.enqueue(j);
					m.enqueue(j);
				}
			}

			int T = r.nextInt(2 * n);

			mThrowsNoElement = tThrowsNoElement = mThrowsOutOfBounds = tThrowsOutOfBounds = mThrowsIllegal = tThrowsIllegal = null;

			try {
				t.enqueue(T);
			}
			catch (IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			catch (IllegalArgumentException e) {
				tThrowsIllegal = e;
			}

			if (tThrowsIllegal == null) { // To skip duplicates
				try {
					m.enqueue(T);
				}
				catch (IndexOutOfBoundsException e) {
					mThrowsOutOfBounds = e;
				}
				catch (IllegalArgumentException e) {
					mThrowsIllegal = e;
				}
			}

			mThrowsIllegal = tThrowsIllegal = null; // To skip duplicates

			assertTrue("Error: enqueue() divergence in IndexOutOfBoundsException for " + T + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error: enqueue() divergence in IllegalArgumentException for " + T + " (" + mThrowsIllegal + ", " + tThrowsIllegal + ")",
					(mThrowsIllegal == null) == (tThrowsIllegal == null));

			assertTrue("Error: m and t differ after enqueue (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

			if (m.size() != 0) {
				assertTrue("Error: m and t differ in first element after enqueue (" + m.first() + "->" + ref[m.first()] + ", " + t.first() + "->" + ref[t.first()] + ")",
						((ref[m.first()]) == (ref[t.first()])));
			}

			mThrowsNoElement = tThrowsNoElement = mThrowsOutOfBounds = tThrowsOutOfBounds = mThrowsIllegal = tThrowsIllegal = null;

			try {
				rm = m.dequeue();
				while (!m.isEmpty() && ((ref[m.first()]) == (ref[rm])))	m.dequeue();
			}
			catch (IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			catch (IllegalArgumentException e) {
				mThrowsIllegal = e;
			}
			catch (java.util.NoSuchElementException e) {
				mThrowsNoElement = e;
			}

			try {
				rt = t.dequeue();
				while (!t.isEmpty() && ((ref[t.first()]) == (ref[rt])))
					t.dequeue();
			}
			catch (IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			catch (IllegalArgumentException e) {
				tThrowsIllegal = e;
			}
			catch (java.util.NoSuchElementException e) {
				tThrowsNoElement = e;
			}

			assertTrue("Error: dequeue() divergence in IndexOutOfBoundsException (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error: dequeue() divergence in IllegalArgumentException  (" + mThrowsIllegal + ", " + tThrowsIllegal + ")", (mThrowsIllegal == null) == (tThrowsIllegal == null));
			assertTrue("Error: dequeue() divergence in java.util.NoSuchElementException  (" + mThrowsNoElement + ", " + tThrowsNoElement + ")",
					(mThrowsNoElement == null) == (tThrowsNoElement == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error: divergence in dequeue() between m and t (" + rm + "->" + ref[rm] + ", " + rt + "->" + ref[rt] + ")",
					((ref[rt]) == (ref[rm])));


			assertTrue("Error: m and t differ after dequeue (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

			if (m.size() != 0) {
				assertTrue("Error: m and t differ in first element after dequeue (" + m.first() + "->" + ref[m.first()] + ", " + t.first() + "->" + ref[t.first()] + ")",
						((ref[m.first()]) == (ref[t.first()])));
			}

			mThrowsNoElement = tThrowsNoElement = mThrowsOutOfBounds = tThrowsOutOfBounds = mThrowsIllegal = tThrowsIllegal = null;


			int pos = r.nextInt(n * 2);

			try {
				m.remove(pos);
			}
			catch (IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			catch (IllegalArgumentException e) {
				mThrowsIllegal = e;
			}
			catch (java.util.NoSuchElementException e) {
				mThrowsNoElement = e;
			}

			try {
				t.remove(pos);
			}
			catch (IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			catch (IllegalArgumentException e) {
				tThrowsIllegal = e;
			}
			catch (java.util.NoSuchElementException e) {
				tThrowsNoElement = e;
			}

			assertTrue("Error: remove(int) divergence in IndexOutOfBoundsException (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error: remove(int) divergence in IllegalArgumentException  (" + mThrowsIllegal + ", " + tThrowsIllegal + ")", (mThrowsIllegal == null) == (tThrowsIllegal == null));
			assertTrue("Error: remove(int) divergence in java.util.NoSuchElementException  (" + mThrowsNoElement + ", " + tThrowsNoElement + ")",
					(mThrowsNoElement == null) == (tThrowsNoElement == null));

			assertTrue("Error: m and t differ after remove(int) (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

			if (m.size() != 0) {
				assertTrue("Error: m and t differ in first element after remove(int) (" + m.first() + "->" + ref[m.first()] + ", " + t.first() + "->" + ref[t.first()] + ")",
						((ref[m.first()]) == (ref[t.first()])));
			}


			mThrowsNoElement = tThrowsNoElement = mThrowsOutOfBounds = tThrowsOutOfBounds = mThrowsIllegal = tThrowsIllegal = null;

			pos = r.nextInt(n);

			try {
				t.changed(pos);
			}
			catch (IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			catch (IllegalArgumentException e) {
				tThrowsIllegal = e;
			}
			catch (java.util.NoSuchElementException e) {
				tThrowsNoElement = e;
			}

			if (tThrowsIllegal == null) {
				try {
					m.changed(pos);
				}
				catch (IndexOutOfBoundsException e) {
					mThrowsOutOfBounds = e;
				}
				catch (IllegalArgumentException e) {
					mThrowsIllegal = e;
				}
				catch (java.util.NoSuchElementException e) {
					mThrowsNoElement = e;
				}
			}

			assertTrue("Error: change(int) divergence in IndexOutOfBoundsException (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			// assertTrue("Error: change(int) divergence in IllegalArgumentException  (" +
			// mThrowsIllegal + ", " + tThrowsIllegal + ")" , (mThrowsIllegal == null) == (
			// tThrowsIllegal == null));
			assertTrue("Error: change(int) divergence in java.util.NoSuchElementException  (" + mThrowsNoElement + ", " + tThrowsNoElement + ")",
					(mThrowsNoElement == null) == (tThrowsNoElement == null));

			assertTrue("Error: m and t differ after change(int) (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

			if (m.size() != 0) {
				assertTrue("Error: m and t differ in first element after change(int) (" + m.first() + "->" + ref[m.first()] + ", " + t.first() + "->" + ref[t.first()] + ")",
						((ref[m.first()]) == (ref[t.first()])));
			}

			int[] temp = t.heap.clone();
			IntArrays.quickSort(temp, 0, t.size()); // To scramble a bit
			m = new IntArrayIndirectPriorityQueue(m.refArray, temp, t.size(), comparator);

			assertTrue("Error: m and t differ after wrap (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

			if (m.size() != 0) {
				assertTrue("Error: m and t differ in first element after wrap (" + m.first() + "->" + ref[m.first()] + ", " + t.first() + "->" + ref[t.first()] + ")",
						((ref[m.first()]) == (ref[t.first()])));
			}

			if (m.size() != 0 && ((new it.unimi.dsi.fastutil.ints.IntOpenHashSet(m.array, 0, m.size)).size() == m.size())) {

				int first = m.first();
				ref[first] = r.nextInt();

				// System.err.println("Pre-change m: " +m);
				// System.err.println("Pre-change t: " +t);
				m.changed();
				t.changed(first);

				// System.err.println("Post-change m: " +m);
				// System.err.println("Post-change t: " +t);

				assertTrue("Error: m and t differ after change (" + m + ", " + t + ")", heapEqual(m.array, t.heap, m.size(), t.size()));

				if (m.size() != 0) {
					assertTrue("Error: m and t differ in first element after change (" + m.first() + "->" + ref[m.first()] + ", " + t.first() + "->" + ref[t.first()] + ")",
							((ref[m.first()]) == (ref[t.first()])));
				}
			}
		}


		/* Now we check that m actually holds the same data. */

		m.clear();
		assertTrue("Error: m is not empty after clear()", m.isEmpty());
	}


	@Test
	public void test1() {
		test(1, null);
		test(1, IntComparators.OPPOSITE_COMPARATOR);

	}

	@Test
	public void test10() {
		test(10, null);
		test(10, IntComparators.OPPOSITE_COMPARATOR);
	}

	@Test
	public void test100() {
		test(100, null);
		test(100, IntComparators.OPPOSITE_COMPARATOR);
	}

	@Test
	public void test1000() {
		test(1000, null);
		test(1000, IntComparators.OPPOSITE_COMPARATOR);
	}

}

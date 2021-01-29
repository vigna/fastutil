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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;


public class IntSemiIndirectHeapsTest {

	@Test
	public void testFront() {
		final int numBits = 20;
		int[] refArray = new int[100], heap = new int[100], front = new int[100];

		for(int i = (1 << numBits) - 1; i-- != 0;) {
			for(int j = 0; j < numBits; j++) {
				refArray[j] = (i & (1 << j));
				heap[j] = j;
			}

			IntSemiIndirectHeaps.makeHeap(refArray, heap, numBits, null);
			assertEquals("Heap " + Integer.toBinaryString(i), numBits - Integer.bitCount(i), IntSemiIndirectHeaps.front(refArray, heap, numBits, front));
		}
	}

	@Test
	public void testFrontWithComparator() {
		final int[] refArray = { 8, 16, 9 };
		final int[] heap = { 2, 1, 0 };

		IntComparator comparator = (k1, k2) -> (k1 & 3) - (k2 & 3);
		IntSemiIndirectHeaps.makeHeap(refArray, heap, 3, comparator);
		final int[] front = new int[2];
		assertEquals(2, IntSemiIndirectHeaps.front(refArray, heap, 3, front, comparator));
		Arrays.sort(front);
		assertArrayEquals(new int[] { 0, 1 }, front);
	}
}

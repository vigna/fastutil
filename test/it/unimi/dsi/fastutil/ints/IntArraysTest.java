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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class IntArraysTest {

	public static int[] identity(final int n) {
		final int[] perm = new int[n];
		for(int i = perm.length; i-- != 0;) perm[i] = i;
		return perm;
	}

	@Test
	public void testMergeSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		IntArrays.mergeSort(b);
		assertArrayEquals(sorted, b);
		IntArrays.mergeSort(b);
		assertArrayEquals(sorted, b);

		final int[] d = a.clone();
		IntArrays.mergeSort(d, (k1, k2) -> k1 - k2);
		assertArrayEquals(sorted, d);

		IntArrays.mergeSort(d, (k1, k2) -> k1 - k2);
		assertArrayEquals(sorted, d);
	}


	@Test
	public void testMergeSortSmallSupport() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < a.length; to++)
			for(int from = 0; from <= to; from++) {
				final int[] support = new int[to];
				System.arraycopy(a,  0, support, 0,  to);
				IntArrays.mergeSort(a, from, to, support);
				if (from < to) for(int i = to - 1; i-- != from;) assertTrue(a[i] <= a[i + 1]);
			}
	}

	@Test
	public void testStableSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		IntArrays.stableSort(b);
		assertArrayEquals(sorted, b);
		IntArrays.stableSort(b);
		assertArrayEquals(sorted, b);

		final int[] d = a.clone();
		IntArrays.stableSort(d, (k1, k2) -> k1 - k2);
		assertArrayEquals(sorted, d);

		IntArrays.stableSort(d, (k1, k2) -> k1 - k2);
		assertArrayEquals(sorted, d);
	}

	@Test
	public void testQuickSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);

		final int[] d = a.clone();
		IntArrays.quickSort(d, (k1, k2) -> k1 - k2);
		assertArrayEquals(sorted, d);
		IntArrays.quickSort(d, (k1, k2) -> k1 - k2);
		assertArrayEquals(sorted, d);
	}

	@Test
	public void testParallelQuickSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);

		final int[] d = a.clone();
		IntArrays.parallelQuickSort(d, 0, d.length);
		assertArrayEquals(sorted, d);
	}

	@Test
	public void testQuickSort1() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.quickSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testQuickSort1Undirect() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity(t.length);
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[] { 2, -1, 0, -4 };
		perm = identity(t.length);
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		perm = identity(t.length);
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSortIndirect(perm, t, 10, 90);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.quickSortIndirect(perm, t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		t = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.quickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);
	}

	@Test
	public void testQuickSort1Comp() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.quickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.quickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.quickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.quickSort(t, 10, 100, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = 99; i-- != 10;) assertTrue(t[i] >= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.quickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);
	}


	@Test
	public void testParallelQuickSort1Comp() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t, 10, 100, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = 99; i-- != 10;) assertTrue(t[i] >= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);
	}


	@Test
	public void testParallelQuickSort1() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testParallelQuickSort1Undirect() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity(t.length);
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[] { 2, -1, 0, -4 };
		perm = identity(t.length);
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		perm = identity(t.length);
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSortIndirect(perm, t, 10, 90);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.parallelQuickSortIndirect(perm, t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		t = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelQuickSortIndirect(perm, t);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);
	}

	@Test
	public void testUnstableSort1() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.unstableSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.unstableSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.unstableSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.unstableSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.unstableSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.unstableSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.unstableSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testQuickSort2() {
		int[][] d = new int[2][];

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = IntArrays.shuffle(identity(10), new Random(0));
		IntArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = IntArrays.shuffle(identity(100000), new Random(6));
		IntArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		IntArrays.quickSort(d[0], d[1], 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}


	@Test
	public void testParallelQuickSort2() {
		int[][] d = new int[2][];

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = IntArrays.shuffle(identity(10), new Random(0));
		IntArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = IntArrays.shuffle(identity(100000), new Random(6));
		IntArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		IntArrays.parallelQuickSort(d[0], d[1], 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testShuffle() {
		int[] a = new int[100];
		for(int i = a.length; i-- != 0;) a[i] = i;
		IntArrays.shuffle(a, new Random());
		boolean[] b = new boolean[a.length];
		for(int i = a.length; i-- != 0;) {
			assertFalse(b[a[i]]);
			b[a[i]] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		int[] a = new int[100];
		for(int i = a.length; i-- != 0;) a[i] = -1;
		for(int i = 10; i < 30; i++) a[i] = i - 10;
		IntArrays.shuffle(a, 10, 30, new Random());
		boolean[] b = new boolean[20];
		for(int i = 20; i-- != 0;) {
			assertFalse(b[a[i + 10]]);
			b[a[i + 10]] = true;
		}
	}

	@Test
	public void testRadixSort1() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.radixSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testParallelRadixSort1() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.parallelRadixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[] { 2, -1, 0, -4 };
		IntArrays.parallelRadixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.parallelRadixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new int[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue("@" + i + ": " + t[i] + " > " + t[i + 1],  t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.parallelRadixSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new int[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testRadixSort2() {
		int[][] d = new int[2][];

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = IntArrays.shuffle(identity(10), new Random(0));
		IntArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = IntArrays.shuffle(identity(100000), new Random(6));
		IntArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d[0], d[1], 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testParallelRadixSort2() {
		int[][] d = new int[2][];

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = IntArrays.shuffle(identity(10), new Random(0));
		IntArrays.parallelRadixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = IntArrays.shuffle(identity(100000), new Random(6));
		IntArrays.parallelRadixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.parallelRadixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.parallelRadixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		IntArrays.parallelRadixSort(d[0], d[1], 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.parallelRadixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}


	@Test
	public void testRadixSort() {
		int[][] t = { { 2, 1, 0, 4 } };
		IntArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		t[0] = IntArrays.shuffle(identity(100), new Random(0));
		IntArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		int[][] d = new int[2][];

		d[0] = new int[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = IntArrays.shuffle(identity(10), new Random(0));
		IntArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new int[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = IntArrays.shuffle(identity(100000), new Random(6));
		IntArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10];
		Random random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new int[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new int[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new int[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		IntArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testRadixSortIndirectStable() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[] { 2, -1, 0, -4 };
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(4);
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);

		t = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, 10, 90, true);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, 10, 100, true);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		t = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(8);
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);
	}

	@Test
	public void testRadixSortIndirectUnstable() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[] { 2, -1, 0, -4 };
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, 10, 90, false);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, 10, 100, false);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		t = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);
	}

	@Test
	public void testParallelRadixSortIndirectStable() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[] { 2, -1, 0, -4 };
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(4);
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);

		t = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, 10, 90, true);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, 10, 100, true);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		t = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(8);
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);
	}

	@Test
	public void testParallelRadixSortIndirectUnstable() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[] { 2, -1, 0, -4 };
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, 10, 90, false);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, 10, 100, false);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(Integer.toString(i), t[perm[i]] <= t[perm[i + 1]]);

		t = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.parallelRadixSortIndirect(perm, t, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);
	}

	@Test
	public void testRadixSort2IndirectStable() {
		int[] t = { 2, 1, 0, 4 };
		int[] u = { 3, 2, 1, 0 };
		int[] perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(u[perm[i]] <= u[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		u = IntArrays.shuffle(identity(100), new Random(1));
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[100];
		u = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[t.length];
		u = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		for(int i = 0; i < u.length; i++) t[i] = random.nextInt(4);
		for(int i = 0; i < u.length; i++) u[i] = random.nextInt(4);
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]] && u[perm[i]] == u[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);

		t = new int[100];
		u = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = u.length; i-- != 0;) u[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, 10, 90, true);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), u[perm[i]] <= u[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		u = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, 10, 100, true);
		for(int i = 99; i-- != 10;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[10000000];
		u = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[t.length];
		u = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(8);
		for(int i = 0; i < t.length; i++) u[i] = random.nextInt(8);
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]] && u[perm[i]] == u[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);
	}

	@Test
	public void testRadixSort2IndirectUnstable() {
		int[] t = { 2, 1, 0, 4 };
		int[] u = { 3, 2, 1, 0 };
		int[] perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(u[perm[i]] <= u[perm[i + 1]]);

		t = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, t, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = IntArrays.shuffle(identity(100), new Random(0));
		u = IntArrays.shuffle(identity(100), new Random(1));
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[100];
		u = new int[100];
		perm = identity(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[t.length];
		u = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		for(int i = 0; i < u.length; i++) t[i] = random.nextInt(4);
		for(int i = 0; i < u.length; i++) u[i] = random.nextInt(4);
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] ||  t[perm[i]] == t[perm[i + 1]]&& u[perm[i]] <= u[perm[i + 1]]);

		t = new int[100];
		u = new int[100];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = u.length; i-- != 0;) u[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, 10, 90, false);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), u[perm[i]] <= u[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new int[100000];
		u = new int[100000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, 10, 100, false);
		for(int i = 99; i-- != 10;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[10000000];
		u = new int[10000000];
		perm = identity(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new int[t.length];
		u = new int[t.length];
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new int[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(8);
		for(int i = 0; i < t.length; i++) u[i] = random.nextInt(8);
		perm = identity(t.length);
		IntArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(i + " " +  t[perm[i]]+ " "+ t[perm[i+1]] + " " + u[perm[i]] + " " + u[perm[i+1]] + "  " + perm[i]+ " " +perm[i+1], t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);
	}

	@Test
	public void testBinarySearchLargeKey() {
		final int[] a = { 1, 2, 3 };
		IntArrays.binarySearch(a, 4);
	}

	@Test
	public void testReverse() {
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, IntArrays.reverse(new int[] { 3, 2, 1, 0 }));
		assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, IntArrays.reverse(new int[] { 4, 3, 2, 1, 0 }));
		assertArrayEquals(new int[] { 4, 1, 2, 3, 0 }, IntArrays.reverse(new int[] { 4, 3, 2, 1, 0 }, 1, 4));
		assertArrayEquals(new int[] { 4, 2, 3, 1, 0 }, IntArrays.reverse(new int[] { 4, 3, 2, 1, 0 }, 1, 3));
		assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, IntArrays.reverse(new int[] { 0, 1, 2, 3, 4 }, 1, 2));
	}

	@Test
	public void testStabilize() {
		int[] perm, val;

		perm = new int[] { 0, 1, 2, 3 };
		val = new int[] { 0, 0, 0, 0 };

		IntArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, perm);

		perm = new int[] { 3, 1, 2, 0 };
		val = new int[] { 0, 0, 0, 0 };

		IntArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, perm);

		perm = new int[] { 3, 2, 1, 0 };
		val = new int[] { 0, 1, 1, 2 };

		IntArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 3, 1, 2, 0 }, perm);

		perm = new int[] { 3, 2, 1, 0 };
		val = new int[] { 0, 0, 1, 1 };

		IntArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 2, 3, 0, 1 }, perm);

		perm = new int[] { 4, 3, 2, 1, 0 };
		val = new int[] { 1, 1, 0, 0, 0 };

		IntArrays.stabilize(perm, val, 1, 3);
		assertArrayEquals(new int[] { 4, 2, 3, 1, 0 }, perm);
	}
	
	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntArrays.class, "test", /*num=*/"1000", /*seed=*/"848747");
	}
}

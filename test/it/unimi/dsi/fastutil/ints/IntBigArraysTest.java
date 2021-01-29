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

import static it.unimi.dsi.fastutil.BigArrays.copy;
import static it.unimi.dsi.fastutil.BigArrays.get;
import static it.unimi.dsi.fastutil.BigArrays.length;
import static it.unimi.dsi.fastutil.BigArrays.set;
import static it.unimi.dsi.fastutil.BigArrays.trim;
import static it.unimi.dsi.fastutil.BigArrays.wrap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.longs.LongBigArrays;

public class IntBigArraysTest {


	public static int[][] identity(final int n) {
		final int[][] perm = IntBigArrays.newBigArray(n);
		for(int i = n; i-- != 0;) set(perm, i , i);
		return perm;
	}

	public static long[][] identity(final long n) {
		final long[][] perm = LongBigArrays.newBigArray(n);
		for(long i = n; i-- != 0;) set(perm, i , i);
		return perm;
	}

	@Test
	public void testQuickSort() {
		final int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };

		Arrays.sort(s);
		final int[][] sorted = wrap(s.clone());

		int[][] a = wrap(s.clone());

		IntBigArrays.quickSort(a);
		assertArrayEquals(sorted, a);

		IntBigArrays.quickSort(a);
		assertArrayEquals(sorted, a);

		a = wrap(s.clone());

		IntBigArrays.quickSort(a, IntComparators.NATURAL_COMPARATOR);
		assertArrayEquals(sorted, a);

		IntBigArrays.quickSort(a, IntComparators.NATURAL_COMPARATOR);
		assertArrayEquals(sorted, a);

	}

	@Test
	public void testParallelQuickSort1Comp() {
		int[][] t = wrap(new int[] { 2, 1, 0, 4 });
		IntBigArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) >= get(t, i + 1));

		t = wrap(new int[] { 2, -1, 0, -4 });
		IntBigArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) >= get(t, i + 1));

		t = IntBigArrays.shuffle(identity(100), new Random(0));
		IntBigArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) >= get(t, i + 1));

		t = IntBigArrays.newBigArray(100);
		Random random = new Random(0);
		for (long i = length(t) - 1; i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) >= get(t, i + 1));

		t = IntBigArrays.newBigArray(100000);
		random = new Random(0);
		for (long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) >= get(t, i + 1));
		for(int i = 100; i-- != 10;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t, 10, 100, IntComparators.OPPOSITE_COMPARATOR);
		for (int i = 99; i-- != 10;) assertTrue(get(t, i) >= get(t, i + 1));

		t = IntBigArrays.newBigArray(10000000);
		random = new Random(0);
		for (long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t, IntComparators.OPPOSITE_COMPARATOR);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) >= get(t, i + 1));
	}

	@Test
	public void testParallelQuickSort1() {
		int[][] t = wrap(new int[] { 2, 1, 0, 4 });
		IntBigArrays.parallelQuickSort(t);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = wrap(new int[] { 2, -1, 0, -4 });
		IntBigArrays.parallelQuickSort(t);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.shuffle(identity(100), new Random(0));
		IntBigArrays.parallelQuickSort(t);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.newBigArray(100);
		Random random = new Random(0);
		for (long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.newBigArray(100000);
		random = new Random(0);
		for (long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));
		for (int i = 100; i-- != 10;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t, 10, 100);
		for (int i = 99; i-- != 10;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.newBigArray(10000000);
		random = new Random(0);
		for (long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.parallelQuickSort(t);
		for (long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));
	}

	private void testCopy(final int n) {
		final int[][] a = IntBigArrays.newBigArray(n);
		for (int i = 0; i < n; i++) set(a, i, i);
		copy(a, 0, a, 1, n - 2);
		assertEquals(0, a[0][0]);
		for (int i = 0; i < n - 2; i++) assertEquals(i,  get(a, i + 1));
		for (int i = 0; i < n; i++) set(a, i, i);
		copy(a, 1, a, 0, n - 1);
		for (int i = 0; i < n - 1; i++) assertEquals(i + 1, get(a, i));
		for (int i = 0; i < n; i++) set(a, i, i);
		final int[] b = new int[n];
		for (int i = 0; i < n; i++) b[i] = i;
		assertArrayEquals(a, wrap(b));
	}

	@Test
	public void testCopy10() {
		testCopy(10);
	}

	@Test
	public void testCopy1000() {
		testCopy(1000);
	}

	@Test
	public void testCopy1000000() {
		testCopy(1000000);
	}

	@Test
	public void testBinarySearch() {
		final int[] a = new int[] { 25, 32, 1, 3, 2, 0, 40, 7, 13, 12, 11, 10, -1, -6, -18, 2000 };

		Arrays.sort(a);
		final int[][] b = wrap(a.clone());

		for(int i = -1; i < 20; i++) {
			assertEquals(String.valueOf(i), Arrays.binarySearch(a, i), IntBigArrays.binarySearch(b, i));
			assertEquals(String.valueOf(i), Arrays.binarySearch(a, i), IntBigArrays.binarySearch(b, i, IntComparators.NATURAL_COMPARATOR));
		}

		for(int i = -1; i < 20; i++) {
			assertEquals(Arrays.binarySearch(a, 5, 13, i), IntBigArrays.binarySearch(b, 5, 13, i));
			assertEquals(Arrays.binarySearch(a, 5, 13, i), IntBigArrays.binarySearch(b, 5, 13, i, IntComparators.NATURAL_COMPARATOR));
		}
	}

	@Test
	public void testTrim() {
		final int[] a = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final int[][] b = wrap(a.clone());

		for(int i = a.length; i-- != 0;) {
			final int[][] t = trim(b, i);
			final long l = length(t);
			assertEquals(i, l);
			for(int p = 0; p < l; p++) assertEquals(a[p], get(t, p));

		}
	}

	@Test
	public void testEquals() {
		final int[] a = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final int[][] b = wrap(a.clone());
		final int[][] c = wrap(a.clone());

		assertTrue(BigArrays.equals(b, c));
		b[0][0] = 0;
		assertFalse(BigArrays.equals(b, c));
	}

	@Test
	public void testRadixSort1() {
		int[][] t = wrap(new int[] { 2, 1, 0, 4 });
		IntBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = wrap(new int[] { 2, -1, 0, -4 });
		IntBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.shuffle(identity(100), new Random(0));
		IntBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.newBigArray(100);
		Random random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.newBigArray(100000);
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));
		for(long i = 100; i-- != 10;) set(t, i, random.nextInt());
		IntBigArrays.radixSort(t, 10, 100);
		for(long i = 99; i-- != 10;) assertTrue(get(t, i) <= get(t, i + 1));

		t = IntBigArrays.newBigArray(1000000);
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));
	}

	@Test
	public void testRadixSort2() {
		int d[][], e[][];
		d = IntBigArrays.newBigArray(10);
		for(long i = length(d); i-- != 0;) set(d, i, (int)(3 - i % 3));
		e = IntBigArrays.shuffle(identity(10), new Random(0));
		IntBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = IntBigArrays.newBigArray(100000);
		for(long i = length(d); i-- != 0;) set(d, i, (int)(100 - i % 100));
		e = IntBigArrays.shuffle(identity(100000), new Random(6));
		IntBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = IntBigArrays.newBigArray(10);
		for(long i = length(d); i-- != 0;) set(d, i, (int)(i % 3 - 2));
		Random random = new Random(0);
		e = IntBigArrays.newBigArray(length(d));
		for(long i = length(d); i-- != 0;) set(e, i, random.nextInt());
		IntBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = IntBigArrays.newBigArray(100000);
		random = new Random(0);
		for(long i = length(d); i-- != 0;) set(d, i, random.nextInt());
		e = IntBigArrays.newBigArray(length(d));
		for(long i = length(d); i-- != 0;) set(e, i, random.nextInt());
		IntBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));
		for(long i = 100; i-- != 10;) set(e, i, random.nextInt());
		IntBigArrays.radixSort(d, e, 10, 100);
		for(long i = 99; i-- != 10;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = IntBigArrays.newBigArray(1000000);
		random = new Random(0);
		for(long i = length(d); i-- != 0;) set(d, i, random.nextInt());
		e = IntBigArrays.newBigArray(length(d));
		for(long i = length(d); i-- != 0;) set(e, i, random.nextInt());
		IntBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));
	}

	@Test
	public void testRadixSort2IndirectStable() {
		int[][] t = wrap(new int[] { 2, 1, 0, 4 });
		int[][] u = wrap(new int[] { 3, 2, 1, 0 });
		long[][] perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) <= get(t, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, t, true);
		for(long i = length(t) - 1; i-- != 0;) assertEquals(i, get(perm, i));

		t = IntBigArrays.shuffle(identity(100), new Random(0));
		u = IntBigArrays.shuffle(identity(100), new Random(1));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(100);
		u = IntBigArrays.newBigArray(100);
		perm = identity(length(t));
		Random random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		for(long i = length(t); i-- != 0;) set(u, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		u = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertEquals(i, get(perm, i));

		for(long i = 0; i < length(t); i++) set(t, i, random.nextInt(4));
		for(long i = 0; i < length(t); i++) set(u, i, random.nextInt(4));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) if (get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) == get(u, get(perm, i + 1))) assertTrue(get(perm, i) < get(perm, i + 1));

		t = IntBigArrays.newBigArray(100);
		u = IntBigArrays.newBigArray(100);
		perm = identity(length(t));
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(u, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, 10, 90, true);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), get(u, get(perm, i)) <= get(u, get(perm, i + 1)));
		for(int i = 0; i < 10; i++) assertEquals(i, get(perm, i));
		for(int i = 90; i < 100; i++) assertEquals(i, get(perm, i));

		t = IntBigArrays.newBigArray(100000);
		u = IntBigArrays.newBigArray(100000);
		perm = identity(length(t));
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		LongBigArrays.shuffle(perm, new Random(0));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		for(int i = 100; i-- != 10;) set(t, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, 10, 100, true);
		for(int i = 99; i-- != 10;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(10000000);
		u = IntBigArrays.newBigArray(10000000);
		perm = identity(length(t));
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		for(long i = length(t); i-- != 0;) set(u, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		u = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) assertEquals(i, get(perm, i));

		t = IntBigArrays.newBigArray(length(t));
		for(long i = 0; i < length(t); i++) set(t, i, random.nextInt(8));
		for(long i = 0; i < length(t); i++) set(u, i, random.nextInt(8));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, true);
		for(long i = length(t) - 1; i-- != 0;) if (get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) == get(u, get(perm, i + 1))) assertTrue(get(perm, i) < get(perm, i + 1));
	}

	@Test
	public void testRadixSort2IndirectUnstable() {
		int[][] t = wrap(new int[] { 2, 1, 0, 4 });
		int[][] u = wrap(new int[] { 3, 2, 1, 0 });
		long[][] perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) <= get(t, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, t, false);
		for(long i = length(t) - 1; i-- != 0;) assertEquals(i, get(perm, i));

		t = IntBigArrays.shuffle(identity(100), new Random(0));
		u = IntBigArrays.shuffle(identity(100), new Random(1));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(100);
		u = IntBigArrays.newBigArray(100);
		perm = identity(length(t));
		Random random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		for(long i = length(t); i-- != 0;) set(u, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		u = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertEquals(i, get(perm, i));

		for(long i = 0; i < length(t); i++) set(t, i, random.nextInt(4));
		for(long i = 0; i < length(t); i++) set(u, i, random.nextInt(4));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) ||  get(t, get(perm, i)) == get(t, get(perm, i + 1))&& get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(100);
		u = IntBigArrays.newBigArray(100);
		perm = identity(length(t));
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(u, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, 10, 90, false);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), get(u, get(perm, i)) <= get(u, get(perm, i + 1)));
		for(int i = 0; i < 10; i++) assertEquals(i, get(perm, i));
		for(int i = 90; i < 100; i++) assertEquals(i, get(perm, i));

		t = IntBigArrays.newBigArray(100000);
		u = IntBigArrays.newBigArray(100000);
		perm = identity(length(t));
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		LongBigArrays.shuffle(perm, new Random(0));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		for(int i = 100; i-- != 10;) set(t, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, 10, 100, false);
		for(int i = 99; i-- != 10;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(10000000);
		u = IntBigArrays.newBigArray(10000000);
		perm = identity(length(t));
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		for(long i = length(t); i-- != 0;) set(u, i, random.nextInt());
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));

		t = IntBigArrays.newBigArray(length(t));
		u = IntBigArrays.newBigArray(length(t));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertEquals(i, get(perm, i));

		t = IntBigArrays.newBigArray(length(t));
		for(long i = 0; i < length(t); i++) set(t, i, random.nextInt(8));
		for(long i = 0; i < length(t); i++) set(u, i, random.nextInt(8));
		perm = identity(length(t));
		IntBigArrays.radixSortIndirect(perm, t, u, false);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(i + " " +  get(t, get(perm, i))+ " "+ get(t, get(perm, i + 1)) + " " + get(u, get(perm, i)) + " " + get(u, get(perm, i + 1)) + "  " + get(perm, i)+ " " +get(perm, i + 1), get(t, get(perm, i)) < get(t, get(perm, i + 1)) || get(t, get(perm, i)) == get(t, get(perm, i + 1)) && get(u, get(perm, i)) <= get(u, get(perm, i + 1)));
	}

	@Test
	public void testShuffle() {
		final int[] a = new int[100];
		for(int i = a.length; i-- != 0;) a[i] = i;
		final int[][] b = wrap(a);
		IntBigArrays.shuffle(b, new Random());
		final boolean[] c = new boolean[a.length];
		for(long i = length(b); i-- != 0;) {
			assertFalse(c[get(b, i)]);
			c[get(b, i)] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		final int[] a = new int[100];
		for(int i = a.length; i-- != 0;) a[i] = -1;
		for(int i = 10; i < 30; i++) a[i] = i - 10;
		final int[][] b = wrap(a);
		IntBigArrays.shuffle(b, 10, 30, new Random());
		final boolean[] c = new boolean[20];
		for(int i = 20; i-- != 0;) {
			assertFalse(c[get(b, i + 10)]);
			c[get(b, i + 10)] = true;
		}
	}

	@Test
	public void testBinarySearchLargeKey() {
		final int[][] a = wrap(new int[] { 1, 2, 3 });
		IntBigArrays.binarySearch(a, 4);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntBigArrays.class, "test", /*num=*/"10000", /*seed=*/"293843");
	}
}
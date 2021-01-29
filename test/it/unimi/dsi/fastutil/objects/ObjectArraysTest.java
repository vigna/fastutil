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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

@SuppressWarnings("boxing")
public class ObjectArraysTest {

	public static Integer[] identity(final int n) {
		final Integer[] perm = new Integer[n];
		for(int i = perm.length; i-- != 0;) perm[i] = i;
		return perm;
	}

	@Test
	public void testMergeSort() {
		Integer[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		ObjectArrays.mergeSort(b);
		assertArrayEquals(sorted, b);
		ObjectArrays.mergeSort(b);
		assertArrayEquals(sorted, b);

		final Integer[] d = a.clone();
		ObjectArrays.mergeSort(d, (k1, k2) -> k1.compareTo(k2));
		assertArrayEquals(sorted, d);

		ObjectArrays.mergeSort(d, (k1, k2) -> k1.compareTo(k2));
		assertArrayEquals(sorted, d);
	}


	@Test
	public void testMergeSortSmallSupport() {
		Integer[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < a.length; to++)
			for(int from = 0; from <= to; from++) {
				final Integer[] support = new Integer[to];
				System.arraycopy(a, 0, support, 0, to);
				ObjectArrays.mergeSort(a, from, to, support);
				if (from < to) for(int i = to - 1; i-- != from;) assertTrue(a[i] <= a[i + 1]);
			}
	}

	@Test
	public void testQuickSort() {
		Integer[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);

		final Integer[] d = a.clone();
		ObjectArrays.quickSort(d, (k1, k2) -> k1.compareTo(k2));
		assertArrayEquals(sorted, d);
		ObjectArrays.quickSort(d, (k1, k2) -> k1.compareTo(k2));
		assertArrayEquals(sorted, d);
	}

	@Test
	public void testParallelQuickSort() {
		Integer[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort(sorted);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);
		Arrays.sort(b);
		assertArrayEquals(sorted, b);

		final Integer[] d = a.clone();
		ObjectArrays.parallelQuickSort(d, 0, d.length);
		assertArrayEquals(sorted, d);
	}

	@Test
	public void testLargeParallelQuickSortWithComparator() {
		Object [] a = new Object[8192+1]; // PARALLEL_QUICKSORT_NO_FORK
		for (int i = 0; i < a.length; i++) {
			a[i] = new Object();
		}
		ObjectArrays.parallelQuickSort(a, (o1, o2) -> Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2)));
	}

	@Test
	public void testSmallParallelQuickSortWithComparator() {
		Object [] a = new Object[8];
		for (int i = 0; i < a.length; i++) {
			a[i] = new Object();
		}
		ObjectArrays.parallelQuickSort(a, (o1, o2) -> Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2)));
	}

	@Test
	public void testQuickSort1() {
		Integer[] t = { 2, 1, 0, 4 };
		ObjectArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[] { 2, -1, 0, -4 };
		ObjectArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = ObjectArrays.shuffle(identity(100), new Random(0));
		ObjectArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	private static final Comparator<Integer> OPPOSITE_COMPARATOR = (o1, o2) -> o2.compareTo(o1);

	@Test
	public void testQuickSort1Comp() {
		Integer[] t = { 2, 1, 0, 4 };
		ObjectArrays.quickSort(t, OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new Integer[] { 2, -1, 0, -4 };
		ObjectArrays.quickSort(t, OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = ObjectArrays.shuffle(identity(100), new Random(0));
		ObjectArrays.quickSort(t, OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new Integer[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t, OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);

		t = new Integer[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t, OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t, 10, 100, OPPOSITE_COMPARATOR);
		for(int i = 99; i-- != 10;) assertTrue(t[i] >= t[i + 1]);

		t = new Integer[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.quickSort(t, OPPOSITE_COMPARATOR);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] >= t[i + 1]);
	}


	@Test
	public void testParallelQuickSort1() {
		Integer[] t = { 2, 1, 0, 4 };
		ObjectArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[] { 2, -1, 0, -4 };
		ObjectArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = ObjectArrays.shuffle(identity(100), new Random(0));
		ObjectArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
		for(int i = 100; i-- != 10;) t[i] = random.nextInt();
		ObjectArrays.parallelQuickSort(t, 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(t[i] <= t[i + 1]);

		t = new Integer[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		ObjectArrays.parallelQuickSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testQuickSort2() {
		Integer[][] d = new Integer[2][];

		d[0] = new Integer[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = ObjectArrays.shuffle(identity(10), new Random(0));
		ObjectArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = ObjectArrays.shuffle(identity(100000), new Random(6));
		ObjectArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new Integer[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		ObjectArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new Integer[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		ObjectArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		ObjectArrays.quickSort(d[0], d[1], 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new Integer[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		ObjectArrays.quickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);
	}


	@Test
	public void testParallelQuickSort2() {
		Integer[][] d = new Integer[2][];

		d[0] = new Integer[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = ObjectArrays.shuffle(identity(10), new Random(0));
		ObjectArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = ObjectArrays.shuffle(identity(100000), new Random(6));
		ObjectArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new Integer[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		ObjectArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new Integer[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		ObjectArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);
		for(int i = 100; i-- != 10;) d[0][i] = random.nextInt();
		for(int i = 100; i-- != 10;) d[1][i] = random.nextInt();
		ObjectArrays.parallelQuickSort(d[0], d[1], 10, 100);
		for(int i = 99; i-- != 10;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);

		d[0] = new Integer[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new Integer[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		ObjectArrays.parallelQuickSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i].equals(d[0][i + 1]) && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testShuffle() {
		Integer[] a = new Integer[100];
		for(int i = a.length; i-- != 0;) a[i] = i;
		ObjectArrays.shuffle(a, new Random());
		boolean[] b = new boolean[a.length];
		for(int i = a.length; i-- != 0;) {
			assertFalse(b[a[i]]);
			b[a[i]] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		Integer[] a = new Integer[100];
		for(int i = a.length; i-- != 0;) a[i] = -1;
		for(int i = 10; i < 30; i++) a[i] = i - 10;
		ObjectArrays.shuffle(a, 10, 30, new Random());
		boolean[] b = new boolean[20];
		for(int i = 20; i-- != 0;) {
			assertFalse(b[a[i + 10]]);
			b[a[i + 10]] = true;
		}
	}

	@Test
	public void testBinarySearchLargeKey() {
		final Integer[] a = { 1, 2, 3 };
		ObjectArrays.binarySearch(a, 4);
	}

	@Test
	public void testReverse() {
		assertArrayEquals(new Integer[] { 0, 1, 2, 3 }, ObjectArrays.reverse(new Integer[] { 3, 2, 1, 0 }));
		assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4 }, ObjectArrays.reverse(new Integer[] { 4, 3, 2, 1, 0 }));
		assertArrayEquals(new Integer[] { 4, 1, 2, 3, 0 }, ObjectArrays.reverse(new Integer[] { 4, 3, 2, 1, 0 }, 1, 4));
		assertArrayEquals(new Integer[] { 4, 2, 3, 1, 0 }, ObjectArrays.reverse(new Integer[] { 4, 3, 2, 1, 0 }, 1, 3));
		assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4 }, ObjectArrays.reverse(new Integer[] { 0, 1, 2, 3, 4 }, 1, 2));
	}

	@Test
	public void testStabilize() {
		int[] perm;
		Integer[] val;

		perm = new int[] { 0, 1, 2, 3 };
		val = new Integer[] { 0, 0, 0, 0 };

		ObjectArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, perm);

		perm = new int[] { 3, 1, 2, 0 };
		val = new Integer[] { 0, 0, 0, 0 };

		ObjectArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, perm);

		perm = new int[] { 3, 2, 1, 0 };
		val = new Integer[] { 0, 1, 1, 2 };

		ObjectArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 3, 1, 2, 0 }, perm);

		perm = new int[] { 3, 2, 1, 0 };
		val = new Integer[] { 0, 0, 1, 1 };

		ObjectArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 2, 3, 0, 1 }, perm);

		perm = new int[] { 4, 3, 2, 1, 0 };
		val = new Integer[] { 1, 1, 0, 0, 0 };

		ObjectArrays.stabilize(perm, val, 1, 3);
		assertArrayEquals(new int[] { 4, 2, 3, 1, 0 }, perm);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectArrays.class, "test", /*num=*/"1000", /*seed=*/"848747");
	}
}

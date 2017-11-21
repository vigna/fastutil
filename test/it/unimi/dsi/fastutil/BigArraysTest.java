package it.unimi.dsi.fastutil;

/*
 * Copyright (C) 2017 Sebastiano Vigna
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

import java.util.Arrays;

import org.junit.Test;

import it.unimi.dsi.fastutil.ints.IntBigArrays;

public class BigArraysTest {

	@Test
	public void testMergeSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final int[][] a = IntBigArrays.wrap(s.clone());

		Arrays.sort(s);
		int[][] sorted = IntBigArrays.wrap(s.clone());
		BigArrays.mergeSort(0, IntBigArrays.length(a), (k1, k2) -> IntBigArrays.get(a, k1) - IntBigArrays.get(a, k2), (k1, k2) -> IntBigArrays.swap(a, k1, k2));
		assertArrayEquals(sorted, a);

		BigArrays.mergeSort(0, IntBigArrays.length(a), (k1, k2) -> IntBigArrays.get(a, k1) - IntBigArrays.get(a, k2), (k1, k2) -> IntBigArrays.swap(a, k1, k2));
		assertArrayEquals(sorted, a);

	}

	@Test
	public void testQuickSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };

		Arrays.sort(s);
		int[][] sorted = IntBigArrays.wrap(s.clone());

		final int[][] a = IntBigArrays.wrap(s.clone());
		BigArrays.quickSort(0, IntBigArrays.length(a), (k1, k2) -> IntBigArrays.get(a, k1) - IntBigArrays.get(a, k2), (k1, k2) -> IntBigArrays.swap(a, k1, k2));
		assertArrayEquals(sorted, a);

		BigArrays.quickSort(0, IntBigArrays.length(a), (k1, k2) -> IntBigArrays.get(a, k1) - IntBigArrays.get(a, k2), (k1, k2) -> IntBigArrays.swap(a, k1, k2));
		assertArrayEquals(sorted, a);
	}
}

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

package it.unimi.dsi.fastutil;

import static it.unimi.dsi.fastutil.BigArrays.get;
import static it.unimi.dsi.fastutil.BigArrays.length;
import static it.unimi.dsi.fastutil.BigArrays.swap;
import static it.unimi.dsi.fastutil.BigArrays.wrap;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

import it.unimi.dsi.fastutil.longs.LongBigArrayBigList;

public class BigArraysTest {

	@Test
	public void testMergeSort() {
		final int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final int[][] a = wrap(s.clone());

		Arrays.sort(s);
		final int[][] sorted = wrap(s.clone());
		BigArrays.mergeSort(0, length(a), (k1, k2) -> get(a, k1) - get(a, k2), (k1, k2) -> swap(a, k1, k2));
		assertArrayEquals(sorted, a);

		BigArrays.mergeSort(0, length(a), (k1, k2) -> get(a, k1) - get(a, k2), (k1, k2) -> swap(a, k1, k2));
		assertArrayEquals(sorted, a);

	}

	@Test
	public void testQuickSort() {
		final int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };

		Arrays.sort(s);
		final int[][] sorted = wrap(s.clone());

		final int[][] a = wrap(s.clone());
		BigArrays.quickSort(0, length(a), (k1, k2) -> get(a, k1) - get(a, k2), (k1, k2) -> swap(a, k1, k2));
		assertArrayEquals(sorted, a);

		BigArrays.quickSort(0, length(a), (k1, k2) -> get(a, k1) - get(a, k2), (k1, k2) -> swap(a, k1, k2));
		assertArrayEquals(sorted, a);
	}

	@Test
	public void testEnsureCapacity() {
		final LongBigArrayBigList longList = new LongBigArrayBigList();
		longList.size(4096);
		// Never completes!
		longList.ensureCapacity(2);
	}
}

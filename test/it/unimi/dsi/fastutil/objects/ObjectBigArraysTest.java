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

import org.junit.Test;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.MainRunner;

public class ObjectBigArraysTest {

	@SuppressWarnings({ "unchecked", "boxing" })
	@Test
	public void testquickSort() {
		final Integer[] s = new Integer[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };

		Arrays.sort(s);
		final Integer[][] sorted = wrap(s.clone());

		Integer[][] a = wrap(s.clone());

		ObjectBigArrays.quickSort(a);
		assertArrayEquals(sorted, a);

		ObjectBigArrays.quickSort(a);
		assertArrayEquals(sorted, a);

		a = wrap(s.clone());

		ObjectBigArrays.quickSort(a, ObjectComparators.NATURAL_COMPARATOR);
		assertArrayEquals(sorted, a);

		ObjectBigArrays.quickSort(a, ObjectComparators.NATURAL_COMPARATOR);
		assertArrayEquals(sorted, a);

	}

	@SuppressWarnings("boxing")
	private void testCopy(final int n) {
		final Object[][] a = ObjectBigArrays.newBigArray(n);
		for (int i = 0; i < n; i++) set(a, i, i);
		copy(a, 0, a, 1, n - 2);
		assertEquals(0, a[0][0]);
		for (int i = 0; i < n - 2; i++) assertEquals(i,  get(a, i + 1));
		for (int i = 0; i < n; i++) set(a, i, i);
		copy(a, 1, a, 0, n - 1);
		for (int i = 0; i < n - 1; i++) assertEquals(i + 1, get(a, i));
		for (int i = 0; i < n; i++) set(a, i, i);
		final Integer[] b = new Integer[n];
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

	@SuppressWarnings({ "boxing", "unchecked" })
	@Test
	public void testBinarySearch() {
		final Integer[] a = new Integer[] { 25, 32, 1, 3, 2, 0, 40, 7, 13, 12, 11, 10, -1, -6, -18, 2000 };

		Arrays.sort(a);
		final Integer[][] b = wrap(a.clone());

		for(int i = -1; i < 20; i++) {
			assertEquals(String.valueOf(i), Arrays.binarySearch(a, i), ObjectBigArrays.binarySearch(b, i));
			assertEquals(String.valueOf(i), Arrays.binarySearch(a, i), ObjectBigArrays.binarySearch(b, i, ObjectComparators.NATURAL_COMPARATOR));
		}

		for(int i = -1; i < 20; i++) {
			assertEquals(Arrays.binarySearch(a, 5, 13, i), ObjectBigArrays.binarySearch(b, 5, 13, i));
			assertEquals(Arrays.binarySearch(a, 5, 13, i), ObjectBigArrays.binarySearch(b, 5, 13, i, ObjectComparators.NATURAL_COMPARATOR));
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public void testTrim() {
		final Integer[] a = new Integer[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final Integer[][] b = wrap(a.clone());

		for(int i = a.length; i-- != 0;) {
			final Integer[][] t = trim(b, i);
			final long l = length(t);
			assertEquals(i, l);
			for(int p = 0; p < l; p++) assertEquals(a[p], get(t, p));

		}
	}

	@SuppressWarnings("boxing")
	@Test
	public void testEquals() {
		final Integer[] a = new Integer[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final Integer[][] b = wrap(a.clone());
		final Integer[][] c = wrap(a.clone());

		assertTrue(BigArrays.equals(b, c));
		b[0][0] = 0;
		assertFalse(BigArrays.equals(b, c));
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectBigArrays.class, "test", /*num=*/"10000", /*seed=*/"293843");
	}
}
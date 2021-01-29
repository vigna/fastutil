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

import org.junit.Test;

public class IntListTest {
	@Test
	public void testOf() {
		final IntList l = IntList.of(0, 1, 2);
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2 }), l);
	}

	@Test
	public void testOfEmpty() {
		final IntList l = IntList.of();
		assertTrue(l.isEmpty());
	}

	@Test
	public void testOfSingleton() {
		final IntList l = IntList.of(0);
		assertEquals(IntArrayList.wrap(new int[] { 0 }), l);
	}

	@Test
	public void testOfPair() {
		final IntList l = IntList.of(0, 1);
		assertEquals(IntArrayList.wrap(new int[] { 0, 1 }), l);
	}

	@Test
	public void testOfTriplet() {
		final IntList l = IntList.of(0, 1, 2);
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2 }), l);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testOfImmutable() {
		final IntList l = IntList.of(0, 1, 2);
		l.add(3);
	}
}

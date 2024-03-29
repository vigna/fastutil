/*
 * Copyright (C) 2017-2022 Sebastiano Vigna
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

public class IntSetTest {
	@Test
	public void testOf() {
		final IntSet s = IntSet.of(0, 1, 2, 3);
		assertEquals(new IntOpenHashSet(new int[] { 0, 1, 2, 3 }), s);
	}

	@Test
	public void testOfEmpty() {
		final IntSet s = IntSet.of();
		assertTrue(s.isEmpty());

	}

	@Test
	public void testOfSingleton() {
		final IntSet s = IntSet.of(0);
		assertEquals(new IntOpenHashSet(new int[] { 0 }), s);
	}

	@Test
	public void testOfPair() {
		final IntSet s = IntSet.of(0, 1);
		assertEquals(new IntOpenHashSet(new int[] { 0, 1 }), s);
	}

	@Test
	public void testOfTriplet() {
		final IntSet s = IntSet.of(0, 1, 2);
		assertEquals(new IntOpenHashSet(new int[] { 0, 1, 2 }), s);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOfDuplicateThrows() {
		IntSet.of(0, 0, 0, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testOfImmutable() {
		final IntSet l = IntSet.of(0, 1, 2);
		l.add(3);
	}
}

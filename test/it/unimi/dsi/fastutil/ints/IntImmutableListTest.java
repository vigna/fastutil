package it.unimi.dsi.fastutil.ints;

/*
 * Copyright (C) 2017-2020 Sebastiano Vigna
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntImmutableListTest {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEmptyListIsDifferentFromEmptySet() {
		assertFalse(IntLists.EMPTY_LIST.equals(IntSets.EMPTY_SET));
		assertFalse(IntSets.EMPTY_SET.equals(IntLists.EMPTY_LIST));
	}


	@Test
	public void testConstructors() {
		final int[] a = new int[] { 0, 1, 2 };
		assertEquals(IntArrayList.wrap(a), new IntImmutableList(a));
		assertEquals(IntArrayList.wrap(a), new IntImmutableList(a, 0, a.length));
	}

	@Test
	public void testOf() {
		final IntImmutableList l = IntImmutableList.of(0, 1, 2);
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2 }), l);
		final int[] a = new int[] { 0, 1, 2 };
		assertEquals(IntArrayList.wrap(a), IntImmutableList.of(a));
	}

	@Test
	public void testOfEmpty() {
		final IntImmutableList l = IntImmutableList.of();
		assertTrue(l.isEmpty());
	}

	@Test
	public void testOfSingleton() {
		final IntImmutableList l = IntImmutableList.of(0);
		assertEquals(IntArrayList.wrap(new int[] { 0 }), l);
	}
}

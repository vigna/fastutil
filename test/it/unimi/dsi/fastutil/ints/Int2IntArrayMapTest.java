/*
 * Copyright (C) 2020 Sebastiano Vigna
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

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

public class Int2IntArrayMapTest {
	@Test
	public void testCopyConstructor() {
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(new int [] {1, 2}, new int[] {3, 4});
		assertEquals(new Int2IntArrayMap(m), m);
		assertEquals(new HashMap<>(m), m);
	}

	@Test
	public void testValuesClear() {
		final Int2IntMap map = new Int2IntArrayMap(Int2IntMaps.singleton(42, 24));
		map.values().clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testValuesRemove() {
		final Int2IntMap map = new Int2IntArrayMap(Int2IntMaps.singleton(42, 24));
		map.values().rem(24);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testValuesRemoveAll() {
		final Int2IntMap map = new Int2IntArrayMap(Int2IntMaps.singleton(42, 24));
		map.values().removeAll(Collections.singleton(Integer.valueOf(24)));
		assertTrue(map.isEmpty());
	}

	@Test
	public void testForEachRemaining() {
		final Int2IntArrayMap s = new Int2IntArrayMap(new Int2IntLinkedOpenHashMap(new int[] { 0, 1, 2, 3,
				4 }, new int[] { 0,
				1, 2, 3, 4 }));
		for (int i = 0; i <= s.size(); i++) {
			final IntIterator iterator = s.keySet().intIterator();
			final int[] j = new int[1];
			for (j[0] = 0; j[0] < i; j[0]++) iterator.nextInt();
			iterator.forEachRemaining(x -> {
				if (x != j[0]++) throw new AssertionError();
			});
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testSkipZeroAtStart() {
		final Int2IntArrayMap s = new Int2IntArrayMap(new Int2IntLinkedOpenHashMap(new int[] { 0, 1 }, new int[] { 0,
				1 }));
		final IntIterator i = s.keySet().intIterator();
		i.skip(0);
		i.remove();
	}

	@Test
	public void testSkipOneAtStart() {
		final Int2IntArrayMap s = new Int2IntArrayMap(new Int2IntLinkedOpenHashMap(new int[] { 0, 1 }, new int[] { 0,
				1 }));
		final IntIterator i = s.keySet().intIterator();
		i.skip(1);
		i.remove();
		assertEquals(IntArraySet.ofUnchecked(1), s.keySet());
	}

	@Test
	public void testSkipBeyondEnd() {
		final Int2IntArrayMap s = new Int2IntArrayMap(new Int2IntLinkedOpenHashMap(new int[] { 0, 1 }, new int[] { 0,
				1 }));
		final IntIterator i = s.keySet().intIterator();
		i.skip(4);
		i.remove();
		assertEquals(IntArraySet.ofUnchecked(0), s.keySet());
	}
}

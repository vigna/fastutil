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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class AbstractIntCollectionTest {
	@Test
	public void testAllSpecific() {
		IntCollection m = new AbstractIntCollection() {
			final IntOpenHashSet s = new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 });

			@Override
			public IntIterator iterator() {
				return s.iterator();
			}

			@Override
			public boolean add(int k) {
				return s.add(k);
			}

			@Override
			public boolean rem(int k) {
				return s.remove(k);
			}

			@Override
			public int size() {
				return s.size();
			}
		};

		m.addAll(new IntOpenHashSet(new int[] { 1, 3 }));
		assertEquals(new IntOpenHashSet(new int[] { 0, 1, 3, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.containsAll(new IntOpenHashSet(new int[] { 1, 3, 6 })));
		assertFalse(m.containsAll(new IntOpenHashSet(new int[] { 1, 2, 6 })));
		assertTrue(m.removeAll(new IntOpenHashSet(new int[] { 1, 2, 3 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.retainAll(new IntOpenHashSet(new int[] { -1, 0, 5, 9, 11 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 5, 9 }), new IntOpenHashSet(m));
	}

	@Test
	public void testAllGeneric() {
		IntCollection m = new AbstractIntCollection() {
			final IntOpenHashSet s = new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 });

			@Override
			public IntIterator iterator() {
				return s.iterator();
			}

			@Override
			public boolean add(int k) {
				return s.add(k);
			}

			@Override
			public boolean rem(int k) {
				return s.remove(k);
			}

			@Override
			public int size() {
				return s.size();
			}
		};

		m.addAll((Collection<? extends Integer>)new IntOpenHashSet(new int[] { 1, 3 }));
		assertEquals(new IntOpenHashSet(new int[] { 0, 1, 3, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.containsAll((Collection<?>)new IntOpenHashSet(new int[] { 1, 3, 6 })));
		assertFalse(m.containsAll((Collection<?>)new IntOpenHashSet(new int[] { 1, 2, 6 })));
		assertTrue(m.removeAll((Collection<?>)new IntOpenHashSet(new int[] { 1, 2, 3 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.retainAll((Collection<?>)new IntOpenHashSet(new int[] { -1, 0, 5, 9, 11 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 5, 9 }), new IntOpenHashSet(m));
	}
}

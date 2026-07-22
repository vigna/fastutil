/*
 * Copyright (C) 2002-2026 Sebastiano Vigna
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

public class Int2IntAVLTreeMapTest {

	@SuppressWarnings("boxing")
	@Test
	public void testSubmapEntrySetRemove() {
		final Int2IntAVLTreeMap m = new Int2IntAVLTreeMap();
		m.put(1, 1);
		m.put(3, 3);
		m.put(10, 10);
		final ObjectSortedSet<Int2IntMap.Entry> entries = m.subMap(2, 5).int2IntEntrySet();

		// The key 10 exists in the backing map but is out of the submap range: removal must fail.
		assertFalse(entries.remove(new AbstractInt2IntMap.BasicEntry(10, 10)));
		assertTrue(m.containsKey(10));

		// The key 3 is in range, but the value does not match: removal must fail.
		assertFalse(entries.remove(new AbstractInt2IntMap.BasicEntry(3, 999)));
		assertTrue(m.containsKey(3));

		// The key 3 is in range and the value matches: removal must succeed.
		assertTrue(entries.remove(new AbstractInt2IntMap.BasicEntry(3, 3)));
		assertFalse(m.containsKey(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testTailMapOutOfRangeMessage() {
		final Int2IntAVLTreeMap m = new Int2IntAVLTreeMap();
		m.put(1, 1);
		m.put(5, 5);
		m.put(10, 10);
		try {
			m.tailMap(5).put(2, 2);
			fail("Expected an IllegalArgumentException");
		} catch (final IllegalArgumentException e) {
			// The upper (unbounded) end of a tail map must be shown as +, not -.
			assertTrue(e.getMessage(), e.getMessage().contains("+)"));
		}
	}
}

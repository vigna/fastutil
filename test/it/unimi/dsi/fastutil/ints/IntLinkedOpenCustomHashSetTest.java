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

package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class IntLinkedOpenCustomHashSetTest {

	@Test
	public void testGetNullKey() {
		final IntLinkedOpenCustomHashSet s = new IntLinkedOpenCustomHashSet(new IntHash.Strategy() {

			@Override
			public int hashCode(final int o) {
				return o % 10;
			}

			@Override
			public boolean equals(final int a, final int b) {
				return (a - b) % 10 == 0;
			}
		});

		s.add(3);
		s.add(10);
		s.add(0);
		assertTrue(s.contains(0));
		assertTrue(s.contains(10));
		assertTrue(s.contains(3));
		assertFalse(s.contains(1));
		IntListIterator i = s.iterator();
		assertEquals(3, i.nextInt());
		assertEquals(10, i.nextInt());
		assertFalse(i.hasNext());

		s.remove(0);
		assertFalse(s.contains(0));
		assertFalse(s.contains(10));
		s.add(10);

		i = s.iterator();
		assertEquals(3, i.nextInt());
		assertEquals(10, i.nextInt());
		assertFalse(i.hasNext());

	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntLinkedOpenCustomHashSet.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"383474");
	}
}

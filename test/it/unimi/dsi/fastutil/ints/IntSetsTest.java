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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class IntSetsTest {
	@Test
	public void testToArrayShouldNullElementAfterLastEntry() {
		final IntSet set = IntSets.EMPTY_SET;
		final Object[] values = new Object[] { "test" };
		set.toArray(values);
		assertNull(values[0]);
	}

	@Test
	public void testFromTo() {
		final IntSet s = IntSets.fromTo(0, 1000);
		assertEquals(1000, s.size());
		assertTrue(s.contains(0));
		assertTrue(s.contains(999));
		assertFalse(s.contains(-1));
		assertFalse(s.contains(1000));
		assertEquals(1000, new IntOpenHashSet(s.iterator()).size());
	}

	@Test
	public void testTo() {
		IntSet s = IntSets.to(0);
		assertEquals(Integer.MAX_VALUE, s.size());
		assertTrue(s.contains(-1));
		assertTrue(s.contains(Integer.MIN_VALUE));
		assertFalse(s.contains(0));

		s = IntSets.to(Integer.MIN_VALUE + 1000);
		final IntIterator iterator = s.iterator();
		assertEquals(Integer.MIN_VALUE, iterator.nextInt());
		for (int i = 0; i < 998; i++) iterator.nextInt();
		assertEquals(Integer.MIN_VALUE + 999, iterator.nextInt());
		assertFalse(iterator.hasNext());

		s = IntSets.to(-2);
		assertEquals(Integer.MAX_VALUE - 1, s.size());
	}

	@Test
	public void testFrom() {
		IntSet s = IntSets.from(0);
		assertEquals(Integer.MAX_VALUE, s.size());
		assertFalse(s.contains(-1));
		assertTrue(s.contains(Integer.MAX_VALUE));
		assertTrue(s.contains(0));

		s = IntSets.from(Integer.MAX_VALUE - 1000);
		final IntIterator iterator = s.iterator();
		assertEquals(Integer.MAX_VALUE - 1000, iterator.nextInt());
		for (int i = 0; i < 999; i++) iterator.nextInt();
		assertEquals(Integer.MAX_VALUE, iterator.nextInt());
		assertFalse(iterator.hasNext());
		s = IntSets.from(2);
		assertEquals(Integer.MAX_VALUE - 1, s.size());

	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntSets.class, "int", /*seed=*/"928374");
	}
}

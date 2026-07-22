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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntIteratorsTest {

	@Test
	public void testConcatSkipDoesNotStopOnEmptyComponent() {
		// After skipping exactly past a component followed by an empty one, the
		// concatenated iterator must still advance to the next non-empty component.
		final IntIterator it = IntIterators.concat(IntIterators.wrap(new int[] { 1, 2 }), IntIterators.EMPTY_ITERATOR, IntIterators.wrap(new int[] { 3 }));
		assertEquals(2, it.skip(2));
		assertTrue(it.hasNext());
		assertEquals(3, it.nextInt());
		assertFalse(it.hasNext());
	}

	@Test
	public void testIntervalIteratorSkipNoOverflow() {
		// Five elements: Integer.MAX_VALUE - 5 .. Integer.MAX_VALUE - 1.
		final IntListIterator it = IntIterators.fromTo(Integer.MAX_VALUE - 5, Integer.MAX_VALUE);
		assertEquals(5, it.skip(10));
		assertFalse(it.hasNext());
	}

	@Test
	public void testIntervalIteratorBackNoOverflow() {
		final IntListIterator it = IntIterators.fromTo(Integer.MIN_VALUE, Integer.MIN_VALUE + 5);
		// At the start there is nothing to go back to, even for a huge argument.
		assertEquals(0, it.back(10));
		assertEquals(Integer.MIN_VALUE, it.nextInt());
	}

	@Test
	public void testIntervalIteratorBackRejectsNegative() {
		final IntListIterator it = IntIterators.fromTo(0, 5);
		assertThrows(IllegalArgumentException.class, () -> it.back(-1));
	}
}

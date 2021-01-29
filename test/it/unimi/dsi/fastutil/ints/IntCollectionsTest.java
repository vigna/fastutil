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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.junit.Test;

public class IntCollectionsTest {

	@Test
	public void testIsNotEmpty() {
		final IntCollection test = IntCollections.asCollection(() -> IntSets.singleton(0).iterator());

		assertFalse(test.isEmpty());
	}

	@Test
	public void testEmpty() {
		final IntCollection test = IntCollections.asCollection(() -> IntSets.EMPTY_SET.iterator());

		assertTrue(test.isEmpty());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveIfOverloads() {
		final IntOpenHashSet s = IntOpenHashSet.of(1, 2, 3);
		s.removeIf(x -> x == 1);
		final IntPredicate p = x -> x == 1;
		s.removeIf(p);
		final it.unimi.dsi.fastutil.ints.IntPredicate q = x -> x == 1;
		s.removeIf(q);
		@SuppressWarnings("boxing")
		final Predicate<Integer> r = x -> x == 1;
		s.removeIf(r);
	}
}

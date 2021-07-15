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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import it.unimi.dsi.fastutil.Hash;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class Int2IntMapGenericOpenHashTest extends Int2IntMapGenericTest<Int2IntOpenHashMap> {
	@Parameter(2)
	public float loadFactor;

	@SuppressWarnings({ "AutoBoxing", "boxing" })
	@Parameters(name = "{index}: lf {2}")
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		final int defSize = Int2IntOpenHashMap.DEFAULT_INITIAL_SIZE;
		final Collection<Object[]> data = new ArrayList<>();
		for (final float loadFactor : new float[] { Hash.DEFAULT_LOAD_FACTOR, Hash.FAST_LOAD_FACTOR, Hash.VERY_FAST_LOAD_FACTOR }) {
			data.add(new Object[] {supplier(defSize, loadFactor), capabilities, loadFactor});
		}
		return data;
	}

	private static Supplier<Int2IntMap> supplier(final int defSize, final float loadFactor) {
		return () -> new Int2IntOpenHashMap(defSize, loadFactor);
	}

	@Test
	public void testAddTo() {
		assertEquals(0, m.addTo(0, 2));
		assertEquals(2, m.get(0));
		assertEquals(2, m.addTo(0, 3));
		assertEquals(5, m.get(0));
		m.defaultReturnValue(-1);
		assertEquals(-1, m.addTo(1, 1));
		assertEquals(0, m.get(1));
		assertEquals(0, m.addTo(1, 1));
		assertEquals(1, m.get(1));
		assertEquals(1, m.addTo(1, -2));
		assertEquals(-1, m.get(1));
	}

	@Test
	public void testComputeIfPresentPrimitiveNoBoxing() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		final IntBinaryOperator add = (key, value) -> key + value;

		assertEquals(-1, m.computeIntIfPresent(2, add));
		assertFalse(m.containsKey(2));

		assertEquals(2, m.computeIntIfPresent(1, add));
		assertEquals(2, m.get(1));
		assertEquals(3, m.computeIntIfPresent(1, add));
		assertEquals(3, m.get(1));

		assertEquals(1, m.computeIntIfPresent(1, (key, value) -> 1));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.computeIntIfPresent(1, (key, value) -> -1));
		assertFalse(m.containsKey(1));
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentPrimitiveNoBoxingNullFunction() {
		m.put(1, 1);
		m.computeIntIfPresent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentPrimitiveNoBoxingNullFunctionMissingKey() {
		m.computeIntIfPresent(1, null);
	}


	@Test
	public void testComputePrimitiveNoBoxing() {
		m.defaultReturnValue(-1);

		// Test parameters of function
		assertEquals(1, m.computeInt(1, (key, value) -> {
			assertEquals(1, key);
			assertEquals(-1, value);
			return 1;
		}));
		assertEquals(1, m.get(1));

		assertEquals(2, m.computeInt(1, (key, value) -> {
			assertEquals(1, key);
			assertEquals(1, value);
			return 2;
		}));
		assertEquals(2, m.get(1));

		assertEquals(-1, m.computeInt(1, (key, value) -> {
			assertEquals(1, key);
			assertEquals(2, value);
			return -1;
		}));
		assertFalse(m.containsKey(1));

		// Test functionality
		assertEquals(1000, m.computeInt(0, (x, y) -> x + (y != -1 ? y : 1000)));
		assertEquals(1000, m.get(0));
		assertEquals(2000, m.computeInt(0, (x, y) -> x + y * 2));
		assertEquals(2000, m.get(0));
		assertEquals(-1, m.computeInt(0, (x, y) -> -1));
		assertEquals(-1, m.get(0));

		assertEquals(1001, m.computeInt(1, (x, y) -> x + (y != -1 ? y : 1000)));
		assertEquals(1001, m.get(1));
		assertEquals(2003, m.computeInt(1, (x, y) -> x + y * 2));
		assertEquals(2003, m.get(1));
		assertEquals(-1, m.computeInt(1, (x, y) -> -1));
		assertEquals(-1, m.get(1));

		assertEquals(-1, m.computeInt(2, (x, y) -> -1));
		assertEquals(-1, m.get(2));
	}

	@Test(expected = NullPointerException.class)
	public void testComputePrimitiveNoBoxingNullFunction() {
		m.put(1, 1);
		m.computeInt(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputePrimitiveNoBoxingNullFunctionMissingKey() {
		m.computeInt(1, null);
	}


	@Test
	public void testMergePrimitiveNoBoxing() {
		m.defaultReturnValue(-1);

		assertEquals(0, m.mergeInt(1, 0, (oldVal, newVal) -> {
			fail();
			return Integer.valueOf(0);
		}));
		assertEquals(0, m.mergeInt(1, 0, (oldVal, newVal) -> {
			assertEquals(0, oldVal);
			assertEquals(0, newVal);
			return Integer.valueOf(0);
		}));
		assertEquals(0, m.get(1));
		m.clear();

		final IntBinaryOperator add =
			(oldVal, newVal) -> oldVal + newVal;

		assertEquals(0, m.mergeInt(1, 0, add));
		assertEquals(1, m.mergeInt(1, 1, add));
		assertEquals(3, m.mergeInt(1, 2, add));
		assertEquals(0, m.mergeInt(2, 0, add));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.mergeInt(1, 2, (key, value) -> -1));
		assertEquals(-1, m.mergeInt(2, 2, (key, value) -> -1));

		assertTrue(m.isEmpty());
	}


	@Test(expected = NullPointerException.class)
	public void testMergePrimitiveNoBoxingNullFunction() {
		m.put(1, 1);
		m.mergeInt(1, 1, null);
	}


	@Test(expected = NullPointerException.class)
	public void testMergePrimitiveNoBoxingNullFunctionMissingKey() {
		m.mergeInt(1, 1, null);
	}
}
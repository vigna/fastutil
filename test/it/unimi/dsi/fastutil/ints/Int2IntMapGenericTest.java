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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2IntMap.FastEntrySet;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;

@Ignore
@RunWith(Parameterized.class)
@SuppressWarnings("deprecation")
public abstract class Int2IntMapGenericTest<M extends Int2IntMap> {
	private static final Integer MINUS_ONE = Integer.valueOf(-1);
	private static final Integer ONE = Integer.valueOf(1);
	private static final Integer THREE = Integer.valueOf(3);
	private static final Integer TWO = Integer.valueOf(2);
	private static final Integer ZERO = Integer.valueOf(0);
	private static final java.util.Random r = new java.util.Random(0);

	@Parameter(1)
	public EnumSet<Capability> capabilities;
	@Parameter()
	public Supplier<M> mapSupplier;
	protected M m;


	protected void check(final Int2IntMap m, final Map<Integer, Integer> t, final IntSupplier keyProvider, final IntSupplier valueProvider, final int size) {
		/* First of all, we fill t with random data. */
		for (int i = 0; i < size; i++) {
			t.put(Integer.valueOf(keyProvider.getAsInt()), Integer.valueOf(valueProvider.getAsInt()));
		}
		/* Now we add to m the same data */
		m.putAll(t);

		checkEquality(m, t, genTestKeys(m.keySet(), size), "after insertion");

		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for (int i = 0; i < 20 * size; i++) {
			final int putKey = keyProvider.getAsInt();
			final int val = valueProvider.getAsInt();

			final Integer mPutObj = m.put(Integer.valueOf(putKey), Integer.valueOf(val));
			final Integer tPut = t.put(Integer.valueOf(putKey), Integer.valueOf(val));
			assertEquals("Error: divergence in put() between t and m", mPutObj, tPut);

			final int remKey = keyProvider.getAsInt();
			assertEquals("Error: divergence in remove() between t and m", m.remove(Integer.valueOf(remKey)), t.remove(Integer.valueOf(remKey)));
		}

		checkEquality(m, t, genTestKeys(m.keySet(), size), "after removal");
	}


	private void checkEquality(final Int2IntMap m, final Map<Integer, Integer> t, final Iterable<Integer> keys, final String description) {
		final int drv = m.defaultReturnValue();

		assertTrue("Error: !m.equals(t) " + description, m.equals(t));
		assertTrue("Error: !t.equals(m) " + description, t.equals(m));

		/* Now we check that m actually holds that data. */
		for (final Map.Entry<Integer, Integer> o2 : t.entrySet()) {
			assertTrue("Error: m and t differ on an entry (" + o2 + ") " + description + " (iterating on t)", Objects.equals(o2.getValue(), m.get(o2.getKey())));
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for (final Map.Entry<Integer, Integer> entry : m.int2IntEntrySet()) {
			assertTrue("Error: m and t differ on an entry (" + entry + ") " + description + " (iterating on m)", Objects.equals(entry.getValue(), t.get(entry.getKey())));
		}

		/* Now we check that m actually holds the same keys. */
		for (final Integer objKey : t.keySet()) {
			assertTrue("Error: m and t differ on a key (" + objKey + ") " + description + " (iterating on t)", m.containsKey(objKey));
			assertTrue("Error: m and t differ on a key (" + objKey + ", in keySet()) " + description + " (iterating on t)", m.keySet().contains(objKey));
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for (final Integer objKey : m.keySet()) {
			assertTrue("Error: m and t differ on a key " + description + " (iterating on m)", t.containsKey(objKey));
			assertTrue("Error: m and t differ on a key (in keySet()) " + description + " (iterating on m)", t.keySet().contains(objKey));
		}

		/* Now we check that m actually hold the same values. */
		for (final Integer objVal : t.values()) {
			assertTrue("Error: m and t differ on a value " + description + " (iterating on t)", m.containsValue(objVal));
			assertTrue("Error: m and t differ on a value (in values()) " + description + " (iterating on t)", m.values().contains(objVal));
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for (final Integer objVal : m.values()) {
			assertTrue("Error: m and t differ on a value " + description + " (iterating on m)", t.containsValue(objVal));
			assertTrue("Error: m and t differ on a value (in values()) " + description + " (iterating on m)", t.values().contains(objVal));
		}

		/* Now we check that inquiries about random data give the same answer in m and t. */
		for (final Integer objKey : keys) {
			assertTrue("Error: divergence in keys between t and m (polymorphic method)", m.containsKey(objKey) == t.containsKey(objKey));

			final int mVal = m.get(objKey.intValue());
			final Integer mValObj = m.get(objKey);
			final Integer tVal = t.get(objKey);
			assertEquals("Error: divergence between t and m " + description + " (polymorphic method)", tVal, mValObj);
			assertTrue("Error: divergence between polymorphic and standard method " + description, mVal == (mValObj == null ? drv : mValObj.intValue()));
			assertEquals("Error: divergence between t and m " + description + " (standard method)", tVal == null ? drv : tVal.intValue(), mVal);
		}
	}

	private IntCollection genTestKeys(final IntCollection initial, final int amount) {
		final IntCollection keys = new IntOpenHashSet(initial);
		for (int i = 0; i < amount; i++) {
			keys.add(r.nextInt());
		}
		return keys;
	}

	@Before
	public void setUp() {
		m = mapSupplier.get();
	}

	@Test
	public void test10() {
		check(m, new HashMap<>(), () -> r.nextInt(10), r::nextInt, 10);
	}

	@Test
	public void test100() {
		check(m, new HashMap<>(), () -> r.nextInt(100), r::nextInt, 100);
	}

	@Test
	public void test1000() {
		check(m, new HashMap<>(), () -> r.nextInt(1000), r::nextInt, 1000);
	}

	@Test
	public void test10000() {
		check(m, new HashMap<>(), () -> r.nextInt(10000), r::nextInt, 10000);
	}

	@Test
	public void testClone() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		assumeTrue(m instanceof Cloneable);
		final Method clone = m.getClass().getMethod("clone");

		assertEquals(m, clone.invoke(m));
		m.put(0, 1);
		assertEquals(m, clone.invoke(m));
		m.put(0, 2);
		assertEquals(m, clone.invoke(m));
		m.put(1, 2);
		assertEquals(m, clone.invoke(m));
		m.remove(1);
		assertEquals(m, clone.invoke(m));
	}

	@Test
	public void testComputeIfAbsentNullablePrimitive() {
		m.defaultReturnValue(-1);

		assertEquals(-1, m.computeIfAbsentNullable(1, key -> null));
		assertEquals(-1, m.computeIfAbsentNullable(2, key -> null));

		m.put(1, 1);
		assertEquals(1, m.computeIfAbsentNullable(1, key -> null));
		assertEquals(-1, m.computeIfAbsentNullable(2, key -> null));

		assertEquals(1, m.computeIfAbsentNullable(1, Integer::valueOf));
		assertEquals(2, m.computeIfAbsentNullable(2, Integer::valueOf));
		assertEquals(2, m.computeIfAbsentNullable(2, key -> null));
		assertEquals(2, m.get(2));
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentNullablePrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfAbsentNullable(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentNullablePrimitiveNullFunctionMissingKey() {
		m.computeIfAbsentNullable(1, null);
	}


	@Test
	public void testComputeIfAbsentObject() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(ONE, m.computeIfAbsent(ONE, key -> Integer.valueOf(key.intValue() - 1)));

		assertEquals(ONE, m.computeIfAbsent(TWO, key -> Integer.valueOf(key.intValue() - 1)));
		assertEquals(ONE, m.computeIfAbsent(TWO, key -> Integer.valueOf(key.intValue() - 2)));
		assertEquals(ONE, m.get(TWO));

		assertEquals(2, m.size());
		m.clear();

		assertNull(m.computeIfAbsent(ONE, key -> null));
		assertNull(m.computeIfAbsent(TWO, key -> null));
		assertTrue(m.isEmpty());

		m.put(ONE, ONE);
		assertEquals(ONE, m.computeIfAbsent(ONE, key -> null));
		assertNull(m.computeIfAbsent(TWO, key -> null));

		assertEquals(ONE, m.computeIfAbsent(ONE, key -> key));
		assertEquals(TWO, m.computeIfAbsent(TWO, key -> key));
		assertEquals(TWO, m.computeIfAbsent(TWO, key -> null));
		assertEquals(TWO, m.get(TWO));
		assertNull(m.computeIfAbsent(null, key -> key));
	}


	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentObjectNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(ONE, null);
	}


	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentObjectNullFunctionMissingKey() {
		m.computeIfAbsent(ONE, null);
	}

	@Test
	public void testComputeIfAbsentPartialPrimitive() {
		m.defaultReturnValue(-1);

		final Int2IntFunction f = new Int2IntArrayMap();
		f.defaultReturnValue(1);
		f.put(1, 1);

		assertEquals(1, m.computeIfAbsent(1, f));
		assertEquals(1, m.get(1));

		assertEquals(-1, m.computeIfAbsent(2, f));
		assertFalse(m.containsKey(2));

		f.put(2, 2);
		assertEquals(2, m.computeIfAbsent(2, f));
		assertTrue(m.containsKey(2));
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPartialPrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPartialPrimitiveNullFunctionMissingKey() {
		m.computeIfAbsent(1, null);
	}

	@Test
	public void testComputeIfAbsentPrimitive() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(1, m.computeIfAbsent(1, key -> key - 1));

		assertEquals(1, m.computeIfAbsent(2, key -> key - 1));
		assertEquals(1, m.computeIfAbsent(2, key -> key - 2));
		assertEquals(1, m.get(2));

		assertEquals(2, m.size());
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPrimitiveNullFunctionMissingKey() {
		m.computeIfAbsent(1, null);
	}


	@Test
	public void testComputeIfPresentObject() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		final BiFunction<Integer, Integer, Integer> add = (key, value) -> Integer.valueOf(key.intValue() + value.intValue());

		assertNull(m.computeIfPresent(TWO, add));
		assertNull(m.computeIfPresent(TWO, (key, value) -> null));
		assertFalse(m.containsKey(TWO));

		assertEquals(TWO, m.computeIfPresent(ONE, add));
		assertEquals(TWO, m.get(ONE));
		assertEquals(THREE, m.computeIfPresent(ONE, add));
		assertEquals(THREE, m.get(ONE));

		assertEquals(MINUS_ONE, m.computeIfPresent(ONE, (key, value) -> MINUS_ONE));
		assertTrue(m.containsKey(ONE));

		assertNull(m.computeIfPresent(ONE, (key, value) -> null));
		assertFalse(m.containsKey(ONE));
		assertNull(m.computeIfPresent(null, (key, value) -> key));
	}


	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentObjectNullFunction() {
		m.put(1, 1);
		m.computeIfPresent(ONE, null);
	}


	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentObjectNullFunctionMissingKey() {
		m.computeIfPresent(ONE, null);
	}

	@Test
	public void testComputeIfPresentPrimitive() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		final BiFunction<Integer, Integer, Integer> add = (key, value) -> Integer.valueOf(key.intValue() + value.intValue());

		assertEquals(-1, m.computeIfPresent(2, add));
		assertEquals(-1, m.computeIfPresent(2, (key, value) -> null));
		assertFalse(m.containsKey(2));

		assertEquals(2, m.computeIfPresent(1, add));
		assertEquals(2, m.get(1));
		assertEquals(3, m.computeIfPresent(1, add));
		assertEquals(3, m.get(1));

		assertEquals(-1, m.computeIfPresent(1, (key, value) -> Integer.valueOf(-1)));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.computeIfPresent(1, (key, value) -> null));
		assertFalse(m.containsKey(1));
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentPrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfPresent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentPrimitiveNullFunctionMissingKey() {
		m.computeIfPresent(1, null);
	}


	@Test
	public void testComputeObject() {
		m.defaultReturnValue(-1);

		// Test parameters of function
		assertEquals(ONE, m.compute(ONE, (key, value) -> {
			assertEquals(ONE, key);
			assertNull(value);
			return ONE;
		}));
		assertEquals(ONE, m.get(ONE));

		assertEquals(TWO, m.compute(ONE, (key, value) -> {
			assertEquals(ONE, key);
			assertEquals(ONE, value);
			return TWO;
		}));
		assertEquals(TWO, m.get(ONE));

		assertNull(m.compute(ONE, (key, value) -> {
			assertEquals(ONE, key);
			assertEquals(TWO, value);
			return null;
		}));
		assertFalse(m.containsKey(ONE));

		// Test functionality
		assertEquals(Integer.valueOf(1000), m.compute(ZERO, (x, y) -> Integer.valueOf(x.intValue() + (y != null ? y.intValue() : 1000))));
		assertEquals(Integer.valueOf(1000), m.get(ZERO));
		assertEquals(Integer.valueOf(2000), m.compute(ZERO, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(Integer.valueOf(2000), m.get(ZERO));
		assertNull(m.compute(ZERO, (x, y) -> null));
		assertFalse(m.containsKey(0));

		assertEquals(Integer.valueOf(1001), m.compute(ONE, (x, y) -> Integer.valueOf(x.intValue() + (y != null ? y.intValue() : 1000))));
		assertEquals(Integer.valueOf(1001), m.get(ONE));
		assertEquals(Integer.valueOf(2003), m.compute(ONE, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(Integer.valueOf(2003), m.get(ONE));
		assertNull(m.compute(ONE, (x, y) -> null));
		assertFalse(m.containsKey(1));

		assertNull(m.compute(TWO, (x, y) -> null));
		assertFalse(m.containsKey(2));
	}


	@Test(expected = NullPointerException.class)
	public void testComputeObjectNullFunction() {
		m.put(1, 1);
		m.compute(ONE, null);
	}


	@Test(expected = NullPointerException.class)
	public void testComputeObjectNullFunctionMissingKey() {
		m.compute(ONE, null);
	}

	@Test
	public void testComputePrimitive() {
		m.defaultReturnValue(-1);

		// Test parameters of function
		assertEquals(1, m.compute(1, (key, value) -> {
			assertEquals(1, key.intValue());
			assertNull(value);
			return Integer.valueOf(1);
		}));
		assertEquals(1, m.get(1));

		assertEquals(2, m.compute(1, (key, value) -> {
			assertEquals(1, key.intValue());
			assertEquals(1, value.intValue());
			return Integer.valueOf(2);
		}));
		assertEquals(2, m.get(1));

		assertEquals(-1, m.compute(1, (key, value) -> {
			assertEquals(1, key.intValue());
			assertEquals(2, value.intValue());
			return null;
		}));
		assertFalse(m.containsKey(1));

		// Test functionality
		assertEquals(1000, m.compute(0, (x, y) -> Integer.valueOf(x.intValue() + (y != null ? y.intValue() : 1000))));
		assertEquals(1000, m.get(0));
		assertEquals(2000, m.compute(0, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2000, m.get(0));
		assertEquals(-1, m.compute(0, (x, y) -> null));
		assertEquals(-1, m.get(0));

		assertEquals(1001, m.compute(1, (x, y) -> Integer.valueOf(x.intValue() + (y != null ? y.intValue() : 1000))));
		assertEquals(1001, m.get(1));
		assertEquals(2003, m.compute(1, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2003, m.get(1));
		assertEquals(-1, m.compute(1, (x, y) -> null));
		assertEquals(-1, m.get(1));

		assertEquals(-1, m.compute(2, (x, y) -> null));
		assertEquals(-1, m.get(2));
	}

	@Test(expected = NullPointerException.class)
	public void testComputePrimitiveNullFunction() {
		m.put(1, 1);
		m.compute(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputePrimitiveNullFunctionMissingKey() {
		m.compute(1, null);
	}


	@Test
	public void testContainsNull() {
		assertFalse(m.containsKey(null));
		assertFalse(m.containsValue(null));

		m.put(0, 0);
		assertFalse(m.containsKey(null));
		assertFalse(m.containsValue(null));

		assertNull(m.get(null));
	}

	@Test
	public void testEntrySet() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(0, 0)));
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(i, -1)));
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(i, i)));
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i, -1)));
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i, i)));
		}
		assertTrue(m.int2IntEntrySet().isEmpty());
	}

	@SuppressWarnings({ "SuspiciousMethodCalls", "unlikely-arg-type" })
	@Test
	public void testEntrySetContains() {
		m.put(0, 0);
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry<>(new Object(), null)));
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry<>(null, new Object())));
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry<>(null, null)));
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry<>(new Object(), new Object())));
	}

	@Test
	public void testEntrySetIteration() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s = new HashSet<>(m.int2IntEntrySet());
		assertEquals(m.int2IntEntrySet(), s);
		// Test symmetry of equals
		assertEquals(s, m.int2IntEntrySet());
	}

	@Test
	public void testEntrySetFastForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s = new HashSet<>();
		final Set<Entry> sIdentity = Collections.newSetFromMap(new IdentityHashMap<Entry, Boolean>());
		Int2IntMaps.fastForEach(m, x -> {
			s.add(new AbstractInt2IntMap.BasicEntry(x.getIntKey(), x.getIntValue()));
			sIdentity.add(x);
		});
		assertEquals(m.int2IntEntrySet(), s);
		if (m.int2IntEntrySet() instanceof FastEntrySet) {
			// Same instance for each iteration
			assertEquals(1, sIdentity.size());
		} else {
		    // Different instance for each iteration
		    assertEquals(m.size(), sIdentity.size());
		}
	}

	@Test
	public void testEntrySetForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s = new HashSet<>();
		//noinspection UseBulkOperation
		m.int2IntEntrySet().forEach(s::add);
		assertEquals(m.int2IntEntrySet(), s);
	}


	@Test
	public void testIteratorEntrySetFastForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s = new HashSet<>();
		final Set<Entry> sIdentity = Collections.newSetFromMap(new IdentityHashMap<Entry, Boolean>());
		
		Int2IntMaps.fastIterator(m).forEachRemaining(x -> {
			s.add(new AbstractInt2IntMap.BasicEntry(x.getIntKey(), x.getIntValue()));
			sIdentity.add(x);
		});
		assertEquals(m.int2IntEntrySet(), s);
		if (m.int2IntEntrySet() instanceof FastEntrySet) {
			// Same instance for each iteration
			assertEquals(1, sIdentity.size());
		} else {
		    // Different instance for each iteration
		    assertEquals(m.size(), sIdentity.size());
		}
	}

	@Test
	public void testIteratorEntrySetForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s = new HashSet<>();
		//noinspection UseBulkOperation
		m.int2IntEntrySet().iterator().forEachRemaining(s::add);
		assertEquals(m.int2IntEntrySet(), s);
	}

	@Test
	public void testEntrySetSpliterator() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s =
				m.int2IntEntrySet().stream().collect(Collectors.toSet());
		assertEquals(m.int2IntEntrySet(), s);
	}

	@Test
	public void testEntrySetIteratorRemove() {
		m.defaultReturnValue(-1);

		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
    	Iterator<Entry> i = m.int2IntEntrySet().iterator();
		int key;
		int value;
		// This will be an invalid entry after the removal, so we can't trust it long term.
		// Enforce this with scoping; get the contents and then not use it again.
		{
			Entry toRemoveEntry = i.next();
			key = toRemoveEntry.getIntKey();
			value = toRemoveEntry.getIntValue();
		}
    	assertTrue(m.containsKey(key));
    	assertTrue(m.containsValue(value));
    	assertEquals(m.get(key), value);
    	i.remove();
    	assertFalse(m.containsKey(key));
    	assertFalse(m.containsValue(value));
    	assertEquals(m.get(key), -1);
    	assertEquals(99, m.size()); 
	}

	@Test(expected = IllegalStateException.class)
	public void testEntrySetEmptyIteratorRemove() {
		final ObjectIterator<Int2IntMap.Entry> iterator = m.int2IntEntrySet().iterator();
		assertFalse(iterator.hasNext());
		iterator.remove();
	}

	@Test(expected = IllegalStateException.class)
	public void testEntrySetIteratorTwoRemoves() {
		m.put(0, 0);
		m.put(1, 1);
		final ObjectIterator<Int2IntMap.Entry> iterator = m.int2IntEntrySet().iterator();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}

	@SuppressWarnings({ "SuspiciousMethodCalls", "unlikely-arg-type" })
	@Test
	public void testEntrySetRemove() {
		m.put(0, 0);
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry<>(new Object(), null)));
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry<>(null, new Object())));
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry<>(null, null)));
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry<>(new Object(), new Object())));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals() {
		m.put(1, 1);
		assertFalse(m.equals(new Object2ObjectOpenHashMap<>(new Integer[] {ONE, null}, new Integer[] {ONE, null})));
		assertTrue(m.equals(new Object2ObjectOpenHashMap<>(new Integer[] {ONE}, new Integer[] {ONE})));
	}

	@Test(expected = IllegalStateException.class)
	public void testFastEntrySetEmptyIteratorRemove() {
		final ObjectSet<Entry> entries = m.int2IntEntrySet();
		assumeTrue(entries instanceof Int2IntMap.FastEntrySet);
		final ObjectIterator<Int2IntMap.Entry> iterator = ((Int2IntMap.FastEntrySet) entries).fastIterator();
		assertFalse(iterator.hasNext());
		iterator.remove();
	}

	@Test(expected = IllegalStateException.class)
	public void testFastEntrySetIteratorTwoRemoves() {
		m.put(0, 0);
		m.put(1, 1);
		final ObjectSet<Entry> entries = m.int2IntEntrySet();
		assumeTrue(entries instanceof Int2IntMap.FastEntrySet);
		final ObjectIterator<Int2IntMap.Entry> iterator = ((Int2IntMap.FastEntrySet) entries).fastIterator();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}

	@Test
	public void testFastIterator() {
		assumeTrue(m.int2IntEntrySet() instanceof FastEntrySet);
		assumeTrue(capabilities.contains(Capability.ITERATOR_MODIFY));

		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		final ObjectIterator<Entry> fastIterator = Int2IntMaps.fastIterator(m);
		final Entry entry = fastIterator.next();
		final int key = entry.getIntKey();
		entry.setValue(-1000);
		assertEquals(m.get(key), -1000);
		fastIterator.remove();
		assertEquals(m.get(key), -1);
	}

	@Test
	public void testGetOrDefaultObject() {
		m.put(1, 1);

		assertEquals(ZERO, m.getOrDefault(ZERO, ZERO));
		assertEquals(ONE, m.getOrDefault(ZERO, ONE));
		assertEquals(ONE, m.getOrDefault(ONE, TWO));

		m.put(0, 1);
		assertEquals(ONE, m.getOrDefault(ZERO, ZERO));
		assertEquals(ONE, m.getOrDefault(ONE, ZERO));

		m.put(1, 1);
		assertEquals(ONE, m.getOrDefault(ONE, TWO));

		assertEquals(THREE, m.getOrDefault(null, THREE));
		assertEquals(THREE, m.getOrDefault(THREE, THREE));
		assertNull(m.getOrDefault(THREE, null));
		assertNull(m.getOrDefault(null, null));
	}


	@Test(expected = ClassCastException.class)
	public void testGetOrDefaultObjectInvalidKey() {
		m.getOrDefault(Long.valueOf(1), ONE);
	}

	@Test
	public void testGetOrDefaultPrimitive() {
		m.put(1, 1);

		assertEquals(0, m.getOrDefault(0, 0));
		assertEquals(1, m.getOrDefault(0, 1));
		assertEquals(1, m.getOrDefault(1, 2));

		m.put(0, 1);
		assertEquals(1, m.getOrDefault(0, 0));
		assertEquals(1, m.getOrDefault(1, 0));

		m.put(1, 1);
		assertEquals(1, m.getOrDefault(1, 2));
	}

	
	@Test
	public void testKeySetIteration() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet(m.keySet());
		assertEquals(m.keySet(), s);
		// Test symmetry of equals
		assertEquals(s, m.keySet());
	}

	@Test
	public void testKeySetForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		//noinspection UseBulkOperation
		m.keySet().forEach(s::add);
		assertEquals(m.keySet(), s);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testKeySetForEachObject() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		final Set<Integer> sObject = new HashSet<>();
		//noinspection UseBulkOperation
		m.keySet().forEach((Consumer<Integer>) s::add);
		//noinspection UseBulkOperation
		m.keySet().forEach(sObject::add);
		assertEquals(m.keySet(), s);
		assertEquals(m.keySet(), sObject);
	}

	@Test
	public void testKeySetIteratorForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		//noinspection UseBulkOperation
		m.keySet().forEach(s::add);
		assertEquals(m.keySet(), s);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testKeySetIteratorForEachObject() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		final Set<Integer> sObject = new HashSet<>();
		//noinspection UseBulkOperation
		m.keySet().iterator().forEachRemaining((Consumer<Integer>) s::add);
		//noinspection UseBulkOperation
		m.keySet().iterator().forEachRemaining(sObject::add);
		assertEquals(m.keySet(), s);
		assertEquals(m.keySet(), sObject);
	}

	@Test
	public void testKeySetIteratorRemove() {
		assumeTrue(capabilities.contains(Capability.ITERATOR_MODIFY));

		m.defaultReturnValue(-1);

		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		IntIterator i = m.keySet().iterator();
		int toRemoveKey = i.nextInt();
		
		// Taking advantage of the fact we mapped keys to values
		assertTrue(m.containsKey(toRemoveKey));
		assertEquals(m.get(toRemoveKey), toRemoveKey);
		i.remove();
		assertFalse(m.containsKey(toRemoveKey));
		assertEquals(m.get(toRemoveKey), -1);
		assertEquals(99, m.size());
	}

	@Test
	public void testMap() {
		assertEquals(0, m.put(1, 1));
		assertEquals(1, m.size());
		assertTrue(m.containsKey(1));
		assertTrue(m.containsValue(1));

		assertEquals(0, m.put(2, 2));
		assertTrue(m.containsKey(2));
		assertTrue(m.containsValue(2));
		assertEquals(2, m.size());

		assertEquals(1, m.put(1, 3));
		assertTrue(m.containsValue(3));
		assertEquals(0, m.remove(3));
		assertEquals(0, m.put(3, 3));
		assertTrue(m.containsKey(3));
		assertTrue(m.containsValue(3));
		assertEquals(3, m.size());

		assertEquals(3, m.get(1));
		assertEquals(2, m.get(2));
		assertEquals(3, m.get(3));

		assertEquals(new IntOpenHashSet(new int[] {1, 2, 3}), new IntOpenHashSet(m.keySet().iterator()));
		assertEquals(new IntOpenHashSet(new int[] {3, 2, 3}), new IntOpenHashSet(m.values().iterator()));

		for (final Map.Entry<Integer, Integer> entry : m.int2IntEntrySet()) {
			assertEquals(entry.getValue(), m.get(entry.getKey()));
		}

		assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(1, 3)));
		assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(2, 2)));
		assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(3, 3)));
		assertFalse(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(1, 2)));
		assertFalse(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(2, 1)));

		assertEquals(3, m.remove(3));
		assertEquals(2, m.size());
		assertEquals(3, m.remove(1));
		assertEquals(1, m.size());
		assertFalse(m.containsKey(1));
		assertEquals(2, m.remove(2));
		assertEquals(0, m.size());
		assertFalse(m.containsKey(1));
	}

	@Test
	public void testMerge() {
		m.defaultReturnValue(-1);
		assertEquals(0, m.merge(0, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(0, m.get(0));
		assertEquals(1000, m.merge(0, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(1000, m.get(0));
		assertEquals(2000, m.merge(0, 500, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2000, m.get(0));
		assertEquals(-1, m.merge(0, 0, (x, y) -> null));
		assertEquals(-1, m.get(0));

		assertEquals(0, m.merge(1, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(0, m.get(1));
		assertEquals(1000, m.merge(1, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(1000, m.get(1));
		assertEquals(2000, m.merge(1, 500, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2000, m.get(1));
		assertEquals(-1, m.merge(1, 0, (x, y) -> null));
		assertEquals(-1, m.get(1));
	}

	@Test
	public void testMergePrimitive() {
		m.defaultReturnValue(-1);
		assertEquals(0, m.mergeInt(0, 0, (x, y) -> 1000));
		assertEquals(0, m.get(0));
		assertEquals(1000, m.mergeInt(0, 0, (x, y) -> 1000));
		assertEquals(1000, m.get(0));
		assertEquals(2000, m.mergeInt(0, 500, (x, y) -> (x + y * 2)));
		assertEquals(2000, m.get(0));

		assertEquals(0, m.mergeInt(1, 0, (x, y) -> 1000));
		assertEquals(0, m.get(1));
		assertEquals(1000, m.mergeInt(1, 0, (x, y) -> 1000));
		assertEquals(1000, m.get(1));
		assertEquals(2000, m.mergeInt(1, 500, (x, y) -> (x + y * 2)));
		assertEquals(2000, m.get(1));
	}

	@Test
	public void testMergeObject() {
		m.defaultReturnValue(-1);

		assertEquals(ZERO, m.merge(ONE, ZERO, (oldVal, newVal) -> {
			assertNull(oldVal);
			assertEquals(ZERO, newVal);
			return ZERO;
		}));
		assertEquals(ZERO, m.get(ONE));
		m.clear();

		final BiFunction<Integer, Integer, Integer> add = (oldVal, newVal) -> Integer.valueOf(oldVal.intValue() + newVal.intValue());

		assertEquals(ZERO, m.merge(ONE, ZERO, add));
		assertEquals(ONE, m.merge(ONE, ONE, add));
		assertEquals(THREE, m.merge(ONE, TWO, add));
		assertEquals(ZERO, m.merge(TWO, ZERO, add));
		assertTrue(m.containsKey(ONE));

		assertNull(m.merge(ONE, TWO, (key, value) -> null));
		assertNull(m.merge(TWO, TWO, (key, value) -> null));

		assertTrue(m.isEmpty());
	}


	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullFunction() {
		m.put(1, 1);
		m.merge(ONE, ONE, null);
	}


	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullFunctionMissingKey() {
		m.merge(ONE, ONE, null);
	}


	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullKey() {
		m.merge(null, ONE, (key, vale) -> ONE);
	}


	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullValue() {
		m.put(1, 1);
		m.merge(ONE, null, (key, vale) -> ONE);
	}


	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullValueMissingKey() {
		m.merge(ONE, null, (key, vale) -> ONE);
	}

	@Test
	public void testMergeDefaultReturnValue() {
		m.defaultReturnValue(-1);

		assertEquals(0, m.merge(1, 0, (oldVal, newVal) -> {
			assertNull(oldVal);
			assertEquals(0, newVal.intValue());
			return Integer.valueOf(0);
		}));
		assertEquals(0, m.get(1));
		m.clear();

		final BiFunction<Integer, Integer, Integer> add = (oldVal, newVal) -> Integer.valueOf(oldVal.intValue() + newVal.intValue());

		assertEquals(0, m.merge(1, 0, add));
		assertEquals(1, m.merge(1, 1, add));
		assertEquals(3, m.merge(1, 2, add));
		assertEquals(0, m.merge(2, 0, add));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.merge(1, 2, (key, value) -> null));
		assertEquals(-1, m.merge(2, 2, (key, value) -> null));

		assertTrue(m.isEmpty());
	}

	@Test
	public void testMergeDefaultReturnValuePrimitive() {
		m.defaultReturnValue(-1);

		assertEquals(0, m.mergeInt(1, 0, (oldVal, newVal) -> {
			assertEquals(-1, oldVal);
			assertEquals(0, newVal);
			return 0;
		}));
		assertEquals(0, m.get(1));
		m.clear();

		final IntBinaryOperator add = (oldVal, newVal) -> oldVal + newVal;

		assertEquals(0, m.mergeInt(1, 0, add));
		assertEquals(1, m.mergeInt(1, 1, add));
		assertEquals(3, m.mergeInt(1, 2, add));
		assertEquals(0, m.mergeInt(2, 0, add));
		assertTrue(m.containsKey(1));
	}

	@Test(expected = NullPointerException.class)
	public void testMergePrimitiveNullFunction() {
		m.put(1, 1);
		m.merge(1, 1, null);
	}


	@Test(expected = NullPointerException.class)
	public void testMergePrimitiveNullFunctionMissingKey() {
		m.merge(1, 1, null);
	}

	@Test
	public void testPutAndGetPrimitive() {
		m.defaultReturnValue(-1);
		assertEquals(-1, m.get(1));
		m.put(1, 1);
		assertEquals(1, m.get(1));
		m.defaultReturnValue(1);
		assertTrue(m.containsKey(1));
		assertEquals(1, m.get(1));
	}


	@Test
	public void testPutIfAbsentObject() {
		m.defaultReturnValue(-1);

		m.put(1, 1);
		assertEquals(ONE, m.putIfAbsent(ONE, TWO));
		assertEquals(ONE, m.putIfAbsent(ONE, null));

		assertNull(m.putIfAbsent(TWO, TWO));
		assertEquals(TWO, m.get(TWO));

		assertEquals(TWO, m.putIfAbsent(TWO, THREE));
		assertEquals(TWO, m.get(TWO));
	}


	@Test(expected = NullPointerException.class)
	public void testPutIfAbsentObjectNullValueMissingKey() {
		m.putIfAbsent(ONE, null);
	}

	@Test
	public void testPutIfAbsentPrimitive() {
		m.defaultReturnValue(-1);

		m.put(1, 1);
		assertEquals(1, m.putIfAbsent(1, 2));

		assertEquals(-1, m.putIfAbsent(2, 2));
		assertEquals(2, m.get(2));

		assertEquals(2, m.putIfAbsent(2, 3));
		assertEquals(2, m.get(2));
	}

	@Test
	public void testRemove() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.remove(100 + i));
		}
		for (int i = 50; i < 150; i++) {
			assertEquals(i % 100, m.remove(i % 100));
			assertEquals(m.size(), m.int2IntEntrySet().size());
		}
		assertTrue(m.isEmpty());
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i + 1, i)));
			assertFalse(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i, i + 1)));
			assertTrue(m.containsKey(i));
			assertTrue(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i, i)));
			assertFalse(m.containsKey(i));
		}
	}


	@Test
	public void testRemoveObject() {
		m.defaultReturnValue(-1);
		m.put(1, 1);
		assertTrue(m.containsKey(ONE));

		assertFalse(m.remove(TWO, ONE));
		assertFalse(m.remove(TWO, TWO));
		assertFalse(m.remove(ONE, TWO));

		assertTrue(m.containsKey(ONE));

		assertTrue(m.remove(ONE, ONE));
		assertFalse(m.containsKey(ONE));

		assertFalse(m.remove(ONE, ONE));
		assertFalse(m.remove(ONE, MINUS_ONE));

		assertNull(m.get(ONE));
		assertFalse(m.containsKey(ONE));
	}


	@Test(expected = ClassCastException.class)
	public void testRemoveObjectInvalidKey() {
		m.put(1, 1);
		m.remove(Long.valueOf(1), ONE);
	}


	@Test
	public void testRemoveObjectInvalidValue() {
		m.put(1, 1);
		assertFalse(m.remove(ONE, Long.valueOf(1)));
	}


	@Test
	public void testRemoveObjectInvalidValueMissingKey() {
		assertFalse(m.remove(ONE, Long.valueOf(1)));
	}

	@Test
	public void testRemovePrimitive() {
		m.defaultReturnValue(-1);

		m.put(1, 1);
		assertTrue(m.containsKey(1));

		assertFalse(m.remove(2, 1));
		assertFalse(m.remove(2, 2));
		assertFalse(m.remove(1, 2));

		assertTrue(m.containsKey(1));

		assertTrue(m.remove(1, 1));
		assertFalse(m.containsKey(1));

		assertFalse(m.remove(1, 1));
		assertFalse(m.remove(1, -1));

		assertEquals(-1, m.get(1));
		assertFalse(m.containsKey(1));
	}

	@Test
	public void testRemoveWithValue() {
		m.defaultReturnValue(-1);

		assertFalse(m.remove(0, 0));
		m.put(0, 1);
		assertFalse(m.remove(0, 0));
		assertTrue(m.containsKey(0));
		m.put(0, 0);
		assertTrue(m.remove(0, 0));
		assertFalse(m.containsKey(0));

		assertFalse(m.remove(1, 0));
		m.put(1, 1);
		assertFalse(m.remove(1, 0));
		assertTrue(m.containsKey(1));
		m.put(1, 0);
		assertTrue(m.remove(1, 0));
		assertFalse(m.containsKey(1));
	}

	@Test
	public void testRemoveZero() {
		m.defaultReturnValue(-1);
		for (int i = -1; i <= 1; i++) {
			assertEquals(-1, m.put(i, i));
		}
		assertEquals(0, m.remove(0));
		final IntIterator iterator = m.keySet().iterator();
		final IntOpenHashSet z = new IntOpenHashSet();
		z.add(iterator.nextInt());
		z.add(iterator.nextInt());
		assertFalse(iterator.hasNext());
		assertEquals(new IntOpenHashSet(new int[] {-1, 1}), z);
	}

	@Test
	public void testRemoveZeroKeySet() {
		assumeTrue(capabilities.contains(Capability.KEY_SET_MODIFY));

		m.defaultReturnValue(-1);
		for (int i = -1; i <= 1; i++) {
			assertEquals(-1, m.put(i, i));
		}
		IntIterator iterator = m.keySet().iterator();
		boolean removed = false;
		while (iterator.hasNext()) {
			if (iterator.nextInt() == 0) {
				assertFalse(removed);
				iterator.remove();
				removed = true;
			}
		}
		assertTrue(removed);

		assertFalse(m.containsKey(0));
		assertEquals(-1, m.get(0));

		assertEquals(2, m.size());
		assertEquals(2, m.keySet().size());
		iterator = m.keySet().iterator();
		final int[] content = new int[2];
		content[0] = iterator.nextInt();
		content[1] = iterator.nextInt();
		assertFalse(iterator.hasNext());
		Arrays.sort(content);
		assertArrayEquals(new int[] {-1, 1}, content);
	}


	@Test
	public void testReplaceBinaryObject() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertNull(m.replace(TWO, ONE));
		assertNull(m.replace(TWO, TWO));
		assertFalse(m.containsKey(TWO));

		assertEquals(ONE, m.replace(ONE, TWO));
		assertEquals(TWO, m.replace(ONE, TWO));
		assertEquals(TWO, m.replace(ONE, ONE));
		assertEquals(ONE, m.get(ONE));
		assertTrue(m.containsKey(ONE));
		assertNull(m.replace(null, ONE));
	}


	@Test(expected = NullPointerException.class)
	public void testReplaceBinaryObjectNullValue() {
		m.put(1, 1);
		m.replace(ONE, null);
	}


	@Test
	public void testReplaceBinaryObjectNullValueMissingKey() {
		assertNull(m.replace(ONE, null));
	}

	@Test
	public void testReplaceBinaryPrimitive() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(-1, m.replace(2, 1));
		assertEquals(-1, m.replace(2, 2));
		assertFalse(m.containsKey(2));

		assertEquals(1, m.replace(1, 2));
		assertEquals(2, m.replace(1, 2));
		assertEquals(2, m.replace(1, 1));
		assertEquals(1, m.get(1));
		assertTrue(m.containsKey(1));
	}


	@Test
	public void testReplaceTernaryObject() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertFalse(m.replace(TWO, ONE, ONE));
		assertFalse(m.replace(TWO, TWO, ONE));
		assertFalse(m.replace(ONE, TWO, ONE));
		assertEquals(ONE, m.get(ONE));

		assertTrue(m.replace(ONE, ONE, ONE));
		assertTrue(m.replace(ONE, ONE, TWO));
		assertFalse(m.replace(ONE, ONE, TWO));

		assertTrue(m.replace(ONE, TWO, MINUS_ONE));
		assertEquals(MINUS_ONE, m.get(ONE));
		assertTrue(m.containsKey(ONE));
		assertFalse(m.replace(ONE, ONE, null));
		assertFalse(m.replace(ONE, null, ONE));
		assertFalse(m.replace(null, ONE, ONE));
	}


	@Test(expected = NullPointerException.class)
	public void testReplaceTernaryObjectNullNewValue() {
		m.put(1, 1);
		m.replace(ONE, ONE, null);
	}


	@Test
	public void testReplaceTernaryPrimitive() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertFalse(m.replace(2, 1, 1));
		assertFalse(m.replace(2, 2, 1));
		assertFalse(m.replace(1, 2, 1));
		assertEquals(1, m.get(1));

		assertTrue(m.replace(1, 1, 1));
		assertTrue(m.replace(1, 1, 2));
		assertFalse(m.replace(1, 1, 2));

		assertTrue(m.replace(1, 2, -1));
		assertEquals(-1, m.get(1));
		assertTrue(m.containsKey(1));
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		assumeTrue(m instanceof Serializable);

		final ByteArrayOutputStream store = new ByteArrayOutputStream();
		try (ObjectOutput oos = new ObjectOutputStream(store)) {
			oos.writeObject(m);
		}
		assertEquals(m, BinIO.loadObject(new ByteArrayInputStream(store.toByteArray())));

		m.put(0, 1);
		m.put(1, 2);

		store.reset();
		try (ObjectOutput oos = new ObjectOutputStream(store)) {
			oos.writeObject(m);
		}
		assertEquals(m, BinIO.loadObject(new ByteArrayInputStream(store.toByteArray())));
	}

	@Test
	public void testSizeAndIsEmpty() {
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());
		for (int i = 0; i < 100; i++) {
			assertEquals(i, m.size());
			assertEquals(m.defaultReturnValue(), m.put(i, i));
			assertFalse(m.isEmpty());
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(100, m.size());
			assertEquals(i, m.put(i, i));
		}
		for (int i = 99; i >= 0; i--) {
			assertEquals(i, m.remove(i));
			assertEquals(i, m.size());
		}
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		m.clear();
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());
	}
	
	// No testValuesIteration; it would basically turn into a test whether
	// new IntOpenHashSet(m.values()).equals(new IntOpenHashSet(m.values()))
	// which is not very instructive.

	@Test
	public void testValuesForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		//noinspection UseBulkOperation
		m.values().forEach(s::add);
		// values() is a Collection without an equals, so need to convert to Set first.
		assertEquals(new IntOpenHashSet(m.values()), s);
		// Test symmetry of equals
	}

	@Test
	public void testValuesForEachObject() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		final Set<Integer> sObject = new HashSet<>();
		//noinspection UseBulkOperation
		m.values().forEach((Consumer<Integer>) s::add);
		//noinspection UseBulkOperation
		m.values().forEach(sObject::add);
		// values() is a Collection without an equals, so need to convert to Set first.
		assertEquals(new IntOpenHashSet(m.values()), s);
		assertEquals(new IntOpenHashSet(m.values()), sObject);
	}

	@Test
	public void testValuesIteratorForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		//noinspection UseBulkOperation
		m.values().iterator().forEachRemaining(s::add);
		// values() is a Collection without an equals, so need to convert to Set first.
		assertEquals(new IntOpenHashSet(m.values()), s);
	}

	@Test
	public void testValuesIteratorForEachObject() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = new IntOpenHashSet();
		final Set<Integer> sObject = new HashSet<>();
		//noinspection UseBulkOperation
		m.values().iterator().forEachRemaining((Consumer<Integer>) s::add);
		//noinspection UseBulkOperation
		m.values().iterator().forEachRemaining(sObject::add);
		// values() is a Collection without an equals, so need to convert to Set first.
		assertEquals(new IntOpenHashSet(m.values()), s);
		assertEquals(new IntOpenHashSet(m.values()), sObject);
	}

	@Test
	public void testValuesSpliterator() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntSet s = IntOpenHashSet.toSet(m.values().intStream());
		// values() is a Collection without an equals, so need to convert to Set first.
		assertEquals(new IntOpenHashSet(m.values()), s);
	}

	@Test
	public void testValuesIteratorRemove() {
		assumeTrue(capabilities.contains(Capability.ITERATOR_MODIFY));

		m.defaultReturnValue(-1);

		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		IntIterator i = m.values().iterator();
		int toRemoveValue = i.nextInt();
		
		// Taking advantage of the fact we mapped keys to values
		assertTrue(m.containsKey(toRemoveValue));
		assertTrue(m.containsValue(toRemoveValue));
		assertEquals(toRemoveValue, m.get(toRemoveValue));
		i.remove();
		assertFalse(m.containsKey(toRemoveValue));
		assertFalse(m.containsValue(toRemoveValue));
		assertEquals(-1, m.get(toRemoveValue));
		assertEquals(99, m.size());
	}

	public enum Capability {
		KEY_SET_MODIFY, ITERATOR_MODIFY
	}
}
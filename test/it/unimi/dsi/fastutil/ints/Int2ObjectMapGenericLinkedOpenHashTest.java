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
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public class Int2ObjectMapGenericLinkedOpenHashTest extends Int2ObjectMapGenericTest<Int2ObjectLinkedOpenHashMap<Integer>> {
	
	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] {(Supplier<Int2ObjectMap<Integer>>) Int2ObjectLinkedOpenHashMap::new, EnumSet.allOf(Capability.class)});
	}

	@Test
	public void testIterator() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.put(i, Integer.valueOf(i)));
		}
		assertEquals(0, m.firstIntKey());

		IntListIterator iterator = (IntListIterator) m.keySet().iterator();
		for (int i = 0; i <= 100; i++) {
			assertEquals(Integer.toString(i), i - 1, iterator.previousIndex());
			assertEquals(Integer.toString(i), i, iterator.nextIndex());
			if (i != 100) {
				assertEquals(Integer.toString(i), i, iterator.nextInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(m.lastIntKey());
		for (int i = 100; i-- != 0; ) {
			assertEquals(Integer.toString(i), i, iterator.previousIndex());
			assertEquals(Integer.toString(i), i + 1, iterator.nextIndex());
			if (i != 0) {
				assertEquals(Integer.toString(i), i, iterator.previousInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i < 100; i++) {
			assertEquals(Integer.toString(i), i, iterator.previousIndex());
			assertEquals(Integer.toString(i), i + 1, iterator.nextIndex());
			if (i != 99) {
				assertEquals(Integer.toString(i), i + 1, iterator.nextInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i-- != -1; ) {
			assertEquals(Integer.toString(i), i + 1, iterator.previousIndex());
			assertEquals(Integer.toString(i), i + 2, iterator.nextIndex());
			if (i != -1) {
				assertEquals(Integer.toString(i), i + 1, iterator.previousInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i-- != -1; ) {
			assertEquals(Integer.toString(i), i + 1, iterator.previousInt());
		}
		assertEquals(-1, iterator.previousIndex());
		assertEquals(0, iterator.nextIndex());

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i < 100 - 1; i++) {
			assertEquals(Integer.toString(i), i + 1, iterator.nextInt());
		}
		assertEquals(99, iterator.previousIndex());
		assertEquals(100, iterator.nextIndex());

		iterator = (IntListIterator) m.keySet().iterator(50);
		iterator.previousInt();
		iterator.remove();
		assertEquals(49, iterator.previousIndex());
		assertEquals(49, iterator.previousInt());

		iterator = (IntListIterator) m.keySet().iterator(49);
		iterator.nextInt();
		iterator.remove();
		assertEquals(50, iterator.nextIndex());
		assertEquals(52, iterator.nextInt());
	}
	
	private static <T> List<T> toList(Collection<T> c) {
		if (c instanceof List) {
			return (List<T>)c;
		}
		if (c instanceof IntCollection) {
			// T is assured to be Integer in this case
			@SuppressWarnings("unchecked")
			List<T> ret = (List<T>)toList((IntCollection) c);
			return ret;
		}
		return new ArrayList<>(c);
	}

	private static IntList toList(IntCollection c) {
		if (c instanceof IntList) {
			return (IntList)c;
		}
		return new IntArrayList(c);
	}

	@Test
	public void testEntrySetSameOrder() {
		List<Entry<Integer>> expectedOrder = new ArrayList<>();
		// Intentionally thrown off natural order
		for (int i = 0; i < 100; i++) {
			int k;
			Integer v;
			if (i == 0) {
				k = 500;
				v = Integer.valueOf(i);
			} else {
				k = i;
				v = Integer.valueOf(i);
			}
			m.put(k, v);
			expectedOrder.add(new AbstractInt2ObjectMap.BasicEntry<>(k, v));
		}

		List<Entry<Integer>> entries = toList(m.int2ObjectEntrySet());
		assertEquals(expectedOrder, entries);

		List<Entry<Integer>> entriesForEach = new ArrayList<>();
		//noinspection UseBulkOperation
		m.int2ObjectEntrySet().forEach(entriesForEach::add);
		assertEquals(expectedOrder, entriesForEach);

		List<Entry<Integer>> entriesFromIterator = new ArrayList<>();
		//noinspection UseBulkOperation
		m.int2ObjectEntrySet().iterator().forEachRemaining(entriesFromIterator::add);
		assertEquals(expectedOrder, entriesFromIterator);

		List<Entry<Integer>> entriesFromSpliterator = m.int2ObjectEntrySet().stream().collect(Collectors.toList());
		assertEquals(expectedOrder, entriesFromSpliterator);
	}

	@Test
	public void testKeySetSameOrder() {
		IntList expectedOrder = new IntArrayList();
		// Intentionally thrown off natural order
		for (int i = 0; i < 100; i++) {
			int k;
			Integer v;
			if (i == 0) {
				k = 500;
				v = Integer.valueOf(i);
			} else {
				k = i;
				v = Integer.valueOf(i);
			}
    		m.put(k, v);
    		expectedOrder.add(k);
		}

		IntList keys = toList(m.keySet());
		assertEquals(expectedOrder, keys);
		
		IntList keysFromEntrySet = IntArrayList.toList(m.int2ObjectEntrySet().stream().mapToInt(Entry::getIntKey));
		assertEquals(expectedOrder, keysFromEntrySet);

		IntList keysForEach = new IntArrayList();
		//noinspection UseBulkOperation
		m.keySet().forEach(keysForEach::add);
		assertEquals(expectedOrder, keysForEach);

		IntList keysFromIterator = new IntArrayList();
		//noinspection UseBulkOperation
		m.keySet().iterator().forEachRemaining(keysFromIterator::add);
		assertEquals(expectedOrder, keysFromIterator);

		IntList keysFromSpliterator = IntArrayList.toList(m.keySet().intStream());
		assertEquals(expectedOrder, keysFromSpliterator);
	}
	
	@Test
	public void testValuesSameOrder() {
		List<Integer> expectedOrder = new ArrayList<>();
		// Intentionally thrown off natural order
		for (int i = 0; i < 100; i++) {
			int k;
			Integer v;
			if (i == 0) {
				k = 500;
				v = Integer.valueOf(1000);
			} else {
				k = i;
				v = Integer.valueOf(i);
			}
    		m.put(k, v);
    		expectedOrder.add(v);
		}

		List<Integer> values = toList(m.values());
		assertEquals(expectedOrder, values);

		List<Integer> valuesFromEntrySet = m.int2ObjectEntrySet().stream().map(Entry::getValue).collect(Collectors.toList());
		assertEquals(expectedOrder, valuesFromEntrySet);

		List<Integer> valuesForEach = new ArrayList<>();
		//noinspection UseBulkOperation
		m.values().forEach(valuesForEach::add);
		assertEquals(expectedOrder, valuesForEach);

		List<Integer> valuesFromIterator = new ArrayList<>();
		//noinspection UseBulkOperation
		m.values().iterator().forEachRemaining(valuesFromIterator::add);
		assertEquals(expectedOrder, valuesFromIterator);

		List<Integer> valuesFromSpliterator = m.values().stream().collect(Collectors.toList());
		assertEquals(expectedOrder, valuesFromSpliterator);
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorMissingElement() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.put(i, Integer.valueOf(i)));
		}
		m.keySet().iterator(1000);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NoSuchElementException.class)
	public void testNextAtEnd() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		final ObjectBidirectionalIterator<Map.Entry<Integer, Integer>> iterator = m.entrySet().iterator(m.entrySet().last());
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextAtEndFast() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		final ObjectBidirectionalIterator<Entry<Integer>> iterator = m.int2ObjectEntrySet().iterator(m.int2ObjectEntrySet().last());
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousAtStart() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		@SuppressWarnings("deprecation") final ObjectBidirectionalIterator<Map.Entry<Integer, Integer>> iterator = m.entrySet().iterator();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousAtStartFast() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		final ObjectBidirectionalIterator<Entry<Integer>> iterator = m.int2ObjectEntrySet().iterator();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}

	@Test
	public void testPutAndMove() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.putAndMoveToFirst(i, Integer.valueOf(i)));
		}
		m.clear();
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.putAndMoveToLast(i, Integer.valueOf(i)));
		}
		assertSame(DEFAULT, m.putAndMoveToFirst(-1, MINUS_ONE));
		assertEquals(-1, m.firstIntKey());
		assertSame(DEFAULT, m.putAndMoveToFirst(-2, Integer.valueOf(-2)));
		assertEquals(-2, m.firstIntKey());
		assertEquals(MINUS_ONE, m.putAndMoveToFirst(-1, MINUS_ONE));
		assertEquals(-1, m.firstIntKey());
		assertEquals(MINUS_ONE, m.putAndMoveToFirst(-1, MINUS_ONE));
		assertEquals(-1, m.firstIntKey());
		assertEquals(MINUS_ONE, m.putAndMoveToLast(-1, MINUS_ONE));
		assertEquals(-1, m.lastIntKey());
		assertSame(DEFAULT, m.putAndMoveToLast(100, Integer.valueOf(100)));
		assertEquals(100, m.lastIntKey());
		assertSame(DEFAULT, m.putAndMoveToLast(101, Integer.valueOf(101)));
		assertEquals(101, m.lastIntKey());
		assertEquals(Integer.valueOf(100), m.putAndMoveToLast(100, Integer.valueOf(100)));
		assertEquals(100, m.lastIntKey());
		assertEquals(Integer.valueOf(100), m.putAndMoveToLast(100, Integer.valueOf(100)));
		assertEquals(100, m.lastIntKey());
		assertEquals(Integer.valueOf(100), m.putAndMoveToFirst(100, Integer.valueOf(100)));
		assertEquals(100, m.firstIntKey());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveFirstEmpty() {
		m.removeFirst();
	}

	@Test
	public void testRemoveFirstLast() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertEquals(DEFAULT, m.put(i, Integer.valueOf(1 + i)));
		}
		assertEquals(ONE, m.removeFirst());
		assertEquals(TWO, m.removeFirst());
		assertEquals(Integer.valueOf(100), m.removeLast());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveLastEmpty() {
		m.removeLast();
	}
}
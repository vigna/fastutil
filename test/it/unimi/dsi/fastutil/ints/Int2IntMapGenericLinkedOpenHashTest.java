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

import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class Int2IntMapGenericLinkedOpenHashTest extends Int2IntMapGenericTest<Int2IntLinkedOpenHashMap> {
	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] {(Supplier<Int2IntMap>) Int2IntLinkedOpenHashMap::new, EnumSet.allOf(Capability.class)});
	}

	@Test
	public void testAddTo() {
		assertEquals(0, m.addTo(0, 2));
		assertEquals(2, m.get(0));
		assertEquals(2, m.addTo(0, 3));
		assertEquals(5, m.get(0));
		ObjectIterator<Entry> fastIterator = m.int2IntEntrySet().fastIterator();
		Int2IntMap.Entry next = fastIterator.next();
		assertEquals(0, next.getIntKey());
		assertEquals(5, next.getIntValue());
		assertFalse(fastIterator.hasNext());

		m.defaultReturnValue(-1);
		assertEquals(-1, m.addTo(1, 1));
		assertEquals(0, m.get(1));
		assertEquals(0, m.addTo(1, 1));
		assertEquals(1, m.get(1));
		assertEquals(1, m.addTo(1, -2));
		assertEquals(-1, m.get(1));
		fastIterator = m.int2IntEntrySet().fastIterator();
		next = fastIterator.next();
		assertEquals(0, next.getIntKey());
		assertEquals(5, next.getIntValue());
		next = fastIterator.next();
		assertEquals(1, next.getIntKey());
		assertEquals(-1, next.getIntValue());
		assertFalse(fastIterator.hasNext());

		for (int i = 0; i < 100; i++) {
			m.addTo(i, 1);
		}
		assertEquals(0, m.firstIntKey());
		assertEquals(99, m.lastIntKey());
	}

	@Test
	public void testIterator() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
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
		List<Entry> expectedOrder = new ArrayList<>();
		// Intentionally thrown off natural order
		for (int i = 0; i < 100; i++) {
			int k;
			int v;
			if (i == 0) {
				k = 500;
				v = i;
			} else {
				k = i;
				v = i;
			}
			m.put(k, v);
			expectedOrder.add(new AbstractInt2IntMap.BasicEntry(k, v));
		}

		List<Entry> entries = toList(m.int2IntEntrySet());
		assertEquals(expectedOrder, entries);

		List<Entry> entriesForEach = new ArrayList<>();
		//noinspection UseBulkOperation
		m.int2IntEntrySet().forEach(entriesForEach::add);
		assertEquals(expectedOrder, entriesForEach);

		List<Entry> entriesFromIterator = new ArrayList<>();
		//noinspection UseBulkOperation
		m.int2IntEntrySet().iterator().forEachRemaining(entriesFromIterator::add);
		assertEquals(expectedOrder, entriesFromIterator);

		List<Entry> entriesFromSpliterator = m.int2IntEntrySet().stream().collect(Collectors.toList());
		assertEquals(expectedOrder, entriesFromSpliterator);
	}

	@Test
	public void testKeySetSameOrder() {
		IntList expectedOrder = new IntArrayList();
		// Intentionally thrown off natural order
		for (int i = 0; i < 100; i++) {
			int k;
			int v;
			if (i == 0) {
				k = 500;
				v = i;
			} else {
				k = i;
				v = i;
			}
    		m.put(k, v);
    		expectedOrder.add(k);
		}

		IntList keys = toList(m.keySet());
		assertEquals(expectedOrder, keys);
		
		IntList keysFromEntrySet = IntArrayList.toList(m.int2IntEntrySet().stream().mapToInt(Entry::getIntKey));
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
		IntList expectedOrder = new IntArrayList();
		// Intentionally thrown off natural order
		for (int i = 0; i < 100; i++) {
			int k;
			int v;
			if (i == 0) {
				k = 500;
				v = 1000;
			} else {
				k = i;
				v = i;
			}
    		m.put(k, v);
    		expectedOrder.add(v);
		}

		IntList values = toList(m.values());
		assertEquals(expectedOrder, values);

		IntList valuesFromEntrySet = IntArrayList.toList(m.int2IntEntrySet().stream().mapToInt(Entry::getIntValue));
		assertEquals(expectedOrder, valuesFromEntrySet);

		IntList valuesForEach = new IntArrayList();
		//noinspection UseBulkOperation
		m.values().forEach(valuesForEach::add);
		assertEquals(expectedOrder, valuesForEach);

		IntList valuesFromIterator = new IntArrayList();
		//noinspection UseBulkOperation
		m.values().iterator().forEachRemaining(valuesFromIterator::add);
		assertEquals(expectedOrder, valuesFromIterator);

		IntList valuesFromSpliterator = IntArrayList.toList(m.values().intStream());
		assertEquals(expectedOrder, valuesFromSpliterator);
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorMissingElement() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		m.keySet().iterator(1000);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NoSuchElementException.class)
	public void testNextAtEnd() {
		m.addTo(1, 1);
		m.addTo(2, 2);
		m.addTo(3, 3);
		final ObjectBidirectionalIterator<Map.Entry<Integer, Integer>> iterator = m.entrySet().iterator(m.entrySet().last());
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextAtEndFast() {
		m.addTo(1, 1);
		m.addTo(2, 2);
		m.addTo(3, 3);
		final ObjectBidirectionalIterator<Entry> iterator = m.int2IntEntrySet().iterator(m.int2IntEntrySet().last());
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousAtStart() {
		m.addTo(1, 1);
		m.addTo(2, 2);
		m.addTo(3, 3);
		@SuppressWarnings("deprecation") final ObjectBidirectionalIterator<java.util.Map.Entry<Integer, Integer>> iterator = m.entrySet().iterator();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousAtStartFast() {
		m.addTo(1, 1);
		m.addTo(2, 2);
		m.addTo(3, 3);
		final ObjectBidirectionalIterator<Entry> iterator = m.int2IntEntrySet().iterator();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}

	@Test
	public void testPutAndMove() {
		m.defaultReturnValue(Integer.MIN_VALUE);
		for (int i = 0; i < 100; i++) {
			assertEquals(Integer.MIN_VALUE, m.putAndMoveToFirst(i, i));
		}
		m.clear();
		for (int i = 0; i < 100; i++) {
			assertEquals(Integer.MIN_VALUE, m.putAndMoveToLast(i, i));
		}
		assertEquals(Integer.MIN_VALUE, m.putAndMoveToFirst(-1, -1));
		assertEquals(-1, m.firstIntKey());
		assertEquals(Integer.MIN_VALUE, m.putAndMoveToFirst(-2, -2));
		assertEquals(-2, m.firstIntKey());
		assertEquals(-1, m.putAndMoveToFirst(-1, -1));
		assertEquals(-1, m.firstIntKey());
		assertEquals(-1, m.putAndMoveToFirst(-1, -1));
		assertEquals(-1, m.firstIntKey());
		assertEquals(-1, m.putAndMoveToLast(-1, -1));
		assertEquals(-1, m.lastIntKey());
		assertEquals(Integer.MIN_VALUE, m.putAndMoveToLast(100, 100));
		assertEquals(100, m.lastIntKey());
		assertEquals(Integer.MIN_VALUE, m.putAndMoveToLast(101, 101));
		assertEquals(101, m.lastIntKey());
		assertEquals(100, m.putAndMoveToLast(100, 100));
		assertEquals(100, m.lastIntKey());
		assertEquals(100, m.putAndMoveToLast(100, 100));
		assertEquals(100, m.lastIntKey());
		assertEquals(100, m.putAndMoveToFirst(100, 100));
		assertEquals(100, m.firstIntKey());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveFirstEmpty() {
		m.removeFirstInt();
	}

	@Test
	public void testRemoveFirstLast() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, 1 + i));
		}
		assertEquals(1, m.removeFirstInt());
		assertEquals(2, m.removeFirstInt());
		assertEquals(100, m.removeLastInt());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveLastEmpty() {
		m.removeLastInt();
	}
}
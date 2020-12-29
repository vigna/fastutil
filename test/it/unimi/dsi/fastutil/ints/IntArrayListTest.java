/*
 * Copyright (C) 2017-2020 Sebastiano Vigna
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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class IntArrayListTest {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEmptyListIsDifferentFromEmptySet() {
		assertFalse(IntLists.EMPTY_LIST.equals(IntSets.EMPTY_SET));
		assertFalse(IntSets.EMPTY_SET.equals(IntLists.EMPTY_LIST));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testNullInContains() {
		assertFalse(new IntArrayList().contains(null));
	}

	@Test
	public void testAddUsingIteratorToTheFirstPosition() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		final IntListIterator it = list.listIterator();
		it.add(42);
		assertTrue(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { 42, 24 }), list);
	}

	@Test
	public void testAddUsingIterator() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		list.add(42);
		final IntListIterator it = list.listIterator(1);
		it.add(86);
		assertTrue(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { 24, 86, 42}), list);
	}

	@Test
	public void testAddUsingSublistIterator() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		list.add(42);
		final IntList sublist = list.subList(1,2);
		final IntListIterator it = sublist.listIterator();
		it.add(86);
		assertTrue(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { 86, 42 }), sublist);
		assertEquals(IntArrayList.wrap(new int[] { 24, 86, 42 }), list);
	}

	@Test
	public void testAddUsingSubSublistIterator() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		list.add(86);
		list.add(42);
		final IntList sublist = list.subList(1,3);
		assertEquals(IntArrayList.wrap(new int[] { 86, 42 }), sublist);
		final IntList subsublist = sublist.subList(0,1);
		assertEquals(IntArrayList.wrap(new int[] { 86 }), subsublist);
		final IntListIterator it = subsublist.listIterator();
		assertTrue(it.hasNext());
		it.add(126);
		assertTrue(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { 126, 86 }), subsublist);
		assertEquals(IntArrayList.wrap(new int[] { 126, 86, 42 }), sublist);
		assertEquals(IntArrayList.wrap(new int[] { 24, 126, 86, 42 }), list);
	}

	@Test
	public void testRemoveUsingIterator() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		list.add(42);
		final IntListIterator it = list.listIterator(1);
		assertEquals(42, it.nextInt());
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { 24 }), list);
	}

	@Test
	public void testRemoveUsingSublistIterator() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		list.add(86);
		list.add(42);
		final IntList sublist = list.subList(1,3);
		final IntListIterator it = sublist.listIterator();
		assertEquals(86, it.nextInt());
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { 42 }), sublist);
		assertEquals(IntArrayList.wrap(new int[] { 24, 42 }), list);
	}


	@Test
	public void testRemoveUsingSubSublistIterator() {
		final IntArrayList list = new IntArrayList();
		list.add(24);
		list.add(126);
		list.add(86);
		list.add(42);
		final IntList sublist = list.subList(1,3);
		assertEquals(IntArrayList.wrap(new int[] { 126, 86 }), sublist);
		final IntList subsublist = sublist.subList(0,1);
		assertEquals(IntArrayList.wrap(new int[] { 126 }), subsublist);
		final IntListIterator it = subsublist.listIterator();
		assertTrue(it.hasNext());
		assertEquals(126, it.nextInt());
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(IntArrayList.wrap(new int[] { }), subsublist);
		assertEquals(IntArrayList.wrap(new int[] { 86 }), sublist);
		assertEquals(IntArrayList.wrap(new int[] { 24, 86, 42 }), list);
	}

	public void testAddAll() {
		final IntArrayList l = IntArrayList.wrap(new int[] { 0, 1 });
		l.addAll(IntArrayList.wrap(new int[] { 2, 3 } ));
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2, 3 }), l);
		// Test object based lists still work too.
		l.addAll(java.util.Arrays.asList(Integer.valueOf(4), Integer.valueOf(5)));
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2, 3, 4, 5 }), l);
	}

	@Test
	public void testAddAllAtIndex() {
		final IntArrayList l = IntArrayList.wrap(new int[] { 0, 3 });
		l.addAll(1, IntArrayList.wrap(new int[] { 1, 2 } ));
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2, 3 }), l);
		// Test object based lists still work too.
		l.addAll(2, java.util.Arrays.asList(Integer.valueOf(4), Integer.valueOf(5)));
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 4, 5, 2, 3 }), l);
	}

	@Test
	public void testRemoveAll() {
		IntArrayList l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		l.removeAll(IntSets.singleton(1));
		assertEquals(IntArrayList.wrap(new int[] { 0, 2 }), l);

		l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		l.removeAll(Collections.singleton(Integer.valueOf(1)));
		assertEquals(IntArrayList.wrap(new int[] { 0, 2 }), l);
	}

	@Test
	public void testClearSublist() {
		final IntArrayList l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		final IntList sublist = l.subList(1,3);
		sublist.clear();
		assertEquals(IntArrayList.wrap(new int[] { }), sublist);
		assertEquals(IntArrayList.wrap(new int[] { 0, 2 }), l);
	}

	public void testSort() {
		final IntArrayList l = IntArrayList.wrap(new int[] { 4, 2, 1, 3 });
		l.sort(null);
		assertEquals(IntArrayList.wrap(new int[] { 1, 2, 3, 4 }), l);
	}

	@Test
	public void testDefaultConstructors() {
		IntArrayList l;

		l = new IntArrayList();
		for(int i = 0; i < IntArrayList.DEFAULT_INITIAL_CAPACITY + 2; i++) l.add(0);

		l = new IntArrayList();
		l.addElements(0, new int[IntArrayList.DEFAULT_INITIAL_CAPACITY], 0, IntArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new IntArrayList();
		l.addElements(0, new int[2 * IntArrayList.DEFAULT_INITIAL_CAPACITY], 0, 2 * IntArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new IntArrayList(0);
		for(int i = 0; i < IntArrayList.DEFAULT_INITIAL_CAPACITY + 2; i++) l.add(0);

		l = new IntArrayList(0);
		l.addElements(0, new int[IntArrayList.DEFAULT_INITIAL_CAPACITY], 0, IntArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new IntArrayList(0);
		l.addElements(0, new int[2 * IntArrayList.DEFAULT_INITIAL_CAPACITY], 0, 2 * IntArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new IntArrayList(2 * IntArrayList.DEFAULT_INITIAL_CAPACITY );
		for(int i = 0; i < 3 * IntArrayList.DEFAULT_INITIAL_CAPACITY; i++) l.add(0);

		l = new IntArrayList(2 * IntArrayList.DEFAULT_INITIAL_CAPACITY );
		l.addElements(0, new int[3 * IntArrayList.DEFAULT_INITIAL_CAPACITY]);

		l = new IntArrayList(2 * IntArrayList.DEFAULT_INITIAL_CAPACITY );
		l.addElements(0, new int[3 * IntArrayList.DEFAULT_INITIAL_CAPACITY]);

		// Test lazy allocation
		l = new IntArrayList();
		l.ensureCapacity(1);
		assertSame(IntArrays.DEFAULT_EMPTY_ARRAY, l.elements());
		l.ensureCapacity(4);
		assertSame(IntArrays.DEFAULT_EMPTY_ARRAY, l.elements());

		l = new IntArrayList();
		l.ensureCapacity(1000000);
		assertNotSame(IntArrays.DEFAULT_EMPTY_ARRAY, l.elements());
		assertEquals(1000000, l.elements().length);

		l = new IntArrayList(0);
		l.ensureCapacity(1);
		assertNotSame(IntArrays.DEFAULT_EMPTY_ARRAY, l.elements());

		l = new IntArrayList(0);
		l.ensureCapacity(1);
		assertNotSame(IntArrays.DEFAULT_EMPTY_ARRAY, l.elements());
		l.ensureCapacity(1);
	}

	@Test
	public void testSizeOnDefaultInstance() {
		final IntArrayList l = new IntArrayList();
		l.size(100);
	}

	@Test
	public void testRemoveIf() {
		final IntArrayList l = IntArrayList.of(1,2,3,4,5,6);
		l.removeIf(i -> i % 2 == 0);
		assertEquals(IntArrayList.of(1,3,5), l);
	}

	@Test
	public void testOf() {
		final IntArrayList l = IntArrayList.of(0, 1, 2);
		assertEquals(IntArrayList.wrap(new int[] { 0, 1, 2 }), l);
	}

	public void testOfEmpty() {
		final IntArrayList l = IntArrayList.of();
		assertTrue(l.isEmpty());
	}

	@Test
	public void testOfSingleton() {
		final IntArrayList l = IntArrayList.of(0);
		assertEquals(IntArrayList.wrap(new int[]{0}), l);
	}

	@Test
	public void testToList() {
		final IntArrayList baseList = IntArrayList.of(2, 380, 1297);
		// Also conveniently serves as a test of the intStream and spliterator.
		final IntArrayList transformed = IntArrayList.toList(baseList.intStream().map(i -> i + 40));
		assertEquals(IntArrayList.of(42, 420, 1337), transformed);
	}

	@Test
	public void testSpliteratorTrySplit() {
		final IntArrayList baseList = IntArrayList.of(0, 1, 2, 3, 72, 5, 6);
		final IntSpliterator willBeSuffix = baseList.spliterator();
		assertEquals(baseList.size(), willBeSuffix.getExactSizeIfKnown());
		// Rather non-intuitively for finite sequences (but makes perfect sense for infinite ones),
		// the spec demands the original spliterator becomes the suffix and the new Spliterator becomes the prefix.
		final IntSpliterator prefix = willBeSuffix.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split into a prefix and suffix.
		final java.util.stream.IntStream suffixStream = java.util.stream.StreamSupport.intStream(willBeSuffix, false);
		final java.util.stream.IntStream prefixStream = java.util.stream.StreamSupport.intStream(prefix, false);

		final IntArrayList prefixList = IntArrayList.toList(prefixStream);
		final IntArrayList suffixList = IntArrayList.toList(suffixStream);
		assertEquals(baseList.size(), prefixList.size() + suffixList.size());
		assertEquals(baseList.subList(0, prefixList.size()), prefixList);
		assertEquals(baseList.subList(prefixList.size(), baseList.size()), suffixList);
		final IntArrayList recombinedList = new IntArrayList(baseList.size());
		recombinedList.addAll(prefixList);
		recombinedList.addAll(suffixList);
		assertEquals(baseList, recombinedList);
	}

	@Test
	public void testSpliteratorSkip() {
		final IntArrayList baseList = IntArrayList.of(0, 1, 2, 3, 72, 5, 6);
		final IntSpliterator spliterator = baseList.spliterator();
		assertEquals(1, spliterator.skip(1));
		final java.util.stream.IntStream stream = java.util.stream.StreamSupport.intStream(spliterator, false);
		assertEquals(baseList.subList(1, baseList.size()), IntArrayList.toList(stream));
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntArrayList.class, "test", /*num=*/"500", /*seed=*/"939384");
	}
}

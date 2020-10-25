package it.unimi.dsi.fastutil.ints;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

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
	public void testRemoveAll() {
		IntArrayList l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		l.removeAll(IntSets.singleton(1));
		assertEquals(IntArrayList.wrap(new int[] { 0, 2 }), l);

		l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		l.removeAll(Collections.singleton(Integer.valueOf(1)));
		assertEquals(IntArrayList.wrap(new int[] { 0, 2 }), l);
	}

	@Test
	public void testRetainAll() {
		IntArrayList l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		l.retainAll(IntSets.singleton(1));
		assertEquals(IntArrayList.wrap(new int[] { 1, 1 }), l);

		l = IntArrayList.wrap(new int[] { 0, 1, 1, 2 });
		l.retainAll(Collections.singleton(Integer.valueOf(2)));
		assertEquals(IntArrayList.wrap(new int[] { 2 }), l);
	}

	@Test
	public void testSort() {
		IntArrayList l = IntArrayList.wrap(new int[] { 4, 2, 1, 3 });
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

}

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

package it.unimi.dsi.fastutil.shorts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.function.Predicate;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class ShortArrayListTest {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEmptyListIsDifferentFromEmptySet() {
		assertFalse(ShortLists.EMPTY_LIST.equals(ShortSets.EMPTY_SET));
		assertFalse(ShortSets.EMPTY_SET.equals(ShortLists.EMPTY_LIST));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testNullInContains() {
		assertFalse(new ShortArrayList().contains(null));
	}

	// Here because Java int -> short literal logic annoyingly only handles variable assignment, not method parameters.
	private static short s(final int i) {
		return it.unimi.dsi.fastutil.SafeMath.safeIntToShort(i);
	}

	@Test
	public void testAddUsingIteratorToTheFirstPosition() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		final ShortListIterator it = list.listIterator();
		it.add(s(42));
		assertTrue(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { 42, 24 }), list);
	}

	@Test
	public void testAddUsingIterator() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		list.add(s(42));
		final ShortListIterator it = list.listIterator(1);
		it.add(s(86));
		assertTrue(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { 24, 86, 42}), list);
	}

	@Test
	public void testAddUsingSublistIterator() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		list.add(s(42));
		final ShortList sublist = list.subList(1,2);
		final ShortListIterator it = sublist.listIterator();
		it.add(s(86));
		assertTrue(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { 86, 42 }), sublist);
		assertEquals(ShortArrayList.wrap(new short[] { 24, 86, 42 }), list);
	}

	@Test
	public void testAddUsingSubSublistIterator() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		list.add(s(86));
		list.add(s(42));
		final ShortList sublist = list.subList(1,3);
		assertEquals(ShortArrayList.wrap(new short[] { 86, 42 }), sublist);
		final ShortList subsublist = sublist.subList(0,1);
		assertEquals(ShortArrayList.wrap(new short[] { 86 }), subsublist);
		final ShortListIterator it = subsublist.listIterator();
		assertTrue(it.hasNext());
		it.add(s(126));
		assertTrue(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { 126, 86 }), subsublist);
		assertEquals(ShortArrayList.wrap(new short[] { 126, 86, 42 }), sublist);
		assertEquals(ShortArrayList.wrap(new short[] { 24, 126, 86, 42 }), list);
	}

	@Test
	public void testRemoveUsingIterator() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		list.add(s(42));
		final ShortListIterator it = list.listIterator(1);
		assertEquals(42, it.nextShort());
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { 24 }), list);
	}

	@Test
	public void testRemoveUsingSublistIterator() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		list.add(s(86));
		list.add(s(42));
		final ShortList sublist = list.subList(1,3);
		final ShortListIterator it = sublist.listIterator();
		assertEquals(86, it.nextShort());
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { 42 }), sublist);
		assertEquals(ShortArrayList.wrap(new short[] { 24, 42 }), list);
	}


	@Test
	public void testRemoveUsingSubSublistIterator() {
		final ShortArrayList list = new ShortArrayList();
		list.add(s(24));
		list.add(s(126));
		list.add(s(86));
		list.add(s(42));
		final ShortList sublist = list.subList(1,3);
		assertEquals(ShortArrayList.wrap(new short[] { 126, 86 }), sublist);
		final ShortList subsublist = sublist.subList(0,1);
		assertEquals(ShortArrayList.wrap(new short[] { 126 }), subsublist);
		final ShortListIterator it = subsublist.listIterator();
		assertTrue(it.hasNext());
		assertEquals(126, it.nextShort());
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(ShortArrayList.wrap(new short[] { }), subsublist);
		assertEquals(ShortArrayList.wrap(new short[] { 86 }), sublist);
		assertEquals(ShortArrayList.wrap(new short[] { 24, 86, 42 }), list);
	}

	public void testAddAll() {
		final ShortArrayList l = ShortArrayList.wrap(new short[] { 0, 1 });
		l.addAll(ShortArrayList.wrap(new short[] { 2, 3 } ));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 1, 2, 3 }), l);
		// Test object based lists still work too.
		l.addAll(java.util.Arrays.asList(Short.valueOf(s(4)), Short.valueOf(s(5))));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 1, 2, 3, 4, 5 }), l);
	}

	@Test
	public void testAddAllAtIndex() {
		final ShortArrayList l = ShortArrayList.wrap(new short[] { 0, 3 });
		l.addAll(1, ShortArrayList.wrap(new short[] { 1, 2 } ));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 1, 2, 3 }), l);
		// Test object based lists still work too.
		l.addAll(2, java.util.Arrays.asList(Short.valueOf(s(4)), Short.valueOf(s(5))));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 1, 4, 5, 2, 3 }), l);
	}

	@Test
	public void testRemoveAll() {
		ShortArrayList l = ShortArrayList.wrap(new short[] { 0, 1, 1, 2 });
		l.removeAll(ShortSets.singleton((short)1));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 2 }), l);

		l = ShortArrayList.wrap(new short[] { 0, 1, 1, 2 });
		l.removeAll(Collections.singleton(Short.valueOf(s(1))));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 2 }), l);
	}

	@Test
	public void testClearSublist() {
		final ShortArrayList l = ShortArrayList.wrap(new short[] { 0, 1, 1, 2 });
		final ShortList sublist = l.subList(1,3);
		sublist.clear();
		assertEquals(ShortArrayList.wrap(new short[] { }), sublist);
		assertEquals(ShortArrayList.wrap(new short[] { 0, 2 }), l);
	}

	public void testSort() {
		final ShortArrayList l = ShortArrayList.wrap(new short[] { 4, 2, 1, 3 });
		l.sort(null);
		assertEquals(ShortArrayList.wrap(new short[] { 1, 2, 3, 4 }), l);
	}

	@Test
	public void testDefaultConstructors() {
		ShortArrayList l;

		l = new ShortArrayList();
		for(int i = 0; i < ShortArrayList.DEFAULT_INITIAL_CAPACITY + 2; i++) l.add(s(0));

		l = new ShortArrayList();
		l.addElements(0, new short[ShortArrayList.DEFAULT_INITIAL_CAPACITY], 0, ShortArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new ShortArrayList();
		l.addElements(0, new short[2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY], 0, 2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new ShortArrayList(0);
		for(int i = 0; i < ShortArrayList.DEFAULT_INITIAL_CAPACITY + 2; i++) l.add(s(0));

		l = new ShortArrayList(0);
		l.addElements(0, new short[ShortArrayList.DEFAULT_INITIAL_CAPACITY], 0, ShortArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new ShortArrayList(0);
		l.addElements(0, new short[2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY], 0, 2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY);

		l = new ShortArrayList(2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY );
		for(int i = 0; i < 3 * ShortArrayList.DEFAULT_INITIAL_CAPACITY; i++) l.add(s(0));

		l = new ShortArrayList(2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY );
		l.addElements(0, new short[3 * ShortArrayList.DEFAULT_INITIAL_CAPACITY]);

		l = new ShortArrayList(2 * ShortArrayList.DEFAULT_INITIAL_CAPACITY );
		l.addElements(0, new short[3 * ShortArrayList.DEFAULT_INITIAL_CAPACITY]);

		// Test lazy allocation
		l = new ShortArrayList();
		l.ensureCapacity(1);
		assertSame(ShortArrays.DEFAULT_EMPTY_ARRAY, l.elements());
		l.ensureCapacity(4);
		assertSame(ShortArrays.DEFAULT_EMPTY_ARRAY, l.elements());

		l = new ShortArrayList();
		l.ensureCapacity(1000000);
		assertNotSame(ShortArrays.DEFAULT_EMPTY_ARRAY, l.elements());
		assertEquals(1000000, l.elements().length);

		l = new ShortArrayList(0);
		l.ensureCapacity(1);
		assertNotSame(ShortArrays.DEFAULT_EMPTY_ARRAY, l.elements());

		l = new ShortArrayList(0);
		l.ensureCapacity(1);
		assertNotSame(ShortArrays.DEFAULT_EMPTY_ARRAY, l.elements());
		l.ensureCapacity(1);
	}

	@Test
	public void testSizeOnDefaultInstance() {
		final ShortArrayList l = new ShortArrayList();
		l.size(100);
	}

	@Test
	public void testForEach() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		// A hack-ish loop testing forEach, in real code you would use intStream to compute the sum.
		final int[] forEachSum = new int[1];
		// i is a short here; we are getting ShortConsumer overload.
		l.forEach(i -> forEachSum[0] += i);
		final int realSum = l.intStream().sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@Test
	public void testForEach_jdkConsumer() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		// A hack-ish loop testing forEach, in real code you would use intStream to compute the sum.
		final int[] forEachSum = new int[1];
		l.forEach((java.util.function.IntConsumer) (i -> forEachSum[0] += i));
		final int realSum = l.intStream().sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testForEach_objectConsumer() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		// A hack-ish loop testing forEach, in real code you would use intStream to compute the sum.
		final int[] forEachSum = new int[1];
		l.forEach((java.util.function.Consumer<Short>)(i -> forEachSum[0] += i.shortValue()));
		final int realSum = l.intStream().sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@Test
	public void testIterator_forEachRemaining() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		// A hack-ish loop testing forEach, in real code you would use intStream to compute the sum.
		final int[] forEachSum = new int[1];
		l.iterator().forEachRemaining(i -> forEachSum[0] += i);
		final int realSum = l.intStream().sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@Test
	public void testIterator_forEachRemaining_jdkConsumer() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		// A hack-ish loop testing forEach, in real code you would use intStream to compute the sum.
		final int[] forEachSum = new int[1];
		l.iterator().forEachRemaining((java.util.function.IntConsumer) (i -> forEachSum[0] += i));
		final int realSum = l.intStream().sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testIterator_forEachRemaining_objectConsumer() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		// A hack-ish loop testing forEach, in real code you would use intStream to compute the sum.
		final int[] forEachSum = new int[1];
		l.iterator().forEachRemaining((java.util.function.Consumer<Short>)(i -> forEachSum[0] += i.shortValue()));
		final int realSum = l.intStream().sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@Test
	public void testRemoveIf() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		l.removeIf(i -> i % 2 == 0);
		assertEquals(ShortArrayList.of(s(1),s(3),s(5)), l);
	}

	@Test
	public void testRemoveIf_jdkPredicate() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		l.removeIf((java.util.function.IntPredicate) (i -> i % 2 == 0));
		assertEquals(ShortArrayList.of(s(1),s(3),s(5)), l);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveIf_objectPredicate() {
		final ShortArrayList l = new ShortArrayList(new short[] {1,2,3,4,5,6});
		l.removeIf((java.util.function.Predicate<Short>)(i -> i.shortValue() % 2 == 0));
		assertEquals(ShortArrayList.of(s(1),s(3),s(5)), l);
	}

	@Test
	public void testOf() {
		final ShortArrayList l = ShortArrayList.of(s(0), s(1), s(2));
		assertEquals(ShortArrayList.wrap(new short[] { 0, 1, 2 }), l);
	}

	public void testOfEmpty() {
		final ShortArrayList l = ShortArrayList.of();
		assertTrue(l.isEmpty());
	}

	@Test
	public void testOfSingleton() {
		final ShortArrayList l = ShortArrayList.of(s(0));
		assertEquals(ShortArrayList.wrap(new short[]{0}), l);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ShortArrayList.class, "test", /*num=*/"500", /*seed=*/"939384");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveIfOverloads() {
		final ShortArrayList l = ShortArrayList.of((short)1, (short)2, (short)3);
		l.removeIf(x -> x == 1);
		final ShortPredicate p = x -> x == 1;
		l.removeIf(p);
		final it.unimi.dsi.fastutil.ints.IntPredicate q = x -> x == 1;
		l.removeIf(q);
		@SuppressWarnings("boxing")
		final Predicate<Short> r = x -> x == 1;
		l.removeIf(r);
	}
}

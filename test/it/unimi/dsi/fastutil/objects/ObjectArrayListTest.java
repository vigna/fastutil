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

package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.ints.IntList;

public class ObjectArrayListTest {

	@SuppressWarnings("boxing")
	@Test
	public void testRemoveAll() {
		ObjectArrayList<Integer> l = ObjectArrayList.wrap(new Integer[] { 0, 1, 1, 2 });
		l.removeAll(ObjectSets.singleton(1));
		assertEquals(ObjectArrayList.wrap(new Integer[] { 0, 2 }), l);
		assertTrue(l.elements()[2] == null);
		assertTrue(l.elements()[3] == null);

		l = ObjectArrayList.wrap(new Integer[] { 0, 1, 1, 2 });
		l.removeAll(ObjectSets.singleton(1));
		assertEquals(ObjectArrayList.wrap(new Integer[] { 0, 2 }), l);
		assertTrue(l.elements()[2] == null);
		assertTrue(l.elements()[3] == null);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testAddAllCollection() {
		final ObjectArrayList<Integer> l = ObjectArrayList.wrap(new Integer[] { 0, 1, 1, 2 });
		final List<Integer> m = Arrays.asList(new Integer[] { 2, 3, 3, 4 });
		l.addAll(0, m);
		assertEquals(List.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, IntList.of());
		l.addAll(2, IntList.of());
		assertEquals(List.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, (Collection<Integer>)ObjectList.of(0));
		assertEquals(List.of(0, 2, 3, 3, 4, 0, 1, 1, 2), l);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testAddAllList() {
		final ObjectArrayList<Integer> l = ObjectArrayList.wrap(new Integer[] { 0, 1, 1, 2 });
		final ObjectArrayList<Integer> m = ObjectArrayList.wrap(new Integer[] { 2, 3, 3, 4 });
		l.addAll(0, m);
		assertEquals(List.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, List.of());
		l.addAll(2, List.of());
		assertEquals(List.of(2, 3, 3, 4, 0, 1, 1, 2), l);
	}

	@Test
	public void testSizeOnDefaultInstance() {
		final ObjectArrayList<Integer> l = new ObjectArrayList<>();
		l.size(100);
	}

	@Test
	public void testForEach() {
		final ObjectArrayList<Integer> l = ObjectArrayList.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6));
		// A hack-ish loop testing forEach, in real code you would mapToInt and then compute the sum.
		final int[] forEachSum = new int[1];
		l.forEach(i -> forEachSum[0] += i.intValue());
		final int realSum = l.stream().mapToInt(Integer::intValue).sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@Test
	public void testIterator_forEachRemaining() {
		final ObjectArrayList<Integer> l = ObjectArrayList.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6));
		// A hack-ish loop testing forEach, in real code you would mapToInt and then compute the sum.
		final int[] forEachSum = new int[1];
		l.iterator().forEachRemaining(i -> forEachSum[0] += i.intValue());
		final int realSum = l.stream().mapToInt(Integer::intValue).sum();
		assertEquals(realSum, forEachSum[0]);
	}

	@Test
	public void testRemoveIf() {
		final ObjectArrayList<Integer> l = ObjectArrayList.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6));
		l.removeIf(i -> i.intValue() % 2 == 0);
		assertEquals(ObjectArrayList.of(Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(5)), l);
	}

	@Test
	public void testOf() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0", "1", "2");
		assertEquals(ObjectArrayList.wrap(new String[] { "0", "1", "2" }), l);
	}

	@Test
	public void testOfEmpty() {
		final ObjectArrayList<String> l = ObjectArrayList.of();
		assertTrue(l.isEmpty());
	}

	@Test
	public void testOfSingleton() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0");
		assertEquals(ObjectArrayList.wrap(new String[] { "0" }), l);
	}

	@Test
	public void testToArray() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0", "1", "2");
		assertArrayEquals(new Object[] {"0", "1", "2"}, l.toArray());
	}

	@Test
	public void testToArrayAlwaysObject() {
		final ObjectArrayList<String> stringBacked = ObjectArrayList.wrap(new String[] {"1", "2"});
		assertEquals(String[].class, stringBacked.elements().getClass());
		assertEquals(Object[].class, stringBacked.toArray().getClass());
	}

	@Test
	public void testCopyingToArray() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0", "1", "2");
		final Object[] out = new String[3];
		Object[] newOut;
		assertArrayEquals(new Object[] {"0", "1", "2"}, newOut = l.toArray(out));
		assertSame(out, newOut);
	}

	@Test
	public void testCopyingToArrayUndersized() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0", "1", "2");
		final Object[] out = new String[2];
		Object[] newOut;
		assertArrayEquals(new Object[] {"0", "1", "2"}, newOut = l.toArray(out));
		assertEquals(String[].class, newOut.getClass());
		assertNotSame(out, newOut);
	}

	@Test
	public void testCopyingToArrayOversized() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0", "1", "2");
		final Object[] out = new String[5];
		out[3] = "I should be replaced with null per spec.";
		out[4] = "Not me though.";
		Object[] newOut;
		assertArrayEquals(new Object[] {"0", "1", "2", null, "Not me though."}, newOut = l.toArray(out));
		assertSame(out, newOut);
	}

	@Test
	public void testToList() {
		final ObjectArrayList<String> baseList = ObjectArrayList.of("wood", "board", "glass", "metal");
		final ObjectArrayList<String> transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectArrayList.toList());
		assertEquals(ObjectArrayList.of("plywood", "plyboard", "plyglass", "plymetal"), transformed);
	}

	@Test
	public void testToListWithExpectedSize() {
		final ObjectArrayList<String> baseList = ObjectArrayList.of("wood", "board", "glass", "metal");
		ObjectArrayList<String> transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectArrayList.toListWithExpectedSize(4));
		final ObjectArrayList<String> expectedList = ObjectArrayList.of("plywood", "plyboard", "plyglass", "plymetal");
		assertEquals(expectedList, transformed);

		// Test undersized below default capacity
		transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectArrayList.toListWithExpectedSize(2));
		assertEquals(expectedList, transformed);

		// Test oversized below default capacity
		transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectArrayList.toListWithExpectedSize(8));
		assertEquals(expectedList, transformed);

		// Test oversized
		transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectArrayList.toListWithExpectedSize(50));
		assertEquals(expectedList, transformed);
	}

	@Test
	public void testSpliteratorTrySplit() {
		final ObjectArrayList<String> baseList = ObjectArrayList
				.of("0", "1", "2", "3", "4", "5", "bird");
		final ObjectSpliterator<String> willBeSuffix = baseList.spliterator();
		assertEquals(baseList.size(), willBeSuffix.getExactSizeIfKnown());
		// Rather non-intuitively for finite sequences (but makes perfect sense for infinite ones),
		// the spec demands the original spliterator becomes the suffix and the new Spliterator becomes the prefix.
		final ObjectSpliterator<String> prefix = willBeSuffix.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split into a prefix and suffix.
		final java.util.stream.Stream<String> suffixStream = java.util.stream.StreamSupport
				.stream(willBeSuffix, false);
		final java.util.stream.Stream<String> prefixStream = java.util.stream.StreamSupport
				.stream(prefix, false);

		final ObjectArrayList<String> prefixList = prefixStream.collect(ObjectArrayList.toList());
		final ObjectArrayList<String> suffixList = suffixStream.collect(ObjectArrayList.toList());
		assertEquals(baseList.size(), prefixList.size() + suffixList.size());
		assertEquals(baseList.subList(0, prefixList.size()), prefixList);
		assertEquals(baseList.subList(prefixList.size(), baseList.size()), suffixList);
		final ObjectArrayList<String> recombinedList = new ObjectArrayList<>(baseList.size());
		recombinedList.addAll(prefixList);
		recombinedList.addAll(suffixList);
		assertEquals(baseList, recombinedList);
	}

	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectArrayList.class, "test", /*num=*/"500", /*seed=*/"939384");
	}
}

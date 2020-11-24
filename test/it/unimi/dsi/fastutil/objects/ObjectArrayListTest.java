package it.unimi.dsi.fastutil.objects;

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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

	@Test
	public void testSizeOnDefaultInstance() {
		final ObjectArrayList<Integer> l = new ObjectArrayList<>();
		l.size(100);
	}

	@Test
	public void testOf() {
		final ObjectArrayList<String> l = ObjectArrayList.of("0", "1", "2");
		assertEquals(ObjectArrayList.wrap(new String[] { "0", "1", "2" }), l);
	}
	
	@Test
	public void testToList() {
		final ObjectArrayList<String> baseList = ObjectArrayList.of("wood", "board", "glass", "metal");
		ObjectArrayList<String> transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectArrayList.toList());
		assertEquals(ObjectArrayList.of("plywood", "plyboard", "plyglass", "plymetal"), transformed);
	}

	@Test
	public void testSpliteratorTrySplit() {
		final ObjectArrayList<String> baseList = ObjectArrayList.of("0", "1", "2", "3", "4", "5", "bird");
		ObjectSpliterator<String> willBeSuffix = baseList.spliterator();
		assertEquals(baseList.size(), willBeSuffix.getExactSizeIfKnown());
		// Rather non-intuitively for finite sequences (but makes perfect sense for infinite ones),
		// the spec demands the original spliterator becomes the suffix and the new Spliterator becomes the prefix.
		ObjectSpliterator<String> prefix = willBeSuffix.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split into a prefix and suffix.
		java.util.stream.Stream<String> suffixStream = java.util.stream.StreamSupport.stream(willBeSuffix, false);
		java.util.stream.Stream<String> prefixStream = java.util.stream.StreamSupport.stream(prefix, false);

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
}

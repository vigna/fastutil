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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

public class IntArraySetTest {

	@Test
	public void testCopyConstructor() {
		final IntOpenHashSet s = new IntOpenHashSet(new int[] { 1, 2 });
		assertEquals(new IntArraySet(s), s);
		assertEquals(new HashSet<>(s), s);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testNullInEquals() {
		assertFalse(new IntArraySet(Arrays.asList(42)).equals(Collections.singleton(null)));
	}

	@Test
	public void testSet() {
		for(int i = 0; i <= 1; i++) {
			final IntArraySet s = i == 0 ? new IntArraySet() : new IntArraySet(new int[i]);
			assertTrue(s.add(1));
			assertEquals(1 + i, s.size());
			assertTrue(s.contains(1));
			assertTrue(s.add(2));
			assertTrue(s.contains(2));
			assertEquals(2 + i, s.size());
			assertFalse(s.add(1));
			assertFalse(s.remove(3));
			assertTrue(s.add(3));
			assertEquals(3 + i, s.size());
			assertTrue(s.contains(1));
			assertTrue(s.contains(2));
			assertTrue(s.contains(3));
			int[] expectedArray = i == 0 ? new int[] { 1, 2, 3 } : new int[] { 0, 1, 2, 3 };
			IntSet expected = new IntOpenHashSet(expectedArray);
			assertEquals(expected, s);
			assertEquals(s, expected);
			assertEquals(expected, new IntOpenHashSet(s.iterator()));
			assertEquals(expected, new IntArraySet(s.intStream().toArray()));
			// Test iterator and spliterator (through stream) preserve order.
			assertArrayEquals(expectedArray, s.toIntArray());
			assertArrayEquals(expectedArray, new IntArrayList(s.iterator()).toIntArray());
			assertArrayEquals(expectedArray, s.intStream().toArray());
			assertTrue(s.remove(3));
			assertEquals(2 + i, s.size());
			assertTrue(s.remove(1));
			assertEquals(1 + i, s.size());
			assertFalse(s.contains(1));
			assertTrue(s.remove(2));
			assertEquals(0 + i, s.size());
			assertFalse(s.contains(1));
		}
	}

	@Test
	public void testClone() {
		final IntArraySet s = new IntArraySet();
		assertEquals(s, s.clone());
		s.add(0);
		assertEquals(s, s.clone());
		s.add(0);
		assertEquals(s, s.clone());
		s.add(1);
		assertEquals(s, s.clone());
		s.add(2);
		assertEquals(s, s.clone());
		s.remove(0);
		assertEquals(s, s.clone());
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		final IntArraySet s = new IntArraySet();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(s);
		oos.close();
		assertEquals(s, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));

		s.add(0);
		s.add(1);

		baos.reset();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(s);
		oos.close();
		assertEquals(s, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));
	}

	@Test
	public void testRemove() {
		IntSet set = new IntArraySet(new int[] { 42 });

		IntIterator iterator = set.iterator();
		assertTrue(iterator.hasNext());
		iterator.nextInt();
		iterator.remove();
		assertFalse(iterator.hasNext());
		assertEquals(0, set.size());

		set = new IntArraySet(new int[] { 42, 43, 44 });

		iterator = set.iterator();
		assertTrue(iterator.hasNext());
		iterator.nextInt();
		iterator.nextInt();
		iterator.remove();
		assertEquals(44, iterator.nextInt ());
		assertFalse(iterator.hasNext());
		assertEquals(new IntArraySet(new int[] { 42, 44 }), set);
	}

	@Test
	public void testOf() {
		final IntArraySet s = IntArraySet.of(0, 1, 2);
		assertEquals(new IntArraySet(new int[] { 0, 1, 2 }), s);
	}

	@Test
	public void testOfEmpty() {
		final IntArraySet s = IntArraySet.of();
		assertTrue(s.isEmpty());
	}

	@Test
	public void testOfSingleton() {
		final IntArraySet s = IntArraySet.of(0);
		assertEquals(new IntArraySet(new int[] { 0 }), s);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOfDuplicateThrows() {
		IntArraySet.of(0, 0);
	}

	@Test
	public void testOfUnchecked() {
		final IntArraySet s = IntArraySet.ofUnchecked(0, 1, 2);
		assertEquals(new IntArraySet(new int[] { 0, 1, 2 }), s);
	}

	@Test
	public void testOfUncheckedEmpty() {
		final IntArraySet s = IntArraySet.ofUnchecked();
		assertTrue(s.isEmpty());
	}

	@Test
	public void testOfUnchekedSingleton() {
		final IntArraySet s = IntArraySet.ofUnchecked(0);
		assertEquals(new IntArraySet(new int[] { 0 }), s);
	}

	@Test
	public void testOfUnchekedDuplicatesNotDetected() {
		// A IntArraySet in an invalid state that by spec we aren't checking for in this method.
		final IntArraySet s = IntArraySet.ofUnchecked(0, 0);
		assertEquals(new IntArraySet(new int[] { 0 , 0 }), s);
	}
}

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

public class ObjectArraySetTest {

	@SuppressWarnings("boxing")
	@Test
	public void testNullInEquals() {
		assertFalse(new ObjectArraySet<>(Arrays.asList(42)).equals(Collections.singleton(null)));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testSet() {
		for(int i = 0; i <= 1; i++) {
			final ObjectArraySet<Integer> s = i == 0 ? new ObjectArraySet<>() : new ObjectArraySet<>(new Integer[] { 0 });
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
			assertTrue(s.contains(2));
			assertEquals(new ObjectOpenHashSet<>(i == 0 ? new Integer[] { 1, 2, 3 } : new Integer[] { 0, 1, 2, 3 }), new ObjectOpenHashSet<>(s.iterator()));
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

	@SuppressWarnings("boxing")
	@Test
	public void testClone() {
		final ObjectArraySet<Integer> s = new ObjectArraySet<>();
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

	@SuppressWarnings("boxing")
	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		final ObjectArraySet<Integer> s = new ObjectArraySet<>();
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
	@SuppressWarnings("boxing")
	public void testRemove() {
		ObjectSet<Integer> set = new ObjectArraySet<>(new Integer[] { 42 });

		Iterator<Integer> iterator = set.iterator();
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.remove();
		assertFalse(iterator.hasNext());
		assertEquals(0, set.size());

		set = new ObjectArraySet<>(new Integer[] { 42, 43, 44 });

		iterator = set.iterator();
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.next();
		iterator.remove();
		assertEquals(Integer.valueOf(44), iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals(new ObjectArraySet<Integer>(new Integer[] { 42, 44 }), set);
	}
}

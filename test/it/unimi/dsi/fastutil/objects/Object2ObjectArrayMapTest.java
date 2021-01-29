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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

public class Object2ObjectArrayMapTest  {


	@SuppressWarnings("boxing")
	@Test
	public void testContainsNull() {
		final Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<>(new Integer[] { 1, 2, 3 },  new Integer[] { 1, 2, 3 });
		assertFalse(m.containsKey(null));
		assertTrue(m.get(null) == null);
	}

	@SuppressWarnings({ "boxing", "unlikely-arg-type" })
	@Test
	public void testEquals() {
		final Object2ObjectArrayMap<Integer,Integer> a1 = new Object2ObjectArrayMap<>();
		a1.put(0,  1);
		a1.put(1000, -1);
		a1.put(2000, 3);

		final Object2ObjectArrayMap<Integer,Integer> a2 = new Object2ObjectArrayMap<>();
		a2.put(0,  1);
		a2.put(1000, -1);
		a2.put(2000, 3);

		assertEquals(a1, a2);

		final Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<>(new Integer[] { 1, 2 },  new Integer[] { 1, 2 });
		assertFalse(m.equals(new Object2ObjectOpenHashMap<>(new Integer[] { 1, null }, new Integer[] { 1, 1 })));
	}

	@SuppressWarnings({ "boxing" })
	@Test
	public void testMap() {
		for(int i = 0; i <= 1; i++) {
			final Object2ObjectArrayMap<Integer,Integer> m = i == 0 ? new Object2ObjectArrayMap<>() : new Object2ObjectArrayMap<>(new Integer[] { 0 }, new Integer[] { 0 });
			assertEquals(null, m.put(1, 1));
			assertEquals(1 + i, m.size());
			assertTrue(m.containsKey(1));
			assertTrue(m.containsValue(1));
			assertEquals(null, m.put(2, 2));
			assertTrue(m.containsKey(2));
			assertTrue(m.containsValue(2));
			assertEquals(2 + i, m.size());
			assertEquals(Integer.valueOf(1), m.put(1, 3));
			assertTrue(m.containsValue(3));
			assertEquals(null, m.remove(3));
			assertEquals(null, m.put(3, 3));
			assertTrue(m.containsKey(3));
			assertTrue(m.containsValue(3));
			assertEquals(3 + i, m.size());
			assertEquals(Integer.valueOf(3), m.get(1));
			assertEquals(Integer.valueOf(2), m.get(2));
			assertEquals(Integer.valueOf(3), m.get(3));
			assertEquals(new ObjectOpenHashSet<>(i == 0 ? new Integer[] { 1, 2, 3 } : new Integer[] { 0, 1, 2, 3 }), new ObjectOpenHashSet<>(m.keySet().iterator()));
			assertEquals(new ObjectOpenHashSet<>(i == 0 ? new Integer[] { 3, 2, 3 } : new Integer[] { 0, 3, 2, 3 }), new ObjectOpenHashSet<>(m.values().iterator()));

			for(final Entry<Integer, Integer> e: m.object2ObjectEntrySet()) assertEquals(e.getValue(), m.get(e.getKey()));

			assertTrue(i != 0 == m.object2ObjectEntrySet().contains(new AbstractObject2ObjectMap.BasicEntry<>(0, 0)));
			assertTrue(m.object2ObjectEntrySet().contains(new AbstractObject2ObjectMap.BasicEntry<>(1, 3)));
			assertTrue(m.object2ObjectEntrySet().contains(new AbstractObject2ObjectMap.BasicEntry<>(2, 2)));
			assertTrue(m.object2ObjectEntrySet().contains(new AbstractObject2ObjectMap.BasicEntry<>(3, 3)));
			assertFalse(m.object2ObjectEntrySet().contains(new AbstractObject2ObjectMap.BasicEntry<>(1, 2)));
			assertFalse(m.object2ObjectEntrySet().contains(new AbstractObject2ObjectMap.BasicEntry<>(2, 1)));

			assertEquals(Integer.valueOf(3), m.remove(3));
			assertEquals(2 + i, m.size());
			assertEquals(Integer.valueOf(3), m.remove(1));
			assertEquals(1 + i, m.size());
			assertFalse(m.containsKey(1));
			assertEquals(Integer.valueOf(2), m.remove(2));
			assertEquals(0 + i, m.size());
			assertFalse(m.containsKey(1));
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public void testClone() {
		final Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<>();
		assertEquals(m, m.clone());
		m.put(0, 1);
		assertEquals(m, m.clone());
		m.put(0, 2);
		assertEquals(m, m.clone());
		m.put(1, 2);
		assertEquals(m, m.clone());
		m.remove(1);
		assertEquals(m, m.clone());
	}

	@SuppressWarnings("boxing")
	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		final Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(m);
		oos.close();
		assertEquals(m, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));

		m.put(0, 1);
		m.put(1, 2);

		baos.reset();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(m);
		oos.close();
		assertEquals(m, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testIteratorRemove() {
		final Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<>(new Integer[] { 1, 2, 3 },  new Integer[] { 1, 2, 3 });
		final ObjectIterator<Object2ObjectMap.Entry<Integer, Integer>> keySet = m.object2ObjectEntrySet().iterator();
		keySet.next();
		keySet.next();
		keySet.remove();
		assertTrue(keySet.hasNext());
		final Entry<Integer, Integer> next = keySet.next();
		assertEquals(Integer.valueOf(3), next.getKey());
		assertEquals(Integer.valueOf(3), next.getValue());
	}
}

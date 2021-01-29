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

@SuppressWarnings("deprecation")
public class Reference2ReferenceArrayMapTest {

	@Test
	public void testMap() {
		for(int i = 0; i <= 2; i++) {
			final Reference2ReferenceArrayMap<Object,Object> m = i == 0 ? new Reference2ReferenceArrayMap<>() : new Reference2ReferenceArrayMap<>(i);
			final Integer one = new Integer(1), two = new Integer(2), three = new Integer(3);
			assertEquals(null, m.put(one, one));
			assertEquals(1, m.size());
			assertTrue(m.containsKey(one));
			assertTrue(m.containsValue(one));
			assertEquals(null, m.put(two, two));
			assertTrue(m.containsKey(two));
			assertTrue(m.containsValue(two));
			assertEquals(2, m.size());
			assertEquals(one, m.put(one, three));
			assertTrue(m.containsValue(three));
			assertEquals(null, m.remove(three));
			assertEquals(null, m.put(three, three));
			assertTrue(m.containsKey(three));

			assertEquals(new ReferenceOpenHashSet<>(new Object[] { one, two, three }), new ReferenceOpenHashSet<>(m.keySet().iterator()));
			assertEquals(new ReferenceOpenHashSet<>(new Object[] { three, two, three }), new ReferenceOpenHashSet<>(m.values().iterator()));

			for(final Entry<Object, Object> e: m.reference2ReferenceEntrySet()) assertEquals(e.getValue(), m.get(e.getKey()));

			assertTrue(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(one, three)));
			assertFalse(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(one, new Integer(3))));
			assertFalse(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(new Integer(1), three)));
			assertTrue(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(two, two)));
			assertFalse(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(one, two)));
			assertFalse(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(two, one)));
			assertTrue(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(three, three)));
			assertFalse(m.reference2ReferenceEntrySet().contains(new AbstractReference2ReferenceMap.BasicEntry<Object,Object>(new Integer(3), two)));

			assertEquals(three, m.remove(three));
			assertEquals(2, m.size());
			assertEquals(three, m.remove(one));
			assertEquals(1, m.size());
			assertFalse(m.containsKey(one));
			assertEquals(two, m.remove(two));
			assertEquals(0, m.size());
			assertFalse(m.containsKey(one));
		}
	}

	@Test
	public void testClone() {
		final Reference2ReferenceArrayMap<Integer, Integer> m = new Reference2ReferenceArrayMap<>();
		assertEquals(m, m.clone());
		m.put(new Integer(0), new Integer(1));
		assertEquals(m, m.clone());
		m.put(new Integer(0), new Integer(2));
		assertEquals(m, m.clone());
		Integer one;
		m.put(one = new Integer(1), new Integer(2));
		assertEquals(m, m.clone());
		m.remove(one);
		assertEquals(m, m.clone());
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		// We can't really test reference maps as equals() doesnt' work
		final Object2ObjectArrayMap<Integer, Integer> m = new Object2ObjectArrayMap<>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(m);
		oos.close();
		assertEquals(m, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));

		m.put(new Integer(0), new Integer(1));
		m.put(new Integer(1), new Integer(2));

		baos.reset();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(m);
		oos.close();
		assertEquals(m, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));
	}
}

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

import org.junit.Test;

import it.unimi.dsi.fastutil.io.BinIO;

@SuppressWarnings("deprecation")
public class ReferenceArraySetTest {

	@Test
	public void testSet() {
		for(int i = 0; i <= 2; i++) {
			final ReferenceArraySet<Object> s = i == 0 ? new ReferenceArraySet<>() : new ReferenceArraySet<>(2);
			final Integer one = new Integer(1), two = new Integer(2), three = new Integer(3);
			assertTrue(s.add(one));
			assertEquals(1, s.size());
			assertTrue(s.contains(one));
			assertFalse(s.contains(new Integer(1)));
			assertTrue(s.add(two));
			assertTrue(s.contains(two));
			assertFalse(s.contains(new Integer(2)));
			assertEquals(2, s.size());
			assertFalse(s.add(one));
			assertFalse(s.remove(three));
			assertTrue(s.add(three));
			assertEquals(3, s.size());
			assertTrue(s.contains(one));
			assertTrue(s.contains(two));
			assertTrue(s.contains(three));
			assertEquals(new ReferenceOpenHashSet<>(new Object[] { one, two, three }), new ReferenceOpenHashSet<>(s.iterator()));
			assertTrue(s.remove(three));
			assertEquals(2, s.size());
			assertTrue(s.remove(one));
			assertEquals(1, s.size());
			assertFalse(s.contains(one));
			assertTrue(s.remove(two));
			assertEquals(0, s.size());
			assertFalse(s.contains(one));
		}
	}

	@Test
	public void testClone() {
		final ReferenceArraySet<Integer> s = new ReferenceArraySet<>();
		assertEquals(s, s.clone());
		Integer zero;
		s.add(zero = new Integer(0));
		assertEquals(s, s.clone());
		s.add(new Integer(0));
		assertEquals(s, s.clone());
		s.add(new Integer(1));
		assertEquals(s, s.clone());
		s.add(new Integer(2));
		assertEquals(s, s.clone());
		s.remove(zero);
		assertEquals(s, s.clone());
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		// We can't really test reference maps as equals() doesnt' work
		final ObjectArraySet<Integer> s = new ObjectArraySet<>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(s);
		oos.close();
		assertEquals(s, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));

		s.add(new Integer(0));
		s.add(new Integer(1));

		baos.reset();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(s);
		oos.close();
		assertEquals(s, BinIO.loadObject(new ByteArrayInputStream(baos.toByteArray())));
	}
}

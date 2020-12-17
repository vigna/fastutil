/*
 * Copyright (C) 2017 Sebastiano Vigna
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
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.bytes.ByteArrays;

public class ObjectOpenCustomHashSetTest {

	@Test
	public void testGetNullKey() {
		final ObjectOpenCustomHashSet<Integer> s = new ObjectOpenCustomHashSet<>(new Hash.Strategy<Integer>() {

			@Override
			public int hashCode(Integer o) {
				return o == null ? 0 : o.intValue() % 10;
			}

			@Override
			public boolean equals(Integer a, Integer b) {
				if (((a == null) != (b == null)) || a == null) return false;
				return ((a.intValue() - b.intValue()) % 10) == 0;
			}
		});

		s.add(Integer.valueOf(10));
		assertTrue(s.contains(Integer.valueOf(0)));
		assertEquals(10, s.iterator().next().intValue());
	}

	@Test
	public void testNullKey() {
		Random random = new Random(0);
		ObjectOpenCustomHashSet<byte[]> s = new ObjectOpenCustomHashSet<>(ByteArrays.HASH_STRATEGY);
		for(int i = 0; i < 1000000; i++) {
			byte[] a = new byte[random.nextInt(10)];
			for(int j = a.length; j-- != 0;) a[j] = (byte) random.nextInt(4);
			s.add(a);
		}
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectOpenCustomHashSet.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"3834745");
	}
}

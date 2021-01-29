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

package it.unimi.dsi.fastutil.longs;


import static it.unimi.dsi.fastutil.BigArrays.get;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.MainRunner;

public class LongOpenHashBigSetTest {

	@Test
	public void testRemove0() {
		LongOpenHashBigSet s = new LongOpenHashBigSet(Hash.DEFAULT_INITIAL_SIZE);
		for(int i = -1; i <= 1; i++) assertTrue(s.add(i));
		assertTrue(s.remove(0));
		LongIterator iterator = s.iterator();
		final LongOpenHashSet z = new LongOpenHashSet();
		z.add(iterator.nextLong());
		z.add(iterator.nextLong());
		assertFalse(iterator.hasNext());
		assertEquals(new LongOpenHashSet(new long[] { -1, 1 }), z);

		s = new LongOpenHashBigSet(Hash.DEFAULT_INITIAL_SIZE);
		for(int i = -1; i <= 1; i++) assertTrue(s.add(i));
		iterator = s.iterator();
		while(iterator.hasNext()) if (iterator.nextLong() == 0) iterator.remove();

		assertFalse(s.contains(0));

		iterator = s.iterator();
		final long[] content = new long[2];
		content[0] = iterator.nextLong();
		content[1] = iterator.nextLong();
		assertFalse(iterator.hasNext());
		Arrays.sort(content);
		assertArrayEquals(new long[] { -1, 1 }, content);
	}

	@Test
	public void testWrapAround() {
		final LongOpenHashBigSet s = new LongOpenHashBigSet(4, .5f);
		assertEquals(8, s.n);
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 6, 7 and 0
		s.add(HashCommon.invMix(6L));
		s.add(HashCommon.invMix(7L));
		s.add(HashCommon.invMix(6L + 8));
		assertNotEquals(0, get(s.key, 0));
		assertNotEquals(0, get(s.key, 6));
		assertNotEquals(0, get(s.key, 7));
		final LongOpenHashBigSet keys = s.clone();
		final LongIterator iterator = s.iterator();
		final LongOpenHashBigSet t = new LongOpenHashBigSet();
		t.add(iterator.nextLong());
		t.add(iterator.nextLong());
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		iterator.remove();
		t.add(iterator.nextLong());
		assertEquals(keys, t);
	}

	@Test
	public void testWrapAround2() {
		final LongOpenHashBigSet s = new LongOpenHashBigSet(4, .75f);
		assertEquals(8, s.n);
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 4, 5, 6, 7 and 0
		s.add(HashCommon.invMix(4L));
		s.add(HashCommon.invMix(5L));
		s.add(HashCommon.invMix(4L + 8));
		s.add(HashCommon.invMix(5L + 8));
		s.add(HashCommon.invMix(4L + 16));
		assertNotEquals(0, get(s.key, 0));
		assertNotEquals(0, get(s.key, 4));
		assertNotEquals(0, get(s.key, 5));
		assertNotEquals(0, get(s.key, 6));
		assertNotEquals(0, get(s.key, 7));
		//System.err.println(Arrays.toString(s.key[0]));
		final LongOpenHashBigSet keys = s.clone();
		final LongIterator iterator = s.iterator();
		final LongOpenHashBigSet t = new LongOpenHashBigSet();
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		//System.err.println(Arrays.toString(s.key[0]));
		assertTrue(t.add(iterator.nextLong()));
		//System.err.println(Arrays.toString(s.key[0]));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue(t.add(iterator.nextLong()));
		//System.err.println(Arrays.toString(s.key[0]));
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		//System.err.println(Arrays.toString(s.key[0]));
		assertTrue(t.add(iterator.nextLong()));
		assertEquals(3, s.size64());
		assertEquals(keys, t);
	}

	@Test
	public void testWrapAround3() {
		final LongOpenHashBigSet s = new LongOpenHashBigSet(4, .75f);
		assertEquals(8, s.n);
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 5, 6, 7, 0 and 1
		s.add(HashCommon.invMix(5L));
		s.add(HashCommon.invMix(5L + 8));
		s.add(HashCommon.invMix(5L + 16));
		s.add(HashCommon.invMix(5L + 32));
		s.add(HashCommon.invMix(5L + 64));
		assertNotEquals(0, get(s.key, 5));
		assertNotEquals(0, get(s.key, 6));
		assertNotEquals(0, get(s.key, 7));
		assertNotEquals(0, get(s.key, 0));
		assertNotEquals(0, get(s.key, 1));
		//System.err.println(Arrays.toString(s.key[0]));
		final LongOpenHashBigSet keys = s.clone();
		final LongIterator iterator = s.iterator();
		final LongOpenHashBigSet t = new LongOpenHashBigSet();
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		//System.err.println(Arrays.toString(s.key[0]));
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		//System.err.println(Arrays.toString(s.key[0]));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		//System.err.println(Arrays.toString(s.key[0]));
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		//System.err.println(Arrays.toString(s.key[0]));
		assertTrue(t.add(iterator.nextLong()));
		iterator.remove();
		assertEquals(0, s.size64());
		assertEquals(keys, t);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(LongOpenHashBigSet.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"383474");
	}
}

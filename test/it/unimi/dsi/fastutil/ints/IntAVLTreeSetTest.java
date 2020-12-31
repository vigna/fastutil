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

package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class IntAVLTreeSetTest {

	@Test
	public void testGet() {
		final IntAVLTreeSet s = new IntAVLTreeSet();
		final int i = 0;
		assertTrue(s.isEmpty());
		assertEquals(0, s.size());
		s.add(i);
		assertTrue(s.contains(i));
		assertFalse(s.add(i));
		assertEquals(1, s.size());
	}
	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntAVLTreeSet.class, "test", /*num=*/"20", /*seed=*/"423429");
	}
}

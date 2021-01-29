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

package it.unimi.dsi.fastutil.floats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class FloatOpenHashSetTest {

	@Test
	public void testNaNs() {
		final FloatOpenHashSet s = new FloatOpenHashSet();
		s.add(Float.NaN);
		s.add(Float.NaN);
		assertEquals(1, s.size());
	}

	@Test
	public void testZeros() {
		final FloatOpenHashSet s = new FloatOpenHashSet();
		assertTrue(s.add(-0.0f));
		assertTrue(s.add(+0.0f));
		assertEquals(2, s.size());
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(FloatOpenHashSet.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"3838474");
	}
}

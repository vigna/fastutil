package it.unimi.dsi.fastutil.floats;

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

public class FloatOpenHashSetTest {

	@Test
	public void testNaNs() {
		FloatOpenHashSet s = new FloatOpenHashSet();
		s.add(Float.NaN);
		s.add(Float.NaN);
		assertEquals(1, s.size());
	}

	@Test
	public void testZeros() {
		FloatOpenHashSet s = new FloatOpenHashSet();
		assertTrue(s.add(-0.0f));
		assertTrue(s.add(+0.0f));
		assertEquals(2, s.size());
	}
}

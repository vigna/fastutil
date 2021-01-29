/*
 * Copyright (C) 2019-2021 Sebastiano Vigna
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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Object2IntArrayMapTest {

	@Test
	public void testIterator() {
		final Object2IntArrayMap<String> testMap = new Object2IntArrayMap<>();

		testMap.put("A", 10);
		testMap.put("B", 10);
		final ObjectIterator<String> iterator = testMap.keySet().iterator();
		iterator.next();
		iterator.remove();
		assertTrue(iterator.hasNext());
	}
}


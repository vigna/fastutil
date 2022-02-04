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
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Object2ObjectOpenHashMapTest {


	@Test
	public void testCombinationMethodsWithDefaultValue() {
		final Object2ObjectOpenHashMap<String, String> map = new Object2ObjectOpenHashMap<>();
		map.defaultReturnValue("undefined");
		assertEquals("nak", map.getOrDefault("bar", "nak"));
		map.putIfAbsent("foo", "one");
		assertEquals("one", map.get("foo"));

		assertEquals("undefined", map.replace("bar", "two"));
		assertFalse(map.containsKey("bar"));

		assertEquals("three", map.computeIfAbsent("qux", s -> "three"));
		assertEquals("three", map.get("qux"));

		assertEquals("undefined", map.computeIfPresent("def", (a, b) -> "four"));
		assertFalse(map.containsKey("def"));
	}

	@Test
	public void testCombinationMethodsWithoutDefaultValue() {
		final Object2ObjectOpenHashMap<String, String> map = new Object2ObjectOpenHashMap<>();
		assertEquals("nak", map.getOrDefault("bar", "nak"));
		map.putIfAbsent("foo", "one");
		assertEquals("one", map.get("foo"));

		assertNull(map.replace("bar", "two"));
		assertFalse(map.containsKey("bar"));

		assertEquals("three", map.computeIfAbsent("qux", s -> "three"));
		assertEquals("three", map.get("qux"));

		assertNull(map.computeIfPresent("def", (a, b) -> "four"));
		assertFalse(map.containsKey("def"));
	}
}

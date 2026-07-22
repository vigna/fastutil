/*
 * Copyright (C) 2002-2026 Sebastiano Vigna
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractObject2ObjectMapTest {

	/** A minimal map that implements only the entry set, so that it relies on the
	 * {@code containsKey}/{@code containsValue} fallbacks of the abstract class. */
	private static Object2ObjectMap<String, String> singletonEntryMap(final String key, final String value) {
		final ObjectSet<Object2ObjectMap.Entry<String, String>> entries = new ObjectOpenHashSet<>();
		entries.add(new AbstractObject2ObjectMap.BasicEntry<>(key, value));
		return new AbstractObject2ObjectMap<String, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public int size() {
				return entries.size();
			}

			@Override
			public String get(final Object k) {
				return null;
			}

			@Override
			public ObjectSet<Object2ObjectMap.Entry<String, String>> object2ObjectEntrySet() {
				return entries;
			}
		};
	}

	@Test
	public void testFallbackContainsUsesEquality() {
		final Object2ObjectMap<String, String> m = singletonEntryMap("a", "b");
		// The fallbacks must use equals(), not reference identity.
		assertTrue(m.containsKey(new String("a")));
		assertTrue(m.containsValue(new String("b")));
		assertFalse(m.containsKey("x"));
		assertFalse(m.containsValue("y"));
	}
}

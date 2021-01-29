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

package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

public class AbstractInt2IntMapTest {
	@Test
	public void testContainsKeyEmptySet() {
		Int2IntMap m = new AbstractInt2IntMap() {
			private static final long serialVersionUID = 0L;

			@Override
			public int size() {
				return 0;
			}

			@Override
			public int get(int key) {
				return -1;
			}

			@SuppressWarnings("unchecked")
			@Override
			public ObjectSet<Entry> int2IntEntrySet() {
				return ObjectSets.EMPTY_SET;
			}
		};

		assertFalse(m.containsKey(0));
	}

	@Test
	public void testContainsKeySingleton() {
		Int2IntMap m = new AbstractInt2IntMap() {
			private static final long serialVersionUID = 0L;

			@Override
			public int size() {
				return 1;
			}

			@Override
			public int get(int key) {
				return key == 0 ? 0 : -1;
			}

			@Override
			public ObjectSet<Entry> int2IntEntrySet() {
				return ObjectSets.singleton((Entry)new AbstractInt2IntMap.BasicEntry(0,0));
			}
		};

		assertTrue(m.containsKey(0));
		assertFalse(m.containsKey(1));
	}
}

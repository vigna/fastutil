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

import java.util.Collections;
import java.util.EnumSet;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

public class AbstractInt2IntMapTest extends Int2IntMapGenericTest<AbstractInt2IntMap> {

	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] {(Supplier<Int2IntMap>) SimpleInt2IntMap::new, EnumSet.allOf(Capability.class)});
	}

	// A basic implementation of AbstractInt2IntMap that contains as little methods implemented
	// possible to make a mutable map, so we can test as many of the AbstractInt2IntMap default
	// implementations as possible.
	static final class SimpleInt2IntMap extends AbstractInt2IntMap implements Int2IntMap {
		private static final long serialVersionUID = 1L;

		private final IntList keys = new IntArrayList();
		private final IntList values = new IntArrayList();
		@Override
		public int get(int key) {
			int index = keys.indexOf(key);
			if (index == -1) {
				return defaultReturnValue();
			}
			return values.getInt(index);
		}
		@Override
		public int size() {
			return keys.size();
		}
		@Override
		public int put(int key, int value) {
			int index = keys.indexOf(key);
			if (index == -1) {
				keys.add(key);
				values.add(value);
				return defaultReturnValue();
			}
			return values.set(index, value);
		}
		@Override
		public int remove(int key) {
			int index = keys.indexOf(key);
			if (index == -1) {
				return defaultReturnValue();
			}
			keys.removeInt(index);
			return values.removeInt(index);
		}
		@Override
		public void clear() {
			keys.clear();
			values.clear();
		}

		@Override
		public ObjectSet<Entry> int2IntEntrySet() {
			return new AbstractInt2IntMap.BasicEntrySet(this) {
				@Override
				public ObjectIterator<Entry> iterator() {
					return new ObjectIterator<Entry>() {
					final IntIterator keyIter = keys.iterator();
					final IntIterator valueIter = values.iterator();
					
					@Override
					public boolean hasNext() {
						return keyIter.hasNext();
					}

						@Override
						public Entry next() {
							return new AbstractInt2IntMap.BasicEntry(keyIter.nextInt(), valueIter.nextInt());
						}

					@Override
					public void remove() {
						keyIter.remove();
						valueIter.remove();
					}
					};
				}
			};
		}
	}

	@Test
	public void testContainsKeyEmptySet() {
		m = new AbstractInt2IntMap() {
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
		m = new AbstractInt2IntMap() {
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

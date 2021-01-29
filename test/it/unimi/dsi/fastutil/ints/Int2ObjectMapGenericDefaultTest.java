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

import it.unimi.dsi.fastutil.ints.Int2ObjectMapGenericDefaultTest.SimpleInt2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericDefaultTest extends Int2ObjectMapGenericTest<SimpleInt2ObjectMap<Integer>> {
	@Parameters
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		capabilities.remove(Capability.ITERATOR_MODIFY);
		capabilities.remove(Capability.KEY_SET_MODIFY);
		return Collections.singletonList(new Object[] {
				(Supplier<Int2ObjectMap<Integer>>) () -> new SimpleInt2ObjectMap<>(new Int2ObjectArrayMap<>()), capabilities
		});
	}

	static final class SimpleInt2ObjectMap<V> implements Int2ObjectMap<V> {
		private final Int2ObjectMap<V> delegate;

		SimpleInt2ObjectMap(final Int2ObjectMap<V> delegate) {
			this.delegate = delegate;
		}

		@Override
		public void clear() {
			delegate.clear();
		}

		@Override
		public boolean containsKey(final int key) {
			return delegate.containsKey(key);
		}

		@Override
		public boolean containsValue(final Object value) {
			return delegate.containsValue(value);
		}

		@Override
		public void defaultReturnValue(final V rv) {
			delegate.defaultReturnValue(rv);
		}

		@Override
		public V defaultReturnValue() {
			return delegate.defaultReturnValue();
		}

		@Override
		public V get(final int key) {
			return delegate.get(key);
		}

		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
			return delegate.int2ObjectEntrySet();
		}

		@Override
		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		@Override
		public IntSet keySet() {
			return delegate.keySet();
		}

		@Override
		public V put(final int key, final V value) {
			return delegate.put(key, value);
		}

		@Override
		public void putAll(final Map<? extends Integer, ? extends V> m) {
			delegate.putAll(m);
		}

		@Override
		public V remove(final int key) {
			return delegate.remove(key);
		}

		@Override
		public int size() {
			return delegate.size();
		}

		@Override
		public ObjectCollection<V> values() {
			return delegate.values();
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			return delegate.equals(o);
		}

		@Override
		public int hashCode() {
			return delegate.hashCode();
		}
	}
}
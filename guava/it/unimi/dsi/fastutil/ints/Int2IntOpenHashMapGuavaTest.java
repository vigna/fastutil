/*
 * Copyright (C) 2017-2022 Sebastiano Vigna
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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionToStringTester;
import com.google.common.collect.testing.testers.MapToStringTester;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Int2IntOpenHashMapGuavaTest extends TestCase {

	public static TestSuite suite() {
		return suite("Int2IntOpenHashMap", Int2IntOpenHashMap::new);
	}

	public static TestSuite suite(final String name, final Supplier<Map<Integer, Integer>> factory) {
		final HashSet<Method> toStringTests = new HashSet<>();
		toStringTests.addAll(Arrays.asList(MapToStringTester.class.getDeclaredMethods()));
		toStringTests.addAll(Arrays.asList(CollectionToStringTester.class.getDeclaredMethods()));

		return MapTestSuiteBuilder.using(new TestMapGenerator<Integer, Integer>() {

			@Override
			public SampleElements<Entry<Integer, Integer>> samples() {
				return new SampleElements<>(Helpers.mapEntry(Integer.valueOf(0), Integer.valueOf(1)),
						Helpers.mapEntry(Integer.valueOf(-2), Integer.valueOf(4)),
						Helpers.mapEntry(Integer.valueOf(100), Integer.valueOf(-1)),
						Helpers.mapEntry(Integer.valueOf(2), Integer.valueOf(1000)),
						Helpers.mapEntry(Integer.valueOf(1), Integer.valueOf(2)));
			}

			@Override
			public Map<Integer, Integer> create(final Object... entries) {
				@SuppressWarnings("unchecked")
				final var map = factory.get();
				for (final Object o : entries) {
					@SuppressWarnings("unchecked")
					final Entry<Integer, Integer> e = (Entry<Integer, Integer>)o;
					map.put(e.getKey(), e.getValue());
				}
				return map;
			}

			@Override
			@SuppressWarnings("unchecked")
			public final Map.Entry<Integer, Integer>[] createArray(final int length) {
				return new Map.Entry[length];
			}

			@Override
			public final Integer[] createKeyArray(final int length) {
				return new Integer[length];
			}

			@Override
			public final Integer[] createValueArray(final int length) {
				return new Integer[length];
			}

			/** Returns the original element list, unchanged. */
			@Override
			public Iterable<Entry<Integer, Integer>> order(final List<Entry<Integer, Integer>> insertionOrder) {
				return insertionOrder;
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.GENERAL_PURPOSE, MapFeature.SUPPORTS_PUT, MapFeature.SUPPORTS_REMOVE, MapFeature.ALLOWS_ANY_NULL_QUERIES, CollectionFeature.SUPPORTS_ITERATOR_REMOVE).suppressing(toStringTests).createTestSuite();
	}
}

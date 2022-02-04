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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.TestStringMapGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionToStringTester;
import com.google.common.collect.testing.testers.MapComputeIfPresentTester;
import com.google.common.collect.testing.testers.MapMergeTester;
import com.google.common.collect.testing.testers.MapToStringTester;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Object2ObjectOpenHashMapGuavaTest extends TestCase {

	public static TestSuite suite() throws NoSuchMethodException, SecurityException {
		return suite("Object2ObjectOpenHashMap", Object2ObjectOpenHashMap::new);
	}

	public static TestSuite suite(final String name, final Supplier<Map<String, String>> factory) throws NoSuchMethodException, SecurityException {
		final HashSet<Method> suppressed = new HashSet<>();
		suppressed.addAll(Arrays.asList(MapToStringTester.class.getDeclaredMethods()));
		suppressed.addAll(Arrays.asList(CollectionToStringTester.class.getDeclaredMethods()));
		// An @apiNote explains that our semantics is slightly different
		suppressed.add(MapMergeTester.class.getDeclaredMethod("testMappedToNull"));
		suppressed.add(MapComputeIfPresentTester.class.getDeclaredMethod("testComputeIfPresent_nullTreatedAsAbsent"));

		return MapTestSuiteBuilder.using(new TestStringMapGenerator() {

			@Override
			protected Map<String, String> create(final Map.Entry<String, String>[] entries) {
				final var map = factory.get();
				for (final var entry : entries) {
					map.put(entry.getKey(), entry.getValue());
				}
				return map;
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.GENERAL_PURPOSE, MapFeature.SUPPORTS_PUT, MapFeature.SUPPORTS_REMOVE, MapFeature.ALLOWS_NULL_KEYS, MapFeature.ALLOWS_NULL_VALUES, MapFeature.ALLOWS_ANY_NULL_QUERIES, CollectionFeature.SUPPORTS_ITERATOR_REMOVE).suppressing(suppressed).createTestSuite();
	}
}

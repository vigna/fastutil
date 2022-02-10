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

package it.unimi.dsi.fastutil.objects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.testers.CollectionToStringTester;
import com.google.common.collect.testing.testers.MapToStringTester;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ObjectOpenHashSetGuavaTest extends TestCase {

	public static TestSuite suite() {
		return suite("ObjectOpenHashSet", ObjectOpenHashSet::new);
	}

	public static TestSuite suite(final String name, final Supplier<Set<String>> factory) {
		final HashSet<Method> toStringTests = new HashSet<>();
		toStringTests.addAll(Arrays.asList(MapToStringTester.class.getDeclaredMethods()));
		toStringTests.addAll(Arrays.asList(CollectionToStringTester.class.getDeclaredMethods()));

		return SetTestSuiteBuilder.using(new TestStringSetGenerator() {

			@Override
			protected Set<String> create(final String[] elements) {
				final Set<String> set = factory.get();
				for (final String e : elements) {
					set.add(e);
				}
				return set;
			}
		}).named(name).withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, CollectionFeature.ALLOWS_NULL_QUERIES, CollectionFeature.RESTRICTS_ELEMENTS, CollectionFeature.SUPPORTS_ADD, CollectionFeature.SUPPORTS_REMOVE, CollectionFeature.SERIALIZABLE, CollectionFeature.REMOVE_OPERATIONS, CollectionFeature.SUPPORTS_ITERATOR_REMOVE).suppressing(toStringTests).createTestSuite();
	}
}

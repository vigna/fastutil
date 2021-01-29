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

import it.unimi.dsi.fastutil.Hash;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericOpenHashTest extends Int2ObjectMapGenericTest<Int2ObjectOpenHashMap<Integer>> {
	@Parameter(2)
	public float loadFactor;

	@SuppressWarnings({"AutoBoxing", "boxing"})
	@Parameters(name = "{index}: lf {2}")
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		final int defSize = Int2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE;
		final Collection<Object[]> data = new ArrayList<>();
		for (final float loadFactor : new float[] {Hash.DEFAULT_LOAD_FACTOR, Hash.FAST_LOAD_FACTOR, Hash.VERY_FAST_LOAD_FACTOR}) {
			data.add(new Object[] {(Supplier<Int2ObjectMap<Integer>>) () -> new Int2ObjectOpenHashMap<>(defSize, loadFactor), capabilities, loadFactor});
		}
		return data;
	}
}
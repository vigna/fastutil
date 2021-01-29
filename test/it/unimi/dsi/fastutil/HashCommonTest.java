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

package it.unimi.dsi.fastutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HashCommonTest {
	@Test
	public void testMaxFillSmall() {
		for(final float f: new float[] { 0.0001f, .25f, .50f, .75f, .9999f }) {
			for(int i = 0; i < 16; i++) {
				final int n = HashCommon.arraySize(i, f);
				final int maxFill = HashCommon.maxFill(n, f);
				assertTrue(n + " <= " + maxFill, n > maxFill);
			}

			for(long i = 0; i < 16; i++) {
				final long n = HashCommon.bigArraySize(i, f);
				final long maxFill = HashCommon.maxFill(n, f);
				assertTrue(n + " <= " + maxFill, n > maxFill);
			}
		}
	}

	@Test
	public void testInverses() {
		for(int i = 0 ; i < 1 << 30; i += 10000) {
			assertEquals(i, HashCommon.invMix(HashCommon.mix(i)));
			assertEquals(i, HashCommon.mix(HashCommon.invMix(i)));
		}
		for(long i = 0 ; i < 1 << 62; i += 1000000) {
			assertEquals(i, HashCommon.invMix(HashCommon.mix(i)));
			assertEquals(i, HashCommon.mix(HashCommon.invMix(i)));
		}
	}
}

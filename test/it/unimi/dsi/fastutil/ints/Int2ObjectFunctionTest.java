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

import static org.junit.Assert.assertSame;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class Int2ObjectFunctionTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultGenericMethods() {
		final Integer zero = Integer.valueOf(0);

		@SuppressWarnings("serial")
		final Int2ObjectFunction<Object> f = new AbstractInt2ObjectFunction<Object>() {
			@Override
			public Object get(final int key) {
				return key == 0 ? zero : defRetValue;
			}
		};

		final Object drv = new Object();
		f.defaultReturnValue(drv);
		assertSame(Integer.valueOf(0), f.get(0));
		assertSame(drv, f.get(1));
		assertSame(drv, f.get(Integer.valueOf(1)));
		assertSame(zero, f.get(0));
		assertSame(zero, f.get(Integer.valueOf(0)));
	}
}

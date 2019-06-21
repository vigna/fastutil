package it.unimi.dsi.fastutil;

/*
 * Copyright (C) 2017 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HashStrategyTest {
	@Test
	public void testIdentityStrategy_equals() {
		final Object to = new Object();
		assertTrue(Hash.Strategy.identity().equals(to, to));
		// AlwaysEqual's equals method returns true, but our hash strategy should not
		assertFalse(Hash.Strategy.identity().equals(new AlwaysEqual(), new AlwaysEqual()));
	}

	@Test
	public void testIdentityStrategy_hashCode() {
		final Object to = new Object();
		// We shouldn't be getting the value of AlwaysEqual's hashCode method here
		assertNotEquals(42, Hash.Strategy.identity().hashCode(new AlwaysEqual()));
		assertEquals(System.identityHashCode(to), Hash.Strategy.identity().hashCode(to));
	}

	private static final class AlwaysEqual {
		@Override
		public int hashCode() {
			return 42;
		}

		@Override
		public boolean equals(final Object that) {
			return that instanceof AlwaysEqual;
		}
	}
}

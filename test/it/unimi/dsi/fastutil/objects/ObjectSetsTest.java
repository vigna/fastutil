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


import static org.junit.Assert.assertNull;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class ObjectSetsTest {
	@Test
	public void testToArrayShouldNullElementAfterLastEntry() {
		ObjectSet<?> set = ObjectSets.EMPTY_SET;
		Object[] values = new Object[] { "test" };
		set.toArray(values);
		assertNull(values[0]);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectSets.class, "Object", /*seed=*/"928374");
	}
}

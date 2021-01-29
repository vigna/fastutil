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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.io.BinIO;

public class ObjectHeapPriorityQueueTest {

	@SuppressWarnings({ "unchecked", "boxing" })
	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		ObjectHeapPriorityQueue<Integer> q = new ObjectHeapPriorityQueue<>();
		for(int i = 0; i < 100; i++) q.enqueue(i);

		File file = File.createTempFile(getClass().getPackage().getName() + "-", "-tmp");
		file.deleteOnExit();
		BinIO.storeObject(q, file);
		ObjectHeapPriorityQueue<Integer> r = (ObjectHeapPriorityQueue<Integer>)BinIO.loadObject(file);
		file.delete();
		for(int i = 0; i < 100; i++) {
			assertEquals(q.first(), r.first());
			assertEquals(q.dequeue(), r.dequeue());
		}
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectHeapPriorityQueue.class, "test", /*num=*/"500", /*seed=*/"9329384");
	}
}


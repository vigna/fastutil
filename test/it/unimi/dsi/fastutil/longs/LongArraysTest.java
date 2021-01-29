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

package it.unimi.dsi.fastutil.longs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class LongArraysTest {

	private static long[] identity(int n) {
		final long[] a = new long[n];
		while(n-- != 0) a[n] = n;
		return a;
	}

	@Test
	public void testRadixSort1() {
		long[] t = { 2, 1, 0, 4 };
		LongArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new long[] { 2, -1, 0, -4 };
		LongArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = LongArrays.shuffle(identity(100), new Random(0));
		LongArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new long[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextLong();
		LongArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new long[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextLong();
		LongArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new long[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextLong();
		LongArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testRadixSort2() {
		long[][] d = new long[2][];

		d[0] = new long[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = LongArrays.shuffle(identity(10), new Random(0));
		LongArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new long[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = LongArrays.shuffle(identity(100000), new Random(6));
		LongArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new long[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new long[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextLong();
		LongArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new long[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextLong();
		d[1] = new long[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextLong();
		LongArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

	}

	@Test
	public void testRadixSort() {
		long[][] t = { { 2, 1, 0, 4 } };
		LongArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		t[0] = LongArrays.shuffle(identity(100), new Random(0));
		LongArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		long[][] d = new long[2][];

		d[0] = new long[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = LongArrays.shuffle(identity(10), new Random(0));
		LongArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new long[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = LongArrays.shuffle(identity(100000), new Random(6));
		LongArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new long[10];
		Random random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextLong();
		d[1] = new long[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextLong();
		LongArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new long[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextLong();
		d[1] = new long[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextLong();
		LongArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

	}

	@Test
	public void testStabilize() {
		int[] perm;
		long[] val;

		perm = new int[] { 0, 1, 2, 3 };
		val = new long[] { 0, 0, 0, 0 };

		LongArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, perm);

		perm = new int[] { 3, 1, 2, 0 };
		val = new long[] { 0, 0, 0, 0 };

		LongArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, perm);

		perm = new int[] { 3, 2, 1, 0 };
		val = new long[] { 0, 1, 1, 2 };

		LongArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 3, 1, 2, 0 }, perm);

		perm = new int[] { 3, 2, 1, 0 };
		val = new long[] { 0, 0, 1, 1 };

		LongArrays.stabilize(perm, val);
		assertArrayEquals(new int[] { 2, 3, 0, 1 }, perm);

		perm = new int[] { 4, 3, 2, 1, 0 };
		val = new long[] { 1, 1, 0, 0, 0 };

		LongArrays.stabilize(perm, val, 1, 3);
		assertArrayEquals(new int[] { 4, 2, 3, 1, 0 }, perm);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(LongArrays.class, "test", /*num=*/"1000", /*seed=*/"848747");
	}
}

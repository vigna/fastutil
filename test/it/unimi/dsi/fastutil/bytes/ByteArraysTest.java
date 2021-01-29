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

package it.unimi.dsi.fastutil.bytes;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class ByteArraysTest {

	private static byte[] castIdentity(int n) {
		final byte[] a = new byte[n];
		while(n-- != 0) a[n] = (byte)n;
		return a;
	}


	@Test
	public void testRadixSort1() {
		byte[] t = { 2, 1, 0, 4 };
		ByteArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new byte[] { 2, (byte)-1, 0, (byte)-4 };
		ByteArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = ByteArrays.shuffle(castIdentity(100), new Random(0));
		ByteArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new byte[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = (byte)random.nextInt();
		ByteArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new byte[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = (byte)random.nextInt();
		ByteArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new byte[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = (byte)random.nextInt();
		ByteArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testRadixSort2() {
		final byte[][] d = new byte[2][];

		d[0] = new byte[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)(3 - i % 3);
		d[1] = ByteArrays.shuffle(castIdentity(10), new Random(0));
		ByteArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new byte[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)(100 - i % 100);
		d[1] = ByteArrays.shuffle(castIdentity(100000), new Random(6));
		ByteArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new byte[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)(i % 3 - 2);
		Random random = new Random(0);
		d[1] = new byte[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = (byte)random.nextInt();
		ByteArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new byte[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)random.nextInt();
		d[1] = new byte[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = (byte)random.nextInt();
		ByteArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new byte[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)random.nextInt();
		d[1] = new byte[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = (byte)random.nextInt();
		ByteArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testRadixSort() {
		final byte[][] t = { { 2, 1, 0, 4 } };
		ByteArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		t[0] = ByteArrays.shuffle(castIdentity(100), new Random(0));
		ByteArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		final byte[][] d = new byte[2][];

		d[0] = new byte[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)(3 - i % 3);
		d[1] = ByteArrays.shuffle(castIdentity(10), new Random(0));
		ByteArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new byte[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)(100 - i % 100);
		d[1] = ByteArrays.shuffle(castIdentity(100000), new Random(6));
		ByteArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new byte[10];
		Random random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)random.nextInt();
		d[1] = new byte[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = (byte)random.nextInt();
		ByteArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new byte[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)random.nextInt();
		d[1] = new byte[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = (byte)random.nextInt();
		ByteArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new byte[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = (byte)random.nextInt();
		d[1] = new byte[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = (byte)random.nextInt();
		ByteArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	
	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ByteArrays.class, "test", /*num=*/"1000", /*seed=*/"848747");
	}
}

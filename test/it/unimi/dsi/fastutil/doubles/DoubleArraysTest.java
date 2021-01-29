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

package it.unimi.dsi.fastutil.doubles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.ints.IntArrays;

public class DoubleArraysTest {

	private static double[] identity(int n) {
		final double[] a = new double[n];
		while(n-- != 0) a[n] = n;
		return a;
	}

	private static int[] identityInt(int n) {
		final int[] a = new int[n];
		while(n-- != 0) a[n] = n;
		return a;
	}


	@Test
	public void testRadixSort1() {
		double[] t = { 2, 1, 0, 4 };
		DoubleArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new double[] { 2, -1, 0, -4 };
		DoubleArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = DoubleArrays.shuffle(identity(100), new Random(0));
		DoubleArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new double[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		DoubleArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new double[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		DoubleArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new double[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		DoubleArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testRadixSort2() {
		final double[][] d = new double[2][];

		d[0] = new double[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = DoubleArrays.shuffle(identity(10), new Random(0));
		DoubleArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new double[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = DoubleArrays.shuffle(identity(100000), new Random(6));
		DoubleArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new double[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new double[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		DoubleArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new double[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new double[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		DoubleArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new double[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new double[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		DoubleArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testRadixSort() {
		final double[][] t = { { 2, 1, 0, 4 } };
		DoubleArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		t[0] = DoubleArrays.shuffle(identity(100), new Random(0));
		DoubleArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		final double[][] d = new double[2][];

		d[0] = new double[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = DoubleArrays.shuffle(identity(10), new Random(0));
		DoubleArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new double[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = DoubleArrays.shuffle(identity(100000), new Random(6));
		DoubleArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new double[10];
		Random random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new double[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		DoubleArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new double[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new double[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		DoubleArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new double[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new double[d[0].length];
		for(int i = d[1].length; i-- != 0;) d[1][i] = random.nextInt();
		DoubleArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testRadixSortIndirectStable() {
		double[] d = { 2, 1, 0, 4 };
		int[] perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[d.length];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		d = new double[] { 2, -1, 0, -4 };
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = DoubleArrays.shuffle(identity(100), new Random(0));
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[100];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		Random random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[d.length];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		d = new double[d.length];
		for(int i = 0; i < d.length; i++) d[i] = random.nextInt(4);
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) if (d[perm[i]] == d[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);

		d = new double[100];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, 10, 90, true);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), d[perm[i]] <= d[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		d = new double[100000];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(Integer.toString(i), d[perm[i]] <= d[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(Integer.toString(i), d[perm[i]] <= d[perm[i + 1]]);

		d = new double[10000000];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[d.length];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		d = new double[d.length];
		for(int i = 0; i < d.length; i++) d[i] = random.nextInt(8);
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, true);
		for(int i = d.length - 1; i-- != 0;) if (d[perm[i]] == d[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);
	}

	@Test
	public void testRadixSortIndirectUnstable() {
		double[] d = { 2, 1, 0, 4 };
		int[] perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[d.length];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		d = new double[] { 2, -1, 0, -4 };
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = DoubleArrays.shuffle(identity(100), new Random(0));
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[100];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		Random random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[100];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, 10, 90, false);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), d[perm[i]] <= d[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		d = new double[100000];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(Integer.toString(i), d[perm[i]] <= d[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(Integer.toString(i), d[perm[i]] <= d[perm[i + 1]]);

		d = new double[10000000];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		random = new Random(0);
		for(int i = d.length; i-- != 0;) d[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertTrue(d[perm[i]] <= d[perm[i + 1]]);

		d = new double[d.length];
		perm = it.unimi.dsi.fastutil.ints.IntArraysTest.identity(d.length);
		DoubleArrays.radixSortIndirect(perm, d, false);
		for(int i = d.length - 1; i-- != 0;) assertEquals(i, perm[i]);
	}

	@Test
	public void testRadixSort2IndirectStable() {
		double[] t = { 2, 1, 0, 4 };
		double[] u = { 3, 2, 1, 0 };
		int[] perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(u[perm[i]] <= u[perm[i + 1]]);

		t = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, t, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = DoubleArrays.shuffle(identity(100), new Random(0));
		u = DoubleArrays.shuffle(identity(100), new Random(1));
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[100];
		u = new double[100];
		perm = identityInt(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[t.length];
		u = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		for(int i = 0; i < u.length; i++) t[i] = random.nextInt(4);
		for(int i = 0; i < u.length; i++) u[i] = random.nextInt(4);
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]] && u[perm[i]] == u[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);

		t = new double[100];
		u = new double[100];
		perm = identityInt(t.length);
		random = new Random(0);
		for(int i = u.length; i-- != 0;) u[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, 10, 90, true);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), u[perm[i]] <= u[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new double[100000];
		u = new double[100000];
		perm = identityInt(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[10000000];
		u = new double[10000000];
		perm = identityInt(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[t.length];
		u = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new double[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(8);
		for(int i = 0; i < t.length; i++) u[i] = random.nextInt(8);
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, true);
		for(int i = t.length - 1; i-- != 0;) if (t[perm[i]] == t[perm[i + 1]] && u[perm[i]] == u[perm[i + 1]]) assertTrue(perm[i] < perm[i + 1]);
	}

	@Test
	public void testRadixSort2IndirectUnstable() {
		double[] t = { 2, 1, 0, 4 };
		double[] u = { 3, 2, 1, 0 };
		int[] perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] <= t[perm[i + 1]]);

		t = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(u[perm[i]] <= u[perm[i + 1]]);

		t = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, t, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = DoubleArrays.shuffle(identity(100), new Random(0));
		u = DoubleArrays.shuffle(identity(100), new Random(1));
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[100];
		u = new double[100];
		perm = identityInt(t.length);
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[t.length];
		u = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		for(int i = 0; i < u.length; i++) t[i] = random.nextInt(4);
		for(int i = 0; i < u.length; i++) u[i] = random.nextInt(4);
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] ||  t[perm[i]] == t[perm[i + 1]]&& u[perm[i]] <= u[perm[i + 1]]);

		t = new double[100];
		u = new double[100];
		perm = identityInt(t.length);
		random = new Random(0);
		for(int i = u.length; i-- != 0;) u[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, 10, 90, false);
		for(int i = 10; i < 89; i++) assertTrue(Integer.toString(i), u[perm[i]] <= u[perm[i + 1]]);
		for(int i = 0; i < 10; i++) assertEquals(i, perm[i]);
		for(int i = 90; i < 100; i++) assertEquals(i, perm[i]);

		t = new double[100000];
		u = new double[100000];
		perm = identityInt(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		IntArrays.shuffle(perm, new Random(0));
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[10000000];
		u = new double[10000000];
		perm = identityInt(t.length);
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		for(int i = t.length; i-- != 0;) u[i] = random.nextInt();
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);

		t = new double[t.length];
		u = new double[t.length];
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertEquals(i, perm[i]);

		t = new double[t.length];
		for(int i = 0; i < t.length; i++) t[i] = random.nextInt(8);
		for(int i = 0; i < t.length; i++) u[i] = random.nextInt(8);
		perm = identityInt(t.length);
		DoubleArrays.radixSortIndirect(perm, t, u, false);
		for(int i = t.length - 1; i-- != 0;) assertTrue(i + " " +  t[perm[i]]+ " "+ t[perm[i+1]] + " " + u[perm[i]] + " " + u[perm[i+1]] + "  " + perm[i]+ " " +perm[i+1], t[perm[i]] < t[perm[i + 1]] || t[perm[i]] == t[perm[i + 1]] && u[perm[i]] <= u[perm[i + 1]]);
	}




	@Test
	public void testMergeSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final double[] a = t.clone();
				DoubleArrays.mergeSort(a, from, to);
				for(int i = to - 1; i-- != from;) assertTrue(Double.compare(a[i], a[i + 1]) <= 0);
			}

	}


	@Test
	public void testRadixSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final double[] a = t.clone();
				DoubleArrays.radixSort(a, from, to);
				for(int i = to - 1; i-- != from;) assertTrue(Double.compare(a[i], a[i + 1]) <= 0);
			}

	}

	@Test
	public void testRadixSortIndirectNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final int perm[] = new int[t.length];
				for(int i = perm.length; i-- != 0;) perm[i] = i;
				DoubleArrays.radixSortIndirect(perm, t, from, to, true);
				for(int i = to - 1; i-- != from;) assertTrue(Double.compare(t[perm[i]], t[perm[i + 1]]) <= 0);
			}

	}

	@Test
	public void testRadixSortIndirect2NaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final int perm[] = new int[t.length];
				for(int i = perm.length; i-- != 0;) perm[i] = i;
				DoubleArrays.radixSortIndirect(perm, t, t, from, to, true);
				for(int i = to - 1; i-- != from;) assertTrue(Double.compare(t[perm[i]], t[perm[i + 1]]) <= 0);
			}

	}

	@Test
	public void testQuickSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final double[] a = t.clone();
				DoubleArrays.quickSort(a, from, to);
				for(int i = to - 1; i-- != from;) assertTrue(Double.compare(a[i], a[i + 1]) <= 0);
			}

	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(DoubleArrays.class, "test", /*num=*/"1000", /*seed=*/"848747");
	}
}

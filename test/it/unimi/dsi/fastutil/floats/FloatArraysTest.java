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

package it.unimi.dsi.fastutil.floats;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.MainRunner;

public class FloatArraysTest {

	private static float[] identity(int n) {
		final float[] a = new float[n];
		while(n-- != 0) a[n] = n;
		return a;
	}

	@Test
	public void testRadixSort1() {
		float[] t = { 2, 1, 0, 4 };
		FloatArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new float[] { 2, -1, 0, -4 };
		FloatArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = FloatArrays.shuffle(identity(100), new Random(0));
		FloatArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new float[100];
		Random random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		FloatArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new float[100000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		FloatArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);

		t = new float[10000000];
		random = new Random(0);
		for(int i = t.length; i-- != 0;) t[i] = random.nextInt();
		FloatArrays.radixSort(t);
		for(int i = t.length - 1; i-- != 0;) assertTrue(t[i] <= t[i + 1]);
	}

	@Test
	public void testRadixSort2() {
		final float[][] d = new float[2][];

		d[0] = new float[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = FloatArrays.shuffle(identity(10), new Random(0));
		FloatArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new float[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = FloatArrays.shuffle(identity(100000), new Random(6));
		FloatArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new float[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = i % 3 - 2;
		Random random = new Random(0);
		d[1] = new float[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextInt();
		FloatArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new float[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new float[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextInt();
		FloatArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new float[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new float[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextInt();
		FloatArrays.radixSort(d[0], d[1]);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}

	@Test
	public void testRadixSort() {
		final float[][] t = { { 2, 1, 0, 4 } };
		FloatArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		t[0] = FloatArrays.shuffle(identity(100), new Random(0));
		FloatArrays.radixSort(t);
		for(int i = t[0].length - 1; i-- != 0;) assertTrue(t[0][i] <= t[0][i + 1]);

		final float[][] d = new float[2][];

		d[0] = new float[10];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 3 - i % 3;
		d[1] = FloatArrays.shuffle(identity(10), new Random(0));
		FloatArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new float[100000];
		for(int i = d[0].length; i-- != 0;) d[0][i] = 100 - i % 100;
		d[1] = FloatArrays.shuffle(identity(100000), new Random(6));
		FloatArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new float[10];
		Random random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new float[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextInt();
		FloatArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);


		d[0] = new float[100000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new float[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextInt();
		FloatArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);

		d[0] = new float[10000000];
		random = new Random(0);
		for(int i = d[0].length; i-- != 0;) d[0][i] = random.nextInt();
		d[1] = new float[d[0].length];
		for(int i = d.length; i-- != 0;) d[1][i] = random.nextInt();
		FloatArrays.radixSort(d);
		for(int i = d[0].length - 1; i-- != 0;) assertTrue(Integer.toString(i) + ": <" + d[0][i] + ", " + d[1][i] + ">, <" + d[0][i + 1] + ", " +  d[1][i + 1] + ">", d[0][i] < d[0][i + 1] || d[0][i] == d[0][i + 1] && d[1][i] <= d[1][i + 1]);
	}




	@Test
	public void testMergeSortNaNs() {
		final float[] t = { Float.NaN, 1, 5, 2, 1, 0, 9, 1, Float.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final float[] a = t.clone();
				FloatArrays.mergeSort(a, from, to);
				for(int i = to - 1; i-- != from;) assertTrue(Float.compare(a[i], a[i + 1]) <= 0);
			}

	}


	@Test
	public void testRadixSortNaNs() {
		final float[] t = { Float.NaN, 1, 5, 2, 1, 0, 9, 1, Float.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final float[] a = t.clone();
				FloatArrays.radixSort(a, from, to);
				for(int i = to - 1; i-- != from;) assertTrue(Float.compare(a[i], a[i + 1]) <= 0);
			}

	}

	@Test
	public void testRadixSortIndirectNaNs() {
		final float[] t = { Float.NaN, 1, 5, 2, 1, 0, 9, 1, Float.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final int perm[] = new int[t.length];
				for(int i = perm.length; i-- != 0;) perm[i] = i;
				FloatArrays.radixSortIndirect(perm, t, from, to, true);
				for(int i = to - 1; i-- != from;) assertTrue(Float.compare(t[perm[i]], t[perm[i + 1]]) <= 0);
			}

	}

	@Test
	public void testRadixSortIndirect2NaNs() {
		final float[] t = { Float.NaN, 1, 5, 2, 1, 0, 9, 1, Float.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final int perm[] = new int[t.length];
				for(int i = perm.length; i-- != 0;) perm[i] = i;
				FloatArrays.radixSortIndirect(perm, t, t, from, to, true);
				for(int i = to - 1; i-- != from;) assertTrue(Float.compare(t[perm[i]], t[perm[i + 1]]) <= 0);
			}

	}


	@Test
	public void testQuickSortNaNs() {
		final float[] t = { Float.NaN, 1, 5, 2, 1, 0, 9, 1, Float.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final float[] a = t.clone();
				FloatArrays.quickSort(a, from, to);
				for(int i = to - 1; i-- != from;) assertTrue(Float.compare(a[i], a[i + 1]) <= 0);
			}

	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(FloatArrays.class, "test", /*num=*/"1000", /*seed=*/"848747");
	}
}

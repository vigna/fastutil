/*
 * Copyright (C) 2003-2024 Barak Ugav and Sebastiano Vigna
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntComparatorTest {

    @Test
    public void comparing() {
        String[] array = new String[] { "68", "98", "30", "62", "81", "61", "80", "63", "62", "77", "10", "95", "40",
                "73", "55", "45", "16", "10", "86", "28", "79", "44", "52", "92", "98", "28", "88", "70", "70", "10" };
        IntComparator c = IntComparator.comparing(i -> array[i]);
        for (int i = 0; i < array.length; i++) {
            int j = ((i + 29) * 1337) % array.length;
            assertEquals(c.compare(i, j), array[i].compareTo(array[j]));
        }
    }

    @Test
    public void comparingInt() {
        int[] array = new int[] { 81, 87, 70, 54, 40, 79, 16, 8, 84, 39, 37, 84, 64, 60, 31, 44, 95, 15, 52, 48, 19, 20,
                75, 31, 46, 61, 38, 27, 32, 84 };
        IntComparator c = IntComparator.comparingInt(i -> array[i]);
        for (int i = 0; i < array.length; i++) {
            int j = ((i + 17) * 1337) % array.length;
            assertEquals(c.compare(i, j), Integer.compare(array[i], array[j]));
        }
    }

    @Test
    public void comparingLong() {
        long[] array = new long[] { 26, 49, 49, 24, 15, 71, 10, 88, 78, 4, 42, 79, 75, 69, 63, 16, 71, 47, 54, 39, 89,
                10, 64, 37, 38, 59, 81, 59, 58, 33 };
        IntComparator c = IntComparator.comparingLong(i -> array[i]);
        for (int i = 0; i < array.length; i++) {
            int j = ((i + 19) * 1337) % array.length;
            assertEquals(c.compare(i, j), Long.compare(array[i], array[j]));
        }
    }

    @Test
    public void comparingDouble() {
        double[] array = new double[] { 0.61, 0.97, 0.97, 0.75, 0.73, 0.36, 0.72, 0.14, 0.93, 0.18, 0.45, 0.03, 0.62,
                0.05, 0.04, 0.05, 0.38, 0.89, 0., 0.93, 0.83, 0.14, 0.21, 0.79, 0.5, 0.17, 0.46, 0.74, 0.88, 0.94 };
        IntComparator c = IntComparator.comparingDouble(i -> array[i]);
        for (int i = 0; i < array.length; i++) {
            int j = ((i + 23) * 1337) % array.length;
            assertEquals(c.compare(i, j), Double.compare(array[i], array[j]));
        }
    }

}

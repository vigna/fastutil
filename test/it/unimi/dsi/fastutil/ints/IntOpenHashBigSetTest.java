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

import static it.unimi.dsi.fastutil.BigArrays.get;
import static it.unimi.dsi.fastutil.BigArrays.length;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.MainRunner;

@SuppressWarnings("rawtypes")
public class IntOpenHashBigSetTest {

	@Test
	public void testStrangeRetainAllCase() {

		final IntArrayList initialElements = IntArrayList.wrap(new int[] { 586, 940,
				1086, 1110, 1168, 1184, 1185, 1191, 1196, 1229, 1237, 1241,
				1277, 1282, 1284, 1299, 1308, 1309, 1310, 1314, 1328, 1360,
				1366, 1370, 1378, 1388, 1392, 1402, 1406, 1411, 1426, 1437,
				1455, 1476, 1489, 1513, 1533, 1538, 1540, 1541, 1543, 1547,
				1548, 1551, 1557, 1568, 1575, 1577, 1582, 1583, 1584, 1588,
				1591, 1592, 1601, 1610, 1618, 1620, 1633, 1635, 1653, 1654,
				1655, 1660, 1661, 1665, 1674, 1686, 1688, 1693, 1700, 1705,
				1717, 1720, 1732, 1739, 1740, 1745, 1746, 1752, 1754, 1756,
				1765, 1766, 1767, 1771, 1772, 1781, 1789, 1790, 1793, 1801,
				1806, 1823, 1825, 1827, 1828, 1829, 1831, 1832, 1837, 1839,
				1844, 2962, 2969, 2974, 2990, 3019, 3023, 3029, 3030, 3052,
				3072, 3074, 3075, 3093, 3109, 3110, 3115, 3116, 3125, 3137,
				3142, 3156, 3160, 3176, 3180, 3188, 3193, 3198, 3207, 3209,
				3210, 3213, 3214, 3221, 3225, 3230, 3231, 3236, 3240, 3247,
				3261, 4824, 4825, 4834, 4845, 4852, 4858, 4859, 4867, 4871,
				4883, 4886, 4887, 4905, 4907, 4911, 4920, 4923, 4924, 4925,
				4934, 4942, 4953, 4957, 4965, 4973, 4976, 4980, 4982, 4990,
				4993, 6938, 6949, 6953, 7010, 7012, 7034, 7037, 7049, 7076,
				7094, 7379, 7384, 7388, 7394, 7414, 7419, 7458, 7459, 7466,
				7467 });

		final IntArrayList retainElements = IntArrayList.wrap(new int[] { 586 });

		// Initialize both implementations with the same data
		final IntOpenHashBigSet instance = new IntOpenHashBigSet(initialElements);
		final IntRBTreeSet referenceInstance = new IntRBTreeSet(initialElements);

		instance.retainAll(retainElements);
		referenceInstance.retainAll(retainElements);

		// print the correct result {586}
		// System.out.println("ref: " + referenceInstance);

		// prints {586, 7379}, which is clearly wrong
		// System.out.println("ohm: " + instance);

		// Fails
		assertEquals(referenceInstance, instance);
	}


	@Test
	public void testTrim() {
		IntOpenHashBigSet s = new IntOpenHashBigSet(100, .75f);
		s.trim(0);
		assertEquals(1, s.n);

		s = new IntOpenHashBigSet(100, .75f);
		s.trim(10);
		assertEquals(16, s.n);
		s.trim(20);
		assertEquals(16, s.n);

		s = new IntOpenHashBigSet(6, .75f);
		assertEquals(8, s.n);
		for(int i = 0; i < 6; i++) s.add(i);
		assertEquals(8, s.n);
		s.trim(2);
		assertEquals(8, s.n);
	}



	@Test
	public void testRemove0() {
		IntOpenHashBigSet s = new IntOpenHashBigSet(Hash.DEFAULT_INITIAL_SIZE);
		for(int i = -1; i <= 1; i++) assertTrue(s.add(i));
		assertTrue(s.remove(0));
		IntIterator iterator = s.iterator();
		final IntOpenHashSet z = new IntOpenHashSet();
		z.add(iterator.nextInt());
		z.add(iterator.nextInt());
		assertFalse(iterator.hasNext());
		assertEquals(new IntOpenHashSet(new int[] { -1, 1 }), z);

		s = new IntOpenHashBigSet(Hash.DEFAULT_INITIAL_SIZE);
		for(int i = -1; i <= 1; i++) assertTrue(s.add(i));
		iterator = s.iterator();
		while(iterator.hasNext()) if (iterator.nextInt() == 0) iterator.remove();

		assertFalse(s.contains(0));

		iterator = s.iterator();
		final int[] content = new int[2];
		content[0] = iterator.nextInt();
		content[1] = iterator.nextInt();
		assertFalse(iterator.hasNext());
		Arrays.sort(content);
		assertArrayEquals(new int[] { -1, 1 }, content);
	}

	// Here because IntOpenHashBigSet has neither an of() nor a wrap().
	private static IntOpenHashBigSet setOf(int... elements) {
		IntOpenHashBigSet result = new IntOpenHashBigSet(elements.length);
		for (int i : elements) {
			result.add(i);
		}
		return result;
	}

	@Test
	public void testToBigSet() {
		final IntOpenHashBigSet baseSet = setOf(2, 380, 1297);
		IntOpenHashBigSet transformed = IntOpenHashBigSet.toBigSet(baseSet.intStream().map(i -> i + 40));
		assertEquals(setOf(42, 420, 1337), transformed);
	}

	@Test
	public void testSpliteratorTrySplit() {
		final IntOpenHashBigSet baseSet = setOf(0, 1, 2, 3, 72, 5, 6);
		IntSpliterator spliterator1 = baseSet.spliterator();
		assertEquals(baseSet.size64(), spliterator1.getExactSizeIfKnown());
		IntSpliterator spliterator2 = spliterator1.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split.
		java.util.stream.IntStream stream1 = java.util.stream.StreamSupport.intStream(spliterator1, false);
		java.util.stream.IntStream stream2 = java.util.stream.StreamSupport.intStream(spliterator2, false);

		final IntOpenHashBigSet subSet1 = IntOpenHashBigSet.toBigSet(stream1);
		// Intentionally collecting to a list for this second one.
		final IntBigArrayBigList subSet2 = IntBigArrayBigList.toBigList(stream2);
		assertEquals(baseSet.size64(), subSet1.size64() + subSet2.size64());
		final IntOpenHashBigSet recombinedSet = new IntOpenHashBigSet(baseSet.size64());
		recombinedSet.addAll(subSet1);
		recombinedSet.addAll(subSet2);
		assertEquals(baseSet, recombinedSet);
	}


	private static java.util.Random r = new java.util.Random(0);

	private static int genKey() {
		return r.nextInt();
	}

	@SuppressWarnings("boxing")
	private static void checkTable(final IntOpenHashBigSet s) {
		final int[][] key = s.key;
		assert (s.n & -s.n) == s.n : "Table length is not a power of two: " + s.n;
		assert s.n == length(key);
		long n = s.n;
		while (n-- != 0)
			if (get(key, n) != 0 && !s.contains(get(key, n))) throw new AssertionError("Hash table has key " + get(key, n)
					+ " marked as occupied, but the key does not belong to the table");

		final java.util.HashSet<Integer> t = new java.util.HashSet<>();
		for (long i = s.size64(); i-- != 0;)
			if (get(key, i) != 0 && !t.add(get(key, i))) throw new AssertionError("Key " + get(key, i) + " appears twice");

	}

	private static void printProbes(final IntOpenHashBigSet m) {
		long totProbes = 0;
		double totSquareProbes = 0;
		long maxProbes = 0;
		final double f = (double)m.size / m.n;
		for (long i = 0, c = 0; i < m.n; i++) {
			if (get(m.key, i) != 0) c++;
			else {
				if (c != 0) {
					final long p = (c + 1) * (c + 2) / 2;
					totProbes += p;
					totSquareProbes += (double)p * p;
				}
				maxProbes = Math.max(c, maxProbes);
				c = 0;
				totProbes++;
				totSquareProbes++;
			}
		}

		final double expected = (double)totProbes / m.n;
		System.err.println("Expected probes: " + (
				3 * Math.sqrt(3) * (f / ((1 - f) * (1 - f))) + 4 / (9 * f) - 1
				) + "; actual: " + expected + "; stddev: " + Math.sqrt(totSquareProbes / m.n - expected * expected) + "; max probes: " + maxProbes);
	}

	@SuppressWarnings({ "unchecked", "boxing", "deprecation" })
	private static void test(final int n, final float f) throws IOException, ClassNotFoundException {
		int c;
		IntOpenHashBigSet m = new IntOpenHashBigSet(Hash.DEFAULT_INITIAL_SIZE, f);
		final java.util.Set t = new java.util.HashSet();

		/* First of all, we fill t with random data. */

		for (int i = 0; i < f * n; i++)
			t.add((Integer.valueOf(genKey())));

		/* Now we add to m the same data */

		m.addAll(t);
		checkTable(m);

		assertTrue("Error: !m.equals(t) after insertion", m.equals(t));
		assertTrue("Error: !t.equals(m) after insertion", t.equals(m));
		printProbes(m);

		/* Now we check that m actually holds that data. */

		for (Object e : t) {
			assertTrue("Error: m and t differ on a key (" + e + ") after insertion (iterating on t)", m.contains(e));
		}

		/* Now we check that m actually holds that data, but iterating on m. */

		c = 0;
		for (final java.util.Iterator i = m.iterator(); i.hasNext();) {
			final Object e = i.next();
			c++;
			assertTrue("Error: m and t differ on a key (" + e + ") after insertion (iterating on m)", t.contains(e));
		}

		assertEquals("Error: m has only " + c + " keys instead of " + t.size() + " after insertion (iterating on m)", c, t.size());
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */

		for (int i = 0; i < n; i++) {
			final int T = genKey();
			assertEquals("Error: divergence in keys between t and m (polymorphic method)", m.contains(T), t.contains((Integer.valueOf(T))));
		}

		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */

		for (int i = 0; i < n; i++) {
			final int T = genKey();
			assertFalse("Error: divergence between t and m (standard method)", m.contains((Integer.valueOf(T))) != t.contains((Integer.valueOf(T))));
		}


		/* Now we put and remove random data in m and t, checking that the result is the same. */

		for (int i = 0; i < 20 * n; i++) {
			int T = genKey();
			assertFalse("Error: divergence in add() between t and m", m.add((Integer.valueOf(T))) != t.add((Integer.valueOf(T))));
			T = genKey();
			assertFalse("Error: divergence in remove() between t and m", m.remove((Integer.valueOf(T))) != t.remove((Integer.valueOf(T))));
		}

		checkTable(m);
		assertTrue("Error: !m.equals(t) after removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after removal", t.equals(m));
		/* Now we check that m actually holds that data. */

		for (Object e : t) {
			assertFalse("Error: m and t differ on a key (" + e + ") after removal (iterating on t)", !m.contains(e));
		}

		/* Now we check that m actually holds that data, but iterating on m. */

		for (final java.util.Iterator i = m.iterator(); i.hasNext();) {
			final Object e = i.next();
			assertFalse("Error: m and t differ on a key (" + e + ") after removal (iterating on m)", !t.contains(e));
		}

		/* Now we make m into an array, make it again a set and check it is OK. */
		final int a[] = m.toIntArray();

		assertTrue("Error: toArray() output (or array-based constructor) is not OK", new IntOpenHashBigSet(a).equals(m));

		/* Now we check cloning. */

		assertTrue("Error: m does not equal m.clone()", m.equals(m.clone()));
		assertTrue("Error: m.clone() does not equal m", m.clone().equals(m));

		final int h = m.hashCode();

		/* Now we save and read m. */

		final java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test.junit." + m.getClass().getSimpleName() + "." + n);
		final java.io.OutputStream os = new java.io.FileOutputStream(ff);
		final java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);

		oos.writeObject(m);
		oos.close();

		final java.io.InputStream is = new java.io.FileInputStream(ff);
		final java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);

		m = (IntOpenHashBigSet)ois.readObject();
		ois.close();
		ff.delete();

		assertEquals("Error: hashCode() changed after save/read", h, m.hashCode());

		printProbes(m);
		checkTable(m);

		/* Now we check that m actually holds that data, but iterating on m. */

		for (final java.util.Iterator i = m.iterator(); i.hasNext();) {
			final Object e = i.next();
			assertFalse("Error: m and t differ on a key (" + e + ") after save/read", !t.contains(e));
		}


		/* Now we put and remove random data in m and t, checking that the result is the same. */

		for (int i = 0; i < 20 * n; i++) {
			int T = genKey();
			assertFalse("Error: divergence in add() between t and m after save/read", m.add((Integer.valueOf(T))) != t.add((Integer.valueOf(T))));
			T = genKey();
			assertFalse("Error: divergence in remove() between t and m after save/read", m.remove((Integer.valueOf(T))) != t.remove((Integer.valueOf(T))));
		}

		assertTrue("Error: !m.equals(t) after post-save/read removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after post-save/read removal", t.equals(m));

		/* Now we take out of m everything, and check that it is empty. */

		for (final java.util.Iterator i = m.iterator(); i.hasNext();) {
			i.next();
			i.remove();
		}

		assertFalse("Error: m is not empty (as it should be)", !m.isEmpty());


		m = new IntOpenHashBigSet(n, f);
		t.clear();

		/* Now we torture-test the hash table. This part is implemented only for integers and longs. */

		for(int i = n; i-- != 0;) m.add(i);
		t.addAll(m);
		printProbes(m);
		checkTable(m);

		/* Now all table entries are REMOVED. */

		for(int i = n; i-- != 0;)
			assertEquals("Error: m and t differ on a key during torture-test insertion.", m.add(i), t.add((Integer.valueOf(i))));

		assertTrue("Error: !m.equals(t) after torture-test insertion", m.equals(t));
		assertTrue("Error: !t.equals(m) after torture-test insertion", t.equals(m));

		for(int i = n; i-- != 0;)
			assertEquals("Error: m and t differ on a key during torture-test insertion.", m.remove(i), t.remove((Integer.valueOf(i))));

		assertTrue("Error: !m.equals(t) after torture-test removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after torture-test removal", t.equals(m));
		assertTrue("Error: !m.equals(m.clone()) after torture-test removal", m.equals(m.clone()));
		assertTrue("Error: !m.clone().equals(m) after torture-test removal", m.clone().equals(m));
		m.trim();

		assertTrue("Error: !m.equals(t) after trim()", m.equals(t));
		assertTrue("Error: !t.equals(m) after trim()", t.equals(m));

		return;
	}


	@Test
	public void test1() throws IOException, ClassNotFoundException {
		test(1, Hash.DEFAULT_LOAD_FACTOR);
		test(1, Hash.FAST_LOAD_FACTOR);
		test(1, Hash.VERY_FAST_LOAD_FACTOR);
	}

	@Test
	public void test10() throws IOException, ClassNotFoundException {
		test(10, Hash.DEFAULT_LOAD_FACTOR);
		test(10, Hash.FAST_LOAD_FACTOR);
		test(10, Hash.VERY_FAST_LOAD_FACTOR);
	}

	@Test
	public void test100() throws IOException, ClassNotFoundException {
		test(100, Hash.DEFAULT_LOAD_FACTOR);
		test(100, Hash.FAST_LOAD_FACTOR);
		test(100, Hash.VERY_FAST_LOAD_FACTOR);
	}

	@Ignore("Too long")
	@Test
	public void test1000() throws IOException, ClassNotFoundException {
		test(1000, Hash.DEFAULT_LOAD_FACTOR);
		test(1000, Hash.FAST_LOAD_FACTOR);
		test(1000, Hash.VERY_FAST_LOAD_FACTOR);
	}
	
	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntOpenHashBigSet.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"383474");
	}
}

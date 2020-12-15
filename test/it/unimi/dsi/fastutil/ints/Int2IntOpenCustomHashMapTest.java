/*
 * Copyright (C) 2017 Sebastiano Vigna
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import it.unimi.dsi.fastutil.Hash;

@SuppressWarnings("rawtypes")

/** Not a particularly good test, but it will check that we use everywhere the same hashing strategy. */

public class Int2IntOpenCustomHashMapTest {

	@Test
	public void testGetNullKey() {
		final Int2IntOpenCustomHashMap s = new Int2IntOpenCustomHashMap(new IntHash.Strategy() {

			@Override
			public int hashCode(int o) {
				return o % 10;
			}

			@Override
			public boolean equals(int a, int b) {
				return (a - b) % 10 == 0;
			}
		});

		s.put(10, 10);
		assertTrue(s.containsKey(0));
		Entry<Integer, Integer> e = s.int2IntEntrySet().iterator().next();
		assertEquals(10, e.getKey().intValue());
		assertEquals(10, e.getValue().intValue());
		s.remove(0);
		assertTrue(s.isEmpty());
	}


	private static final class Strategy implements IntHash.Strategy, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int hashCode(int e) {
			return Integer.reverse(e);
		}

		@Override
		public boolean equals(int a, int b) {
			return a == b;
		}
	}

	private static final Strategy strategy = new Strategy();

	private static java.util.Random r = new java.util.Random(0);

	private static int genKey() {
		return r.nextInt(10);
	}

	@SuppressWarnings("boxing")
	private static void checkTable(Int2IntOpenCustomHashMap s) {
		final int[]key = s.key;
		assert (s.n & -s.n) == s.n : "Table length is not a power of two: " + s.n;
		assert s.n == s.key.length - 1;
		int n = s.n;
		while (n-- != 0)
			if (key[n] != 0 && !s.containsKey(key[n])) throw new AssertionError("Hash table has key " + key[n]
					+ " marked as occupied, but the key does not belong to the table");

		java.util.HashMap<Integer,Integer> t = new java.util.HashMap<>();
		for (int i = s.size(); i-- != 0;)
			if (key[i] != 0 && t.put(key[i], key[i]) != null) throw new AssertionError("Key " + key[i] + " appears twice");

	}

	private static void printProbes(Int2IntOpenCustomHashMap m) {
		long totProbes = 0;
		double totSquareProbes = 0;
		int maxProbes = 0;
		final int[] key = m.key;
		final double f = (double)m.size / m.n;
		for (int i = 0, c = 0; i < m.n; i++) {
			if (key[i] != 0) c++;
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

	@SuppressWarnings({ "deprecation", "unlikely-arg-type" })
	private static void test(int n, float f) throws IOException, ClassNotFoundException {
		int c;
		final Integer key[] = new Integer[(int)Math.ceil(n * f)];
		HashMap<Integer,Integer> t = new HashMap<>();
		/* First of all, we fill t with random data. */

		for (int i = 0; i < key.length; i++) t.put(key[i] = new Integer(genKey()), key[i]);

		Int2IntOpenCustomHashMap m = new Int2IntOpenCustomHashMap(Hash.DEFAULT_INITIAL_SIZE, f, strategy);


		/* Now we add to m the same data */

		m.putAll(t);
		checkTable(m);

		assertTrue("Error: !m.equals(t) after insertion", m.equals(t));
		assertTrue("Error: !t.equals(m) after insertion", t.equals(m));
		printProbes(m);

		/* Now we check that m actually holds that data. */

		for (java.util.Iterator i = t.keySet().iterator(); i.hasNext();) {
			Object e = i.next();
			assertTrue("Error: m and t differ on a key (" + e + ") after insertion (iterating on t)", m.get(e).equals(e));
		}

		/* Now we check that m actually holds that data, but iterating on m. */

		c = 0;
		for (java.util.Iterator i = m.keySet().iterator(); i.hasNext();) {
			Object e = i.next();
			c++;
			assertTrue("Error: m and t differ on a key (" + e + ") after insertion (iterating on m)", t.get(e).equals(e));
		}

		assertEquals("Error: m has only " + c + " keys instead of " + t.size() + " after insertion (iterating on m)", c, t.size());
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */

		for (int i = 0; i < n; i++) {
			int T = genKey();
			if (m.containsKey(T)) assertEquals("Error: divergence in keys between t and m (polymorphic method)", Integer.valueOf(m.get(T)), t.get(Integer.valueOf(T)));
			else assertFalse("Error: divergence in keys between t and m (polymorphic method)", t.containsKey(Integer.valueOf(T)));
		}

		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */

		for (int i = 0; i < n; i++) {
			int T = genKey();
			assertEquals("Error: divergence between t and m (standard method)", m.get(Integer.valueOf(T)), t.get(Integer.valueOf(T)));
		}


		/* Now we put and remove random data in m and t, checking that the result is the same. */

		for (int i = 0; i < 20 * n; i++) {
			int T = genKey();
			assertEquals("Error: divergence in add() between t and m", m.put(Integer.valueOf(T), Integer.valueOf(T)), t.put(Integer.valueOf(T), Integer.valueOf(T)));
			T = genKey();
			assertEquals("Error: divergence in remove() between t and m", m.remove(Integer.valueOf(T)), t.remove(Integer.valueOf(T)));
		}

		checkTable(m);
		assertTrue("Error: !m.equals(t) after removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after removal", t.equals(m));
		/* Now we check that m actually holds that data. */

		for (java.util.Iterator i = t.keySet().iterator(); i.hasNext();) {
			Object e = i.next();
			assertFalse("Error: m and t differ on a key (" + e + ") after removal (iterating on t)", !m.get(e).equals(e));
		}

		/* Now we check that m actually holds that data, but iterating on m. */

		for (java.util.Iterator i = m.keySet().iterator(); i.hasNext();) {
			Object e = i.next();
			assertFalse("Error: m and t differ on a key (" + e + ") after removal (iterating on m)", !t.get(e).equals(e));
		}

		/* Now we check cloning. */

		assertTrue("Error: m does not equal m.clone()", m.equals(m.clone()));
		assertTrue("Error: m.clone() does not equal m", m.clone().equals(m));

		int h = m.hashCode();

		/* Now we save and read m. */

		java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test.junit." + m.getClass().getSimpleName() + "." + n);
		java.io.OutputStream os = new java.io.FileOutputStream(ff);
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);

		oos.writeObject(m);
		oos.close();

		java.io.InputStream is = new java.io.FileInputStream(ff);
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);

		m = (Int2IntOpenCustomHashMap)ois.readObject();
		ois.close();
		ff.delete();

		assertEquals("Error: hashCode() changed after save/read", h, m.hashCode());

		printProbes(m);
		checkTable(m);

		/* Now we check that m actually holds that data, but iterating on m. */

		for (java.util.Iterator i = m.keySet().iterator(); i.hasNext();) {
			Object e = i.next();
			assertFalse("Error: m and t differ on a key (" + e + ") after save/read", !t.get(e).equals(e));
		}


		/* Now we put and remove random data in m and t, checking that the result is the same. */

		for (int i = 0; i < 20 * n; i++) {
			int T = genKey();
			assertEquals("Error: divergence in add() between t and m after save/read", m.put(Integer.valueOf(T), Integer.valueOf(T)), t.put(Integer.valueOf(T), Integer.valueOf(T)));
			T = genKey();
			assertEquals("Error: divergence in remove() between t and m after save/read", m.remove(Integer.valueOf(T)), t.remove(Integer.valueOf(T)));
		}

		assertTrue("Error: !m.equals(t) after post-save/read removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after post-save/read removal", t.equals(m));

		/* Now we take out of m everything, and check that it is empty. */

		for (java.util.Iterator i = m.keySet().iterator(); i.hasNext();) {
			i.next();
			i.remove();
		}

		assertFalse("Error: m is not empty (as it should be)", !m.isEmpty());


		m = new Int2IntOpenCustomHashMap(n, f, strategy);
		t.clear();

		/* Now we torture-test the hash table. This part is implemented only for integers and longs. */

		for(int i = n; i-- != 0;) m.put(i, i);
		t.putAll(m);
		printProbes(m);
		checkTable(m);

		for(int i = n; i-- != 0;)
			assertEquals("Error: m and t differ on a key during torture-test insertion.", Integer.valueOf(m.put(i, i)), t.put((Integer.valueOf(i)), (Integer.valueOf(i))));

		assertTrue("Error: !m.equals(t) after torture-test insertion", m.equals(t));
		assertTrue("Error: !t.equals(m) after torture-test insertion", t.equals(m));

		for(int i = n; i-- != 0;)
			assertEquals("Error: m and t differ on a key during torture-test insertion.", Integer.valueOf(m.remove(i)), t.remove((Integer.valueOf(i))));

		assertTrue("Error: !m.equals(t) after torture-test removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after torture-test removal", t.equals(m));
		assertTrue("Error: !m.equals(m.clone()) after torture-test removal", m.equals(m.clone()));
		assertTrue("Error: !m.clone().equals(m) after torture-test removal", m.clone().equals(m));

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
}

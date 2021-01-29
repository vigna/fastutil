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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.MainRunner;

@SuppressWarnings({"rawtypes","deprecation"})
public class Object2IntOpenHashMapTest {

	private static java.util.Random r = new java.util.Random(0);

	private static Object genKey() {
		return Integer.toBinaryString(r.nextInt());
	}

	private static int genValue() {
		return r.nextInt();
	}

	private static boolean valEquals(final Object o1, final Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	@SuppressWarnings("unchecked")
	protected static void test(final int n, final float f) throws IOException, ClassNotFoundException {
		Object2IntOpenHashMap m = new Object2IntOpenHashMap(Hash.DEFAULT_INITIAL_SIZE, f);
		final Map t = new java.util.HashMap();
		/* First of all, we fill t with random data. */
		for (int i = 0; i < n; i++)
			t.put((genKey()), (Integer.valueOf(genValue())));
		/* Now we add to m the same data */
		m.putAll(t);
		assertTrue("Error: !m.equals(t) after insertion", m.equals(t));
		assertTrue("Error: !t.equals(m) after insertion", t.equals(m));
		/* Now we check that m actually holds that data. */
		for (final Object element : t.entrySet()) {
			final java.util.Map.Entry e = (java.util.Map.Entry)element;
			assertTrue("Error: m and t differ on an entry (" + e + ") after insertion (iterating on t)", valEquals(e.getValue(), m.get(e.getKey())));
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for (final java.util.Iterator i = m.entrySet().iterator(); i.hasNext();) {
			final java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			assertTrue("Error: m and t differ on an entry (" + e + ") after insertion (iterating on m)", valEquals(e.getValue(), t.get(e.getKey())));
		}
		/* Now we check that m actually holds the same keys. */
		for (final Object o : t.keySet()) {
			assertTrue("Error: m and t differ on a key (" + o + ") after insertion (iterating on t)", m.containsKey(o));
			assertTrue("Error: m and t differ on a key (" + o + ", in keySet()) after insertion (iterating on t)", m.keySet().contains(o));
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for (final java.util.Iterator i = m.keySet().iterator(); i.hasNext();) {
			final Object o = i.next();
			assertTrue("Error: m and t differ on a key after insertion (iterating on m)", t.containsKey(o));
			assertTrue("Error: m and t differ on a key (in keySet()) after insertion (iterating on m)", t.keySet().contains(o));
		}
		/* Now we check that m actually hold the same values. */
 		for (final Object o : t.values()) {
			assertTrue("Error: m and t differ on a value after insertion (iterating on t)", m.containsValue(o));
			assertTrue("Error: m and t differ on a value (in values()) after insertion (iterating on t)", m.values().contains(o));
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for (final java.util.Iterator i = m.values().iterator(); i.hasNext();) {
			final Object o = i.next();
			assertTrue("Error: m and t differ on a value after insertion (iterating on m)", t.containsValue(o));
			assertTrue("Error: m and t differ on a value (in values()) after insertion (iterating on m)", t.values().contains(o));
		}
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for (int i = 0; i < n; i++) {
			final Object T = genKey();
			assertFalse("Error: divergence in keys between t and m (polymorphic method)", m.containsKey((T)) != t.containsKey((T)));
			assertFalse("Error: divergence between t and m (polymorphic method)", (m.getInt(T) != (0)) != ((t.get((T)) == null ? (0) : ((((Integer)(t.get((T)))).intValue()))) != (0)) ||
					t.get((T)) != null &&
					!(Integer.valueOf(m.getInt(T))).equals(t.get((T))));
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for (int i = 0; i < n; i++) {
			final Object T = genKey();
			assertTrue("Error: divergence between t and m (standard method)", valEquals(m.get((T)), t.get((T))));
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for (int i = 0; i < 20 * n; i++) {
			Object T = genKey();
			final int U = genValue();
			assertTrue("Error: divergence in put() between t and m", valEquals(m.put((T), (Integer.valueOf(U))), t.put((T), (Integer.valueOf(U)))));
			T = genKey();
			assertTrue("Error: divergence in remove() between t and m", valEquals(m.remove((T)), t.remove((T))));
		}
		assertTrue("Error: !m.equals(t) after removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after removal", t.equals(m));
		/* Now we check that m actually holds the same data. */
		for (final Object element : t.entrySet()) {
			final java.util.Map.Entry e = (java.util.Map.Entry)element;
			assertTrue("Error: m and t differ on an entry (" + e + ") after removal (iterating on t)", valEquals(e.getValue(), m.get(e.getKey())));
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for (final java.util.Iterator i = m.entrySet().iterator(); i.hasNext();) {
			final java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			assertTrue("Error: m and t differ on an entry (" + e + ") after removal (iterating on m)", valEquals(e.getValue(), t.get(e.getKey())));
		}
		/* Now we check that m actually holds the same keys. */
		for (final Object o : t.keySet()) {
			assertTrue("Error: m and t differ on a key (" + o + ") after removal (iterating on t)", m.containsKey(o));
			assertTrue("Error: m and t differ on a key (" + o + ", in keySet()) after removal (iterating on t)", m.keySet().contains(o));
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for (final java.util.Iterator i = m.keySet().iterator(); i.hasNext();) {
			final Object o = i.next();
			assertTrue("Error: m and t differ on a key after removal (iterating on m)", t.containsKey(o));
			assertTrue("Error: m and t differ on a key (in keySet()) after removal (iterating on m)", t.keySet().contains(o));
		}
		/* Now we check that m actually hold the same values. */
		for (final Object o : t.values()) {
			assertTrue("Error: m and t differ on a value after removal (iterating on t)", m.containsValue(o));
			assertTrue("Error: m and t differ on a value (in values()) after removal (iterating on t)", m.values().contains(o));
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for (final java.util.Iterator i = m.values().iterator(); i.hasNext();) {
			final Object o = i.next();
			assertTrue("Error: m and t differ on a value after removal (iterating on m)", t.containsValue(o));
			assertTrue("Error: m and t differ on a value (in values()) after removal (iterating on m)", t.values().contains(o));
		}
		final int h = m.hashCode();
		/* Now we save and read m. */
		final java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test.junit." + m.getClass().getSimpleName() + "." + n);
		final java.io.OutputStream os = new java.io.FileOutputStream(ff);
		final java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);
		oos.writeObject(m);
		oos.close();
		final java.io.InputStream is = new java.io.FileInputStream(ff);
		final java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);
		m = (Object2IntOpenHashMap)ois.readObject();
		ois.close();
		ff.delete();
		assertTrue("Error: hashCode() changed after save/read", m.hashCode() == h);
		/* Now we check that m actually holds that data. */
		for (final Object o : t.keySet()) {
			assertTrue("Error: m and t differ on an entry after save/read", valEquals(m.get(o), t.get(o)));
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for (int i = 0; i < 20 * n; i++) {
			Object T = genKey();
			final int U = genValue();
			assertTrue("Error: divergence in put() between t and m after save/read", valEquals(m.put((T), (Integer.valueOf(U))), t.put((T), (Integer.valueOf(U)))));
			T = genKey();
			assertTrue("Error: divergence in remove() between t and m after save/read", valEquals(m.remove((T)), t.remove((T))));
		}
		assertTrue("Error: !m.equals(t) after post-save/read removal", m.equals(t));
		assertTrue("Error: !t.equals(m) after post-save/read removal", t.equals(m));
		/* Now we take out of m everything, and check that it is empty. */
		for (final Object element : t.keySet()) m.remove(element);
		assertTrue("Error: m is not empty (as it should be)", m.isEmpty());
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
		MainRunner.callMainIfExists(Object2IntOpenHashMap.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"383454");
	}
}


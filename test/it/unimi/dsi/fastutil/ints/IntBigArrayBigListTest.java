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

import static it.unimi.dsi.fastutil.BigArrays.wrap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.MainRunner;

@SuppressWarnings({"rawtypes", "static-method"})
public class IntBigArrayBigListTest {

	@Test
	public void testRemoveAllModifiesCollection() {
		final IntBigList list = new IntBigArrayBigList();
		assertFalse(list.removeAll(Collections.emptySet()));
		assertEquals(IntBigLists.EMPTY_BIG_LIST, list);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testRemoveAllSkipSegment() {
		final IntBigList list = new IntBigArrayBigList();
		for(long i = 0; i < BigArrays.SEGMENT_SIZE + 10; i++) list.add((int)(i % 2));
		assertTrue(list.removeAll(IntSets.singleton(1)));
		assertEquals((BigArrays.SEGMENT_SIZE + 1) / 2 + 5, list.size64());
		for (long i = 0; i < (BigArrays.SEGMENT_SIZE + 1) / 2 + 5; i++) assertEquals(0, list.getInt(i));
	}


	@Test(expected = IndexOutOfBoundsException.class)
	public void testListIteratorTooLow() {
		new IntBigArrayBigList().listIterator(-1L);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testListIteratorTooHigh() {
		new IntBigArrayBigList().listIterator(1L);
	}

	@Test
	public void testAddWithIterator() {
		final IntBigList list = new IntBigArrayBigList();
		list.iterator().add(1);
		assertEquals(IntBigLists.singleton(1), list);
	}

	@Test
	public void testRemoveAll() {
		IntBigArrayBigList l = new IntBigArrayBigList(wrap(new int[] { 0, 1, 2 }));
		l.removeAll(IntSets.singleton(1));
		assertEquals(IntBigArrayBigList.of(0, 2), l);

		l = new IntBigArrayBigList(wrap(new int[] { 0, 1, 1, 2 }));
		l.removeAll(Collections.singleton(Integer.valueOf(1)));
		assertEquals(IntBigArrayBigList.of(0, 2), l);
	}


	private static IntBigArrayBigList of(final int... members) {
		return new IntBigArrayBigList(IntArrayList.of(members));
	}

	@Test
	public void testEquals_AnotherArrayList() {
		final IntBigArrayBigList baseList = of(2, 380, 1297);
		assertEquals(of(2, 380, 1297), baseList);
		assertNotEquals(of(42, 420, 1337), baseList);
	}

	@Test
	public void testEquals_Sublist() {
		final IntBigArrayBigList l1 = of(0, 1, 2, 3);
		final IntBigArrayBigList l2 = of(5, 0, 1, 2, 3, 4);
		final IntBigList sl2 = l2.subList(1, 5); // 0, 1, 2, 3
		assertEquals(l1, sl2);
		final IntBigList sl2b = l2.subList(0, 4); // 5, 0, 1, 2
		assertNotEquals(l1, sl2b);
	}

	@Test
	public void testEquals_OtherListImpl() {
		final IntBigArrayBigList baseList = of(2, 380, 1297);
		assertEquals(IntBigLists.unmodifiable(of(2, 380, 1297)), baseList);
		assertNotEquals(IntBigLists.unmodifiable(of(42, 420, 1337)), baseList);
	}

	@Test
	public void testCompareTo_AnotherArrayList() {
		final IntBigArrayBigList baseList = of(2, 380, 1297);
		final IntBigArrayBigList lessThenList = of(2, 365, 1297);
		final IntBigArrayBigList greaterThenList = of(2, 380, 1300);
		final IntBigArrayBigList lessBecauseItIsSmaller = of(2, 380);
		final IntBigArrayBigList greaterBecauseItIsLarger = of(2, 380, 1297, 1);
		final IntBigArrayBigList equalList = of(2, 380, 1297);
		assertTrue(baseList.compareTo(lessThenList) > 0);
		assertTrue(baseList.compareTo(greaterThenList) < 0);
		assertTrue(baseList.compareTo(lessBecauseItIsSmaller) > 0);
		assertTrue(baseList.compareTo(greaterBecauseItIsLarger) < 0);
		assertTrue(baseList.compareTo(equalList) == 0);
	}

	@Test
	public void testCompareTo_Sublist() {
		final IntBigArrayBigList baseList = of(2, 380, 1297);
		final IntBigList lessThenList = of(2, 365, 1297, 1).subList(0, 3);
		final IntBigList greaterThenList = of(2, 380, 1300, 1).subList(0, 3);
		final IntBigList lessBecauseItIsSmaller = of(2, 380, 1).subList(0, 2);
		final IntBigList greaterBecauseItIsLarger = of(2, 380, 1297, 1, 1).subList(0, 4);
		final IntBigList equalList = of(2, 380, 1297, 1).subList(0, 3);
		assertTrue(baseList.compareTo(lessThenList) > 0);
		assertTrue(baseList.compareTo(greaterThenList) < 0);
		assertTrue(baseList.compareTo(lessBecauseItIsSmaller) > 0);
		assertTrue(baseList.compareTo(greaterBecauseItIsLarger) < 0);
		assertTrue(baseList.compareTo(equalList) == 0);
	}

	@Test
	public void testCompareTo_OtherListImpl() {
		final IntBigArrayBigList baseList = of(2, 380, 1297);
		final IntBigList lessThenList = IntBigLists.unmodifiable(of(2, 365, 1297));
		final IntBigList greaterThenList = IntBigLists.unmodifiable(of(2, 380, 1300));
		final IntBigList lessBecauseItIsSmaller = IntBigLists.unmodifiable(of(2, 380));
		final IntBigList greaterBecauseItIsLarger = IntBigLists.unmodifiable(of(2, 380, 1297, 1));
		final IntBigList equalList = IntBigLists.unmodifiable(of(2, 380, 1297));
		assertTrue(baseList.compareTo(lessThenList) > 0);
		assertTrue(baseList.compareTo(greaterThenList) < 0);
		assertTrue(baseList.compareTo(lessBecauseItIsSmaller) > 0);
		assertTrue(baseList.compareTo(greaterBecauseItIsLarger) < 0);
		assertTrue(baseList.compareTo(equalList) == 0);
	}

	private static java.util.Random r = new java.util.Random(0);

	private static int genKey() {
		return r.nextInt();
	}

	private static Object[] k, nk;

	private static int kt[];

	private static int nkt[];

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected static void testLists(final IntBigList m, final IntBigList t, final int n, final int level) {
		Exception mThrowsOutOfBounds, tThrowsOutOfBounds;
		Object rt = null;
		int rm = (0);
		if (level > 4) return;
		/* Now we check that both sets agree on random keys. For m we use the polymorphic method. */
		for (int i = 0; i < n; i++) {
			int p = r.nextInt() % (n * 2);
			final int T = genKey();
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.set(p, T);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.set(p, (Integer.valueOf(T)));
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}

			if (mThrowsOutOfBounds == null)
			p = r.nextInt() % (n * 2);
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.getInt(p);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.get(p);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): get() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ aftre get() on position " + p + " (" + m.getInt(p) + ", " + t.get(p) + ")" ,  t.get(p).equals((Integer.valueOf(m.getInt(p)))));
		}
		/* Now we check that both sets agree on random keys. For m we use the standard method. */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (n * 2);
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.get(p);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.get(p);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): get() divergence at start in IndexOutOfBoundsException for index " + p+ "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ at start on position " + p + " (" + m.get(p) + ", " + t.get(p) + ")" ,  t.get(p).equals(m.get(p)));
		}
		/* Now we check that m and t are equal. */
		if (!m.equals(t) || !t.equals(m)) System.err.println("m: " + m + " t: " + t);
		assertTrue("Error (" + level + "): ! m.equals(t) at start" ,  m.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m) at start" ,  t.equals(m));
		/* Now we check that m actually holds that data. */
		for (final Iterator i = t.iterator(); i.hasNext();) {
			assertTrue("Error (" + level + "): m and t differ on an entry after insertion (iterating on t)" ,  m.contains(i.next()));
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for (final Iterator i = m.listIterator(); i.hasNext();) {
			assertTrue("Error (" + level + "): m and t differ on an entry after insertion (iterating on m)" ,  t.contains(i.next()));
		}
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for (int i = 0; i < n; i++) {
			final int T = genKey();
			assertTrue("Error (" + level + "): divergence in content between t and m (polymorphic method)" ,  m.contains(T) == t.contains((Integer.valueOf(T))));
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for (int i = 0; i < n; i++) {
			final int T = genKey();
			assertTrue("Error (" + level + "): divergence in content between t and m (polymorphic method)" ,  m.contains((Integer.valueOf(T))) == t.contains((Integer.valueOf(T))));
		}
		/* Now we add and remove random data in m and t, checking that the result is the same. */
		for (int i = 0; i < 2 * n; i++) {
			int T = genKey();
			try {
				m.add(T);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.add((Integer.valueOf(T)));
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			T = genKey();
			int p = r.nextInt() % (2 * n + 1);
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.add(p, T);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.add(p, (Integer.valueOf(T)));
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): add() divergence in IndexOutOfBoundsException for index " + p + " for " + T+ " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			p = r.nextInt() % (2 * n + 1);
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				rm = m.removeInt(p);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				rt = t.remove(p);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): remove() divergence in IndexOutOfBoundsException for index " + p + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): divergence in remove() between t and m (" + rt + ", " + rm + ")" ,  rt.equals((Integer.valueOf(rm))));
		}
		assertTrue("Error (" + level + "): ! m.equals(t) after add/remove" ,  m.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m) after add/remove" ,  t.equals(m));
		/*
		 * Now we add random data in m and t using addAll on a collection, checking that the result
		 * is the same.
		 */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (2 * n + 1);
			final java.util.Collection m1 = new java.util.ArrayList();
			final int s = r.nextInt(n / 2 + 1);
			for (int j = 0; j < s; j++)
				m1.add((Integer.valueOf(genKey())));
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.addAll(p, m1);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.addAll(p, m1);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): addAll() divergence in IndexOutOfBoundsException for index " + p + " for "+ m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error (" + level + "," + m + t + "): ! m.equals(t) after addAll" ,  m.equals(t));
			assertTrue("Error (" + level + "," + m + t + "): ! t.equals(m) after addAll" ,  t.equals(m));
		}
		if (m.size64() > n) {
			m.size(n);
			while (t.size() != n)
				t.remove(t.size() - 1);
		}
		/*
		 * Now we add random data in m and t using addAll on a type-specific collection, checking
		 * that the result is the same.
		 */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (2 * n + 1);
			final IntCollection m1 = new IntBigArrayBigList();
			final java.util.Collection t1 = new java.util.ArrayList();
			final int s = r.nextInt(n / 2 + 1);
			for (int j = 0; j < s; j++) {
				final int x = genKey();
				m1.add(x);
				t1.add((Integer.valueOf(x)));
			}
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.addAll(p, m1);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.addAll(p, t1);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): polymorphic addAll() divergence in IndexOutOfBoundsException for index "+ p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error (" + level + "," + m + t + "): ! m.equals(t) after polymorphic addAll" ,  m.equals(t));
			assertTrue("Error (" + level + "," + m + t + "): ! t.equals(m) after polymorphic addAll" ,  t.equals(m));
		}
		if (m.size64() > n) {
			m.size(n);
			while (t.size() != n)
				t.remove(t.size() - 1);
		}
		/*
		 * Now we add random data in m and t using addAll on a list, checking that the result is the
		 * same.
		 */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (2 * n + 1);
			final IntBigList m1 = new IntBigArrayBigList();
			final java.util.Collection t1 = new java.util.ArrayList();
			final int s = r.nextInt(n / 2 + 1);
			for (int j = 0; j < s; j++) {
				final int x = genKey();
				m1.add(x);
				t1.add((Integer.valueOf(x)));
			}
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.addAll(p, m1);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.addAll(p, t1);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): list addAll() divergence in IndexOutOfBoundsException for index " + p+ " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error (" + level + "): ! m.equals(t) after list addAll" ,  m.equals(t));
			assertTrue("Error (" + level + "): ! t.equals(m) after list addAll" ,  t.equals(m));
		}
		/* Now we check that both sets agree on random keys. For m we use the standard method. */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (n * 2);
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.get(p);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.get(p);
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): get() divergence in IndexOutOfBoundsException for index " + p + "  ("	+ mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")" ,  (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ on position " + p + " (" + m.get(p) + ", " + t.get(p)	+ ")" ,  t.get(p).equals(m.get(p)));
		}
		/* Now we inquiry about the content with indexOf()/lastIndexOf(). */
		for (int i = 0; i < 10 * n; i++) {
			final int T = genKey();
			assertTrue("Error (" + level + "): indexOf() divergence for " + T + "  (" + m.indexOf((Integer.valueOf(T))) + ", " + t.indexOf((Integer.valueOf(T))) + ")", m.indexOf((Integer.valueOf(T))) == t.indexOf((Integer.valueOf(T))));
			assertTrue("Error (" + level + "): lastIndexOf() divergence for " + T + "  (" + m.lastIndexOf((Integer.valueOf(T))) + ", " + t.lastIndexOf((Integer.valueOf(T)))	+ ")", m.lastIndexOf((Integer.valueOf(T))) == t.lastIndexOf((Integer.valueOf(T))));
			assertTrue("Error (" + level + "): polymorphic indexOf() divergence for " + T + "  (" + m.indexOf(T) + ", " + t.indexOf((Integer.valueOf(T))) + ")" ,  m.indexOf(T) == t.indexOf((Integer.valueOf(T))));
			assertTrue("Error (" + level + "): polymorphic lastIndexOf() divergence for " + T + "  (" + m.lastIndexOf(T) + ", " + t.lastIndexOf((Integer.valueOf(T))) + ")" ,  m.lastIndexOf(T) == t.lastIndexOf((Integer.valueOf(T))));
		}
		/* Now we check cloning. */
		if (level == 0) {
			assertTrue("Error (" + level + "): m does not equal m.clone()" ,  m.equals(((IntBigArrayBigList)m).clone()));
			assertTrue("Error (" + level + "): m.clone() does not equal m" ,  ((IntBigArrayBigList)m).clone().equals(m));
		}
		/* Now we play with constructors. */
		assertTrue("Error (" + level + "): m does not equal new (type-specific Collection m)" ,  m.equals(new IntBigArrayBigList((IntCollection)m)));
		assertTrue("Error (" + level + "): new (type-specific nCollection m) does not equal m" ,  (new IntBigArrayBigList((IntCollection)m)).equals(m));
		assertTrue("Error (" + level + "): m does not equal new (type-specific List m)" ,  m.equals(new IntBigArrayBigList(m)));
		assertTrue("Error (" + level + "): new (type-specific List m) does not equal m" ,  (new IntBigArrayBigList(m)).equals(m));
		assertTrue("Error (" + level + "): m does not equal new (m.listIterator())" ,  m.equals(new IntBigArrayBigList(m.listIterator())));
		assertTrue("Error (" + level + "): new (m.listIterator()) does not equal m" ,  (new IntBigArrayBigList(m.listIterator())).equals(m));
		assertTrue("Error (" + level + "): m does not equal new (m.type_specific_iterator())" ,  m.equals(new IntBigArrayBigList(m.iterator())));
		assertTrue("Error (" + level + "): new (m.type_specific_iterator()) does not equal m" ,  (new IntBigArrayBigList(m.iterator())).equals(m));
		final int h = m.hashCode();
		/* Now we save and read m. */
		IntBigList m2 = null;
		try {
			final java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test.junit." + m.getClass().getSimpleName() + "." + n);
			final java.io.OutputStream os = new java.io.FileOutputStream(ff);
			final java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);
			oos.writeObject(m);
			oos.close();
			final java.io.InputStream is = new java.io.FileInputStream(ff);
			final java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);
			m2 = (IntBigList)ois.readObject();
			ois.close();
			ff.delete();
		}
		catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		assertTrue("Error (" + level + "): hashCode() changed after save/read" ,  m2.hashCode() == h);
		/* Now we check that m2 actually holds that data. */
		assertTrue("Error (" + level + "): ! m2.equals(t) after save/read" ,  m2.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m2) after save/read" ,  t.equals(m2));
		/* Now we take out of m everything, and check that it is empty. */
		for (final Iterator i = t.iterator(); i.hasNext();)
			m2.remove(i.next());
		assertTrue("Error (" + level + "): m2 is not empty (as it should be)" ,  m2.isEmpty());
		/* Now we play with iterators. */
		{
			IntBigListIterator i;
			IntBigListIterator j;
			i = m.listIterator();
			j = t.listIterator();
			for (int k = 0; k < 2 * n; k++) {
				assertTrue("Error (" + level + "): divergence in hasNext()" ,  i.hasNext() == j.hasNext());
				assertTrue("Error (" + level + "): divergence in hasPrevious()" ,  i.hasPrevious() == j.hasPrevious());
				if (r.nextFloat() < .8 && i.hasNext()) {
					assertTrue("Error (" + level + "): divergence in next()" ,  i.next().equals(j.next()));
					if (r.nextFloat() < 0.2) {
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.set(T);
						j.set((Integer.valueOf(T)));
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.add(T);
						j.add((Integer.valueOf(T)));
					}
				}
				else if (r.nextFloat() < .2 && i.hasPrevious()) {
					assertTrue("Error (" + level + "): divergence in previous()" ,  i.previous().equals(j.previous()));
					if (r.nextFloat() < 0.2) {
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.set(T);
						j.set((Integer.valueOf(T)));
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.add(T);
						j.add((Integer.valueOf(T)));
					}
				}
				assertTrue("Error (" + level + "): divergence in nextIndex()" ,  i.nextIndex() == j.nextIndex());
				assertTrue("Error (" + level + "): divergence in previousIndex()" ,  i.previousIndex() == j.previousIndex());
			}
		}
		{
			Object I, J;
			final int from = r.nextInt(m.size() + 1);
			IntBigListIterator i;
			IntBigListIterator j;
			i = m.listIterator(from);
			j = t.listIterator(from);
			for (int k = 0; k < 2 * n; k++) {
				assertTrue("Error (" + level + "): divergence in hasNext() (iterator with starting point " + from + ")" ,  i.hasNext() == j.hasNext());
				assertTrue("Error (" + level + "): divergence in hasPrevious() (iterator with starting point " + from + ")" ,  i.hasPrevious() == j.hasPrevious());
				if (r.nextFloat() < .8 && i.hasNext()) {
					I = i.next();
					J = j.next();
					assertTrue("Error (" + level + "): divergence in next() (" + I + ", " + J + ", iterator with starting point " + from + ")" ,  I.equals(J));
					// System.err.println("Done next " + I + " " + J + "  " + badPrevious);
					if (r.nextFloat() < 0.2) {
						// System.err.println("Removing in next");
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.set(T);
						j.set((Integer.valueOf(T)));
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.add(T);
						j.add((Integer.valueOf(T)));
					}
				}
				else if (r.nextFloat() < .2 && i.hasPrevious()) {
					I = i.previous();
					J = j.previous();
					assertTrue("Error (" + level + "): divergence in previous() (" + I + ", " + J + ", iterator with starting point "	+ from + ")" ,  I.equals(J));
					if (r.nextFloat() < 0.2) {
						// System.err.println("Removing in prev");
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.set(T);
						j.set((Integer.valueOf(T)));
					}
					else if (r.nextFloat() < 0.2) {
						final int T = genKey();
						i.add(T);
						j.add((Integer.valueOf(T)));
					}
				}
			}
		}
		/* Now we check that m actually holds that data. */
		assertTrue("Error (" + level + "): ! m.equals(t) after iteration" ,  m.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m) after iteration" ,  t.equals(m));
		/* Now we select a pair of keys and create a subset. */
		if (!m.isEmpty()) {
			final int start = r.nextInt(m.size());
			final int end = start + r.nextInt(m.size() - start);
			// System.err.println("Checking subList from " + start + " to " + end + " (level=" +
			// (level+1) + ")...");
			testLists(m.subList(start, end), t.subList(start, end), n, level + 1);
			assertTrue("Error (" + level + "," + m + t + "): ! m.equals(t) after subList" ,  m.equals(t));
			assertTrue("Error (" + level + "): ! t.equals(m) after subList" ,  t.equals(m));
		}
		m.clear();
		t.clear();
		assertTrue("Error (" + level + "): m is not empty after clear()" ,  m.isEmpty());
	}

	@SuppressWarnings("deprecation")
	protected static void test(final int n) {
		final IntBigArrayBigList m = new IntBigArrayBigList();
		final IntBigList t = IntBigLists.asBigList(new IntArrayList());
		k = new Object[n];
		nk = new Object[n];
		kt = new int[n];
		nkt = new int[n];
		for (int i = 0; i < n; i++) {
			k[i] = new Integer(kt[i] = genKey());
			nk[i] = new Integer(nkt[i] = genKey());
		}
		/* We add pairs to t. */
		for (int i = 0; i < n; i++)
			t.add((Integer)k[i]);
		/* We add to m the same data */
		m.addAll(t);
		testLists(m, t, n, 0);
		return;
	}

	@Test
	public void test1() {
		test(1);
	}

	@Test
	public void test10() {
		test(10);
	}

	@Test
	public void test100() {
		test(100);
	}

	@Ignore("Too long")
	@Test
	public void test1000() {
		test(1000);
	}

	@Test
	public void testDefaultConstructors() {
		IntBigArrayBigList l;

		l = new IntBigArrayBigList();
		for(int i = 0; i < IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY + 2; i++) l.add(0);

		l = new IntBigArrayBigList();
		l.addElements(0, wrap(new int[IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY]), 0, IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY);

		l = new IntBigArrayBigList();
		l.addElements(0, wrap(new int[2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY]), 0, 2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY);

		l = new IntBigArrayBigList(0);
		for(int i = 0; i < IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY + 2; i++) l.add(0);

		l = new IntBigArrayBigList(0);
		l.addElements(0, wrap(new int[IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY]), 0, IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY);

		l = new IntBigArrayBigList(0);
		l.addElements(0, wrap(new int[2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY]), 0, 2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY);

		l = new IntBigArrayBigList(2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY );
		for(int i = 0; i < 3 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY; i++) l.add(0);

		l = new IntBigArrayBigList(2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY );
		l.addElements(0, wrap(new int[3 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY]), 0, 3 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY);

		l = new IntBigArrayBigList(2 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY );
		l.addElements(0, wrap(new int[3 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY]), 0, 3 * IntBigArrayBigList.DEFAULT_INITIAL_CAPACITY);

		// Test lazy allocation
		l = new IntBigArrayBigList();
		l.ensureCapacity(1000000);
		assertSame(IntBigArrays.DEFAULT_EMPTY_BIG_ARRAY, l.elements());

		l = new IntBigArrayBigList(0);
		l.ensureCapacity(1);
		assertNotSame(IntBigArrays.DEFAULT_EMPTY_BIG_ARRAY, l.elements());

		l = new IntBigArrayBigList(0);
		l.ensureCapacity(1);
		assertNotSame(IntBigArrays.DEFAULT_EMPTY_BIG_ARRAY, l.elements());
		l.ensureCapacity(1);
	}

	@Test
	public void testSizeOnDefaultInstance() {
		final IntBigArrayBigList l = new IntBigArrayBigList();
		l.size(100);
	}

	@Test
	public void testOf() {
		final IntBigArrayBigList l = IntBigArrayBigList.of(0, 1, 2);
		assertEquals(IntBigArrayBigList.wrap(wrap(new int[] { 0, 1, 2 })), l);
	}

	@Test
	public void testToBigList() {
		final IntBigArrayBigList baseList = IntBigArrayBigList.of(2, 380, 1297);
		// Also conveniently serves as a test of the intStream and spliterator.
		final IntBigArrayBigList transformed = IntBigArrayBigList.toBigList(baseList.intStream().map(i -> i + 40));
		assertEquals(IntBigArrayBigList.of(42, 420, 1337), transformed);
	}

	@Test
	public void testSpliteratorTrySplit() {
		final IntBigArrayBigList baseList = IntBigArrayBigList.of(0, 1, 2, 3, 72, 5, 6);
		final IntSpliterator willBeSuffix = baseList.spliterator();
		assertEquals(baseList.size64(), willBeSuffix.getExactSizeIfKnown());

		// Rather non-intuitively for finite sequences (but makes perfect sense for infinite ones),
		// the spec demands the original spliterator becomes the suffix and the new Spliterator becomes the prefix.
		final IntSpliterator prefix = willBeSuffix.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split into a prefix and suffix.
		final java.util.stream.IntStream suffixStream = java.util.stream.StreamSupport.intStream(willBeSuffix, false);
		final java.util.stream.IntStream prefixStream = java.util.stream.StreamSupport.intStream(prefix, false);

		final IntBigArrayBigList prefixList = IntBigArrayBigList.toBigList(prefixStream);
		final IntBigArrayBigList suffixList = IntBigArrayBigList.toBigList(suffixStream);
		assertEquals(baseList.size64(), prefixList.size64() + suffixList.size64());
		assertEquals(baseList.subList(0, prefixList.size64()), prefixList);
		assertEquals(baseList.subList(prefixList.size64(), baseList.size64()), suffixList);
		final IntBigArrayBigList recombinedList = new IntBigArrayBigList(baseList.size64());
		recombinedList.addAll(prefixList);
		recombinedList.addAll(suffixList);
		assertEquals(baseList, recombinedList);
	}

	@Test
	public void testSpliteratorSkip() {
		final IntBigArrayBigList baseList = IntBigArrayBigList.of(0, 1, 2, 3, 72, 5, 6);
		final IntSpliterator spliterator = baseList.spliterator();
		assertEquals(1, spliterator.skip(1));
		final java.util.stream.IntStream stream = java.util.stream.StreamSupport.intStream(spliterator, false);
		assertEquals(baseList.subList(1, baseList.size64()), IntBigArrayBigList.toBigList(stream));
	}

	@Test
	public void testSubList_testEquals_ArrayList() {
		final IntBigArrayBigList l = of(0, 1, 2, 3);
		final IntBigList sl = l.subList(0, 3);
		assertEquals(of(0, 1, 2), sl);
		assertNotEquals(of(0, 1, 3), sl);
	}

	@Test
	public void testSubList_testEquals_Sublist() {
		final IntBigArrayBigList l1 = of(0, 1, 2, 3);
		final IntBigArrayBigList l2 = of(5, 0, 1, 2, 3, 4);
		final IntBigList sl1 = l1.subList(0, 3); // 0, 1, 2
		final IntBigList sl2 = l2.subList(1, 4); // 0, 1, 2
		assertEquals(sl1, sl2);
		final IntBigList sl3 = l2.subList(0, 3); // 5, 0, 1
		assertNotEquals(sl1, sl3);
	}

	@Test
	public void testSubList_testEquals_OtherListImpl() {
		final IntBigArrayBigList l = of(0, 1, 2, 3);
		final IntBigList sl = l.subList(0, 3);
		assertEquals(IntBigLists.unmodifiable(of(0, 1, 2)), sl);
		assertNotEquals(IntBigLists.unmodifiable(of(0, 1, 3)), sl);
	}

	@Test
	public void testSubList_testCompareTo_ArrayList() {
		final IntBigList baseList = of(2, 380, 1297, 1).subList(0, 3);
		final IntBigArrayBigList lessThenList = of(2, 365, 1297);
		final IntBigArrayBigList greaterThenList = of(2, 380, 1300);
		final IntBigArrayBigList lessBecauseItIsSmaller = of(2, 380);
		final IntBigArrayBigList greaterBecauseItIsLarger = of(2, 380, 1297, 1);
		final IntBigArrayBigList equalList = of(2, 380, 1297);
		assertTrue(baseList.compareTo(lessThenList) > 0);
		assertTrue(baseList.compareTo(greaterThenList) < 0);
		assertTrue(baseList.compareTo(lessBecauseItIsSmaller) > 0);
		assertTrue(baseList.compareTo(greaterBecauseItIsLarger) < 0);
		assertTrue(baseList.compareTo(equalList) == 0);
	}

	@Test
	public void testSubList_testCompareTo_Sublist() {
		final IntList baseList = IntArrayList.of(2, 380, 1297, 1).subList(0, 3);
		final IntList lessThenList = IntArrayList.of(2, 365, 1297, 1).subList(0, 3);
		final IntList greaterThenList = IntArrayList.of(2, 380, 1300, 1).subList(0, 3);
		final IntList lessBecauseItIsSmaller = IntArrayList.of(2, 380, 1).subList(0, 2);
		final IntList greaterBecauseItIsLarger = IntArrayList.of(2, 380, 1297, 1, 1).subList(0, 4);
		final IntList equalList = IntArrayList.of(2, 380, 1297, 1).subList(0, 3);
		assertTrue(baseList.compareTo(lessThenList) > 0);
		assertTrue(baseList.compareTo(greaterThenList) < 0);
		assertTrue(baseList.compareTo(lessBecauseItIsSmaller) > 0);
		assertTrue(baseList.compareTo(greaterBecauseItIsLarger) < 0);
		assertTrue(baseList.compareTo(equalList) == 0);
	}

	@Test
	public void testSubList_testCompareTo_OtherListImpl() {
		final IntList baseList = IntArrayList.of(2, 380, 1297, 1).subList(0, 3);
		final IntImmutableList lessThenList = IntImmutableList.of(2, 365, 1297);
		final IntImmutableList greaterThenList = IntImmutableList.of(2, 380, 1300);
		final IntImmutableList lessBecauseItIsSmaller = IntImmutableList.of(2, 380);
		final IntImmutableList greaterBecauseItIsLarger = IntImmutableList.of(2, 380, 1297, 1);
		final IntImmutableList equalList = IntImmutableList.of(2, 380, 1297);
		assertTrue(baseList.compareTo(lessThenList) > 0);
		assertTrue(baseList.compareTo(greaterThenList) < 0);
		assertTrue(baseList.compareTo(lessBecauseItIsSmaller) > 0);
		assertTrue(baseList.compareTo(greaterBecauseItIsLarger) < 0);
		assertTrue(baseList.compareTo(equalList) == 0);
	}

	@Test
	public void testSubList_testSpliteratorTrySplit() {
		final IntBigList baseList = of(0, 1, 2, 3, 72, 5, 6).subList(1, 5); // 1, 2, 3, 72, 5
		final IntSpliterator willBeSuffix = baseList.spliterator();
		assertEquals(baseList.size64(), willBeSuffix.getExactSizeIfKnown());
		// Rather non-intuitively for finite sequences (but makes perfect sense for infinite ones),
		// the spec demands the original spliterator becomes the suffix and the new Spliterator becomes the prefix.
		final IntSpliterator prefix = willBeSuffix.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split into a prefix and suffix.
		final java.util.stream.IntStream suffixStream = java.util.stream.StreamSupport.intStream(willBeSuffix, false);
		final java.util.stream.IntStream prefixStream = java.util.stream.StreamSupport.intStream(prefix, false);

		final IntBigArrayBigList prefixList = IntBigArrayBigList.toBigList(prefixStream);
		final IntBigArrayBigList suffixList = IntBigArrayBigList.toBigList(suffixStream);
		assertEquals(baseList.size64(), prefixList.size64() + suffixList.size64());
		assertEquals(baseList.subList(0, prefixList.size64()), prefixList);
		assertEquals(baseList.subList(prefixList.size64(), baseList.size64()), suffixList);
		final IntBigArrayBigList recombinedList = new IntBigArrayBigList(baseList.size64());
		recombinedList.addAll(prefixList);
		recombinedList.addAll(suffixList);
		assertEquals(baseList, recombinedList);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(IntBigArrayBigList.class, "test", /*num=*/"200", /*seed=*/"90293");
	}

	@Test
	public void testReallyLargeListIteration() {
		final AbstractIntBigList l = new AbstractIntBigList() {

			@Override
			public int getInt(final long index) {
				return 0;
			}

			@Override
			public long size64() {
				return 1L << 31;
			}

		};
		final IntBigListIterator iterator = l.iterator();
		for (long i = 0; i < (1L << 31); i++) iterator.nextInt();
		assertFalse(iterator.hasNext());
		for (long i = 0; i < (1L << 31); i++) iterator.previousInt();
		assertFalse(iterator.hasPrevious());
	}
}

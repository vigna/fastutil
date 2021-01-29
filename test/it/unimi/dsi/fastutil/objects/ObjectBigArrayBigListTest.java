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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.ints.IntList;

@SuppressWarnings("rawtypes")
public class ObjectBigArrayBigListTest {

	@Test
	public void testRemoveAllModifiesCollection() {
		final ObjectBigList<Integer> list = new ObjectBigArrayBigList<>();
		assertFalse(list.removeAll(Collections.emptySet()));
		assertEquals(ObjectBigLists.EMPTY_BIG_LIST, list);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testRemoveAllSkipSegment() {
		final ObjectBigList<Integer> list = new ObjectBigArrayBigList<>();
		for(long i = 0; i < BigArrays.SEGMENT_SIZE + 10; i++) list.add(Integer.valueOf((int)(i % 2)));
		assertTrue(list.removeAll(ObjectSets.singleton(1)));
		assertEquals((BigArrays.SEGMENT_SIZE + 1) / 2 + 5, list.size64());
		for (long i = 0; i < (BigArrays.SEGMENT_SIZE + 1) / 2 + 5; i++) assertEquals(Integer.valueOf(0), list.get(i));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testRemoveAll() {
		final ObjectBigArrayBigList<Integer> l = new ObjectBigArrayBigList<>(ObjectBigArrayBigList.of(0, 1, 1, 2));
		l.removeAll(ObjectSets.singleton(1));
		assertEquals(ObjectBigArrayBigList.of(0, 2), l);
		final Object[][] elements = l.elements();
		assertNull(BigArrays.get(elements, 2));
		assertNull(BigArrays.get(elements, 3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testAddAllCollection() {
		final ObjectBigArrayBigList<Integer> l = new ObjectBigArrayBigList<>(ObjectBigArrayBigList.of(0, 1, 1, 2));
		final List<Integer> m = Arrays.asList(new Integer[] { 2, 3, 3, 4 });
		l.addAll(0, m);
		assertEquals(ObjectBigArrayBigList.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, IntList.of());
		l.addAll(2, IntList.of());
		assertEquals(ObjectBigArrayBigList.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, (Collection<Integer>)ObjectList.of(0));
		assertEquals(ObjectBigArrayBigList.of(0, 2, 3, 3, 4, 0, 1, 1, 2), l);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testAddAllList() {
		final ObjectBigArrayBigList<Integer> l = new ObjectBigArrayBigList<>(ObjectBigArrayBigList.of(0, 1, 1, 2));
		final ObjectList<Integer> m = ObjectList.of(2, 3, 3, 4);
		l.addAll(0, m);
		assertEquals(ObjectBigArrayBigList.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, List.of());
		l.addAll(2, List.of());
		assertEquals(ObjectBigArrayBigList.of(2, 3, 3, 4, 0, 1, 1, 2), l);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testAddAllBigList() {
		final ObjectBigArrayBigList<Integer> l = new ObjectBigArrayBigList<>(ObjectBigArrayBigList.of(0, 1, 1, 2));
		final ObjectBigArrayBigList<Integer> m = ObjectBigArrayBigList.of(2, 3, 3, 4);
		l.addAll(0, m);
		assertEquals(ObjectBigArrayBigList.of(2, 3, 3, 4, 0, 1, 1, 2), l);
		l.addAll(0, ObjectBigArrayBigList.of());
		l.addAll(2, ObjectBigArrayBigList.of());
		assertEquals(ObjectBigArrayBigList.of(2, 3, 3, 4, 0, 1, 1, 2), l);
	}

	@Test
	public void testOf() {
		final ObjectBigArrayBigList<String> l = ObjectBigArrayBigList.of("0", "1", "2");
		assertEquals(new ObjectBigArrayBigList<>(List.of("0", "1", "2")), l);
	}

	@Test
	public void testToBigList() {
		final ObjectBigArrayBigList<String> baseList = ObjectBigArrayBigList.of("wood", "board", "glass", "metal");
		final ObjectBigArrayBigList<String> transformed = baseList.stream().map(s -> "ply" + s).collect(ObjectBigArrayBigList.toBigList());
		assertEquals(ObjectBigArrayBigList.of("plywood", "plyboard", "plyglass", "plymetal"), transformed);
	}

	@Test
	public void testSpliteratorTrySplit() {
		final ObjectBigArrayBigList<String> baseList = ObjectBigArrayBigList.of("0", "1", "2", "3", "4", "5", "bird");
		final ObjectSpliterator<String> willBeSuffix = baseList.spliterator();
		assertEquals(baseList.size64(), willBeSuffix.getExactSizeIfKnown());
		// Rather non-intuitively for finite sequences (but makes perfect sense for infinite ones),
		// the spec demands the original spliterator becomes the suffix and the new Spliterator becomes the prefix.
		final ObjectSpliterator<String> prefix = willBeSuffix.trySplit();
		// No assurance of where we split, but where ever it is it should be a perfect split into a prefix and suffix.
		final java.util.stream.Stream<String> suffixStream = java.util.stream.StreamSupport.stream(willBeSuffix, false);
		final java.util.stream.Stream<String> prefixStream = java.util.stream.StreamSupport.stream(prefix, false);

		final ObjectBigArrayBigList<String> prefixList = prefixStream.collect(ObjectBigArrayBigList.toBigList());
		final ObjectBigArrayBigList<String> suffixList = suffixStream.collect(ObjectBigArrayBigList.toBigList());
		assertEquals(baseList.size64(), prefixList.size64() + suffixList.size64());
		assertEquals(baseList.subList(0, prefixList.size64()), prefixList);
		assertEquals(baseList.subList(prefixList.size64(), baseList.size64()), suffixList);
		final ObjectBigArrayBigList<String> recombinedList = new ObjectBigArrayBigList<>(baseList.size64());
		recombinedList.addAll(prefixList);
		recombinedList.addAll(suffixList);
		assertEquals(baseList, recombinedList);
	}

	private static java.util.Random r = new java.util.Random(0);

	private static int genKey() {
		return r.nextInt();
	}

	private static Object[] k, nk;

	private static Object kt[];

	private static Object nkt[];

	@SuppressWarnings({ "unchecked", "boxing" })
	protected static void testLists(final ObjectBigList m, final ObjectBigList t, final int n, final int level) {
		Exception mThrowsOutOfBounds, tThrowsOutOfBounds;
		Object rt = null;
		Object rm = (null);
		if (level > 4) return;
		/* Now we check that both sets agree on random keys. For m we use the polymorphic method. */
		for (int i = 0; i < n; i++) {
			int p = r.nextInt() % (n * 2);
			final Object T = genKey();
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.set(p, T);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.set(p, (T));
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): set() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ after set() on position " + p + " (" + m.get(p) + ", " + t.get(p) + ")",
					t.get(p).equals((m.get(p))));
			p = r.nextInt() % (n * 2);
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
			assertTrue("Error (" + level + "): get() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ aftre get() on position " + p + " (" + m.get(p) + ", " + t.get(p) + ")",
					t.get(p).equals((m.get(p))));
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
			assertTrue("Error (" + level + "): get() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ at start on position " + p + " (" + m.get(p) + ", " + t.get(p) + ")", t.get(p)
					.equals(m.get(p)));
		}
		/* Now we check that m and t are equal. */
		if (!m.equals(t) || !t.equals(m)) System.err.println("m: " + m + " t: " + t);
		assertTrue("Error (" + level + "): ! m.equals(t) at start", m.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m) at start", t.equals(m));
		/* Now we check that m actually holds that data. */
		for (final Iterator i = t.iterator(); i.hasNext();) {
			assertTrue("Error (" + level + "): m and t differ on an entry after insertion (iterating on t)", m.contains(i.next()));
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for (final Iterator i = m.listIterator(); i.hasNext();) {
			assertTrue("Error (" + level + "): m and t differ on an entry after insertion (iterating on m)", t.contains(i.next()));
		}
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for (int i = 0; i < n; i++) {
			final Object T = genKey();
			assertTrue("Error (" + level + "): divergence in content between t and m (polymorphic method)", m.contains(T) == t.contains((T)));
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for (int i = 0; i < n; i++) {
			final Object T = genKey();
			assertTrue("Error (" + level + "): divergence in content between t and m (polymorphic method)", m.contains((T)) == t.contains((T)));
		}
		/* Now we add and remove random data in m and t, checking that the result is the same. */
		for (int i = 0; i < 2 * n; i++) {
			Object T = genKey();
			try {
				m.add(T);
			}
			catch (final IndexOutOfBoundsException e) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.add((T));
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
				t.add(p, (T));
			}
			catch (final IndexOutOfBoundsException e) {
				tThrowsOutOfBounds = e;
			}
			assertTrue("Error (" + level + "): add() divergence in IndexOutOfBoundsException for index " + p + " for " + T + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			p = r.nextInt() % (2 * n + 1);
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				rm = m.remove(p);
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
			assertTrue("Error (" + level + "): remove() divergence in IndexOutOfBoundsException for index " + p + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): divergence in remove() between t and m (" + rt + ", " + rm + ")", rt.equals((rm)));
		}
		assertTrue("Error (" + level + "): ! m.equals(t) after add/remove", m.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m) after add/remove", t.equals(m));
		/*
		 * Now we add random data in m and t using addAll on a collection, checking that the result
		 * is the same.
		 */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (2 * n + 1);
			final java.util.Collection m1 = new java.util.ArrayList();
			final int s = r.nextInt(n / 2 + 1);
			for (int j = 0; j < s; j++)
				m1.add((genKey()));
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
			assertTrue("Error (" + level + "): addAll() divergence in IndexOutOfBoundsException for index " + p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error (" + level + m + t + "): ! m.equals(t) after addAll", m.equals(t));
			assertTrue("Error (" + level + m + t + "): ! t.equals(m) after addAll", t.equals(m));
		}
		if (m.size64() > n) {
			m.size(n);
			while (t.size64() != n)
				t.remove(t.size64() - 1);
		}
		/*
		 * Now we add random data in m and t using addAll on a type-specific collection, checking
		 * that the result is the same.
		 */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (2 * n + 1);
			final ObjectCollection m1 = new ObjectBigArrayBigList();
			final java.util.Collection t1 = new java.util.ArrayList();
			final int s = r.nextInt(n / 2 + 1);
			for (int j = 0; j < s; j++) {
				final Object x = genKey();
				m1.add(x);
				t1.add((x));
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
			assertTrue("Error (" + level + "): polymorphic addAll() divergence in IndexOutOfBoundsException for index " + p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds
					+ ")", (mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error (" + level + m + t + "): ! m.equals(t) after polymorphic addAll", m.equals(t));
			assertTrue("Error (" + level + m + t + "): ! t.equals(m) after polymorphic addAll", t.equals(m));
		}
		if (m.size64() > n) {
			m.size(n);
			while (t.size64() != n)
				t.remove(t.size64() - 1);
		}
		/*
		 * Now we add random data in m and t using addAll on a list, checking that the result is the
		 * same.
		 */
		for (int i = 0; i < n; i++) {
			final int p = r.nextInt() % (2 * n + 1);
			final ObjectBigList m1 = new ObjectBigArrayBigList();
			final java.util.Collection t1 = new java.util.ArrayList();
			final int s = r.nextInt(n / 2 + 1);
			for (int j = 0; j < s; j++) {
				final Object x = genKey();
				m1.add(x);
				t1.add((x));
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
			assertTrue("Error (" + level + "): list addAll() divergence in IndexOutOfBoundsException for index " + p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			assertTrue("Error (" + level + "): ! m.equals(t) after list addAll", m.equals(t));
			assertTrue("Error (" + level + "): ! t.equals(m) after list addAll", t.equals(m));
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
			assertTrue("Error (" + level + "): get() divergence in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					(mThrowsOutOfBounds == null) == (tThrowsOutOfBounds == null));
			if (mThrowsOutOfBounds == null) assertTrue("Error (" + level + "): m and t differ on position " + p + " (" + m.get(p) + ", " + t.get(p) + ")", t.get(p).equals(m.get(p)));
		}
		/* Now we inquiry about the content with indexOf()/lastIndexOf(). */
		for (int i = 0; i < 10 * n; i++) {
			final Object T = genKey();
			assertTrue("Error (" + level + "): indexOf() divergence for " + T + "  (" + m.indexOf((T)) + ", " + t.indexOf((T)) + ")", m.indexOf((T)) == t.indexOf((T)));
			assertTrue("Error (" + level + "): lastIndexOf() divergence for " + T + "  (" + m.lastIndexOf((T)) + ", " + t.lastIndexOf((T)) + ")",
					m.lastIndexOf((T)) == t.lastIndexOf((T)));
			assertTrue("Error (" + level + "): polymorphic indexOf() divergence for " + T + "  (" + m.indexOf(T) + ", " + t.indexOf((T)) + ")", m.indexOf(T) == t.indexOf((T)));
			assertTrue("Error (" + level + "): polymorphic lastIndexOf() divergence for " + T + "  (" + m.lastIndexOf(T) + ", " + t.lastIndexOf((T)) + ")",
					m.lastIndexOf(T) == t.lastIndexOf((T)));
		}
		/* Now we check cloning. */
		if (level == 0) {
			assertTrue("Error (" + level + "): m does not equal m.clone()", m.equals(((ObjectBigArrayBigList)m).clone()));
			assertTrue("Error (" + level + "): m.clone() does not equal m", ((ObjectBigArrayBigList)m).clone().equals(m));
		}
		/* Now we play with constructors. */
		assertTrue("Error (" + level + "): m does not equal new (type-specific Collection m)", m.equals(new ObjectBigArrayBigList((ObjectCollection)m)));
		assertTrue("Error (" + level + "): new (type-specific nCollection m) does not equal m", (new ObjectBigArrayBigList((ObjectCollection)m)).equals(m));
		assertTrue("Error (" + level + "): m does not equal new (type-specific List m)", m.equals(new ObjectBigArrayBigList(m)));
		assertTrue("Error (" + level + "): new (type-specific List m) does not equal m", (new ObjectBigArrayBigList(m)).equals(m));
		assertTrue("Error (" + level + "): m does not equal new (m.listIterator())", m.equals(new ObjectBigArrayBigList(m.listIterator())));
		assertTrue("Error (" + level + "): new (m.listIterator()) does not equal m", (new ObjectBigArrayBigList(m.listIterator())).equals(m));
		assertTrue("Error (" + level + "): m does not equal new (m.type_specific_iterator())", m.equals(new ObjectBigArrayBigList(m.iterator())));
		assertTrue("Error (" + level + "): new (m.type_specific_iterator()) does not equal m", (new ObjectBigArrayBigList(m.iterator())).equals(m));
		final int h = m.hashCode();
		/* Now we save and read m. */
		ObjectBigList m2 = null;
		try {
			final java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test.junit." + m.getClass().getSimpleName() + "." + n);
			final java.io.OutputStream os = new java.io.FileOutputStream(ff);
			final java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);
			oos.writeObject(m);
			oos.close();
			final java.io.InputStream is = new java.io.FileInputStream(ff);
			final java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);
			m2 = (ObjectBigList)ois.readObject();
			ois.close();
			ff.delete();
		}
		catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		assertTrue("Error (" + level + "): hashCode() changed after save/read", m2.hashCode() == h);
		/* Now we check that m2 actually holds that data. */
		assertTrue("Error (" + level + "): ! m2.equals(t) after save/read", m2.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m2) after save/read", t.equals(m2));
		/* Now we take out of m everything, and check that it is empty. */
		for (final Iterator i = t.iterator(); i.hasNext();)
			m2.remove(i.next());
		assertTrue("Error (" + level + "): m2 is not empty (as it should be)", m2.isEmpty());
		/* Now we play with iterators. */
		{
			ObjectBigListIterator i;
			ObjectBigListIterator j;
			i = m.listIterator();
			j = t.listIterator();
			for (int k = 0; k < 2 * n; k++) {
				assertTrue("Error (" + level + "): divergence in hasNext()", i.hasNext() == j.hasNext());
				assertTrue("Error (" + level + "): divergence in hasPrevious()", i.hasPrevious() == j.hasPrevious());
				if (r.nextFloat() < .8 && i.hasNext()) {
					assertTrue("Error (" + level + "): divergence in next()", i.next().equals(j.next()));
					if (r.nextFloat() < 0.2) {
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.set(T);
						j.set((T));
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.add(T);
						j.add((T));
					}
				}
				else if (r.nextFloat() < .2 && i.hasPrevious()) {
					assertTrue("Error (" + level + "): divergence in previous()", i.previous().equals(j.previous()));
					if (r.nextFloat() < 0.2) {
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.set(T);
						j.set((T));
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.add(T);
						j.add((T));
					}
				}
				assertTrue("Error (" + level + "): divergence in nextIndex()", i.nextIndex() == j.nextIndex());
				assertTrue("Error (" + level + "): divergence in previousIndex()", i.previousIndex() == j.previousIndex());
			}
		}
		{
			Object I, J;
			final long from = (r.nextLong() >>> 1) % (m.size64() + 1);
			ObjectBigListIterator i;
			ObjectBigListIterator j;
			i = m.listIterator(from);
			j = t.listIterator(from);
			for (int k = 0; k < 2 * n; k++) {
				assertTrue("Error (" + level + "): divergence in hasNext() (iterator with starting point " + from + ")", i.hasNext() == j.hasNext());
				assertTrue("Error (" + level + "): divergence in hasPrevious() (iterator with starting point " + from + ")", i.hasPrevious() == j.hasPrevious());
				if (r.nextFloat() < .8 && i.hasNext()) {
					I = i.next();
					J = j.next();
					assertTrue("Error (" + level + "): divergence in next() (" + I + ", " + J + ", iterator with starting point " + from + ")", I.equals(J));
					// System.err.println("Done next " + I + " " + J + "  " + badPrevious);
					if (r.nextFloat() < 0.2) {
						// System.err.println("Removing in next");
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.set(T);
						j.set((T));
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.add(T);
						j.add((T));
					}
				}
				else if (r.nextFloat() < .2 && i.hasPrevious()) {
					I = i.previous();
					J = j.previous();
					assertTrue("Error (" + level + "): divergence in previous() (" + I + ", " + J + ", iterator with starting point " + from + ")", I.equals(J));
					if (r.nextFloat() < 0.2) {
						// System.err.println("Removing in prev");
						i.remove();
						j.remove();
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.set(T);
						j.set((T));
					}
					else if (r.nextFloat() < 0.2) {
						final Object T = genKey();
						i.add(T);
						j.add((T));
					}
				}
			}
		}
		/* Now we check that m actually holds that data. */
		assertTrue("Error (" + level + "): ! m.equals(t) after iteration", m.equals(t));
		assertTrue("Error (" + level + "): ! t.equals(m) after iteration", t.equals(m));
		/* Now we select a pair of keys and create a subset. */
		if (!m.isEmpty()) {
			final long start = (r.nextLong() >>> 1) % m.size64();
			final long end = start + (r.nextLong() >>> 1) % (m.size64() - start);
			// System.err.println("Checking subList from " + start + " to " + end + " (level=" +
			// (level+1) + ")...");
			testLists(m.subList(start, end), t.subList(start, end), n, level + 1);
			assertTrue("Error (" + level + m + t + "): ! m.equals(t) after subList", m.equals(t));
			assertTrue("Error (" + level + "): ! t.equals(m) after subList", t.equals(m));
		}
		m.clear();
		t.clear();
		assertTrue("Error (" + level + "): m is not empty after clear()", m.isEmpty());
	}

	@SuppressWarnings({ "boxing", "unchecked" })
	protected static void test(final int n) {
		ObjectBigArrayBigList m = new ObjectBigArrayBigList();
		ObjectBigList t = ObjectBigLists.asBigList(new ObjectArrayList());
		k = new Object[n];
		nk = new Object[n];
		kt = new Object[n];
		nkt = new Object[n];
		for (int i = 0; i < n; i++) {
			k[i] = kt[i] = genKey();
			nk[i] = nkt[i] = genKey();
		}
		/* We add pairs to t. */
		for (int i = 0; i < n; i++) t.add(k[i]);
		/* We add to m the same data */
		m.addAll(t);
		testLists(m, t, n, 0);

		// This tests all reflection-based methods.
		m = ObjectBigArrayBigList.wrap(ObjectBigArrays.EMPTY_BIG_ARRAY);
		t = ObjectBigLists.asBigList(new ObjectArrayList());
		/* We add pairs to t. */
		for (int i = 0; i < n; i++) t.add(k[i]);
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
	public void testSizeOnDefaultInstance() {
		final ObjectBigArrayBigList<Integer> l = new ObjectBigArrayBigList<>();
		l.size(100);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(ObjectBigArrayBigList.class, "test", /*num=*/"200", /*seed=*/"90293");
	}
}

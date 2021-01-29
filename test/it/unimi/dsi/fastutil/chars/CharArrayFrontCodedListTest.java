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

package it.unimi.dsi.fastutil.chars;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CharArrayFrontCodedListTest {


	private static java.util.Random r = new java.util.Random(0);

	private static char genKey() {
		return (char)(r.nextInt());
	}

	private static boolean contentEquals(final java.util.List x, final java.util.List y) {
		if (x.size() != y.size()) return false;
		for (int i = 0; i < x.size(); i++)
			if (!java.util.Arrays.equals((char[])x.get(i), (char[])y.get(i))) return false;
		return true;
	}

	private static int l[];

	private static char[][] a;

	private static void test(final int n) throws IOException, ClassNotFoundException {
		l = new int[n];
		a = new char[n][];
		for (int i = 0; i < n; i++)
			l[i] = (int)(Math.abs(r.nextGaussian()) * 32);
		for (int i = 0; i < n; i++)
			a[i] = new char[l[i]];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < l[i]; j++)
				a[i][j] = genKey();
		CharArrayFrontCodedList m = new CharArrayFrontCodedList(it.unimi.dsi.fastutil.objects.ObjectIterators.wrap(a), r.nextInt(4) + 1);
		final it.unimi.dsi.fastutil.objects.ObjectArrayList t = new it.unimi.dsi.fastutil.objects.ObjectArrayList(a);
		// System.out.println(m);
		// for(i = 0; i < t.size(); i++)
		// System.out.println(ARRAY_LIST.wrap((KEY_TYPE[])t.get(i)));
		/* Now we check that m actually holds that data. */
		assertTrue("Error: m does not equal t at creation", contentEquals(m, t));
		/* Now we check cloning. */
		assertTrue("Error: m does not equal m.clone()", contentEquals(m, m.clone()));
		/* Now we play with iterators. */
		{
			ObjectListIterator i;
			java.util.ListIterator j;
			i = m.listIterator();
			j = t.listIterator();
			for (int k = 0; k < 2 * n; k++) {
				assertTrue("Error: divergence in hasNext()", i.hasNext() == j.hasNext());
				assertTrue("Error: divergence in hasPrevious()", i.hasPrevious() == j.hasPrevious());
				if (r.nextFloat() < .8 && i.hasNext()) {
					assertTrue("Error: divergence in next()", java.util.Arrays.equals((char[])i.next(), (char[])j.next()));
				}
				else if (r.nextFloat() < .2 && i.hasPrevious()) {
					assertTrue("Error: divergence in previous()", java.util.Arrays.equals((char[])i.previous(), (char[])j.previous()));
				}
				assertTrue("Error: divergence in nextIndex()", i.nextIndex() == j.nextIndex());
				assertTrue("Error: divergence in previousIndex()", i.previousIndex() == j.previousIndex());
			}
		}
		{
			final int from = r.nextInt(m.size() + 1);
			ObjectListIterator i;
			java.util.ListIterator j;
			i = m.listIterator(from);
			j = t.listIterator(from);
			for (int k = 0; k < 2 * n; k++) {
				assertTrue("Error: divergence in hasNext() (iterator with starting point " + from + ")", i.hasNext() == j.hasNext());
				assertTrue("Error: divergence in hasPrevious() (iterator with starting point " + from + ")", i.hasPrevious() == j.hasPrevious());
				if (r.nextFloat() < .8 && i.hasNext()) {
					assertTrue("Error: divergence in next() (iterator with starting point " + from + ")", java.util.Arrays.equals((char[])i.next(), (char[])j.next()));
					// System.err.println("Done next " + I + " " + J + "  " + badPrevious);
				}
				else if (r.nextFloat() < .2 && i.hasPrevious()) {
					assertTrue("Error: divergence in previous() (iterator with starting point " + from + ")", java.util.Arrays.equals((char[])i.previous(), (char[])j.previous()));
				}
			}
		}
		final java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test.junit." + m.getClass().getSimpleName() + "." + n);
		final java.io.OutputStream os = new java.io.FileOutputStream(ff);
		final java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);
		oos.writeObject(m);
		oos.close();
		final java.io.InputStream is = new java.io.FileInputStream(ff);
		final java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);
		m = (CharArrayFrontCodedList)ois.readObject();
		ois.close();
		ff.delete();
		assertTrue("Error: m does not equal t after save/read", contentEquals(m, t));
		return;
	}


	@Test
	public void test1() throws IOException, ClassNotFoundException {
		test(1);
	}

	@Test
	public void test10() throws Exception, ClassNotFoundException {
		test(10);
	}

	@Test
	public void test100() throws IOException, ClassNotFoundException {
		test(100);
	}

	@Test
	public void test1000() throws IOException, ClassNotFoundException {
		test(1000);
	}

	@Test
	public void test10000() throws IOException, ClassNotFoundException {
		test(10000);
	}
}

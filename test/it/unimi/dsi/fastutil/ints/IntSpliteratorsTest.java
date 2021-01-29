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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IntSpliteratorsTest {

	@Parameters(name = "useSplits = {0}")
	public static Object[] data() {
		return new Boolean[] { Boolean.FALSE, Boolean.TRUE };
	}

	// Using Boolean object so we can set to null to get NullPointerException if the parameterization doesn't trigger.
	@Parameter
	public Boolean useSplits = null;

	@Test
	public void testArraySpliterator() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final IntSpliterator m = IntSpliterators.wrap(a);
		final java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.NONNULL, m.characteristics());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final IntStream tStream = StreamSupport.intStream(t, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testArraySpliteratorSkip() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final int[] expected = { 4, 10, 5, 42, 10239 };
		final IntSpliterator m = IntSpliterators.wrap(a);
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expected.length, m.getExactSizeIfKnown());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testArraySpliteratorWithComparator() {
		final int[] a = { 10239, 42, 10, 5, 4, 3, 2 };
		final IntComparator comparator = IntComparators.OPPOSITE_COMPARATOR;
		final IntSpliterator m = IntSpliterators.wrapPreSorted(a, comparator);
		final java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, Spliterator.SORTED);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SORTED, m.characteristics());
		assertEquals(m.getComparator(), comparator);
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final IntStream tStream = StreamSupport.intStream(t, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testFromToSpliterator() {
		final int from = -5;
		final int to = 17;
		final int expectedSize = to - from;
		final IntSpliterator m = IntSpliterators.fromTo(from, to);
		final Spliterator.OfInt t = java.util.stream.IntStream.range(from, to).spliterator();
		assertEquals(expectedSize, m.estimateSize());
		assertEquals(expectedSize, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED
				| Spliterator.SORTED | Spliterator.DISTINCT | Spliterator.IMMUTABLE
				| Spliterator.NONNULL, m.characteristics());
		org.junit.Assert.assertNull(m.getComparator());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		IntStream tStream = java.util.stream.IntStream.range(from, to);
		if (useSplits.booleanValue()) tStream = tStream.parallel();
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testFromToSpliteratorSkip() {
		final int from = -5;
		final int to = 7;
		final int expectedSize = to - from;
		final int[] expectedAfterSkipping = { -3, -2, -1, 0, 1, 2, 3, 4, 5, 6 };
		final IntSpliterator m = IntSpliterators.fromTo(from, to);
		assertEquals(expectedSize, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expectedSize - 2, m.getExactSizeIfKnown());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		assertArrayEquals(expectedAfterSkipping, mArray);
	}

	@Test
	public void testConcatSpliterator() {
		final int[] a = { 2, 4, 6, 8, 10, 12, 14 };
		final int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		final int[] c = { 1, 2, 4, 8, 16 };
		final int[] expectedConcat = { 2, 4, 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		final IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		final IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		final IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		final IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		assertEquals(expectedConcat.length, concatSpliterator.estimateSize());
		assertEquals(expectedConcat.length, concatSpliterator.getExactSizeIfKnown());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.NONNULL, concatSpliterator.characteristics());
		final IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits.booleanValue());
		final int[] actualConcat = concatStream.toArray();
		assertArrayEquals(expectedConcat, actualConcat);
	}


	@Test
	public void testConcatSpliteratorSkipWithinOne() {
		final int[] a = { 2, 4, 6, 8, 10, 12, 14 };
		final int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		final int[] c = { 1, 2, 4, 8, 16 };
		final int[] expectedAfterSkipping = { 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		final IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		final IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		final IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		final IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		assertEquals(expectedAfterSkipping.length + 2, concatSpliterator.getExactSizeIfKnown());
		assertEquals(2, concatSpliterator.skip(2));
		assertEquals(expectedAfterSkipping.length, concatSpliterator.getExactSizeIfKnown());
		final IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits.booleanValue());
		final int[] actualConcat = concatStream.toArray();
		assertArrayEquals(expectedAfterSkipping, actualConcat);
	}


	@Test
	public void testConcatSpliteratorSkipSpanMultiple() {
		final int[] a = { 2, 4, 6, 8, 10, 12, 14 };
		final int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		final int[] c = { 1, 2, 4, 8, 16 };
		final int[] expectedAfterSkipping = { 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		final IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		final IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		final IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		final IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		assertEquals(expectedAfterSkipping.length + 9, concatSpliterator.getExactSizeIfKnown());
		assertEquals(9, concatSpliterator.skip(9));
		assertEquals(expectedAfterSkipping.length, concatSpliterator.getExactSizeIfKnown());
		final IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits.booleanValue());
		final int[] actualConcat = concatStream.toArray();
		assertArrayEquals(expectedAfterSkipping, actualConcat);
	}

	@SuppressWarnings("static-method")
	@Test
	public void testConcatSpliteratorNotSortedOrDistinctPreserved() {
		final IntSortedSet s1 = new IntRBTreeSet(new int[] {1, 2, 3});
		final IntSortedSet s2 = new IntRBTreeSet(new int[] {1, 3, 4});
		final IntSpliterator split1 = s1.spliterator();
		final IntSpliterator split2 = s2.spliterator();
		assertTrue(split1.hasCharacteristics(Spliterator.SORTED));
		assertTrue(split2.hasCharacteristics(Spliterator.SORTED));
		assertTrue(split1.hasCharacteristics(Spliterator.DISTINCT));
		assertTrue(split2.hasCharacteristics(Spliterator.DISTINCT));
		assertTrue(split1.hasCharacteristics(Spliterator.SIZED));
		assertTrue(split2.hasCharacteristics(Spliterator.SIZED));
		final IntSpliterator concat = IntSpliterators.concat(split1, split2);
		assertFalse(concat.hasCharacteristics(Spliterator.SORTED));
		assertFalse(concat.hasCharacteristics(Spliterator.DISTINCT));
		// SIZED is preserved though.
		assertTrue(concat.hasCharacteristics(Spliterator.SIZED));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testConcatSpliteratorSortedAndDistinctPreservedIfOnlyOne() {
		final IntSortedSet s1 = new IntRBTreeSet(new int[] {1, 2, 3});
		final IntSpliterator split1 = s1.spliterator();
		final IntSpliterator concat = IntSpliterators.concat(split1);
		assertTrue(concat.hasCharacteristics(Spliterator.SORTED));
		assertTrue(concat.hasCharacteristics(Spliterator.DISTINCT));
		assertTrue(concat.hasCharacteristics(Spliterator.SIZED));
		// Null for natural ordering, as opposed to IllegalStateException thrown for not being sorted.
		assertNull(concat.getComparator());
	}

	@Test
	public void testAsSpliterator() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliterator(IntIterators.wrap(a), a.length, 0);
		final java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED, Spliterator.NONNULL, m.characteristics());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final IntStream tStream = StreamSupport.intStream(t, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSpliteratorSkip() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final int[] expected = { 4, 10, 5, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliterator(IntIterators.wrap(a), a.length, 0);
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expected.length, m.getExactSizeIfKnown());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testAsSpliteratorUnknownSize() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliteratorUnknownSize(IntIterators.wrap(a), 0);
		final java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		assertEquals(Spliterator.NONNULL, m.characteristics());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final IntStream tStream = StreamSupport.intStream(t, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSpliteratorUnknownSizeSkip() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final int[] expected = { 4, 10, 5, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliteratorUnknownSize(IntIterators.wrap(a), 0);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testAsSortedSpliterator() {
		final int[] a = { 2, 3, 4, 10, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliteratorFromSorted(IntIterators.wrap(a), a.length, 0, IntComparators.NATURAL_COMPARATOR);
		final java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, java.util.Spliterator.ORDERED | java.util.Spliterator.SORTED);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		org.junit.Assert.assertSame(IntComparators.NATURAL_COMPARATOR, m.getComparator());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.SORTED | Spliterator.NONNULL, m.characteristics());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final IntStream tStream = StreamSupport.intStream(t, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSortedSpliteratorSkip() {
		final int[] a = { 2, 3, 4, 10, 42, 10239 };
		final int[] expected = { 4, 10, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliteratorFromSorted(IntIterators.wrap(a), a.length, 0, IntComparators.NATURAL_COMPARATOR);
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expected.length, m.getExactSizeIfKnown());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testAsSortedUnknownSizeSpliterator() {
		final int[] a = { 2, 3, 4, 10, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliteratorFromSortedUnknownSize(IntIterators.wrap(a), 0, IntComparators.NATURAL_COMPARATOR);
		final java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, java.util.Spliterator.ORDERED | java.util.Spliterator.SORTED);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		org.junit.Assert.assertSame(IntComparators.NATURAL_COMPARATOR, m.getComparator());
		assertEquals(Spliterator.ORDERED | Spliterator.SORTED | Spliterator.NONNULL, m.characteristics());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final IntStream tStream = StreamSupport.intStream(t, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		final int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSortedUnknownSizeSpliteratorSkip() {
		final int[] a = { 2, 3, 4, 10, 42, 10239 };
		final int[] expected = { 4, 10, 42, 10239 };
		final IntSpliterator m = IntSpliterators.asSpliteratorFromSortedUnknownSize(IntIterators.wrap(a), 0, IntComparators.NATURAL_COMPARATOR);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		final IntStream mStream = StreamSupport.intStream(m, useSplits.booleanValue());
		final int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAsIterator() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final IntIterator m = IntSpliterators.asIterator(IntSpliterators.wrap(a));
		final java.util.PrimitiveIterator.OfInt t = java.util.Spliterators.iterator(java.util.Spliterators.spliterator(a, 0));
		while (m.hasNext()) {
			if (!t.hasNext()) org.junit.Assert.fail(" m hasNext() but t does not");
			assertEquals(m.nextInt(), t.nextInt());
		}
		if(t.hasNext()) org.junit.Assert.fail("t hasNext() but m does not");
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAsIteratorSkip() {
		final int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		final int[] expected = { 4, 10, 5, 42, 10239 };
		final IntIterator m = IntSpliterators.asIterator(IntSpliterators.wrap(a));
		assertEquals(2, m.skip(2));
		final int[] mArray = IntIterators.unwrap(m);
		assertArrayEquals(expected, mArray);
	}

	@SuppressWarnings("static-method")
	@Test
	public void testSingletonSpliterator() {
		final int data = 42;
		final IntSpliterator singleton = IntSpliterators.singleton(data);
		assertEquals(1, singleton.getExactSizeIfKnown());
		assertEquals(
				Spliterator.NONNULL
				| Spliterator.IMMUTABLE
				| Spliterator.SIZED
				| Spliterator.SUBSIZED
				| Spliterator.DISTINCT
				| Spliterator.ORDERED
				| Spliterator.SORTED,
				singleton.characteristics());
		org.junit.Assert.assertNull(singleton.trySplit());
		singleton.forEachRemaining((final int actual) -> assertEquals(data, actual));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testSingletonSpliteratorSkip() {
		final int data = 42;
		final IntSpliterator singleton = IntSpliterators.singleton(data);
		assertEquals(1, singleton.skip(5));
		assertEquals(0, singleton.getExactSizeIfKnown());
		singleton.forEachRemaining(unused -> org.junit.Assert.fail("Expected no elements left"));
	}
}

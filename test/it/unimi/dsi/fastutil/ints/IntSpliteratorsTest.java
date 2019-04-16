package it.unimi.dsi.fastutil.ints;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class IntSpliteratorsTest {
	
	@Parameters(name = "useSplits = {0}")
	public static Object[] data() { return new Boolean[] { false, true }; }

	// Using Boolean object so we can set to null to get NullPointerException if the parameterization doesn't trigger.
	@Parameter
	public Boolean useSplits = null;
	
	@Test
	public void testArraySpliterator() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		IntSpliterator m = IntSpliterators.wrap(a);
		java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.NONNULL, m.characteristics());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = StreamSupport.intStream(t, useSplits);
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testArraySpliteratorSkip() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		int[] expected = { 4, 10, 5, 42, 10239 };
		IntSpliterator m = IntSpliterators.wrap(a);
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expected.length, m.getExactSizeIfKnown());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testFromToSpliterator() {
		final int from = -5;
		final int to = 17;
		final int expectedSize = to - from;
		IntSpliterator m = IntSpliterators.fromTo(from, to);
		Spliterator.OfInt t = java.util.stream.IntStream.range(from, to).spliterator();
		assertEquals(expectedSize, m.estimateSize());
		assertEquals(expectedSize, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED
				| Spliterator.SORTED | Spliterator.DISTINCT | Spliterator.IMMUTABLE
				| Spliterator.NONNULL, m.characteristics());
		org.junit.Assert.assertNull(m.getComparator());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = java.util.stream.IntStream.range(from, to);
		if (useSplits) tStream = tStream.parallel();
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testFromToSpliteratorSkip() {
		final int from = -5;
		final int to = 7;
		final int expectedSize = to - from;
		int[] expectedAfterSkipping = { -3, -2, -1, 0, 1, 2, 3, 4, 5, 6 };
		IntSpliterator m = IntSpliterators.fromTo(from, to);
		assertEquals(expectedSize, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expectedSize - 2, m.getExactSizeIfKnown());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		assertArrayEquals(expectedAfterSkipping, mArray);
	}

	@Test
	public void testConcatSpliterator() {
		int[] a = { 2, 4, 6, 8, 10, 12, 14 };
		int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		int[] c = { 1, 2, 4, 8, 16 };
		int[] expectedConcat = { 2, 4, 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		assertEquals(expectedConcat.length, concatSpliterator.estimateSize());
		assertEquals(expectedConcat.length, concatSpliterator.getExactSizeIfKnown());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.NONNULL, concatSpliterator.characteristics());
		IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits);
		int[] actualConcat = concatStream.toArray();
		assertArrayEquals(expectedConcat, actualConcat);
	}


	@Test
	public void testConcatSpliteratorSkipWithinOne() {
		int[] a = { 2, 4, 6, 8, 10, 12, 14 };
		int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		int[] c = { 1, 2, 4, 8, 16 };
		int[] expectedAfterSkipping = { 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		assertEquals(expectedAfterSkipping.length + 2, concatSpliterator.getExactSizeIfKnown());
		assertEquals(2, concatSpliterator.skip(2));
		assertEquals(expectedAfterSkipping.length, concatSpliterator.getExactSizeIfKnown());
		IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits);
		int[] actualConcat = concatStream.toArray();
		assertArrayEquals(expectedAfterSkipping, actualConcat);
	}


	@Test
	public void testConcatSpliteratorSkipSpanMultiple() {
		int[] a = { 2, 4, 6, 8, 10, 12, 14 };
		int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		int[] c = { 1, 2, 4, 8, 16 };
		int[] expectedAfterSkipping = { 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		assertEquals(expectedAfterSkipping.length + 9, concatSpliterator.getExactSizeIfKnown());
		assertEquals(9, concatSpliterator.skip(9));
		assertEquals(expectedAfterSkipping.length, concatSpliterator.getExactSizeIfKnown());
		IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits);
		int[] actualConcat = concatStream.toArray();
		assertArrayEquals(expectedAfterSkipping, actualConcat);
	}
	
	@Test
	public void testAsSpliterator() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliterator(IntIterators.wrap(a), a.length, 0);
		java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED, Spliterator.NONNULL, m.characteristics());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = StreamSupport.intStream(t, useSplits);
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSpliteratorSkip() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		int[] expected = { 4, 10, 5, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliterator(IntIterators.wrap(a), a.length, 0);
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expected.length, m.getExactSizeIfKnown());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testAsSpliteratorUnknownSize() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliteratorUnknownSize(IntIterators.wrap(a), 0);
		java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		assertEquals(Spliterator.NONNULL, m.characteristics());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = StreamSupport.intStream(t, useSplits);
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSpliteratorUnknownSizeSkip() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		int[] expected = { 4, 10, 5, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliteratorUnknownSize(IntIterators.wrap(a), 0);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testAsSortedSpliterator() {
		int[] a = { 2, 3, 4, 10, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliteratorFromSorted(IntIterators.wrap(a), a.length, 0, IntComparators.NATURAL_COMPARATOR);
		java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, java.util.Spliterator.ORDERED | java.util.Spliterator.SORTED);
		assertEquals(a.length, m.estimateSize());
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(m.estimateSize(), t.estimateSize());
		org.junit.Assert.assertSame(IntComparators.NATURAL_COMPARATOR, m.getComparator());
		assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.SORTED | Spliterator.NONNULL, m.characteristics());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = StreamSupport.intStream(t, useSplits);
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSortedSpliteratorSkip() {
		int[] a = { 2, 3, 4, 10, 42, 10239 };
		int[] expected = { 4, 10, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliteratorFromSorted(IntIterators.wrap(a), a.length, 0, IntComparators.NATURAL_COMPARATOR);
		assertEquals(a.length, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(expected.length, m.getExactSizeIfKnown());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@Test
	public void testAsSortedUnknownSizeSpliterator() {
		int[] a = { 2, 3, 4, 10, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliteratorFromSortedUnknownSize(IntIterators.wrap(a), 0, IntComparators.NATURAL_COMPARATOR);
		java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, java.util.Spliterator.ORDERED | java.util.Spliterator.SORTED);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		org.junit.Assert.assertSame(IntComparators.NATURAL_COMPARATOR, m.getComparator());
		assertEquals(Spliterator.ORDERED | Spliterator.SORTED | Spliterator.NONNULL, m.characteristics());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = StreamSupport.intStream(t, useSplits);
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testAsSortedUnknownSizeSpliteratorSkip() {
		int[] a = { 2, 3, 4, 10, 42, 10239 };
		int[] expected = { 4, 10, 42, 10239 };
		IntSpliterator m = IntSpliterators.asSpliteratorFromSortedUnknownSize(IntIterators.wrap(a), 0, IntComparators.NATURAL_COMPARATOR);
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		assertEquals(2, m.skip(2));
		assertEquals(Long.MAX_VALUE, m.estimateSize());
		assertEquals(-1, m.getExactSizeIfKnown());
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		assertArrayEquals(expected, mArray);
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAsIterator() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		IntIterator m = IntSpliterators.asIterator(IntSpliterators.wrap(a));
		java.util.PrimitiveIterator.OfInt t = java.util.Spliterators.iterator(java.util.Spliterators.spliterator(a, 0));
		while (m.hasNext()) {
			if (!t.hasNext()) org.junit.Assert.fail(" m hasNext() but t does not");
			assertEquals(m.nextInt(), t.nextInt());
		}
		if(t.hasNext()) org.junit.Assert.fail("t hasNext() but m does not");
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAsIteratorSkip() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239 };
		int[] expected = { 4, 10, 5, 42, 10239 };
		IntIterator m = IntSpliterators.asIterator(IntSpliterators.wrap(a));
		assertEquals(2, m.skip(2));
		int[] mArray = IntIterators.unwrap(m);
		assertArrayEquals(expected, mArray);
	}
}

package it.unimi.dsi.fastutil.ints;

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
	public static Object[] data() { return new Boolean[] { false, true }; }

	// Using Boolean object so we can set to null to get NullPointerException if the parameterization doesn't trigger.
	@Parameter
	public Boolean useSplits = null;
	
	@Test
	public void testArraySpliterator() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239};
		IntSpliterator m = IntSpliterators.wrap(a);
		java.util.Spliterator.OfInt t = java.util.Spliterators.spliterator(a, 0);
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		IntStream tStream = StreamSupport.intStream(t, useSplits);
		int[] mArray = mStream.toArray();
		int[] tArray = tStream.toArray();
		org.junit.Assert.assertArrayEquals(mArray, tArray);
	}

	@Test
	public void testArraySpliteratorSkip() {
		int[] a = { 2, 3, 4, 10, 5, 42, 10239};
		int[] expected = { 4, 10, 5, 42, 10239};
		IntSpliterator m = IntSpliterators.wrap(a);
		org.junit.Assert.assertEquals(2, m.skip(2));
		IntStream mStream = StreamSupport.intStream(m, useSplits);
		int[] mArray = mStream.toArray();
		org.junit.Assert.assertArrayEquals(expected, mArray);
	}

	@Test
	public void testConcatSpliterator() {
		int[] a = { 2, 4, 6, 8, 10, 12, 14};
		int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		int[] c = { 1, 2, 4, 8, 16 };
		int[] expectedConcat = { 2, 4, 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits);
		int[] actualConcat = concatStream.toArray();
		org.junit.Assert.assertArrayEquals(expectedConcat, actualConcat);
	}


	@Test
	public void testConcatSpliteratorSkipWithinOne() {
		int[] a = { 2, 4, 6, 8, 10, 12, 14};
		int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		int[] c = { 1, 2, 4, 8, 16 };
		IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		org.junit.Assert.assertEquals(2, concatSpliterator.skip(2));
		int[] expectedConcat = { 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits);
		int[] actualConcat = concatStream.toArray();
		org.junit.Assert.assertArrayEquals(expectedConcat, actualConcat);
	}


	@Test
	public void testConcatSpliteratorSkipSpanMultiple() {
		int[] a = { 2, 4, 6, 8, 10, 12, 14};
		int[] b = { 1, 3, 5, 7, 9, 11, 13 };
		int[] c = { 1, 2, 4, 8, 16 };
		IntSpliterator aSpliterator = IntSpliterators.wrap(a);
		IntSpliterator bSpliterator = IntSpliterators.wrap(b);
		IntSpliterator cSpliterator = IntSpliterators.wrap(c);
		IntSpliterator concatSpliterator = IntSpliterators.concat(aSpliterator, bSpliterator, cSpliterator);
		org.junit.Assert.assertEquals(9, concatSpliterator.skip(9));
		int[] expectedConcat = { 5, 7, 9, 11, 13, 1, 2, 4, 8, 16 };
		IntStream concatStream = StreamSupport.intStream(concatSpliterator, useSplits);
		int[] actualConcat = concatStream.toArray();
		org.junit.Assert.assertArrayEquals(expectedConcat, actualConcat);
	}
}

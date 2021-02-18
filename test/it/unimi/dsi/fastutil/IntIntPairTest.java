package it.unimi.dsi.fastutil;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import it.unimi.dsi.fastutil.ints.IntIntPair;

public class IntIntPairTest {

	@SuppressWarnings("boxing")
	@Test
	public void test() {
		final Comparator<IntIntPair> comparator = IntIntPair.lexComparator();
		assertEquals(0, comparator.compare(IntIntPair.of(0, 1), IntIntPair.of(0, 1)));
		assertEquals(-1, comparator.compare(IntIntPair.of(0, 1), IntIntPair.of(0, 2)));
		assertEquals(1, comparator.compare(IntIntPair.of(0, 2), IntIntPair.of(0, 1)));
		assertEquals(1, comparator.compare(IntIntPair.of(1, 1), IntIntPair.of(0, 1)));
		assertEquals(-1, comparator.compare(IntIntPair.of(0, 1), IntIntPair.of(1, 1)));
	}

}

package it.unimi.dsi.fastutil;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

public class PairTest {

	@SuppressWarnings("boxing")
	@Test
	public void test() {
		final Comparator<Pair<Integer, Integer>> comparator = Pair.lexComparator();
		assertEquals(0, comparator.compare(Pair.of(0, 1), Pair.of(0, 1)));
		assertEquals(-1, comparator.compare(Pair.of(0, 1), Pair.of(0, 2)));
		assertEquals(1, comparator.compare(Pair.of(0, 2), Pair.of(0, 1)));
		assertEquals(1, comparator.compare(Pair.of(1, 1), Pair.of(0, 1)));
		assertEquals(-1, comparator.compare(Pair.of(0, 1), Pair.of(1, 1)));
	}

}

package it.unimi.dsi.fastutil.ints;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class IntArraysTest {
	
	@Test
	public void testMergeSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone();
		IntArrays.mergeSort( a );
		Arrays.sort( b );
		Assert.assertArrayEquals( b, a );
	}

}

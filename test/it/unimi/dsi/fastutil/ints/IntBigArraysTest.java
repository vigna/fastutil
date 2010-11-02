package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

public class IntBigArraysTest {

	@Test
	public void testQuickSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		
		Arrays.sort( s );
		int[][] sorted = IntBigArrays.wrap( s.clone() );

		final int[][] a = IntBigArrays.wrap( s.clone()  );
		IntBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );

		IntBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );
	}
	
	
}

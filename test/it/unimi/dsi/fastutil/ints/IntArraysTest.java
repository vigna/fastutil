package it.unimi.dsi.fastutil.ints;

import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Test;

public class IntArraysTest {
	
	@Test
	public void testMergeSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort( sorted );
		IntArrays.mergeSort( b );
		assertArrayEquals( sorted, b );
		IntArrays.mergeSort( b );
		assertArrayEquals( sorted, b );
		
		final int[] d = a.clone();
		IntArrays.mergeSort( d, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return k1 - k2;
			}
		});
		assertArrayEquals( sorted, d );

		IntArrays.mergeSort( d, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return k1 - k2;
			}
		});
		assertArrayEquals( sorted, d );
	}

	@Test
	public void testQuickSort() {
		int[] a = { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 }, b = a.clone(), sorted = a.clone();
		Arrays.sort( sorted );
		IntArrays.quickSort( b );
		assertArrayEquals( sorted, b );
		IntArrays.quickSort( b );
		assertArrayEquals( sorted, b );

		final int[] d = a.clone();
		IntArrays.quickSort( d, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return k1 - k2;
			}
		});
		assertArrayEquals( sorted, d );
		IntArrays.quickSort( d, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return k1 - k2;
			}
		});
		assertArrayEquals( sorted, d );
	}
}

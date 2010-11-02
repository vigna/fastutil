package it.unimi.dsi.fastutil;

import static org.junit.Assert.assertArrayEquals;
import it.unimi.dsi.fastutil.ints.AbstractIntComparator;

import org.junit.Test;

public class ArraysTest {
	
	@Test
	public void testMergeSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final int[] a = s.clone();
		
		java.util.Arrays.sort( s );
		int[] sorted = s.clone();
		Arrays.mergeSort( 0, a.length, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return a[ k1 ] - a[ k2 ]; 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = a[ k1 ];
				a[ k1 ] = a[ k2 ];
				a[ k2 ] = t;
			}
		});
		assertArrayEquals( sorted, a );

		Arrays.mergeSort( 0, a.length, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return a[ k1 ] - a[ k2 ]; 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = a[ k1 ];
				a[ k1 ] = a[ k2 ];
				a[ k2 ] = t;
			}
		});
		assertArrayEquals( sorted, a );

	}

	@Test
	public void testQuickSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		
		java.util.Arrays.sort( s );
		int[] sorted = s.clone();

		final int[] a = s.clone();
		Arrays.quickSort( 0, a.length, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return a[ k1 ] - a[ k2 ]; 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = a[ k1 ];
				a[ k1 ] = a[ k2 ];
				a[ k2 ] = t;
			}
		});
		assertArrayEquals( sorted, a );

		Arrays.quickSort( 0, a.length, new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return a[ k1 ] - a[ k2 ]; 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = a[ k1 ];
				a[ k1 ] = a[ k2 ];
				a[ k2 ] = t;
			}
		});
		assertArrayEquals( sorted, a );
	}
}

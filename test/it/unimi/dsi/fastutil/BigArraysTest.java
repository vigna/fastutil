package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.BigSwapper;
import it.unimi.dsi.fastutil.ints.IntBigArrays;
import it.unimi.dsi.fastutil.longs.AbstractLongComparator;

import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Test;

public class BigArraysTest {
	
	@Test
	public void testMergeSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final int[][] a = IntBigArrays.wrap( s.clone()  );
		
		Arrays.sort( s );
		int[][] sorted = IntBigArrays.wrap( s.clone() );
		BigArrays.mergeSort( 0, IntBigArrays.length( a ), new AbstractLongComparator() {
			@Override
			public int compare( long k1, long k2 ) {
				return IntBigArrays.get( a, k1 ) - IntBigArrays.get( a, k2 ); 
			}
		}, new BigSwapper() {
			@Override
			public void swap( long k1, long k2 ) {
				IntBigArrays.swap( a, k1, k2 );
			}
		});
		assertArrayEquals( sorted, a );

		BigArrays.mergeSort( 0, IntBigArrays.length( a ), new AbstractLongComparator() {
			@Override
			public int compare( long k1, long k2 ) {
				return IntBigArrays.get( a, k1 ) - IntBigArrays.get( a, k2 ); 
			}
		}, new BigSwapper() {
			@Override
			public void swap( long k1, long k2 ) {
				IntBigArrays.swap( a, k1, k2 );
			}
		});
		assertArrayEquals( sorted, a );

	}

	@Test
	public void testQuickSort() {
		int[] s = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		
		Arrays.sort( s );
		int[][] sorted = IntBigArrays.wrap( s.clone() );

		final int[][] a = IntBigArrays.wrap( s.clone()  );
		BigArrays.quickSort( 0, IntBigArrays.length( a ), new AbstractLongComparator() {
			@Override
			public int compare( long k1, long k2 ) {
				return IntBigArrays.get( a, k1 ) - IntBigArrays.get( a, k2 ); 
			}
		}, new BigSwapper() {
			@Override
			public void swap( long k1, long k2 ) {
				IntBigArrays.swap( a, k1, k2 );
			}
		});
		assertArrayEquals( sorted, a );

		BigArrays.quickSort( 0, IntBigArrays.length( a ), new AbstractLongComparator() {
			@Override
			public int compare( long k1, long k2 ) {
				return IntBigArrays.get( a, k1 ) - IntBigArrays.get( a, k2 ); 
			}
		}, new BigSwapper() {
			@Override
			public void swap( long k1, long k2 ) {
				IntBigArrays.swap( a, k1, k2 );
			}
		});
		assertArrayEquals( sorted, a );
	}
}

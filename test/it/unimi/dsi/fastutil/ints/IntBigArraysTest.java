package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.*;
import static it.unimi.dsi.fastutil.ints.IntBigArrays.set;
import static it.unimi.dsi.fastutil.ints.IntBigArrays.get;

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

	private void testCopy( int n ) {
		int[][] a = IntBigArrays.newBigArray( n );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		IntBigArrays.copy( a, 0, a, 1, n - 2 );
		assertEquals( 0, a[ 0 ][ 0 ] );
		for ( int i = 0; i < n - 2; i++ ) assertEquals( i,  get( a, i + 1 ) );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		IntBigArrays.copy( a, 1, a, 0, n - 1 );
		for ( int i = 0; i < n - 1; i++ ) assertEquals( i + 1, get( a, i ) );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		int[] b = new int[ n ];
		for ( int i = 0; i < n; i++ ) b[ i ] = i;
		assertArrayEquals( a, IntBigArrays.wrap( b ) );
	}
	
	@Test
	public void testCopy10() {
		testCopy( 10 );
	}

	@Test
	public void testCopy1000() {
		testCopy( 1000 );
	}

	@Test
	public void testCopy1000000() {
		testCopy( 1000000 );
	}
}

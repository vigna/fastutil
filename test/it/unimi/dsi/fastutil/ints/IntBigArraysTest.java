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

		int[][] a = IntBigArrays.wrap( s.clone()  );

		IntBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );

		IntBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );
		
		a = IntBigArrays.wrap( s.clone()  );
		
		IntBigArrays.quickSort( a, IntComparators.NATURAL_COMPARATOR );
		assertArrayEquals( sorted, a );

		IntBigArrays.quickSort( a, IntComparators.NATURAL_COMPARATOR );
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

	@Test
	public void testBinarySearch() {
		int[] a = new int[] { 25, 32, 1, 3, 2, 0, 40, 7, 13, 12, 11, 10, -1, -6, -18, 2000 };
		
		Arrays.sort( a );
		int[][] b = IntBigArrays.wrap( a.clone() );

		for( int i = -1; i < 20; i++ ) {
			assertEquals( "" + i, Arrays.binarySearch( a, i ), IntBigArrays.binarySearch( b, i ) );
			assertEquals( "" + i, Arrays.binarySearch( a, i ), IntBigArrays.binarySearch( b, i, IntComparators.NATURAL_COMPARATOR ) );
		}
	
		for( int i = -1; i < 20; i++ ) {
			assertEquals( Arrays.binarySearch( a, 5, 13, i ), IntBigArrays.binarySearch( b, 5, 13, i ) );
			assertEquals( Arrays.binarySearch( a, 5, 13, i ), IntBigArrays.binarySearch( b, 5, 13, i, IntComparators.NATURAL_COMPARATOR ) );
		}
	}

	@Test
	public void testTrim() {
		int[] a = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		int[][] b = IntBigArrays.wrap( a.clone() );

		for( int i = a.length; i-- != 0; ) {
			int[][] t = IntBigArrays.trim( b, i );
			final long l = IntBigArrays.length( t );
			assertEquals( i, l );
			for( int p = 0; p < l; p++ ) assertEquals( a[ p ], IntBigArrays.get( t, p ) );
			
		}
	}

	@Test
	public void testEquals() {
		int[] a = new int[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		int[][] b = IntBigArrays.wrap( a.clone() );
		int[][] c = IntBigArrays.wrap( a.clone() );

		assertTrue( IntBigArrays.equals( b, c ) );
		b[ 0 ][ 0 ] = 0;
		assertFalse( IntBigArrays.equals( b, c ) );
	}
}
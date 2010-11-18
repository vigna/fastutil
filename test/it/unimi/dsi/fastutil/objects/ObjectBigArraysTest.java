package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.*;
import static it.unimi.dsi.fastutil.objects.ObjectBigArrays.set;
import static it.unimi.dsi.fastutil.objects.ObjectBigArrays.get;

import java.util.Arrays;

import org.junit.Test;

public class ObjectBigArraysTest {

	@SuppressWarnings({ "unchecked", "boxing" })
	@Test
	public void testQuickSort() {
		Integer[] s = new Integer[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };

		Arrays.sort( s );
		Integer[][] sorted = ObjectBigArrays.wrap( s.clone() );

		Integer[][] a = ObjectBigArrays.wrap( s.clone()  );

		ObjectBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );

		ObjectBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );
		
		a = ObjectBigArrays.wrap( s.clone()  );
		
		ObjectBigArrays.quickSort( a, ObjectComparators.NATURAL_COMPARATOR );
		assertArrayEquals( sorted, a );

		ObjectBigArrays.quickSort( a, ObjectComparators.NATURAL_COMPARATOR );
		assertArrayEquals( sorted, a );
		
	}

	@SuppressWarnings("boxing")
	private void testCopy( int n ) {
		Object[][] a = ObjectBigArrays.newBigArray( n );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		ObjectBigArrays.copy( a, 0, a, 1, n - 2 );
		assertEquals( 0, a[ 0 ][ 0 ] );
		for ( int i = 0; i < n - 2; i++ ) assertEquals( i,  get( a, i + 1 ) );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		ObjectBigArrays.copy( a, 1, a, 0, n - 1 );
		for ( int i = 0; i < n - 1; i++ ) assertEquals( i + 1, get( a, i ) );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		Integer[] b = new Integer[ n ];
		for ( int i = 0; i < n; i++ ) b[ i ] = i;
		assertArrayEquals( a, ObjectBigArrays.wrap( b ) );
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

	@SuppressWarnings({ "boxing", "unchecked" })
	@Test
	public void testBinarySearch() {
		Integer[] a = new Integer[] { 25, 32, 1, 3, 2, 0, 40, 7, 13, 12, 11, 10, -1, -6, -18, 2000 };
		
		Arrays.sort( a );
		Integer[][] b = ObjectBigArrays.wrap( a.clone() );

		for( int i = -1; i < 20; i++ ) { 
			assertEquals( "" + i, Arrays.binarySearch( a, i ), ObjectBigArrays.binarySearch( b, i ) );
			assertEquals( "" + i, Arrays.binarySearch( a, i ), ObjectBigArrays.binarySearch( b, i, ObjectComparators.NATURAL_COMPARATOR ) );
		}
	
		for( int i = -1; i < 20; i++ ) {
			assertEquals( Arrays.binarySearch( a, 5, 13, i ), ObjectBigArrays.binarySearch( b, 5, 13, i ) );
			assertEquals( Arrays.binarySearch( a, 5, 13, i ), ObjectBigArrays.binarySearch( b, 5, 13, i, ObjectComparators.NATURAL_COMPARATOR ) );
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public void testTrim() {
		Integer[] a = new Integer[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		Integer[][] b = ObjectBigArrays.wrap( a.clone() );

		for( int i = a.length; i-- != 0; ) {
			Integer[][] t = ObjectBigArrays.trim( b, i );
			final long l = ObjectBigArrays.length( t );
			assertEquals( i, l );
			for( int p = 0; p < l; p++ ) assertEquals( a[ p ], ObjectBigArrays.get( t, p ) );
			
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public void testEquals() {
		Integer[] a = new Integer[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		Integer[][] b = ObjectBigArrays.wrap( a.clone() );
		Integer[][] c = ObjectBigArrays.wrap( a.clone() );

		assertTrue( ObjectBigArrays.equals( b, c ) );
		b[ 0 ][ 0 ] = 0;
		assertFalse( ObjectBigArrays.equals( b, c ) );
	}
}
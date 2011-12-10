package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.*;
import static it.unimi.dsi.fastutil.ints.IntBigArrays.set;
import static it.unimi.dsi.fastutil.ints.IntBigArrays.get;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class IntBigArraysTest {

	
	public static int[][] identity( final int n ) {
		final int[][] perm = IntBigArrays.newBigArray( n );
		for( int i = n; i-- != 0; ) IntBigArrays.set( perm, i , i );
		return perm;
	}

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

	@Test
	public void testRadixSort1() {
		int[][] t = IntBigArrays.wrap( new int[] { 2, 1, 0, 4 } );
		IntBigArrays.radixSort( t );
		for( long i = IntBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );
		
		t = IntBigArrays.wrap( new int[] { 2, -1, 0, -4 } );
		IntBigArrays.radixSort( t );
		for( long i = IntBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );
		
		t = IntBigArrays.shuffle( identity( 100 ), new Random( 0 ) );
		IntBigArrays.radixSort( t );
		for( long i = IntBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );

		t = IntBigArrays.newBigArray( 100 );
		Random random = new Random( 0 );
		for( long i = IntBigArrays.length( t ); i-- != 0; ) IntBigArrays.set( t, i, random.nextInt() );
		IntBigArrays.radixSort( t );
		for( long i = IntBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );

		t = IntBigArrays.newBigArray( 100000 );
		random = new Random( 0 );
		for( long i = IntBigArrays.length( t ); i-- != 0; ) IntBigArrays.set( t, i, random.nextInt() );
		IntBigArrays.radixSort( t );
		for( long i = IntBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );
		for( long i = 100; i-- != 10; ) IntBigArrays.set( t, i, random.nextInt() );
		IntBigArrays.radixSort( t, 10, 100 );
		for( long i = 99; i-- != 10; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );

		t = IntBigArrays.newBigArray( 1000000 );
		random = new Random( 0 );
		for( long i = IntBigArrays.length( t ); i-- != 0; ) IntBigArrays.set( t, i, random.nextInt() );
		IntBigArrays.radixSort( t );
		for( long i = IntBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( IntBigArrays.get( t, i ) <= IntBigArrays.get( t, i + 1 ) );
	}

	@Test
	public void testRadixSort2() {
		int d[][], e[][];
		d = IntBigArrays.newBigArray( 10 );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( d, i, (int)( 3 - i % 3 ) );
		e = IntBigArrays.shuffle( identity( 10 ), new Random( 0 ) );
		IntBigArrays.radixSort( d, e );
		for( long i = IntBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + IntBigArrays.get( d, i ) + ", " + IntBigArrays.get( e, i ) + ">, <" + IntBigArrays.get( d, i + 1 ) + ", " +  IntBigArrays.get( e, i + 1 ) + ">", IntBigArrays.get( d, i ) < IntBigArrays.get( d, i + 1 ) || IntBigArrays.get( d, i ) == IntBigArrays.get( d, i + 1 ) && IntBigArrays.get( e, i ) <= IntBigArrays.get( e, i + 1 ) );
		
		d = IntBigArrays.newBigArray( 100000 );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( d, i, (int)( 100 - i % 100 ) );
		e = IntBigArrays.shuffle( identity( 100000 ), new Random( 6 ) );
		IntBigArrays.radixSort( d, e );
		for( long i = IntBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + IntBigArrays.get( d, i ) + ", " + IntBigArrays.get( e, i ) + ">, <" + IntBigArrays.get( d, i + 1 ) + ", " +  IntBigArrays.get( e, i + 1 ) + ">", IntBigArrays.get( d, i ) < IntBigArrays.get( d, i + 1 ) || IntBigArrays.get( d, i ) == IntBigArrays.get( d, i + 1 ) && IntBigArrays.get( e, i ) <= IntBigArrays.get( e, i + 1 ) );

		d = IntBigArrays.newBigArray( 10 );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( d, i, (int)( i % 3 - 2 ) );
		Random random = new Random( 0 );
		e = IntBigArrays.newBigArray( IntBigArrays.length(  d ) );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( e, i, random.nextInt() );
		IntBigArrays.radixSort( d, e );
		for( long i = IntBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + IntBigArrays.get( d, i ) + ", " + IntBigArrays.get( e, i ) + ">, <" + IntBigArrays.get( d, i + 1 ) + ", " +  IntBigArrays.get( e, i + 1 ) + ">", IntBigArrays.get( d, i ) < IntBigArrays.get( d, i + 1 ) || IntBigArrays.get( d, i ) == IntBigArrays.get( d, i + 1 ) && IntBigArrays.get( e, i ) <= IntBigArrays.get( e, i + 1 ) );
		
		d = IntBigArrays.newBigArray( 100000 );
		random = new Random( 0 );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( d, i, random.nextInt() );
		e = IntBigArrays.newBigArray( IntBigArrays.length(  d ) );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( e, i, random.nextInt() );
		IntBigArrays.radixSort( d, e );
		for( long i = IntBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + IntBigArrays.get( d, i ) + ", " + IntBigArrays.get( e, i ) + ">, <" + IntBigArrays.get( d, i + 1 ) + ", " +  IntBigArrays.get( e, i + 1 ) + ">", IntBigArrays.get( d, i ) < IntBigArrays.get( d, i + 1 ) || IntBigArrays.get( d, i ) == IntBigArrays.get( d, i + 1 ) && IntBigArrays.get( e, i ) <= IntBigArrays.get( e, i + 1 ) );
		for( long i = 100; i-- != 10; ) IntBigArrays.set( e, i, random.nextInt() );
		IntBigArrays.radixSort( d, e, 10, 100 );
		for( long i = 99; i-- != 10; ) assertTrue( Long.toString( i ) + ": <" + IntBigArrays.get( d, i ) + ", " + IntBigArrays.get( e, i ) + ">, <" + IntBigArrays.get( d, i + 1 ) + ", " +  IntBigArrays.get( e, i + 1 ) + ">", IntBigArrays.get( d, i ) < IntBigArrays.get( d, i + 1 ) || IntBigArrays.get( d, i ) == IntBigArrays.get( d, i + 1 ) && IntBigArrays.get( e, i ) <= IntBigArrays.get( e, i + 1 ) );

		d = IntBigArrays.newBigArray( 1000000 );
		random = new Random( 0 );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( d, i, random.nextInt() );
		e = IntBigArrays.newBigArray( IntBigArrays.length(  d ) );
		for( long i = IntBigArrays.length( d ); i-- != 0; ) IntBigArrays.set( e, i, random.nextInt() );
		IntBigArrays.radixSort( d, e );
		for( long i = IntBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + IntBigArrays.get( d, i ) + ", " + IntBigArrays.get( e, i ) + ">, <" + IntBigArrays.get( d, i + 1 ) + ", " +  IntBigArrays.get( e, i + 1 ) + ">", IntBigArrays.get( d, i ) < IntBigArrays.get( d, i + 1 ) || IntBigArrays.get( d, i ) == IntBigArrays.get( d, i + 1 ) && IntBigArrays.get( e, i ) <= IntBigArrays.get( e, i + 1 ) );
	}


	@Test
	public void testShuffle() {
		int[] a = new int[ 100 ];
		for( int i = a.length; i-- != 0; ) a[ i ] = i;
		int[][] b = IntBigArrays.wrap( a );
		IntBigArrays.shuffle( b, new Random() );
		boolean[] c = new boolean[ a.length ];
		for( long i = IntBigArrays.length( b ); i-- != 0; ) {
			assertFalse( c[ IntBigArrays.get( b, i ) ] );
			c[ IntBigArrays.get( b, i ) ] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		int[] a = new int[ 100 ];
		for( int i = a.length; i-- != 0; ) a[ i ] = -1;
		for( int i = 10; i < 30; i++ ) a[ i ] = i - 10;
		int[][] b = IntBigArrays.wrap( a );
		IntBigArrays.shuffle( b, 10, 30, new Random() );
		boolean[] c = new boolean[ 20 ];
		for( int i = 20; i-- != 0; ) {
			assertFalse( c[ IntBigArrays.get( b, i + 10 ) ] );
			c[ IntBigArrays.get( b, i + 10 ) ] = true;
		}
	}

	@Test
	public void testBinarySearchLargeKey() {
		final int[][] a = IntBigArrays.wrap( new int[] { 1, 2, 3 } );
		IntBigArrays.binarySearch( a, 4 );
	}

}
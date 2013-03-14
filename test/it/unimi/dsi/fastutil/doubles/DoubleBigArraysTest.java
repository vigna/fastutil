package it.unimi.dsi.fastutil.doubles;

import static org.junit.Assert.*;
import static it.unimi.dsi.fastutil.doubles.DoubleBigArrays.set;
import static it.unimi.dsi.fastutil.doubles.DoubleBigArrays.get;

import it.unimi.dsi.fastutil.ints.IntBigArrays;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class DoubleBigArraysTest {

	
	public static double[][] identity( final int n ) {
		final double[][] perm = DoubleBigArrays.newBigArray( n );
		for( int i = n; i-- != 0; ) DoubleBigArrays.set( perm, i , i );
		return perm;
	}

	@Test
	public void testQuickSort() {
		double[] s = new double[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		
		Arrays.sort( s );
		double[][] sorted = DoubleBigArrays.wrap( s.clone() );

		double[][] a = DoubleBigArrays.wrap( s.clone()  );

		DoubleBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );

		DoubleBigArrays.quickSort( a );
		assertArrayEquals( sorted, a );
		
		a = DoubleBigArrays.wrap( s.clone()  );
		
		DoubleBigArrays.quickSort( a, DoubleComparators.NATURAL_COMPARATOR );
		assertArrayEquals( sorted, a );

		DoubleBigArrays.quickSort( a, DoubleComparators.NATURAL_COMPARATOR );
		assertArrayEquals( sorted, a );
		
	}

	private void testCopy( int n ) {
		double[][] a = DoubleBigArrays.newBigArray( n );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		DoubleBigArrays.copy( a, 0, a, 1, n - 2 );
		assertEquals( 0, a[ 0 ][ 0 ], 0 );
		for ( int i = 0; i < n - 2; i++ ) assertEquals( i,  get( a, i + 1 ), 0 );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		DoubleBigArrays.copy( a, 1, a, 0, n - 1 );
		for ( int i = 0; i < n - 1; i++ ) assertEquals( i + 1, get( a, i ) ,0 );
		for ( int i = 0; i < n; i++ ) set( a, i, i );
		double[] b = new double[ n ];
		for ( int i = 0; i < n; i++ ) b[ i ] = i;
		assertArrayEquals( a, DoubleBigArrays.wrap( b ) );
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
		double[] a = new double[] { 25, 32, 1, 3, 2, 0, 40, 7, 13, 12, 11, 10, -1, -6, -18, 2000 };
		
		Arrays.sort( a );
		double[][] b = DoubleBigArrays.wrap( a.clone() );

		for( int i = -1; i < 20; i++ ) {
			assertEquals( "" + i, Arrays.binarySearch( a, i ), DoubleBigArrays.binarySearch( b, i ) );
			assertEquals( "" + i, Arrays.binarySearch( a, i ), DoubleBigArrays.binarySearch( b, i, DoubleComparators.NATURAL_COMPARATOR ) );
		}
	
		for( int i = -1; i < 20; i++ ) {
			assertEquals( Arrays.binarySearch( a, 5, 13, i ), DoubleBigArrays.binarySearch( b, 5, 13, i ) );
			assertEquals( Arrays.binarySearch( a, 5, 13, i ), DoubleBigArrays.binarySearch( b, 5, 13, i, DoubleComparators.NATURAL_COMPARATOR ) );
		}
	}

	@Test
	public void testTrim() {
		double[] a = new double[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		double[][] b = DoubleBigArrays.wrap( a.clone() );

		for( int i = a.length; i-- != 0; ) {
			double[][] t = DoubleBigArrays.trim( b, i );
			final long l = DoubleBigArrays.length( t );
			assertEquals( i, l );
			for( int p = 0; p < l; p++ ) assertEquals( a[ p ], DoubleBigArrays.get( t, p ), 0 );
			
		}
	}

	@Test
	public void testEquals() {
		double[] a = new double[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		double[][] b = DoubleBigArrays.wrap( a.clone() );
		double[][] c = DoubleBigArrays.wrap( a.clone() );

		assertTrue( DoubleBigArrays.equals( b, c ) );
		b[ 0 ][ 0 ] = 0;
		assertFalse( DoubleBigArrays.equals( b, c ) );
	}

	@Test
	public void testRadixSort1() {
		double[][] t = DoubleBigArrays.wrap( new double[] { 2, 1, 0, 4 } );
		DoubleBigArrays.radixSort( t );
		for( long i = DoubleBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );
		
		t = DoubleBigArrays.wrap( new double[] { 2, -1, 0, -4 } );
		DoubleBigArrays.radixSort( t );
		for( long i = DoubleBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );
		
		t = DoubleBigArrays.shuffle( identity( 100 ), new Random( 0 ) );
		DoubleBigArrays.radixSort( t );
		for( long i = DoubleBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );

		t = DoubleBigArrays.newBigArray( 100 );
		Random random = new Random( 0 );
		for( long i = DoubleBigArrays.length( t ); i-- != 0; ) DoubleBigArrays.set( t, i, random.nextInt() );
		DoubleBigArrays.radixSort( t );
		for( long i = DoubleBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );

		t = DoubleBigArrays.newBigArray( 100000 );
		random = new Random( 0 );
		for( long i = DoubleBigArrays.length( t ); i-- != 0; ) DoubleBigArrays.set( t, i, random.nextInt() );
		DoubleBigArrays.radixSort( t );
		for( long i = DoubleBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );
		for( long i = 100; i-- != 10; ) DoubleBigArrays.set( t, i, random.nextInt() );
		DoubleBigArrays.radixSort( t, 10, 100 );
		for( long i = 99; i-- != 10; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );

		t = DoubleBigArrays.newBigArray( 1000000 );
		random = new Random( 0 );
		for( long i = DoubleBigArrays.length( t ); i-- != 0; ) DoubleBigArrays.set( t, i, random.nextInt() );
		DoubleBigArrays.radixSort( t );
		for( long i = DoubleBigArrays.length( t ) - 1; i-- != 0; ) assertTrue( DoubleBigArrays.get( t, i ) <= DoubleBigArrays.get( t, i + 1 ) );
	}

	@Test
	public void testRadixSort2() {
		double d[][], e[][];
		d = DoubleBigArrays.newBigArray( 10 );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( d, i, (int)( 3 - i % 3 ) );
		e = DoubleBigArrays.shuffle( identity( 10 ), new Random( 0 ) );
		DoubleBigArrays.radixSort( d, e );
		for( long i = DoubleBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + DoubleBigArrays.get( d, i ) + ", " + DoubleBigArrays.get( e, i ) + ">, <" + DoubleBigArrays.get( d, i + 1 ) + ", " +  DoubleBigArrays.get( e, i + 1 ) + ">", DoubleBigArrays.get( d, i ) < DoubleBigArrays.get( d, i + 1 ) || DoubleBigArrays.get( d, i ) == DoubleBigArrays.get( d, i + 1 ) && DoubleBigArrays.get( e, i ) <= DoubleBigArrays.get( e, i + 1 ) );
		
		d = DoubleBigArrays.newBigArray( 100000 );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( d, i, (int)( 100 - i % 100 ) );
		e = DoubleBigArrays.shuffle( identity( 100000 ), new Random( 6 ) );
		DoubleBigArrays.radixSort( d, e );
		for( long i = DoubleBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + DoubleBigArrays.get( d, i ) + ", " + DoubleBigArrays.get( e, i ) + ">, <" + DoubleBigArrays.get( d, i + 1 ) + ", " +  DoubleBigArrays.get( e, i + 1 ) + ">", DoubleBigArrays.get( d, i ) < DoubleBigArrays.get( d, i + 1 ) || DoubleBigArrays.get( d, i ) == DoubleBigArrays.get( d, i + 1 ) && DoubleBigArrays.get( e, i ) <= DoubleBigArrays.get( e, i + 1 ) );

		d = DoubleBigArrays.newBigArray( 10 );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( d, i, (int)( i % 3 - 2 ) );
		Random random = new Random( 0 );
		e = DoubleBigArrays.newBigArray( DoubleBigArrays.length(  d ) );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( e, i, random.nextInt() );
		DoubleBigArrays.radixSort( d, e );
		for( long i = DoubleBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + DoubleBigArrays.get( d, i ) + ", " + DoubleBigArrays.get( e, i ) + ">, <" + DoubleBigArrays.get( d, i + 1 ) + ", " +  DoubleBigArrays.get( e, i + 1 ) + ">", DoubleBigArrays.get( d, i ) < DoubleBigArrays.get( d, i + 1 ) || DoubleBigArrays.get( d, i ) == DoubleBigArrays.get( d, i + 1 ) && DoubleBigArrays.get( e, i ) <= DoubleBigArrays.get( e, i + 1 ) );
		
		d = DoubleBigArrays.newBigArray( 100000 );
		random = new Random( 0 );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( d, i, random.nextInt() );
		e = DoubleBigArrays.newBigArray( DoubleBigArrays.length(  d ) );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( e, i, random.nextInt() );
		DoubleBigArrays.radixSort( d, e );
		for( long i = DoubleBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + DoubleBigArrays.get( d, i ) + ", " + DoubleBigArrays.get( e, i ) + ">, <" + DoubleBigArrays.get( d, i + 1 ) + ", " +  DoubleBigArrays.get( e, i + 1 ) + ">", DoubleBigArrays.get( d, i ) < DoubleBigArrays.get( d, i + 1 ) || DoubleBigArrays.get( d, i ) == DoubleBigArrays.get( d, i + 1 ) && DoubleBigArrays.get( e, i ) <= DoubleBigArrays.get( e, i + 1 ) );
		for( long i = 100; i-- != 10; ) DoubleBigArrays.set( e, i, random.nextInt() );
		DoubleBigArrays.radixSort( d, e, 10, 100 );
		for( long i = 99; i-- != 10; ) assertTrue( Long.toString( i ) + ": <" + DoubleBigArrays.get( d, i ) + ", " + DoubleBigArrays.get( e, i ) + ">, <" + DoubleBigArrays.get( d, i + 1 ) + ", " +  DoubleBigArrays.get( e, i + 1 ) + ">", DoubleBigArrays.get( d, i ) < DoubleBigArrays.get( d, i + 1 ) || DoubleBigArrays.get( d, i ) == DoubleBigArrays.get( d, i + 1 ) && DoubleBigArrays.get( e, i ) <= DoubleBigArrays.get( e, i + 1 ) );

		d = DoubleBigArrays.newBigArray( 1000000 );
		random = new Random( 0 );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( d, i, random.nextInt() );
		e = DoubleBigArrays.newBigArray( DoubleBigArrays.length(  d ) );
		for( long i = DoubleBigArrays.length( d ); i-- != 0; ) DoubleBigArrays.set( e, i, random.nextInt() );
		DoubleBigArrays.radixSort( d, e );
		for( long i = DoubleBigArrays.length( d ) - 1; i-- != 0; ) assertTrue( Long.toString( i ) + ": <" + DoubleBigArrays.get( d, i ) + ", " + DoubleBigArrays.get( e, i ) + ">, <" + DoubleBigArrays.get( d, i + 1 ) + ", " +  DoubleBigArrays.get( e, i + 1 ) + ">", DoubleBigArrays.get( d, i ) < DoubleBigArrays.get( d, i + 1 ) || DoubleBigArrays.get( d, i ) == DoubleBigArrays.get( d, i + 1 ) && DoubleBigArrays.get( e, i ) <= DoubleBigArrays.get( e, i + 1 ) );
	}


	@Test
	public void testShuffle() {
		double[] a = new double[ 100 ];
		for( int i = a.length; i-- != 0; ) a[ i ] = i;
		double[][] b = DoubleBigArrays.wrap( a );
		DoubleBigArrays.shuffle( b, new Random() );
		boolean[] c = new boolean[ a.length ];
		for( long i = DoubleBigArrays.length( b ); i-- != 0; ) {
			assertFalse( c[ (int)DoubleBigArrays.get( b, i ) ] );
			c[ (int)DoubleBigArrays.get( b, i ) ] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		double[] a = new double[ 100 ];
		for( int i = a.length; i-- != 0; ) a[ i ] = -1;
		for( int i = 10; i < 30; i++ ) a[ i ] = i - 10;
		double[][] b = DoubleBigArrays.wrap( a );
		DoubleBigArrays.shuffle( b, 10, 30, new Random() );
		boolean[] c = new boolean[ 20 ];
		for( int i = 20; i-- != 0; ) {
			assertFalse( c[ (int)DoubleBigArrays.get( b, i + 10 ) ] );
			c[ (int)DoubleBigArrays.get( b, i + 10 ) ] = true;
		}
	}

	@Test
	public void testBinarySearchLargeKey() {
		final double[][] a = DoubleBigArrays.wrap( new double[] { 1, 2, 3 } );
		DoubleBigArrays.binarySearch( a, 4 );
	}

	@Test
	public void testMergeSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for( int to = 1; to < t.length; to++ )
			for( int from = 0; from < to; from++ ) {
				final double[] a = t.clone();
				DoubleArrays.mergeSort( a, from, to );
				for( int i = to - 1; i-- != from; ) assertTrue( Double.compare( a[ i ], a[ i + 1 ] ) <= 0 );
			}
		
	}


	@Test
	public void testRadixSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for( int to = 1; to < t.length; to++ )
			for( int from = 0; from < to; from++ ) {
				final double[] a = t.clone();
				DoubleBigArrays.radixSort( DoubleBigArrays.wrap( a ), from, to );
				for( int i = to - 1; i-- != from; ) assertTrue( Double.compare( a[ i ], a[ i + 1 ] ) <= 0 );
			}
		
	}

	@Test
	public void testRadixSort2NaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for( int to = 1; to < t.length; to++ )
			for( int from = 0; from < to; from++ ) {
				final double[] a = t.clone();
				final double[] b = t.clone();
				DoubleBigArrays.radixSort( DoubleBigArrays.wrap( a ), DoubleBigArrays.wrap( b ), from, to );
				for( int i = to - 1; i-- != from; ) {
					assertTrue( Double.compare( a[ i ], a[ i + 1 ] ) <= 0 );
					assertTrue( Double.compare( b[ i ], b[ i + 1 ] ) <= 0 );
				}
			}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testQuickSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for( int to = 1; to < t.length; to++ )
			for( int from = 0; from < to; from++ ) {
				final double[] a = t.clone();
				DoubleBigArrays.quickSort( DoubleBigArrays.wrap( a ), from, to );
				for( int i = to - 1; i-- != from; ) assertTrue( Double.compare( a[ i ], a[ i + 1 ] ) <= 0 );
			}
		
	}


}
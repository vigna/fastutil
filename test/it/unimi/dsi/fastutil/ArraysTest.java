package it.unimi.dsi.fastutil;

import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.ints.AbstractIntComparator;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntArraysTest;

import java.util.Random;

import org.junit.Test;

public class ArraysTest {
	

	private static void testMergeSort( final int x[] ) {
		testMergeSort( x, 0, x.length );
	}
	
	private static void testMergeSort( final int x[], int from, int to ) {
		Arrays.mergeSort( from, to, new AbstractIntComparator() {
			private static final long serialVersionUID = 1L;

			@Override
			public int compare( int k1, int k2 ) {
				return Integer.compare( x[ k1 ], x[ k2 ] ); 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = x[ k1 ];
				x[ k1 ] = x[ k2 ];
				x[ k2 ] = t;
			}
		});
		for( int i = to - 1; i-- != from; ) assertTrue( x[ i ] <= x[ i + 1 ] );
	}

	@Test
	public void testMergeSort() {
		testMergeSort( new int[] { 2, 1, 0, 4 } );
		testMergeSort( new int[] { 2, -1, 0, -4 } );
		testMergeSort( IntArrays.shuffle( IntArraysTest.identity( 100 ), new Random( 0 ) ) );

		int[] t = new int[ 100 ];
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testMergeSort( t );
		t = new int[ 100000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testMergeSort( t );
		for( int i = 100; i-- != 10; ) t[ i ] = random.nextInt();
		testMergeSort( t, 10, 100 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt() & 0xF;
		testMergeSort( t );

		t = new int[ 10000000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testMergeSort( t );
	}
	
	private static void testQuickSort( final int x[] ) {
		testQuickSort( x, 0, x.length );
	}
	
	private static void testQuickSort( final int x[], int from, int to ) {
		Arrays.quickSort( from, to, new AbstractIntComparator() {
			private static final long serialVersionUID = 1L;

			@Override
			public int compare( int k1, int k2 ) {
				return Integer.compare( x[ k1 ], x[ k2 ] ); 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = x[ k1 ];
				x[ k1 ] = x[ k2 ];
				x[ k2 ] = t;
			}
		});
		for( int i = to - 1; i-- != from; ) assertTrue( x[ i ] <= x[ i + 1 ] );
	}

	@Test
	public void testQuickSort() {
		testQuickSort( new int[] { 2, 1, 0, 4 } );
		testQuickSort( new int[] { 2, -1, 0, -4 } );
		testQuickSort( IntArrays.shuffle( IntArraysTest.identity( 100 ), new Random( 0 ) ) );

		int[] t = new int[ 100 ];
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testQuickSort( t );
		t = new int[ 100000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testQuickSort( t );
		for( int i = 100; i-- != 10; ) t[ i ] = random.nextInt();
		testQuickSort( t, 10, 100 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt() & 0xF;
		testQuickSort( t );

		t = new int[ 10000000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testQuickSort( t );
	}

	private static void testParallelQuickSort( final int x[] ) {
		testParallelQuickSort( x, 0, x.length );
	}
	
	private static void testParallelQuickSort( final int x[], int from, int to ) {
		Arrays.parallelQuickSort( from, to, new AbstractIntComparator() {
			private static final long serialVersionUID = 1L;

			@Override
			public int compare( int k1, int k2 ) {
				return Integer.compare( x[ k1 ], x[ k2 ] ); 
			}
		}, new Swapper() {
			@Override
			public void swap( int k1, int k2 ) {
				final int t = x[ k1 ];
				x[ k1 ] = x[ k2 ];
				x[ k2 ] = t;
			}
		});
		for( int i = to - 1; i-- != from; ) assertTrue( x[ i ] <= x[ i + 1 ] );
	}
	
	@Test
	public void testParallelQuickSort() {
		testParallelQuickSort( new int[] { 2, 1, 0, 4 } );
		testParallelQuickSort( new int[] { 2, -1, 0, -4 } );
		testParallelQuickSort( IntArrays.shuffle( IntArraysTest.identity( 100 ), new Random( 0 ) ) );

		int[] t = new int[ 100 ];
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testParallelQuickSort( t );
		t = new int[ 100000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testParallelQuickSort( t );
		for( int i = 100; i-- != 10; ) t[ i ] = random.nextInt();
		testParallelQuickSort( t, 10, 100 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt() & 0xF;
		testParallelQuickSort( t );

		t = new int[ 10000000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		testParallelQuickSort( t );
	}

}

package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class IntArraysTest {
	
	private static int[] identity( final int n ) {
		final int[] perm = new int[ n ];
		for( int i = perm.length; i-- != 0; ) perm[ i ] = i;
		return perm;
	}

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
	
	@Test
	public void testShuffle() {
		int[] a = new int[ 100 ];
		for( int i = a.length; i-- != 0; ) a[ i ] = i;
		IntArrays.shuffle( a, new Random() );
		boolean[] b = new boolean[ a.length ];
		for( int i = a.length; i-- != 0; ) {
			assertFalse( b[ a[ i ] ] );
			b[ a[ i ] ] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		int[] a = new int[ 100 ];
		for( int i = a.length; i-- != 0; ) a[ i ] = -1;
		for( int i = 10; i < 30; i++ ) a[ i ] = i - 10;
		IntArrays.shuffle( a, 10, 30, new Random() );
		boolean[] b = new boolean[ 20 ];
		for( int i = 20; i-- != 0; ) {
			assertFalse( b[ a[ i + 10 ] ] );
			b[ a[ i + 10 ] ] = true;
		}
	}

	@Test
	public void testRadixSort1() {
		int[] t = { 2, 1, 0, 4 };
		IntArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );
		
		t = new int[] { 2, -1, 0, -4 };
		IntArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );
		
		t = IntArrays.shuffle( identity( 100 ), new Random( 0 ) );
		IntArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );

		t = new int[ 100 ];
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );

		t = new int[ 100000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );

		t = new int[ 10000000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );
	}

	@Test
	public void testRadixSort2() {
		int[][] d = new int[ 2 ][];

		d[ 0 ] = new int[ 10 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 3 - i % 3;
		d[ 1 ] = IntArrays.shuffle( identity( 10 ), new Random( 0 ) );
		IntArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
		
		d[ 0 ] = new int[ 100000 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 100 - i % 100;
		d[ 1 ] = IntArrays.shuffle( identity( 100000 ), new Random( 6 ) );
		IntArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new int[ 10 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = i % 3 - 2;
		Random random = new Random( 0 );
		d[ 1 ] = new int[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		IntArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
		
		d[ 0 ] = new int[ 100000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new int[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		IntArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new int[ 10000000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new int[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		IntArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
	}

	@Test
	public void testRadixSort() {
		int[][] t = { { 2, 1, 0, 4 } };
		IntArrays.radixSort( t );
		for( int i = t[ 0 ].length - 1; i-- != 0; ) assertTrue( t[ 0 ][ i ] <= t[ 0 ][ i + 1 ] );
		
		t[ 0 ] = IntArrays.shuffle( identity( 100 ), new Random( 0 ) );
		IntArrays.radixSort( t );
		for( int i = t[ 0 ].length - 1; i-- != 0; ) assertTrue( t[ 0 ][ i ] <= t[ 0 ][ i + 1 ] );

		int[][] d = new int[ 2 ][];

		d[ 0 ] = new int[ 10 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 3 - i % 3;
		d[ 1 ] = IntArrays.shuffle( identity( 10 ), new Random( 0 ) );
		IntArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		
		d[ 0 ] = new int[ 100000 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 100 - i % 100;
		d[ 1 ] = IntArrays.shuffle( identity( 100000 ), new Random( 6 ) );
		IntArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new int[ 10 ];
		Random random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new int[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		IntArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		
		d[ 0 ] = new int[ 100000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new int[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		IntArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new int[ 10000000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new int[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		IntArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
	}

	@Test
	public void testRadixSortIndirectStable() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );
		
		t = new int[ t.length ];
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertEquals( i, perm[ i ] );
		
		t = new int[] { 2, -1, 0, -4 };
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );
		
		t = IntArrays.shuffle( identity( 100 ), new Random( 0 ) );
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ 100 ];
		perm = identity( t.length );
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ t.length ];
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertEquals( i, perm[ i ] );
		
		t = new int[ t.length ];
		for( int i = 0; i < t.length; i++ ) t[ i ] = random.nextInt( 4 ); 
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) if ( t[ perm[ i ] ] == t[ perm[ i + 1 ] ] ) assertTrue( perm[ i ] < perm[ i + 1 ] );

		t = new int[ 100 ];
		perm = identity( t.length );
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, 10, 90, true );
		for( int i = 10; i < 89; i++ ) assertTrue( Integer.toString( i ), t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );
		for( int i = 0; i < 10; i++ ) assertEquals( i, perm[ i ] );
		for( int i = 90; i < 100; i++ ) assertEquals( i, perm[ i ] );

		t = new int[ 100000 ];
		perm = identity( t.length );
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( Integer.toString( i ), t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		IntArrays.shuffle( perm, new Random( 0 ) );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( Integer.toString( i ), t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ 10000000 ];
		perm = identity( t.length );
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ t.length ];
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) assertEquals( i, perm[ i ] );

		t = new int[ t.length ];
		for( int i = 0; i < t.length; i++ ) t[ i ] = random.nextInt( 8 ); 
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, true );
		for( int i = t.length - 1; i-- != 0; ) if ( t[ perm[ i ] ] == t[ perm[ i + 1 ] ] ) assertTrue( perm[ i ] < perm[ i + 1 ] );
	}
	
	@Test
	public void testRadixSortIndirectUnstable() {
		int[] t = { 2, 1, 0, 4 };
		int[] perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );
		
		t = new int[ t.length ];
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertEquals( i, perm[ i ] );
		
		t = new int[] { 2, -1, 0, -4 };
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );
		
		t = IntArrays.shuffle( identity( 100 ), new Random( 0 ) );
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ 100 ];
		perm = identity( t.length );
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		/*t = new int[ 100 ];
		perm = identity( t.length );
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, 10, 90, false );
		for( int i = 10; i < 89; i++ ) assertTrue( Integer.toString( i ), t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );
		for( int i = 0; i < 10; i++ ) assertEquals( i, perm[ i ] );
		for( int i = 90; i < 100; i++ ) assertEquals( i, perm[ i ] );
*/
		t = new int[ 100000 ];
		perm = identity( t.length );
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( Integer.toString( i ), t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		IntArrays.shuffle( perm, new Random( 0 ) );
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( Integer.toString( i ), t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ 10000000 ];
		perm = identity( t.length );
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ perm[ i ] ] <= t[ perm[ i + 1 ] ] );

		t = new int[ t.length ];
		perm = identity( t.length );
		IntArrays.radixSortIndirect( perm, t, false );
		for( int i = t.length - 1; i-- != 0; ) assertEquals( i, perm[ i ] );
	}
}

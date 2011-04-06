package it.unimi.dsi.fastutil.doubles;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Random;

public class DoubleArraysTest {
	
	private static double[] identity( int n ) {
		final double[] a = new double[ n ];
		while( n-- != 0 ) a[ n ] = n;
		return a;
	}

	@Test
	public void testRadixSort1() {
		double[] t = { 2, 1, 0, 4 };
		DoubleArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );
		
		t = new double[] { 2, -1, 0, -4 };
		DoubleArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );
		
		t = DoubleArrays.shuffle( identity( 100 ), new Random( 0 ) );
		DoubleArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );

		t = new double[ 100 ];
		Random random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		DoubleArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );

		t = new double[ 100000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		DoubleArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );

		t = new double[ 10000000 ];
		random = new Random( 0 );
		for( int i = t.length; i-- != 0; ) t[ i ] = random.nextInt();
		DoubleArrays.radixSort( t );
		for( int i = t.length - 1; i-- != 0; ) assertTrue( t[ i ] <= t[ i + 1 ] );
	}

	@Test
	public void testRadixSort2() {
		double[][] d = new double[ 2 ][];

		d[ 0 ] = new double[ 10 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 3 - i % 3;
		d[ 1 ] = DoubleArrays.shuffle( identity( 10 ), new Random( 0 ) );
		DoubleArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
		
		d[ 0 ] = new double[ 100000 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 100 - i % 100;
		d[ 1 ] = DoubleArrays.shuffle( identity( 100000 ), new Random( 6 ) );
		DoubleArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new double[ 10 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = i % 3 - 2;
		Random random = new Random( 0 );
		d[ 1 ] = new double[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		DoubleArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
		
		d[ 0 ] = new double[ 100000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new double[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		DoubleArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new double[ 10000000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new double[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		DoubleArrays.radixSort( d[ 0 ], d[ 1 ] );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
	}

	@Test
	public void testRadixSort() {
		double[][] t = { { 2, 1, 0, 4 } };
		DoubleArrays.radixSort( t );
		for( int i = t[ 0 ].length - 1; i-- != 0; ) assertTrue( t[ 0 ][ i ] <= t[ 0 ][ i + 1 ] );
		
		t[ 0 ] = DoubleArrays.shuffle( identity( 100 ), new Random( 0 ) );
		DoubleArrays.radixSort( t );
		for( int i = t[ 0 ].length - 1; i-- != 0; ) assertTrue( t[ 0 ][ i ] <= t[ 0 ][ i + 1 ] );

		double[][] d = new double[ 2 ][];

		d[ 0 ] = new double[ 10 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 3 - i % 3;
		d[ 1 ] = DoubleArrays.shuffle( identity( 10 ), new Random( 0 ) );
		DoubleArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		
		d[ 0 ] = new double[ 100000 ];
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = 100 - i % 100;
		d[ 1 ] = DoubleArrays.shuffle( identity( 100000 ), new Random( 6 ) );
		DoubleArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new double[ 10 ];
		Random random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new double[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		DoubleArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		
		d[ 0 ] = new double[ 100000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new double[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		DoubleArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );

		d[ 0 ] = new double[ 10000000 ];
		random = new Random( 0 );
		for( int i = d[ 0 ].length; i-- != 0; ) d[ 0 ][ i ] = random.nextInt();
		d[ 1 ] = new double[ d[ 0 ].length ];
		for( int i = d.length; i-- != 0; ) d[ 1 ][ i ] = random.nextInt();
		DoubleArrays.radixSort( d );
		for( int i = d[ 0 ].length - 1; i-- != 0; ) assertTrue( Integer.toString( i ) + ": <" + d[ 0 ][ i ] + ", " + d[ 1 ][ i ] + ">, <" + d[ 0 ][ i + 1 ] + ", " +  d[ 1 ][ i + 1 ] + ">", d[ 0 ][ i ] < d[ 0 ][ i + 1 ] || d[ 0 ][ i ] == d[ 0 ][ i + 1 ] && d[ 1 ][ i ] <= d[ 1 ][ i + 1 ] );
	}
}

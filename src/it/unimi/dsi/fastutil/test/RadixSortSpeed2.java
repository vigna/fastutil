package it.unimi.dsi.fastutil.test;

import it.unimi.dsi.fastutil.ints.IntArrays;

public class RadixSortSpeed2 {
	public static void main( String[] args ) {
		SplitMix64 random = new SplitMix64();
		final int n = Integer.parseInt( args[ 0 ] );
		int[] a = new int[ n ];
		int[] b = new int[ n ];
		long start;
		
		for( int repeat = 3; repeat-- != 0; ) {
			random.state = 0;
			for( int i = n; i-- != 0; ) { a[ i ] = (int)random.nextLong(); b[ i ] = (int)random.nextLong(); }
			start = System.nanoTime();
			IntArrays.radixSort( a, b );
			System.err.println( "fastutil sequential: " + ( System.nanoTime() - start ) / 1E9 + "s" );

			random.state = 0;
			for( int i = n; i-- != 0; ) { a[ i ] = (int)random.nextLong(); b[ i ] = (int)random.nextLong(); }
			start = System.nanoTime();
			IntArrays.parallelRadixSort( a, b, 0, n );
			System.err.println( "fastutil parallel: " + ( System.nanoTime() - start ) / 1E9 + "s" );

		}
	}
}

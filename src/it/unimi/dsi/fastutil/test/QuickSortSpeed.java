package it.unimi.dsi.fastutil.test;

import java.util.Arrays;

import it.unimi.dsi.fastutil.ints.IntArrays;

public class QuickSortSpeed {
	public static void main( String[] args ) {
		SplitMix64 random = new SplitMix64();
		final int n = Integer.parseInt( args[ 0 ] );
		int[] a = new int[ n ];
		long start;
		
		for( int repeat = 3; repeat-- != 0; ) {
			random.state = 0;
			for( int i = n; i-- != 0; ) a[ i ] = (int)random.nextLong();
			start = System.nanoTime();
			IntArrays.quickSort( a );
			System.err.println( "fastutil sequential: " + ( System.nanoTime() - start ) / 1E9 + "s" );
	
			random.state = 0;
			for( int i = n; i-- != 0; ) a[ i ] = (int)random.nextLong();
			start = System.nanoTime();
			IntArrays.parallelQuickSort( a, 0, a.length );
			System.err.println( "fastutil parallel: " + ( System.nanoTime() - start ) / 1E9 + "s" );

			random.state = 0;
			for( int i = n; i-- != 0; ) a[ i ] = (int)random.nextLong();
			start = System.nanoTime();
			Arrays.parallelSort( a );
			System.err.println( "java.util parallel: " + ( System.nanoTime() - start ) / 1E9 + "s" );
		}
	}
}

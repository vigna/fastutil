package it.unimi.dsi.fastutil.test;

import it.unimi.dsi.fastutil.ints.AbstractIntComparator;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;

public class IndirectLongSortSpeed {
	private static SplitMix64 random = new SplitMix64();

	public static void main( String[] args ) {
		final int n = Integer.parseInt( args[ 0 ] );
		final int[] a = new int[ n ];
		final long[] b = new long[ n ];
		long start;
		
		for( int repeat = 3; repeat-- != 0; ) {
			random.state = 0;
			for( int i = n; i-- != 0; ) {
				a[ i ] = i;
				b[ i ] = random.nextLong();
			}
			start = System.nanoTime();
			IntArrays.quickSort( a, new AbstractIntComparator() {
				@Override
				public int compare( int x, int y ) {
					return Long.compare( b[ x ], b[ y ] );
				}
			} );
			System.err.println( "sequential quicksort: " + ( System.nanoTime() - start ) / 1E9 + "s" );

			random.state = 0;
			for( int i = n; i-- != 0; ) {
				a[ i ] = i;
				b[ i ] = random.nextLong();
			}
			start = System.nanoTime();
			IntArrays.parallelQuickSort( a, new AbstractIntComparator() {
				@Override
				public int compare( int x, int y ) {
					return Long.compare( b[ x ], b[ y ] );
				}
			} );
			System.err.println( "parallel quicksort: " + ( System.nanoTime() - start ) / 1E9 + "s" );

			random.state = 0;
			for( int i = n; i-- != 0; ) {
				a[ i ] = i;
				b[ i ] = random.nextLong();
			}
			start = System.nanoTime();
			LongArrays.radixSortIndirect( a, b, false );
			System.err.println( "sequential radixsort: " + ( System.nanoTime() - start ) / 1E9 + "s" );

			random.state = 0;
			for( int i = n; i-- != 0; ) {
				a[ i ] = i;
				b[ i ] = random.nextLong();
			}
			start = System.nanoTime();
			LongArrays.parallelRadixSortIndirect( a, b, false );
			System.err.println( "parallel radixsort: " + ( System.nanoTime() - start ) / 1E9 + "s" );
		}
	}
}

package it.unimi.dsi.fastutil.test;

import java.util.Arrays;

import it.unimi.dsi.fastutil.ints.IntArrays;

public class RadixSortSpeed2 {
	private static int x;
	/** 2<sup>64</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
	private static final long PHI = 0x9E3779B97F4A7C15L;

	/* David Stafford's (http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html)
     * "Mix13" variant of the 64-bit finalizer in Austin Appleby's MurmurHash3 algorithm. */
	private static long staffordMix13( long z ) {
		z = ( z ^ ( z >>> 30 ) ) * 0xBF58476D1CE4E5B9L; 
		z = ( z ^ ( z >>> 27 ) ) * 0x94D049BB133111EBL;
		return z ^ ( z >>> 31 );
	}

	public static long nextLong() {
		return staffordMix13( x += PHI );
	}

	public static void main( String[] args ) {
		final int n = Integer.parseInt( args[ 0 ] );
		int[] a = new int[ n ];
		int[] b = new int[ n ];
		long start;
		
		for( int repeat = 3; repeat-- != 0; ) {
			x = 0;
			for( int i = n; i-- != 0; ) { a[ i ] = (int)nextLong(); b[ i ] = (int)nextLong(); }
			start = System.nanoTime();
			IntArrays.radixSort( a, b );
			System.err.println( ( System.nanoTime() - start ) / 1E9 + "s" );

		}
	}
}

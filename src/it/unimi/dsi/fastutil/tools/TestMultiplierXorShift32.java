package it.unimi.dsi.fastutil.tools;

import java.math.BigInteger;
import java.util.Arrays;

public class TestMultiplierXorShift32 {
	/** 2<sup>64</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
    /** 2<sup>32</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
    public static final int INT_PHI = 0x9E3779B9;
    /** 2<sup>64</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
    public static final long LONG_PHI = 0x9E3779B97F4A7C15L;

    private static long state = System.nanoTime();
	
	/* David Stafford's (http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html)
     * "Mix13" variant of the 64-bit finalizer in Austin Appleby's MurmurHash3 algorithm. */
	private static long staffordMix13( long z ) {
		z = ( z ^ ( z >>> 30 ) ) * 0xBF58476D1CE4E5B9L; 
		z = ( z ^ ( z >>> 27 ) ) * 0x94D049BB133111EBL;
		return z ^ ( z >>> 31 );
	}

	public static long nextLong() {
		return staffordMix13( state += LONG_PHI );
	}
	
	public static void main( String[] args ) {
		final int multiplier = new BigInteger( args[ 0 ] ).intValue();
		final int shift = Integer.parseInt( args[ 1 ] );
		int[] count = new int[ 32 ];
		Arrays.fill( count, 0 );

		for( long n = 1; ; n++ ) {
			final int x = (int)nextLong();
			final int mapped = ( multiplier * x ) ^ ( ( multiplier * x ) >>> shift );
			int flippedBitMask = 1;
			for( int f = 32; f-- != 0; ) {
				final int t  = multiplier * ( x ^ flippedBitMask );
				final int flippedMapped = mapped ^ t ^ ( t >>> shift );
				int bitMask = 1 << 31;
				for( int b = 32; b-- != 0; ) {
					if ( ( bitMask & flippedMapped ) != 0 ) count[ b ]++; 
					bitMask >>>= 1;
				}

				flippedBitMask <<= 1;
			}

			if ( ( n & 0xFFFFF ) == 0 ) {
				double chiSquared = 0;
				double maxBias = 0;
				for( int b = 2; b < 32; b++ ) {
					final double d = Math.abs( 0.5 - count[ b ] / ( 32. * n ) );
					chiSquared += d * d;
					maxBias = Math.max( maxBias, d / 0.5 );
				}
				System.out.println( "maxBias=" + maxBias + ", chiSquared=" + 2 * chiSquared );
			}
		}
	}
}

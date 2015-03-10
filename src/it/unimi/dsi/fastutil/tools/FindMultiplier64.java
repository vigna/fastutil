package it.unimi.dsi.fastutil.tools;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FindMultiplier64 {
	/** 2<sup>64</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
	private static final long PHI = 0x9E3779B97F4A7C15L;
	private static final int TOP = 20;
	private static long state = System.nanoTime();
	
	/* David Stafford's (http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html)
     * "Mix13" variant of the 64-bit finalizer in Austin Appleby's MurmurHash3 algorithm. */
	private static long staffordMix13( long z ) {
		z = ( z ^ ( z >>> 30 ) ) * 0xBF58476D1CE4E5B9L; 
		z = ( z ^ ( z >>> 27 ) ) * 0x94D049BB133111EBL;
		return z ^ ( z >>> 31 );
	}

	public static long nextLong() {
		return staffordMix13( state += PHI );
	}

	public final static class Candidate implements Comparable<Candidate> {

		public final long multiplier;
		public double chiSquared;
		public double bias;

		public Candidate( long multiplier ) {
			this.multiplier = multiplier;
		}

		@Override
		public String toString() {
			return "Candidate [multiplier=0x" + Long.toHexString( multiplier ) + ", bias=" + bias + ", chiSquared=" + chiSquared + "]";
		}

		@Override
		public int compareTo( Candidate o ) {
			final int t = Double.compare( bias, o.bias );
			if ( t != 0 ) return t;
			return Double.compare( chiSquared, o.chiSquared );
		}

		@Override
		public boolean equals( Object obj ) {
			if ( this == obj ) return true;
			if ( obj == null ) return false;
			if ( getClass() != obj.getClass() ) return false;
			Candidate other = (Candidate)obj;
			if ( multiplier != other.multiplier ) return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int)( multiplier ^ ( multiplier >>> 32 ) );
			return result;
		}
	}
	
	public static void main( String[] args ) throws InterruptedException {
		final ArrayBlockingQueue<Candidate> candidates = new ArrayBlockingQueue<Candidate>( 1000000 );
		final ObjectSet<Candidate> scores = ObjectSets.synchronize( new ObjectOpenHashSet<Candidate>() );
		final ObjectSet<Candidate> forbidden = ObjectSets.synchronize( new ObjectOpenHashSet<Candidate>() );
		final int processors = Runtime.getRuntime().availableProcessors();
		final Thread[] thread = new Thread[ processors ];
			
		Runnable barrierAction = new Runnable() {
			private ObjectSet<Candidate> top = new ObjectOpenHashSet<Candidate>();
			Candidate best;

			@Override
			public void run() {
				Candidate[] array = scores.toArray( new Candidate[ scores.size() ] );
				Arrays.sort( array );
				if ( array.length > 0 && ( best == null || array[ 0 ].compareTo( best ) < 0 ) ) best = array[ 0 ];
				
				final ObjectOpenHashSet<Candidate> currentTop = new ObjectOpenHashSet<Candidate>( array, 0, Math.min( TOP, array.length ) );
				if ( top.equals( currentTop ) ) {
					System.err.println( "Fixed point: restarting" );
					array = new Candidate[ 0 ];
					forbidden.addAll( top );
					top.clear();
				}
				else top = currentTop;
				scores.clear();
				for( int i = 0; i < Math.min( 10, array.length ); i++ ) System.out.println( array[ i ] );
				
				// Generate new candidates
				for( int i = 0; i < Math.min( TOP, array.length ); i++ ) {
					final Candidate c = array[ i ];
					candidates.add( c );

					candidates.add( new Candidate( c.multiplier << 1 | 1 ) );

					for( int b0 = 64; b0-- != 1; ) {
						candidates.add( new Candidate( c.multiplier ^ ( 1 << b0 ) ) );
						for( int b1 = b0; b1-- != 1; ) {
							candidates.add( new Candidate( c.multiplier ^ ( 1 << b0 ) ^ ( 1 << b1 ) ) );
						}
					}

					for( long m : new long[] { 0x33333333, 0x55555555, 0x99999999 } ) {
						candidates.add( new Candidate( c.multiplier & 0xFFFFFFFF | m << 32 ) );
						candidates.add( new Candidate( c.multiplier & 0xFFFFFFFF00000000L | m ) );
					}

					
					for( int j = 0; j < Math.min( TOP, array.length ); j++ ) {
						final Candidate d = array[ j ];
						if ( c.multiplier == d.multiplier ) continue;
						candidates.add( new Candidate( c.multiplier ^ d.multiplier | 1 ) );
						candidates.add( new Candidate( c.multiplier + d.multiplier | 1 ) );

						candidates.add( new Candidate( c.multiplier & 0xFFFFFFFF | d.multiplier & 0xFFFFFFFF00000000L ) );
					}
				}

				for( int r = 64; r-- != 0; ) {
					final int multiplier = (int)nextLong() | 1;
					candidates.add( new Candidate( multiplier ) );
				}

				candidates.removeAll( forbidden );
				System.out.println();
				System.out.println( "Best: " + best );
				System.out.println( "Now testing " + candidates.size() + " candidates " );
			}
		};
		final CyclicBarrier barrier = new CyclicBarrier( processors, barrierAction );
		for ( int i = thread.length; i-- != 0; ) thread[ i ] = new Thread() {
			public void run() {
				final int[] count = new int[ 64 ];
				for(;;) {
					Candidate candidate;
					try {
						for(;;) {
							candidate = candidates.poll();
							while ( candidate == null ) {
								barrier.await();
								candidate = candidates.poll();
							}

							if ( candidate.chiSquared != 0 ) scores.add(  candidate );
							else break;
						}
					}
					catch ( InterruptedException e ) {
						return;
					}
					catch ( BrokenBarrierException e ) {
						throw new RuntimeException( e.getMessage(), e );
					}
					final long multiplier = candidate.multiplier;

					Arrays.fill( count, 0 );

					// Enumerate low-entropy vectors, map, flip a bit, remap, count flipped bits.
					long x = 1;	
					long n = 0;
					do {
						n++;
						final long mapped = ( multiplier * x );
						long flippedBitMask = 1;
						for( int f = 64; f-- != 0; ) {
							final long flippedMapped = mapped ^ multiplier * ( x ^ flippedBitMask );
							long bitMask = 1L << 63;
							for( int b = 64; b-- != 0; ) {
								if ( ( bitMask & flippedMapped ) != 0 ) count[ b ]++; 
								bitMask >>>= 1;
							}

							flippedBitMask <<= 1;
						}

						if ( x == 1L << 63 ) {
							x = 0x3;
							continue;
						}
						else if ( x == 0x3L << 62 ) {
							x = 0x7;
							continue;
						}
						else if ( x == 0x7L << 61 ) break;

						// Gosper's trick
						final long c = ( x & -x );
						final long r = x + c;
						x = ( ( ( r ^ x ) >>> 2 ) / c ) | r;
					} while ( x != 0 );

					double chiSquared = 0;
					double maxBias = 0;
					for( int b = 8; b < 64; b++ ) {
						final double d = Math.abs( 0.5 - count[ b ] / ( 64. * n ) );
						chiSquared += d * d;
						maxBias = Math.max( maxBias, d / 0.5 );
					}

					candidate.chiSquared = 2 * chiSquared;
					candidate.bias = maxBias;
					scores.add( candidate );
				}
			}
		};

		for( int shift = 16; shift <= 48; shift++ ) { 
			candidates.add( new Candidate( 0x3333333333333333L ) );
			candidates.add( new Candidate( 0x5555555555555555L ) );
			candidates.add( new Candidate( 0x9999999999999999L ) );
		}

		for( Thread t: thread ) t.start();
		for( Thread t: thread ) t.join();
	}
}

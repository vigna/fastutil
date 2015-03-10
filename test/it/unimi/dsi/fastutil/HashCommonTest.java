package it.unimi.dsi.fastutil;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashCommonTest {
	@Test
	public void testMaxFillSmall() {
		for( float f: new float[] { 0.0001f, .25f, .50f, .75f, .9999f } ) {
			for( int i = 0; i < 16; i++ ) {
				final int n = HashCommon.arraySize( i, f );
				final int maxFill = HashCommon.maxFill( n, f );
				assertTrue( n + " <= " + maxFill, n > maxFill );
			}

			for( long i = 0; i < 16; i++ ) {
				final long n = HashCommon.bigArraySize( i, f );
				final long maxFill = HashCommon.maxFill( n, f );
				assertTrue( n + " <= " + maxFill, n > maxFill );
			}
		}
	}
	
	@Test
	public void testInverses() {
		for( int i = 0 ; i < 1 << 30; i += 10000 ) {
			assertEquals( i, HashCommon.invMix( HashCommon.mix( i ) ) );
			assertEquals( i, HashCommon.mix( HashCommon.invMix( i ) ) );
		}
		for( long i = 0 ; i < 1 << 62; i += 1000000 ) {
			assertEquals( i, HashCommon.invMix( HashCommon.mix( i ) ) );
			assertEquals( i, HashCommon.mix( HashCommon.invMix( i ) ) );
		}
	}
}

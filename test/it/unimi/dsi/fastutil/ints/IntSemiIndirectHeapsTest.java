package it.unimi.dsi.fastutil.ints;

import org.junit.Test;

import it.unimi.dsi.fastutil.ints.IntSemiIndirectHeaps;
import static org.junit.Assert.*;


public class IntSemiIndirectHeapsTest {
	
	@Test
	public void testFront() {
		final int numBits = 20;
		int[] refArray = new int[ 100 ], heap = new int[ 100 ], front = new int[ 100 ];

		for( int i = ( 1 << numBits ) - 1; i-- != 0; ) {
			for( int j = 0; j < numBits; j++ ) {
				refArray[ j ] = ( i & ( 1 << j ) );
				heap[ j ] = j;
			}

			IntSemiIndirectHeaps.makeHeap( refArray, heap, numBits, null );
			assertEquals( "Heap " + Integer.toBinaryString( i ), numBits - Integer.bitCount( i ), IntSemiIndirectHeaps.front( refArray, heap, numBits, front ) );
		}
	}
}

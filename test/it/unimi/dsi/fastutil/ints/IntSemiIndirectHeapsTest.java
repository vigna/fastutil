package it.unimi.dsi.fastutil.ints;

import java.util.Arrays;

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

	@Test
	public void testFrontWithComparator() {
		final int[] refArray = { 8, 16, 9 };
		final int[] heap = { 2, 1, 0 };

		IntComparator comparator = new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return ( k1 & 3 ) - ( k2 & 3 );
			}
		};
		IntSemiIndirectHeaps.makeHeap( refArray, heap, 3, comparator );
		final int[] front = new int[ 2 ];
		assertEquals( 2, IntSemiIndirectHeaps.front( refArray, heap, 3, front, comparator ) );
		Arrays.sort( front );
		assertArrayEquals( new int[] { 0, 1 }, front );
	}
}

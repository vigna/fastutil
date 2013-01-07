package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

public class IntHeapSemiIndirectPriorityQueueTest extends TestCase {

	public void testTops() {
		int refArray[] = { 4, 3, 2, 1, 0, 3, 2, 1, 0, 2, 1, 0, 1, 0, 0 };
		int tops[] = new int[ refArray.length ];
		final IntHeapSemiIndirectPriorityQueue queue = new IntHeapSemiIndirectPriorityQueue( refArray );
		for( int i = refArray.length; i-- != 0; ) queue.enqueue( i );

		assertEquals( 5, queue.front( tops ) );
		assertEquals( new IntOpenHashSet( new int[] { 4, 8, 11, 13, 14 } ), new IntOpenHashSet( tops, 0, 5 ) );
		for( int i = 4; i-- != 0; ) {
			queue.dequeue();
			assertEquals( i + 1, queue.front( tops ) );
		}
		queue.dequeue();

		assertEquals( 4, queue.front( tops ) );
		assertEquals( new IntOpenHashSet( new int[] { 3, 7, 10, 12 } ), new IntOpenHashSet( tops, 0, 4 ) );
		for( int i = 3; i-- != 0; ) {
			queue.dequeue();
			assertEquals( i + 1, queue.front( tops ) );
		}
		queue.dequeue();

		assertEquals( 3, queue.front( tops ) );
		assertEquals( new IntOpenHashSet( new int[] { 2, 6, 9 } ), new IntOpenHashSet( tops, 0, 3 ) );
		for( int i = 2; i-- != 0; ) {
			queue.dequeue();
			assertEquals( i + 1, queue.front( tops ) );
		}
		queue.dequeue();

		assertEquals( 2, queue.front( tops ) );
		assertEquals( new IntOpenHashSet( new int[] { 1, 5 } ), new IntOpenHashSet( tops, 0, 2 ) );
		queue.dequeue();
		assertEquals( 1, queue.front( tops ) );
		queue.dequeue();

		assertEquals( 1, queue.front( tops ) );	
	}
	
	@Test
	public void testFrontWithComparator() {
		final int[] refArray = { 8, 16, 9 };

		IntComparator comparator = new AbstractIntComparator() {
			@Override
			public int compare( int k1, int k2 ) {
				return ( k1 & 3 ) - ( k2 & 3 );
			}
		};

		IntHeapSemiIndirectPriorityQueue queue = new IntHeapSemiIndirectPriorityQueue( refArray, comparator );
		queue.enqueue( 0 );
		queue.enqueue( 1 );
		queue.enqueue( 2 );
		final int[] front = new int[ 2 ];
		assertEquals( 2, queue.front( front ) );
		Arrays.sort( front );
		assertArrayEquals( new int[] { 0, 1 }, front );
	}
	
}

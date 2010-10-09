package test.it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntArrayIndirectPriorityQueue;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import junit.framework.TestCase;

public class IntArrayIndirectPriorityQueueTest extends TestCase {

	public void testFront() {
		int refArray[] = { 4, 3, 2, 1, 0, 3, 2, 1, 0, 2, 1, 0, 1, 0, 0 };
		int tops[] = new int[ refArray.length ];
		final IntArrayIndirectPriorityQueue queue = new IntArrayIndirectPriorityQueue( refArray );
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
	
	
}

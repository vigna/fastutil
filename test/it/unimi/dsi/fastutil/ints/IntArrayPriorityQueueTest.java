package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntArrayPriorityQueueTest {
	
	@Test
	public void testEnqueueDequeue() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		IntHeapPriorityQueue h = new IntHeapPriorityQueue();
		for( int i = 0; i < 100; i++ ) {
			q.enqueue( i );
			h.enqueue( i );
		}
		for( int i = 0; i < 100; i++ ) {
			assertEquals( h.first(), q.first() );
			assertEquals( h.dequeue(), q.dequeue() );
		}

		q = new IntArrayPriorityQueue( 10 );
		h.clear();
		for( int i = 0; i < 100; i++ ) {
			q.enqueue( i );
			h.enqueue( i );
		}
		for( int i = 0; i < 100; i++ ) {
			assertEquals( h.first(), q.first() );
			assertEquals( h.dequeue(), q.dequeue() );
		}

		q = new IntArrayPriorityQueue( 200 );
		h.clear();
		for( int i = 0; i < 100; i++ ) {
			q.enqueue( i );
			h.enqueue( i );
		}
		for( int i = 0; i < 100; i++ ) {
			assertEquals( h.first(), q.first() );
			assertEquals( h.dequeue(), q.dequeue() );
		}
	}


	@Test
	public void testEnqueueDequeueComp() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue( IntComparators.OPPOSITE_COMPARATOR );
		IntHeapPriorityQueue h = new IntHeapPriorityQueue( IntComparators.OPPOSITE_COMPARATOR );
		for( int i = 0; i < 100; i++ ) {
			q.enqueue( i );
			h.enqueue( i );
		}
		for( int i = 0; i < 100; i++ ) {
			assertEquals( h.first(), q.first() );
			assertEquals( h.dequeue(), q.dequeue() );
		}

		q = new IntArrayPriorityQueue( 10, IntComparators.OPPOSITE_COMPARATOR );
		h.clear();
		for( int i = 0; i < 100; i++ ) {
			q.enqueue( i );
			h.enqueue( i );
		}
		for( int i = 0; i < 100; i++ ) {
			assertEquals( h.first(), q.first() );
			assertEquals( h.dequeue(), q.dequeue() );
		}

		q = new IntArrayPriorityQueue( 200, IntComparators.OPPOSITE_COMPARATOR );
		h.clear();
		for( int i = 0; i < 100; i++ ) {
			q.enqueue( i );
			h.enqueue( i );
		}
		for( int i = 0; i < 100; i++ ) {
			assertEquals( h.first(), q.first() );
			assertEquals( h.dequeue(), q.dequeue() );
		}
	}
	
	@Test
	public void testMix() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue();
		IntHeapPriorityQueue h = new IntHeapPriorityQueue();
		for( int i = 0; i < 200; i++ ) {
			for( int j = 0; j < 20; j++ ) {
				q.enqueue( j + i * 20 );
				h.enqueue( j + i * 20 );
			}
			for( int j = 0; j < 10; j++ ) assertEquals( h.dequeueInt(), q.dequeueInt() );
		}
		
		q = new IntArrayPriorityQueue( 10 );
		h = new IntHeapPriorityQueue();
		for( int i = 0; i < 200; i++ ) {
			for( int j = 0; j < 20; j++ ) {
				q.enqueue( j + i * -20 );
				h.enqueue( j + i * -20 );
				q.first();
			}
			for( int j = 0; j < 10; j++ ) assertEquals( h.dequeueInt(), q.dequeueInt() );
		}

		q = new IntArrayPriorityQueue( 200 );
		h = new IntHeapPriorityQueue();
		for( int i = 0; i < 200; i++ ) {
			for( int j = 0; j < 20; j++ ) {
				q.enqueue( j + i * 20 );
				h.enqueue( j + i * 20 );
			}
			for( int j = 0; j < 10; j++ ) assertEquals( h.dequeueInt(), q.dequeueInt() );
		}
	}

	@Test
	public void testMixComp() {
		IntArrayPriorityQueue q = new IntArrayPriorityQueue( IntComparators.OPPOSITE_COMPARATOR );
		IntHeapPriorityQueue h = new IntHeapPriorityQueue( IntComparators.OPPOSITE_COMPARATOR );
		for( int i = 0; i < 200; i++ ) {
			for( int j = 0; j < 20; j++ ) {
				q.enqueue( j + i * 20 );
				h.enqueue( j + i * 20 );
			}
			for( int j = 0; j < 10; j++ ) assertEquals( h.dequeueInt(), q.dequeueInt() );
		}
		
		q = new IntArrayPriorityQueue( 10, IntComparators.OPPOSITE_COMPARATOR );
		h = new IntHeapPriorityQueue( IntComparators.OPPOSITE_COMPARATOR );
		for( int i = 0; i < 200; i++ ) {
			for( int j = 0; j < 20; j++ ) {
				q.enqueue( j + i * -20 );
				h.enqueue( j + i * -20 );
				q.first();
			}
			for( int j = 0; j < 10; j++ ) assertEquals( h.dequeueInt(), q.dequeueInt() );
		}

		q = new IntArrayPriorityQueue( 200, IntComparators.OPPOSITE_COMPARATOR );
		h = new IntHeapPriorityQueue( IntComparators.OPPOSITE_COMPARATOR );
		for( int i = 0; i < 200; i++ ) {
			for( int j = 0; j < 20; j++ ) {
				q.enqueue( j + i * 20 );
				h.enqueue( j + i * 20 );
			}
			for( int j = 0; j < 10; j++ ) assertEquals( h.dequeueInt(), q.dequeueInt() );
		}
	}
}

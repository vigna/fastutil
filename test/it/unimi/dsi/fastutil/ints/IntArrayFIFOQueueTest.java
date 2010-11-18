package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntArrayFIFOQueueTest {
	
	@Test
	public void testEnqueueDequeue() {
		IntArrayFIFOQueue q = new IntArrayFIFOQueue();
		for( int i = 0; i < 100; i++ ) q.enqueue( i );
		for( int i = 0; i < 100; i++ ) {
			assertEquals( i, q.firstInt() );
			assertEquals( i, q.dequeueInt() );
			if ( i != 99 ) assertEquals( 99, q.lastInt() );
		}

		q = new IntArrayFIFOQueue( 10 );
		for( int i = 0; i < 100; i++ ) q.enqueue( i );
		for( int i = 0; i < 100; i++ ) {
			assertEquals( i, q.firstInt() );
			assertEquals( i, q.dequeueInt() );
			if ( i != 99 ) assertEquals( 99, q.lastInt() );
		}

		q = new IntArrayFIFOQueue( 200 );
		for( int i = 0; i < 100; i++ ) q.enqueue( i );
		for( int i = 0; i < 100; i++ ) {
			assertEquals( i, q.firstInt() );
			assertEquals( i, q.dequeueInt() );
			if ( i != 99 ) assertEquals( 99, q.lastInt() );
		}
}
}

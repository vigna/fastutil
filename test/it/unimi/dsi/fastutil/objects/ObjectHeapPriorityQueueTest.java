package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import it.unimi.dsi.fastutil.io.BinIO;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ObjectHeapPriorityQueueTest {
	
	@SuppressWarnings({ "unchecked", "boxing" })
	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		ObjectHeapPriorityQueue<Integer> q = new ObjectHeapPriorityQueue<Integer>();
		for( int i = 0; i < 100; i++ ) q.enqueue( i );
		
		File file = File.createTempFile( getClass().getPackage().getName() + "-", "-tmp" );
		file.deleteOnExit();
		BinIO.storeObject( q, file );
		ObjectHeapPriorityQueue<Integer> r = (ObjectHeapPriorityQueue<Integer>)BinIO.loadObject( file );
		file.delete();
		for( int i = 0; i < 100; i++ ) {
			assertEquals( q.first(), r.first() );
			assertEquals( q.dequeue(), r.dequeue() );
		}
	}
}


package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectArrayListTest {

	@SuppressWarnings("boxing")
	@Test
	public void testRemoveAll() {
		ObjectArrayList<Integer> l = ObjectArrayList.wrap( new Integer[] { 0, 1, 1, 2 } );
		l.removeAll( ObjectSets.singleton( 1 ) );
		assertEquals( ObjectArrayList.wrap( new Integer[] { 0, 2 } ), l );
		assertTrue( l.elements()[ 2 ] == null );
		assertTrue( l.elements()[ 3 ] == null );
	}
}

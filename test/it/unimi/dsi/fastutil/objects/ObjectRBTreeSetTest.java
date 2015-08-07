package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ObjectRBTreeSetTest {

	@Test
	public void testGet() {
		ObjectRBTreeSet<Integer> s = new ObjectRBTreeSet<Integer>();
		Integer o = new Integer( 0 );
		s.add( o );
		assertSame( o,  s.get( new Integer( 0 ) ) );
	}
}

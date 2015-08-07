package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ObjectAVLTreeSetTest {

	@Test
	public void testGet() {
		ObjectAVLTreeSet<Integer> s = new ObjectAVLTreeSet<Integer>();
		Integer o = new Integer( 0 );
		s.add( o );
		assertSame( o,  s.get( new Integer( 0 ) ) );
	}
}

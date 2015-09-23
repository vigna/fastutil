package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;

import org.junit.Test;

public class ObjectOpenCustomHashSetTest {

	@Test
	public void testGetNullKey() {
		final ObjectOpenCustomHashSet<Integer> s = new ObjectOpenCustomHashSet<Integer>( new Hash.Strategy<Integer>() {

			@Override
			public int hashCode( Integer o ) {
				return o == null ? 0 : o.intValue() % 10;
			}

			@Override
			public boolean equals( Integer a, Integer b ) {
				if ( ( ( a == null ) != ( b == null ) ) || a == null ) return false;
				return ( a.intValue() - b.intValue() % 10 ) == 0;
			}
		});
		
		s.add( Integer.valueOf( 10 ) );
		assertTrue( s.contains( Integer.valueOf( 0 ) ) );
		assertEquals( 10, s.iterator().next().intValue() );
	}


}

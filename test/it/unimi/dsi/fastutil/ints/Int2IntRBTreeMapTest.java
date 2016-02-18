package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import org.junit.Test;

public class Int2IntRBTreeMapTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testContainsNull() {
		Int2IntRBTreeMap m = new Int2IntRBTreeMap( new int[] { 1, 2, 3 },  new int[] { 1, 2, 3 } );
		assertFalse( m.containsKey( null ) );
		assertTrue( m.get( null ) == null );
	}

	@SuppressWarnings("boxing")
	@Test
	public void testEquals() {
		Int2IntRBTreeMap m = new Int2IntRBTreeMap( new int[] { 1, 2 },  new int[] { 1, 2 } );
		assertFalse( m.equals( new Object2ObjectOpenHashMap<Integer,Integer>( new Integer[] { 1, null }, new Integer[] { 1, 1 } ) ) );
	}	
}

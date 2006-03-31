package test.it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import junit.framework.TestCase;

public class IntArraySetTest extends TestCase {
	
	public void testSet() {
		IntArraySet s = new IntArraySet( new int[ 5 ] );
		assertTrue( s.add( 1 ) );
		assertEquals( 1, s.size() );
		assertTrue( s.contains( 1 ) );
		assertTrue( s.add(  2  ) );
		assertTrue( s.contains( 2 ) );
		assertEquals( 2, s.size() );
		assertFalse( s.add( 1 ) );
		assertFalse( s.remove( 3 ) );
		assertEquals( new IntOpenHashSet( new int[] { 1, 2 } ), new IntOpenHashSet( s.iterator() ) );
		assertEquals( 2, s.size() );
		assertTrue( s.remove( 1 ) );
		assertEquals( 1, s.size() );
		assertFalse( s.contains( 1 ) );
		assertTrue( s.remove( 2 ) );
		assertEquals( 0, s.size() );
		assertFalse( s.contains( 1 ) );
	}

}

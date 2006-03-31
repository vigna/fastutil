package test.it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import junit.framework.TestCase;

public class IntArraySetTest extends TestCase {
	
	public void testSet() {
		for( int i = 0; i <= 2; i++ ) {
			final IntArraySet s = i == 0 ? new IntArraySet() : new IntArraySet( new int[ i ] );
			assertTrue( s.add( 1 ) );
			assertEquals( 1, s.size() );
			assertTrue( s.contains( 1 ) );
			assertTrue( s.add(  2  ) );
			assertTrue( s.contains( 2 ) );
			assertEquals( 2, s.size() );
			assertFalse( s.add( 1 ) );
			assertFalse( s.remove( 3 ) );
			assertTrue( s.add( 3 ) );
			assertEquals( 3, s.size() );
			assertTrue( s.contains( 1 ) );
			assertTrue( s.contains( 2 ) );
			assertTrue( s.contains( 2 ) );
			assertEquals( new IntOpenHashSet( new int[] { 1, 2, 3 } ), new IntOpenHashSet( s.iterator() ) );
			assertTrue( s.remove( 3 ) );
			assertEquals( 2, s.size() );
			assertTrue( s.remove( 1 ) );
			assertEquals( 1, s.size() );
			assertFalse( s.contains( 1 ) );
			assertTrue( s.remove( 2 ) );
			assertEquals( 0, s.size() );
			assertFalse( s.contains( 1 ) );
		}
	}
}
